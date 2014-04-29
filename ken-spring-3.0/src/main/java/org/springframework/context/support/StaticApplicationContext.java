/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.MutablePropertyValues;
/*   6:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   7:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   8:    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ 
/*  11:    */ public class StaticApplicationContext
/*  12:    */   extends GenericApplicationContext
/*  13:    */ {
/*  14:    */   private final StaticMessageSource staticMessageSource;
/*  15:    */   
/*  16:    */   public StaticApplicationContext()
/*  17:    */     throws BeansException
/*  18:    */   {
/*  19: 52 */     this(null);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public StaticApplicationContext(ApplicationContext parent)
/*  23:    */     throws BeansException
/*  24:    */   {
/*  25: 63 */     super(parent);
/*  26:    */     
/*  27:    */ 
/*  28: 66 */     this.staticMessageSource = new StaticMessageSource();
/*  29: 67 */     getBeanFactory().registerSingleton("messageSource", this.staticMessageSource);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final StaticMessageSource getStaticMessageSource()
/*  33:    */   {
/*  34: 77 */     return this.staticMessageSource;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void registerSingleton(String name, Class clazz)
/*  38:    */     throws BeansException
/*  39:    */   {
/*  40: 87 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  41: 88 */     bd.setBeanClass(clazz);
/*  42: 89 */     getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void registerSingleton(String name, Class clazz, MutablePropertyValues pvs)
/*  46:    */     throws BeansException
/*  47:    */   {
/*  48: 98 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  49: 99 */     bd.setBeanClass(clazz);
/*  50:100 */     bd.setPropertyValues(pvs);
/*  51:101 */     getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void registerPrototype(String name, Class clazz)
/*  55:    */     throws BeansException
/*  56:    */   {
/*  57:110 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  58:111 */     bd.setScope("prototype");
/*  59:112 */     bd.setBeanClass(clazz);
/*  60:113 */     getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void registerPrototype(String name, Class clazz, MutablePropertyValues pvs)
/*  64:    */     throws BeansException
/*  65:    */   {
/*  66:122 */     GenericBeanDefinition bd = new GenericBeanDefinition();
/*  67:123 */     bd.setScope("prototype");
/*  68:124 */     bd.setBeanClass(clazz);
/*  69:125 */     bd.setPropertyValues(pvs);
/*  70:126 */     getDefaultListableBeanFactory().registerBeanDefinition(name, bd);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void addMessage(String code, Locale locale, String defaultMessage)
/*  74:    */   {
/*  75:137 */     getStaticMessageSource().addMessage(code, locale, defaultMessage);
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.StaticApplicationContext
 * JD-Core Version:    0.7.0.1
 */