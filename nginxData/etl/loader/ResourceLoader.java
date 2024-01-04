package com.root.etl.loader;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
  public static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
  
  public static final ArrayList<String> NORMAL_USER_AGENTS = new ArrayList<>();
  
  public static final ArrayList<String> BOT_USER_AGENTS = new ArrayList<>();
  
  public static final ArrayList<String> SPIDER_USER_AGENTS = new ArrayList<>();
  
  public void loadNormalUserAgents() {
    logger.info(");
    InputStream useragentCache = ResourceLoader.class.getClassLoader().getResourceAsStream("normal_user-agents");
    Scanner normalUseragentScanner = new Scanner(useragentCache);
    while (normalUseragentScanner.hasNextLine()) {
      String oneAgent = normalUseragentScanner.nextLine();
      NORMAL_USER_AGENTS.add(oneAgent);
    } 
    logger.info("+ NORMAL_USER_AGENTS.size() + ");
  }
  
  public void loadOtherUserAgents() {
    logger.info(");
    InputStream resourceAsStream = ResourceLoader.class.getClassLoader().getResourceAsStream("crawler-user-agents.json");
    assert resourceAsStream != null;
    Scanner scanner = new Scanner(resourceAsStream);
    String content = scanner.useDelimiter("\\A").next();
    JSONArray agentArray = JSONUtil.parseArray(content);
    for (int i = 0; i < agentArray.size(); i++) {
      JSONObject jsonObject = agentArray.getJSONObject(Integer.valueOf(i));
      JSONArray instances = jsonObject.getJSONArray("instances");
      String url = jsonObject.getStr("url");
      if (url != null && url.length() != 0) {
        for (int j = 0; j < instances.size(); j++) {
          String agent = instances.getStr(Integer.valueOf(j));
          if (agent.contains("bot")) {
            BOT_USER_AGENTS.add(agent);
          } else {
            SPIDER_USER_AGENTS.add(agent);
          } 
        } 
      } else {
        for (int j = 0; j < instances.size(); j++)
          BOT_USER_AGENTS.add(instances.getStr(Integer.valueOf(j))); 
      } 
    } 
    logger.info("+ BOT_USER_AGENTS.size() + ");
    logger.info("+ SPIDER_USER_AGENTS.size() + ");
  }
}
