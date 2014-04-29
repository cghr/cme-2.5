/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.InvalidClassException;
/*   4:    */ import java.net.ConnectException;
/*   5:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   6:    */ import org.aopalliance.intercept.MethodInvocation;
/*   7:    */ import org.springframework.aop.support.AopUtils;
/*   8:    */ import org.springframework.remoting.RemoteAccessException;
/*   9:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  10:    */ import org.springframework.remoting.RemoteInvocationFailureException;
/*  11:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  12:    */ import org.springframework.remoting.support.RemoteInvocationBasedAccessor;
/*  13:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  14:    */ 
/*  15:    */ public class HttpInvokerClientInterceptor
/*  16:    */   extends RemoteInvocationBasedAccessor
/*  17:    */   implements MethodInterceptor, HttpInvokerClientConfiguration
/*  18:    */ {
/*  19:    */   private String codebaseUrl;
/*  20:    */   private HttpInvokerRequestExecutor httpInvokerRequestExecutor;
/*  21:    */   
/*  22:    */   public void setCodebaseUrl(String codebaseUrl)
/*  23:    */   {
/*  24: 88 */     this.codebaseUrl = codebaseUrl;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String getCodebaseUrl()
/*  28:    */   {
/*  29: 95 */     return this.codebaseUrl;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setHttpInvokerRequestExecutor(HttpInvokerRequestExecutor httpInvokerRequestExecutor)
/*  33:    */   {
/*  34:108 */     this.httpInvokerRequestExecutor = httpInvokerRequestExecutor;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public HttpInvokerRequestExecutor getHttpInvokerRequestExecutor()
/*  38:    */   {
/*  39:117 */     if (this.httpInvokerRequestExecutor == null)
/*  40:    */     {
/*  41:118 */       SimpleHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
/*  42:119 */       executor.setBeanClassLoader(getBeanClassLoader());
/*  43:120 */       this.httpInvokerRequestExecutor = executor;
/*  44:    */     }
/*  45:122 */     return this.httpInvokerRequestExecutor;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void afterPropertiesSet()
/*  49:    */   {
/*  50:127 */     super.afterPropertiesSet();
/*  51:    */     
/*  52:    */ 
/*  53:130 */     getHttpInvokerRequestExecutor();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Object invoke(MethodInvocation methodInvocation)
/*  57:    */     throws Throwable
/*  58:    */   {
/*  59:135 */     if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
/*  60:136 */       return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
/*  61:    */     }
/*  62:139 */     RemoteInvocation invocation = createRemoteInvocation(methodInvocation);
/*  63:140 */     RemoteInvocationResult result = null;
/*  64:    */     try
/*  65:    */     {
/*  66:142 */       result = executeRequest(invocation, methodInvocation);
/*  67:    */     }
/*  68:    */     catch (Throwable ex)
/*  69:    */     {
/*  70:145 */       throw convertHttpInvokerAccessException(ex);
/*  71:    */     }
/*  72:    */     try
/*  73:    */     {
/*  74:148 */       return recreateRemoteInvocationResult(result);
/*  75:    */     }
/*  76:    */     catch (Throwable ex)
/*  77:    */     {
/*  78:151 */       if (result.hasInvocationTargetException()) {
/*  79:152 */         throw ex;
/*  80:    */       }
/*  81:155 */       throw new RemoteInvocationFailureException("Invocation of method [" + methodInvocation.getMethod() + 
/*  82:156 */         "] failed in HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation, MethodInvocation originalInvocation)
/*  87:    */     throws Exception
/*  88:    */   {
/*  89:174 */     return executeRequest(invocation);
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected RemoteInvocationResult executeRequest(RemoteInvocation invocation)
/*  93:    */     throws Exception
/*  94:    */   {
/*  95:192 */     return getHttpInvokerRequestExecutor().executeRequest(this, invocation);
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected RemoteAccessException convertHttpInvokerAccessException(Throwable ex)
/*  99:    */   {
/* 100:202 */     if ((ex instanceof ConnectException)) {
/* 101:203 */       throw new RemoteConnectFailureException(
/* 102:204 */         "Could not connect to HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
/* 103:    */     }
/* 104:206 */     if (((ex instanceof ClassNotFoundException)) || ((ex instanceof NoClassDefFoundError)) || 
/* 105:207 */       ((ex instanceof InvalidClassException))) {
/* 106:208 */       throw new RemoteAccessException(
/* 107:209 */         "Could not deserialize result from HTTP invoker remote service [" + getServiceUrl() + "]", ex);
/* 108:    */     }
/* 109:212 */     throw new RemoteAccessException(
/* 110:213 */       "Could not access HTTP invoker remote service at [" + getServiceUrl() + "]", ex);
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor
 * JD-Core Version:    0.7.0.1
 */