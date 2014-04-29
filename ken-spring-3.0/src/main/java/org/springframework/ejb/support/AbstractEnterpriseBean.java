/*   1:    */ package org.springframework.ejb.support;
/*   2:    */ 
/*   3:    */ import javax.ejb.EnterpriseBean;
/*   4:    */ import org.springframework.beans.BeansException;
/*   5:    */ import org.springframework.beans.FatalBeanException;
/*   6:    */ import org.springframework.beans.factory.BeanFactory;
/*   7:    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*   8:    */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*   9:    */ import org.springframework.context.access.ContextJndiBeanFactoryLocator;
/*  10:    */ import org.springframework.util.WeakReferenceMonitor;
/*  11:    */ import org.springframework.util.WeakReferenceMonitor.ReleaseListener;
/*  12:    */ 
/*  13:    */ public abstract class AbstractEnterpriseBean
/*  14:    */   implements EnterpriseBean
/*  15:    */ {
/*  16:    */   public static final String BEAN_FACTORY_PATH_ENVIRONMENT_KEY = "java:comp/env/ejb/BeanFactoryPath";
/*  17:    */   private BeanFactoryLocator beanFactoryLocator;
/*  18:    */   private String beanFactoryLocatorKey;
/*  19:    */   private BeanFactoryReference beanFactoryReference;
/*  20:    */   
/*  21:    */   public void setBeanFactoryLocator(BeanFactoryLocator beanFactoryLocator)
/*  22:    */   {
/*  23: 88 */     this.beanFactoryLocator = beanFactoryLocator;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setBeanFactoryLocatorKey(String factoryKey)
/*  27:    */   {
/*  28:101 */     this.beanFactoryLocatorKey = factoryKey;
/*  29:    */   }
/*  30:    */   
/*  31:    */   void loadBeanFactory()
/*  32:    */     throws BeansException
/*  33:    */   {
/*  34:111 */     if (this.beanFactoryLocator == null) {
/*  35:112 */       this.beanFactoryLocator = new ContextJndiBeanFactoryLocator();
/*  36:    */     }
/*  37:114 */     if (this.beanFactoryLocatorKey == null) {
/*  38:115 */       this.beanFactoryLocatorKey = "java:comp/env/ejb/BeanFactoryPath";
/*  39:    */     }
/*  40:118 */     this.beanFactoryReference = this.beanFactoryLocator.useBeanFactory(this.beanFactoryLocatorKey);
/*  41:    */     
/*  42:    */ 
/*  43:    */ 
/*  44:    */ 
/*  45:123 */     WeakReferenceMonitor.monitor(this, new BeanFactoryReferenceReleaseListener(this.beanFactoryReference));
/*  46:    */   }
/*  47:    */   
/*  48:    */   void unloadBeanFactory()
/*  49:    */     throws FatalBeanException
/*  50:    */   {
/*  51:136 */     if (this.beanFactoryReference != null)
/*  52:    */     {
/*  53:137 */       this.beanFactoryReference.release();
/*  54:138 */       this.beanFactoryReference = null;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected BeanFactory getBeanFactory()
/*  59:    */   {
/*  60:147 */     return this.beanFactoryReference.getFactory();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void ejbRemove()
/*  64:    */   {
/*  65:157 */     onEjbRemove();
/*  66:158 */     unloadBeanFactory();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void onEjbRemove() {}
/*  70:    */   
/*  71:    */   private static class BeanFactoryReferenceReleaseListener
/*  72:    */     implements WeakReferenceMonitor.ReleaseListener
/*  73:    */   {
/*  74:    */     private final BeanFactoryReference beanFactoryReference;
/*  75:    */     
/*  76:    */     public BeanFactoryReferenceReleaseListener(BeanFactoryReference beanFactoryReference)
/*  77:    */     {
/*  78:184 */       this.beanFactoryReference = beanFactoryReference;
/*  79:    */     }
/*  80:    */     
/*  81:    */     public void released()
/*  82:    */     {
/*  83:188 */       this.beanFactoryReference.release();
/*  84:    */     }
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.AbstractEnterpriseBean
 * JD-Core Version:    0.7.0.1
 */