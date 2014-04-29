/*   1:    */ package org.springframework.jmx.export.metadata;
/*   2:    */ 
/*   3:    */ import org.springframework.jmx.support.MetricType;
/*   4:    */ 
/*   5:    */ public class ManagedMetric
/*   6:    */   extends AbstractJmxAttribute
/*   7:    */ {
/*   8: 32 */   private String category = "";
/*   9: 34 */   private String displayName = "";
/*  10: 36 */   private MetricType metricType = MetricType.GAUGE;
/*  11: 38 */   private int persistPeriod = -1;
/*  12: 40 */   private String persistPolicy = "";
/*  13: 42 */   private String unit = "";
/*  14:    */   
/*  15:    */   public void setCategory(String category)
/*  16:    */   {
/*  17: 49 */     this.category = category;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String getCategory()
/*  21:    */   {
/*  22: 56 */     return this.category;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setDisplayName(String displayName)
/*  26:    */   {
/*  27: 63 */     this.displayName = displayName;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String getDisplayName()
/*  31:    */   {
/*  32: 70 */     return this.displayName;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setMetricType(MetricType metricType)
/*  36:    */   {
/*  37: 77 */     this.metricType = metricType;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public MetricType getMetricType()
/*  41:    */   {
/*  42: 84 */     return this.metricType;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setPersistPeriod(int persistPeriod)
/*  46:    */   {
/*  47: 91 */     this.persistPeriod = persistPeriod;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getPersistPeriod()
/*  51:    */   {
/*  52: 98 */     return this.persistPeriod;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setPersistPolicy(String persistPolicy)
/*  56:    */   {
/*  57:105 */     this.persistPolicy = persistPolicy;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getPersistPolicy()
/*  61:    */   {
/*  62:112 */     return this.persistPolicy;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setUnit(String unit)
/*  66:    */   {
/*  67:119 */     this.unit = unit;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getUnit()
/*  71:    */   {
/*  72:126 */     return this.unit;
/*  73:    */   }
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.ManagedMetric
 * JD-Core Version:    0.7.0.1
 */