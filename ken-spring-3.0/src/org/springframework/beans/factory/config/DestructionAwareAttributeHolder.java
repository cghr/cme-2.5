/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.concurrent.ConcurrentHashMap;
/*   6:    */ 
/*   7:    */ public class DestructionAwareAttributeHolder
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10: 33 */   private final Map<String, Object> attributes = new ConcurrentHashMap();
/*  11:    */   private Map<String, Runnable> registeredDestructionCallbacks;
/*  12:    */   
/*  13:    */   public Map<String, Object> getAttributeMap()
/*  14:    */   {
/*  15: 51 */     return this.attributes;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public Object getAttribute(String name)
/*  19:    */   {
/*  20: 64 */     return this.attributes.get(name);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Object setAttribute(String name, Object value)
/*  24:    */   {
/*  25: 80 */     return this.attributes.put(name, value);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Object removeAttribute(String name)
/*  29:    */   {
/*  30:106 */     Object value = this.attributes.remove(name);
/*  31:    */     
/*  32:    */ 
/*  33:109 */     Runnable callback = getDestructionCallback(name, true);
/*  34:110 */     if (callback != null) {
/*  35:111 */       callback.run();
/*  36:    */     }
/*  37:114 */     return value;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void clear()
/*  41:    */   {
/*  42:122 */     synchronized (this)
/*  43:    */     {
/*  44:125 */       if (this.registeredDestructionCallbacks != null)
/*  45:    */       {
/*  46:126 */         for (Runnable runnable : this.registeredDestructionCallbacks.values()) {
/*  47:127 */           runnable.run();
/*  48:    */         }
/*  49:130 */         this.registeredDestructionCallbacks.clear();
/*  50:    */       }
/*  51:    */     }
/*  52:135 */     this.attributes.clear();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void registerDestructionCallback(String name, Runnable callback)
/*  56:    */   {
/*  57:172 */     if (this.registeredDestructionCallbacks == null) {
/*  58:173 */       this.registeredDestructionCallbacks = new ConcurrentHashMap();
/*  59:    */     }
/*  60:176 */     this.registeredDestructionCallbacks.put(name, callback);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public Runnable getDestructionCallback(String name, boolean remove)
/*  64:    */   {
/*  65:192 */     if (this.registeredDestructionCallbacks == null) {
/*  66:193 */       return null;
/*  67:    */     }
/*  68:196 */     if (remove) {
/*  69:197 */       return (Runnable)this.registeredDestructionCallbacks.remove(name);
/*  70:    */     }
/*  71:200 */     return (Runnable)this.registeredDestructionCallbacks.get(name);
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.DestructionAwareAttributeHolder
 * JD-Core Version:    0.7.0.1
 */