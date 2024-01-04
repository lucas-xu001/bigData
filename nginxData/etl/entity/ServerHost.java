package com.root.etl.entity;

public class ServerHost {
  private int id;
  
  private String ipv4;
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getIpv4() {
    return this.ipv4;
  }
  
  public void setIpv4(String ipv4) {
    this.ipv4 = ipv4;
  }
  
  public String toString() {
    return "ServerHost{id=" + this.id + ", ipv4='" + this.ipv4 + '\'' + '}';
  }
}
