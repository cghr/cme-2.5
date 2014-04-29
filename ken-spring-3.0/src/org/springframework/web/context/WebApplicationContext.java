/*  1:   */ package org.springframework.web.context;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.context.ApplicationContext;
/*  5:   */ 
/*  6:   */ public abstract interface WebApplicationContext
/*  7:   */   extends ApplicationContext
/*  8:   */ {
/*  9:54 */   public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
/* 10:   */   public static final String SCOPE_REQUEST = "request";
/* 11:   */   public static final String SCOPE_SESSION = "session";
/* 12:   */   public static final String SCOPE_GLOBAL_SESSION = "globalSession";
/* 13:   */   public static final String SCOPE_APPLICATION = "application";
/* 14:   */   public static final String SERVLET_CONTEXT_BEAN_NAME = "servletContext";
/* 15:   */   public static final String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";
/* 16:   */   public static final String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";
/* 17:   */   
/* 18:   */   public abstract ServletContext getServletContext();
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.WebApplicationContext
 * JD-Core Version:    0.7.0.1
 */