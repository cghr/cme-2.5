/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import org.springframework.util.Assert;
/*   4:    */ 
/*   5:    */ public class RuntimeBeanReference
/*   6:    */   implements BeanReference
/*   7:    */ {
/*   8:    */   private final String beanName;
/*   9:    */   private final boolean toParent;
/*  10:    */   private Object source;
/*  11:    */   
/*  12:    */   public RuntimeBeanReference(String beanName)
/*  13:    */   {
/*  14: 46 */     this(beanName, false);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public RuntimeBeanReference(String beanName, boolean toParent)
/*  18:    */   {
/*  19: 58 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  20: 59 */     this.beanName = beanName;
/*  21: 60 */     this.toParent = toParent;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String getBeanName()
/*  25:    */   {
/*  26: 65 */     return this.beanName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public boolean isToParent()
/*  30:    */   {
/*  31: 73 */     return this.toParent;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setSource(Object source)
/*  35:    */   {
/*  36: 81 */     this.source = source;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Object getSource()
/*  40:    */   {
/*  41: 85 */     return this.source;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean equals(Object other)
/*  45:    */   {
/*  46: 91 */     if (this == other) {
/*  47: 92 */       return true;
/*  48:    */     }
/*  49: 94 */     if (!(other instanceof RuntimeBeanReference)) {
/*  50: 95 */       return false;
/*  51:    */     }
/*  52: 97 */     RuntimeBeanReference that = (RuntimeBeanReference)other;
/*  53: 98 */     return (this.beanName.equals(that.beanName)) && (this.toParent == that.toParent);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int hashCode()
/*  57:    */   {
/*  58:103 */     int result = this.beanName.hashCode();
/*  59:104 */     result = 29 * result + (this.toParent ? 1 : 0);
/*  60:105 */     return result;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String toString()
/*  64:    */   {
/*  65:110 */     return '<' + getBeanName() + '>';
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.RuntimeBeanReference
 * JD-Core Version:    0.7.0.1
 */