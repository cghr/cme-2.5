/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation.support;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import org.springframework.core.GenericCollectionTypeResolver;
/*   8:    */ import org.springframework.core.MethodParameter;
/*   9:    */ import org.springframework.http.HttpInputMessage;
/*  10:    */ import org.springframework.http.converter.HttpMessageConverter;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.validation.BindingResult;
/*  13:    */ import org.springframework.web.bind.WebDataBinder;
/*  14:    */ import org.springframework.web.bind.annotation.RequestParam;
/*  15:    */ import org.springframework.web.bind.annotation.RequestPart;
/*  16:    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*  17:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  18:    */ import org.springframework.web.method.annotation.support.MethodArgumentNotValidException;
/*  19:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  20:    */ import org.springframework.web.multipart.MultipartException;
/*  21:    */ import org.springframework.web.multipart.MultipartFile;
/*  22:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  23:    */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*  24:    */ import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;
/*  25:    */ import org.springframework.web.util.WebUtils;
/*  26:    */ 
/*  27:    */ public class RequestPartMethodArgumentResolver
/*  28:    */   extends AbstractMessageConverterMethodArgumentResolver
/*  29:    */ {
/*  30:    */   public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters)
/*  31:    */   {
/*  32: 78 */     super(messageConverters);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean supportsParameter(MethodParameter parameter)
/*  36:    */   {
/*  37: 90 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/*  38: 91 */       return true;
/*  39:    */     }
/*  40: 94 */     if (parameter.hasParameterAnnotation(RequestParam.class)) {
/*  41: 95 */       return false;
/*  42:    */     }
/*  43: 97 */     if (MultipartFile.class.equals(parameter.getParameterType())) {
/*  44: 98 */       return true;
/*  45:    */     }
/*  46:100 */     if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
/*  47:101 */       return true;
/*  48:    */     }
/*  49:104 */     return false;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory)
/*  53:    */     throws Exception
/*  54:    */   {
/*  55:114 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/*  56:115 */     if (!isMultipartRequest(servletRequest)) {
/*  57:116 */       throw new MultipartException("The current request is not a multipart request.");
/*  58:    */     }
/*  59:119 */     MultipartHttpServletRequest multipartRequest = 
/*  60:120 */       (MultipartHttpServletRequest)WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);
/*  61:    */     
/*  62:122 */     String partName = getPartName(parameter);
/*  63:    */     Object arg;
/*  64:    */     Object arg;
/*  65:125 */     if (MultipartFile.class.equals(parameter.getParameterType()))
/*  66:    */     {
/*  67:126 */       Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*  68:127 */       arg = multipartRequest.getFile(partName);
/*  69:    */     }
/*  70:    */     else
/*  71:    */     {
/*  72:    */       Object arg;
/*  73:129 */       if (isMultipartFileCollection(parameter))
/*  74:    */       {
/*  75:130 */         Assert.notNull(multipartRequest, "Expected MultipartHttpServletRequest: is a MultipartResolver configured?");
/*  76:131 */         arg = multipartRequest.getFiles(partName);
/*  77:    */       }
/*  78:    */       else
/*  79:    */       {
/*  80:    */         Object arg;
/*  81:133 */         if ("javax.servlet.http.Part".equals(parameter.getParameterType().getName())) {
/*  82:134 */           arg = servletRequest.getPart(partName);
/*  83:    */         } else {
/*  84:    */           try
/*  85:    */           {
/*  86:138 */             HttpInputMessage inputMessage = new RequestPartServletServerHttpRequest(servletRequest, partName);
/*  87:139 */             Object arg = readWithMessageConverters(inputMessage, parameter, parameter.getParameterType());
/*  88:140 */             if (isValidationApplicable(arg, parameter))
/*  89:    */             {
/*  90:141 */               WebDataBinder binder = binderFactory.createBinder(request, arg, partName);
/*  91:142 */               binder.validate();
/*  92:143 */               BindingResult bindingResult = binder.getBindingResult();
/*  93:144 */               if (bindingResult.hasErrors()) {
/*  94:145 */                 throw new MethodArgumentNotValidException(parameter, bindingResult);
/*  95:    */               }
/*  96:    */             }
/*  97:    */           }
/*  98:    */           catch (MissingServletRequestPartException localMissingServletRequestPartException)
/*  99:    */           {
/* 100:151 */             arg = null;
/* 101:    */           }
/* 102:    */         }
/* 103:    */       }
/* 104:    */     }
/* 105:155 */     RequestPart annot = (RequestPart)parameter.getParameterAnnotation(RequestPart.class);
/* 106:156 */     boolean isRequired = annot != null ? annot.required() : true;
/* 107:158 */     if ((arg == null) && (isRequired)) {
/* 108:159 */       throw new MissingServletRequestPartException(partName);
/* 109:    */     }
/* 110:162 */     return arg;
/* 111:    */   }
/* 112:    */   
/* 113:    */   private boolean isMultipartRequest(HttpServletRequest request)
/* 114:    */   {
/* 115:166 */     if (!"post".equals(request.getMethod().toLowerCase())) {
/* 116:167 */       return false;
/* 117:    */     }
/* 118:169 */     String contentType = request.getContentType();
/* 119:170 */     return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
/* 120:    */   }
/* 121:    */   
/* 122:    */   private String getPartName(MethodParameter parameter)
/* 123:    */   {
/* 124:174 */     RequestPart annot = (RequestPart)parameter.getParameterAnnotation(RequestPart.class);
/* 125:175 */     String partName = annot != null ? annot.value() : "";
/* 126:176 */     if (partName.length() == 0)
/* 127:    */     {
/* 128:177 */       partName = parameter.getParameterName();
/* 129:178 */       Assert.notNull(partName, "Request part name for argument type [" + parameter.getParameterType().getName() + 
/* 130:179 */         "] not available, and parameter name information not found in class file either.");
/* 131:    */     }
/* 132:181 */     return partName;
/* 133:    */   }
/* 134:    */   
/* 135:    */   private boolean isMultipartFileCollection(MethodParameter parameter)
/* 136:    */   {
/* 137:185 */     Class<?> paramType = parameter.getParameterType();
/* 138:186 */     if ((Collection.class.equals(paramType)) || (List.class.isAssignableFrom(paramType)))
/* 139:    */     {
/* 140:187 */       Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
/* 141:188 */       if ((valueType != null) && (valueType.equals(MultipartFile.class))) {
/* 142:189 */         return true;
/* 143:    */       }
/* 144:    */     }
/* 145:192 */     return false;
/* 146:    */   }
/* 147:    */   
/* 148:    */   protected boolean isValidationApplicable(Object argument, MethodParameter parameter)
/* 149:    */   {
/* 150:202 */     if (argument == null) {
/* 151:203 */       return false;
/* 152:    */     }
/* 153:206 */     Annotation[] annotations = parameter.getParameterAnnotations();
/* 154:207 */     for (Annotation annot : annotations) {
/* 155:208 */       if ("Valid".equals(annot.annotationType().getSimpleName())) {
/* 156:209 */         return true;
/* 157:    */       }
/* 158:    */     }
/* 159:212 */     return false;
/* 160:    */   }
/* 161:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.support.RequestPartMethodArgumentResolver
 * JD-Core Version:    0.7.0.1
 */