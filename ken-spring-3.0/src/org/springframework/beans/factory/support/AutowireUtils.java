/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.lang.reflect.Constructor;
/*   6:    */ import java.lang.reflect.InvocationHandler;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.lang.reflect.Modifier;
/*  10:    */ import java.lang.reflect.Proxy;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Comparator;
/*  13:    */ import java.util.Set;
/*  14:    */ import org.springframework.beans.factory.ObjectFactory;
/*  15:    */ import org.springframework.util.ClassUtils;
/*  16:    */ 
/*  17:    */ abstract class AutowireUtils
/*  18:    */ {
/*  19:    */   public static void sortConstructors(Constructor[] constructors)
/*  20:    */   {
/*  21: 53 */     Arrays.sort(constructors, new Comparator()
/*  22:    */     {
/*  23:    */       public int compare(Constructor c1, Constructor c2)
/*  24:    */       {
/*  25: 55 */         boolean p1 = Modifier.isPublic(c1.getModifiers());
/*  26: 56 */         boolean p2 = Modifier.isPublic(c2.getModifiers());
/*  27: 57 */         if (p1 != p2) {
/*  28: 58 */           return p1 ? -1 : 1;
/*  29:    */         }
/*  30: 60 */         int c1pl = c1.getParameterTypes().length;
/*  31: 61 */         int c2pl = c2.getParameterTypes().length;
/*  32: 62 */         return new Integer(c1pl).compareTo(Integer.valueOf(c2pl)) * -1;
/*  33:    */       }
/*  34:    */     });
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static void sortFactoryMethods(Method[] factoryMethods)
/*  38:    */   {
/*  39: 75 */     Arrays.sort(factoryMethods, new Comparator()
/*  40:    */     {
/*  41:    */       public int compare(Method fm1, Method fm2)
/*  42:    */       {
/*  43: 77 */         boolean p1 = Modifier.isPublic(fm1.getModifiers());
/*  44: 78 */         boolean p2 = Modifier.isPublic(fm2.getModifiers());
/*  45: 79 */         if (p1 != p2) {
/*  46: 80 */           return p1 ? -1 : 1;
/*  47:    */         }
/*  48: 82 */         int c1pl = fm1.getParameterTypes().length;
/*  49: 83 */         int c2pl = fm2.getParameterTypes().length;
/*  50: 84 */         return new Integer(c1pl).compareTo(Integer.valueOf(c2pl)) * -1;
/*  51:    */       }
/*  52:    */     });
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static boolean isExcludedFromDependencyCheck(PropertyDescriptor pd)
/*  56:    */   {
/*  57: 96 */     Method wm = pd.getWriteMethod();
/*  58: 97 */     if (wm == null) {
/*  59: 98 */       return false;
/*  60:    */     }
/*  61:100 */     if (!wm.getDeclaringClass().getName().contains("$$")) {
/*  62:102 */       return false;
/*  63:    */     }
/*  64:106 */     Class superclass = wm.getDeclaringClass().getSuperclass();
/*  65:107 */     return !ClassUtils.hasMethod(superclass, wm.getName(), wm.getParameterTypes());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static boolean isSetterDefinedInInterface(PropertyDescriptor pd, Set<Class> interfaces)
/*  69:    */   {
/*  70:118 */     Method setter = pd.getWriteMethod();
/*  71:119 */     if (setter != null)
/*  72:    */     {
/*  73:120 */       Class targetClass = setter.getDeclaringClass();
/*  74:121 */       for (Class ifc : interfaces) {
/*  75:122 */         if ((ifc.isAssignableFrom(targetClass)) && 
/*  76:123 */           (ClassUtils.hasMethod(ifc, setter.getName(), setter.getParameterTypes()))) {
/*  77:124 */           return true;
/*  78:    */         }
/*  79:    */       }
/*  80:    */     }
/*  81:128 */     return false;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static Object resolveAutowiringValue(Object autowiringValue, Class requiredType)
/*  85:    */   {
/*  86:139 */     if (((autowiringValue instanceof ObjectFactory)) && (!requiredType.isInstance(autowiringValue)))
/*  87:    */     {
/*  88:140 */       ObjectFactory factory = (ObjectFactory)autowiringValue;
/*  89:141 */       if (((autowiringValue instanceof Serializable)) && (requiredType.isInterface())) {
/*  90:142 */         autowiringValue = Proxy.newProxyInstance(requiredType.getClassLoader(), 
/*  91:143 */           new Class[] { requiredType }, new ObjectFactoryDelegatingInvocationHandler(factory));
/*  92:    */       } else {
/*  93:146 */         return factory.getObject();
/*  94:    */       }
/*  95:    */     }
/*  96:149 */     return autowiringValue;
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static class ObjectFactoryDelegatingInvocationHandler
/* 100:    */     implements InvocationHandler, Serializable
/* 101:    */   {
/* 102:    */     private final ObjectFactory objectFactory;
/* 103:    */     
/* 104:    */     public ObjectFactoryDelegatingInvocationHandler(ObjectFactory objectFactory)
/* 105:    */     {
/* 106:161 */       this.objectFactory = objectFactory;
/* 107:    */     }
/* 108:    */     
/* 109:    */     public Object invoke(Object proxy, Method method, Object[] args)
/* 110:    */       throws Throwable
/* 111:    */     {
/* 112:165 */       String methodName = method.getName();
/* 113:166 */       if (methodName.equals("equals"))
/* 114:    */       {
/* 115:168 */         if (proxy == args[0]) {
/* 116:168 */           return Boolean.valueOf(true);
/* 117:    */         }
/* 118:168 */         return Boolean.valueOf(false);
/* 119:    */       }
/* 120:170 */       if (methodName.equals("hashCode")) {
/* 121:172 */         return Integer.valueOf(System.identityHashCode(proxy));
/* 122:    */       }
/* 123:174 */       if (methodName.equals("toString")) {
/* 124:175 */         return this.objectFactory.toString();
/* 125:    */       }
/* 126:    */       try
/* 127:    */       {
/* 128:178 */         return method.invoke(this.objectFactory.getObject(), args);
/* 129:    */       }
/* 130:    */       catch (InvocationTargetException ex)
/* 131:    */       {
/* 132:181 */         throw ex.getTargetException();
/* 133:    */       }
/* 134:    */     }
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AutowireUtils
 * JD-Core Version:    0.7.0.1
 */