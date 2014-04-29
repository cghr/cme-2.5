/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.util.Enumeration;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import java.util.Properties;
/*  11:    */ import java.util.ResourceBundle;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.springframework.beans.BeansException;
/*  14:    */ import org.springframework.beans.MutablePropertyValues;
/*  15:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  16:    */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*  17:    */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*  18:    */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*  19:    */ import org.springframework.core.io.Resource;
/*  20:    */ import org.springframework.core.io.support.EncodedResource;
/*  21:    */ import org.springframework.util.DefaultPropertiesPersister;
/*  22:    */ import org.springframework.util.PropertiesPersister;
/*  23:    */ import org.springframework.util.StringUtils;
/*  24:    */ 
/*  25:    */ public class PropertiesBeanDefinitionReader
/*  26:    */   extends AbstractBeanDefinitionReader
/*  27:    */ {
/*  28:    */   public static final String TRUE_VALUE = "true";
/*  29:    */   public static final String SEPARATOR = ".";
/*  30:    */   public static final String CLASS_KEY = "(class)";
/*  31:    */   public static final String PARENT_KEY = "(parent)";
/*  32:    */   public static final String SCOPE_KEY = "(scope)";
/*  33:    */   public static final String SINGLETON_KEY = "(singleton)";
/*  34:    */   public static final String ABSTRACT_KEY = "(abstract)";
/*  35:    */   public static final String LAZY_INIT_KEY = "(lazy-init)";
/*  36:    */   public static final String REF_SUFFIX = "(ref)";
/*  37:    */   public static final String REF_PREFIX = "*";
/*  38:    */   public static final String CONSTRUCTOR_ARG_PREFIX = "$";
/*  39:    */   private String defaultParentBean;
/*  40:145 */   private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
/*  41:    */   
/*  42:    */   public PropertiesBeanDefinitionReader(BeanDefinitionRegistry registry)
/*  43:    */   {
/*  44:154 */     super(registry);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setDefaultParentBean(String defaultParentBean)
/*  48:    */   {
/*  49:171 */     this.defaultParentBean = defaultParentBean;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getDefaultParentBean()
/*  53:    */   {
/*  54:178 */     return this.defaultParentBean;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setPropertiesPersister(PropertiesPersister propertiesPersister)
/*  58:    */   {
/*  59:187 */     this.propertiesPersister = 
/*  60:188 */       (propertiesPersister != null ? propertiesPersister : new DefaultPropertiesPersister());
/*  61:    */   }
/*  62:    */   
/*  63:    */   public PropertiesPersister getPropertiesPersister()
/*  64:    */   {
/*  65:195 */     return this.propertiesPersister;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int loadBeanDefinitions(Resource resource)
/*  69:    */     throws BeanDefinitionStoreException
/*  70:    */   {
/*  71:208 */     return loadBeanDefinitions(new EncodedResource(resource), null);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int loadBeanDefinitions(Resource resource, String prefix)
/*  75:    */     throws BeanDefinitionStoreException
/*  76:    */   {
/*  77:220 */     return loadBeanDefinitions(new EncodedResource(resource), prefix);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int loadBeanDefinitions(EncodedResource encodedResource)
/*  81:    */     throws BeanDefinitionStoreException
/*  82:    */   {
/*  83:231 */     return loadBeanDefinitions(encodedResource, null);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int loadBeanDefinitions(EncodedResource encodedResource, String prefix)
/*  87:    */     throws BeanDefinitionStoreException
/*  88:    */   {
/*  89:246 */     Properties props = new Properties();
/*  90:    */     try
/*  91:    */     {
/*  92:248 */       InputStream is = encodedResource.getResource().getInputStream();
/*  93:    */       try
/*  94:    */       {
/*  95:250 */         if (encodedResource.getEncoding() != null) {
/*  96:251 */           getPropertiesPersister().load(props, new InputStreamReader(is, encodedResource.getEncoding()));
/*  97:    */         } else {
/*  98:254 */           getPropertiesPersister().load(props, is);
/*  99:    */         }
/* 100:    */       }
/* 101:    */       finally
/* 102:    */       {
/* 103:258 */         is.close();
/* 104:    */       }
/* 105:260 */       return registerBeanDefinitions(props, prefix, encodedResource.getResource().getDescription());
/* 106:    */     }
/* 107:    */     catch (IOException ex)
/* 108:    */     {
/* 109:263 */       throw new BeanDefinitionStoreException("Could not parse properties from " + encodedResource.getResource(), ex);
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int registerBeanDefinitions(ResourceBundle rb)
/* 114:    */     throws BeanDefinitionStoreException
/* 115:    */   {
/* 116:276 */     return registerBeanDefinitions(rb, null);
/* 117:    */   }
/* 118:    */   
/* 119:    */   public int registerBeanDefinitions(ResourceBundle rb, String prefix)
/* 120:    */     throws BeanDefinitionStoreException
/* 121:    */   {
/* 122:291 */     Map<String, Object> map = new HashMap();
/* 123:292 */     Enumeration keys = rb.getKeys();
/* 124:293 */     while (keys.hasMoreElements())
/* 125:    */     {
/* 126:294 */       String key = (String)keys.nextElement();
/* 127:295 */       map.put(key, rb.getObject(key));
/* 128:    */     }
/* 129:297 */     return registerBeanDefinitions(map, prefix);
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int registerBeanDefinitions(Map map)
/* 133:    */     throws BeansException
/* 134:    */   {
/* 135:312 */     return registerBeanDefinitions(map, null);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public int registerBeanDefinitions(Map map, String prefix)
/* 139:    */     throws BeansException
/* 140:    */   {
/* 141:327 */     return registerBeanDefinitions(map, prefix, "Map " + map);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public int registerBeanDefinitions(Map map, String prefix, String resourceDescription)
/* 145:    */     throws BeansException
/* 146:    */   {
/* 147:347 */     if (prefix == null) {
/* 148:348 */       prefix = "";
/* 149:    */     }
/* 150:350 */     int beanCount = 0;
/* 151:352 */     for (Object key : map.keySet())
/* 152:    */     {
/* 153:353 */       if (!(key instanceof String)) {
/* 154:354 */         throw new IllegalArgumentException("Illegal key [" + key + "]: only Strings allowed");
/* 155:    */       }
/* 156:356 */       String keyString = (String)key;
/* 157:357 */       if (keyString.startsWith(prefix))
/* 158:    */       {
/* 159:359 */         String nameAndProperty = keyString.substring(prefix.length());
/* 160:    */         
/* 161:361 */         int sepIdx = -1;
/* 162:362 */         int propKeyIdx = nameAndProperty.indexOf("[");
/* 163:363 */         if (propKeyIdx != -1) {
/* 164:364 */           sepIdx = nameAndProperty.lastIndexOf(".", propKeyIdx);
/* 165:    */         } else {
/* 166:367 */           sepIdx = nameAndProperty.lastIndexOf(".");
/* 167:    */         }
/* 168:369 */         if (sepIdx != -1)
/* 169:    */         {
/* 170:370 */           String beanName = nameAndProperty.substring(0, sepIdx);
/* 171:371 */           if (this.logger.isDebugEnabled()) {
/* 172:372 */             this.logger.debug("Found bean name '" + beanName + "'");
/* 173:    */           }
/* 174:374 */           if (!getRegistry().containsBeanDefinition(beanName))
/* 175:    */           {
/* 176:376 */             registerBeanDefinition(beanName, map, prefix + beanName, resourceDescription);
/* 177:377 */             beanCount++;
/* 178:    */           }
/* 179:    */         }
/* 180:383 */         else if (this.logger.isDebugEnabled())
/* 181:    */         {
/* 182:384 */           this.logger.debug("Invalid bean name and property [" + nameAndProperty + "]");
/* 183:    */         }
/* 184:    */       }
/* 185:    */     }
/* 186:390 */     return beanCount;
/* 187:    */   }
/* 188:    */   
/* 189:    */   protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription)
/* 190:    */     throws BeansException
/* 191:    */   {
/* 192:406 */     String className = null;
/* 193:407 */     String parent = null;
/* 194:408 */     String scope = "singleton";
/* 195:409 */     boolean isAbstract = false;
/* 196:410 */     boolean lazyInit = false;
/* 197:    */     
/* 198:412 */     ConstructorArgumentValues cas = new ConstructorArgumentValues();
/* 199:413 */     MutablePropertyValues pvs = new MutablePropertyValues();
/* 200:415 */     for (Map.Entry entry : map.entrySet())
/* 201:    */     {
/* 202:416 */       String key = StringUtils.trimWhitespace((String)entry.getKey());
/* 203:417 */       if (key.startsWith(prefix + "."))
/* 204:    */       {
/* 205:418 */         String property = key.substring(prefix.length() + ".".length());
/* 206:419 */         if ("(class)".equals(property))
/* 207:    */         {
/* 208:420 */           className = StringUtils.trimWhitespace((String)entry.getValue());
/* 209:    */         }
/* 210:422 */         else if ("(parent)".equals(property))
/* 211:    */         {
/* 212:423 */           parent = StringUtils.trimWhitespace((String)entry.getValue());
/* 213:    */         }
/* 214:425 */         else if ("(abstract)".equals(property))
/* 215:    */         {
/* 216:426 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 217:427 */           isAbstract = "true".equals(val);
/* 218:    */         }
/* 219:429 */         else if ("(scope)".equals(property))
/* 220:    */         {
/* 221:431 */           scope = StringUtils.trimWhitespace((String)entry.getValue());
/* 222:    */         }
/* 223:433 */         else if ("(singleton)".equals(property))
/* 224:    */         {
/* 225:435 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 226:436 */           scope = (val == null) || ("true".equals(val)) ? "singleton" : 
/* 227:437 */             "prototype";
/* 228:    */         }
/* 229:439 */         else if ("(lazy-init)".equals(property))
/* 230:    */         {
/* 231:440 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 232:441 */           lazyInit = "true".equals(val);
/* 233:    */         }
/* 234:443 */         else if (property.startsWith("$"))
/* 235:    */         {
/* 236:444 */           if (property.endsWith("(ref)"))
/* 237:    */           {
/* 238:445 */             int index = Integer.parseInt(property.substring(1, property.length() - "(ref)".length()));
/* 239:446 */             cas.addIndexedArgumentValue(index, new RuntimeBeanReference(entry.getValue().toString()));
/* 240:    */           }
/* 241:    */           else
/* 242:    */           {
/* 243:449 */             int index = Integer.parseInt(property.substring(1));
/* 244:450 */             cas.addIndexedArgumentValue(index, readValue(entry));
/* 245:    */           }
/* 246:    */         }
/* 247:453 */         else if (property.endsWith("(ref)"))
/* 248:    */         {
/* 249:456 */           property = property.substring(0, property.length() - "(ref)".length());
/* 250:457 */           String ref = StringUtils.trimWhitespace((String)entry.getValue());
/* 251:    */           
/* 252:    */ 
/* 253:    */ 
/* 254:461 */           Object val = new RuntimeBeanReference(ref);
/* 255:462 */           pvs.add(property, val);
/* 256:    */         }
/* 257:    */         else
/* 258:    */         {
/* 259:466 */           pvs.add(property, readValue(entry));
/* 260:    */         }
/* 261:    */       }
/* 262:    */     }
/* 263:471 */     if (this.logger.isDebugEnabled()) {
/* 264:472 */       this.logger.debug("Registering bean definition for bean name '" + beanName + "' with " + pvs);
/* 265:    */     }
/* 266:478 */     if ((parent == null) && (className == null) && (!beanName.equals(this.defaultParentBean))) {
/* 267:479 */       parent = this.defaultParentBean;
/* 268:    */     }
/* 269:    */     try
/* 270:    */     {
/* 271:483 */       AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(
/* 272:484 */         parent, className, getBeanClassLoader());
/* 273:485 */       bd.setScope(scope);
/* 274:486 */       bd.setAbstract(isAbstract);
/* 275:487 */       bd.setLazyInit(lazyInit);
/* 276:488 */       bd.setConstructorArgumentValues(cas);
/* 277:489 */       bd.setPropertyValues(pvs);
/* 278:490 */       getRegistry().registerBeanDefinition(beanName, bd);
/* 279:    */     }
/* 280:    */     catch (ClassNotFoundException ex)
/* 281:    */     {
/* 282:493 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
/* 283:    */     }
/* 284:    */     catch (LinkageError err)
/* 285:    */     {
/* 286:496 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, err);
/* 287:    */     }
/* 288:    */   }
/* 289:    */   
/* 290:    */   private Object readValue(Map.Entry entry)
/* 291:    */   {
/* 292:505 */     Object val = entry.getValue();
/* 293:506 */     if ((val instanceof String))
/* 294:    */     {
/* 295:507 */       String strVal = (String)val;
/* 296:509 */       if (strVal.startsWith("*"))
/* 297:    */       {
/* 298:511 */         String targetName = strVal.substring(1);
/* 299:512 */         if (targetName.startsWith("*")) {
/* 300:514 */           val = targetName;
/* 301:    */         } else {
/* 302:517 */           val = new RuntimeBeanReference(targetName);
/* 303:    */         }
/* 304:    */       }
/* 305:    */     }
/* 306:521 */     return val;
/* 307:    */   }
/* 308:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.PropertiesBeanDefinitionReader
 * JD-Core Version:    0.7.0.1
 */