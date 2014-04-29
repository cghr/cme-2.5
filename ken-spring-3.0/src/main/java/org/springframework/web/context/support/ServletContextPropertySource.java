/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContext;
/*  4:   */ import org.springframework.core.env.EnumerablePropertySource;
/*  5:   */ import org.springframework.util.CollectionUtils;
/*  6:   */ 
/*  7:   */ public class ServletContextPropertySource
/*  8:   */   extends EnumerablePropertySource<ServletContext>
/*  9:   */ {
/* 10:   */   public ServletContextPropertySource(String name, ServletContext servletContext)
/* 11:   */   {
/* 12:35 */     super(name, servletContext);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public String[] getPropertyNames()
/* 16:   */   {
/* 17:40 */     return (String[])CollectionUtils.toArray(
/* 18:41 */       ((ServletContext)this.source).getInitParameterNames(), EMPTY_NAMES_ARRAY);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getProperty(String name)
/* 22:   */   {
/* 23:46 */     return ((ServletContext)this.source).getInitParameter(name);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextPropertySource
 * JD-Core Version:    0.7.0.1
 */