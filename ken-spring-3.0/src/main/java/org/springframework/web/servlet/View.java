/*  1:   */ package org.springframework.web.servlet;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.http.HttpServletResponse;
/*  6:   */ 
/*  7:   */ public abstract interface View
/*  8:   */ {
/*  9:50 */   public static final String RESPONSE_STATUS_ATTRIBUTE = View.class.getName() + ".responseStatus";
/* 10:59 */   public static final String PATH_VARIABLES = View.class.getName() + ".pathVariables";
/* 11:   */   
/* 12:   */   public abstract String getContentType();
/* 13:   */   
/* 14:   */   public abstract void render(Map<String, ?> paramMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/* 15:   */     throws Exception;
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.View
 * JD-Core Version:    0.7.0.1
 */