package com.atguigu.datax;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.atguigu.datax.beans.Table;
import com.atguigu.datax.configuration.Configuration;
import com.atguigu.datax.helper.DataxJsonHelper;
import com.atguigu.datax.helper.MysqlHelper;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    if (Configuration.IMPORT_OUT_DIR != null && 
      !Configuration.IMPORT_OUT_DIR.equals("")) {
      MysqlHelper mysqlHelper = new MysqlHelper(Configuration.MYSQL_URL_IMPORT, Configuration.MYSQL_DATABASE_IMPORT, Configuration.MYSQL_TABLES_IMPORT);
      DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
      String migrationType = Configuration.IMPORT_MIGRATION_TYPE;
      Files.createDirectories(Paths.get(Configuration.IMPORT_OUT_DIR, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
      List<Table> tables = mysqlHelper.getTables();
      if (Configuration.IS_SEPERATED_TABLES.equals("1")) {
        for (int i = 0; i < tables.size(); i++) {
          Table table = tables.get(i);
          dataxJsonHelper.setTable(table, i, migrationType);
        } 
        dataxJsonHelper.setColumns(tables.get(0), migrationType);
        FileWriter inputWriter = new FileWriter(Configuration.IMPORT_OUT_DIR + "/" + Configuration.MYSQL_DATABASE_IMPORT + "." + ((Table)tables.get(0)).name() + ".json");
        JSONUtil.toJsonStr((JSON)dataxJsonHelper.getInputConfig(), inputWriter);
        inputWriter.close();
      } else {
        for (Table table : tables) {
          dataxJsonHelper.setTableAndColumns(table, 0, migrationType);
          FileWriter inputWriter = new FileWriter(Configuration.IMPORT_OUT_DIR + "/" + Configuration.MYSQL_DATABASE_IMPORT + "." + table.name() + ".json");
          JSONUtil.toJsonStr((JSON)dataxJsonHelper.getInputConfig(), inputWriter);
          inputWriter.close();
        } 
      } 
    } 
    if (Configuration.EXPORT_OUT_DIR != null && 
      !"".equals(Configuration.EXPORT_OUT_DIR)) {
      MysqlHelper mysqlHelper = new MysqlHelper(Configuration.MYSQL_URL_EXPORT, Configuration.MYSQL_DATABASE_EXPORT, Configuration.MYSQL_TABLES_EXPORT);
      DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
      String migrationType = Configuration.EXPORT_MIGRATION_TYPE;
      Files.createDirectories(Paths.get(Configuration.EXPORT_OUT_DIR, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
      List<Table> tables = mysqlHelper.getTables();
      if (Configuration.IS_SEPERATED_TABLES.equals("1")) {
        for (int i = 0; i < tables.size(); i++) {
          Table table = tables.get(i);
          dataxJsonHelper.setTable(table, i, migrationType);
        } 
        dataxJsonHelper.setColumns(tables.get(0), migrationType);
        FileWriter outputWriter = new FileWriter(Configuration.EXPORT_OUT_DIR + "/" + Configuration.MYSQL_DATABASE_EXPORT + "." + ((Table)tables.get(0)).name() + ".json");
        JSONUtil.toJsonStr((JSON)dataxJsonHelper.getOutputConfig(), outputWriter);
        outputWriter.close();
      } 
      for (Table table : tables) {
        dataxJsonHelper.setTableAndColumns(table, 0, migrationType);
        FileWriter outputWriter = new FileWriter(Configuration.EXPORT_OUT_DIR + "/" + Configuration.MYSQL_DATABASE_EXPORT + "." + table.name() + ".json");
        JSONUtil.toJsonStr((JSON)dataxJsonHelper.getOutputConfig(), outputWriter);
        outputWriter.close();
      } 
    } 
  }
}
