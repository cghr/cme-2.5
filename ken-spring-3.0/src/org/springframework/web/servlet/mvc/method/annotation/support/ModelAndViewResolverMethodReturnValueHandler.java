/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.core.MethodParameter;
/*   6:    */ import org.springframework.ui.ExtendedModelMap;
/*   7:    */ import org.springframework.web.context.request.NativeWebRequest;
/*   8:    */ import org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor;
/*   9:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  10:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  11:    */ import org.springframework.web.servlet.ModelAndView;
/*  12:    */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
/*  13:    */ 
/*  14:    */ public class ModelAndViewResolverMethodReturnValueHandler
/*  15:    */   implements HandlerMethodReturnValueHandler
/*  16:    */ {
/*  17:    */   private final List<ModelAndViewResolver> mavResolvers;
/*  18: 58 */   private final ModelAttributeMethodProcessor modelAttributeProcessor = new ModelAttributeMethodProcessor(true);
/*  19:    */   
/*  20:    */   public ModelAndViewResolverMethodReturnValueHandler(List<ModelAndViewResolver> mavResolvers)
/*  21:    */   {
/*  22: 64 */     this.mavResolvers = mavResolvers;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public boolean supportsReturnType(MethodParameter returnType)
/*  26:    */   {
/*  27: 71 */     return true;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest request)
/*  31:    */     throws Exception
/*  32:    */   {
/*  33: 79 */     if (this.mavResolvers != null) {
/*  34: 80 */       for (ModelAndViewResolver mavResolver : this.mavResolvers)
/*  35:    */       {
/*  36: 81 */         Class<?> handlerType = returnType.getDeclaringClass();
/*  37: 82 */         Method method = returnType.getMethod();
/*  38: 83 */         ExtendedModelMap model = (ExtendedModelMap)mavContainer.getModel();
/*  39: 84 */         ModelAndView mav = mavResolver.resolveModelAndView(method, handlerType, returnValue, model, request);
/*  40: 85 */         if (mav != ModelAndViewResolver.UNRESOLVED)
/*  41:    */         {
/*  42: 86 */           mavContainer.addAllAttributes(mav.getModel());
/*  43: 87 */           mavContainer.setViewName(mav.getViewName());
/*  44: 88 */           if (!mav.isReference()) {
/*  45: 89 */             mavContainer.setView(mav.getView());
/*  46:    */           }
/*  47: 91 */           return;
/*  48:    */         }
/*  49:    */       }
/*  50:    */     }
/*  51: 98 */     if (this.modelAttributeProcessor.supportsReturnType(returnType)) {
/*  52: 99 */       this.modelAttributeProcessor.handleReturnValue(returnValue, returnType, mavContainer, request);
/*  53:    */     } else {
/*  54:102 */       throw new UnsupportedOperationException("Unexpected return type: " + 
/*  55:103 */         returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
/*  56:    */     }
/*  57:    */   }
/*  58:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ModelAndViewResolverMethodReturnValueHandler
 * JD-Core Version:    0.7.0.1
 */