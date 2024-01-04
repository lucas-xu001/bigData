package com.root.etl.producer;

import cn.hutool.core.lang.WeightRandom;
import com.root.etl.bean.Config;
import com.root.etl.behaviors.RequestBehavior;
import com.root.etl.behaviors.impl.CycleBrowserBehavior;
import com.root.etl.behaviors.impl.CycleDeviceBehavior;
import com.root.etl.behaviors.impl.FastRequestAdFixedDeviceFixedBehavior;
import com.root.etl.behaviors.impl.FastRequestAdFixedIpFixedBehavior;
import com.root.etl.behaviors.impl.NormalBotBehavior;
import com.root.etl.behaviors.impl.NormalBrowserBehavior;
import com.root.etl.behaviors.impl.NormalMobilBehavior;
import com.root.etl.loader.ConfigLoader;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Executor extends Thread {
  private final WeightRandom<Class<?>> weightRandom;
  
  private final Integer num;
  
  public static Logger logger = LoggerFactory.getLogger(Executor.class);
  
  public Long rowNum = Long.valueOf(0L);
  
  public CountDownLatch latch;
  
  public Executor(CountDownLatch latch, Integer num) {
    Config config = ConfigLoader.getConfig();
    this.weightRandom = WeightRandom.create();
    this.weightRandom.add(NormalBrowserBehavior.class, config.getNormalBrowser().doubleValue());
    this.weightRandom.add(NormalMobilBehavior.class, config.getNormalDevice().doubleValue());
    this.weightRandom.add(FastRequestAdFixedIpFixedBehavior.class, config.getFastFixedIpBrowser().doubleValue());
    this.weightRandom.add(FastRequestAdFixedDeviceFixedBehavior.class, config.getFastFixedIdDevice().doubleValue());
    this.weightRandom.add(NormalBotBehavior.class, config.getBotBrowser().doubleValue());
    this.weightRandom.add(CycleBrowserBehavior.class, config.getCycleBrowser().doubleValue());
    this.weightRandom.add(CycleDeviceBehavior.class, config.getCycleDevice().doubleValue());
    this.num = num;
    this.latch = latch;
  }
  
  public void run() {
    logger.info(");
    for (int i = 0; i < this.num.intValue(); i++) {
      Class<?> behavior = (Class)this.weightRandom.next();
      RequestBehavior requestBehavior = null;
      try {
        requestBehavior = (RequestBehavior)behavior.newInstance();
      } catch (InstantiationException|IllegalAccessException e) {
        e.printStackTrace();
      } 
      assert requestBehavior != null;
      this.rowNum = Long.valueOf(this.rowNum.longValue() + requestBehavior.run().longValue());
    } 
    logger.info("+ this.rowNum + ");
    this.latch.countDown();
  }
}
