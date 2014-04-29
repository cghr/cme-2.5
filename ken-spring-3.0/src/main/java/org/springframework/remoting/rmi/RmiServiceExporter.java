/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.rmi.AlreadyBoundException;
/*   4:    */ import java.rmi.NoSuchObjectException;
/*   5:    */ import java.rmi.NotBoundException;
/*   6:    */ import java.rmi.Remote;
/*   7:    */ import java.rmi.RemoteException;
/*   8:    */ import java.rmi.registry.LocateRegistry;
/*   9:    */ import java.rmi.registry.Registry;
/*  10:    */ import java.rmi.server.RMIClientSocketFactory;
/*  11:    */ import java.rmi.server.RMIServerSocketFactory;
/*  12:    */ import java.rmi.server.UnicastRemoteObject;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.beans.factory.DisposableBean;
/*  15:    */ import org.springframework.beans.factory.InitializingBean;
/*  16:    */ 
/*  17:    */ public class RmiServiceExporter
/*  18:    */   extends RmiBasedExporter
/*  19:    */   implements InitializingBean, DisposableBean
/*  20:    */ {
/*  21:    */   private String serviceName;
/*  22: 73 */   private int servicePort = 0;
/*  23:    */   private RMIClientSocketFactory clientSocketFactory;
/*  24:    */   private RMIServerSocketFactory serverSocketFactory;
/*  25:    */   private Registry registry;
/*  26:    */   private String registryHost;
/*  27: 83 */   private int registryPort = 1099;
/*  28:    */   private RMIClientSocketFactory registryClientSocketFactory;
/*  29:    */   private RMIServerSocketFactory registryServerSocketFactory;
/*  30: 89 */   private boolean alwaysCreateRegistry = false;
/*  31: 91 */   private boolean replaceExistingBinding = true;
/*  32:    */   private Remote exportedObject;
/*  33: 95 */   private boolean createdRegistry = false;
/*  34:    */   
/*  35:    */   public void setServiceName(String serviceName)
/*  36:    */   {
/*  37:103 */     this.serviceName = serviceName;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setServicePort(int servicePort)
/*  41:    */   {
/*  42:111 */     this.servicePort = servicePort;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory)
/*  46:    */   {
/*  47:124 */     this.clientSocketFactory = clientSocketFactory;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory)
/*  51:    */   {
/*  52:137 */     this.serverSocketFactory = serverSocketFactory;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setRegistry(Registry registry)
/*  56:    */   {
/*  57:155 */     this.registry = registry;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setRegistryHost(String registryHost)
/*  61:    */   {
/*  62:164 */     this.registryHost = registryHost;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setRegistryPort(int registryPort)
/*  66:    */   {
/*  67:174 */     this.registryPort = registryPort;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setRegistryClientSocketFactory(RMIClientSocketFactory registryClientSocketFactory)
/*  71:    */   {
/*  72:187 */     this.registryClientSocketFactory = registryClientSocketFactory;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setRegistryServerSocketFactory(RMIServerSocketFactory registryServerSocketFactory)
/*  76:    */   {
/*  77:200 */     this.registryServerSocketFactory = registryServerSocketFactory;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void setAlwaysCreateRegistry(boolean alwaysCreateRegistry)
/*  81:    */   {
/*  82:211 */     this.alwaysCreateRegistry = alwaysCreateRegistry;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setReplaceExistingBinding(boolean replaceExistingBinding)
/*  86:    */   {
/*  87:224 */     this.replaceExistingBinding = replaceExistingBinding;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void afterPropertiesSet()
/*  91:    */     throws RemoteException
/*  92:    */   {
/*  93:229 */     prepare();
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void prepare()
/*  97:    */     throws RemoteException
/*  98:    */   {
/*  99:238 */     checkService();
/* 100:240 */     if (this.serviceName == null) {
/* 101:241 */       throw new IllegalArgumentException("Property 'serviceName' is required");
/* 102:    */     }
/* 103:245 */     if ((this.clientSocketFactory instanceof RMIServerSocketFactory)) {
/* 104:246 */       this.serverSocketFactory = ((RMIServerSocketFactory)this.clientSocketFactory);
/* 105:    */     }
/* 106:248 */     if (((this.clientSocketFactory != null) && (this.serverSocketFactory == null)) || (
/* 107:249 */       (this.clientSocketFactory == null) && (this.serverSocketFactory != null))) {
/* 108:250 */       throw new IllegalArgumentException(
/* 109:251 */         "Both RMIClientSocketFactory and RMIServerSocketFactory or none required");
/* 110:    */     }
/* 111:255 */     if ((this.registryClientSocketFactory instanceof RMIServerSocketFactory)) {
/* 112:256 */       this.registryServerSocketFactory = ((RMIServerSocketFactory)this.registryClientSocketFactory);
/* 113:    */     }
/* 114:258 */     if ((this.registryClientSocketFactory == null) && (this.registryServerSocketFactory != null)) {
/* 115:259 */       throw new IllegalArgumentException(
/* 116:260 */         "RMIServerSocketFactory without RMIClientSocketFactory for registry not supported");
/* 117:    */     }
/* 118:263 */     this.createdRegistry = false;
/* 119:266 */     if (this.registry == null)
/* 120:    */     {
/* 121:267 */       this.registry = 
/* 122:268 */         getRegistry(this.registryHost, this.registryPort, this.registryClientSocketFactory, this.registryServerSocketFactory);
/* 123:269 */       this.createdRegistry = true;
/* 124:    */     }
/* 125:273 */     this.exportedObject = getObjectToExport();
/* 126:275 */     if (this.logger.isInfoEnabled()) {
/* 127:276 */       this.logger.info("Binding service '" + this.serviceName + "' to RMI registry: " + this.registry);
/* 128:    */     }
/* 129:280 */     if (this.clientSocketFactory != null) {
/* 130:281 */       UnicastRemoteObject.exportObject(
/* 131:282 */         this.exportedObject, this.servicePort, this.clientSocketFactory, this.serverSocketFactory);
/* 132:    */     } else {
/* 133:285 */       UnicastRemoteObject.exportObject(this.exportedObject, this.servicePort);
/* 134:    */     }
/* 135:    */     try
/* 136:    */     {
/* 137:290 */       if (this.replaceExistingBinding) {
/* 138:291 */         this.registry.rebind(this.serviceName, this.exportedObject);
/* 139:    */       } else {
/* 140:294 */         this.registry.bind(this.serviceName, this.exportedObject);
/* 141:    */       }
/* 142:    */     }
/* 143:    */     catch (AlreadyBoundException ex)
/* 144:    */     {
/* 145:299 */       unexportObjectSilently();
/* 146:300 */       throw new IllegalStateException(
/* 147:301 */         "Already an RMI object bound for name '" + this.serviceName + "': " + ex.toString());
/* 148:    */     }
/* 149:    */     catch (RemoteException ex)
/* 150:    */     {
/* 151:305 */       unexportObjectSilently();
/* 152:306 */       throw ex;
/* 153:    */     }
/* 154:    */   }
/* 155:    */   
/* 156:    */   protected Registry getRegistry(String registryHost, int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/* 157:    */     throws RemoteException
/* 158:    */   {
/* 159:325 */     if (registryHost != null)
/* 160:    */     {
/* 161:327 */       if (this.logger.isInfoEnabled()) {
/* 162:328 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
/* 163:    */       }
/* 164:330 */       Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
/* 165:331 */       testRegistry(reg);
/* 166:332 */       return reg;
/* 167:    */     }
/* 168:336 */     return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/* 169:    */   }
/* 170:    */   
/* 171:    */   protected Registry getRegistry(int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/* 172:    */     throws RemoteException
/* 173:    */   {
/* 174:352 */     if (clientSocketFactory != null)
/* 175:    */     {
/* 176:353 */       if (this.alwaysCreateRegistry)
/* 177:    */       {
/* 178:354 */         this.logger.info("Creating new RMI registry");
/* 179:355 */         return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/* 180:    */       }
/* 181:357 */       if (this.logger.isInfoEnabled()) {
/* 182:358 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "', using custom socket factory");
/* 183:    */       }
/* 184:360 */       synchronized (LocateRegistry.class)
/* 185:    */       {
/* 186:    */         try
/* 187:    */         {
/* 188:363 */           Registry reg = LocateRegistry.getRegistry(null, registryPort, clientSocketFactory);
/* 189:364 */           testRegistry(reg);
/* 190:365 */           return reg;
/* 191:    */         }
/* 192:    */         catch (RemoteException ex)
/* 193:    */         {
/* 194:368 */           this.logger.debug("RMI registry access threw exception", ex);
/* 195:369 */           this.logger.info("Could not detect RMI registry - creating new one");
/* 196:    */           
/* 197:371 */           return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/* 198:    */         }
/* 199:    */       }
/* 200:    */     }
/* 201:377 */     return getRegistry(registryPort);
/* 202:    */   }
/* 203:    */   
/* 204:    */   protected Registry getRegistry(int registryPort)
/* 205:    */     throws RemoteException
/* 206:    */   {
/* 207:388 */     if (this.alwaysCreateRegistry)
/* 208:    */     {
/* 209:389 */       this.logger.info("Creating new RMI registry");
/* 210:390 */       return LocateRegistry.createRegistry(registryPort);
/* 211:    */     }
/* 212:392 */     if (this.logger.isInfoEnabled()) {
/* 213:393 */       this.logger.info("Looking for RMI registry at port '" + registryPort + "'");
/* 214:    */     }
/* 215:395 */     synchronized (LocateRegistry.class)
/* 216:    */     {
/* 217:    */       try
/* 218:    */       {
/* 219:398 */         Registry reg = LocateRegistry.getRegistry(registryPort);
/* 220:399 */         testRegistry(reg);
/* 221:400 */         return reg;
/* 222:    */       }
/* 223:    */       catch (RemoteException ex)
/* 224:    */       {
/* 225:403 */         this.logger.debug("RMI registry access threw exception", ex);
/* 226:404 */         this.logger.info("Could not detect RMI registry - creating new one");
/* 227:    */         
/* 228:406 */         return LocateRegistry.createRegistry(registryPort);
/* 229:    */       }
/* 230:    */     }
/* 231:    */   }
/* 232:    */   
/* 233:    */   protected void testRegistry(Registry registry)
/* 234:    */     throws RemoteException
/* 235:    */   {
/* 236:420 */     registry.list();
/* 237:    */   }
/* 238:    */   
/* 239:    */   public void destroy()
/* 240:    */     throws RemoteException
/* 241:    */   {
/* 242:428 */     if (this.logger.isInfoEnabled()) {
/* 243:429 */       this.logger.info("Unbinding RMI service '" + this.serviceName + 
/* 244:430 */         "' from registry" + (this.createdRegistry ? " at port '" + this.registryPort + "'" : ""));
/* 245:    */     }
/* 246:    */     try
/* 247:    */     {
/* 248:433 */       this.registry.unbind(this.serviceName);
/* 249:    */     }
/* 250:    */     catch (NotBoundException ex)
/* 251:    */     {
/* 252:436 */       if (this.logger.isWarnEnabled()) {
/* 253:437 */         this.logger.warn("RMI service '" + this.serviceName + "' is not bound to registry" + (
/* 254:438 */           this.createdRegistry ? " at port '" + this.registryPort + "' anymore" : ""), ex);
/* 255:    */       }
/* 256:    */     }
/* 257:    */     finally
/* 258:    */     {
/* 259:442 */       unexportObjectSilently();
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   private void unexportObjectSilently()
/* 264:    */   {
/* 265:    */     try
/* 266:    */     {
/* 267:451 */       UnicastRemoteObject.unexportObject(this.exportedObject, true);
/* 268:    */     }
/* 269:    */     catch (NoSuchObjectException ex)
/* 270:    */     {
/* 271:454 */       if (this.logger.isWarnEnabled()) {
/* 272:455 */         this.logger.warn("RMI object for service '" + this.serviceName + "' isn't exported anymore", ex);
/* 273:    */       }
/* 274:    */     }
/* 275:    */   }
/* 276:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiServiceExporter
 * JD-Core Version:    0.7.0.1
 */