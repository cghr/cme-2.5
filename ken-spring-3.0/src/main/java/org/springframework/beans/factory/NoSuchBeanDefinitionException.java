/*   1:    */ package org.springframework.beans.factory;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeansException;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class NoSuchBeanDefinitionException
/*   7:    */   extends BeansException
/*   8:    */ {
/*   9:    */   private String beanName;
/*  10:    */   private Class beanType;
/*  11:    */   
/*  12:    */   public NoSuchBeanDefinitionException(String name)
/*  13:    */   {
/*  14: 43 */     super("No bean named '" + name + "' is defined");
/*  15: 44 */     this.beanName = name;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public NoSuchBeanDefinitionException(String name, String message)
/*  19:    */   {
/*  20: 53 */     super("No bean named '" + name + "' is defined: " + message);
/*  21: 54 */     this.beanName = name;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public NoSuchBeanDefinitionException(Class type)
/*  25:    */   {
/*  26: 62 */     super("No unique bean of type [" + type.getName() + "] is defined");
/*  27: 63 */     this.beanType = type;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public NoSuchBeanDefinitionException(Class type, String message)
/*  31:    */   {
/*  32: 72 */     super("No unique bean of type [" + type.getName() + "] is defined: " + message);
/*  33: 73 */     this.beanType = type;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public NoSuchBeanDefinitionException(Class type, String dependencyDescription, String message)
/*  37:    */   {
/*  38: 85 */     super("No matching bean of type [" + type.getName() + "] found for dependency" + (StringUtils.hasLength(dependencyDescription) ? " [" + dependencyDescription + "]" : "") + ": " + message);
/*  39: 86 */     this.beanType = type;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getBeanName()
/*  43:    */   {
/*  44: 95 */     return this.beanName;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Class getBeanType()
/*  48:    */   {
/*  49:103 */     return this.beanType;
/*  50:    */   }
/*  51:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.NoSuchBeanDefinitionException
 * JD-Core Version:    0.7.0.1
 */