/*   1:    */ package org.springframework.jmx.export.assembler;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Properties;
/*   9:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  10:    */ import org.springframework.beans.factory.InitializingBean;
/*  11:    */ import org.springframework.util.ClassUtils;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class InterfaceBasedMBeanInfoAssembler
/*  15:    */   extends AbstractConfigurableMBeanInfoAssembler
/*  16:    */   implements BeanClassLoaderAware, InitializingBean
/*  17:    */ {
/*  18:    */   private Class[] managedInterfaces;
/*  19:    */   private Properties interfaceMappings;
/*  20: 73 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  21:    */   private Map<String, Class[]> resolvedInterfaceMappings;
/*  22:    */   
/*  23:    */   public void setManagedInterfaces(Class[] managedInterfaces)
/*  24:    */   {
/*  25: 90 */     if (managedInterfaces != null) {
/*  26: 91 */       for (Class<?> ifc : managedInterfaces) {
/*  27: 92 */         if (!ifc.isInterface()) {
/*  28: 93 */           throw new IllegalArgumentException(
/*  29: 94 */             "Management interface [" + ifc.getName() + "] is not an interface");
/*  30:    */         }
/*  31:    */       }
/*  32:    */     }
/*  33: 98 */     this.managedInterfaces = managedInterfaces;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setInterfaceMappings(Properties mappings)
/*  37:    */   {
/*  38:109 */     this.interfaceMappings = mappings;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  42:    */   {
/*  43:113 */     this.beanClassLoader = beanClassLoader;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void afterPropertiesSet()
/*  47:    */   {
/*  48:118 */     if (this.interfaceMappings != null) {
/*  49:119 */       this.resolvedInterfaceMappings = resolveInterfaceMappings(this.interfaceMappings);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   private Map<String, Class[]> resolveInterfaceMappings(Properties mappings)
/*  54:    */   {
/*  55:129 */     Map<String, Class[]> resolvedMappings = new HashMap(mappings.size());
/*  56:130 */     for (Enumeration en = mappings.propertyNames(); en.hasMoreElements();)
/*  57:    */     {
/*  58:131 */       String beanKey = (String)en.nextElement();
/*  59:132 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  60:133 */       Class[] classes = resolveClassNames(classNames, beanKey);
/*  61:134 */       resolvedMappings.put(beanKey, classes);
/*  62:    */     }
/*  63:136 */     return resolvedMappings;
/*  64:    */   }
/*  65:    */   
/*  66:    */   private Class[] resolveClassNames(String[] classNames, String beanKey)
/*  67:    */   {
/*  68:146 */     Class[] classes = new Class[classNames.length];
/*  69:147 */     for (int x = 0; x < classes.length; x++)
/*  70:    */     {
/*  71:148 */       Class cls = ClassUtils.resolveClassName(classNames[x].trim(), this.beanClassLoader);
/*  72:149 */       if (!cls.isInterface()) {
/*  73:150 */         throw new IllegalArgumentException(
/*  74:151 */           "Class [" + classNames[x] + "] mapped to bean key [" + beanKey + "] is no interface");
/*  75:    */       }
/*  76:153 */       classes[x] = cls;
/*  77:    */     }
/*  78:155 */     return classes;
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected boolean includeReadAttribute(Method method, String beanKey)
/*  82:    */   {
/*  83:170 */     return isPublicInInterface(method, beanKey);
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected boolean includeWriteAttribute(Method method, String beanKey)
/*  87:    */   {
/*  88:184 */     return isPublicInInterface(method, beanKey);
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected boolean includeOperation(Method method, String beanKey)
/*  92:    */   {
/*  93:198 */     return isPublicInInterface(method, beanKey);
/*  94:    */   }
/*  95:    */   
/*  96:    */   private boolean isPublicInInterface(Method method, String beanKey)
/*  97:    */   {
/*  98:210 */     return ((method.getModifiers() & 0x1) > 0) && (isDeclaredInInterface(method, beanKey));
/*  99:    */   }
/* 100:    */   
/* 101:    */   private boolean isDeclaredInInterface(Method method, String beanKey)
/* 102:    */   {
/* 103:218 */     Class[] ifaces = (Class[])null;
/* 104:220 */     if (this.resolvedInterfaceMappings != null) {
/* 105:221 */       ifaces = (Class[])this.resolvedInterfaceMappings.get(beanKey);
/* 106:    */     }
/* 107:224 */     if (ifaces == null)
/* 108:    */     {
/* 109:225 */       ifaces = this.managedInterfaces;
/* 110:226 */       if (ifaces == null) {
/* 111:227 */         ifaces = ClassUtils.getAllInterfacesForClass(method.getDeclaringClass());
/* 112:    */       }
/* 113:    */     }
/* 114:231 */     if (ifaces != null) {
/* 115:232 */       for (Class ifc : ifaces) {
/* 116:233 */         for (Method ifcMethod : ifc.getMethods()) {
/* 117:234 */           if ((ifcMethod.getName().equals(method.getName())) && 
/* 118:235 */             (Arrays.equals(ifcMethod.getParameterTypes(), method.getParameterTypes()))) {
/* 119:236 */             return true;
/* 120:    */           }
/* 121:    */         }
/* 122:    */       }
/* 123:    */     }
/* 124:242 */     return false;
/* 125:    */   }
/* 126:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */