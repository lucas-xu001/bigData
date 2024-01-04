package com.root.datax.helper;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.root.datax.beans.Table;
import com.root.datax.configuration.Configuration;

public class DataxJsonHelper {
  private final JSONObject inputConfig = JSONUtil.parseObj("{\"job\": {\"content\": [{\"reader\": {\"name\": \"mysqlreader\",\"parameter\": {\"column\": [],\"connection\": [{\"jdbcUrl\": [],\"table\": []}],\"password\": \"\",\"splitPk\": \"\",\"username\": \"\"}},\"writer\": {\"name\": \"hdfswriter\",\"parameter\": {\"column\": [],\"compress\": \"gzip\",\"hadoopConfig\":{\n         \"dfs.nameservices\": \"mycluster\",\n         \"dfs.ha.namenodes.mycluster\": \"namenode1,namenode2\",\n         \"dfs.namenode.rpc-address.mycluster.namenode1\": \"hadoop102:8020\",\n         \"dfs.namenode.rpc-address.mycluster.namenode2\": \"hadoop103:8020\",\n         \"dfs.client.failover.proxy.provider.mycluster\": \"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider\"\n },\"fieldDelimiter\": \"\\t\",\"fileName\": \"content\",\"fileType\": \"text\",\"path\": \"${targetdir}\",\"writeMode\": \"truncate\",\"nullFormat\": \"\\\\N\"}}}],\"setting\": {\"speed\": {\"channel\": 1}}}}");
  
  private final JSONObject outputConfig = JSONUtil.parseObj("{\"job\": {\"setting\": {\"speed\": {\"channel\": 1}},\"content\": [{\"reader\": {\"name\": \"hdfsreader\",\"parameter\": {\"path\": \"${exportdir}\",\"hadoopConfig\":{\n         \"dfs.nameservices\": \"mycluster\",\n         \"dfs.ha.namenodes.mycluster\": \"namenode1,namenode2\",\n         \"dfs.namenode.rpc-address.mycluster.namenode1\": \"hadoop102:8020\",\n         \"dfs.namenode.rpc-address.mycluster.namenode2\": \"hadoop103:8020\",\n         \"dfs.client.failover.proxy.provider.mycluster\": \"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider\"\n },\"column\": [\"*\"],\"fileType\": \"text\",\"encoding\": \"UTF-8\",\"fieldDelimiter\": \"\\t\",\"nullFormat\": \"\\\\N\"}},\"writer\": {\"name\": \"mysqlwriter\",\"parameter\": {\"writeMode\": \"replace\",\"username\": \"\",\"password\": \"\",\"column\": [],\"connection\": [{\"jdbcUrl\": \"\",\"table\": []}]}}}]}}");
  
  public DataxJsonHelper() {
    JSONObject mysqlReaderPara = (JSONObject)this.inputConfig.getByPath("job.content[0].reader.parameter", JSONObject.class);
    JSONObject hdfsWriterPara = (JSONObject)this.inputConfig.getByPath("job.content[0].writer.parameter", JSONObject.class);
    JSONObject hdfsReaderPara = (JSONObject)this.outputConfig.getByPath("job.content[0].reader.parameter", JSONObject.class);
    JSONObject mysqlWriterPara = (JSONObject)this.outputConfig.getByPath("job.content[0].writer.parameter", JSONObject.class);
    hdfsReaderPara.set("defaultFS", Configuration.HDFS_URI);
    hdfsWriterPara.set("defaultFS", Configuration.HDFS_URI);
    mysqlReaderPara.set("username", Configuration.MYSQL_USER);
    mysqlWriterPara.set("username", Configuration.MYSQL_USER);
    mysqlReaderPara.set("password", Configuration.MYSQL_PASSWORD);
    mysqlWriterPara.set("password", Configuration.MYSQL_PASSWORD);
    mysqlReaderPara.putByPath("connection[0].jdbcUrl[0]", Configuration.MYSQL_URL_IMPORT);
    mysqlWriterPara.putByPath("connection[0].jdbcUrl", Configuration.MYSQL_URL_EXPORT);
    this.inputConfig.putByPath("job.content[0].reader.parameter", mysqlReaderPara);
    this.inputConfig.putByPath("job.content[0].writer.parameter", hdfsWriterPara);
    this.outputConfig.putByPath("job.content[0].reader.parameter", hdfsReaderPara);
    this.outputConfig.putByPath("job.content[0].writer.parameter", mysqlWriterPara);
  }
  
  public void setTableAndColumns(Table table, int index, String migrationType) {
    setTable(table, index, migrationType);
    setColumns(table, migrationType);
  }
  
  public void setColumns(Table table, String migrationType) {
    if (migrationType.equals("import")) {
      this.inputConfig.putByPath("job.content[0].writer.parameter.fileName", table.name());
      this.inputConfig.putByPath("job.content[0].reader.parameter.column", table.getColumnNames());
      this.inputConfig.putByPath("job.content[0].writer.parameter.column", table.getColumnNamesAndTypes());
    } else {
      this.outputConfig.putByPath("job.content[0].writer.parameter.column", table.getColumnNames());
    } 
  }
  
  public void setTable(Table table, int index, String migrationType) {
    if (migrationType.equals("import")) {
      this.inputConfig.putByPath("job.content[0].reader.parameter.connection[0].table[" + index + "]", table.name());
    } else {
      this.outputConfig.putByPath("job.content[0].writer.parameter.connection[0].table[" + index + "]", table.name());
    } 
  }
  
  public JSONObject getInputConfig() {
    return this.inputConfig;
  }
  
  public JSONObject getOutputConfig() {
    return this.outputConfig;
  }
}
