package com.root.etl.loader;

import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.RsHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.root.etl.entity.Ads;
import com.root.etl.entity.AdsPlatform;
import com.root.etl.entity.PlatformInfo;
import com.root.etl.entity.Product;
import com.root.etl.entity.ServerHost;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBLoader {
  public enum OneTable {
    SERVER_HOST("server_host", ServerHost.class),
    ADS("ads", Ads.class),
    ADS_PLATFORM("ads_platform", AdsPlatform.class),
    PRODUCT("product", Product.class),
    PLATFORM_INFO("platform_info", PlatformInfo.class);
    
    private ArrayList<?> arrayList = new ArrayList();
    
    private final String tableName;
    
    private final Class<?> clazz;
    
    OneTable(String tableName, Class<?> clazz) {
      this.tableName = tableName;
      this.clazz = clazz;
    }
    
    public String getTableName() {
      return this.tableName;
    }
    
    public Class<?> getClazz() {
      return this.clazz;
    }
    
    public <T> ArrayList<T> getArrayList(Class<T> clazz) {
      return (ArrayList)this.arrayList;
    }
    
    public ArrayList<?> getArrayList() {
      return this.arrayList;
    }
    
    public void setArrayList(ArrayList<?> arrayList) {
      this.arrayList = arrayList;
    }
  }
  
  private static final Logger logger = LoggerFactory.getLogger(DBLoader.class);
  
  private final String database;
  
  private final Connection connection;
  
  public DBLoader(String jdbcUrl, String user, String password, String driver, String database) throws SQLException {
    SimpleDataSource ds = new SimpleDataSource(jdbcUrl, user, password, driver);
    ds.setLoginTimeout(5);
    this.connection = ds.getConnection();
    logger.info(");
    this.database = database.trim();
  }
  
  public <T> void loadOneTable(OneTable oneTable, Class<T> clazz) throws SQLException {
    String tableName = oneTable.getTableName();
    ArrayList<T> beanList = oneTable.getArrayList(clazz);
    logger.info("+ this.database + "." + tableName);
    List<Entity> query = (List<Entity>)SqlExecutor.query(this.connection, "select * from " + this.database + "." + tableName, (RsHandler)new EntityListHandler(), new Object[0]);
    for (Entity entity : query) {
      T bean = (T)entity.toBean(clazz);
      beanList.add(bean);
    } 
    if (logger.isDebugEnabled())
      for (T bean : beanList)
        logger.debug("" + bean);  
    logger.info(this.database + "." + tableName + "+ beanList.size() + ");
  }
  
  public void loadAll() throws SQLException {
    for (OneTable oneTable : OneTable.values())
      loadOneTable(oneTable, oneTable.getClazz()); 
  }
}
