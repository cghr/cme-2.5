/*   1:    */ package org.springframework.jca.support;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.spi.BootstrapContext;
/*   5:    */ import javax.resource.spi.ResourceAdapter;
/*   6:    */ import javax.resource.spi.XATerminator;
/*   7:    */ import javax.resource.spi.work.WorkManager;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.beans.factory.DisposableBean;
/*  10:    */ import org.springframework.beans.factory.FactoryBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ 
/*  14:    */ public class ResourceAdapterFactoryBean
/*  15:    */   implements FactoryBean<ResourceAdapter>, InitializingBean, DisposableBean
/*  16:    */ {
/*  17:    */   private ResourceAdapter resourceAdapter;
/*  18:    */   private BootstrapContext bootstrapContext;
/*  19:    */   private WorkManager workManager;
/*  20:    */   private XATerminator xaTerminator;
/*  21:    */   
/*  22:    */   public void setResourceAdapterClass(Class resourceAdapterClass)
/*  23:    */   {
/*  24: 70 */     Assert.isAssignable(ResourceAdapter.class, resourceAdapterClass);
/*  25: 71 */     this.resourceAdapter = ((ResourceAdapter)BeanUtils.instantiateClass(resourceAdapterClass));
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setResourceAdapter(ResourceAdapter resourceAdapter)
/*  29:    */   {
/*  30: 81 */     this.resourceAdapter = resourceAdapter;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setBootstrapContext(BootstrapContext bootstrapContext)
/*  34:    */   {
/*  35: 92 */     this.bootstrapContext = bootstrapContext;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setWorkManager(WorkManager workManager)
/*  39:    */   {
/*  40:100 */     this.workManager = workManager;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setXaTerminator(XATerminator xaTerminator)
/*  44:    */   {
/*  45:108 */     this.xaTerminator = xaTerminator;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void afterPropertiesSet()
/*  49:    */     throws ResourceException
/*  50:    */   {
/*  51:117 */     if (this.resourceAdapter == null) {
/*  52:118 */       throw new IllegalArgumentException("'resourceAdapter' or 'resourceAdapterClass' is required");
/*  53:    */     }
/*  54:120 */     if (this.bootstrapContext == null) {
/*  55:121 */       this.bootstrapContext = new SimpleBootstrapContext(this.workManager, this.xaTerminator);
/*  56:    */     }
/*  57:123 */     this.resourceAdapter.start(this.bootstrapContext);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public ResourceAdapter getObject()
/*  61:    */   {
/*  62:128 */     return this.resourceAdapter;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Class<? extends ResourceAdapter> getObjectType()
/*  66:    */   {
/*  67:132 */     return this.resourceAdapter != null ? this.resourceAdapter.getClass() : ResourceAdapter.class;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public boolean isSingleton()
/*  71:    */   {
/*  72:136 */     return true;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void destroy()
/*  76:    */   {
/*  77:145 */     this.resourceAdapter.stop();
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.support.ResourceAdapterFactoryBean
 * JD-Core Version:    0.7.0.1
 */