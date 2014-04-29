/*   1:    */ package org.springframework.web.method.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.springframework.core.GenericTypeResolver;
/*   8:    */ import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
/*   9:    */ import org.springframework.core.MethodParameter;
/*  10:    */ import org.springframework.core.ParameterNameDiscoverer;
/*  11:    */ import org.springframework.util.ReflectionUtils;
/*  12:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  13:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  14:    */ import org.springframework.web.method.HandlerMethod;
/*  15:    */ 
/*  16:    */ public class InvocableHandlerMethod
/*  17:    */   extends HandlerMethod
/*  18:    */ {
/*  19: 50 */   private HandlerMethodArgumentResolverComposite argumentResolvers = new HandlerMethodArgumentResolverComposite();
/*  20:    */   private WebDataBinderFactory dataBinderFactory;
/*  21: 54 */   private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
/*  22:    */   
/*  23:    */   public InvocableHandlerMethod(Object bean, Method method)
/*  24:    */   {
/*  25: 62 */     super(bean, method);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes)
/*  29:    */     throws NoSuchMethodException
/*  30:    */   {
/*  31: 74 */     super(bean, methodName, parameterTypes);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory)
/*  35:    */   {
/*  36: 83 */     this.dataBinderFactory = dataBinderFactory;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers)
/*  40:    */   {
/*  41: 90 */     this.argumentResolvers = argumentResolvers;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*  45:    */   {
/*  46: 98 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public final Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs)
/*  50:    */     throws Exception
/*  51:    */   {
/*  52:117 */     Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
/*  53:119 */     if (this.logger.isTraceEnabled())
/*  54:    */     {
/*  55:120 */       StringBuilder builder = new StringBuilder("Invoking [");
/*  56:121 */       builder.append(getMethod().getName()).append("] method with arguments ");
/*  57:122 */       builder.append(Arrays.asList(args));
/*  58:123 */       this.logger.trace(builder.toString());
/*  59:    */     }
/*  60:126 */     Object returnValue = invoke(args);
/*  61:128 */     if (this.logger.isTraceEnabled()) {
/*  62:129 */       this.logger.trace("Method [" + getMethod().getName() + "] returned [" + returnValue + "]");
/*  63:    */     }
/*  64:132 */     return returnValue;
/*  65:    */   }
/*  66:    */   
/*  67:    */   private Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs)
/*  68:    */     throws Exception
/*  69:    */   {
/*  70:141 */     MethodParameter[] parameters = getMethodParameters();
/*  71:142 */     Object[] args = new Object[parameters.length];
/*  72:143 */     for (int i = 0; i < parameters.length; i++)
/*  73:    */     {
/*  74:144 */       MethodParameter parameter = parameters[i];
/*  75:145 */       parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
/*  76:146 */       GenericTypeResolver.resolveParameterType(parameter, getBean().getClass());
/*  77:    */       
/*  78:148 */       args[i] = resolveProvidedArgument(parameter, providedArgs);
/*  79:149 */       if (args[i] == null) {
/*  80:153 */         if (this.argumentResolvers.supportsParameter(parameter))
/*  81:    */         {
/*  82:    */           try
/*  83:    */           {
/*  84:155 */             args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
/*  85:    */           }
/*  86:    */           catch (Exception ex)
/*  87:    */           {
/*  88:158 */             if (this.logger.isTraceEnabled()) {
/*  89:159 */               this.logger.trace(getArgumentResolutionErrorMessage("Error resolving argument", i), ex);
/*  90:    */             }
/*  91:161 */             throw ex;
/*  92:    */           }
/*  93:    */         }
/*  94:165 */         else if (args[i] == null)
/*  95:    */         {
/*  96:166 */           String msg = getArgumentResolutionErrorMessage("No suitable resolver for argument", i);
/*  97:167 */           throw new IllegalStateException(msg);
/*  98:    */         }
/*  99:    */       }
/* 100:    */     }
/* 101:170 */     return args;
/* 102:    */   }
/* 103:    */   
/* 104:    */   private String getArgumentResolutionErrorMessage(String message, int index)
/* 105:    */   {
/* 106:174 */     MethodParameter param = getMethodParameters()[index];
/* 107:175 */     message = message + " [" + index + "] [type=" + param.getParameterType().getName() + "]";
/* 108:176 */     return getDetailedErrorMessage(message);
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected String getDetailedErrorMessage(String message)
/* 112:    */   {
/* 113:184 */     StringBuilder sb = new StringBuilder(message).append("\n");
/* 114:185 */     sb.append("HandlerMethod details: \n");
/* 115:186 */     sb.append("Controller [").append(getBeanType().getName()).append("]\n");
/* 116:187 */     sb.append("Method [").append(getBridgedMethod().toGenericString()).append("]\n");
/* 117:188 */     return sb.toString();
/* 118:    */   }
/* 119:    */   
/* 120:    */   private Object resolveProvidedArgument(MethodParameter parameter, Object... providedArgs)
/* 121:    */   {
/* 122:195 */     if ((providedArgs == null) || (parameter.hasParameterAnnotations())) {
/* 123:196 */       return null;
/* 124:    */     }
/* 125:198 */     for (Object providedArg : providedArgs) {
/* 126:199 */       if (parameter.getParameterType().isInstance(providedArg)) {
/* 127:200 */         return providedArg;
/* 128:    */       }
/* 129:    */     }
/* 130:203 */     return null;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private Object invoke(Object... args)
/* 134:    */     throws Exception
/* 135:    */   {
/* 136:210 */     ReflectionUtils.makeAccessible(getBridgedMethod());
/* 137:    */     try
/* 138:    */     {
/* 139:212 */       return getBridgedMethod().invoke(getBean(), args);
/* 140:    */     }
/* 141:    */     catch (IllegalArgumentException e)
/* 142:    */     {
/* 143:215 */       String msg = getInvocationErrorMessage(e.getMessage(), args);
/* 144:216 */       throw new IllegalArgumentException(msg, e);
/* 145:    */     }
/* 146:    */     catch (InvocationTargetException e)
/* 147:    */     {
/* 148:220 */       Throwable targetException = e.getTargetException();
/* 149:221 */       if ((targetException instanceof RuntimeException)) {
/* 150:222 */         throw ((RuntimeException)targetException);
/* 151:    */       }
/* 152:224 */       if ((targetException instanceof Error)) {
/* 153:225 */         throw ((Error)targetException);
/* 154:    */       }
/* 155:227 */       if ((targetException instanceof Exception)) {
/* 156:228 */         throw ((Exception)targetException);
/* 157:    */       }
/* 158:231 */       String msg = getInvocationErrorMessage("Failed to invoke controller method", args);
/* 159:232 */       throw new IllegalStateException(msg, targetException);
/* 160:    */     }
/* 161:    */   }
/* 162:    */   
/* 163:    */   private String getInvocationErrorMessage(String message, Object[] resolvedArgs)
/* 164:    */   {
/* 165:238 */     StringBuilder sb = new StringBuilder(getDetailedErrorMessage(message));
/* 166:239 */     sb.append("Resolved arguments: \n");
/* 167:240 */     for (int i = 0; i < resolvedArgs.length; i++)
/* 168:    */     {
/* 169:241 */       sb.append("[").append(i).append("] ");
/* 170:242 */       if (resolvedArgs[i] == null)
/* 171:    */       {
/* 172:243 */         sb.append("[null] \n");
/* 173:    */       }
/* 174:    */       else
/* 175:    */       {
/* 176:246 */         sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
/* 177:247 */         sb.append("[value=").append(resolvedArgs[i]).append("]\n");
/* 178:    */       }
/* 179:    */     }
/* 180:250 */     return sb.toString();
/* 181:    */   }
/* 182:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.InvocableHandlerMethod
 * JD-Core Version:    0.7.0.1
 */