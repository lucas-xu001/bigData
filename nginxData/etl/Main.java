package com.root.etl;

import com.root.etl.loader.ConfigLoader;
import com.root.etl.loader.DBLoader;
import com.root.etl.loader.ResourceLoader;
import com.root.etl.log.MyAppender;
import com.root.etl.producer.Executor;
import java.io.File;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static Logger logger = LoggerFactory.getLogger(Main.class);
  
  public static void main(String[] args) throws InterruptedException, SQLException {
    long startTime = System.currentTimeMillis();
    String currentPath = (new File("")).getAbsolutePath();
    String settingPath = null;
    if (args.length == 0) {
      settingPath = currentPath + "/nginxLogGen.setting";
    } else {
      settingPath = args[0];
    } 
    prepare(settingPath);
    execute(ConfigLoader.getConfig().getThreadNum().intValue());
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    logger.info("+ duration + "ms");
  }
  
  public static void prepare(String settingPath) throws SQLException {
    ConfigLoader.loadConfig(settingPath);
    MyAppender.addAppender();
    DBLoader dbLoader = new DBLoader(ConfigLoader.getConfig().getJdbcUrl(), ConfigLoader.getConfig().getUser(), ConfigLoader.getConfig().getPassword(), ConfigLoader.getConfig().getDriver(), ConfigLoader.getConfig().getDatabase());
    dbLoader.loadAll();
    ResourceLoader resourceLoader = new ResourceLoader();
    resourceLoader.loadNormalUserAgents();
    resourceLoader.loadOtherUserAgents();
  }
  
  public static void execute(int threadNum) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(threadNum);
    List<Executor> executors = new LinkedList<>();
    for (int i = 0; i < threadNum; i++) {
      Executor executor = new Executor(latch, ConfigLoader.getConfig().getBaseDataNum());
      executors.add(executor);
      executor.start();
    } 
    latch.await();
    Long allRowNum = Long.valueOf(0L);
    for (Executor executor : executors)
      allRowNum = Long.valueOf(allRowNum.longValue() + executor.rowNum.longValue()); 
    logger.info("+ allRowNum + ");
  }
}
