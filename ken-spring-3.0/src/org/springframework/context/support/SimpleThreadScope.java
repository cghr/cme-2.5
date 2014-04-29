/*  1:   */ package org.springframework.context.support;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.springframework.beans.factory.ObjectFactory;
/*  8:   */ import org.springframework.beans.factory.config.Scope;
/*  9:   */ import org.springframework.core.NamedThreadLocal;
/* 10:   */ 
/* 11:   */ public class SimpleThreadScope
/* 12:   */   implements Scope
/* 13:   */ {
/* 14:48 */   private static final Log logger = LogFactory.getLog(SimpleThreadScope.class);
/* 15:51 */   private final ThreadLocal<Map<String, Object>> threadScope = new NamedThreadLocal("SimpleThreadScope")
/* 16:   */   {
/* 17:   */     protected Map<String, Object> initialValue()
/* 18:   */     {
/* 19:54 */       return new HashMap();
/* 20:   */     }
/* 21:   */   };
/* 22:   */   
/* 23:   */   public Object get(String name, ObjectFactory objectFactory)
/* 24:   */   {
/* 25:59 */     Map<String, Object> scope = (Map)this.threadScope.get();
/* 26:60 */     Object object = scope.get(name);
/* 27:61 */     if (object == null)
/* 28:   */     {
/* 29:62 */       object = objectFactory.getObject();
/* 30:63 */       scope.put(name, object);
/* 31:   */     }
/* 32:65 */     return object;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Object remove(String name)
/* 36:   */   {
/* 37:69 */     Map<String, Object> scope = (Map)this.threadScope.get();
/* 38:70 */     return scope.remove(name);
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void registerDestructionCallback(String name, Runnable callback)
/* 42:   */   {
/* 43:74 */     logger.warn("SimpleThreadScope does not support descruction callbacks. Consider using a RequestScope in a Web environment.");
/* 44:   */   }
/* 45:   */   
/* 46:   */   public Object resolveContextualObject(String key)
/* 47:   */   {
/* 48:79 */     return null;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String getConversationId()
/* 52:   */   {
/* 53:83 */     return Thread.currentThread().getName();
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.SimpleThreadScope
 * JD-Core Version:    0.7.0.1
 */