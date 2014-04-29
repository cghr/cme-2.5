/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.WeakHashMap;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  11:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*  12:    */ import org.springframework.core.io.ClassPathResource;
/*  13:    */ import org.springframework.core.io.Resource;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.PatternMatchUtils;
/*  16:    */ 
/*  17:    */ public class SQLErrorCodesFactory
/*  18:    */ {
/*  19:    */   public static final String SQL_ERROR_CODE_OVERRIDE_PATH = "sql-error-codes.xml";
/*  20:    */   public static final String SQL_ERROR_CODE_DEFAULT_PATH = "org/springframework/jdbc/support/sql-error-codes.xml";
/*  21: 63 */   private static final Log logger = LogFactory.getLog(SQLErrorCodesFactory.class);
/*  22: 68 */   private static final SQLErrorCodesFactory instance = new SQLErrorCodesFactory();
/*  23:    */   private final Map<String, SQLErrorCodes> errorCodesMap;
/*  24:    */   
/*  25:    */   public static SQLErrorCodesFactory getInstance()
/*  26:    */   {
/*  27: 75 */     return instance;
/*  28:    */   }
/*  29:    */   
/*  30: 88 */   private final Map<DataSource, SQLErrorCodes> dataSourceCache = new WeakHashMap(16);
/*  31:    */   
/*  32:    */   protected SQLErrorCodesFactory()
/*  33:    */   {
/*  34:    */     Map<String, SQLErrorCodes> errorCodes;
/*  35:    */     try
/*  36:    */     {
/*  37:103 */       DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
/*  38:104 */       lbf.setBeanClassLoader(getClass().getClassLoader());
/*  39:105 */       XmlBeanDefinitionReader bdr = new XmlBeanDefinitionReader(lbf);
/*  40:    */       
/*  41:    */ 
/*  42:108 */       Resource resource = loadResource("org/springframework/jdbc/support/sql-error-codes.xml");
/*  43:109 */       if ((resource != null) && (resource.exists())) {
/*  44:110 */         bdr.loadBeanDefinitions(resource);
/*  45:    */       } else {
/*  46:113 */         logger.warn("Default sql-error-codes.xml not found (should be included in spring.jar)");
/*  47:    */       }
/*  48:117 */       resource = loadResource("sql-error-codes.xml");
/*  49:118 */       if ((resource != null) && (resource.exists()))
/*  50:    */       {
/*  51:119 */         bdr.loadBeanDefinitions(resource);
/*  52:120 */         logger.info("Found custom sql-error-codes.xml file at the root of the classpath");
/*  53:    */       }
/*  54:124 */       Map<String, SQLErrorCodes> errorCodes = lbf.getBeansOfType(SQLErrorCodes.class, true, false);
/*  55:125 */       if (logger.isInfoEnabled()) {
/*  56:126 */         logger.info("SQLErrorCodes loaded: " + errorCodes.keySet());
/*  57:    */       }
/*  58:    */     }
/*  59:    */     catch (BeansException ex)
/*  60:    */     {
/*  61:130 */       logger.warn("Error loading SQL error codes from config file", ex);
/*  62:131 */       errorCodes = Collections.emptyMap();
/*  63:    */     }
/*  64:134 */     this.errorCodesMap = errorCodes;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected Resource loadResource(String path)
/*  68:    */   {
/*  69:149 */     return new ClassPathResource(path, getClass().getClassLoader());
/*  70:    */   }
/*  71:    */   
/*  72:    */   public SQLErrorCodes getErrorCodes(String dbName)
/*  73:    */   {
/*  74:161 */     Assert.notNull(dbName, "Database product name must not be null");
/*  75:    */     
/*  76:163 */     SQLErrorCodes sec = (SQLErrorCodes)this.errorCodesMap.get(dbName);
/*  77:164 */     if (sec == null) {
/*  78:165 */       for (SQLErrorCodes candidate : this.errorCodesMap.values()) {
/*  79:166 */         if (PatternMatchUtils.simpleMatch(candidate.getDatabaseProductNames(), dbName))
/*  80:    */         {
/*  81:167 */           sec = candidate;
/*  82:168 */           break;
/*  83:    */         }
/*  84:    */       }
/*  85:    */     }
/*  86:172 */     if (sec != null)
/*  87:    */     {
/*  88:173 */       if (logger.isDebugEnabled()) {
/*  89:174 */         logger.debug("SQL error codes for '" + dbName + "' found");
/*  90:    */       }
/*  91:176 */       return sec;
/*  92:    */     }
/*  93:180 */     if (logger.isDebugEnabled()) {
/*  94:181 */       logger.debug("SQL error codes for '" + dbName + "' not found");
/*  95:    */     }
/*  96:183 */     return new SQLErrorCodes();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public SQLErrorCodes getErrorCodes(DataSource dataSource)
/* 100:    */   {
/* 101:196 */     Assert.notNull(dataSource, "DataSource must not be null");
/* 102:197 */     if (logger.isDebugEnabled()) {
/* 103:198 */       logger.debug("Looking up default SQLErrorCodes for DataSource [" + dataSource + "]");
/* 104:    */     }
/* 105:201 */     synchronized (this.dataSourceCache)
/* 106:    */     {
/* 107:203 */       SQLErrorCodes sec = (SQLErrorCodes)this.dataSourceCache.get(dataSource);
/* 108:204 */       if (sec != null)
/* 109:    */       {
/* 110:205 */         if (logger.isDebugEnabled()) {
/* 111:206 */           logger.debug("SQLErrorCodes found in cache for DataSource [" + 
/* 112:207 */             dataSource.getClass().getName() + '@' + Integer.toHexString(dataSource.hashCode()) + "]");
/* 113:    */         }
/* 114:209 */         return sec;
/* 115:    */       }
/* 116:    */       try
/* 117:    */       {
/* 118:213 */         String dbName = (String)JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
/* 119:214 */         if (dbName != null)
/* 120:    */         {
/* 121:215 */           if (logger.isDebugEnabled()) {
/* 122:216 */             logger.debug("Database product name cached for DataSource [" + 
/* 123:217 */               dataSource.getClass().getName() + '@' + Integer.toHexString(dataSource.hashCode()) + 
/* 124:218 */               "]: name is '" + dbName + "'");
/* 125:    */           }
/* 126:220 */           sec = getErrorCodes(dbName);
/* 127:221 */           this.dataSourceCache.put(dataSource, sec);
/* 128:222 */           return sec;
/* 129:    */         }
/* 130:    */       }
/* 131:    */       catch (MetaDataAccessException ex)
/* 132:    */       {
/* 133:226 */         logger.warn("Error while extracting database product name - falling back to empty error codes", ex);
/* 134:    */       }
/* 135:    */     }
/* 136:231 */     return new SQLErrorCodes();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public SQLErrorCodes registerDatabase(DataSource dataSource, String dbName)
/* 140:    */   {
/* 141:242 */     synchronized (this.dataSourceCache)
/* 142:    */     {
/* 143:243 */       SQLErrorCodes sec = getErrorCodes(dbName);
/* 144:244 */       this.dataSourceCache.put(dataSource, sec);
/* 145:245 */       return sec;
/* 146:    */     }
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.SQLErrorCodesFactory
 * JD-Core Version:    0.7.0.1
 */