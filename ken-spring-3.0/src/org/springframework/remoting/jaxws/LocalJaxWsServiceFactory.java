/*   1:    */ package org.springframework.remoting.jaxws;
/*   2:    */ 
/*   3:    */ import java.net.URL;
/*   4:    */ import java.util.concurrent.Executor;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import javax.xml.ws.Service;
/*   7:    */ import javax.xml.ws.handler.HandlerResolver;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class LocalJaxWsServiceFactory
/*  11:    */ {
/*  12:    */   private URL wsdlDocumentUrl;
/*  13:    */   private String namespaceUri;
/*  14:    */   private String serviceName;
/*  15:    */   private Executor executor;
/*  16:    */   private HandlerResolver handlerResolver;
/*  17:    */   
/*  18:    */   public void setWsdlDocumentUrl(URL wsdlDocumentUrl)
/*  19:    */   {
/*  20: 58 */     this.wsdlDocumentUrl = wsdlDocumentUrl;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public URL getWsdlDocumentUrl()
/*  24:    */   {
/*  25: 65 */     return this.wsdlDocumentUrl;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setNamespaceUri(String namespaceUri)
/*  29:    */   {
/*  30: 73 */     this.namespaceUri = (namespaceUri != null ? namespaceUri.trim() : null);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getNamespaceUri()
/*  34:    */   {
/*  35: 80 */     return this.namespaceUri;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setServiceName(String serviceName)
/*  39:    */   {
/*  40: 88 */     this.serviceName = serviceName;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getServiceName()
/*  44:    */   {
/*  45: 95 */     return this.serviceName;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setExecutor(Executor executor)
/*  49:    */   {
/*  50:104 */     this.executor = executor;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setHandlerResolver(HandlerResolver handlerResolver)
/*  54:    */   {
/*  55:113 */     this.handlerResolver = handlerResolver;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Service createJaxWsService()
/*  59:    */   {
/*  60:123 */     Assert.notNull(this.serviceName, "No service name specified");
/*  61:124 */     Service service = this.wsdlDocumentUrl != null ? 
/*  62:125 */       Service.create(this.wsdlDocumentUrl, getQName(this.serviceName)) : 
/*  63:126 */       Service.create(getQName(this.serviceName));
/*  64:128 */     if (this.executor != null) {
/*  65:129 */       service.setExecutor(this.executor);
/*  66:    */     }
/*  67:131 */     if (this.handlerResolver != null) {
/*  68:132 */       service.setHandlerResolver(this.handlerResolver);
/*  69:    */     }
/*  70:134 */     return service;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected QName getQName(String name)
/*  74:    */   {
/*  75:143 */     return getNamespaceUri() != null ? new QName(getNamespaceUri(), name) : new QName(name);
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.LocalJaxWsServiceFactory
 * JD-Core Version:    0.7.0.1
 */