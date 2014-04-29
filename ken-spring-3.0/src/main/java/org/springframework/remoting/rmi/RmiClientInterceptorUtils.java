/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.rmi.ConnectException;
/*   6:    */ import java.rmi.ConnectIOException;
/*   7:    */ import java.rmi.NoSuchObjectException;
/*   8:    */ import java.rmi.Remote;
/*   9:    */ import java.rmi.RemoteException;
/*  10:    */ import java.rmi.StubNotFoundException;
/*  11:    */ import java.rmi.UnknownHostException;
/*  12:    */ import org.aopalliance.intercept.MethodInvocation;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.apache.commons.logging.LogFactory;
/*  15:    */ import org.omg.CORBA.COMM_FAILURE;
/*  16:    */ import org.omg.CORBA.CompletionStatus;
/*  17:    */ import org.omg.CORBA.NO_RESPONSE;
/*  18:    */ import org.omg.CORBA.SystemException;
/*  19:    */ import org.springframework.remoting.RemoteAccessException;
/*  20:    */ import org.springframework.remoting.RemoteConnectFailureException;
/*  21:    */ import org.springframework.remoting.RemoteProxyFailureException;
/*  22:    */ import org.springframework.util.ReflectionUtils;
/*  23:    */ 
/*  24:    */ public abstract class RmiClientInterceptorUtils
/*  25:    */ {
/*  26:    */   private static final String ORACLE_CONNECTION_EXCEPTION = "com.evermind.server.rmi.RMIConnectionException";
/*  27: 55 */   private static final Log logger = LogFactory.getLog(RmiClientInterceptorUtils.class);
/*  28:    */   
/*  29:    */   @Deprecated
/*  30:    */   public static Object invoke(MethodInvocation invocation, Remote stub, String serviceName)
/*  31:    */     throws Throwable
/*  32:    */   {
/*  33:    */     try
/*  34:    */     {
/*  35: 71 */       return invokeRemoteMethod(invocation, stub);
/*  36:    */     }
/*  37:    */     catch (InvocationTargetException ex)
/*  38:    */     {
/*  39: 74 */       Throwable targetEx = ex.getTargetException();
/*  40: 75 */       if ((targetEx instanceof RemoteException))
/*  41:    */       {
/*  42: 76 */         RemoteException rex = (RemoteException)targetEx;
/*  43: 77 */         throw convertRmiAccessException(invocation.getMethod(), rex, serviceName);
/*  44:    */       }
/*  45: 80 */       throw targetEx;
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   @Deprecated
/*  50:    */   public static Object doInvoke(MethodInvocation invocation, Remote stub)
/*  51:    */     throws InvocationTargetException
/*  52:    */   {
/*  53: 92 */     return invokeRemoteMethod(invocation, stub);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static Object invokeRemoteMethod(MethodInvocation invocation, Object stub)
/*  57:    */     throws InvocationTargetException
/*  58:    */   {
/*  59:106 */     Method method = invocation.getMethod();
/*  60:    */     try
/*  61:    */     {
/*  62:108 */       if (method.getDeclaringClass().isInstance(stub)) {
/*  63:110 */         return method.invoke(stub, invocation.getArguments());
/*  64:    */       }
/*  65:114 */       Method stubMethod = stub.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  66:115 */       return stubMethod.invoke(stub, invocation.getArguments());
/*  67:    */     }
/*  68:    */     catch (InvocationTargetException ex)
/*  69:    */     {
/*  70:119 */       throw ex;
/*  71:    */     }
/*  72:    */     catch (NoSuchMethodException ex)
/*  73:    */     {
/*  74:122 */       throw new RemoteProxyFailureException("No matching RMI stub method found for: " + method, ex);
/*  75:    */     }
/*  76:    */     catch (Throwable ex)
/*  77:    */     {
/*  78:125 */       throw new RemoteProxyFailureException("Invocation of RMI stub method failed: " + method, ex);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static Exception convertRmiAccessException(Method method, Throwable ex, String message)
/*  83:    */   {
/*  84:143 */     if (logger.isDebugEnabled()) {
/*  85:144 */       logger.debug(message, ex);
/*  86:    */     }
/*  87:146 */     if (ReflectionUtils.declaresException(method, RemoteException.class)) {
/*  88:147 */       return new RemoteException(message, ex);
/*  89:    */     }
/*  90:150 */     return new RemoteAccessException(message, ex);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static Exception convertRmiAccessException(Method method, RemoteException ex, String serviceName)
/*  94:    */   {
/*  95:164 */     return convertRmiAccessException(method, ex, isConnectFailure(ex), serviceName);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static Exception convertRmiAccessException(Method method, RemoteException ex, boolean isConnectFailure, String serviceName)
/*  99:    */   {
/* 100:181 */     if (logger.isDebugEnabled()) {
/* 101:182 */       logger.debug("Remote service [" + serviceName + "] threw exception", ex);
/* 102:    */     }
/* 103:184 */     if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 104:185 */       return ex;
/* 105:    */     }
/* 106:188 */     if (isConnectFailure) {
/* 107:189 */       return new RemoteConnectFailureException("Could not connect to remote service [" + serviceName + "]", ex);
/* 108:    */     }
/* 109:192 */     return new RemoteAccessException("Could not access remote service [" + serviceName + "]", ex);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static boolean isConnectFailure(RemoteException ex)
/* 113:    */   {
/* 114:215 */     return ((ex instanceof ConnectException)) || ((ex instanceof ConnectIOException)) || ((ex instanceof UnknownHostException)) || ((ex instanceof NoSuchObjectException)) || ((ex instanceof StubNotFoundException)) || (isCorbaConnectFailure(ex.getCause())) || ("com.evermind.server.rmi.RMIConnectionException".equals(ex.getClass().getName()));
/* 115:    */   }
/* 116:    */   
/* 117:    */   private static boolean isCorbaConnectFailure(Throwable ex)
/* 118:    */   {
/* 119:229 */     return (((ex instanceof COMM_FAILURE)) || ((ex instanceof NO_RESPONSE))) && (((SystemException)ex).completed == CompletionStatus.COMPLETED_NO);
/* 120:    */   }
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiClientInterceptorUtils
 * JD-Core Version:    0.7.0.1
 */