package com.root.etl.behaviors;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.root.etl.bean.AdsPlatformInfoBridge;
import com.root.etl.bean.IpGen;
import com.root.etl.entity.Ads;
import com.root.etl.entity.AdsPlatform;
import com.root.etl.entity.PlatformInfo;
import com.root.etl.entity.ServerHost;
import com.root.etl.loader.ConfigLoader;
import com.root.etl.loader.DBLoader;
import com.root.etl.loader.ResourceLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BehaviorTool {
  public static WeightRandom<Integer> NormalRequestTimes = WeightRandom.create()
    .add(Integer.valueOf(1), 0.8D)
    .add(Integer.valueOf(2), 0.15D)
    .add(Integer.valueOf(3), 0.05D);
  
  public static WeightRandom<String> OSTypeWeight = WeightRandom.create()
    .add("android", 0.7D)
    .add("ios", 0.3D);
  
  public static List<AdsPlatformInfoBridge> adsPlatformInfoBridges;
  
  public static IpGen ipGen = new IpGen();
  
  public static String popOneIpv4() {
    return ipGen.genOne();
  }
  
  public static String popOneDeviceId() {
    return UUID.fastUUID().toString(true);
  }
  
  public static String popOneNormalUa() {
    return (String)RandomUtil.randomEle(ResourceLoader.NORMAL_USER_AGENTS);
  }
  
  public static String popOneHostIp() {
    return ((ServerHost)RandomUtil.randomEle(DBLoader.OneTable.SERVER_HOST.getArrayList(ServerHost.class))).getIpv4();
  }
  
  public static String popOneOSType() {
    return (String)OSTypeWeight.next();
  }
  
  public static String popOneBotUa() {
    return (String)RandomUtil.randomEle(ResourceLoader.BOT_USER_AGENTS);
  }
  
  public static AdsPlatformInfoBridge popOneAdsPlatformInfoBridge() {
    if (adsPlatformInfoBridges == null)
      synchronized (BehaviorTool.class) {
        if (adsPlatformInfoBridges == null) {
          List<AdsPlatformInfoBridge> list = new ArrayList<>();
          ArrayList<AdsPlatform> adsPlatforms = DBLoader.OneTable.ADS_PLATFORM.getArrayList(AdsPlatform.class);
          Map<Long, Ads> indexedAds = new HashMap<>();
          ArrayList<Ads> adsList = DBLoader.OneTable.ADS.getArrayList(Ads.class);
          for (Ads ads : adsList)
            indexedAds.put(ads.getId(), ads); 
          ArrayList<PlatformInfo> platformInfos = DBLoader.OneTable.PLATFORM_INFO.getArrayList(PlatformInfo.class);
          Map<Long, PlatformInfo> indexedPlatformInfo = new HashMap<>();
          for (PlatformInfo platformInfo : platformInfos)
            indexedPlatformInfo.put(platformInfo.getId(), platformInfo); 
          for (AdsPlatform adsPlatform : adsPlatforms) {
            AdsPlatformInfoBridge adsPlatformInfoBridge = new AdsPlatformInfoBridge(indexedAds.get(adsPlatform.getAdId()), indexedPlatformInfo.get(adsPlatform.getPlatformId()));
            list.add(adsPlatformInfoBridge);
          } 
          adsPlatformInfoBridges = list;
        } 
      }  
    return (AdsPlatformInfoBridge)RandomUtil.randomEle(adsPlatformInfoBridges);
  }
  
  public static List<Tuple> popNormalTimeSeries() {
    RandomUtil.randomInt(3);
    Integer times = (Integer)NormalRequestTimes.next();
    LinkedList<Tuple> timeSeries = new LinkedList<>();
    for (int i = 0; i < times.intValue(); i++) {
      long eventTime = RandomUtil.randomLong(ConfigLoader.getConfig().getStartTimeTs().longValue(), ConfigLoader.getConfig().getEndTimeTs().longValue());
      long serverTime = eventTime + RandomUtil.randomLong(50L, 3000L);
      if (serverTime >= ConfigLoader.getConfig().getEndTimeTs().longValue()) {
        serverTime = ConfigLoader.getConfig().getEndTimeTs().longValue() - 1L;
        if (eventTime < serverTime)
          eventTime = serverTime; 
      } 
      timeSeries.add(new Tuple(new Object[] { LocalDateTimeUtil.of(serverTime).toString(), 
              Long.toString(eventTime) }));
    } 
    return timeSeries;
  }
  
  public static List<Tuple> popFastTimesSeries() {
    long initTs = RandomUtil.randomLong(ConfigLoader.getConfig().getStartTimeTs().longValue(), ConfigLoader.getConfig().getEndTimeTs().longValue() - 420000L);
    int duration = RandomUtil.randomInt(5, 8);
    int times = RandomUtil.randomInt(100, 150) * duration;
    LinkedList<Tuple> timeSeries = new LinkedList<>();
    for (int i = 0; i < times; i++) {
      long eventTime = RandomUtil.randomLong(initTs, initTs + duration * 60000L);
      long serverTime = eventTime + RandomUtil.randomLong(50L, 3000L);
      timeSeries.add(new Tuple(new Object[] { LocalDateTimeUtil.of(serverTime).toString(), 
              Long.toString(eventTime) }));
    } 
    return timeSeries;
  }
  
  public static String popOneClickOrImpressionSeries() {
    return (String)RandomUtil.randomEle((Object[])new String[] { "click", "impression" });
  }
  
  public static List<String> popNormalClickOrImpressionSeries() {
    List<String> linkedList = new LinkedList<>();
    linkedList.add("impression");
    if (RandomUtil.randomDouble(0.0D, 1.0D) < 0.3D)
      linkedList.add("click"); 
    return linkedList;
  }
  
  public static List<Tuple> popCycleTimeSeries() {
    long initTs = RandomUtil.randomLong(ConfigLoader.getConfig().getStartTimeTs().longValue(), ConfigLoader.getConfig().getEndTimeTs().longValue());
    List<Tuple> timeSeries = new LinkedList<>();
    long step = RandomUtil.randomLong(30000L, 300000L);
    boolean flag = true;
    long currentEventTime = initTs;
    long times = RandomUtil.randomLong(200L, 500L);
    for (int i = 0; i < times; i++) {
      long serverTime = currentEventTime + RandomUtil.randomLong(50L, 3000L);
      if (serverTime > ConfigLoader.getConfig().getEndTimeTs().longValue() && 
        timeSeries.size() == 0) {
        timeSeries.add(new Tuple(new Object[] { LocalDateTimeUtil.of(currentEventTime).toString(), 
                Long.toString(currentEventTime) }));
        break;
      } 
      if (serverTime >= ConfigLoader.getConfig().getEndTimeTs().longValue()) {
        serverTime = ConfigLoader.getConfig().getEndTimeTs().longValue() - 1L;
        if (currentEventTime > serverTime)
          currentEventTime = serverTime; 
        timeSeries.add(new Tuple(new Object[] { LocalDateTimeUtil.of(serverTime).toString(), 
                Long.toString(currentEventTime) }));
        break;
      } 
      timeSeries.add(new Tuple(new Object[] { LocalDateTimeUtil.of(serverTime).toString(), 
              Long.toString(currentEventTime) }));
      currentEventTime += step;
    } 
    return timeSeries;
  }
}
