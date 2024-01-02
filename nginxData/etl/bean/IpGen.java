package com.atguigu.etl.bean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class IpGen {
  ArrayList<IpRange> ipRanges;
  
  Random random;
  
  public IpGen() {
    InputStream ipTable = getClass().getClassLoader().getResourceAsStream("ip_table");
    assert ipTable != null;
    Scanner scanner = new Scanner(ipTable);
    this.ipRanges = new ArrayList<>();
    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      String[] split = s.split("\t");
      this.ipRanges.add(new IpRange(split[0], split[1]));
    } 
    this.random = new Random();
  }
  
  private String num2ip(long num) {
    return String.format("%s.%s.%s.%s", new Object[] { Long.valueOf((num & 0xFFFFFFFFFF000000L) >> 24L), 
          Long.valueOf((num & 0xFF0000L) >> 16L), 
          Long.valueOf((num & 0xFF00L) >> 8L), 
          Long.valueOf(num & 0xFFL) });
  }
  
  private String genIp(long start, long end) {
    boolean flag = true;
    String ip = null;
    while (flag) {
      int bound = Math.toIntExact(end - start + 1L);
      long test = start + this.random.nextInt(bound);
      ip = num2ip(test);
      if (!ip.endsWith(".0"))
        flag = false; 
    } 
    return ip;
  }
  
  public String genOne() {
    IpRange ipRange = this.ipRanges.get(this.random.nextInt(this.ipRanges.size()));
    return genIp(ipRange.getIpStart(), ipRange.getIpEnd());
  }
  
  private static class IpRange {
    private String ipStart;
    
    private String ipEnd;
    
    private long ip2num(String ip) {
      String[] split = ip.split("\\.");
      LinkedList<Long> longs = new LinkedList<>();
      for (String s : split)
        longs.add(Long.valueOf(Long.parseLong(s))); 
      return (((Long)longs.get(0)).longValue() << 24L) + (((Long)longs.get(1)).longValue() << 16L) + (((Long)longs.get(2)).longValue() << 8L) + ((Long)longs.get(3)).longValue();
    }
    
    public IpRange(String ipStart, String ipEnd) {
      this.ipStart = ipStart;
      this.ipEnd = ipEnd;
    }
    
    public long getIpStart() {
      return ip2num(this.ipStart);
    }
    
    public long getIpEnd() {
      return ip2num(this.ipEnd);
    }
    
    public String toString() {
      return "IpRange{IpStart='" + this.ipStart + '\'' + ", IpEnd='" + this.ipEnd + '\'' + '}';
    }
  }
}
