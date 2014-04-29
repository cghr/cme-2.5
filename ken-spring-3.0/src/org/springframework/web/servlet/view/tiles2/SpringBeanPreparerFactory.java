/*  1:   */ package org.springframework.web.servlet.view.tiles2;
/*  2:   */ 
/*  3:   */ import org.apache.tiles.TilesException;
/*  4:   */ import org.apache.tiles.preparer.ViewPreparer;
/*  5:   */ import org.springframework.web.context.WebApplicationContext;
/*  6:   */ 
/*  7:   */ public class SpringBeanPreparerFactory
/*  8:   */   extends AbstractSpringPreparerFactory
/*  9:   */ {
/* 10:   */   protected ViewPreparer getPreparer(String name, WebApplicationContext context)
/* 11:   */     throws TilesException
/* 12:   */   {
/* 13:39 */     return (ViewPreparer)context.getBean(name, ViewPreparer.class);
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.SpringBeanPreparerFactory
 * JD-Core Version:    0.7.0.1
 */