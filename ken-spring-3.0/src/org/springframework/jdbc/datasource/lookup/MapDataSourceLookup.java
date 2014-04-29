/*   1:    */ package org.springframework.jdbc.datasource.lookup;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class MapDataSourceLookup
/*  10:    */   implements DataSourceLookup
/*  11:    */ {
/*  12: 39 */   private final Map<String, DataSource> dataSources = new HashMap(4);
/*  13:    */   
/*  14:    */   public MapDataSourceLookup() {}
/*  15:    */   
/*  16:    */   public MapDataSourceLookup(Map<String, DataSource> dataSources)
/*  17:    */   {
/*  18: 54 */     setDataSources(dataSources);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public MapDataSourceLookup(String dataSourceName, DataSource dataSource)
/*  22:    */   {
/*  23: 63 */     addDataSource(dataSourceName, dataSource);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setDataSources(Map<String, DataSource> dataSources)
/*  27:    */   {
/*  28: 75 */     if (dataSources != null) {
/*  29: 76 */       this.dataSources.putAll(dataSources);
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public Map<String, DataSource> getDataSources()
/*  34:    */   {
/*  35: 86 */     return Collections.unmodifiableMap(this.dataSources);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void addDataSource(String dataSourceName, DataSource dataSource)
/*  39:    */   {
/*  40: 96 */     Assert.notNull(dataSourceName, "DataSource name must not be null");
/*  41: 97 */     Assert.notNull(dataSource, "DataSource must not be null");
/*  42: 98 */     this.dataSources.put(dataSourceName, dataSource);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public DataSource getDataSource(String dataSourceName)
/*  46:    */     throws DataSourceLookupFailureException
/*  47:    */   {
/*  48:102 */     Assert.notNull(dataSourceName, "DataSource name must not be null");
/*  49:103 */     DataSource dataSource = (DataSource)this.dataSources.get(dataSourceName);
/*  50:104 */     if (dataSource == null) {
/*  51:105 */       throw new DataSourceLookupFailureException(
/*  52:106 */         "No DataSource with name '" + dataSourceName + "' registered");
/*  53:    */     }
/*  54:108 */     return dataSource;
/*  55:    */   }
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.MapDataSourceLookup
 * JD-Core Version:    0.7.0.1
 */