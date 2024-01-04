package com.root.etl.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class DataFilter extends Filter<ILoggingEvent> {
  public FilterReply decide(ILoggingEvent event) {
    if (event.getMessage().contains("GET"))
      return FilterReply.ACCEPT; 
    return FilterReply.DENY;
  }
}
