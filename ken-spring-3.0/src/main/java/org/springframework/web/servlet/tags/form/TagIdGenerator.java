/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.PageContext;
/*  4:   */ 
/*  5:   */ abstract class TagIdGenerator
/*  6:   */ {
/*  7:39 */   private static final String PAGE_CONTEXT_ATTRIBUTE_PREFIX = TagIdGenerator.class.getName() + ".";
/*  8:   */   
/*  9:   */   public static String nextId(String name, PageContext pageContext)
/* 10:   */   {
/* 11:45 */     String attributeName = PAGE_CONTEXT_ATTRIBUTE_PREFIX + name;
/* 12:46 */     Integer currentCount = (Integer)pageContext.getAttribute(attributeName);
/* 13:47 */     currentCount = Integer.valueOf(currentCount != null ? currentCount.intValue() + 1 : 1);
/* 14:48 */     pageContext.setAttribute(attributeName, currentCount);
/* 15:49 */     return name + currentCount;
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.TagIdGenerator
 * JD-Core Version:    0.7.0.1
 */