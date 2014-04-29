/*   1:    */ package org.springframework.jdbc.support;
/*   2:    */ 
/*   3:    */ import java.sql.Connection;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.sql.Statement;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.jdbc.CannotGetJdbcConnectionException;
/*  11:    */ 
/*  12:    */ public class DatabaseStartupValidator
/*  13:    */   implements InitializingBean
/*  14:    */ {
/*  15:    */   public static final int DEFAULT_INTERVAL = 1;
/*  16:    */   public static final int DEFAULT_TIMEOUT = 60;
/*  17: 49 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private DataSource dataSource;
/*  19:    */   private String validationQuery;
/*  20: 55 */   private int interval = 1;
/*  21: 57 */   private int timeout = 60;
/*  22:    */   
/*  23:    */   public void setDataSource(DataSource dataSource)
/*  24:    */   {
/*  25: 64 */     this.dataSource = dataSource;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setValidationQuery(String validationQuery)
/*  29:    */   {
/*  30: 71 */     this.validationQuery = validationQuery;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setInterval(int interval)
/*  34:    */   {
/*  35: 79 */     this.interval = interval;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setTimeout(int timeout)
/*  39:    */   {
/*  40: 87 */     this.timeout = timeout;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void afterPropertiesSet()
/*  44:    */   {
/*  45: 97 */     if (this.dataSource == null) {
/*  46: 98 */       throw new IllegalArgumentException("dataSource is required");
/*  47:    */     }
/*  48:100 */     if (this.validationQuery == null) {
/*  49:101 */       throw new IllegalArgumentException("validationQuery is required");
/*  50:    */     }
/*  51:    */     try
/*  52:    */     {
/*  53:105 */       boolean validated = false;
/*  54:106 */       long beginTime = System.currentTimeMillis();
/*  55:107 */       long deadLine = beginTime + this.timeout * 1000;
/*  56:108 */       SQLException latestEx = null;
/*  57:110 */       while ((!validated) && (System.currentTimeMillis() < deadLine))
/*  58:    */       {
/*  59:111 */         Connection con = null;
/*  60:112 */         Statement stmt = null;
/*  61:    */         try
/*  62:    */         {
/*  63:114 */           con = this.dataSource.getConnection();
/*  64:115 */           stmt = con.createStatement();
/*  65:116 */           stmt.execute(this.validationQuery);
/*  66:117 */           validated = true;
/*  67:    */         }
/*  68:    */         catch (SQLException ex)
/*  69:    */         {
/*  70:120 */           latestEx = ex;
/*  71:121 */           this.logger.debug("Validation query [" + this.validationQuery + "] threw exception", ex);
/*  72:122 */           float rest = (float)(deadLine - System.currentTimeMillis()) / 1000.0F;
/*  73:123 */           if (rest > this.interval) {
/*  74:124 */             this.logger.warn("Database has not started up yet - retrying in " + this.interval + 
/*  75:125 */               " seconds (timeout in " + rest + " seconds)");
/*  76:    */           }
/*  77:    */         }
/*  78:    */         finally
/*  79:    */         {
/*  80:129 */           JdbcUtils.closeStatement(stmt);
/*  81:130 */           JdbcUtils.closeConnection(con);
/*  82:    */         }
/*  83:133 */         if (!validated) {
/*  84:134 */           Thread.sleep(this.interval * 1000);
/*  85:    */         }
/*  86:    */       }
/*  87:138 */       if (!validated) {
/*  88:139 */         throw new CannotGetJdbcConnectionException(
/*  89:140 */           "Database has not started up within " + this.timeout + " seconds", latestEx);
/*  90:    */       }
/*  91:143 */       float duration = (float)((System.currentTimeMillis() - beginTime) / 1000L);
/*  92:144 */       if (this.logger.isInfoEnabled()) {
/*  93:145 */         this.logger.info("Database startup detected after " + duration + " seconds");
/*  94:    */       }
/*  95:    */     }
/*  96:    */     catch (InterruptedException localInterruptedException)
/*  97:    */     {
/*  98:150 */       Thread.currentThread().interrupt();
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.DatabaseStartupValidator
 * JD-Core Version:    0.7.0.1
 */