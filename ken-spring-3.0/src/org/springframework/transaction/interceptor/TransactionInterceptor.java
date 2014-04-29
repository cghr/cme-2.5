/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.ObjectInputStream;
/*   5:    */ import java.io.ObjectOutputStream;
/*   6:    */ import java.io.Serializable;
/*   7:    */ import java.util.Properties;
/*   8:    */ import org.aopalliance.intercept.MethodInterceptor;
/*   9:    */ import org.aopalliance.intercept.MethodInvocation;
/*  10:    */ import org.springframework.aop.support.AopUtils;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.transaction.PlatformTransactionManager;
/*  13:    */ import org.springframework.transaction.TransactionStatus;
/*  14:    */ import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
/*  15:    */ import org.springframework.transaction.support.TransactionCallback;
/*  16:    */ 
/*  17:    */ public class TransactionInterceptor
/*  18:    */   extends TransactionAspectSupport
/*  19:    */   implements MethodInterceptor, Serializable
/*  20:    */ {
/*  21:    */   public TransactionInterceptor() {}
/*  22:    */   
/*  23:    */   public TransactionInterceptor(PlatformTransactionManager ptm, Properties attributes)
/*  24:    */   {
/*  25: 74 */     setTransactionManager(ptm);
/*  26: 75 */     setTransactionAttributes(attributes);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public TransactionInterceptor(PlatformTransactionManager ptm, TransactionAttributeSource tas)
/*  30:    */   {
/*  31: 86 */     setTransactionManager(ptm);
/*  32: 87 */     setTransactionAttributeSource(tas);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public Object invoke(final MethodInvocation invocation)
/*  36:    */     throws Throwable
/*  37:    */   {
/*  38: 95 */     Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
/*  39:    */     
/*  40:    */ 
/*  41: 98 */     final TransactionAttribute txAttr = 
/*  42: 99 */       getTransactionAttributeSource().getTransactionAttribute(invocation.getMethod(), targetClass);
/*  43:100 */     final PlatformTransactionManager tm = determineTransactionManager(txAttr);
/*  44:101 */     final String joinpointIdentification = methodIdentification(invocation.getMethod(), targetClass);
/*  45:103 */     if ((txAttr == null) || (!(tm instanceof CallbackPreferringPlatformTransactionManager)))
/*  46:    */     {
/*  47:105 */       TransactionAspectSupport.TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
/*  48:106 */       Object retVal = null;
/*  49:    */       try
/*  50:    */       {
/*  51:110 */         retVal = invocation.proceed();
/*  52:    */       }
/*  53:    */       catch (Throwable ex)
/*  54:    */       {
/*  55:114 */         completeTransactionAfterThrowing(txInfo, ex);
/*  56:115 */         throw ex;
/*  57:    */       }
/*  58:    */       finally
/*  59:    */       {
/*  60:118 */         cleanupTransactionInfo(txInfo);
/*  61:    */       }
/*  62:120 */       commitTransactionAfterReturning(txInfo);
/*  63:121 */       return retVal;
/*  64:    */     }
/*  65:    */     try
/*  66:    */     {
/*  67:127 */       Object result = ((CallbackPreferringPlatformTransactionManager)tm).execute(txAttr, 
/*  68:128 */         new TransactionCallback()
/*  69:    */         {
/*  70:    */           public Object doInTransaction(TransactionStatus status)
/*  71:    */           {
/*  72:130 */             TransactionAspectSupport.TransactionInfo txInfo = TransactionInterceptor.this.prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
/*  73:    */             try
/*  74:    */             {
/*  75:132 */               return invocation.proceed();
/*  76:    */             }
/*  77:    */             catch (Throwable ex)
/*  78:    */             {
/*  79:    */               Object localObject2;
/*  80:135 */               if (txAttr.rollbackOn(ex))
/*  81:    */               {
/*  82:137 */                 if ((ex instanceof RuntimeException)) {
/*  83:138 */                   throw ((RuntimeException)ex);
/*  84:    */                 }
/*  85:141 */                 throw new TransactionInterceptor.ThrowableHolderException(ex);
/*  86:    */               }
/*  87:146 */               return new TransactionInterceptor.ThrowableHolder(ex);
/*  88:    */             }
/*  89:    */             finally
/*  90:    */             {
/*  91:150 */               TransactionInterceptor.this.cleanupTransactionInfo(txInfo);
/*  92:    */             }
/*  93:    */           }
/*  94:    */         });
/*  95:156 */       if ((result instanceof ThrowableHolder)) {
/*  96:157 */         throw ((ThrowableHolder)result).getThrowable();
/*  97:    */       }
/*  98:160 */       return result;
/*  99:    */     }
/* 100:    */     catch (ThrowableHolderException ex)
/* 101:    */     {
/* 102:164 */       throw ex.getCause();
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private void writeObject(ObjectOutputStream oos)
/* 107:    */     throws IOException
/* 108:    */   {
/* 109:176 */     oos.defaultWriteObject();
/* 110:    */     
/* 111:    */ 
/* 112:179 */     oos.writeObject(getTransactionManagerBeanName());
/* 113:180 */     oos.writeObject(getTransactionManager());
/* 114:181 */     oos.writeObject(getTransactionAttributeSource());
/* 115:182 */     oos.writeObject(getBeanFactory());
/* 116:    */   }
/* 117:    */   
/* 118:    */   private void readObject(ObjectInputStream ois)
/* 119:    */     throws IOException, ClassNotFoundException
/* 120:    */   {
/* 121:187 */     ois.defaultReadObject();
/* 122:    */     
/* 123:    */ 
/* 124:    */ 
/* 125:    */ 
/* 126:192 */     setTransactionManagerBeanName((String)ois.readObject());
/* 127:193 */     setTransactionManager((PlatformTransactionManager)ois.readObject());
/* 128:194 */     setTransactionAttributeSource((TransactionAttributeSource)ois.readObject());
/* 129:195 */     setBeanFactory((BeanFactory)ois.readObject());
/* 130:    */   }
/* 131:    */   
/* 132:    */   private static class ThrowableHolder
/* 133:    */   {
/* 134:    */     private final Throwable throwable;
/* 135:    */     
/* 136:    */     public ThrowableHolder(Throwable throwable)
/* 137:    */     {
/* 138:208 */       this.throwable = throwable;
/* 139:    */     }
/* 140:    */     
/* 141:    */     public final Throwable getThrowable()
/* 142:    */     {
/* 143:212 */       return this.throwable;
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   private static class ThrowableHolderException
/* 148:    */     extends RuntimeException
/* 149:    */   {
/* 150:    */     public ThrowableHolderException(Throwable throwable)
/* 151:    */     {
/* 152:224 */       super();
/* 153:    */     }
/* 154:    */     
/* 155:    */     public String toString()
/* 156:    */     {
/* 157:229 */       return getCause().toString();
/* 158:    */     }
/* 159:    */   }
/* 160:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionInterceptor
 * JD-Core Version:    0.7.0.1
 */