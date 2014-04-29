/*  1:   */ package org.springframework.web.servlet.view.tiles2;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.apache.tiles.TilesApplicationContext;
/*  5:   */ import org.apache.tiles.TilesException;
/*  6:   */ import org.apache.tiles.context.TilesRequestContext;
/*  7:   */ import org.apache.tiles.preparer.PreparerFactory;
/*  8:   */ import org.apache.tiles.preparer.ViewPreparer;
/*  9:   */ import org.springframework.web.context.WebApplicationContext;
/* 10:   */ import org.springframework.web.servlet.DispatcherServlet;
/* 11:   */ 
/* 12:   */ public abstract class AbstractSpringPreparerFactory
/* 13:   */   implements PreparerFactory
/* 14:   */ {
/* 15:   */   public ViewPreparer getPreparer(String name, TilesRequestContext context)
/* 16:   */     throws TilesException
/* 17:   */   {
/* 18:43 */     WebApplicationContext webApplicationContext = (WebApplicationContext)context.getRequestScope().get(
/* 19:44 */       DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 20:45 */     if (webApplicationContext == null)
/* 21:   */     {
/* 22:46 */       webApplicationContext = (WebApplicationContext)context.getApplicationContext().getApplicationScope().get(
/* 23:47 */         WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 24:48 */       if (webApplicationContext == null) {
/* 25:49 */         throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/* 26:   */       }
/* 27:   */     }
/* 28:52 */     return getPreparer(name, webApplicationContext);
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected abstract ViewPreparer getPreparer(String paramString, WebApplicationContext paramWebApplicationContext)
/* 32:   */     throws TilesException;
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.AbstractSpringPreparerFactory
 * JD-Core Version:    0.7.0.1
 */