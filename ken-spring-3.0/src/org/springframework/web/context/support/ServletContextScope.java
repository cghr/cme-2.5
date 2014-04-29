/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import org.springframework.beans.factory.DisposableBean;
/*   7:    */ import org.springframework.beans.factory.ObjectFactory;
/*   8:    */ import org.springframework.beans.factory.config.Scope;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ public class ServletContextScope
/*  12:    */   implements Scope, DisposableBean
/*  13:    */ {
/*  14:    */   private final ServletContext servletContext;
/*  15: 52 */   private final Map<String, Runnable> destructionCallbacks = new LinkedHashMap();
/*  16:    */   
/*  17:    */   public ServletContextScope(ServletContext servletContext)
/*  18:    */   {
/*  19: 60 */     Assert.notNull(servletContext, "ServletContext must not be null");
/*  20: 61 */     this.servletContext = servletContext;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Object get(String name, ObjectFactory<?> objectFactory)
/*  24:    */   {
/*  25: 66 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  26: 67 */     if (scopedObject == null)
/*  27:    */     {
/*  28: 68 */       scopedObject = objectFactory.getObject();
/*  29: 69 */       this.servletContext.setAttribute(name, scopedObject);
/*  30:    */     }
/*  31: 71 */     return scopedObject;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Object remove(String name)
/*  35:    */   {
/*  36: 75 */     Object scopedObject = this.servletContext.getAttribute(name);
/*  37: 76 */     if (scopedObject != null)
/*  38:    */     {
/*  39: 77 */       this.servletContext.removeAttribute(name);
/*  40: 78 */       this.destructionCallbacks.remove(name);
/*  41: 79 */       return scopedObject;
/*  42:    */     }
/*  43: 82 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void registerDestructionCallback(String name, Runnable callback)
/*  47:    */   {
/*  48: 87 */     this.destructionCallbacks.put(name, callback);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Object resolveContextualObject(String key)
/*  52:    */   {
/*  53: 91 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getConversationId()
/*  57:    */   {
/*  58: 95 */     return null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void destroy()
/*  62:    */   {
/*  63:105 */     for (Runnable runnable : this.destructionCallbacks.values()) {
/*  64:106 */       runnable.run();
/*  65:    */     }
/*  66:108 */     this.destructionCallbacks.clear();
/*  67:    */   }
/*  68:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextScope
 * JD-Core Version:    0.7.0.1
 */