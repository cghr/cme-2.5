/*   1:    */ package org.springframework.remoting.support;
/*   2:    */ 
/*   3:    */ import com.sun.net.httpserver.Authenticator;
/*   4:    */ import com.sun.net.httpserver.Filter;
/*   5:    */ import com.sun.net.httpserver.HttpContext;
/*   6:    */ import com.sun.net.httpserver.HttpHandler;
/*   7:    */ import com.sun.net.httpserver.HttpServer;
/*   8:    */ import java.io.IOException;
/*   9:    */ import java.net.InetSocketAddress;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import java.util.concurrent.Executor;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.apache.commons.logging.LogFactory;
/*  15:    */ import org.springframework.beans.factory.DisposableBean;
/*  16:    */ import org.springframework.beans.factory.FactoryBean;
/*  17:    */ import org.springframework.beans.factory.InitializingBean;
/*  18:    */ 
/*  19:    */ public class SimpleHttpServerFactoryBean
/*  20:    */   implements FactoryBean<HttpServer>, InitializingBean, DisposableBean
/*  21:    */ {
/*  22: 56 */   protected final Log logger = LogFactory.getLog(getClass());
/*  23: 58 */   private int port = 8080;
/*  24:    */   private String hostname;
/*  25: 62 */   private int backlog = -1;
/*  26: 64 */   private int shutdownDelay = 0;
/*  27:    */   private Executor executor;
/*  28:    */   private Map<String, HttpHandler> contexts;
/*  29:    */   private List<Filter> filters;
/*  30:    */   private Authenticator authenticator;
/*  31:    */   private HttpServer server;
/*  32:    */   
/*  33:    */   public void setPort(int port)
/*  34:    */   {
/*  35: 81 */     this.port = port;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setHostname(String hostname)
/*  39:    */   {
/*  40: 89 */     this.hostname = hostname;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setBacklog(int backlog)
/*  44:    */   {
/*  45: 97 */     this.backlog = backlog;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setShutdownDelay(int shutdownDelay)
/*  49:    */   {
/*  50:105 */     this.shutdownDelay = shutdownDelay;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setExecutor(Executor executor)
/*  54:    */   {
/*  55:113 */     this.executor = executor;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setContexts(Map<String, HttpHandler> contexts)
/*  59:    */   {
/*  60:126 */     this.contexts = contexts;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setFilters(List<Filter> filters)
/*  64:    */   {
/*  65:134 */     this.filters = filters;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setAuthenticator(Authenticator authenticator)
/*  69:    */   {
/*  70:142 */     this.authenticator = authenticator;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void afterPropertiesSet()
/*  74:    */     throws IOException
/*  75:    */   {
/*  76:147 */     InetSocketAddress address = this.hostname != null ? 
/*  77:148 */       new InetSocketAddress(this.hostname, this.port) : new InetSocketAddress(this.port);
/*  78:149 */     this.server = HttpServer.create(address, this.backlog);
/*  79:150 */     if (this.executor != null) {
/*  80:151 */       this.server.setExecutor(this.executor);
/*  81:    */     }
/*  82:153 */     if (this.contexts != null) {
/*  83:154 */       for (String key : this.contexts.keySet())
/*  84:    */       {
/*  85:155 */         HttpContext httpContext = this.server.createContext(key, (HttpHandler)this.contexts.get(key));
/*  86:156 */         if (this.filters != null) {
/*  87:157 */           httpContext.getFilters().addAll(this.filters);
/*  88:    */         }
/*  89:159 */         if (this.authenticator != null) {
/*  90:160 */           httpContext.setAuthenticator(this.authenticator);
/*  91:    */         }
/*  92:    */       }
/*  93:    */     }
/*  94:164 */     if (this.logger.isInfoEnabled()) {
/*  95:165 */       this.logger.info("Starting HttpServer at address " + address);
/*  96:    */     }
/*  97:167 */     this.server.start();
/*  98:    */   }
/*  99:    */   
/* 100:    */   public HttpServer getObject()
/* 101:    */   {
/* 102:171 */     return this.server;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public Class<? extends HttpServer> getObjectType()
/* 106:    */   {
/* 107:175 */     return this.server != null ? this.server.getClass() : HttpServer.class;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isSingleton()
/* 111:    */   {
/* 112:179 */     return true;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void destroy()
/* 116:    */   {
/* 117:183 */     this.logger.info("Stopping HttpServer");
/* 118:184 */     this.server.stop(this.shutdownDelay);
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.SimpleHttpServerFactoryBean
 * JD-Core Version:    0.7.0.1
 */