#!/bin/bash
case $1 in
"start"){
    for i in hadoop102 hadoop103 hadoop104
    do
            echo -------- zookeeper $i start --------
        ssh $i "/opt/software/zookeeper/bin/zkServer.sh start"
    done
};;
"stop"){
    for i in hadoop102 hadoop103 hadoop104
    do
            echo -------- zookeeper $i stop --------
        ssh $i "/opt/software/zookeeper/bin/zkServer.sh stop"
    done
};;
"status"){
    for i in hadoop102 hadoop103 hadoop104
    do
            echo -------- zookeeper $i status --------
        ssh $i "/opt/software/zookeeper/bin/zkServer.sh status"
    done
};;
esac
