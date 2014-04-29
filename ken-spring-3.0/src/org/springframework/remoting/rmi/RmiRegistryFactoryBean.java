/*   1:    */ package org.springframework.remoting.rmi;
/*   2:    */ 
/*   3:    */ import java.rmi.RemoteException;
/*   4:    */ import java.rmi.registry.LocateRegistry;
/*   5:    */ import java.rmi.registry.Registry;
/*   6:    */ import java.rmi.server.RMIClientSocketFactory;
/*   7:    */ import java.rmi.server.RMIServerSocketFactory;
/*   8:    */ import java.rmi.server.UnicastRemoteObject;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.factory.DisposableBean;
/*  12:    */ import org.springframework.beans.factory.FactoryBean;
/*  13:    */ import org.springframework.beans.factory.InitializingBean;
/*  14:    */ 
/*  15:    */ public class RmiRegistryFactoryBean
/*  16:    */   implements FactoryBean<Registry>, InitializingBean, DisposableBean
/*  17:    */ {
/*  18: 65 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private String host;
/*  20: 69 */   private int port = 1099;
/*  21:    */   private RMIClientSocketFactory clientSocketFactory;
/*  22:    */   private RMIServerSocketFactory serverSocketFactory;
/*  23:    */   private Registry registry;
/*  24: 77 */   private boolean alwaysCreate = false;
/*  25: 79 */   private boolean created = false;
/*  26:    */   
/*  27:    */   public void setHost(String host)
/*  28:    */   {
/*  29: 88 */     this.host = host;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getHost()
/*  33:    */   {
/*  34: 95 */     return this.host;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setPort(int port)
/*  38:    */   {
/*  39:104 */     this.port = port;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getPort()
/*  43:    */   {
/*  44:111 */     return this.port;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory)
/*  48:    */   {
/*  49:124 */     this.clientSocketFactory = clientSocketFactory;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory)
/*  53:    */   {
/*  54:137 */     this.serverSocketFactory = serverSocketFactory;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setAlwaysCreate(boolean alwaysCreate)
/*  58:    */   {
/*  59:148 */     this.alwaysCreate = alwaysCreate;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void afterPropertiesSet()
/*  63:    */     throws Exception
/*  64:    */   {
/*  65:154 */     if ((this.clientSocketFactory instanceof RMIServerSocketFactory)) {
/*  66:155 */       this.serverSocketFactory = ((RMIServerSocketFactory)this.clientSocketFactory);
/*  67:    */     }
/*  68:157 */     if (((this.clientSocketFactory != null) && (this.serverSocketFactory == null)) || (
/*  69:158 */       (this.clientSocketFactory == null) && (this.serverSocketFactory != null))) {
/*  70:159 */       throw new IllegalArgumentException(
/*  71:160 */         "Both RMIClientSocketFactory and RMIServerSocketFactory or none required");
/*  72:    */     }
/*  73:164 */     this.registry = getRegistry(this.host, this.port, this.clientSocketFactory, this.serverSocketFactory);
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected Registry getRegistry(String registryHost, int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/*  77:    */     throws RemoteException
/*  78:    */   {
/*  79:182 */     if (registryHost != null)
/*  80:    */     {
/*  81:184 */       if (this.logger.isInfoEnabled()) {
/*  82:185 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
/*  83:    */       }
/*  84:187 */       Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
/*  85:188 */       testRegistry(reg);
/*  86:189 */       return reg;
/*  87:    */     }
/*  88:193 */     return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected Registry getRegistry(int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/*  92:    */     throws RemoteException
/*  93:    */   {
/*  94:209 */     if (clientSocketFactory != null)
/*  95:    */     {
/*  96:210 */       if (this.alwaysCreate)
/*  97:    */       {
/*  98:211 */         this.logger.info("Creating new RMI registry");
/*  99:212 */         this.created = true;
/* 100:213 */         return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/* 101:    */       }
/* 102:215 */       if (this.logger.isInfoEnabled()) {
/* 103:216 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "', using custom socket factory");
/* 104:    */       }
/* 105:218 */       synchronized (LocateRegistry.class)
/* 106:    */       {
/* 107:    */         try
/* 108:    */         {
/* 109:221 */           Registry reg = LocateRegistry.getRegistry(null, registryPort, clientSocketFactory);
/* 110:222 */           testRegistry(reg);
/* 111:223 */           return reg;
/* 112:    */         }
/* 113:    */         catch (RemoteException ex)
/* 114:    */         {
/* 115:226 */           this.logger.debug("RMI registry access threw exception", ex);
/* 116:227 */           this.logger.info("Could not detect RMI registry - creating new one");
/* 117:    */           
/* 118:229 */           this.created = true;
/* 119:230 */           return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/* 120:    */         }
/* 121:    */       }
/* 122:    */     }
/* 123:236 */     return getRegistry(registryPort);
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected Registry getRegistry(int registryPort)
/* 127:    */     throws RemoteException
/* 128:    */   {
/* 129:247 */     if (this.alwaysCreate)
/* 130:    */     {
/* 131:248 */       this.logger.info("Creating new RMI registry");
/* 132:249 */       this.created = true;
/* 133:250 */       return LocateRegistry.createRegistry(registryPort);
/* 134:    */     }
/* 135:252 */     if (this.logger.isInfoEnabled()) {
/* 136:253 */       this.logger.info("Looking for RMI registry at port '" + registryPort + "'");
/* 137:    */     }
/* 138:255 */     synchronized (LocateRegistry.class)
/* 139:    */     {
/* 140:    */       try
/* 141:    */       {
/* 142:258 */         Registry reg = LocateRegistry.getRegistry(registryPort);
/* 143:259 */         testRegistry(reg);
/* 144:260 */         return reg;
/* 145:    */       }
/* 146:    */       catch (RemoteException ex)
/* 147:    */       {
/* 148:263 */         this.logger.debug("RMI registry access threw exception", ex);
/* 149:264 */         this.logger.info("Could not detect RMI registry - creating new one");
/* 150:    */         
/* 151:266 */         this.created = true;
/* 152:267 */         return LocateRegistry.createRegistry(registryPort);
/* 153:    */       }
/* 154:    */     }
/* 155:    */   }
/* 156:    */   
/* 157:    */   protected void testRegistry(Registry registry)
/* 158:    */     throws RemoteException
/* 159:    */   {
/* 160:281 */     registry.list();
/* 161:    */   }
/* 162:    */   
/* 163:    */   public Registry getObject()
/* 164:    */     throws Exception
/* 165:    */   {
/* 166:286 */     return this.registry;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public Class<? extends Registry> getObjectType()
/* 170:    */   {
/* 171:290 */     return this.registry != null ? this.registry.getClass() : Registry.class;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public boolean isSingleton()
/* 175:    */   {
/* 176:294 */     return true;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void destroy()
/* 180:    */     throws RemoteException
/* 181:    */   {
/* 182:303 */     if (this.created)
/* 183:    */     {
/* 184:304 */       this.logger.info("Unexporting RMI registry");
/* 185:305 */       UnicastRemoteObject.unexportObject(this.registry, true);
/* 186:    */     }
/* 187:    */   }
/* 188:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.rmi.RmiRegistryFactoryBean
 * JD-Core Version:    0.7.0.1
 */