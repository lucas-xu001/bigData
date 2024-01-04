package com.root.etl.bean;

import cn.hutool.core.date.LocalDateTimeUtil;
import java.time.ZoneOffset;

public class Config {
  private String fileName;
  
  private String jdbcUrl;
  
  private String user;
  
  private String password;
  
  private String database;
  
  private String driver;
  
  private String targetPath;
  
  private String startTime;
  
  private Long startTimeTs;
  
  private String endTime;
  
  private Long endTimeTs;
  
  private Integer baseDataNum;
  
  private Integer threadNum;
  
  private String logLevel;
  
  private Double cycleBrowser;
  
  private Double cycleDevice;
  
  private Double fastFixedIpBrowser;
  
  private Double fastFixedIdDevice;
  
  private Double normalBrowser;
  
  private Double normalDevice;
  
  private Double botBrowser;
  
  private Boolean whereDataFrom;
  
  public String getFileName() {
    return this.fileName;
  }
  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public String getJdbcUrl() {
    return this.jdbcUrl;
  }
  
  public void setJdbcUrl(String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public void setUser(String user) {
    this.user = user;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getDatabase() {
    return this.database;
  }
  
  public void setDatabase(String database) {
    this.database = database;
  }
  
  public String getDriver() {
    return this.driver;
  }
  
  public void setDriver(String driver) {
    this.driver = driver;
  }
  
  public String getTargetPath() {
    return this.targetPath;
  }
  
  public void setTargetPath(String targetPath) {
    this.targetPath = targetPath;
  }
  
  public String getStartTime() {
    return this.startTime;
  }
  
  public void setStartTime(String startTime) {
    this.startTime = startTime;
    this.startTimeTs = Long.valueOf(LocalDateTimeUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss").toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
  }
  
  public String getEndTime() {
    return this.endTime;
  }
  
  public void setEndTime(String endTime) {
    this.endTime = endTime;
    this.endTimeTs = Long.valueOf(LocalDateTimeUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss").toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
  }
  
  public Integer getBaseDataNum() {
    return this.baseDataNum;
  }
  
  public void setBaseDataNum(Integer baseDataNum) {
    this.baseDataNum = baseDataNum;
  }
  
  public Integer getThreadNum() {
    return this.threadNum;
  }
  
  public void setThreadNum(Integer threadNum) {
    this.threadNum = threadNum;
  }
  
  public Long getStartTimeTs() {
    return this.startTimeTs;
  }
  
  public void setStartTimeTs(Long startTimeTs) {
    this.startTimeTs = startTimeTs;
  }
  
  public Long getEndTimeTs() {
    return this.endTimeTs;
  }
  
  public void setEndTimeTs(Long endTimeTs) {
    this.endTimeTs = endTimeTs;
  }
  
  public String getLogLevel() {
    return this.logLevel;
  }
  
  public void setLogLevel(String logLevel) {
    this.logLevel = logLevel;
  }
  
  public Double getCycleBrowser() {
    return this.cycleBrowser;
  }
  
  public void setCycleBrowser(Double cycleBrowser) {
    this.cycleBrowser = cycleBrowser;
  }
  
  public Double getCycleDevice() {
    return this.cycleDevice;
  }
  
  public void setCycleDevice(Double cycleDevice) {
    this.cycleDevice = cycleDevice;
  }
  
  public Double getFastFixedIpBrowser() {
    return this.fastFixedIpBrowser;
  }
  
  public void setFastFixedIpBrowser(Double fastFixedIpBrowser) {
    this.fastFixedIpBrowser = fastFixedIpBrowser;
  }
  
  public Double getFastFixedIdDevice() {
    return this.fastFixedIdDevice;
  }
  
  public void setFastFixedIdDevice(Double fastFixedIdDevice) {
    this.fastFixedIdDevice = fastFixedIdDevice;
  }
  
  public Double getNormalBrowser() {
    return this.normalBrowser;
  }
  
  public void setNormalBrowser(Double normalBrowser) {
    this.normalBrowser = normalBrowser;
  }
  
  public Double getNormalDevice() {
    return this.normalDevice;
  }
  
  public void setNormalDevice(Double normalDevice) {
    this.normalDevice = normalDevice;
  }
  
  public Double getBotBrowser() {
    return this.botBrowser;
  }
  
  public void setBotBrowser(Double botBrowser) {
    this.botBrowser = botBrowser;
  }
  
  public Boolean getWhereDataFrom() {
    return this.whereDataFrom;
  }
  
  public void setWhereDataFrom(Boolean whereDataFrom) {
    this.whereDataFrom = whereDataFrom;
  }
}
