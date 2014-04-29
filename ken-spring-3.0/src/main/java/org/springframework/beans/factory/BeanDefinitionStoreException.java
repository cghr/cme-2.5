/*   1:    */ package org.springframework.beans.factory;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.FatalBeanException;
/*   4:    */ 
/*   5:    */ public class BeanDefinitionStoreException
/*   6:    */   extends FatalBeanException
/*   7:    */ {
/*   8:    */   private String resourceDescription;
/*   9:    */   private String beanName;
/*  10:    */   
/*  11:    */   public BeanDefinitionStoreException(String msg)
/*  12:    */   {
/*  13: 41 */     super(msg);
/*  14:    */   }
/*  15:    */   
/*  16:    */   public BeanDefinitionStoreException(String msg, Throwable cause)
/*  17:    */   {
/*  18: 50 */     super(msg, cause);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public BeanDefinitionStoreException(String resourceDescription, String msg)
/*  22:    */   {
/*  23: 59 */     super(msg);
/*  24: 60 */     this.resourceDescription = resourceDescription;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public BeanDefinitionStoreException(String resourceDescription, String msg, Throwable cause)
/*  28:    */   {
/*  29: 70 */     super(msg, cause);
/*  30: 71 */     this.resourceDescription = resourceDescription;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public BeanDefinitionStoreException(String resourceDescription, String beanName, String msg)
/*  34:    */   {
/*  35: 82 */     this(resourceDescription, beanName, msg, null);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public BeanDefinitionStoreException(String resourceDescription, String beanName, String msg, Throwable cause)
/*  39:    */   {
/*  40: 94 */     super("Invalid bean definition with name '" + beanName + "' defined in " + resourceDescription + ": " + msg, cause);
/*  41: 95 */     this.resourceDescription = resourceDescription;
/*  42: 96 */     this.beanName = beanName;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String getResourceDescription()
/*  46:    */   {
/*  47:105 */     return this.resourceDescription;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public String getBeanName()
/*  51:    */   {
/*  52:112 */     return this.beanName;
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanDefinitionStoreException
 * JD-Core Version:    0.7.0.1
 */