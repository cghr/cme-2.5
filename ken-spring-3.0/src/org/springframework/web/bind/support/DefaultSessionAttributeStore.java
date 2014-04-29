/*  1:   */ package org.springframework.web.bind.support;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.web.context.request.WebRequest;
/*  5:   */ 
/*  6:   */ public class DefaultSessionAttributeStore
/*  7:   */   implements SessionAttributeStore
/*  8:   */ {
/*  9:36 */   private String attributeNamePrefix = "";
/* 10:   */   
/* 11:   */   public void setAttributeNamePrefix(String attributeNamePrefix)
/* 12:   */   {
/* 13:45 */     this.attributeNamePrefix = (attributeNamePrefix != null ? attributeNamePrefix : "");
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void storeAttribute(WebRequest request, String attributeName, Object attributeValue)
/* 17:   */   {
/* 18:50 */     Assert.notNull(request, "WebRequest must not be null");
/* 19:51 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 20:52 */     Assert.notNull(attributeValue, "Attribute value must not be null");
/* 21:53 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 22:54 */     request.setAttribute(storeAttributeName, attributeValue, 1);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Object retrieveAttribute(WebRequest request, String attributeName)
/* 26:   */   {
/* 27:58 */     Assert.notNull(request, "WebRequest must not be null");
/* 28:59 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 29:60 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 30:61 */     return request.getAttribute(storeAttributeName, 1);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void cleanupAttribute(WebRequest request, String attributeName)
/* 34:   */   {
/* 35:65 */     Assert.notNull(request, "WebRequest must not be null");
/* 36:66 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 37:67 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 38:68 */     request.removeAttribute(storeAttributeName, 1);
/* 39:   */   }
/* 40:   */   
/* 41:   */   protected String getAttributeNameInSession(WebRequest request, String attributeName)
/* 42:   */   {
/* 43:81 */     return this.attributeNamePrefix + attributeName;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.DefaultSessionAttributeStore
 * JD-Core Version:    0.7.0.1
 */