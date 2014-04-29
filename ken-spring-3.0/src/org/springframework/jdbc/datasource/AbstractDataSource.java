/*   1:    */ package org.springframework.jdbc.datasource;
/*   2:    */ 
/*   3:    */ import java.io.PrintWriter;
/*   4:    */ import java.sql.SQLException;
/*   5:    */ import java.util.logging.Logger;
/*   6:    */ import javax.sql.DataSource;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public abstract class AbstractDataSource
/*  12:    */   implements DataSource
/*  13:    */ {
/*  14: 44 */   protected final Log logger = LogFactory.getLog(getClass());
/*  15:    */   
/*  16:    */   public int getLoginTimeout()
/*  17:    */     throws SQLException
/*  18:    */   {
/*  19: 51 */     return 0;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setLoginTimeout(int timeout)
/*  23:    */     throws SQLException
/*  24:    */   {
/*  25: 58 */     throw new UnsupportedOperationException("setLoginTimeout");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PrintWriter getLogWriter()
/*  29:    */   {
/*  30: 65 */     throw new UnsupportedOperationException("getLogWriter");
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setLogWriter(PrintWriter pw)
/*  34:    */     throws SQLException
/*  35:    */   {
/*  36: 72 */     throw new UnsupportedOperationException("setLogWriter");
/*  37:    */   }
/*  38:    */   
/*  39:    */   public <T> T unwrap(Class<T> iface)
/*  40:    */     throws SQLException
/*  41:    */   {
/*  42: 82 */     Assert.notNull(iface, "Interface argument must not be null");
/*  43: 83 */     if (!DataSource.class.equals(iface)) {
/*  44: 84 */       throw new SQLException("DataSource of type [" + getClass().getName() + 
/*  45: 85 */         "] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
/*  46:    */     }
/*  47: 87 */     return this;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean isWrapperFor(Class<?> iface)
/*  51:    */     throws SQLException
/*  52:    */   {
/*  53: 91 */     return DataSource.class.equals(iface);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Logger getParentLogger()
/*  57:    */   {
/*  58:100 */     return Logger.getLogger("global");
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.AbstractDataSource
 * JD-Core Version:    0.7.0.1
 */