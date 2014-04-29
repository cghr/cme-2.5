/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import org.springframework.core.MethodParameter;
/*   6:    */ import org.springframework.web.bind.ServletRequestBindingException;
/*   7:    */ import org.springframework.web.bind.annotation.PathVariable;
/*   8:    */ import org.springframework.web.context.request.NativeWebRequest;
/*   9:    */ import org.springframework.web.method.annotation.support.AbstractNamedValueMethodArgumentResolver;
/*  10:    */ import org.springframework.web.method.annotation.support.AbstractNamedValueMethodArgumentResolver.NamedValueInfo;
/*  11:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  12:    */ import org.springframework.web.servlet.HandlerMapping;
/*  13:    */ import org.springframework.web.servlet.View;
/*  14:    */ 
/*  15:    */ public class PathVariableMethodArgumentResolver
/*  16:    */   extends AbstractNamedValueMethodArgumentResolver
/*  17:    */ {
/*  18:    */   public PathVariableMethodArgumentResolver()
/*  19:    */   {
/*  20: 51 */     super(null);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public boolean supportsParameter(MethodParameter parameter)
/*  24:    */   {
/*  25: 55 */     return parameter.hasParameterAnnotation(PathVariable.class);
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*  29:    */   {
/*  30: 60 */     PathVariable annotation = (PathVariable)parameter.getParameterAnnotation(PathVariable.class);
/*  31: 61 */     return new PathVariableNamedValueInfo(annotation, null);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request)
/*  35:    */     throws Exception
/*  36:    */   {
/*  37: 67 */     Map<String, String> uriTemplateVars = 
/*  38: 68 */       (Map)request.getAttribute(
/*  39: 69 */       HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
/*  40: 70 */     return uriTemplateVars != null ? (String)uriTemplateVars.get(name) : null;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected void handleMissingValue(String name, MethodParameter param)
/*  44:    */     throws ServletRequestBindingException
/*  45:    */   {
/*  46: 75 */     String paramType = param.getParameterType().getName();
/*  47: 76 */     throw new ServletRequestBindingException(
/*  48: 77 */       "Missing URI template variable '" + name + "' for method parameter type [" + paramType + "]");
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request)
/*  52:    */   {
/*  53: 87 */     String key = View.PATH_VARIABLES;
/*  54: 88 */     int scope = 0;
/*  55: 89 */     Map<String, Object> pathVars = (Map)request.getAttribute(key, scope);
/*  56: 90 */     if (pathVars == null)
/*  57:    */     {
/*  58: 91 */       pathVars = new HashMap();
/*  59: 92 */       request.setAttribute(key, pathVars, scope);
/*  60:    */     }
/*  61: 94 */     pathVars.put(name, arg);
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static class PathVariableNamedValueInfo
/*  65:    */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*  66:    */   {
/*  67:    */     private PathVariableNamedValueInfo(PathVariable annotation)
/*  68:    */     {
/*  69:100 */       super(true, "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*  70:    */     }
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.PathVariableMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */