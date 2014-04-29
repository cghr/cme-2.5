/*  1:   */ package org.springframework.web.servlet.view.tiles2;
/*  2:   */ 
/*  3:   */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*  4:   */ 
/*  5:   */ public class TilesViewResolver
/*  6:   */   extends UrlBasedViewResolver
/*  7:   */ {
/*  8:   */   public TilesViewResolver()
/*  9:   */   {
/* 10:43 */     setViewClass(requiredViewClass());
/* 11:   */   }
/* 12:   */   
/* 13:   */   protected Class requiredViewClass()
/* 14:   */   {
/* 15:51 */     return TilesView.class;
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.TilesViewResolver
 * JD-Core Version:    0.7.0.1
 */