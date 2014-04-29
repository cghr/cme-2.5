/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  5:   */ import org.springframework.web.bind.support.WebBindingInitializer;
/*  6:   */ import org.springframework.web.context.request.NativeWebRequest;
/*  7:   */ import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
/*  8:   */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*  9:   */ 
/* 10:   */ public class ServletRequestDataBinderFactory
/* 11:   */   extends InitBinderDataBinderFactory
/* 12:   */ {
/* 13:   */   public ServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer)
/* 14:   */   {
/* 15:41 */     super(binderMethods, initializer);
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected ServletRequestDataBinder createBinderInstance(Object target, String objectName, NativeWebRequest request)
/* 19:   */   {
/* 20:49 */     return new ExtendedServletRequestDataBinder(target, objectName);
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory
 * JD-Core Version:    0.7.0.1
 */