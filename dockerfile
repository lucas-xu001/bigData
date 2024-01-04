**步骤 1: 创建基础 Docker 镜像**

1. 创建一个 `Dockerfile` 文件，内容如下:

```Dockerfile
FROM centos:7.5.1804
LABEL maintainer="yourname@example.com"

# 安装必要的软件包
RUN yum install -y epel-release && \
    yum install -y wget vim net-tools java-1.8.0-openjdk-devel openssh-server openssh-clients rsync && \
    yum clean all

# 创建用户root并设置密码
RUN useradd root && \
    echo "123456" | passwd --stdin root && \
    echo "root ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# SSH设置
RUN ssh-keygen -A && \
    mkdir /var/run/sshd && \
    echo 'root:root' | chpasswd && \
    sed -i 's/^#PermitRootLogin yes/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/^#RSAAuthentication yes/RSAAuthentication yes/' /etc/ssh/sshd_config && \
    sed -i 's/^#PubkeyAuthentication yes/PubkeyAuthentication yes/' /etc/ssh/sshd_config

# 设置SSH免密登录
RUN su - root -c "ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa" && \
    su - root -c "cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys" && \
    su - root -c "chmod 0600 ~/.ssh/authorized_keys"

# 设置工作目录和环境变量
WORKDIR /home/root
ENV JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
ENV PATH=$PATH:$JAVA_HOME/bin

# 配置JDK环境变量
RUN echo "export JAVA_HOME=$JAVA_HOME" >> /etc/profile.d/java.sh && \
    echo "export PATH=$PATH:$JAVA_HOME/bin" >> /etc/profile.d/java.sh

# 暴露22端口用于SSH服务
EXPOSE 22

# 默认启动SSHD服务
CMD ["/usr/sbin/sshd", "-D"]
```

2. 构建基础 Docker 镜像:

```bash
docker build -t centos-hadoop-base .
```

**步骤 2: 创建 Hadoop Docker 镜像**

1. 创建一个新的 `Dockerfile` 用于 Hadoop:

```Dockerfile
FROM centos-hadoop-base
LABEL maintainer="yourname@example.com"

# 将Hadoop压缩包复制到容器中
COPY hadoop-3.3.4.tar.gz /home/root/

# 解压Hadoop，并设置环境变量
RUN tar -zxvf hadoop-3.3.4.tar.gz && \
    mv hadoop-3.3.4 hadoop && \
    chown -R root:root hadoop && \
    echo "export HADOOP_HOME=/home/root/hadoop" >> /etc/profile.d/hadoop.sh && \
    echo "export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin" >> /etc/profile.d/hadoop.sh

# 配置Hadoop环境和SSH免密登录
COPY core-site.xml /home/root/hadoop/etc/hadoop/
COPY hdfs-site.xml /home/root/hadoop/etc/hadoop/
COPY yarn-site.xml /home/root/hadoop/etc/hadoop/
COPY mapred-site.xml /home/root/hadoop/etc/hadoop/
COPY workers /home/root/hadoop/etc/hadoop/
```

2. 构建 Hadoop Docker 镜像:

```bash
docker build -t centos-hadoop .
```

**步骤 3: 创建 Docker Compose 文件**

1. 创建 `docker-compose.yml` 文件，内容如下:

```yaml
version: '3'
services:
  hadoop-master:
    image: centos-hadoop
    container_name: hadoop-master
    hostname: hadoop102
    ports:
      - "9870:9870"
      - "8088:8088"
    environment:
      - HADOOP_HOME=/home/root/hadoop
    command: >
      bash -c "source /etc/profile.d/java.sh &&
               source /etc/profile.d/hadoop.sh &&
               /home/root/hadoop/bin/hdfs namenode -format &&
               /usr/sbin/sshd -D"

  hadoop-slave1:
    image: centos-hadoop
    container_name: hadoop-slave1
    hostname: hadoop103
    environment:
      - HADOOP_HOME=/home/root/hadoop
    command: /usr/sbin/sshd -D

  hadoop-slave2:
    image: centos-hadoop
    container_name: hadoop-slave2
    hostname: hadoop104
    environment:
      - HADOOP_HOME=/home/root/hadoop
    command: /usr/sbin/sshd -D
```

2. 使用以下命令启动集群:

```bash
docker-compose up -d
```

**步骤 4: 配置集群**

1. 登录到 Hadoop Master 容器，并执行集群相关配置:

```bash
docker exec -it hadoop-master bash
```

2. 修改 `/home/root/hadoop/etc/hadoop/workers` 文件，确保其中包含所有 DataNode 的主机名。

3. 使用以下命令启动 Hadoop 集群:

```bash
/home/root/bin/hdp.sh start
```

4. 验证集群是否启动成功:

```bash
jps
```

5. 退出容器。



**步骤 5: 保存 Docker 镜像为 tar 文件**

1. 使用`docker save`命令将创建的Docker镜像保存为tar文件。

    ```bash
    docker save centos-hadoop-base | gzip > centos-hadoop-base.tar.gz
    docker save centos-hadoop | gzip > centos-hadoop.tar.gz
    ```

2. 将`docker-compose.yml`和所有相关的配置文件（如Hadoop配置文件和启动脚本）一起打包为一个tar文件。

    ```bash
    tar -cvf hadoop-docker-cluster.tar.gz docker-compose.yml *.xml start-script.sh centos-hadoop-base.tar.gz centos-hadoop.tar.gz
    ```

**步骤 7: 分发和导入 Docker 集群**

1. 其他人可以通过以下命令解压并导入镜像，然后根据`docker-compose.yml`文件启动Hadoop集群：

    ```bash
    tar -xvf hadoop-docker-cluster.tar.gz
    gunzip < centos-hadoop-base.tar.gz | docker load
    gunzip < centos-hadoop.tar.gz | docker load
    docker-com
