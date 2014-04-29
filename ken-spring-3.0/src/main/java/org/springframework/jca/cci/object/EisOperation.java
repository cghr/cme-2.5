/*  1:   */ package org.springframework.jca.cci.object;
/*  2:   */ 
/*  3:   */ import javax.resource.cci.ConnectionFactory;
/*  4:   */ import javax.resource.cci.InteractionSpec;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ import org.springframework.jca.cci.core.CciTemplate;
/*  7:   */ 
/*  8:   */ public abstract class EisOperation
/*  9:   */   implements InitializingBean
/* 10:   */ {
/* 11:39 */   private CciTemplate cciTemplate = new CciTemplate();
/* 12:   */   private InteractionSpec interactionSpec;
/* 13:   */   
/* 14:   */   public void setCciTemplate(CciTemplate cciTemplate)
/* 15:   */   {
/* 16:50 */     if (cciTemplate == null) {
/* 17:51 */       throw new IllegalArgumentException("cciTemplate must not be null");
/* 18:   */     }
/* 19:53 */     this.cciTemplate = cciTemplate;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public CciTemplate getCciTemplate()
/* 23:   */   {
/* 24:60 */     return this.cciTemplate;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setConnectionFactory(ConnectionFactory connectionFactory)
/* 28:   */   {
/* 29:67 */     this.cciTemplate.setConnectionFactory(connectionFactory);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void setInteractionSpec(InteractionSpec interactionSpec)
/* 33:   */   {
/* 34:74 */     this.interactionSpec = interactionSpec;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public InteractionSpec getInteractionSpec()
/* 38:   */   {
/* 39:81 */     return this.interactionSpec;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public void afterPropertiesSet()
/* 43:   */   {
/* 44:86 */     this.cciTemplate.afterPropertiesSet();
/* 45:88 */     if (this.interactionSpec == null) {
/* 46:89 */       throw new IllegalArgumentException("interactionSpec is required");
/* 47:   */     }
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.object.EisOperation
 * JD-Core Version:    0.7.0.1
 */