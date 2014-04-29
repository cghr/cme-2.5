/*  1:   */ package org.springframework.web.servlet.view.tiles2;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.concurrent.ConcurrentHashMap;
/*  5:   */ import org.apache.tiles.TilesException;
/*  6:   */ import org.apache.tiles.preparer.NoSuchPreparerException;
/*  7:   */ import org.apache.tiles.preparer.PreparerException;
/*  8:   */ import org.apache.tiles.preparer.ViewPreparer;
/*  9:   */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/* 10:   */ import org.springframework.web.context.WebApplicationContext;
/* 11:   */ 
/* 12:   */ public class SimpleSpringPreparerFactory
/* 13:   */   extends AbstractSpringPreparerFactory
/* 14:   */ {
/* 15:42 */   private final Map<String, ViewPreparer> sharedPreparers = new ConcurrentHashMap();
/* 16:   */   
/* 17:   */   protected ViewPreparer getPreparer(String name, WebApplicationContext context)
/* 18:   */     throws TilesException
/* 19:   */   {
/* 20:48 */     ViewPreparer preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 21:49 */     if (preparer == null) {
/* 22:50 */       synchronized (this.sharedPreparers)
/* 23:   */       {
/* 24:51 */         preparer = (ViewPreparer)this.sharedPreparers.get(name);
/* 25:52 */         if (preparer == null) {
/* 26:   */           try
/* 27:   */           {
/* 28:54 */             Class<?> beanClass = context.getClassLoader().loadClass(name);
/* 29:55 */             if (!ViewPreparer.class.isAssignableFrom(beanClass)) {
/* 30:56 */               throw new PreparerException(
/* 31:57 */                 "Invalid preparer class [" + name + "]: does not implement ViewPreparer interface");
/* 32:   */             }
/* 33:59 */             preparer = (ViewPreparer)context.getAutowireCapableBeanFactory().createBean(beanClass);
/* 34:60 */             this.sharedPreparers.put(name, preparer);
/* 35:   */           }
/* 36:   */           catch (ClassNotFoundException ex)
/* 37:   */           {
/* 38:63 */             throw new NoSuchPreparerException("Preparer class [" + name + "] not found", ex);
/* 39:   */           }
/* 40:   */         }
/* 41:   */       }
/* 42:   */     }
/* 43:68 */     return preparer;
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.SimpleSpringPreparerFactory
 * JD-Core Version:    0.7.0.1
 */