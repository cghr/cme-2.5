/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import javax.management.MBeanServer;
/*   4:    */ import javax.management.MBeanServerFactory;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.beans.factory.DisposableBean;
/*   8:    */ import org.springframework.beans.factory.FactoryBean;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.jmx.MBeanServerNotFoundException;
/*  11:    */ 
/*  12:    */ public class MBeanServerFactoryBean
/*  13:    */   implements FactoryBean<MBeanServer>, InitializingBean, DisposableBean
/*  14:    */ {
/*  15: 56 */   protected final Log logger = LogFactory.getLog(getClass());
/*  16: 58 */   private boolean locateExistingServerIfPossible = false;
/*  17:    */   private String agentId;
/*  18:    */   private String defaultDomain;
/*  19: 64 */   private boolean registerWithFactory = true;
/*  20:    */   private MBeanServer server;
/*  21: 68 */   private boolean newlyRegistered = false;
/*  22:    */   
/*  23:    */   public void setLocateExistingServerIfPossible(boolean locateExistingServerIfPossible)
/*  24:    */   {
/*  25: 77 */     this.locateExistingServerIfPossible = locateExistingServerIfPossible;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setAgentId(String agentId)
/*  29:    */   {
/*  30: 91 */     this.agentId = agentId;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setDefaultDomain(String defaultDomain)
/*  34:    */   {
/*  35:103 */     this.defaultDomain = defaultDomain;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setRegisterWithFactory(boolean registerWithFactory)
/*  39:    */   {
/*  40:114 */     this.registerWithFactory = registerWithFactory;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void afterPropertiesSet()
/*  44:    */     throws MBeanServerNotFoundException
/*  45:    */   {
/*  46:123 */     if ((this.locateExistingServerIfPossible) || (this.agentId != null)) {
/*  47:    */       try
/*  48:    */       {
/*  49:125 */         this.server = locateMBeanServer(this.agentId);
/*  50:    */       }
/*  51:    */       catch (MBeanServerNotFoundException ex)
/*  52:    */       {
/*  53:130 */         if (this.agentId != null) {
/*  54:131 */           throw ex;
/*  55:    */         }
/*  56:133 */         this.logger.info("No existing MBeanServer found - creating new one");
/*  57:    */       }
/*  58:    */     }
/*  59:138 */     if (this.server == null)
/*  60:    */     {
/*  61:139 */       this.server = createMBeanServer(this.defaultDomain, this.registerWithFactory);
/*  62:140 */       this.newlyRegistered = this.registerWithFactory;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected MBeanServer locateMBeanServer(String agentId)
/*  67:    */     throws MBeanServerNotFoundException
/*  68:    */   {
/*  69:160 */     return JmxUtils.locateMBeanServer(agentId);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected MBeanServer createMBeanServer(String defaultDomain, boolean registerWithFactory)
/*  73:    */   {
/*  74:173 */     if (registerWithFactory) {
/*  75:174 */       return MBeanServerFactory.createMBeanServer(defaultDomain);
/*  76:    */     }
/*  77:177 */     return MBeanServerFactory.newMBeanServer(defaultDomain);
/*  78:    */   }
/*  79:    */   
/*  80:    */   public MBeanServer getObject()
/*  81:    */   {
/*  82:183 */     return this.server;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Class<? extends MBeanServer> getObjectType()
/*  86:    */   {
/*  87:187 */     return this.server != null ? this.server.getClass() : MBeanServer.class;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isSingleton()
/*  91:    */   {
/*  92:191 */     return true;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void destroy()
/*  96:    */   {
/*  97:199 */     if (this.newlyRegistered) {
/*  98:200 */       MBeanServerFactory.releaseMBeanServer(this.server);
/*  99:    */     }
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.MBeanServerFactoryBean
 * JD-Core Version:    0.7.0.1
 */