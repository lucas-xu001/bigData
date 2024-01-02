package com.atguigu.etl.entity;

public class Ads {
  private Long id;
  
  private Long productId;
  
  private Long materialId;
  
  private Long groupId;
  
  private String adName;
  
  private String materialUrl;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getProductId() {
    return this.productId;
  }
  
  public void setProductId(Long productId) {
    this.productId = productId;
  }
  
  public Long getMaterialId() {
    return this.materialId;
  }
  
  public void setMaterialId(Long materialId) {
    this.materialId = materialId;
  }
  
  public Long getGroupId() {
    return this.groupId;
  }
  
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }
  
  public String getAdName() {
    return this.adName;
  }
  
  public void setAdName(String adName) {
    this.adName = adName;
  }
  
  public String getMaterialUrl() {
    return this.materialUrl;
  }
  
  public void setMaterialUrl(String materialUrl) {
    this.materialUrl = materialUrl;
  }
  
  public String toString() {
    return "Ads{id=" + this.id + ", productId=" + this.productId + ", materialId=" + this.materialId + ", groupId=" + this.groupId + ", adName='" + this.adName + '\'' + ", materialUrl='" + this.materialUrl + '\'' + '}';
  }
}
