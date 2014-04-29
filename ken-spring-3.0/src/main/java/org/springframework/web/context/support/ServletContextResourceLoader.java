/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.core.io.DefaultResourceLoader;
/*  5:   */ import org.springframework.core.io.Resource;
/*  6:   */ 
/*  7:   */ public class ServletContextResourceLoader
/*  8:   */   extends DefaultResourceLoader
/*  9:   */ {
/* 10:   */   private final ServletContext servletContext;
/* 11:   */   
/* 12:   */   public ServletContextResourceLoader(ServletContext servletContext)
/* 13:   */   {
/* 14:50 */     this.servletContext = servletContext;
/* 15:   */   }
/* 16:   */   
/* 17:   */   protected Resource getResourceByPath(String path)
/* 18:   */   {
/* 19:59 */     return new ServletContextResource(this.servletContext, path);
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextResourceLoader
 * JD-Core Version:    0.7.0.1
 */