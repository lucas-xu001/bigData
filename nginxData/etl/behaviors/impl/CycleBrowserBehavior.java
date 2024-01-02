package com.atguigu.etl.behaviors.impl;

import cn.hutool.core.lang.Tuple;
import com.atguigu.etl.behaviors.BehaviorTool;
import com.atguigu.etl.behaviors.RequestBehavior;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CycleBrowserBehavior extends RequestBehavior {
  public static Logger logger = LoggerFactory.getLogger(CycleBrowserBehavior.class);
  
  public Long run() {
    List<Tuple> times = BehaviorTool.popCycleTimeSeries();
    setUserIp(BehaviorTool.popOneIpv4());
    setServerIp(BehaviorTool.popOneHostIp());
    setClickOrImpression(BehaviorTool.popOneClickOrImpressionSeries());
    doSetAdIdAndPlatformInfo();
    for (Tuple time : times) {
      setUa(BehaviorTool.popOneNormalUa());
      setServerTime((String)time.get(0));
      setEventTime((String)time.get(1));
      logger.info(output());
      setRowNum(Long.valueOf(getRowNum().longValue() + 1L));
    } 
    return getRowNum();
  }
}
