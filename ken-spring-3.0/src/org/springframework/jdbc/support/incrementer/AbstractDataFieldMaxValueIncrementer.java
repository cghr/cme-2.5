/*   1:    */ package org.springframework.jdbc.support.incrementer;
/*   2:    */ 
/*   3:    */ import javax.sql.DataSource;
/*   4:    */ import org.springframework.beans.factory.InitializingBean;
/*   5:    */ import org.springframework.dao.DataAccessException;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ 
/*   8:    */ public abstract class AbstractDataFieldMaxValueIncrementer
/*   9:    */   implements DataFieldMaxValueIncrementer, InitializingBean
/*  10:    */ {
/*  11:    */   private DataSource dataSource;
/*  12:    */   private String incrementerName;
/*  13: 43 */   protected int paddingLength = 0;
/*  14:    */   
/*  15:    */   public AbstractDataFieldMaxValueIncrementer() {}
/*  16:    */   
/*  17:    */   public AbstractDataFieldMaxValueIncrementer(DataSource dataSource, String incrementerName)
/*  18:    */   {
/*  19: 60 */     Assert.notNull(dataSource, "DataSource must not be null");
/*  20: 61 */     Assert.notNull(incrementerName, "Incrementer name must not be null");
/*  21: 62 */     this.dataSource = dataSource;
/*  22: 63 */     this.incrementerName = incrementerName;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setDataSource(DataSource dataSource)
/*  26:    */   {
/*  27: 71 */     this.dataSource = dataSource;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DataSource getDataSource()
/*  31:    */   {
/*  32: 78 */     return this.dataSource;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setIncrementerName(String incrementerName)
/*  36:    */   {
/*  37: 85 */     this.incrementerName = incrementerName;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getIncrementerName()
/*  41:    */   {
/*  42: 92 */     return this.incrementerName;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setPaddingLength(int paddingLength)
/*  46:    */   {
/*  47:100 */     this.paddingLength = paddingLength;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getPaddingLength()
/*  51:    */   {
/*  52:107 */     return this.paddingLength;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void afterPropertiesSet()
/*  56:    */   {
/*  57:111 */     if (this.dataSource == null) {
/*  58:112 */       throw new IllegalArgumentException("Property 'dataSource' is required");
/*  59:    */     }
/*  60:114 */     if (this.incrementerName == null) {
/*  61:115 */       throw new IllegalArgumentException("Property 'incrementerName' is required");
/*  62:    */     }
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int nextIntValue()
/*  66:    */     throws DataAccessException
/*  67:    */   {
/*  68:121 */     return (int)getNextKey();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public long nextLongValue()
/*  72:    */     throws DataAccessException
/*  73:    */   {
/*  74:125 */     return getNextKey();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String nextStringValue()
/*  78:    */     throws DataAccessException
/*  79:    */   {
/*  80:129 */     String s = Long.toString(getNextKey());
/*  81:130 */     int len = s.length();
/*  82:131 */     if (len < this.paddingLength)
/*  83:    */     {
/*  84:132 */       StringBuilder sb = new StringBuilder(this.paddingLength);
/*  85:133 */       for (int i = 0; i < this.paddingLength - len; i++) {
/*  86:134 */         sb.append('0');
/*  87:    */       }
/*  88:136 */       sb.append(s);
/*  89:137 */       s = sb.toString();
/*  90:    */     }
/*  91:139 */     return s;
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected abstract long getNextKey();
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer
 * JD-Core Version:    0.7.0.1
 */