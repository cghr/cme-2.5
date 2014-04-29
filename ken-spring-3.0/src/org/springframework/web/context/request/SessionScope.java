/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.ObjectFactory;
/*   4:    */ 
/*   5:    */ public class SessionScope
/*   6:    */   extends AbstractRequestAttributesScope
/*   7:    */ {
/*   8:    */   private final int scope;
/*   9:    */   
/*  10:    */   public SessionScope()
/*  11:    */   {
/*  12: 58 */     this.scope = 1;
/*  13:    */   }
/*  14:    */   
/*  15:    */   public SessionScope(boolean globalSession)
/*  16:    */   {
/*  17: 75 */     this.scope = (globalSession ? 2 : 1);
/*  18:    */   }
/*  19:    */   
/*  20:    */   protected int getScope()
/*  21:    */   {
/*  22: 81 */     return this.scope;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getConversationId()
/*  26:    */   {
/*  27: 85 */     return RequestContextHolder.currentRequestAttributes().getSessionId();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Object get(String name, ObjectFactory objectFactory)
/*  31:    */   {
/*  32: 90 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/*  33: 91 */     synchronized (mutex)
/*  34:    */     {
/*  35: 92 */       return super.get(name, objectFactory);
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Object remove(String name)
/*  40:    */   {
/*  41: 98 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/*  42: 99 */     synchronized (mutex)
/*  43:    */     {
/*  44:100 */       return super.remove(name);
/*  45:    */     }
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.SessionScope
 * JD-Core Version:    0.7.0.1
 */