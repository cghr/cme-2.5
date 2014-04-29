/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletRequest;
/*   6:    */ import org.springframework.core.MethodParameter;
/*   7:    */ import org.springframework.core.convert.ConversionService;
/*   8:    */ import org.springframework.core.convert.TypeDescriptor;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.validation.DataBinder;
/*  11:    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  12:    */ import org.springframework.web.bind.WebDataBinder;
/*  13:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  14:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  15:    */ import org.springframework.web.method.annotation.support.ModelAttributeMethodProcessor;
/*  16:    */ import org.springframework.web.servlet.HandlerMapping;
/*  17:    */ 
/*  18:    */ public class ServletModelAttributeMethodProcessor
/*  19:    */   extends ModelAttributeMethodProcessor
/*  20:    */ {
/*  21:    */   public ServletModelAttributeMethodProcessor(boolean annotationNotRequired)
/*  22:    */   {
/*  23: 59 */     super(annotationNotRequired);
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected final Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29: 75 */     String value = getRequestValueForAttribute(attributeName, request);
/*  30: 76 */     if (value != null)
/*  31:    */     {
/*  32: 77 */       Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
/*  33: 78 */       if (attribute != null) {
/*  34: 79 */         return attribute;
/*  35:    */       }
/*  36:    */     }
/*  37: 83 */     return super.createAttribute(attributeName, parameter, binderFactory, request);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request)
/*  41:    */   {
/*  42: 96 */     Map<String, String> variables = getUriTemplateVariables(request);
/*  43: 97 */     if (StringUtils.hasText((String)variables.get(attributeName))) {
/*  44: 98 */       return (String)variables.get(attributeName);
/*  45:    */     }
/*  46:100 */     if (StringUtils.hasText(request.getParameter(attributeName))) {
/*  47:101 */       return request.getParameter(attributeName);
/*  48:    */     }
/*  49:104 */     return null;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request)
/*  53:    */   {
/*  54:110 */     Map<String, String> variables = 
/*  55:111 */       (Map)request.getAttribute(
/*  56:112 */       HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*  57:113 */     return variables != null ? variables : Collections.emptyMap();
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected Object createAttributeFromRequestValue(String sourceValue, String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
/*  61:    */     throws Exception
/*  62:    */   {
/*  63:134 */     DataBinder binder = binderFactory.createBinder(request, null, attributeName);
/*  64:135 */     ConversionService conversionService = binder.getConversionService();
/*  65:136 */     if (conversionService != null)
/*  66:    */     {
/*  67:137 */       TypeDescriptor source = TypeDescriptor.valueOf(String.class);
/*  68:138 */       TypeDescriptor target = new TypeDescriptor(parameter);
/*  69:139 */       if (conversionService.canConvert(source, target)) {
/*  70:140 */         return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
/*  71:    */       }
/*  72:    */     }
/*  73:143 */     return null;
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request)
/*  77:    */   {
/*  78:153 */     ServletRequest servletRequest = (ServletRequest)request.getNativeRequest(ServletRequest.class);
/*  79:154 */     ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
/*  80:155 */     servletBinder.bind(servletRequest);
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.ServletModelAttributeMethodProcessor
 * JD-Core Version:    0.7.0.1
 */