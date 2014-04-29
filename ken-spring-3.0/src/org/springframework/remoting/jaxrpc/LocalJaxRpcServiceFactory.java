/*   1:    */ package org.springframework.remoting.jaxrpc;
/*   2:    */ 
/*   3:    */ import java.net.URL;
/*   4:    */ import java.util.Properties;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import javax.xml.rpc.Service;
/*   7:    */ import javax.xml.rpc.ServiceException;
/*   8:    */ import javax.xml.rpc.ServiceFactory;
/*   9:    */ import org.apache.commons.logging.Log;
/*  10:    */ import org.apache.commons.logging.LogFactory;
/*  11:    */ import org.springframework.beans.BeanUtils;
/*  12:    */ 
/*  13:    */ @Deprecated
/*  14:    */ public class LocalJaxRpcServiceFactory
/*  15:    */ {
/*  16: 52 */   protected final Log logger = LogFactory.getLog(getClass());
/*  17:    */   private ServiceFactory serviceFactory;
/*  18:    */   private Class serviceFactoryClass;
/*  19:    */   private URL wsdlDocumentUrl;
/*  20:    */   private String namespaceUri;
/*  21:    */   private String serviceName;
/*  22:    */   private Class jaxRpcServiceInterface;
/*  23:    */   private Properties jaxRpcServiceProperties;
/*  24:    */   private JaxRpcServicePostProcessor[] servicePostProcessors;
/*  25:    */   
/*  26:    */   public void setServiceFactory(ServiceFactory serviceFactory)
/*  27:    */   {
/*  28: 78 */     this.serviceFactory = serviceFactory;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ServiceFactory getServiceFactory()
/*  32:    */   {
/*  33: 85 */     return this.serviceFactory;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setServiceFactoryClass(Class serviceFactoryClass)
/*  37:    */   {
/*  38: 96 */     if ((serviceFactoryClass != null) && (!ServiceFactory.class.isAssignableFrom(serviceFactoryClass))) {
/*  39: 97 */       throw new IllegalArgumentException("'serviceFactoryClass' must implement [javax.xml.rpc.ServiceFactory]");
/*  40:    */     }
/*  41: 99 */     this.serviceFactoryClass = serviceFactoryClass;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Class getServiceFactoryClass()
/*  45:    */   {
/*  46:106 */     return this.serviceFactoryClass;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setWsdlDocumentUrl(URL wsdlDocumentUrl)
/*  50:    */   {
/*  51:113 */     this.wsdlDocumentUrl = wsdlDocumentUrl;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public URL getWsdlDocumentUrl()
/*  55:    */   {
/*  56:120 */     return this.wsdlDocumentUrl;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setNamespaceUri(String namespaceUri)
/*  60:    */   {
/*  61:128 */     this.namespaceUri = (namespaceUri != null ? namespaceUri.trim() : null);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getNamespaceUri()
/*  65:    */   {
/*  66:135 */     return this.namespaceUri;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void setServiceName(String serviceName)
/*  70:    */   {
/*  71:146 */     this.serviceName = serviceName;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public String getServiceName()
/*  75:    */   {
/*  76:153 */     return this.serviceName;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public void setJaxRpcServiceInterface(Class jaxRpcServiceInterface)
/*  80:    */   {
/*  81:169 */     this.jaxRpcServiceInterface = jaxRpcServiceInterface;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public Class getJaxRpcServiceInterface()
/*  85:    */   {
/*  86:176 */     return this.jaxRpcServiceInterface;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void setJaxRpcServiceProperties(Properties jaxRpcServiceProperties)
/*  90:    */   {
/*  91:186 */     this.jaxRpcServiceProperties = jaxRpcServiceProperties;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public Properties getJaxRpcServiceProperties()
/*  95:    */   {
/*  96:193 */     return this.jaxRpcServiceProperties;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setServicePostProcessors(JaxRpcServicePostProcessor[] servicePostProcessors)
/* 100:    */   {
/* 101:208 */     this.servicePostProcessors = servicePostProcessors;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public JaxRpcServicePostProcessor[] getServicePostProcessors()
/* 105:    */   {
/* 106:216 */     return this.servicePostProcessors;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Service createJaxRpcService()
/* 110:    */     throws ServiceException
/* 111:    */   {
/* 112:227 */     ServiceFactory serviceFactory = getServiceFactory();
/* 113:228 */     if (serviceFactory == null) {
/* 114:229 */       serviceFactory = createServiceFactory();
/* 115:    */     }
/* 116:233 */     Service service = createService(serviceFactory);
/* 117:    */     
/* 118:    */ 
/* 119:236 */     postProcessJaxRpcService(service);
/* 120:    */     
/* 121:238 */     return service;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected QName getQName(String name)
/* 125:    */   {
/* 126:247 */     return getNamespaceUri() != null ? new QName(getNamespaceUri(), name) : new QName(name);
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected ServiceFactory createServiceFactory()
/* 130:    */     throws ServiceException
/* 131:    */   {
/* 132:258 */     if (getServiceFactoryClass() != null) {
/* 133:259 */       return (ServiceFactory)BeanUtils.instantiateClass(getServiceFactoryClass());
/* 134:    */     }
/* 135:262 */     return ServiceFactory.newInstance();
/* 136:    */   }
/* 137:    */   
/* 138:    */   protected Service createService(ServiceFactory serviceFactory)
/* 139:    */     throws ServiceException
/* 140:    */   {
/* 141:276 */     if ((getServiceName() == null) && (getJaxRpcServiceInterface() == null)) {
/* 142:277 */       throw new IllegalArgumentException("Either 'serviceName' or 'jaxRpcServiceInterface' is required");
/* 143:    */     }
/* 144:280 */     if (getJaxRpcServiceInterface() != null)
/* 145:    */     {
/* 146:283 */       if ((getWsdlDocumentUrl() != null) || (getJaxRpcServiceProperties() != null)) {
/* 147:284 */         return serviceFactory.loadService(
/* 148:285 */           getWsdlDocumentUrl(), getJaxRpcServiceInterface(), getJaxRpcServiceProperties());
/* 149:    */       }
/* 150:287 */       return serviceFactory.loadService(getJaxRpcServiceInterface());
/* 151:    */     }
/* 152:291 */     QName serviceQName = getQName(getServiceName());
/* 153:292 */     if (getJaxRpcServiceProperties() != null) {
/* 154:294 */       return serviceFactory.loadService(getWsdlDocumentUrl(), serviceQName, getJaxRpcServiceProperties());
/* 155:    */     }
/* 156:296 */     if (getWsdlDocumentUrl() != null) {
/* 157:297 */       return serviceFactory.createService(getWsdlDocumentUrl(), serviceQName);
/* 158:    */     }
/* 159:299 */     return serviceFactory.createService(serviceQName);
/* 160:    */   }
/* 161:    */   
/* 162:    */   protected void postProcessJaxRpcService(Service service)
/* 163:    */   {
/* 164:315 */     JaxRpcServicePostProcessor[] postProcessors = getServicePostProcessors();
/* 165:316 */     if (postProcessors != null) {
/* 166:317 */       for (int i = 0; i < postProcessors.length; i++) {
/* 167:318 */         postProcessors[i].postProcessJaxRpcService(service);
/* 168:    */       }
/* 169:    */     }
/* 170:    */   }
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.LocalJaxRpcServiceFactory
 * JD-Core Version:    0.7.0.1
 */