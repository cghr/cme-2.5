/*  1:   */ package org.springframework.web.bind.support;
/*  2:   */ 
/*  3:   */ import org.springframework.web.bind.WebDataBinder;
/*  4:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  5:   */ 
/*  6:   */ public class DefaultDataBinderFactory
/*  7:   */   implements WebDataBinderFactory
/*  8:   */ {
/*  9:   */   private final WebBindingInitializer initializer;
/* 10:   */   
/* 11:   */   public DefaultDataBinderFactory(WebBindingInitializer initializer)
/* 12:   */   {
/* 13:38 */     this.initializer = initializer;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:48 */     WebDataBinder dataBinder = createBinderInstance(target, objectName, webRequest);
/* 20:49 */     if (this.initializer != null) {
/* 21:50 */       this.initializer.initBinder(dataBinder, webRequest);
/* 22:   */     }
/* 23:52 */     initBinder(dataBinder, webRequest);
/* 24:53 */     return dataBinder;
/* 25:   */   }
/* 26:   */   
/* 27:   */   protected WebDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest webRequest)
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:66 */     return new WebRequestDataBinder(target, objectName);
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected void initBinder(WebDataBinder dataBinder, NativeWebRequest webRequest)
/* 34:   */     throws Exception
/* 35:   */   {}
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.support.DefaultDataBinderFactory
 * JD-Core Version:    0.7.0.1
 */