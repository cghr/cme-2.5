/*   1:    */ package org.springframework.web;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Modifier;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.servlet.ServletContainerInitializer;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import javax.servlet.ServletException;
/*  11:    */ import javax.servlet.annotation.HandlesTypes;
/*  12:    */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*  13:    */ 
/*  14:    */ @HandlesTypes({WebApplicationInitializer.class})
/*  15:    */ public class SpringServletContainerInitializer
/*  16:    */   implements ServletContainerInitializer
/*  17:    */ {
/*  18:    */   public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
/*  19:    */     throws ServletException
/*  20:    */   {
/*  21:135 */     List<WebApplicationInitializer> initializers = new LinkedList();
/*  22:137 */     if (webAppInitializerClasses != null) {
/*  23:138 */       for (Class<?> waiClass : webAppInitializerClasses) {
/*  24:141 */         if ((!waiClass.isInterface()) && (!Modifier.isAbstract(waiClass.getModifiers())) && 
/*  25:142 */           (WebApplicationInitializer.class.isAssignableFrom(waiClass))) {
/*  26:    */           try
/*  27:    */           {
/*  28:144 */             initializers.add((WebApplicationInitializer)waiClass.newInstance());
/*  29:    */           }
/*  30:    */           catch (Throwable ex)
/*  31:    */           {
/*  32:147 */             throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
/*  33:    */           }
/*  34:    */         }
/*  35:    */       }
/*  36:    */     }
/*  37:153 */     if (initializers.isEmpty())
/*  38:    */     {
/*  39:154 */       servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
/*  40:155 */       return;
/*  41:    */     }
/*  42:158 */     Collections.sort(initializers, new AnnotationAwareOrderComparator());
/*  43:159 */     servletContext.log("Spring WebApplicationInitializers detected on classpath: " + initializers);
/*  44:161 */     for (WebApplicationInitializer initializer : initializers) {
/*  45:162 */       initializer.onStartup(servletContext);
/*  46:    */     }
/*  47:    */   }
/*  48:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.SpringServletContainerInitializer
 * JD-Core Version:    0.7.0.1
 */