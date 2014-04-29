/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ public class RequestScope
/*  4:   */   extends AbstractRequestAttributesScope
/*  5:   */ {
/*  6:   */   protected int getScope()
/*  7:   */   {
/*  8:48 */     return 0;
/*  9:   */   }
/* 10:   */   
/* 11:   */   public String getConversationId()
/* 12:   */   {
/* 13:56 */     return null;
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.RequestScope
 * JD-Core Version:    0.7.0.1
 */