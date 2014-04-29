/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import javax.servlet.jsp.tagext.Tag;
/*   4:    */ import org.springframework.util.Assert;
/*   5:    */ 
/*   6:    */ public abstract class TagUtils
/*   7:    */ {
/*   8:    */   public static final String SCOPE_PAGE = "page";
/*   9:    */   public static final String SCOPE_REQUEST = "request";
/*  10:    */   public static final String SCOPE_SESSION = "session";
/*  11:    */   public static final String SCOPE_APPLICATION = "application";
/*  12:    */   
/*  13:    */   public static int getScope(String scope)
/*  14:    */   {
/*  15: 69 */     Assert.notNull(scope, "Scope to search for cannot be null");
/*  16: 70 */     if (scope.equals("request")) {
/*  17: 71 */       return 2;
/*  18:    */     }
/*  19: 73 */     if (scope.equals("session")) {
/*  20: 74 */       return 3;
/*  21:    */     }
/*  22: 76 */     if (scope.equals("application")) {
/*  23: 77 */       return 4;
/*  24:    */     }
/*  25: 80 */     return 1;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public static boolean hasAncestorOfType(Tag tag, Class ancestorTagClass)
/*  29:    */   {
/*  30: 96 */     Assert.notNull(tag, "Tag cannot be null");
/*  31: 97 */     Assert.notNull(ancestorTagClass, "Ancestor tag class cannot be null");
/*  32: 98 */     if (!Tag.class.isAssignableFrom(ancestorTagClass)) {
/*  33: 99 */       throw new IllegalArgumentException(
/*  34:100 */         "Class '" + ancestorTagClass.getName() + "' is not a valid Tag type");
/*  35:    */     }
/*  36:102 */     Tag ancestor = tag.getParent();
/*  37:103 */     while (ancestor != null)
/*  38:    */     {
/*  39:104 */       if (ancestorTagClass.isAssignableFrom(ancestor.getClass())) {
/*  40:105 */         return true;
/*  41:    */       }
/*  42:107 */       ancestor = ancestor.getParent();
/*  43:    */     }
/*  44:109 */     return false;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static void assertHasAncestorOfType(Tag tag, Class ancestorTagClass, String tagName, String ancestorTagName)
/*  48:    */   {
/*  49:129 */     Assert.hasText(tagName, "'tagName' must not be empty");
/*  50:130 */     Assert.hasText(ancestorTagName, "'ancestorTagName' must not be empty");
/*  51:131 */     if (!hasAncestorOfType(tag, ancestorTagClass)) {
/*  52:132 */       throw new IllegalStateException("The '" + tagName + "' tag can only be used inside a valid '" + ancestorTagName + "' tag.");
/*  53:    */     }
/*  54:    */   }
/*  55:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.TagUtils
 * JD-Core Version:    0.7.0.1
 */