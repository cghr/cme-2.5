/*   1:    */ package org.springframework.remoting.support;
/*   2:    */ 
/*   3:    */ import org.springframework.aop.framework.ProxyFactory;
/*   4:    */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*   5:    */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*   6:    */ import org.springframework.util.ClassUtils;
/*   7:    */ 
/*   8:    */ public abstract class RemoteExporter
/*   9:    */   extends RemotingSupport
/*  10:    */ {
/*  11:    */   private Object service;
/*  12:    */   private Class serviceInterface;
/*  13:    */   private Boolean registerTraceInterceptor;
/*  14:    */   private Object[] interceptors;
/*  15:    */   
/*  16:    */   public void setService(Object service)
/*  17:    */   {
/*  18: 51 */     this.service = service;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public Object getService()
/*  22:    */   {
/*  23: 58 */     return this.service;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setServiceInterface(Class serviceInterface)
/*  27:    */   {
/*  28: 66 */     if ((serviceInterface != null) && (!serviceInterface.isInterface())) {
/*  29: 67 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/*  30:    */     }
/*  31: 69 */     this.serviceInterface = serviceInterface;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Class getServiceInterface()
/*  35:    */   {
/*  36: 76 */     return this.serviceInterface;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setRegisterTraceInterceptor(boolean registerTraceInterceptor)
/*  40:    */   {
/*  41: 92 */     this.registerTraceInterceptor = Boolean.valueOf(registerTraceInterceptor);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setInterceptors(Object[] interceptors)
/*  45:    */   {
/*  46:104 */     this.interceptors = interceptors;
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected void checkService()
/*  50:    */     throws IllegalArgumentException
/*  51:    */   {
/*  52:113 */     if (getService() == null) {
/*  53:114 */       throw new IllegalArgumentException("Property 'service' is required");
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected void checkServiceInterface()
/*  58:    */     throws IllegalArgumentException
/*  59:    */   {
/*  60:125 */     Class serviceInterface = getServiceInterface();
/*  61:126 */     Object service = getService();
/*  62:127 */     if (serviceInterface == null) {
/*  63:128 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*  64:    */     }
/*  65:130 */     if ((service instanceof String)) {
/*  66:131 */       throw new IllegalArgumentException("Service [" + service + "] is a String " + 
/*  67:132 */         "rather than an actual service reference: Have you accidentally specified " + 
/*  68:133 */         "the service bean name as value instead of as reference?");
/*  69:    */     }
/*  70:135 */     if (!serviceInterface.isInstance(service)) {
/*  71:136 */       throw new IllegalArgumentException("Service interface [" + serviceInterface.getName() + 
/*  72:137 */         "] needs to be implemented by service [" + service + "] of class [" + 
/*  73:138 */         service.getClass().getName() + "]");
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected Object getProxyForService()
/*  78:    */   {
/*  79:154 */     checkService();
/*  80:155 */     checkServiceInterface();
/*  81:156 */     ProxyFactory proxyFactory = new ProxyFactory();
/*  82:157 */     proxyFactory.addInterface(getServiceInterface());
/*  83:158 */     if (this.registerTraceInterceptor != null ? 
/*  84:159 */       this.registerTraceInterceptor.booleanValue() : this.interceptors == null) {
/*  85:160 */       proxyFactory.addAdvice(new RemoteInvocationTraceInterceptor(getExporterName()));
/*  86:    */     }
/*  87:162 */     if (this.interceptors != null)
/*  88:    */     {
/*  89:163 */       AdvisorAdapterRegistry adapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*  90:164 */       for (int i = 0; i < this.interceptors.length; i++) {
/*  91:165 */         proxyFactory.addAdvisor(adapterRegistry.wrap(this.interceptors[i]));
/*  92:    */       }
/*  93:    */     }
/*  94:168 */     proxyFactory.setTarget(getService());
/*  95:169 */     proxyFactory.setOpaque(true);
/*  96:170 */     return proxyFactory.getProxy(getBeanClassLoader());
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected String getExporterName()
/* 100:    */   {
/* 101:183 */     return ClassUtils.getShortName(getClass());
/* 102:    */   }
/* 103:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteExporter
 * JD-Core Version:    0.7.0.1
 */