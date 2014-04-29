/*   1:    */ package org.springframework.jca.endpoint;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.spi.UnavailableException;
/*   5:    */ import javax.resource.spi.endpoint.MessageEndpoint;
/*   6:    */ import javax.transaction.xa.XAResource;
/*   7:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   8:    */ import org.aopalliance.intercept.MethodInvocation;
/*   9:    */ import org.springframework.aop.framework.ProxyFactory;
/*  10:    */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ 
/*  13:    */ public class GenericMessageEndpointFactory
/*  14:    */   extends AbstractMessageEndpointFactory
/*  15:    */ {
/*  16:    */   private Object messageListener;
/*  17:    */   
/*  18:    */   public void setMessageListener(Object messageListener)
/*  19:    */   {
/*  20: 64 */     this.messageListener = messageListener;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public MessageEndpoint createEndpoint(XAResource xaResource)
/*  24:    */     throws UnavailableException
/*  25:    */   {
/*  26: 74 */     GenericMessageEndpoint endpoint = (GenericMessageEndpoint)super.createEndpoint(xaResource);
/*  27: 75 */     ProxyFactory proxyFactory = new ProxyFactory(this.messageListener);
/*  28: 76 */     DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(endpoint);
/*  29: 77 */     introduction.suppressInterface(MethodInterceptor.class);
/*  30: 78 */     proxyFactory.addAdvice(introduction);
/*  31: 79 */     return (MessageEndpoint)proxyFactory.getProxy();
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected AbstractMessageEndpointFactory.AbstractMessageEndpoint createEndpointInternal()
/*  35:    */     throws UnavailableException
/*  36:    */   {
/*  37: 87 */     return new GenericMessageEndpoint(null);
/*  38:    */   }
/*  39:    */   
/*  40:    */   private class GenericMessageEndpoint
/*  41:    */     extends AbstractMessageEndpointFactory.AbstractMessageEndpoint
/*  42:    */     implements MethodInterceptor
/*  43:    */   {
/*  44:    */     private GenericMessageEndpoint()
/*  45:    */     {
/*  46: 95 */       super();
/*  47:    */     }
/*  48:    */     
/*  49:    */     public Object invoke(MethodInvocation methodInvocation)
/*  50:    */       throws Throwable
/*  51:    */     {
/*  52: 98 */       boolean applyDeliveryCalls = !hasBeforeDeliveryBeenCalled();
/*  53: 99 */       if (applyDeliveryCalls) {
/*  54:    */         try
/*  55:    */         {
/*  56:101 */           beforeDelivery(null);
/*  57:    */         }
/*  58:    */         catch (ResourceException ex)
/*  59:    */         {
/*  60:104 */           if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
/*  61:105 */             throw ex;
/*  62:    */           }
/*  63:108 */           throw new GenericMessageEndpointFactory.InternalResourceException(ex);
/*  64:    */         }
/*  65:    */       }
/*  66:    */       try
/*  67:    */       {
/*  68:113 */         return methodInvocation.proceed();
/*  69:    */       }
/*  70:    */       catch (Throwable ex)
/*  71:    */       {
/*  72:116 */         onEndpointException(ex);
/*  73:117 */         throw ex;
/*  74:    */       }
/*  75:    */       finally
/*  76:    */       {
/*  77:120 */         if (applyDeliveryCalls) {
/*  78:    */           try
/*  79:    */           {
/*  80:122 */             afterDelivery();
/*  81:    */           }
/*  82:    */           catch (ResourceException ex)
/*  83:    */           {
/*  84:125 */             if (ReflectionUtils.declaresException(methodInvocation.getMethod(), ex.getClass())) {
/*  85:126 */               throw ex;
/*  86:    */             }
/*  87:129 */             throw new GenericMessageEndpointFactory.InternalResourceException(ex);
/*  88:    */           }
/*  89:    */         }
/*  90:    */       }
/*  91:    */     }
/*  92:    */     
/*  93:    */     protected ClassLoader getEndpointClassLoader()
/*  94:    */     {
/*  95:138 */       return GenericMessageEndpointFactory.this.messageListener.getClass().getClassLoader();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public static class InternalResourceException
/* 100:    */     extends RuntimeException
/* 101:    */   {
/* 102:    */     protected InternalResourceException(ResourceException cause)
/* 103:    */     {
/* 104:154 */       super();
/* 105:    */     }
/* 106:    */   }
/* 107:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.endpoint.GenericMessageEndpointFactory
 * JD-Core Version:    0.7.0.1
 */