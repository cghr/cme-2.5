/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.util.ObjectUtils;
/*  5:   */ 
/*  6:   */ public class BeanMetadataAttribute
/*  7:   */   implements BeanMetadataElement
/*  8:   */ {
/*  9:   */   private final String name;
/* 10:   */   private final Object value;
/* 11:   */   private Object source;
/* 12:   */   
/* 13:   */   public BeanMetadataAttribute(String name, Object value)
/* 14:   */   {
/* 15:44 */     Assert.notNull(name, "Name must not be null");
/* 16:45 */     this.name = name;
/* 17:46 */     this.value = value;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public String getName()
/* 21:   */   {
/* 22:54 */     return this.name;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Object getValue()
/* 26:   */   {
/* 27:61 */     return this.value;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void setSource(Object source)
/* 31:   */   {
/* 32:69 */     this.source = source;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Object getSource()
/* 36:   */   {
/* 37:73 */     return this.source;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean equals(Object other)
/* 41:   */   {
/* 42:79 */     if (this == other) {
/* 43:80 */       return true;
/* 44:   */     }
/* 45:82 */     if (!(other instanceof BeanMetadataAttribute)) {
/* 46:83 */       return false;
/* 47:   */     }
/* 48:85 */     BeanMetadataAttribute otherMa = (BeanMetadataAttribute)other;
/* 49:   */     
/* 50:   */ 
/* 51:88 */     return (this.name.equals(otherMa.name)) && (ObjectUtils.nullSafeEquals(this.value, otherMa.value)) && (ObjectUtils.nullSafeEquals(this.source, otherMa.source));
/* 52:   */   }
/* 53:   */   
/* 54:   */   public int hashCode()
/* 55:   */   {
/* 56:93 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/* 57:   */   }
/* 58:   */   
/* 59:   */   public String toString()
/* 60:   */   {
/* 61:98 */     return "metadata attribute '" + this.name + "'";
/* 62:   */   }
/* 63:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanMetadataAttribute
 * JD-Core Version:    0.7.0.1
 */