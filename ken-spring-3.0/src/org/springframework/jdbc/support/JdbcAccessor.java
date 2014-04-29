/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import javax.sql.DataSource;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.InitializingBean;
/*   7:    */ 
/*   8:    */ public abstract class JdbcAccessor
/*   9:    */   implements InitializingBean
/*  10:    */ {
/*  11: 41 */   protected final Log logger = LogFactory.getLog(getClass());
/*  12:    */   private DataSource dataSource;
/*  13:    */   private SQLExceptionTranslator exceptionTranslator;
/*  14: 47 */   private boolean lazyInit = true;
/*  15:    */   
/*  16:    */   public void setDataSource(DataSource dataSource)
/*  17:    */   {
/*  18: 54 */     this.dataSource = dataSource;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public DataSource getDataSource()
/*  22:    */   {
/*  23: 61 */     return this.dataSource;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setDatabaseProductName(String dbName)
/*  27:    */   {
/*  28: 73 */     this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dbName);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator)
/*  32:    */   {
/*  33: 85 */     this.exceptionTranslator = exceptionTranslator;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public synchronized SQLExceptionTranslator getExceptionTranslator()
/*  37:    */   {
/*  38: 96 */     if (this.exceptionTranslator == null)
/*  39:    */     {
/*  40: 97 */       DataSource dataSource = getDataSource();
/*  41: 98 */       if (dataSource != null) {
/*  42: 99 */         this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
/*  43:    */       } else {
/*  44:102 */         this.exceptionTranslator = new SQLStateSQLExceptionTranslator();
/*  45:    */       }
/*  46:    */     }
/*  47:105 */     return this.exceptionTranslator;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setLazyInit(boolean lazyInit)
/*  51:    */   {
/*  52:117 */     this.lazyInit = lazyInit;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean isLazyInit()
/*  56:    */   {
/*  57:125 */     return this.lazyInit;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void afterPropertiesSet()
/*  61:    */   {
/*  62:133 */     if (getDataSource() == null) {
/*  63:134 */       throw new IllegalArgumentException("Property 'dataSource' is required");
/*  64:    */     }
/*  65:136 */     if (!isLazyInit()) {
/*  66:137 */       getExceptionTranslator();
/*  67:    */     }
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.JdbcAccessor
 * JD-Core Version:    0.7.0.1
 */