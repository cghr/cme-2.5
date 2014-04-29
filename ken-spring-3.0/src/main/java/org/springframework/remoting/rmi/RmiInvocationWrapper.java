/*  1:   */ package org.springframework.remoting.rmi;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import java.rmi.RemoteException;
/*  5:   */ import org.springframework.remoting.support.RemoteInvocation;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ class RmiInvocationWrapper
/*  9:   */   implements RmiInvocationHandler
/* 10:   */ {
/* 11:   */   private final Object wrappedObject;
/* 12:   */   private final RmiBasedExporter rmiExporter;
/* 13:   */   
/* 14:   */   public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter)
/* 15:   */   {
/* 16:49 */     Assert.notNull(wrappedObject, "Object to wrap is required");
/* 17:50 */     Assert.notNull(rmiExporter, "RMI exporter is required");
/* 18:51 */     this.wrappedObject = wrappedObject;
/* 19:52 */     this.rmiExporter = rmiExporter;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public String getTargetInterfaceName()
/* 23:   */   {
/* 24:61 */     Class ifc = this.rmiExporter.getServiceInterface();
/* 25:62 */     return ifc != null ? ifc.getName() : null;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Object invoke(RemoteInvocation invocation)
/* 29:   */     throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/* 30:   */   {
/* 31:72 */     return this.rmiExporter.invoke(invocation, this.wrappedObject);
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiInvocationWrapper
 * JD-Core Version:    0.7.0.1
 */