/*   1:    */ package org.springframework.ejb.access;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import javax.naming.Context;
/*   6:    */ import javax.naming.NamingException;
/*   7:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   8:    */ import org.aopalliance.intercept.MethodInvocation;
/*   9:    */ import org.springframework.jndi.JndiObjectLocator;
/*  10:    */ import org.springframework.jndi.JndiTemplate;
/*  11:    */ 
/*  12:    */ public abstract class AbstractSlsbInvokerInterceptor
/*  13:    */   extends JndiObjectLocator
/*  14:    */   implements MethodInterceptor
/*  15:    */ {
/*  16: 44 */   private boolean lookupHomeOnStartup = true;
/*  17: 46 */   private boolean cacheHome = true;
/*  18: 48 */   private boolean exposeAccessContext = false;
/*  19:    */   private Object cachedHome;
/*  20:    */   private Method createMethod;
/*  21: 61 */   private final Object homeMonitor = new Object();
/*  22:    */   
/*  23:    */   public void setLookupHomeOnStartup(boolean lookupHomeOnStartup)
/*  24:    */   {
/*  25: 72 */     this.lookupHomeOnStartup = lookupHomeOnStartup;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setCacheHome(boolean cacheHome)
/*  29:    */   {
/*  30: 83 */     this.cacheHome = cacheHome;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setExposeAccessContext(boolean exposeAccessContext)
/*  34:    */   {
/*  35: 95 */     this.exposeAccessContext = exposeAccessContext;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void afterPropertiesSet()
/*  39:    */     throws NamingException
/*  40:    */   {
/*  41:106 */     super.afterPropertiesSet();
/*  42:107 */     if (this.lookupHomeOnStartup) {
/*  43:109 */       refreshHome();
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected void refreshHome()
/*  48:    */     throws NamingException
/*  49:    */   {
/*  50:121 */     synchronized (this.homeMonitor)
/*  51:    */     {
/*  52:122 */       Object home = lookup();
/*  53:123 */       if (this.cacheHome)
/*  54:    */       {
/*  55:124 */         this.cachedHome = home;
/*  56:125 */         this.createMethod = getCreateMethod(home);
/*  57:    */       }
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected Method getCreateMethod(Object home)
/*  62:    */     throws EjbAccessException
/*  63:    */   {
/*  64:    */     try
/*  65:    */     {
/*  66:139 */       return home.getClass().getMethod("create", null);
/*  67:    */     }
/*  68:    */     catch (NoSuchMethodException localNoSuchMethodException)
/*  69:    */     {
/*  70:142 */       throw new EjbAccessException("EJB home [" + home + "] has no no-arg create() method");
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected Object getHome()
/*  75:    */     throws NamingException
/*  76:    */   {
/*  77:159 */     if ((!this.cacheHome) || ((this.lookupHomeOnStartup) && (!isHomeRefreshable()))) {
/*  78:160 */       return this.cachedHome != null ? this.cachedHome : lookup();
/*  79:    */     }
/*  80:163 */     synchronized (this.homeMonitor)
/*  81:    */     {
/*  82:164 */       if (this.cachedHome == null)
/*  83:    */       {
/*  84:165 */         this.cachedHome = lookup();
/*  85:166 */         this.createMethod = getCreateMethod(this.cachedHome);
/*  86:    */       }
/*  87:168 */       return this.cachedHome;
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected boolean isHomeRefreshable()
/*  92:    */   {
/*  93:178 */     return false;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public Object invoke(MethodInvocation invocation)
/*  97:    */     throws Throwable
/*  98:    */   {
/*  99:187 */     Context ctx = this.exposeAccessContext ? getJndiTemplate().getContext() : null;
/* 100:    */     try
/* 101:    */     {
/* 102:189 */       return invokeInContext(invocation);
/* 103:    */     }
/* 104:    */     finally
/* 105:    */     {
/* 106:192 */       getJndiTemplate().releaseContext(ctx);
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected abstract Object invokeInContext(MethodInvocation paramMethodInvocation)
/* 111:    */     throws Throwable;
/* 112:    */   
/* 113:    */   protected Object create()
/* 114:    */     throws NamingException, InvocationTargetException
/* 115:    */   {
/* 116:    */     try
/* 117:    */     {
/* 118:215 */       Object home = getHome();
/* 119:216 */       Method createMethodToUse = this.createMethod;
/* 120:217 */       if (createMethodToUse == null) {
/* 121:218 */         createMethodToUse = getCreateMethod(home);
/* 122:    */       }
/* 123:220 */       if (createMethodToUse == null) {
/* 124:221 */         return home;
/* 125:    */       }
/* 126:224 */       return createMethodToUse.invoke(home, null);
/* 127:    */     }
/* 128:    */     catch (IllegalAccessException ex)
/* 129:    */     {
/* 130:227 */       throw new EjbAccessException("Could not access EJB home create() method", ex);
/* 131:    */     }
/* 132:    */   }
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.access.AbstractSlsbInvokerInterceptor
 * JD-Core Version:    0.7.0.1
 */