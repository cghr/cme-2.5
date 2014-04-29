/*  1:   */ package org.springframework.web.servlet.view.freemarker;
/*  2:   */ 
/*  3:   */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
/*  4:   */ 
/*  5:   */ public class FreeMarkerViewResolver
/*  6:   */   extends AbstractTemplateViewResolver
/*  7:   */ {
/*  8:   */   public FreeMarkerViewResolver()
/*  9:   */   {
/* 10:44 */     setViewClass(requiredViewClass());
/* 11:   */   }
/* 12:   */   
/* 13:   */   protected Class requiredViewClass()
/* 14:   */   {
/* 15:52 */     return FreeMarkerView.class;
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
 * JD-Core Version:    0.7.0.1
 */