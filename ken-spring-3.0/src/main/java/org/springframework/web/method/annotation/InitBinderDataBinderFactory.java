/*  1:   */ package org.springframework.web.method.annotation;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Arrays;
/*  5:   */ import java.util.Collection;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.web.bind.WebDataBinder;
/*  8:   */ import org.springframework.web.bind.annotation.InitBinder;
/*  9:   */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/* 10:   */ import org.springframework.web.bind.support.WebBindingInitializer;
/* 11:   */ import org.springframework.web.context.request.NativeWebRequest;
/* 12:   */ import org.springframework.web.method.HandlerMethod;
/* 13:   */ import org.springframework.web.method.support.InvocableHandlerMethod;
/* 14:   */ 
/* 15:   */ public class InitBinderDataBinderFactory
/* 16:   */   extends DefaultDataBinderFactory
/* 17:   */ {
/* 18:   */   private final List<InvocableHandlerMethod> binderMethods;
/* 19:   */   
/* 20:   */   public InitBinderDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer)
/* 21:   */   {
/* 22:48 */     super(initializer);
/* 23:49 */     this.binderMethods = (binderMethods != null ? binderMethods : new ArrayList());
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void initBinder(WebDataBinder binder, NativeWebRequest request)
/* 27:   */     throws Exception
/* 28:   */   {
/* 29:60 */     for (InvocableHandlerMethod binderMethod : this.binderMethods) {
/* 30:61 */       if (isBinderMethodApplicable(binderMethod, binder))
/* 31:   */       {
/* 32:62 */         Object returnValue = binderMethod.invokeForRequest(request, null, new Object[] { binder });
/* 33:63 */         if (returnValue != null) {
/* 34:64 */           throw new IllegalStateException("@InitBinder methods should return void: " + binderMethod);
/* 35:   */         }
/* 36:   */       }
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected boolean isBinderMethodApplicable(HandlerMethod initBinderMethod, WebDataBinder binder)
/* 41:   */   {
/* 42:77 */     InitBinder annot = (InitBinder)initBinderMethod.getMethodAnnotation(InitBinder.class);
/* 43:78 */     Collection<String> names = (Collection)Arrays.asList(annot.value());
/* 44:79 */     return (names.size() == 0) || (names.contains(binder.getObjectName()));
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.InitBinderDataBinderFactory
 * JD-Core Version:    0.7.0.1
 */