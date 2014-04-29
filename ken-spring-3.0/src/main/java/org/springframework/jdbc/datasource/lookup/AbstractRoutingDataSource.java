/*   1:    */ package org.springframework.jdbc.datasource.lookup;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import javax.sql.DataSource;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.jdbc.datasource.AbstractDataSource;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public abstract class AbstractRoutingDataSource
/*  14:    */   extends AbstractDataSource
/*  15:    */   implements InitializingBean
/*  16:    */ {
/*  17:    */   private Map<Object, Object> targetDataSources;
/*  18:    */   private Object defaultTargetDataSource;
/*  19: 46 */   private boolean lenientFallback = true;
/*  20: 48 */   private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
/*  21:    */   private Map<Object, DataSource> resolvedDataSources;
/*  22:    */   private DataSource resolvedDefaultDataSource;
/*  23:    */   
/*  24:    */   public void setTargetDataSources(Map<Object, Object> targetDataSources)
/*  25:    */   {
/*  26: 65 */     this.targetDataSources = targetDataSources;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setDefaultTargetDataSource(Object defaultTargetDataSource)
/*  30:    */   {
/*  31: 78 */     this.defaultTargetDataSource = defaultTargetDataSource;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setLenientFallback(boolean lenientFallback)
/*  35:    */   {
/*  36: 95 */     this.lenientFallback = lenientFallback;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setDataSourceLookup(DataSourceLookup dataSourceLookup)
/*  40:    */   {
/*  41:105 */     this.dataSourceLookup = (dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup());
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void afterPropertiesSet()
/*  45:    */   {
/*  46:110 */     if (this.targetDataSources == null) {
/*  47:111 */       throw new IllegalArgumentException("Property 'targetDataSources' is required");
/*  48:    */     }
/*  49:113 */     this.resolvedDataSources = new HashMap(this.targetDataSources.size());
/*  50:114 */     for (Map.Entry entry : this.targetDataSources.entrySet())
/*  51:    */     {
/*  52:115 */       Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
/*  53:116 */       DataSource dataSource = resolveSpecifiedDataSource(entry.getValue());
/*  54:117 */       this.resolvedDataSources.put(lookupKey, dataSource);
/*  55:    */     }
/*  56:119 */     if (this.defaultTargetDataSource != null) {
/*  57:120 */       this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource);
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected DataSource resolveSpecifiedDataSource(Object dataSource)
/*  62:    */     throws IllegalArgumentException
/*  63:    */   {
/*  64:134 */     if ((dataSource instanceof DataSource)) {
/*  65:135 */       return (DataSource)dataSource;
/*  66:    */     }
/*  67:137 */     if ((dataSource instanceof String)) {
/*  68:138 */       return this.dataSourceLookup.getDataSource((String)dataSource);
/*  69:    */     }
/*  70:141 */     throw new IllegalArgumentException(
/*  71:142 */       "Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public Connection getConnection()
/*  75:    */     throws SQLException
/*  76:    */   {
/*  77:148 */     return determineTargetDataSource().getConnection();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Connection getConnection(String username, String password)
/*  81:    */     throws SQLException
/*  82:    */   {
/*  83:152 */     return determineTargetDataSource().getConnection(username, password);
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected DataSource determineTargetDataSource()
/*  87:    */   {
/*  88:164 */     Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
/*  89:165 */     Object lookupKey = determineCurrentLookupKey();
/*  90:166 */     DataSource dataSource = (DataSource)this.resolvedDataSources.get(lookupKey);
/*  91:167 */     if ((dataSource == null) && ((this.lenientFallback) || (lookupKey == null))) {
/*  92:168 */       dataSource = this.resolvedDefaultDataSource;
/*  93:    */     }
/*  94:170 */     if (dataSource == null) {
/*  95:171 */       throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
/*  96:    */     }
/*  97:173 */     return dataSource;
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected Object resolveSpecifiedLookupKey(Object lookupKey)
/* 101:    */   {
/* 102:187 */     return lookupKey;
/* 103:    */   }
/* 104:    */   
/* 105:    */   protected abstract Object determineCurrentLookupKey();
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 * JD-Core Version:    0.7.0.1
 */