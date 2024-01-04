package com.root.etl.behaviors.impl;

import cn.hutool.core.lang.Tuple;
import com.root.etl.behaviors.BehaviorTool;
import com.root.etl.behaviors.RequestBehavior;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastRequestAdFixedDeviceFixedBehavior extends RequestBehavior {
  public static Logger logger = LoggerFactory.getLogger(FastRequestAdFixedDeviceFixedBehavior.class);
  
  public Long run() {
    List<Tuple> times = BehaviorTool.popFastTimesSeries();
    setUserIp(BehaviorTool.popOneIpv4());
    setServerIp(BehaviorTool.popOneHostIp());
    doSetAdIdAndPlatformInfo();
    setUa(BehaviorTool.popOneNormalUa());
    setClickOrImpression(BehaviorTool.popOneClickOrImpressionSeries());
    Long num = Long.valueOf(0L);
    setDeviceId(BehaviorTool.popOneDeviceId());
    setOsType(BehaviorTool.popOneOSType());
    for (Tuple time : times) {
      setServerTime((String)time.get(0));
      setEventTime((String)time.get(1));
      logger.info(output());
      Long long_1 = num, long_2 = num = Long.valueOf(num.longValue() + 1L);
    } 
    return num;
  }
}
