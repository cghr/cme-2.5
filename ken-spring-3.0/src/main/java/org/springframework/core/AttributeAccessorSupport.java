/*  1:   */ package org.springframework.core;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.LinkedHashMap;
/*  5:   */ import java.util.Map;
/*  6:   */ import java.util.Set;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public abstract class AttributeAccessorSupport
/* 10:   */   implements AttributeAccessor, Serializable
/* 11:   */ {
/* 12:38 */   private final Map<String, Object> attributes = new LinkedHashMap(0);
/* 13:   */   
/* 14:   */   public void setAttribute(String name, Object value)
/* 15:   */   {
/* 16:42 */     Assert.notNull(name, "Name must not be null");
/* 17:43 */     if (value != null) {
/* 18:44 */       this.attributes.put(name, value);
/* 19:   */     } else {
/* 20:47 */       removeAttribute(name);
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Object getAttribute(String name)
/* 25:   */   {
/* 26:52 */     Assert.notNull(name, "Name must not be null");
/* 27:53 */     return this.attributes.get(name);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Object removeAttribute(String name)
/* 31:   */   {
/* 32:57 */     Assert.notNull(name, "Name must not be null");
/* 33:58 */     return this.attributes.remove(name);
/* 34:   */   }
/* 35:   */   
/* 36:   */   public boolean hasAttribute(String name)
/* 37:   */   {
/* 38:62 */     Assert.notNull(name, "Name must not be null");
/* 39:63 */     return this.attributes.containsKey(name);
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String[] attributeNames()
/* 43:   */   {
/* 44:67 */     return (String[])this.attributes.keySet().toArray(new String[this.attributes.size()]);
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected void copyAttributesFrom(AttributeAccessor source)
/* 48:   */   {
/* 49:76 */     Assert.notNull(source, "Source must not be null");
/* 50:77 */     String[] attributeNames = source.attributeNames();
/* 51:78 */     for (String attributeName : attributeNames) {
/* 52:79 */       setAttribute(attributeName, source.getAttribute(attributeName));
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   public boolean equals(Object other)
/* 57:   */   {
/* 58:86 */     if (this == other) {
/* 59:87 */       return true;
/* 60:   */     }
/* 61:89 */     if (!(other instanceof AttributeAccessorSupport)) {
/* 62:90 */       return false;
/* 63:   */     }
/* 64:92 */     AttributeAccessorSupport that = (AttributeAccessorSupport)other;
/* 65:93 */     return this.attributes.equals(that.attributes);
/* 66:   */   }
/* 67:   */   
/* 68:   */   public int hashCode()
/* 69:   */   {
/* 70:98 */     return this.attributes.hashCode();
/* 71:   */   }
/* 72:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.AttributeAccessorSupport
 * JD-Core Version:    0.7.0.1
 */