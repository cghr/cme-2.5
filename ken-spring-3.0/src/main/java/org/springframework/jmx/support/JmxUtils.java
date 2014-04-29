/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.management.ManagementFactory;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.Hashtable;
/*   7:    */ import java.util.List;
/*   8:    */ import javax.management.DynamicMBean;
/*   9:    */ import javax.management.MBeanParameterInfo;
/*  10:    */ import javax.management.MBeanServer;
/*  11:    */ import javax.management.MBeanServerFactory;
/*  12:    */ import javax.management.MXBean;
/*  13:    */ import javax.management.MalformedObjectNameException;
/*  14:    */ import javax.management.ObjectName;
/*  15:    */ import org.apache.commons.logging.Log;
/*  16:    */ import org.apache.commons.logging.LogFactory;
/*  17:    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ import org.springframework.util.ObjectUtils;
/*  20:    */ import org.springframework.util.StringUtils;
/*  21:    */ 
/*  22:    */ public abstract class JmxUtils
/*  23:    */ {
/*  24:    */   public static final String IDENTITY_OBJECT_NAME_KEY = "identity";
/*  25:    */   private static final String MBEAN_SUFFIX = "MBean";
/*  26:    */   private static final String MXBEAN_SUFFIX = "MXBean";
/*  27:    */   private static final String MXBEAN_ANNOTATION_CLASS_NAME = "javax.management.MXBean";
/*  28: 71 */   private static final boolean mxBeanAnnotationAvailable = ClassUtils.isPresent("javax.management.MXBean", JmxUtils.class.getClassLoader());
/*  29: 73 */   private static final Log logger = LogFactory.getLog(JmxUtils.class);
/*  30:    */   
/*  31:    */   public static MBeanServer locateMBeanServer()
/*  32:    */     throws MBeanServerNotFoundException
/*  33:    */   {
/*  34: 86 */     return locateMBeanServer(null);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static MBeanServer locateMBeanServer(String agentId)
/*  38:    */     throws MBeanServerNotFoundException
/*  39:    */   {
/*  40:102 */     MBeanServer server = null;
/*  41:105 */     if (!"".equals(agentId))
/*  42:    */     {
/*  43:106 */       List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(agentId);
/*  44:107 */       if ((servers != null) && (servers.size() > 0))
/*  45:    */       {
/*  46:109 */         if ((servers.size() > 1) && (logger.isWarnEnabled())) {
/*  47:110 */           logger.warn("Found more than one MBeanServer instance" + (
/*  48:111 */             agentId != null ? " with agent id [" + agentId + "]" : "") + 
/*  49:112 */             ". Returning first from list.");
/*  50:    */         }
/*  51:114 */         server = (MBeanServer)servers.get(0);
/*  52:    */       }
/*  53:    */     }
/*  54:118 */     if ((server == null) && (!StringUtils.hasLength(agentId))) {
/*  55:    */       try
/*  56:    */       {
/*  57:121 */         server = ManagementFactory.getPlatformMBeanServer();
/*  58:    */       }
/*  59:    */       catch (SecurityException ex)
/*  60:    */       {
/*  61:124 */         throw new MBeanServerNotFoundException("No specific MBeanServer found, and not allowed to obtain the Java platform MBeanServer", 
/*  62:125 */           ex);
/*  63:    */       }
/*  64:    */     }
/*  65:129 */     if (server == null) {
/*  66:130 */       throw new MBeanServerNotFoundException(
/*  67:131 */         "Unable to locate an MBeanServer instance" + (
/*  68:132 */         agentId != null ? " with agent id [" + agentId + "]" : ""));
/*  69:    */     }
/*  70:135 */     if (logger.isDebugEnabled()) {
/*  71:136 */       logger.debug("Found MBeanServer: " + server);
/*  72:    */     }
/*  73:138 */     return server;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static Class[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo)
/*  77:    */     throws ClassNotFoundException
/*  78:    */   {
/*  79:149 */     return parameterInfoToTypes(paramInfo, ClassUtils.getDefaultClassLoader());
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static Class[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo, ClassLoader classLoader)
/*  83:    */     throws ClassNotFoundException
/*  84:    */   {
/*  85:163 */     Class[] types = (Class[])null;
/*  86:164 */     if ((paramInfo != null) && (paramInfo.length > 0))
/*  87:    */     {
/*  88:165 */       types = new Class[paramInfo.length];
/*  89:166 */       for (int x = 0; x < paramInfo.length; x++) {
/*  90:167 */         types[x] = ClassUtils.forName(paramInfo[x].getType(), classLoader);
/*  91:    */       }
/*  92:    */     }
/*  93:170 */     return types;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static String[] getMethodSignature(Method method)
/*  97:    */   {
/*  98:181 */     Class[] types = method.getParameterTypes();
/*  99:182 */     String[] signature = new String[types.length];
/* 100:183 */     for (int x = 0; x < types.length; x++) {
/* 101:184 */       signature[x] = types[x].getName();
/* 102:    */     }
/* 103:186 */     return signature;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static String getAttributeName(PropertyDescriptor property, boolean useStrictCasing)
/* 107:    */   {
/* 108:200 */     if (useStrictCasing) {
/* 109:201 */       return StringUtils.capitalize(property.getName());
/* 110:    */     }
/* 111:204 */     return property.getName();
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static ObjectName appendIdentityToObjectName(ObjectName objectName, Object managedResource)
/* 115:    */     throws MalformedObjectNameException
/* 116:    */   {
/* 117:225 */     Hashtable<String, String> keyProperties = objectName.getKeyPropertyList();
/* 118:226 */     keyProperties.put("identity", ObjectUtils.getIdentityHexString(managedResource));
/* 119:227 */     return ObjectNameManager.getInstance(objectName.getDomain(), keyProperties);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public static Class<?> getClassToExpose(Object managedBean)
/* 123:    */   {
/* 124:241 */     return ClassUtils.getUserClass(managedBean);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static Class<?> getClassToExpose(Class<?> clazz)
/* 128:    */   {
/* 129:255 */     return ClassUtils.getUserClass(clazz);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public static boolean isMBean(Class<?> clazz)
/* 133:    */   {
/* 134:270 */     return (clazz != null) && ((DynamicMBean.class.isAssignableFrom(clazz)) || (getMBeanInterface(clazz) != null) || (getMXBeanInterface(clazz) != null));
/* 135:    */   }
/* 136:    */   
/* 137:    */   public static Class<?> getMBeanInterface(Class<?> clazz)
/* 138:    */   {
/* 139:281 */     if ((clazz == null) || (clazz.getSuperclass() == null)) {
/* 140:282 */       return null;
/* 141:    */     }
/* 142:284 */     String mbeanInterfaceName = clazz.getName() + "MBean";
/* 143:285 */     Class[] implementedInterfaces = clazz.getInterfaces();
/* 144:286 */     for (Class<?> iface : implementedInterfaces) {
/* 145:287 */       if (iface.getName().equals(mbeanInterfaceName)) {
/* 146:288 */         return iface;
/* 147:    */       }
/* 148:    */     }
/* 149:291 */     return getMBeanInterface(clazz.getSuperclass());
/* 150:    */   }
/* 151:    */   
/* 152:    */   public static Class<?> getMXBeanInterface(Class<?> clazz)
/* 153:    */   {
/* 154:302 */     if ((clazz == null) || (clazz.getSuperclass() == null)) {
/* 155:303 */       return null;
/* 156:    */     }
/* 157:305 */     Class[] implementedInterfaces = clazz.getInterfaces();
/* 158:306 */     for (Class<?> iface : implementedInterfaces)
/* 159:    */     {
/* 160:307 */       boolean isMxBean = iface.getName().endsWith("MXBean");
/* 161:308 */       if (mxBeanAnnotationAvailable)
/* 162:    */       {
/* 163:309 */         Boolean checkResult = MXBeanChecker.evaluateMXBeanAnnotation(iface);
/* 164:310 */         if (checkResult != null) {
/* 165:311 */           isMxBean = checkResult.booleanValue();
/* 166:    */         }
/* 167:    */       }
/* 168:314 */       if (isMxBean) {
/* 169:315 */         return iface;
/* 170:    */       }
/* 171:    */     }
/* 172:318 */     return getMXBeanInterface(clazz.getSuperclass());
/* 173:    */   }
/* 174:    */   
/* 175:    */   public static boolean isMXBeanSupportAvailable()
/* 176:    */   {
/* 177:327 */     return mxBeanAnnotationAvailable;
/* 178:    */   }
/* 179:    */   
/* 180:    */   private static class MXBeanChecker
/* 181:    */   {
/* 182:    */     public static Boolean evaluateMXBeanAnnotation(Class<?> iface)
/* 183:    */     {
/* 184:337 */       MXBean mxBean = (MXBean)iface.getAnnotation(MXBean.class);
/* 185:338 */       return mxBean != null ? Boolean.valueOf(mxBean.value()) : null;
/* 186:    */     }
/* 187:    */   }
/* 188:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.JmxUtils
 * JD-Core Version:    0.7.0.1
 */