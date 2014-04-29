/*   1:    */ package org.springframework.web.method.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.core.Conventions;
/*  10:    */ import org.springframework.core.GenericTypeResolver;
/*  11:    */ import org.springframework.core.MethodParameter;
/*  12:    */ import org.springframework.ui.ModelMap;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.validation.BindingResult;
/*  15:    */ import org.springframework.web.HttpSessionRequiredException;
/*  16:    */ import org.springframework.web.bind.WebDataBinder;
/*  17:    */ import org.springframework.web.bind.annotation.ModelAttribute;
/*  18:    */ import org.springframework.web.bind.support.SessionStatus;
/*  19:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  20:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  21:    */ import org.springframework.web.method.HandlerMethod;
/*  22:    */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*  23:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  24:    */ 
/*  25:    */ public final class ModelFactory
/*  26:    */ {
/*  27:    */   private final List<InvocableHandlerMethod> attributeMethods;
/*  28:    */   private final WebDataBinderFactory binderFactory;
/*  29:    */   private final SessionAttributesHandler sessionAttributesHandler;
/*  30:    */   
/*  31:    */   public ModelFactory(List<InvocableHandlerMethod> attributeMethods, WebDataBinderFactory binderFactory, SessionAttributesHandler sessionAttributesHandler)
/*  32:    */   {
/*  33: 77 */     this.attributeMethods = (attributeMethods != null ? attributeMethods : new ArrayList());
/*  34: 78 */     this.binderFactory = binderFactory;
/*  35: 79 */     this.sessionAttributesHandler = sessionAttributesHandler;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void initModel(NativeWebRequest request, ModelAndViewContainer mavContainer, HandlerMethod handlerMethod)
/*  39:    */     throws Exception
/*  40:    */   {
/*  41: 97 */     Map<String, ?> sessionAttrs = this.sessionAttributesHandler.retrieveAttributes(request);
/*  42: 98 */     mavContainer.mergeAttributes(sessionAttrs);
/*  43:    */     
/*  44:100 */     invokeModelAttributeMethods(request, mavContainer);
/*  45:    */     
/*  46:102 */     checkHandlerSessionAttributes(request, mavContainer, handlerMethod);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void invokeModelAttributeMethods(NativeWebRequest request, ModelAndViewContainer mavContainer)
/*  50:    */     throws Exception
/*  51:    */   {
/*  52:112 */     for (InvocableHandlerMethod attrMethod : this.attributeMethods)
/*  53:    */     {
/*  54:113 */       String modelName = ((ModelAttribute)attrMethod.getMethodAnnotation(ModelAttribute.class)).value();
/*  55:114 */       if (!mavContainer.containsAttribute(modelName))
/*  56:    */       {
/*  57:118 */         Object returnValue = attrMethod.invokeForRequest(request, mavContainer, new Object[0]);
/*  58:120 */         if (!attrMethod.isVoid())
/*  59:    */         {
/*  60:121 */           String returnValueName = getNameForReturnValue(returnValue, attrMethod.getReturnType());
/*  61:122 */           if (!mavContainer.containsAttribute(returnValueName)) {
/*  62:123 */             mavContainer.addAttribute(returnValueName, returnValue);
/*  63:    */           }
/*  64:    */         }
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void checkHandlerSessionAttributes(NativeWebRequest request, ModelAndViewContainer mavContainer, HandlerMethod handlerMethod)
/*  70:    */     throws HttpSessionRequiredException
/*  71:    */   {
/*  72:142 */     for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
/*  73:143 */       if (parameter.hasParameterAnnotation(ModelAttribute.class))
/*  74:    */       {
/*  75:144 */         String attrName = getNameForParameter(parameter);
/*  76:145 */         if ((!mavContainer.containsAttribute(attrName)) && 
/*  77:146 */           (this.sessionAttributesHandler.isHandlerSessionAttribute(attrName, parameter.getParameterType())))
/*  78:    */         {
/*  79:147 */           Object attrValue = this.sessionAttributesHandler.retrieveAttribute(request, attrName);
/*  80:148 */           if (attrValue == null) {
/*  81:149 */             throw new HttpSessionRequiredException(
/*  82:150 */               "Session attribute '" + attrName + "' not found in session: " + handlerMethod);
/*  83:    */           }
/*  84:152 */           mavContainer.addAttribute(attrName, attrValue);
/*  85:    */         }
/*  86:    */       }
/*  87:    */     }
/*  88:    */   }
/*  89:    */   
/*  90:    */   public static String getNameForReturnValue(Object returnValue, MethodParameter returnType)
/*  91:    */   {
/*  92:171 */     ModelAttribute annot = (ModelAttribute)returnType.getMethodAnnotation(ModelAttribute.class);
/*  93:172 */     if ((annot != null) && (StringUtils.hasText(annot.value()))) {
/*  94:173 */       return annot.value();
/*  95:    */     }
/*  96:176 */     Method method = returnType.getMethod();
/*  97:177 */     Class<?> resolvedType = GenericTypeResolver.resolveReturnType(method, returnType.getDeclaringClass());
/*  98:178 */     return Conventions.getVariableNameForReturnType(method, resolvedType, returnValue);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public static String getNameForParameter(MethodParameter parameter)
/* 102:    */   {
/* 103:191 */     ModelAttribute annot = (ModelAttribute)parameter.getParameterAnnotation(ModelAttribute.class);
/* 104:192 */     String attrName = annot != null ? annot.value() : null;
/* 105:193 */     return StringUtils.hasText(attrName) ? attrName : Conventions.getVariableNameForParameter(parameter);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void updateModel(NativeWebRequest request, ModelAndViewContainer mavContainer)
/* 109:    */     throws Exception
/* 110:    */   {
/* 111:205 */     if (mavContainer.getSessionStatus().isComplete()) {
/* 112:206 */       this.sessionAttributesHandler.cleanupAttributes(request);
/* 113:    */     } else {
/* 114:209 */       this.sessionAttributesHandler.storeAttributes(request, mavContainer.getModel());
/* 115:    */     }
/* 116:212 */     if (!mavContainer.isRequestHandled()) {
/* 117:213 */       updateBindingResult(request, mavContainer.getModel());
/* 118:    */     }
/* 119:    */   }
/* 120:    */   
/* 121:    */   private void updateBindingResult(NativeWebRequest request, ModelMap model)
/* 122:    */     throws Exception
/* 123:    */   {
/* 124:221 */     List<String> keyNames = new ArrayList((Collection)model.keySet());
/* 125:222 */     for (String name : keyNames)
/* 126:    */     {
/* 127:223 */       Object value = model.get(name);
/* 128:225 */       if (isBindingCandidate(name, value))
/* 129:    */       {
/* 130:226 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + name;
/* 131:228 */         if (!model.containsAttribute(bindingResultKey))
/* 132:    */         {
/* 133:229 */           WebDataBinder dataBinder = this.binderFactory.createBinder(request, value, name);
/* 134:230 */           model.put(bindingResultKey, dataBinder.getBindingResult());
/* 135:    */         }
/* 136:    */       }
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   private boolean isBindingCandidate(String attributeName, Object value)
/* 141:    */   {
/* 142:240 */     if (attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 143:241 */       return false;
/* 144:    */     }
/* 145:244 */     Class<?> attrType = value != null ? value.getClass() : null;
/* 146:245 */     if (this.sessionAttributesHandler.isHandlerSessionAttribute(attributeName, attrType)) {
/* 147:246 */       return true;
/* 148:    */     }
/* 149:250 */     return (value != null) && (!value.getClass().isArray()) && (!(value instanceof Collection)) && (!(value instanceof Map)) && (!BeanUtils.isSimpleValueType(value.getClass()));
/* 150:    */   }
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.ModelFactory
 * JD-Core Version:    0.7.0.1
 */