/*  1:   */ package org.springframework.ui;
/*  2:   */ 
/*  3:   */ import java.util.Collection;
/*  4:   */ import java.util.Map;
/*  5:   */ 
/*  6:   */ public class ExtendedModelMap
/*  7:   */   extends ModelMap
/*  8:   */   implements Model
/*  9:   */ {
/* 10:   */   public ExtendedModelMap addAttribute(String attributeName, Object attributeValue)
/* 11:   */   {
/* 12:34 */     super.addAttribute(attributeName, attributeValue);
/* 13:35 */     return this;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ExtendedModelMap addAttribute(Object attributeValue)
/* 17:   */   {
/* 18:40 */     super.addAttribute(attributeValue);
/* 19:41 */     return this;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ExtendedModelMap addAllAttributes(Collection<?> attributeValues)
/* 23:   */   {
/* 24:46 */     super.addAllAttributes(attributeValues);
/* 25:47 */     return this;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public ExtendedModelMap addAllAttributes(Map<String, ?> attributes)
/* 29:   */   {
/* 30:52 */     super.addAllAttributes(attributes);
/* 31:53 */     return this;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public ExtendedModelMap mergeAttributes(Map<String, ?> attributes)
/* 35:   */   {
/* 36:58 */     super.mergeAttributes(attributes);
/* 37:59 */     return this;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public Map<String, Object> asMap()
/* 41:   */   {
/* 42:63 */     return this;
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.ExtendedModelMap
 * JD-Core Version:    0.7.0.1
 */