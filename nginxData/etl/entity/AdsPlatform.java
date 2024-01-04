package com.root.etl.entity;

public class AdsPlatform {
  private Long id;
  
  private Long adId;
  
  private Long platformId;
  
  private String createTime;
  
  private String cancelTime;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getAdId() {
    return this.adId;
  }
  
  public void setAdId(Long adId) {
    this.adId = adId;
  }
  
  public Long getPlatformId() {
    return this.platformId;
  }
  
  public void setPlatformId(Long platformId) {
    this.platformId = platformId;
  }
  
  public String getCreateTime() {
    return this.createTime;
  }
  
  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }
  
  public String getCancelTime() {
    return this.cancelTime;
  }
  
  public void setCancelTime(String cancelTime) {
    this.cancelTime = cancelTime;
  }
  
  public String toString() {
    return "AdsPlatform{id=" + this.id + ", adId=" + this.adId + ", platformId=" + this.platformId + ", createTime='" + this.createTime + '\'' + ", cancelTime='" + this.cancelTime + '\'' + '}';
  }
}
