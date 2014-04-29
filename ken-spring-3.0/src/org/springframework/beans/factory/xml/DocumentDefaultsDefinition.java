/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.parsing.DefaultsDefinition;
/*   4:    */ 
/*   5:    */ public class DocumentDefaultsDefinition
/*   6:    */   implements DefaultsDefinition
/*   7:    */ {
/*   8:    */   private String lazyInit;
/*   9:    */   private String merge;
/*  10:    */   private String autowire;
/*  11:    */   private String dependencyCheck;
/*  12:    */   private String autowireCandidates;
/*  13:    */   private String initMethod;
/*  14:    */   private String destroyMethod;
/*  15:    */   private Object source;
/*  16:    */   
/*  17:    */   public void setLazyInit(String lazyInit)
/*  18:    */   {
/*  19: 52 */     this.lazyInit = lazyInit;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public String getLazyInit()
/*  23:    */   {
/*  24: 59 */     return this.lazyInit;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setMerge(String merge)
/*  28:    */   {
/*  29: 66 */     this.merge = merge;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getMerge()
/*  33:    */   {
/*  34: 73 */     return this.merge;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setAutowire(String autowire)
/*  38:    */   {
/*  39: 80 */     this.autowire = autowire;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getAutowire()
/*  43:    */   {
/*  44: 87 */     return this.autowire;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setDependencyCheck(String dependencyCheck)
/*  48:    */   {
/*  49: 94 */     this.dependencyCheck = dependencyCheck;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getDependencyCheck()
/*  53:    */   {
/*  54:101 */     return this.dependencyCheck;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setAutowireCandidates(String autowireCandidates)
/*  58:    */   {
/*  59:109 */     this.autowireCandidates = autowireCandidates;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getAutowireCandidates()
/*  63:    */   {
/*  64:117 */     return this.autowireCandidates;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setInitMethod(String initMethod)
/*  68:    */   {
/*  69:124 */     this.initMethod = initMethod;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getInitMethod()
/*  73:    */   {
/*  74:131 */     return this.initMethod;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setDestroyMethod(String destroyMethod)
/*  78:    */   {
/*  79:138 */     this.destroyMethod = destroyMethod;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getDestroyMethod()
/*  83:    */   {
/*  84:145 */     return this.destroyMethod;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setSource(Object source)
/*  88:    */   {
/*  89:153 */     this.source = source;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Object getSource()
/*  93:    */   {
/*  94:157 */     return this.source;
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DocumentDefaultsDefinition
 * JD-Core Version:    0.7.0.1
 */