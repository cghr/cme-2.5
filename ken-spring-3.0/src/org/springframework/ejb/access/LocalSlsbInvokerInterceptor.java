/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import javax.ejb.CreateException;
/*   6:    */ import javax.ejb.EJBLocalHome;
/*   7:    */ import javax.ejb.EJBLocalObject;
/*   8:    */ import javax.naming.NamingException;
/*   9:    */ import org.aopalliance.intercept.MethodInvocation;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ 
/*  12:    */ public class LocalSlsbInvokerInterceptor
/*  13:    */   extends AbstractSlsbInvokerInterceptor
/*  14:    */ {
/*  15: 53 */   private volatile boolean homeAsComponent = false;
/*  16:    */   
/*  17:    */   public Object invokeInContext(MethodInvocation invocation)
/*  18:    */     throws Throwable
/*  19:    */   {
/*  20: 65 */     Object ejb = null;
/*  21:    */     try
/*  22:    */     {
/*  23: 67 */       ejb = getSessionBeanInstance();
/*  24: 68 */       Method method = invocation.getMethod();
/*  25:    */       Object localObject2;
/*  26: 69 */       if (method.getDeclaringClass().isInstance(ejb)) {
/*  27: 71 */         return method.invoke(ejb, invocation.getArguments());
/*  28:    */       }
/*  29:    */       Method method;
/*  30: 75 */       Method ejbMethod = ejb.getClass().getMethod(method.getName(), method.getParameterTypes());
/*  31: 76 */       return ejbMethod.invoke(ejb, invocation.getArguments());
/*  32:    */     }
/*  33:    */     catch (InvocationTargetException ex)
/*  34:    */     {
/*  35: 80 */       Throwable targetEx = ex.getTargetException();
/*  36: 81 */       if (this.logger.isDebugEnabled()) {
/*  37: 82 */         this.logger.debug("Method of local EJB [" + getJndiName() + "] threw exception", targetEx);
/*  38:    */       }
/*  39: 84 */       if ((targetEx instanceof CreateException)) {
/*  40: 85 */         throw new EjbAccessException("Could not create local EJB [" + getJndiName() + "]", targetEx);
/*  41:    */       }
/*  42: 88 */       throw targetEx;
/*  43:    */     }
/*  44:    */     catch (NamingException ex)
/*  45:    */     {
/*  46: 92 */       throw new EjbAccessException("Failed to locate local EJB [" + getJndiName() + "]", ex);
/*  47:    */     }
/*  48:    */     catch (IllegalAccessException ex)
/*  49:    */     {
/*  50: 95 */       throw new EjbAccessException("Could not access method [" + invocation.getMethod().getName() + 
/*  51: 96 */         "] of local EJB [" + getJndiName() + "]", ex);
/*  52:    */     }
/*  53:    */     finally
/*  54:    */     {
/*  55: 99 */       if ((ejb instanceof EJBLocalObject)) {
/*  56:100 */         releaseSessionBeanInstance((EJBLocalObject)ejb);
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected Method getCreateMethod(Object home)
/*  62:    */     throws EjbAccessException
/*  63:    */   {
/*  64:110 */     if (this.homeAsComponent) {
/*  65:111 */       return null;
/*  66:    */     }
/*  67:113 */     if (!(home instanceof EJBLocalHome))
/*  68:    */     {
/*  69:115 */       this.homeAsComponent = true;
/*  70:116 */       return null;
/*  71:    */     }
/*  72:118 */     return super.getCreateMethod(home);
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected Object getSessionBeanInstance()
/*  76:    */     throws NamingException, InvocationTargetException
/*  77:    */   {
/*  78:129 */     return newSessionBeanInstance();
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected void releaseSessionBeanInstance(EJBLocalObject ejb)
/*  82:    */   {
/*  83:139 */     removeSessionBeanInstance(ejb);
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected Object newSessionBeanInstance()
/*  87:    */     throws NamingException, InvocationTargetException
/*  88:    */   {
/*  89:150 */     if (this.logger.isDebugEnabled()) {
/*  90:151 */       this.logger.debug("Trying to create reference to local EJB");
/*  91:    */     }
/*  92:153 */     Object ejbInstance = create();
/*  93:154 */     if (this.logger.isDebugEnabled()) {
/*  94:155 */       this.logger.debug("Obtained reference to local EJB: " + ejbInstance);
/*  95:    */     }
/*  96:157 */     return ejbInstance;
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void removeSessionBeanInstance(EJBLocalObject ejb)
/* 100:    */   {
/* 101:166 */     if ((ejb != null) && (!this.homeAsComponent)) {
/* 102:    */       try
/* 103:    */       {
/* 104:168 */         ejb.remove();
/* 105:    */       }
/* 106:    */       catch (Throwable ex)
/* 107:    */       {
/* 108:171 */         this.logger.warn("Could not invoke 'remove' on local EJB proxy", ex);
/* 109:    */       }
/* 110:    */     }
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.LocalSlsbInvokerInterceptor
 * JD-Core Version:    0.7.0.1
 */