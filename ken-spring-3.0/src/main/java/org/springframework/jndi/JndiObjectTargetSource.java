/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.springframework.aop.TargetSource;
/*   5:    */ 
/*   6:    */ public class JndiObjectTargetSource
/*   7:    */   extends JndiObjectLocator
/*   8:    */   implements TargetSource
/*   9:    */ {
/*  10: 63 */   private boolean lookupOnStartup = true;
/*  11: 65 */   private boolean cache = true;
/*  12:    */   private Object cachedObject;
/*  13:    */   private Class targetClass;
/*  14:    */   
/*  15:    */   public void setLookupOnStartup(boolean lookupOnStartup)
/*  16:    */   {
/*  17: 79 */     this.lookupOnStartup = lookupOnStartup;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setCache(boolean cache)
/*  21:    */   {
/*  22: 90 */     this.cache = cache;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void afterPropertiesSet()
/*  26:    */     throws NamingException
/*  27:    */   {
/*  28: 95 */     super.afterPropertiesSet();
/*  29: 96 */     if (this.lookupOnStartup)
/*  30:    */     {
/*  31: 97 */       Object object = lookup();
/*  32: 98 */       if (this.cache) {
/*  33: 99 */         this.cachedObject = object;
/*  34:    */       } else {
/*  35:102 */         this.targetClass = object.getClass();
/*  36:    */       }
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Class<?> getTargetClass()
/*  41:    */   {
/*  42:109 */     if (this.cachedObject != null) {
/*  43:110 */       return this.cachedObject.getClass();
/*  44:    */     }
/*  45:112 */     if (this.targetClass != null) {
/*  46:113 */       return this.targetClass;
/*  47:    */     }
/*  48:116 */     return getExpectedType();
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isStatic()
/*  52:    */   {
/*  53:121 */     return this.cachedObject != null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   /* Error */
/*  57:    */   public Object getTarget()
/*  58:    */   {
/*  59:    */     // Byte code:
/*  60:    */     //   0: aload_0
/*  61:    */     //   1: getfield 19	org/springframework/jndi/JndiObjectTargetSource:lookupOnStartup	Z
/*  62:    */     //   4: ifne +10 -> 14
/*  63:    */     //   7: aload_0
/*  64:    */     //   8: getfield 21	org/springframework/jndi/JndiObjectTargetSource:cache	Z
/*  65:    */     //   11: ifne +22 -> 33
/*  66:    */     //   14: aload_0
/*  67:    */     //   15: getfield 40	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*  68:    */     //   18: ifnull +10 -> 28
/*  69:    */     //   21: aload_0
/*  70:    */     //   22: getfield 40	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*  71:    */     //   25: goto +7 -> 32
/*  72:    */     //   28: aload_0
/*  73:    */     //   29: invokevirtual 36	org/springframework/jndi/JndiObjectTargetSource:lookup	()Ljava/lang/Object;
/*  74:    */     //   32: areturn
/*  75:    */     //   33: aload_0
/*  76:    */     //   34: dup
/*  77:    */     //   35: astore_1
/*  78:    */     //   36: monitorenter
/*  79:    */     //   37: aload_0
/*  80:    */     //   38: getfield 40	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*  81:    */     //   41: ifnonnull +11 -> 52
/*  82:    */     //   44: aload_0
/*  83:    */     //   45: aload_0
/*  84:    */     //   46: invokevirtual 36	org/springframework/jndi/JndiObjectTargetSource:lookup	()Ljava/lang/Object;
/*  85:    */     //   49: putfield 40	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*  86:    */     //   52: aload_0
/*  87:    */     //   53: getfield 40	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*  88:    */     //   56: aload_1
/*  89:    */     //   57: monitorexit
/*  90:    */     //   58: areturn
/*  91:    */     //   59: aload_1
/*  92:    */     //   60: monitorexit
/*  93:    */     //   61: athrow
/*  94:    */     //   62: astore_1
/*  95:    */     //   63: new 60	org/springframework/jndi/JndiLookupFailureException
/*  96:    */     //   66: dup
/*  97:    */     //   67: ldc 62
/*  98:    */     //   69: aload_1
/*  99:    */     //   70: invokespecial 64	org/springframework/jndi/JndiLookupFailureException:<init>	(Ljava/lang/String;Ljavax/naming/NamingException;)V
/* 100:    */     //   73: athrow
/* 101:    */     // Line number table:
/* 102:    */     //   Java source line #126	-> byte code offset #0
/* 103:    */     //   Java source line #127	-> byte code offset #14
/* 104:    */     //   Java source line #130	-> byte code offset #33
/* 105:    */     //   Java source line #131	-> byte code offset #37
/* 106:    */     //   Java source line #132	-> byte code offset #44
/* 107:    */     //   Java source line #134	-> byte code offset #52
/* 108:    */     //   Java source line #130	-> byte code offset #59
/* 109:    */     //   Java source line #138	-> byte code offset #62
/* 110:    */     //   Java source line #139	-> byte code offset #63
/* 111:    */     // Local variable table:
/* 112:    */     //   start	length	slot	name	signature
/* 113:    */     //   0	74	0	this	JndiObjectTargetSource
/* 114:    */     //   62	8	1	ex	NamingException
/* 115:    */     // Exception table:
/* 116:    */     //   from	to	target	type
/* 117:    */     //   37	58	59	finally
/* 118:    */     //   59	61	59	finally
/* 119:    */     //   0	32	62	javax/naming/NamingException
/* 120:    */     //   33	58	62	javax/naming/NamingException
/* 121:    */     //   59	62	62	javax/naming/NamingException
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void releaseTarget(Object target) {}
/* 125:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiObjectTargetSource
 * JD-Core Version:    0.7.0.1
 */