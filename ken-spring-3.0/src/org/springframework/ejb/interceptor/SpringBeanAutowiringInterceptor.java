/*   1:    */ package org.springframework.ejb.interceptor;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import java.util.WeakHashMap;
/*   5:    */ import javax.annotation.PostConstruct;
/*   6:    */ import javax.annotation.PreDestroy;
/*   7:    */ import javax.ejb.EJBException;
/*   8:    */ import javax.ejb.PostActivate;
/*   9:    */ import javax.ejb.PrePassivate;
/*  10:    */ import javax.interceptor.InvocationContext;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*  13:    */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*  14:    */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*  15:    */ import org.springframework.context.ApplicationContext;
/*  16:    */ import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
/*  17:    */ 
/*  18:    */ public class SpringBeanAutowiringInterceptor
/*  19:    */ {
/*  20: 87 */   private final Map<Object, BeanFactoryReference> beanFactoryReferences = new WeakHashMap();
/*  21:    */   
/*  22:    */   @PostConstruct
/*  23:    */   @PostActivate
/*  24:    */   public void autowireBean(InvocationContext invocationContext)
/*  25:    */   {
/*  26: 97 */     doAutowireBean(invocationContext.getTarget());
/*  27:    */     try
/*  28:    */     {
/*  29: 99 */       invocationContext.proceed();
/*  30:    */     }
/*  31:    */     catch (RuntimeException ex)
/*  32:    */     {
/*  33:102 */       throw ex;
/*  34:    */     }
/*  35:    */     catch (Exception ex)
/*  36:    */     {
/*  37:106 */       throw new EJBException(ex);
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected void doAutowireBean(Object target)
/*  42:    */   {
/*  43:115 */     AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
/*  44:116 */     configureBeanPostProcessor(bpp, target);
/*  45:117 */     bpp.setBeanFactory(getBeanFactory(target));
/*  46:118 */     bpp.processInjection(target);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected void configureBeanPostProcessor(AutowiredAnnotationBeanPostProcessor processor, Object target) {}
/*  50:    */   
/*  51:    */   protected BeanFactory getBeanFactory(Object target)
/*  52:    */   {
/*  53:137 */     BeanFactory factory = getBeanFactoryReference(target).getFactory();
/*  54:138 */     if ((factory instanceof ApplicationContext)) {
/*  55:139 */       factory = ((ApplicationContext)factory).getAutowireCapableBeanFactory();
/*  56:    */     }
/*  57:141 */     return factory;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected BeanFactoryReference getBeanFactoryReference(Object target)
/*  61:    */   {
/*  62:155 */     String key = getBeanFactoryLocatorKey(target);
/*  63:156 */     BeanFactoryReference ref = getBeanFactoryLocator(target).useBeanFactory(key);
/*  64:157 */     this.beanFactoryReferences.put(target, ref);
/*  65:158 */     return ref;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected BeanFactoryLocator getBeanFactoryLocator(Object target)
/*  69:    */   {
/*  70:170 */     return ContextSingletonBeanFactoryLocator.getInstance();
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected String getBeanFactoryLocatorKey(Object target)
/*  74:    */   {
/*  75:185 */     return null;
/*  76:    */   }
/*  77:    */   
/*  78:    */   @PreDestroy
/*  79:    */   @PrePassivate
/*  80:    */   public void releaseBean(InvocationContext invocationContext)
/*  81:    */   {
/*  82:196 */     doReleaseBean(invocationContext.getTarget());
/*  83:    */     try
/*  84:    */     {
/*  85:198 */       invocationContext.proceed();
/*  86:    */     }
/*  87:    */     catch (RuntimeException ex)
/*  88:    */     {
/*  89:201 */       throw ex;
/*  90:    */     }
/*  91:    */     catch (Exception ex)
/*  92:    */     {
/*  93:205 */       throw new EJBException(ex);
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void doReleaseBean(Object target)
/*  98:    */   {
/*  99:214 */     BeanFactoryReference ref = (BeanFactoryReference)this.beanFactoryReferences.remove(target);
/* 100:215 */     if (ref != null) {
/* 101:216 */       ref.release();
/* 102:    */     }
/* 103:    */   }
/* 104:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor
 * JD-Core Version:    0.7.0.1
 */