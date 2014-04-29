/*   1:    */ package org.springframework.web.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.apache.commons.logging.Log;
/*   6:    */ import org.apache.commons.logging.LogFactory;
/*   7:    */ import org.springframework.beans.BeanUtils;
/*   8:    */ import org.springframework.core.MethodParameter;
/*   9:    */ import org.springframework.ui.ModelMap;
/*  10:    */ import org.springframework.validation.BindException;
/*  11:    */ import org.springframework.validation.BindingResult;
/*  12:    */ import org.springframework.validation.Errors;
/*  13:    */ import org.springframework.web.bind.WebDataBinder;
/*  14:    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*  15:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  16:    */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*  17:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  18:    */ import org.springframework.web.method.annotation.ModelFactory;
/*  19:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  20:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*  21:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  22:    */ 
/*  23:    */ public class ModelAttributeMethodProcessor
/*  24:    */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*  25:    */ {
/*  26: 56 */   protected Log logger = LogFactory.getLog(getClass());
/*  27:    */   private final boolean annotationNotRequired;
/*  28:    */   
/*  29:    */   public ModelAttributeMethodProcessor(boolean annotationNotRequired)
/*  30:    */   {
/*  31: 67 */     this.annotationNotRequired = annotationNotRequired;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean supportsParameter(MethodParameter parameter)
/*  35:    */   {
/*  36: 75 */     if (parameter.hasParameterAnnotation(ModelAttribute.class)) {
/*  37: 76 */       return true;
/*  38:    */     }
/*  39: 78 */     if (this.annotationNotRequired) {
/*  40: 79 */       return !BeanUtils.isSimpleProperty(parameter.getParameterType());
/*  41:    */     }
/*  42: 82 */     return false;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48: 99 */     String name = ModelFactory.getNameForParameter(parameter);
/*  49:100 */     Object target = mavContainer.containsAttribute(name) ? 
/*  50:101 */       mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);
/*  51:    */     
/*  52:103 */     WebDataBinder binder = binderFactory.createBinder(request, target, name);
/*  53:105 */     if (binder.getTarget() != null)
/*  54:    */     {
/*  55:106 */       bindRequestParameters(binder, request);
/*  56:108 */       if (isValidationApplicable(binder.getTarget(), parameter)) {
/*  57:109 */         binder.validate();
/*  58:    */       }
/*  59:112 */       if ((binder.getBindingResult().hasErrors()) && 
/*  60:113 */         (isBindExceptionRequired(binder, parameter))) {
/*  61:114 */         throw new BindException(binder.getBindingResult());
/*  62:    */       }
/*  63:    */     }
/*  64:119 */     mavContainer.addAllAttributes(binder.getBindingResult().getModel());
/*  65:    */     
/*  66:121 */     return binder.getTarget();
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
/*  70:    */     throws Exception
/*  71:    */   {
/*  72:137 */     return BeanUtils.instantiateClass(parameter.getParameterType());
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request)
/*  76:    */   {
/*  77:146 */     ((WebRequestDataBinder)binder).bind(request);
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected boolean isValidationApplicable(Object modelAttribute, MethodParameter parameter)
/*  81:    */   {
/*  82:156 */     Annotation[] annotations = parameter.getParameterAnnotations();
/*  83:157 */     for (Annotation annot : annotations) {
/*  84:158 */       if ("Valid".equals(annot.annotationType().getSimpleName())) {
/*  85:159 */         return true;
/*  86:    */       }
/*  87:    */     }
/*  88:162 */     return false;
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter)
/*  92:    */   {
/*  93:173 */     int i = parameter.getParameterIndex();
/*  94:174 */     Class[] paramTypes = parameter.getMethod().getParameterTypes();
/*  95:175 */     boolean hasBindingResult = (paramTypes.length > i + 1) && (Errors.class.isAssignableFrom(paramTypes[(i + 1)]));
/*  96:    */     
/*  97:177 */     return !hasBindingResult;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public boolean supportsReturnType(MethodParameter returnType)
/* 101:    */   {
/* 102:185 */     if (returnType.getMethodAnnotation(ModelAttribute.class) != null) {
/* 103:186 */       return true;
/* 104:    */     }
/* 105:188 */     if (this.annotationNotRequired) {
/* 106:189 */       return !BeanUtils.isSimpleProperty(returnType.getParameterType());
/* 107:    */     }
/* 108:192 */     return false;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:203 */     if (returnValue != null)
/* 115:    */     {
/* 116:204 */       String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
/* 117:205 */       mavContainer.addAttribute(name, returnValue);
/* 118:    */     }
/* 119:    */   }
/* 120:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor
 * JD-Core Version:    0.7.0.1
 */