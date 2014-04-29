/*  1:   */ package org.springframework.http.client.support;
/*  2:   */ 
/*  3:   */ import java.net.InetSocketAddress;
/*  4:   */ import java.net.Proxy;
/*  5:   */ import java.net.Proxy.Type;
/*  6:   */ import java.net.SocketAddress;
/*  7:   */ import org.springframework.beans.factory.FactoryBean;
/*  8:   */ import org.springframework.beans.factory.InitializingBean;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class ProxyFactoryBean
/* 12:   */   implements FactoryBean<Proxy>, InitializingBean
/* 13:   */ {
/* 14:37 */   private Proxy.Type type = Proxy.Type.HTTP;
/* 15:   */   private String hostname;
/* 16:41 */   private int port = -1;
/* 17:   */   private Proxy proxy;
/* 18:   */   
/* 19:   */   public void setType(Proxy.Type type)
/* 20:   */   {
/* 21:49 */     this.type = type;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setHostname(String hostname)
/* 25:   */   {
/* 26:56 */     this.hostname = hostname;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void setPort(int port)
/* 30:   */   {
/* 31:63 */     this.port = port;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void afterPropertiesSet()
/* 35:   */     throws IllegalArgumentException
/* 36:   */   {
/* 37:67 */     Assert.notNull(this.type, "'type' must not be null");
/* 38:68 */     Assert.hasLength(this.hostname, "'hostname' must not be empty");
/* 39:69 */     Assert.isTrue((this.port >= 0) && (this.port <= 65535), "'port' out of range: " + this.port);
/* 40:   */     
/* 41:71 */     SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
/* 42:72 */     this.proxy = new Proxy(this.type, socketAddress);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public Proxy getObject()
/* 46:   */   {
/* 47:77 */     return this.proxy;
/* 48:   */   }
/* 49:   */   
/* 50:   */   public Class<?> getObjectType()
/* 51:   */   {
/* 52:81 */     return Proxy.class;
/* 53:   */   }
/* 54:   */   
/* 55:   */   public boolean isSingleton()
/* 56:   */   {
/* 57:85 */     return true;
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.client.support.ProxyFactoryBean
 * JD-Core Version:    0.7.0.1
 */