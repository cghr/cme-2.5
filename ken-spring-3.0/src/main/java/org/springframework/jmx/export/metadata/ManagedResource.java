/*   1:    */ package org.springframework.jmx.export.metadata;
/*   2:    */ 
/*   3:    */ public class ManagedResource
/*   4:    */   extends AbstractJmxAttribute
/*   5:    */ {
/*   6:    */   private String objectName;
/*   7: 34 */   private boolean log = false;
/*   8:    */   private String logFile;
/*   9:    */   private String persistPolicy;
/*  10: 40 */   private int persistPeriod = -1;
/*  11:    */   private String persistName;
/*  12:    */   private String persistLocation;
/*  13:    */   
/*  14:    */   public void setObjectName(String objectName)
/*  15:    */   {
/*  16: 51 */     this.objectName = objectName;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public String getObjectName()
/*  20:    */   {
/*  21: 58 */     return this.objectName;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setLog(boolean log)
/*  25:    */   {
/*  26: 62 */     this.log = log;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isLog()
/*  30:    */   {
/*  31: 66 */     return this.log;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setLogFile(String logFile)
/*  35:    */   {
/*  36: 70 */     this.logFile = logFile;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getLogFile()
/*  40:    */   {
/*  41: 74 */     return this.logFile;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setPersistPolicy(String persistPolicy)
/*  45:    */   {
/*  46: 78 */     this.persistPolicy = persistPolicy;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getPersistPolicy()
/*  50:    */   {
/*  51: 82 */     return this.persistPolicy;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setPersistPeriod(int persistPeriod)
/*  55:    */   {
/*  56: 86 */     this.persistPeriod = persistPeriod;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public int getPersistPeriod()
/*  60:    */   {
/*  61: 90 */     return this.persistPeriod;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setPersistName(String persistName)
/*  65:    */   {
/*  66: 94 */     this.persistName = persistName;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getPersistName()
/*  70:    */   {
/*  71: 98 */     return this.persistName;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setPersistLocation(String persistLocation)
/*  75:    */   {
/*  76:102 */     this.persistLocation = persistLocation;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String getPersistLocation()
/*  80:    */   {
/*  81:106 */     return this.persistLocation;
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.metadata.ManagedResource
 * JD-Core Version:    0.7.0.1
 */