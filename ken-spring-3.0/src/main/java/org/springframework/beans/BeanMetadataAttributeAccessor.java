/*  1:   */ package org.springframework.beans;
/*  2:   */ 
/*  3:   */ import org.springframework.core.AttributeAccessorSupport;
/*  4:   */ 
/*  5:   */ public class BeanMetadataAttributeAccessor
/*  6:   */   extends AttributeAccessorSupport
/*  7:   */   implements BeanMetadataElement
/*  8:   */ {
/*  9:   */   private Object source;
/* 10:   */   
/* 11:   */   public void setSource(Object source)
/* 12:   */   {
/* 13:39 */     this.source = source;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Object getSource()
/* 17:   */   {
/* 18:43 */     return this.source;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void addMetadataAttribute(BeanMetadataAttribute attribute)
/* 22:   */   {
/* 23:52 */     super.setAttribute(attribute.getName(), attribute);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public BeanMetadataAttribute getMetadataAttribute(String name)
/* 27:   */   {
/* 28:62 */     return (BeanMetadataAttribute)super.getAttribute(name);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void setAttribute(String name, Object value)
/* 32:   */   {
/* 33:67 */     super.setAttribute(name, new BeanMetadataAttribute(name, value));
/* 34:   */   }
/* 35:   */   
/* 36:   */   public Object getAttribute(String name)
/* 37:   */   {
/* 38:72 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.getAttribute(name);
/* 39:73 */     return attribute != null ? attribute.getValue() : null;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public Object removeAttribute(String name)
/* 43:   */   {
/* 44:78 */     BeanMetadataAttribute attribute = (BeanMetadataAttribute)super.removeAttribute(name);
/* 45:79 */     return attribute != null ? attribute.getValue() : null;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.BeanMetadataAttributeAccessor
 * JD-Core Version:    0.7.0.1
 */