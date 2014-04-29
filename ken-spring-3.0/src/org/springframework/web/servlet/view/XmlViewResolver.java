/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.factory.BeanFactory;
/*   6:    */ import org.springframework.beans.factory.DisposableBean;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*   9:    */ import org.springframework.beans.factory.xml.ResourceEntityResolver;
/*  10:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*  11:    */ import org.springframework.context.ApplicationContext;
/*  12:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  13:    */ import org.springframework.core.Ordered;
/*  14:    */ import org.springframework.core.io.Resource;
/*  15:    */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*  16:    */ import org.springframework.web.servlet.View;
/*  17:    */ 
/*  18:    */ public class XmlViewResolver
/*  19:    */   extends AbstractCachingViewResolver
/*  20:    */   implements Ordered, InitializingBean, DisposableBean
/*  21:    */ {
/*  22:    */   public static final String DEFAULT_LOCATION = "/WEB-INF/views.xml";
/*  23: 61 */   private int order = 2147483647;
/*  24:    */   private Resource location;
/*  25:    */   private ConfigurableApplicationContext cachedFactory;
/*  26:    */   
/*  27:    */   public void setOrder(int order)
/*  28:    */   {
/*  29: 69 */     this.order = order;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getOrder()
/*  33:    */   {
/*  34: 73 */     return this.order;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setLocation(Resource location)
/*  38:    */   {
/*  39: 82 */     this.location = location;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void afterPropertiesSet()
/*  43:    */     throws BeansException
/*  44:    */   {
/*  45: 90 */     if (isCache()) {
/*  46: 91 */       initFactory();
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected Object getCacheKey(String viewName, Locale locale)
/*  51:    */   {
/*  52:102 */     return viewName;
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected View loadView(String viewName, Locale locale)
/*  56:    */     throws BeansException
/*  57:    */   {
/*  58:107 */     BeanFactory factory = initFactory();
/*  59:    */     try
/*  60:    */     {
/*  61:109 */       return (View)factory.getBean(viewName, View.class);
/*  62:    */     }
/*  63:    */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  64:113 */     return null;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected synchronized BeanFactory initFactory()
/*  68:    */     throws BeansException
/*  69:    */   {
/*  70:123 */     if (this.cachedFactory != null) {
/*  71:124 */       return this.cachedFactory;
/*  72:    */     }
/*  73:127 */     Resource actualLocation = this.location;
/*  74:128 */     if (actualLocation == null) {
/*  75:129 */       actualLocation = getApplicationContext().getResource("/WEB-INF/views.xml");
/*  76:    */     }
/*  77:133 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/*  78:134 */     factory.setParent(getApplicationContext());
/*  79:135 */     factory.setServletContext(getServletContext());
/*  80:    */     
/*  81:    */ 
/*  82:138 */     XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
/*  83:139 */     reader.setEnvironment(getApplicationContext().getEnvironment());
/*  84:140 */     reader.setEntityResolver(new ResourceEntityResolver(getApplicationContext()));
/*  85:141 */     reader.loadBeanDefinitions(actualLocation);
/*  86:    */     
/*  87:143 */     factory.refresh();
/*  88:145 */     if (isCache()) {
/*  89:146 */       this.cachedFactory = factory;
/*  90:    */     }
/*  91:148 */     return factory;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void destroy()
/*  95:    */     throws BeansException
/*  96:    */   {
/*  97:156 */     if (this.cachedFactory != null) {
/*  98:157 */       this.cachedFactory.close();
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.XmlViewResolver
 * JD-Core Version:    0.7.0.1
 */