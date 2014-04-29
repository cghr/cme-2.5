/*   1:    */ package org.springframework.jca.support;
/*   2:    */ 
/*   3:    */ import javax.resource.ResourceException;
/*   4:    */ import javax.resource.spi.ConnectionManager;
/*   5:    */ import javax.resource.spi.ManagedConnectionFactory;
/*   6:    */ import org.springframework.beans.factory.FactoryBean;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ 
/*   9:    */ public class LocalConnectionFactoryBean
/*  10:    */   implements FactoryBean<Object>, InitializingBean
/*  11:    */ {
/*  12:    */   private ManagedConnectionFactory managedConnectionFactory;
/*  13:    */   private ConnectionManager connectionManager;
/*  14:    */   private Object connectionFactory;
/*  15:    */   
/*  16:    */   public void setManagedConnectionFactory(ManagedConnectionFactory managedConnectionFactory)
/*  17:    */   {
/*  18: 97 */     this.managedConnectionFactory = managedConnectionFactory;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setConnectionManager(ConnectionManager connectionManager)
/*  22:    */   {
/*  23:109 */     this.connectionManager = connectionManager;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void afterPropertiesSet()
/*  27:    */     throws ResourceException
/*  28:    */   {
/*  29:113 */     if (this.managedConnectionFactory == null) {
/*  30:114 */       throw new IllegalArgumentException("Property 'managedConnectionFactory' is required");
/*  31:    */     }
/*  32:116 */     if (this.connectionManager != null) {
/*  33:117 */       this.connectionFactory = this.managedConnectionFactory.createConnectionFactory(this.connectionManager);
/*  34:    */     } else {
/*  35:120 */       this.connectionFactory = this.managedConnectionFactory.createConnectionFactory();
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Object getObject()
/*  40:    */   {
/*  41:126 */     return this.connectionFactory;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Class<?> getObjectType()
/*  45:    */   {
/*  46:130 */     return this.connectionFactory != null ? this.connectionFactory.getClass() : null;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isSingleton()
/*  50:    */   {
/*  51:134 */     return true;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.support.LocalConnectionFactoryBean
 * JD-Core Version:    0.7.0.1
 */