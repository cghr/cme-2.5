/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.annotation.Annotation;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.core.Conventions;
/*   7:    */ import org.springframework.core.MethodParameter;
/*   8:    */ import org.springframework.http.converter.HttpMessageConverter;
/*   9:    */ import org.springframework.validation.BindingResult;
/*  10:    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*  11:    */ import org.springframework.web.bind.WebDataBinder;
/*  12:    */ import org.springframework.web.bind.annotation.RequestBody;
/*  13:    */ import org.springframework.web.bind.annotation.ResponseBody;
/*  14:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  15:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  16:    */ import org.springframework.web.method.annotation.support.MethodArgumentNotValidException;
/*  17:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  18:    */ 
/*  19:    */ public class RequestResponseBodyMethodProcessor
/*  20:    */   extends AbstractMessageConverterMethodProcessor
/*  21:    */ {
/*  22:    */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters)
/*  23:    */   {
/*  24: 56 */     super(messageConverters);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean supportsParameter(MethodParameter parameter)
/*  28:    */   {
/*  29: 60 */     return parameter.hasParameterAnnotation(RequestBody.class);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean supportsReturnType(MethodParameter returnType)
/*  33:    */   {
/*  34: 64 */     return returnType.getMethodAnnotation(ResponseBody.class) != null;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  38:    */     throws Exception
/*  39:    */   {
/*  40: 71 */     Object arg = readWithMessageConverters(webRequest, parameter, parameter.getParameterType());
/*  41: 72 */     if (isValidationApplicable(arg, parameter))
/*  42:    */     {
/*  43: 73 */       String name = Conventions.getVariableNameForParameter(parameter);
/*  44: 74 */       WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
/*  45: 75 */       binder.validate();
/*  46: 76 */       BindingResult bindingResult = binder.getBindingResult();
/*  47: 77 */       if (bindingResult.hasErrors()) {
/*  48: 78 */         throw new MethodArgumentNotValidException(parameter, bindingResult);
/*  49:    */       }
/*  50:    */     }
/*  51: 81 */     return arg;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected boolean isValidationApplicable(Object argument, MethodParameter parameter)
/*  55:    */   {
/*  56: 91 */     Annotation[] annotations = parameter.getParameterAnnotations();
/*  57: 92 */     for (Annotation annot : annotations) {
/*  58: 93 */       if ("Valid".equals(annot.annotationType().getSimpleName())) {
/*  59: 94 */         return true;
/*  60:    */       }
/*  61:    */     }
/*  62: 97 */     return false;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*  66:    */     throws IOException, HttpMediaTypeNotAcceptableException
/*  67:    */   {
/*  68:104 */     mavContainer.setRequestHandled(true);
/*  69:105 */     if (returnValue != null) {
/*  70:106 */       writeWithMessageConverters(returnValue, returnType, webRequest);
/*  71:    */     }
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.RequestResponseBodyMethodProcessor
 * JD-Core Version:    0.7.0.1
 */