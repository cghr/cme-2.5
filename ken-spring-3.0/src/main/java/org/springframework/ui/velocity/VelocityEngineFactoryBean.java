/*  1:   */ package org.springframework.ui.velocity;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.apache.velocity.app.VelocityEngine;
/*  5:   */ import org.apache.velocity.exception.VelocityException;
/*  6:   */ import org.springframework.beans.factory.FactoryBean;
/*  7:   */ import org.springframework.beans.factory.InitializingBean;
/*  8:   */ import org.springframework.context.ResourceLoaderAware;
/*  9:   */ 
/* 10:   */ public class VelocityEngineFactoryBean
/* 11:   */   extends VelocityEngineFactory
/* 12:   */   implements FactoryBean<VelocityEngine>, InitializingBean, ResourceLoaderAware
/* 13:   */ {
/* 14:   */   private VelocityEngine velocityEngine;
/* 15:   */   
/* 16:   */   public void afterPropertiesSet()
/* 17:   */     throws IOException, VelocityException
/* 18:   */   {
/* 19:57 */     this.velocityEngine = createVelocityEngine();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public VelocityEngine getObject()
/* 23:   */   {
/* 24:62 */     return this.velocityEngine;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Class<? extends VelocityEngine> getObjectType()
/* 28:   */   {
/* 29:66 */     return VelocityEngine.class;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public boolean isSingleton()
/* 33:   */   {
/* 34:70 */     return true;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.velocity.VelocityEngineFactoryBean
 * JD-Core Version:    0.7.0.1
 */