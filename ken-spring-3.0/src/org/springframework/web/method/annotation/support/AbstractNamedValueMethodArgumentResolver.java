/*   1:    */ package org.springframework.web.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import java.util.concurrent.ConcurrentHashMap;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*   7:    */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*   8:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*   9:    */ import org.springframework.core.MethodParameter;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.web.bind.WebDataBinder;
/*  12:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  13:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  14:    */ import org.springframework.web.context.request.RequestScope;
/*  15:    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*  16:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  17:    */ 
/*  18:    */ public abstract class AbstractNamedValueMethodArgumentResolver
/*  19:    */   implements HandlerMethodArgumentResolver
/*  20:    */ {
/*  21:    */   private final ConfigurableBeanFactory configurableBeanFactory;
/*  22:    */   private final BeanExpressionContext expressionContext;
/*  23: 63 */   private Map<MethodParameter, NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap();
/*  24:    */   
/*  25:    */   public AbstractNamedValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory)
/*  26:    */   {
/*  27: 70 */     this.configurableBeanFactory = beanFactory;
/*  28: 71 */     this.expressionContext = (beanFactory != null ? new BeanExpressionContext(beanFactory, new RequestScope()) : null);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 78 */     Class<?> paramType = parameter.getParameterType();
/*  35:    */     
/*  36: 80 */     NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
/*  37:    */     
/*  38: 82 */     Object arg = resolveName(namedValueInfo.name, parameter, webRequest);
/*  39: 84 */     if (arg == null)
/*  40:    */     {
/*  41: 85 */       if (namedValueInfo.defaultValue != null) {
/*  42: 86 */         arg = resolveDefaultValue(namedValueInfo.defaultValue);
/*  43: 88 */       } else if (namedValueInfo.required) {
/*  44: 89 */         handleMissingValue(namedValueInfo.name, parameter);
/*  45:    */       }
/*  46: 91 */       arg = handleNullValue(namedValueInfo.name, arg, paramType);
/*  47:    */     }
/*  48: 94 */     if (binderFactory != null)
/*  49:    */     {
/*  50: 95 */       WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
/*  51: 96 */       arg = binder.convertIfNecessary(arg, paramType, parameter);
/*  52:    */     }
/*  53: 99 */     handleResolvedValue(arg, namedValueInfo.name, parameter, mavContainer, webRequest);
/*  54:    */     
/*  55:101 */     return arg;
/*  56:    */   }
/*  57:    */   
/*  58:    */   private NamedValueInfo getNamedValueInfo(MethodParameter parameter)
/*  59:    */   {
/*  60:108 */     NamedValueInfo namedValueInfo = (NamedValueInfo)this.namedValueInfoCache.get(parameter);
/*  61:109 */     if (namedValueInfo == null)
/*  62:    */     {
/*  63:110 */       namedValueInfo = createNamedValueInfo(parameter);
/*  64:111 */       namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
/*  65:112 */       this.namedValueInfoCache.put(parameter, namedValueInfo);
/*  66:    */     }
/*  67:114 */     return namedValueInfo;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected abstract NamedValueInfo createNamedValueInfo(MethodParameter paramMethodParameter);
/*  71:    */   
/*  72:    */   private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info)
/*  73:    */   {
/*  74:130 */     String name = info.name;
/*  75:131 */     if (info.name.length() == 0)
/*  76:    */     {
/*  77:132 */       name = parameter.getParameterName();
/*  78:133 */       Assert.notNull(name, "Name for argument type [" + parameter.getParameterType().getName() + 
/*  79:134 */         "] not available, and parameter name information not found in class file either.");
/*  80:    */     }
/*  81:136 */     String defaultValue = "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(info.defaultValue) ? null : info.defaultValue;
/*  82:137 */     return new NamedValueInfo(name, info.required, defaultValue);
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected abstract Object resolveName(String paramString, MethodParameter paramMethodParameter, NativeWebRequest paramNativeWebRequest)
/*  86:    */     throws Exception;
/*  87:    */   
/*  88:    */   private Object resolveDefaultValue(String defaultValue)
/*  89:    */   {
/*  90:156 */     if (this.configurableBeanFactory == null) {
/*  91:157 */       return defaultValue;
/*  92:    */     }
/*  93:159 */     String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(defaultValue);
/*  94:160 */     BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
/*  95:161 */     if (exprResolver == null) {
/*  96:162 */       return defaultValue;
/*  97:    */     }
/*  98:164 */     return exprResolver.evaluate(placeholdersResolved, this.expressionContext);
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected abstract void handleMissingValue(String paramString, MethodParameter paramMethodParameter)
/* 102:    */     throws ServletException;
/* 103:    */   
/* 104:    */   private Object handleNullValue(String name, Object value, Class<?> paramType)
/* 105:    */   {
/* 106:179 */     if (value == null)
/* 107:    */     {
/* 108:180 */       if (Boolean.TYPE.equals(paramType)) {
/* 109:181 */         return Boolean.FALSE;
/* 110:    */       }
/* 111:183 */       if (paramType.isPrimitive()) {
/* 112:184 */         throw new IllegalStateException("Optional " + paramType + " parameter '" + name + 
/* 113:185 */           "' is present but cannot be translated into a null value due to being declared as a " + 
/* 114:186 */           "primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/* 115:    */       }
/* 116:    */     }
/* 117:189 */     return value;
/* 118:    */   }
/* 119:    */   
/* 120:    */   protected void handleResolvedValue(Object arg, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {}
/* 121:    */   
/* 122:    */   protected static class NamedValueInfo
/* 123:    */   {
/* 124:    */     private final String name;
/* 125:    */     private final boolean required;
/* 126:    */     private final String defaultValue;
/* 127:    */     
/* 128:    */     protected NamedValueInfo(String name, boolean required, String defaultValue)
/* 129:    */     {
/* 130:217 */       this.name = name;
/* 131:218 */       this.required = required;
/* 132:219 */       this.defaultValue = defaultValue;
/* 133:    */     }
/* 134:    */   }
/* 135:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.AbstractNamedValueMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */