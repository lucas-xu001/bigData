package com.atguigu.etl.bean;

import com.atguigu.etl.entity.Ads;
import com.atguigu.etl.entity.PlatformInfo;

public class AdsPlatformInfoBridge {
  private Ads ads;
  
  private PlatformInfo platformInfo;
  
  public AdsPlatformInfoBridge(Ads ads, PlatformInfo platformInfos) {
    this.ads = ads;
    this.platformInfo = platformInfos;
  }
  
  public Ads getAds() {
    return this.ads;
  }
  
  public void setAds(Ads ads) {
    this.ads = ads;
  }
  
  public PlatformInfo getPlatformInfo() {
    return this.platformInfo;
  }
  
  public void setPlatformInfos(PlatformInfo platformInfos) {
    this.platformInfo = platformInfos;
  }
}
