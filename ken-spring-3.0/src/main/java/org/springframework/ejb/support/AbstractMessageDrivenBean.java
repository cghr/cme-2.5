/*  1:   */ package org.springframework.ejb.support;
/*  2:   */ 
/*  3:   */ import javax.ejb.MessageDrivenBean;
/*  4:   */ import javax.ejb.MessageDrivenContext;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ 
/*  8:   */ public abstract class AbstractMessageDrivenBean
/*  9:   */   extends AbstractEnterpriseBean
/* 10:   */   implements MessageDrivenBean
/* 11:   */ {
/* 12:48 */   protected final Log logger = LogFactory.getLog(getClass());
/* 13:   */   private MessageDrivenContext messageDrivenContext;
/* 14:   */   
/* 15:   */   public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext)
/* 16:   */   {
/* 17:58 */     this.messageDrivenContext = messageDrivenContext;
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected final MessageDrivenContext getMessageDrivenContext()
/* 21:   */   {
/* 22:66 */     return this.messageDrivenContext;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void ejbCreate()
/* 26:   */   {
/* 27:78 */     loadBeanFactory();
/* 28:79 */     onEjbCreate();
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected abstract void onEjbCreate();
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.AbstractMessageDrivenBean
 * JD-Core Version:    0.7.0.1
 */