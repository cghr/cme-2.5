/*  1:   */ package org.springframework.jmx.export.metadata;
/*  2:   */ 
/*  3:   */ public class ManagedAttribute
/*  4:   */   extends AbstractJmxAttribute
/*  5:   */ {
/*  6:30 */   public static final ManagedAttribute EMPTY = new ManagedAttribute();
/*  7:   */   private Object defaultValue;
/*  8:   */   private String persistPolicy;
/*  9:37 */   private int persistPeriod = -1;
/* 10:   */   
/* 11:   */   public void setDefaultValue(Object defaultValue)
/* 12:   */   {
/* 13:44 */     this.defaultValue = defaultValue;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Object getDefaultValue()
/* 17:   */   {
/* 18:51 */     return this.defaultValue;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setPersistPolicy(String persistPolicy)
/* 22:   */   {
/* 23:55 */     this.persistPolicy = persistPolicy;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getPersistPolicy()
/* 27:   */   {
/* 28:59 */     return this.persistPolicy;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setPersistPeriod(int persistPeriod)
/* 32:   */   {
/* 33:63 */     this.persistPeriod = persistPeriod;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public int getPersistPeriod()
/* 37:   */   {
/* 38:67 */     return this.persistPeriod;
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.ManagedAttribute
 * JD-Core Version:    0.7.0.1
 */