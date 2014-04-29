/*   1:    */ package org.springframework.web.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import org.springframework.beans.BeanUtils;
/*   9:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  10:    */ import org.springframework.core.GenericCollectionTypeResolver;
/*  11:    */ import org.springframework.core.MethodParameter;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*  15:    */ import org.springframework.web.bind.annotation.RequestParam;
/*  16:    */ import org.springframework.web.bind.annotation.RequestPart;
/*  17:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  18:    */ import org.springframework.web.multipart.MultipartException;
/*  19:    */ import org.springframework.web.multipart.MultipartFile;
/*  20:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  21:    */ import org.springframework.web.util.WebUtils;
/*  22:    */ 
/*  23:    */ public class RequestParamMethodArgumentResolver
/*  24:    */   extends AbstractNamedValueMethodArgumentResolver
/*  25:    */ {
/*  26:    */   private final boolean useDefaultResolution;
/*  27:    */   
/*  28:    */   public RequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution)
/*  29:    */   {
/*  30: 85 */     super(beanFactory);
/*  31: 86 */     this.useDefaultResolution = useDefaultResolution;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean supportsParameter(MethodParameter parameter)
/*  35:    */   {
/*  36:105 */     Class<?> paramType = parameter.getParameterType();
/*  37:106 */     if (parameter.hasParameterAnnotation(RequestParam.class))
/*  38:    */     {
/*  39:107 */       if (Map.class.isAssignableFrom(paramType))
/*  40:    */       {
/*  41:108 */         String paramName = ((RequestParam)parameter.getParameterAnnotation(RequestParam.class)).value();
/*  42:109 */         return StringUtils.hasText(paramName);
/*  43:    */       }
/*  44:112 */       return true;
/*  45:    */     }
/*  46:116 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/*  47:117 */       return false;
/*  48:    */     }
/*  49:119 */     if ((MultipartFile.class.equals(paramType)) || ("javax.servlet.http.Part".equals(paramType.getName()))) {
/*  50:120 */       return true;
/*  51:    */     }
/*  52:122 */     if (this.useDefaultResolution) {
/*  53:123 */       return BeanUtils.isSimpleProperty(paramType);
/*  54:    */     }
/*  55:126 */     return false;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter)
/*  59:    */   {
/*  60:133 */     RequestParam annotation = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/*  61:134 */     return annotation != null ? 
/*  62:135 */       new RequestParamNamedValueInfo(annotation, null) : 
/*  63:136 */       new RequestParamNamedValueInfo(null);
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest)
/*  67:    */     throws Exception
/*  68:    */   {
/*  69:144 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  70:145 */     MultipartHttpServletRequest multipartRequest = 
/*  71:146 */       (MultipartHttpServletRequest)WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);
/*  72:    */     Object arg;
/*  73:    */     Object arg;
/*  74:148 */     if (MultipartFile.class.equals(parameter.getParameterType()))
/*  75:    */     {
/*  76:149 */       assertIsMultipartRequest(servletRequest);
/*  77:150 */       Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*  78:151 */       arg = multipartRequest.getFile(name);
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:    */       Object arg;
/*  83:153 */       if (isMultipartFileCollection(parameter))
/*  84:    */       {
/*  85:154 */         assertIsMultipartRequest(servletRequest);
/*  86:155 */         Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*  87:156 */         arg = multipartRequest.getFiles(name);
/*  88:    */       }
/*  89:    */       else
/*  90:    */       {
/*  91:    */         Object arg;
/*  92:158 */         if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName()))
/*  93:    */         {
/*  94:159 */           assertIsMultipartRequest(servletRequest);
/*  95:160 */           arg = servletRequest.getPart(name);
/*  96:    */         }
/*  97:    */         else
/*  98:    */         {
/*  99:163 */           arg = null;
/* 100:164 */           if (multipartRequest != null)
/* 101:    */           {
/* 102:165 */             List<MultipartFile> files = multipartRequest.getFiles(name);
/* 103:166 */             if (!files.isEmpty()) {
/* 104:167 */               arg = files.size() == 1 ? files.get(0) : files;
/* 105:    */             }
/* 106:    */           }
/* 107:170 */           if (arg == null)
/* 108:    */           {
/* 109:171 */             String[] paramValues = webRequest.getParameterValues(name);
/* 110:172 */             if (paramValues != null) {
/* 111:173 */               arg = paramValues.length == 1 ? paramValues[0] : paramValues;
/* 112:    */             }
/* 113:    */           }
/* 114:    */         }
/* 115:    */       }
/* 116:    */     }
/* 117:178 */     return arg;
/* 118:    */   }
/* 119:    */   
/* 120:    */   private void assertIsMultipartRequest(HttpServletRequest request)
/* 121:    */   {
/* 122:182 */     if (!isMultipartRequest(request)) {
/* 123:183 */       throw new MultipartException("The current request is not a multipart request.");
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   private boolean isMultipartRequest(HttpServletRequest request)
/* 128:    */   {
/* 129:188 */     if (!"post".equals(request.getMethod().toLowerCase())) {
/* 130:189 */       return false;
/* 131:    */     }
/* 132:191 */     String contentType = request.getContentType();
/* 133:192 */     return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
/* 134:    */   }
/* 135:    */   
/* 136:    */   private boolean isMultipartFileCollection(MethodParameter parameter)
/* 137:    */   {
/* 138:196 */     Class<?> paramType = parameter.getParameterType();
/* 139:197 */     if ((Collection.class.equals(paramType)) || (List.class.isAssignableFrom(paramType)))
/* 140:    */     {
/* 141:198 */       Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
/* 142:199 */       if ((valueType != null) && (valueType.equals(MultipartFile.class))) {
/* 143:200 */         return true;
/* 144:    */       }
/* 145:    */     }
/* 146:203 */     return false;
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected void handleMissingValue(String paramName, MethodParameter parameter)
/* 150:    */     throws ServletException
/* 151:    */   {
/* 152:208 */     throw new MissingServletRequestParameterException(paramName, parameter.getParameterType().getSimpleName());
/* 153:    */   }
/* 154:    */   
/* 155:    */   private class RequestParamNamedValueInfo
/* 156:    */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/* 157:    */   {
/* 158:    */     private RequestParamNamedValueInfo()
/* 159:    */     {
/* 160:214 */       super(false, "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/* 161:    */     }
/* 162:    */     
/* 163:    */     private RequestParamNamedValueInfo(RequestParam annotation)
/* 164:    */     {
/* 165:218 */       super(annotation.required(), annotation.defaultValue());
/* 166:    */     }
/* 167:    */   }
/* 168:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.annotation.support.RequestParamMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */