/*  1:   */ package org.springframework.web.context.request;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.ObjectFactory;
/*  4:   */ import org.springframework.beans.factory.config.Scope;
/*  5:   */ 
/*  6:   */ public abstract class AbstractRequestAttributesScope
/*  7:   */   implements Scope
/*  8:   */ {
/*  9:   */   public Object get(String name, ObjectFactory objectFactory)
/* 10:   */   {
/* 11:40 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 12:41 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 13:42 */     if (scopedObject == null)
/* 14:   */     {
/* 15:43 */       scopedObject = objectFactory.getObject();
/* 16:44 */       attributes.setAttribute(name, scopedObject, getScope());
/* 17:   */     }
/* 18:46 */     return scopedObject;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public Object remove(String name)
/* 22:   */   {
/* 23:50 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 24:51 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 25:52 */     if (scopedObject != null)
/* 26:   */     {
/* 27:53 */       attributes.removeAttribute(name, getScope());
/* 28:54 */       return scopedObject;
/* 29:   */     }
/* 30:57 */     return null;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void registerDestructionCallback(String name, Runnable callback)
/* 34:   */   {
/* 35:62 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 36:63 */     attributes.registerDestructionCallback(name, callback, getScope());
/* 37:   */   }
/* 38:   */   
/* 39:   */   public Object resolveContextualObject(String key)
/* 40:   */   {
/* 41:67 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 42:68 */     return attributes.resolveReference(key);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected abstract int getScope();
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.AbstractRequestAttributesScope
 * JD-Core Version:    0.7.0.1
 */