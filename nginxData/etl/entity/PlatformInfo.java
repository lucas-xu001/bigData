package com.atguigu.etl.entity;

public class PlatformInfo {
  private Long id;
  
  private String platform;
  
  private String platformAliasZh;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getPlatform() {
    return this.platform;
  }
  
  public void setPlatform(String platform) {
    this.platform = platform;
  }
  
  public String getPlatformAliasZh() {
    return this.platformAliasZh;
  }
  
  public void setPlatformAliasZh(String platformAliasZh) {
    this.platformAliasZh = platformAliasZh;
  }
  
  public String toString() {
    return "PlatformInfo{id=" + this.id + ", platform='" + this.platform + '\'' + ", platformAliasZh='" + this.platformAliasZh + '\'' + '}';
  }
}
