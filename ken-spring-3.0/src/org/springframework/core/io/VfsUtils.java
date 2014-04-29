/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.lang.reflect.Field;
/*   7:    */ import java.lang.reflect.InvocationTargetException;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.net.URI;
/*  10:    */ import java.net.URL;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.core.NestedIOException;
/*  14:    */ import org.springframework.util.ReflectionUtils;
/*  15:    */ 
/*  16:    */ public abstract class VfsUtils
/*  17:    */ {
/*  18: 47 */   private static final Log logger = LogFactory.getLog(VfsUtils.class);
/*  19:    */   private static final String VFS2_PKG = "org.jboss.virtual.";
/*  20:    */   private static final String VFS3_PKG = "org.jboss.vfs.";
/*  21:    */   private static final String VFS_NAME = "VFS";
/*  22:    */   private static VFS_VER version;
/*  23:    */   
/*  24:    */   private static enum VFS_VER
/*  25:    */   {
/*  26: 53 */     V2,  V3;
/*  27:    */   }
/*  28:    */   
/*  29: 57 */   private static Method VFS_METHOD_GET_ROOT_URL = null;
/*  30: 58 */   private static Method VFS_METHOD_GET_ROOT_URI = null;
/*  31: 60 */   private static Method VIRTUAL_FILE_METHOD_EXISTS = null;
/*  32:    */   private static Method VIRTUAL_FILE_METHOD_GET_SIZE;
/*  33:    */   private static Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
/*  34:    */   private static Method VIRTUAL_FILE_METHOD_GET_CHILD;
/*  35:    */   private static Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
/*  36:    */   private static Method VIRTUAL_FILE_METHOD_TO_URL;
/*  37:    */   private static Method VIRTUAL_FILE_METHOD_TO_URI;
/*  38:    */   private static Method VIRTUAL_FILE_METHOD_GET_NAME;
/*  39:    */   private static Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
/*  40:    */   protected static Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
/*  41:    */   protected static Method VIRTUAL_FILE_METHOD_VISIT;
/*  42: 72 */   private static Method VFS_UTILS_METHOD_IS_NESTED_FILE = null;
/*  43: 73 */   private static Method VFS_UTILS_METHOD_GET_COMPATIBLE_URI = null;
/*  44: 74 */   private static Field VISITOR_ATTRIBUTES_FIELD_RECURSE = null;
/*  45: 75 */   private static Method GET_PHYSICAL_FILE = null;
/*  46:    */   
/*  47:    */   static
/*  48:    */   {
/*  49: 78 */     ClassLoader loader = VfsUtils.class.getClassLoader();
/*  50:    */     try
/*  51:    */     {
/*  52: 84 */       Class<?> vfsClass = loader.loadClass("org.jboss.vfs.VFS");
/*  53: 85 */       version = VFS_VER.V3;
/*  54: 86 */       String pkg = "org.jboss.vfs.";
/*  55: 88 */       if (logger.isDebugEnabled()) {
/*  56: 89 */         logger.debug("JBoss VFS packages for JBoss AS 6 found");
/*  57:    */       }
/*  58:    */     }
/*  59:    */     catch (ClassNotFoundException localClassNotFoundException1)
/*  60:    */     {
/*  61: 94 */       if (logger.isDebugEnabled()) {
/*  62: 95 */         logger.debug("JBoss VFS packages for JBoss AS 6 not found; falling back to JBoss AS 5 packages");
/*  63:    */       }
/*  64:    */       try
/*  65:    */       {
/*  66: 97 */         Class<?> vfsClass = loader.loadClass("org.jboss.virtual.VFS");
/*  67:    */         
/*  68: 99 */         version = VFS_VER.V2;
/*  69:100 */         String pkg = "org.jboss.virtual.";
/*  70:102 */         if (logger.isDebugEnabled()) {
/*  71:103 */           logger.debug("JBoss VFS packages for JBoss AS 5 found");
/*  72:    */         }
/*  73:    */       }
/*  74:    */       catch (ClassNotFoundException ex1)
/*  75:    */       {
/*  76:105 */         logger.error("JBoss VFS packages (for both JBoss AS 5 and 6) were not found - JBoss VFS support disabled");
/*  77:106 */         throw new IllegalStateException("Cannot detect JBoss VFS packages", ex1);
/*  78:    */       }
/*  79:    */     }
/*  80:    */     try
/*  81:    */     {
/*  82:    */       Class<?> vfsClass;
/*  83:    */       String pkg;
/*  84:112 */       String methodName = VFS_VER.V3.equals(version) ? "getChild" : "getRoot";
/*  85:    */       
/*  86:114 */       VFS_METHOD_GET_ROOT_URL = ReflectionUtils.findMethod(vfsClass, methodName, new Class[] { URL.class });
/*  87:115 */       VFS_METHOD_GET_ROOT_URI = ReflectionUtils.findMethod(vfsClass, methodName, new Class[] { URI.class });
/*  88:    */       
/*  89:117 */       Class<?> virtualFile = loader.loadClass(pkg + "VirtualFile");
/*  90:    */       
/*  91:119 */       VIRTUAL_FILE_METHOD_EXISTS = ReflectionUtils.findMethod(virtualFile, "exists");
/*  92:120 */       VIRTUAL_FILE_METHOD_GET_SIZE = ReflectionUtils.findMethod(virtualFile, "getSize");
/*  93:121 */       VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = ReflectionUtils.findMethod(virtualFile, "openStream");
/*  94:122 */       VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = ReflectionUtils.findMethod(virtualFile, "getLastModified");
/*  95:123 */       VIRTUAL_FILE_METHOD_TO_URI = ReflectionUtils.findMethod(virtualFile, "toURI");
/*  96:124 */       VIRTUAL_FILE_METHOD_TO_URL = ReflectionUtils.findMethod(virtualFile, "toURL");
/*  97:125 */       VIRTUAL_FILE_METHOD_GET_NAME = ReflectionUtils.findMethod(virtualFile, "getName");
/*  98:126 */       VIRTUAL_FILE_METHOD_GET_PATH_NAME = ReflectionUtils.findMethod(virtualFile, "getPathName");
/*  99:127 */       GET_PHYSICAL_FILE = ReflectionUtils.findMethod(virtualFile, "getPhysicalFile");
/* 100:    */       
/* 101:129 */       methodName = VFS_VER.V3.equals(version) ? "getChild" : "findChild";
/* 102:    */       
/* 103:131 */       VIRTUAL_FILE_METHOD_GET_CHILD = ReflectionUtils.findMethod(virtualFile, methodName, new Class[] { String.class });
/* 104:    */       
/* 105:133 */       Class<?> utilsClass = loader.loadClass(pkg + "VFSUtils");
/* 106:    */       
/* 107:135 */       VFS_UTILS_METHOD_GET_COMPATIBLE_URI = ReflectionUtils.findMethod(utilsClass, "getCompatibleURI", new Class[] {
/* 108:136 */         virtualFile });
/* 109:137 */       VFS_UTILS_METHOD_IS_NESTED_FILE = ReflectionUtils.findMethod(utilsClass, "isNestedFile", new Class[] { virtualFile });
/* 110:    */       
/* 111:139 */       VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass(pkg + "VirtualFileVisitor");
/* 112:140 */       VIRTUAL_FILE_METHOD_VISIT = ReflectionUtils.findMethod(virtualFile, "visit", new Class[] { VIRTUAL_FILE_VISITOR_INTERFACE });
/* 113:    */       
/* 114:142 */       Class<?> visitorAttributesClass = loader.loadClass(pkg + "VisitorAttributes");
/* 115:143 */       VISITOR_ATTRIBUTES_FIELD_RECURSE = ReflectionUtils.findField(visitorAttributesClass, "RECURSE");
/* 116:    */     }
/* 117:    */     catch (ClassNotFoundException ex)
/* 118:    */     {
/* 119:146 */       throw new IllegalStateException("Could not detect the JBoss VFS infrastructure", ex);
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected static Object invokeVfsMethod(Method method, Object target, Object... args)
/* 124:    */     throws IOException
/* 125:    */   {
/* 126:    */     try
/* 127:    */     {
/* 128:152 */       return method.invoke(target, args);
/* 129:    */     }
/* 130:    */     catch (InvocationTargetException ex)
/* 131:    */     {
/* 132:155 */       Throwable targetEx = ex.getTargetException();
/* 133:156 */       if ((targetEx instanceof IOException)) {
/* 134:157 */         throw ((IOException)targetEx);
/* 135:    */       }
/* 136:159 */       ReflectionUtils.handleInvocationTargetException(ex);
/* 137:    */     }
/* 138:    */     catch (Exception ex)
/* 139:    */     {
/* 140:162 */       ReflectionUtils.handleReflectionException(ex);
/* 141:    */     }
/* 142:165 */     throw new IllegalStateException("Invalid code path reached");
/* 143:    */   }
/* 144:    */   
/* 145:    */   static boolean exists(Object vfsResource)
/* 146:    */   {
/* 147:    */     try
/* 148:    */     {
/* 149:170 */       return ((Boolean)invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource, new Object[0])).booleanValue();
/* 150:    */     }
/* 151:    */     catch (IOException localIOException) {}
/* 152:173 */     return false;
/* 153:    */   }
/* 154:    */   
/* 155:    */   static boolean isReadable(Object vfsResource)
/* 156:    */   {
/* 157:    */     try
/* 158:    */     {
/* 159:179 */       return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource, new Object[0])).longValue() > 0L;
/* 160:    */     }
/* 161:    */     catch (IOException localIOException) {}
/* 162:182 */     return false;
/* 163:    */   }
/* 164:    */   
/* 165:    */   static long getLastModified(Object vfsResource)
/* 166:    */     throws IOException
/* 167:    */   {
/* 168:187 */     return ((Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource, new Object[0])).longValue();
/* 169:    */   }
/* 170:    */   
/* 171:    */   static InputStream getInputStream(Object vfsResource)
/* 172:    */     throws IOException
/* 173:    */   {
/* 174:191 */     return (InputStream)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource, new Object[0]);
/* 175:    */   }
/* 176:    */   
/* 177:    */   static URL getURL(Object vfsResource)
/* 178:    */     throws IOException
/* 179:    */   {
/* 180:195 */     return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource, new Object[0]);
/* 181:    */   }
/* 182:    */   
/* 183:    */   static URI getURI(Object vfsResource)
/* 184:    */     throws IOException
/* 185:    */   {
/* 186:199 */     return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource, new Object[0]);
/* 187:    */   }
/* 188:    */   
/* 189:    */   static String getName(Object vfsResource)
/* 190:    */   {
/* 191:    */     try
/* 192:    */     {
/* 193:204 */       return (String)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource, new Object[0]);
/* 194:    */     }
/* 195:    */     catch (IOException ex)
/* 196:    */     {
/* 197:207 */       throw new IllegalStateException("Cannot get resource name", ex);
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:    */   static Object getRelative(URL url)
/* 202:    */     throws IOException
/* 203:    */   {
/* 204:212 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/* 205:    */   }
/* 206:    */   
/* 207:    */   static Object getChild(Object vfsResource, String path)
/* 208:    */     throws IOException
/* 209:    */   {
/* 210:216 */     return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, new Object[] { path });
/* 211:    */   }
/* 212:    */   
/* 213:    */   static File getFile(Object vfsResource)
/* 214:    */     throws IOException
/* 215:    */   {
/* 216:220 */     if (VFS_VER.V2.equals(version))
/* 217:    */     {
/* 218:221 */       if (((Boolean)invokeVfsMethod(VFS_UTILS_METHOD_IS_NESTED_FILE, null, new Object[] { vfsResource })).booleanValue()) {
/* 219:222 */         throw new IOException("File resolution not supported for nested resource: " + vfsResource);
/* 220:    */       }
/* 221:    */       try
/* 222:    */       {
/* 223:225 */         return new File((URI)invokeVfsMethod(VFS_UTILS_METHOD_GET_COMPATIBLE_URI, null, new Object[] { vfsResource }));
/* 224:    */       }
/* 225:    */       catch (Exception ex)
/* 226:    */       {
/* 227:228 */         throw new NestedIOException("Failed to obtain File reference for " + vfsResource, ex);
/* 228:    */       }
/* 229:    */     }
/* 230:232 */     return (File)invokeVfsMethod(GET_PHYSICAL_FILE, vfsResource, new Object[0]);
/* 231:    */   }
/* 232:    */   
/* 233:    */   static Object getRoot(URI url)
/* 234:    */     throws IOException
/* 235:    */   {
/* 236:237 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, new Object[] { url });
/* 237:    */   }
/* 238:    */   
/* 239:    */   protected static Object getRoot(URL url)
/* 240:    */     throws IOException
/* 241:    */   {
/* 242:243 */     return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, new Object[] { url });
/* 243:    */   }
/* 244:    */   
/* 245:    */   protected static Object doGetVisitorAttribute()
/* 246:    */   {
/* 247:247 */     return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
/* 248:    */   }
/* 249:    */   
/* 250:    */   protected static String doGetPath(Object resource)
/* 251:    */   {
/* 252:251 */     return (String)ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
/* 253:    */   }
/* 254:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.VfsUtils
 * JD-Core Version:    0.7.0.1
 */