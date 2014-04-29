/*   1:    */ package org.springframework.remoting.jaxws;
/*   2:    */ 
/*   3:    */ import com.sun.net.httpserver.Authenticator;
/*   4:    */ import com.sun.net.httpserver.Filter;
/*   5:    */ import com.sun.net.httpserver.HttpContext;
/*   6:    */ import com.sun.net.httpserver.HttpServer;
/*   7:    */ import java.net.InetSocketAddress;
/*   8:    */ import java.util.List;
/*   9:    */ import javax.jws.WebService;
/*  10:    */ import javax.xml.ws.Endpoint;
/*  11:    */ import javax.xml.ws.WebServiceProvider;
/*  12:    */ import org.apache.commons.logging.Log;
/*  13:    */ import org.apache.commons.logging.LogFactory;
/*  14:    */ 
/*  15:    */ public class SimpleHttpServerJaxWsServiceExporter
/*  16:    */   extends AbstractJaxWsServiceExporter
/*  17:    */ {
/*  18: 51 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private HttpServer server;
/*  20: 55 */   private int port = 8080;
/*  21:    */   private String hostname;
/*  22: 59 */   private int backlog = -1;
/*  23: 61 */   private int shutdownDelay = 0;
/*  24: 63 */   private String basePath = "/";
/*  25:    */   private List<Filter> filters;
/*  26:    */   private Authenticator authenticator;
/*  27: 69 */   private boolean localServer = false;
/*  28:    */   
/*  29:    */   public void setServer(HttpServer server)
/*  30:    */   {
/*  31: 81 */     this.server = server;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setPort(int port)
/*  35:    */   {
/*  36: 90 */     this.port = port;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setHostname(String hostname)
/*  40:    */   {
/*  41:100 */     this.hostname = hostname;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setBacklog(int backlog)
/*  45:    */   {
/*  46:110 */     this.backlog = backlog;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setShutdownDelay(int shutdownDelay)
/*  50:    */   {
/*  51:120 */     this.shutdownDelay = shutdownDelay;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setBasePath(String basePath)
/*  55:    */   {
/*  56:132 */     this.basePath = basePath;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setFilters(List<Filter> filters)
/*  60:    */   {
/*  61:140 */     this.filters = filters;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public void setAuthenticator(Authenticator authenticator)
/*  65:    */   {
/*  66:148 */     this.authenticator = authenticator;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void afterPropertiesSet()
/*  70:    */     throws Exception
/*  71:    */   {
/*  72:154 */     if (this.server == null)
/*  73:    */     {
/*  74:155 */       InetSocketAddress address = this.hostname != null ? 
/*  75:156 */         new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*  76:157 */       this.server = HttpServer.create(address, this.backlog);
/*  77:158 */       if (this.logger.isInfoEnabled()) {
/*  78:159 */         this.logger.info("Starting HttpServer at address " + address);
/*  79:    */       }
/*  80:161 */       this.server.start();
/*  81:162 */       this.localServer = true;
/*  82:    */     }
/*  83:164 */     super.afterPropertiesSet();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected void publishEndpoint(Endpoint endpoint, WebService annotation)
/*  87:    */   {
/*  88:169 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation)
/*  92:    */   {
/*  93:174 */     endpoint.publish(buildHttpContext(endpoint, annotation.serviceName()));
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected HttpContext buildHttpContext(Endpoint endpoint, String serviceName)
/*  97:    */   {
/*  98:184 */     String fullPath = calculateEndpointPath(endpoint, serviceName);
/*  99:185 */     HttpContext httpContext = this.server.createContext(fullPath);
/* 100:186 */     if (this.filters != null) {
/* 101:187 */       httpContext.getFilters().addAll(this.filters);
/* 102:    */     }
/* 103:189 */     if (this.authenticator != null) {
/* 104:190 */       httpContext.setAuthenticator(this.authenticator);
/* 105:    */     }
/* 106:192 */     return httpContext;
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected String calculateEndpointPath(Endpoint endpoint, String serviceName)
/* 110:    */   {
/* 111:202 */     return this.basePath + serviceName;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void destroy()
/* 115:    */   {
/* 116:208 */     super.destroy();
/* 117:209 */     if (this.localServer)
/* 118:    */     {
/* 119:210 */       this.logger.info("Stopping HttpServer");
/* 120:211 */       this.server.stop(this.shutdownDelay);
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxws.SimpleHttpServerJaxWsServiceExporter
 * JD-Core Version:    0.7.0.1
 */