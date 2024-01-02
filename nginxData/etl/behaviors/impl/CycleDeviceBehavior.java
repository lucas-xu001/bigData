package com.atguigu.etl.behaviors.impl;

import cn.hutool.core.lang.Tuple;
import com.atguigu.etl.behaviors.BehaviorTool;
import com.atguigu.etl.behaviors.RequestBehavior;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CycleDeviceBehavior extends RequestBehavior {
  private static Logger logger = LoggerFactory.getLogger(CycleDeviceBehavior.class);
  
  public Long run() {
    List<Tuple> times = BehaviorTool.popCycleTimeSeries();
    setServerIp(BehaviorTool.popOneHostIp());
    setClickOrImpression(BehaviorTool.popOneClickOrImpressionSeries());
    setDeviceId(BehaviorTool.popOneDeviceId());
    setOsType(BehaviorTool.popOneOSType());
    doSetAdIdAndPlatformInfo();
    setUa(BehaviorTool.popOneNormalUa());
    for (Tuple time : times) {
      setUserIp(BehaviorTool.popOneIpv4());
      setServerTime((String)time.get(0));
      setEventTime((String)time.get(1));
      logger.info(output());
      setRowNum(Long.valueOf(getRowNum().longValue() + 1L));
    } 
    return getRowNum();
  }
}
