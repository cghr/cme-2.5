/*   1:    */ package org.springframework.beans.factory.wiring;
/*   2:    */ 
/*   3:    */ import org.springframework.util.Assert;
/*   4:    */ 
/*   5:    */ public class BeanWiringInfo
/*   6:    */ {
/*   7:    */   public static final int AUTOWIRE_BY_NAME = 1;
/*   8:    */   public static final int AUTOWIRE_BY_TYPE = 2;
/*   9: 51 */   private String beanName = null;
/*  10: 53 */   private boolean isDefaultBeanName = false;
/*  11: 55 */   private int autowireMode = 0;
/*  12: 57 */   private boolean dependencyCheck = false;
/*  13:    */   
/*  14:    */   public BeanWiringInfo() {}
/*  15:    */   
/*  16:    */   public BeanWiringInfo(String beanName)
/*  17:    */   {
/*  18: 74 */     this(beanName, false);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public BeanWiringInfo(String beanName, boolean isDefaultBeanName)
/*  22:    */   {
/*  23: 86 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  24: 87 */     this.beanName = beanName;
/*  25: 88 */     this.isDefaultBeanName = isDefaultBeanName;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public BeanWiringInfo(int autowireMode, boolean dependencyCheck)
/*  29:    */   {
/*  30:103 */     if ((autowireMode != 1) && (autowireMode != 2)) {
/*  31:104 */       throw new IllegalArgumentException("Only constants AUTOWIRE_BY_NAME and AUTOWIRE_BY_TYPE supported");
/*  32:    */     }
/*  33:106 */     this.autowireMode = autowireMode;
/*  34:107 */     this.dependencyCheck = dependencyCheck;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean indicatesAutowiring()
/*  38:    */   {
/*  39:115 */     return this.beanName == null;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getBeanName()
/*  43:    */   {
/*  44:122 */     return this.beanName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public boolean isDefaultBeanName()
/*  48:    */   {
/*  49:130 */     return this.isDefaultBeanName;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getAutowireMode()
/*  53:    */   {
/*  54:138 */     return this.autowireMode;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean getDependencyCheck()
/*  58:    */   {
/*  59:146 */     return this.dependencyCheck;
/*  60:    */   }
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.wiring.BeanWiringInfo
 * JD-Core Version:    0.7.0.1
 */