/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.security.AccessController;
/*   6:    */ import java.security.PrivilegedAction;
/*   7:    */ import java.security.PrivilegedExceptionAction;
/*   8:    */ import org.springframework.beans.BeanInstantiationException;
/*   9:    */ import org.springframework.beans.BeanUtils;
/*  10:    */ import org.springframework.beans.factory.BeanFactory;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ 
/*  13:    */ public class SimpleInstantiationStrategy
/*  14:    */   implements InstantiationStrategy
/*  15:    */ {
/*  16: 45 */   private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal();
/*  17:    */   
/*  18:    */   public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner)
/*  19:    */   {
/*  20: 49 */     if (beanDefinition.getMethodOverrides().isEmpty())
/*  21:    */     {
/*  22: 51 */       synchronized (beanDefinition.constructorArgumentLock)
/*  23:    */       {
/*  24: 52 */         Constructor<?> constructorToUse = (Constructor)beanDefinition.resolvedConstructorOrFactoryMethod;
/*  25: 53 */         if (constructorToUse == null)
/*  26:    */         {
/*  27: 54 */           final Class clazz = beanDefinition.getBeanClass();
/*  28: 55 */           if (clazz.isInterface()) {
/*  29: 56 */             throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*  30:    */           }
/*  31:    */           try
/*  32:    */           {
/*  33: 59 */             if (System.getSecurityManager() != null) {
/*  34: 60 */               constructorToUse = (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*  35:    */               {
/*  36:    */                 public Constructor run()
/*  37:    */                   throws Exception
/*  38:    */                 {
/*  39: 62 */                   return clazz.getDeclaredConstructor(null);
/*  40:    */                 }
/*  41:    */               });
/*  42:    */             } else {
/*  43: 67 */               constructorToUse = clazz.getDeclaredConstructor(null);
/*  44:    */             }
/*  45: 69 */             beanDefinition.resolvedConstructorOrFactoryMethod = constructorToUse;
/*  46:    */           }
/*  47:    */           catch (Exception ex)
/*  48:    */           {
/*  49: 72 */             throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*  50:    */           }
/*  51:    */         }
/*  52:    */       }
/*  53:    */       Constructor<?> constructorToUse;
/*  54: 76 */       return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
/*  55:    */     }
/*  56: 80 */     return instantiateWithMethodInjection(beanDefinition, beanName, owner);
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected Object instantiateWithMethodInjection(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner)
/*  60:    */   {
/*  61: 93 */     throw new UnsupportedOperationException(
/*  62: 94 */       "Method Injection not supported in SimpleInstantiationStrategy");
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, final Constructor<?> ctor, Object[] args)
/*  66:    */   {
/*  67:100 */     if (beanDefinition.getMethodOverrides().isEmpty())
/*  68:    */     {
/*  69:101 */       if (System.getSecurityManager() != null) {
/*  70:103 */         AccessController.doPrivileged(new PrivilegedAction()
/*  71:    */         {
/*  72:    */           public Object run()
/*  73:    */           {
/*  74:105 */             ReflectionUtils.makeAccessible(ctor);
/*  75:106 */             return null;
/*  76:    */           }
/*  77:    */         });
/*  78:    */       }
/*  79:110 */       return BeanUtils.instantiateClass(ctor, args);
/*  80:    */     }
/*  81:113 */     return instantiateWithMethodInjection(beanDefinition, beanName, owner, ctor, args);
/*  82:    */   }
/*  83:    */   
/*  84:    */   protected Object instantiateWithMethodInjection(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Constructor ctor, Object[] args)
/*  85:    */   {
/*  86:126 */     throw new UnsupportedOperationException(
/*  87:127 */       "Method Injection not supported in SimpleInstantiationStrategy");
/*  88:    */   }
/*  89:    */   
/*  90:    */   /* Error */
/*  91:    */   public Object instantiate(RootBeanDefinition beanDefinition, String beanName, BeanFactory owner, Object factoryBean, final Method factoryMethod, Object[] args)
/*  92:    */   {
/*  93:    */     // Byte code:
/*  94:    */     //   0: invokestatic 65	java/lang/System:getSecurityManager	()Ljava/lang/SecurityManager;
/*  95:    */     //   3: ifnull +20 -> 23
/*  96:    */     //   6: new 139	org/springframework/beans/factory/support/SimpleInstantiationStrategy$3
/*  97:    */     //   9: dup
/*  98:    */     //   10: aload_0
/*  99:    */     //   11: aload 5
/* 100:    */     //   13: invokespecial 141	org/springframework/beans/factory/support/SimpleInstantiationStrategy$3:<init>	(Lorg/springframework/beans/factory/support/SimpleInstantiationStrategy;Ljava/lang/reflect/Method;)V
/* 101:    */     //   16: invokestatic 130	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/* 102:    */     //   19: pop
/* 103:    */     //   20: goto +8 -> 28
/* 104:    */     //   23: aload 5
/* 105:    */     //   25: invokestatic 144	org/springframework/util/ReflectionUtils:makeAccessible	(Ljava/lang/reflect/Method;)V
/* 106:    */     //   28: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 107:    */     //   31: invokevirtual 150	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/* 108:    */     //   34: checkcast 154	java/lang/reflect/Method
/* 109:    */     //   37: astore 7
/* 110:    */     //   39: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 111:    */     //   42: aload 5
/* 112:    */     //   44: invokevirtual 156	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/* 113:    */     //   47: aload 5
/* 114:    */     //   49: aload 4
/* 115:    */     //   51: aload 6
/* 116:    */     //   53: invokevirtual 160	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
/* 117:    */     //   56: astore 9
/* 118:    */     //   58: aload 7
/* 119:    */     //   60: ifnull +14 -> 74
/* 120:    */     //   63: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 121:    */     //   66: aload 7
/* 122:    */     //   68: invokevirtual 156	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/* 123:    */     //   71: goto +9 -> 80
/* 124:    */     //   74: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 125:    */     //   77: invokevirtual 164	java/lang/ThreadLocal:remove	()V
/* 126:    */     //   80: aload 9
/* 127:    */     //   82: areturn
/* 128:    */     //   83: astore 8
/* 129:    */     //   85: aload 7
/* 130:    */     //   87: ifnull +14 -> 101
/* 131:    */     //   90: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 132:    */     //   93: aload 7
/* 133:    */     //   95: invokevirtual 156	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/* 134:    */     //   98: goto +9 -> 107
/* 135:    */     //   101: getstatic 19	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/* 136:    */     //   104: invokevirtual 164	java/lang/ThreadLocal:remove	()V
/* 137:    */     //   107: aload 8
/* 138:    */     //   109: athrow
/* 139:    */     //   110: pop
/* 140:    */     //   111: new 167	org/springframework/beans/factory/BeanDefinitionStoreException
/* 141:    */     //   114: dup
/* 142:    */     //   115: new 169	java/lang/StringBuilder
/* 143:    */     //   118: dup
/* 144:    */     //   119: ldc 171
/* 145:    */     //   121: invokespecial 173	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 146:    */     //   124: aload 5
/* 147:    */     //   126: invokevirtual 174	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 148:    */     //   129: ldc 178
/* 149:    */     //   131: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 150:    */     //   134: ldc 183
/* 151:    */     //   136: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 152:    */     //   139: aload 6
/* 153:    */     //   141: invokestatic 185	org/springframework/util/StringUtils:arrayToCommaDelimitedString	([Ljava/lang/Object;)Ljava/lang/String;
/* 154:    */     //   144: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 155:    */     //   147: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 156:    */     //   150: invokespecial 195	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;)V
/* 157:    */     //   153: athrow
/* 158:    */     //   154: pop
/* 159:    */     //   155: new 167	org/springframework/beans/factory/BeanDefinitionStoreException
/* 160:    */     //   158: dup
/* 161:    */     //   159: new 169	java/lang/StringBuilder
/* 162:    */     //   162: dup
/* 163:    */     //   163: ldc 196
/* 164:    */     //   165: invokespecial 173	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 165:    */     //   168: aload 5
/* 166:    */     //   170: invokevirtual 174	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 167:    */     //   173: ldc 198
/* 168:    */     //   175: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 169:    */     //   178: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 170:    */     //   181: invokespecial 195	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;)V
/* 171:    */     //   184: athrow
/* 172:    */     //   185: astore 7
/* 173:    */     //   187: new 167	org/springframework/beans/factory/BeanDefinitionStoreException
/* 174:    */     //   190: dup
/* 175:    */     //   191: new 169	java/lang/StringBuilder
/* 176:    */     //   194: dup
/* 177:    */     //   195: ldc 200
/* 178:    */     //   197: invokespecial 173	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/* 179:    */     //   200: aload 5
/* 180:    */     //   202: invokevirtual 174	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/* 181:    */     //   205: ldc 202
/* 182:    */     //   207: invokevirtual 180	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/* 183:    */     //   210: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
/* 184:    */     //   213: aload 7
/* 185:    */     //   215: invokevirtual 204	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
/* 186:    */     //   218: invokespecial 210	org/springframework/beans/factory/BeanDefinitionStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/* 187:    */     //   221: athrow
/* 188:    */     // Line number table:
/* 189:    */     //   Java source line #134	-> byte code offset #0
/* 190:    */     //   Java source line #135	-> byte code offset #6
/* 191:    */     //   Java source line #143	-> byte code offset #23
/* 192:    */     //   Java source line #146	-> byte code offset #28
/* 193:    */     //   Java source line #148	-> byte code offset #39
/* 194:    */     //   Java source line #149	-> byte code offset #47
/* 195:    */     //   Java source line #151	-> byte code offset #58
/* 196:    */     //   Java source line #152	-> byte code offset #63
/* 197:    */     //   Java source line #155	-> byte code offset #74
/* 198:    */     //   Java source line #149	-> byte code offset #80
/* 199:    */     //   Java source line #150	-> byte code offset #83
/* 200:    */     //   Java source line #151	-> byte code offset #85
/* 201:    */     //   Java source line #152	-> byte code offset #90
/* 202:    */     //   Java source line #155	-> byte code offset #101
/* 203:    */     //   Java source line #157	-> byte code offset #107
/* 204:    */     //   Java source line #159	-> byte code offset #110
/* 205:    */     //   Java source line #160	-> byte code offset #111
/* 206:    */     //   Java source line #161	-> byte code offset #115
/* 207:    */     //   Java source line #162	-> byte code offset #134
/* 208:    */     //   Java source line #161	-> byte code offset #147
/* 209:    */     //   Java source line #160	-> byte code offset #150
/* 210:    */     //   Java source line #164	-> byte code offset #154
/* 211:    */     //   Java source line #165	-> byte code offset #155
/* 212:    */     //   Java source line #166	-> byte code offset #159
/* 213:    */     //   Java source line #165	-> byte code offset #181
/* 214:    */     //   Java source line #168	-> byte code offset #185
/* 215:    */     //   Java source line #169	-> byte code offset #187
/* 216:    */     //   Java source line #170	-> byte code offset #191
/* 217:    */     //   Java source line #169	-> byte code offset #218
/* 218:    */     // Local variable table:
/* 219:    */     //   start	length	slot	name	signature
/* 220:    */     //   0	222	0	this	SimpleInstantiationStrategy
/* 221:    */     //   0	222	1	beanDefinition	RootBeanDefinition
/* 222:    */     //   0	222	2	beanName	String
/* 223:    */     //   0	222	3	owner	BeanFactory
/* 224:    */     //   0	222	4	factoryBean	Object
/* 225:    */     //   0	222	5	factoryMethod	Method
/* 226:    */     //   0	222	6	args	Object[]
/* 227:    */     //   37	57	7	priorInvokedFactoryMethod	Method
/* 228:    */     //   185	29	7	ex	java.lang.reflect.InvocationTargetException
/* 229:    */     //   83	25	8	localObject1	Object
/* 230:    */     //   110	1	11	localIllegalArgumentException	java.lang.IllegalArgumentException
/* 231:    */     //   154	1	12	localIllegalAccessException	java.lang.IllegalAccessException
/* 232:    */     // Exception table:
/* 233:    */     //   from	to	target	type
/* 234:    */     //   39	58	83	finally
/* 235:    */     //   0	80	110	java/lang/IllegalArgumentException
/* 236:    */     //   83	110	110	java/lang/IllegalArgumentException
/* 237:    */     //   0	80	154	java/lang/IllegalAccessException
/* 238:    */     //   83	110	154	java/lang/IllegalAccessException
/* 239:    */     //   0	80	185	java/lang/reflect/InvocationTargetException
/* 240:    */     //   83	110	185	java/lang/reflect/InvocationTargetException
/* 241:    */   }
/* 242:    */   
/* 243:    */   public static Method getCurrentlyInvokedFactoryMethod()
/* 244:    */   {
/* 245:180 */     return (Method)currentlyInvokedFactoryMethod.get();
/* 246:    */   }
/* 247:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleInstantiationStrategy
 * JD-Core Version:    0.7.0.1
 */