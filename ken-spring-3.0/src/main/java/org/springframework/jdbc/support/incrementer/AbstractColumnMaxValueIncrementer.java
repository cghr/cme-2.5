/*  1:   */ package org.springframework.jdbc.support.incrementer;
/*  2:   */ 
/*  3:   */ import javax.sql.DataSource;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public abstract class AbstractColumnMaxValueIncrementer
/*  7:   */   extends AbstractDataFieldMaxValueIncrementer
/*  8:   */ {
/*  9:   */   private String columnName;
/* 10:37 */   private int cacheSize = 1;
/* 11:   */   
/* 12:   */   public AbstractColumnMaxValueIncrementer() {}
/* 13:   */   
/* 14:   */   public AbstractColumnMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName)
/* 15:   */   {
/* 16:56 */     super(dataSource, incrementerName);
/* 17:57 */     Assert.notNull(columnName, "Column name must not be null");
/* 18:58 */     this.columnName = columnName;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setColumnName(String columnName)
/* 22:   */   {
/* 23:66 */     this.columnName = columnName;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getColumnName()
/* 27:   */   {
/* 28:73 */     return this.columnName;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setCacheSize(int cacheSize)
/* 32:   */   {
/* 33:80 */     this.cacheSize = cacheSize;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public int getCacheSize()
/* 37:   */   {
/* 38:87 */     return this.cacheSize;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void afterPropertiesSet()
/* 42:   */   {
/* 43:92 */     super.afterPropertiesSet();
/* 44:93 */     if (this.columnName == null) {
/* 45:94 */       throw new IllegalArgumentException("Property 'columnName' is required");
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.AbstractColumnMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */