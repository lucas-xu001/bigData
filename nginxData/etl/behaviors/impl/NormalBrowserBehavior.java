package com.atguigu.etl.behaviors.impl;

import cn.hutool.core.lang.Tuple;
import com.atguigu.etl.behaviors.BehaviorTool;
import com.atguigu.etl.behaviors.RequestBehavior;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalBrowserBehavior extends RequestBehavior {
  public static Logger logger = LoggerFactory.getLogger(NormalBrowserBehavior.class);
  
  public Long run() {
    List<Tuple> times = BehaviorTool.popNormalTimeSeries();
    setUa(BehaviorTool.popOneNormalUa());
    setUserIp(BehaviorTool.popOneIpv4());
    setServerIp(BehaviorTool.popOneHostIp());
    doSetAdIdAndPlatformInfo();
    for (Tuple time : times) {
      setServerTime((String)time.get(0));
      setEventTime((String)time.get(1));
      List<String> normalClickOrImpressionSeries = BehaviorTool.popNormalClickOrImpressionSeries();
      for (String normalClickOrImpression : normalClickOrImpressionSeries) {
        setClickOrImpression(normalClickOrImpression);
        logger.info(output());
        setRowNum(Long.valueOf(getRowNum().longValue() + 1L));
      } 
    } 
    return getRowNum();
  }
}