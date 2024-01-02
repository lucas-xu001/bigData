package com.atguigu.etl.entity;

public class Product {
  private Long id;
  
  private String name;
  
  private Double price;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Double getPrice() {
    return this.price;
  }
  
  public void setPrice(Double price) {
    this.price = price;
  }
  
  public String toString() {
    return "Product{id=" + this.id + ", name='" + this.name + '\'' + ", price=" + this.price + '}';
  }
}
