/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.BeanDescriptor;
/*   4:    */ import java.beans.BeanInfo;
/*   5:    */ import java.beans.IntrospectionException;
/*   6:    */ import java.beans.Introspector;
/*   7:    */ import java.beans.PropertyDescriptor;
/*   8:    */ import java.lang.ref.Reference;
/*   9:    */ import java.lang.ref.WeakReference;
/*  10:    */ import java.util.Collections;
/*  11:    */ import java.util.HashSet;
/*  12:    */ import java.util.Iterator;
/*  13:    */ import java.util.LinkedHashMap;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Set;
/*  16:    */ import java.util.WeakHashMap;
/*  17:    */ import org.apache.commons.logging.Log;
/*  18:    */ import org.apache.commons.logging.LogFactory;
/*  19:    */ import org.springframework.util.ClassUtils;
/*  20:    */ import org.springframework.util.StringUtils;
/*  21:    */ 
/*  22:    */ public class CachedIntrospectionResults
/*  23:    */ {
/*  24: 61 */   private static final Log logger = LogFactory.getLog(CachedIntrospectionResults.class);
/*  25: 67 */   static final Set<ClassLoader> acceptedClassLoaders = Collections.synchronizedSet(new HashSet());
/*  26: 74 */   static final Map<Class, Object> classCache = Collections.synchronizedMap(new WeakHashMap());
/*  27:    */   private final BeanInfo beanInfo;
/*  28:    */   private final Map<String, PropertyDescriptor> propertyDescriptorCache;
/*  29:    */   
/*  30:    */   public static void acceptClassLoader(ClassLoader classLoader)
/*  31:    */   {
/*  32: 90 */     if (classLoader != null) {
/*  33: 91 */       acceptedClassLoaders.add(classLoader);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static void clearClassLoader(ClassLoader classLoader)
/*  38:    */   {
/*  39:103 */     if (classLoader == null) {
/*  40:104 */       return;
/*  41:    */     }
/*  42:106 */     synchronized (classCache)
/*  43:    */     {
/*  44:107 */       for (Iterator<Class> it = classCache.keySet().iterator(); it.hasNext();)
/*  45:    */       {
/*  46:108 */         Class beanClass = (Class)it.next();
/*  47:109 */         if (isUnderneathClassLoader(beanClass.getClassLoader(), classLoader)) {
/*  48:110 */           it.remove();
/*  49:    */         }
/*  50:    */       }
/*  51:    */     }
/*  52:114 */     synchronized (acceptedClassLoaders)
/*  53:    */     {
/*  54:115 */       for (Iterator<ClassLoader> it = acceptedClassLoaders.iterator(); it.hasNext();)
/*  55:    */       {
/*  56:116 */         ClassLoader registeredLoader = (ClassLoader)it.next();
/*  57:117 */         if (isUnderneathClassLoader(registeredLoader, classLoader)) {
/*  58:118 */           it.remove();
/*  59:    */         }
/*  60:    */       }
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   static CachedIntrospectionResults forClass(Class beanClass)
/*  65:    */     throws BeansException
/*  66:    */   {
/*  67:134 */     Object value = classCache.get(beanClass);
/*  68:    */     CachedIntrospectionResults results;
/*  69:    */     CachedIntrospectionResults results;
/*  70:135 */     if ((value instanceof Reference))
/*  71:    */     {
/*  72:136 */       Reference ref = (Reference)value;
/*  73:137 */       results = (CachedIntrospectionResults)ref.get();
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:140 */       results = (CachedIntrospectionResults)value;
/*  78:    */     }
/*  79:142 */     if (results == null)
/*  80:    */     {
/*  81:145 */       boolean fullyCacheable = 
/*  82:146 */         (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader())) || 
/*  83:147 */         (isClassLoaderAccepted(beanClass.getClassLoader()));
/*  84:148 */       if ((fullyCacheable) || (!ClassUtils.isPresent(beanClass.getName() + "BeanInfo", beanClass.getClassLoader())))
/*  85:    */       {
/*  86:149 */         results = new CachedIntrospectionResults(beanClass, fullyCacheable);
/*  87:150 */         classCache.put(beanClass, results);
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:153 */         if (logger.isDebugEnabled()) {
/*  92:154 */           logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
/*  93:    */         }
/*  94:156 */         results = new CachedIntrospectionResults(beanClass, true);
/*  95:157 */         classCache.put(beanClass, new WeakReference(results));
/*  96:    */       }
/*  97:    */     }
/*  98:160 */     return results;
/*  99:    */   }
/* 100:    */   
/* 101:    */   private static boolean isClassLoaderAccepted(ClassLoader classLoader)
/* 102:    */   {
/* 103:173 */     ClassLoader[] acceptedLoaderArray = 
/* 104:174 */       (ClassLoader[])acceptedClassLoaders.toArray(new ClassLoader[acceptedClassLoaders.size()]);
/* 105:175 */     for (ClassLoader registeredLoader : acceptedLoaderArray) {
/* 106:176 */       if (isUnderneathClassLoader(classLoader, registeredLoader)) {
/* 107:177 */         return true;
/* 108:    */       }
/* 109:    */     }
/* 110:180 */     return false;
/* 111:    */   }
/* 112:    */   
/* 113:    */   private static boolean isUnderneathClassLoader(ClassLoader candidate, ClassLoader parent)
/* 114:    */   {
/* 115:190 */     if (candidate == null) {
/* 116:191 */       return false;
/* 117:    */     }
/* 118:193 */     if (candidate == parent) {
/* 119:194 */       return true;
/* 120:    */     }
/* 121:196 */     ClassLoader classLoaderToCheck = candidate;
/* 122:197 */     while (classLoaderToCheck != null)
/* 123:    */     {
/* 124:198 */       classLoaderToCheck = classLoaderToCheck.getParent();
/* 125:199 */       if (classLoaderToCheck == parent) {
/* 126:200 */         return true;
/* 127:    */       }
/* 128:    */     }
/* 129:203 */     return false;
/* 130:    */   }
/* 131:    */   
/* 132:    */   private CachedIntrospectionResults(Class beanClass, boolean cacheFullMetadata)
/* 133:    */     throws BeansException
/* 134:    */   {
/* 135:    */     try
/* 136:    */     {
/* 137:221 */       if (logger.isTraceEnabled()) {
/* 138:222 */         logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
/* 139:    */       }
/* 140:224 */       this.beanInfo = new ExtendedBeanInfo(Introspector.getBeanInfo(beanClass));
/* 141:    */       
/* 142:    */ 
/* 143:    */ 
/* 144:    */ 
/* 145:    */ 
/* 146:230 */       Class classToFlush = beanClass;
/* 147:    */       do
/* 148:    */       {
/* 149:232 */         Introspector.flushFromCaches(classToFlush);
/* 150:233 */         classToFlush = classToFlush.getSuperclass();
/* 151:235 */       } while (classToFlush != null);
/* 152:237 */       if (logger.isTraceEnabled()) {
/* 153:238 */         logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
/* 154:    */       }
/* 155:240 */       this.propertyDescriptorCache = new LinkedHashMap();
/* 156:    */       
/* 157:    */ 
/* 158:243 */       PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
/* 159:244 */       for (PropertyDescriptor pd : pds) {
/* 160:245 */         if ((!Class.class.equals(beanClass)) || (!"classLoader".equals(pd.getName())))
/* 161:    */         {
/* 162:249 */           if (logger.isTraceEnabled()) {
/* 163:250 */             logger.trace("Found bean property '" + pd.getName() + "'" + (
/* 164:251 */               pd.getPropertyType() != null ? " of type [" + pd.getPropertyType().getName() + "]" : "") + (
/* 165:252 */               pd.getPropertyEditorClass() != null ? 
/* 166:253 */               "; editor [" + pd.getPropertyEditorClass().getName() + "]" : ""));
/* 167:    */           }
/* 168:255 */           if (cacheFullMetadata) {
/* 169:256 */             pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
/* 170:    */           }
/* 171:258 */           this.propertyDescriptorCache.put(pd.getName(), pd);
/* 172:    */         }
/* 173:    */       }
/* 174:    */     }
/* 175:    */     catch (IntrospectionException ex)
/* 176:    */     {
/* 177:262 */       throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
/* 178:    */     }
/* 179:    */   }
/* 180:    */   
/* 181:    */   BeanInfo getBeanInfo()
/* 182:    */   {
/* 183:267 */     return this.beanInfo;
/* 184:    */   }
/* 185:    */   
/* 186:    */   Class getBeanClass()
/* 187:    */   {
/* 188:271 */     return this.beanInfo.getBeanDescriptor().getBeanClass();
/* 189:    */   }
/* 190:    */   
/* 191:    */   PropertyDescriptor getPropertyDescriptor(String name)
/* 192:    */   {
/* 193:275 */     PropertyDescriptor pd = (PropertyDescriptor)this.propertyDescriptorCache.get(name);
/* 194:276 */     if ((pd == null) && (StringUtils.hasLength(name)))
/* 195:    */     {
/* 196:278 */       pd = (PropertyDescriptor)this.propertyDescriptorCache.get(name.substring(0, 1).toLowerCase() + name.substring(1));
/* 197:279 */       if (pd == null) {
/* 198:280 */         pd = (PropertyDescriptor)this.propertyDescriptorCache.get(name.substring(0, 1).toUpperCase() + name.substring(1));
/* 199:    */       }
/* 200:    */     }
/* 201:283 */     return (pd == null) || ((pd instanceof GenericTypeAwarePropertyDescriptor)) ? pd : 
/* 202:284 */       buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
/* 203:    */   }
/* 204:    */   
/* 205:    */   PropertyDescriptor[] getPropertyDescriptors()
/* 206:    */   {
/* 207:288 */     PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
/* 208:289 */     int i = 0;
/* 209:290 */     for (PropertyDescriptor pd : this.propertyDescriptorCache.values())
/* 210:    */     {
/* 211:291 */       pds[i] = ((pd instanceof GenericTypeAwarePropertyDescriptor) ? pd : 
/* 212:292 */         buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd));
/* 213:293 */       i++;
/* 214:    */     }
/* 215:295 */     return pds;
/* 216:    */   }
/* 217:    */   
/* 218:    */   private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class beanClass, PropertyDescriptor pd)
/* 219:    */   {
/* 220:    */     try
/* 221:    */     {
/* 222:300 */       return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(), 
/* 223:301 */         pd.getWriteMethod(), pd.getPropertyEditorClass());
/* 224:    */     }
/* 225:    */     catch (IntrospectionException ex)
/* 226:    */     {
/* 227:304 */       throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", ex);
/* 228:    */     }
/* 229:    */   }
/* 230:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.CachedIntrospectionResults
 * JD-Core Version:    0.7.0.1
 */