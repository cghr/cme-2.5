/*  1:   */ package org.springframework.web.servlet;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class ModelAndViewDefiningException
/*  7:   */   extends ServletException
/*  8:   */ {
/*  9:   */   private ModelAndView modelAndView;
/* 10:   */   
/* 11:   */   public ModelAndViewDefiningException(ModelAndView modelAndView)
/* 12:   */   {
/* 13:46 */     Assert.notNull(modelAndView, "ModelAndView must not be null in ModelAndViewDefiningException");
/* 14:47 */     this.modelAndView = modelAndView;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ModelAndView getModelAndView()
/* 18:   */   {
/* 19:54 */     return this.modelAndView;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ModelAndViewDefiningException
 * JD-Core Version:    0.7.0.1
 */