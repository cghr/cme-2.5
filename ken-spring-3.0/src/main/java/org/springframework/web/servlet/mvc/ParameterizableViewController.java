/*  1:   */ package org.springframework.web.servlet.mvc;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ import javax.servlet.http.HttpServletResponse;
/*  5:   */ import org.springframework.web.servlet.ModelAndView;
/*  6:   */ 
/*  7:   */ public class ParameterizableViewController
/*  8:   */   extends AbstractController
/*  9:   */ {
/* 10:   */   private String viewName;
/* 11:   */   
/* 12:   */   public void setViewName(String viewName)
/* 13:   */   {
/* 14:78 */     this.viewName = viewName;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getViewName()
/* 18:   */   {
/* 19:85 */     return this.viewName;
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:95 */     return new ModelAndView(getViewName());
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.ParameterizableViewController
 * JD-Core Version:    0.7.0.1
 */