/*   1:    */ package org.springframework.web.servlet.mvc.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.List;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.ConversionNotSupportedException;
/*  10:    */ import org.springframework.beans.TypeMismatchException;
/*  11:    */ import org.springframework.http.MediaType;
/*  12:    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*  13:    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*  14:    */ import org.springframework.util.CollectionUtils;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*  17:    */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*  18:    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*  19:    */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*  20:    */ import org.springframework.web.bind.ServletRequestBindingException;
/*  21:    */ import org.springframework.web.method.annotation.support.MethodArgumentNotValidException;
/*  22:    */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*  23:    */ import org.springframework.web.servlet.ModelAndView;
/*  24:    */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
/*  25:    */ import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
/*  26:    */ 
/*  27:    */ public class DefaultHandlerExceptionResolver
/*  28:    */   extends AbstractHandlerExceptionResolver
/*  29:    */ {
/*  30:    */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  31: 81 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*  32:    */   
/*  33:    */   public DefaultHandlerExceptionResolver()
/*  34:    */   {
/*  35: 88 */     setOrder(2147483647);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42: 97 */       if ((ex instanceof NoSuchRequestHandlingMethodException)) {
/*  43: 98 */         return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException)ex, request, response, 
/*  44: 99 */           handler);
/*  45:    */       }
/*  46:101 */       if ((ex instanceof HttpRequestMethodNotSupportedException)) {
/*  47:102 */         return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, request, 
/*  48:103 */           response, handler);
/*  49:    */       }
/*  50:105 */       if ((ex instanceof HttpMediaTypeNotSupportedException)) {
/*  51:106 */         return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, request, response, 
/*  52:107 */           handler);
/*  53:    */       }
/*  54:109 */       if ((ex instanceof HttpMediaTypeNotAcceptableException)) {
/*  55:110 */         return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, request, response, 
/*  56:111 */           handler);
/*  57:    */       }
/*  58:113 */       if ((ex instanceof MissingServletRequestParameterException)) {
/*  59:114 */         return handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, request, 
/*  60:115 */           response, handler);
/*  61:    */       }
/*  62:117 */       if ((ex instanceof ServletRequestBindingException)) {
/*  63:118 */         return handleServletRequestBindingException((ServletRequestBindingException)ex, request, response, 
/*  64:119 */           handler);
/*  65:    */       }
/*  66:121 */       if ((ex instanceof ConversionNotSupportedException)) {
/*  67:122 */         return handleConversionNotSupported((ConversionNotSupportedException)ex, request, response, handler);
/*  68:    */       }
/*  69:124 */       if ((ex instanceof TypeMismatchException)) {
/*  70:125 */         return handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
/*  71:    */       }
/*  72:127 */       if ((ex instanceof HttpMessageNotReadableException)) {
/*  73:128 */         return handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
/*  74:    */       }
/*  75:130 */       if ((ex instanceof HttpMessageNotWritableException)) {
/*  76:131 */         return handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, request, response, handler);
/*  77:    */       }
/*  78:133 */       if ((ex instanceof MethodArgumentNotValidException)) {
/*  79:134 */         return handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex, request, response, handler);
/*  80:    */       }
/*  81:136 */       if ((ex instanceof MissingServletRequestPartException)) {
/*  82:137 */         return handleMissingServletRequestPartException((MissingServletRequestPartException)ex, request, response, handler);
/*  83:    */       }
/*  84:    */     }
/*  85:    */     catch (Exception handlerException)
/*  86:    */     {
/*  87:141 */       this.logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
/*  88:    */     }
/*  89:143 */     return null;
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/*  93:    */     throws IOException
/*  94:    */   {
/*  95:162 */     pageNotFoundLogger.warn(ex.getMessage());
/*  96:163 */     response.sendError(404);
/*  97:164 */     return new ModelAndView();
/*  98:    */   }
/*  99:    */   
/* 100:    */   protected ModelAndView handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 101:    */     throws IOException
/* 102:    */   {
/* 103:183 */     pageNotFoundLogger.warn(ex.getMessage());
/* 104:184 */     String[] supportedMethods = ex.getSupportedMethods();
/* 105:185 */     if (supportedMethods != null) {
/* 106:186 */       response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
/* 107:    */     }
/* 108:188 */     response.sendError(405, ex.getMessage());
/* 109:189 */     return new ModelAndView();
/* 110:    */   }
/* 111:    */   
/* 112:    */   protected ModelAndView handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 113:    */     throws IOException
/* 114:    */   {
/* 115:207 */     response.sendError(415);
/* 116:208 */     List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
/* 117:209 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 118:210 */       response.setHeader("Accept", MediaType.toString(mediaTypes));
/* 119:    */     }
/* 120:212 */     return new ModelAndView();
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected ModelAndView handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 124:    */     throws IOException
/* 125:    */   {
/* 126:231 */     response.sendError(406);
/* 127:232 */     return new ModelAndView();
/* 128:    */   }
/* 129:    */   
/* 130:    */   protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 131:    */     throws IOException
/* 132:    */   {
/* 133:250 */     response.sendError(400);
/* 134:251 */     return new ModelAndView();
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected ModelAndView handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 138:    */     throws IOException
/* 139:    */   {
/* 140:268 */     response.sendError(400);
/* 141:269 */     return new ModelAndView();
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected ModelAndView handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 145:    */     throws IOException
/* 146:    */   {
/* 147:286 */     response.sendError(500);
/* 148:287 */     return new ModelAndView();
/* 149:    */   }
/* 150:    */   
/* 151:    */   protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 152:    */     throws IOException
/* 153:    */   {
/* 154:304 */     response.sendError(400);
/* 155:305 */     return new ModelAndView();
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 159:    */     throws IOException
/* 160:    */   {
/* 161:324 */     response.sendError(400);
/* 162:325 */     return new ModelAndView();
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected ModelAndView handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 166:    */     throws IOException
/* 167:    */   {
/* 168:344 */     response.sendError(500);
/* 169:345 */     return new ModelAndView();
/* 170:    */   }
/* 171:    */   
/* 172:    */   protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 173:    */     throws IOException
/* 174:    */   {
/* 175:360 */     response.sendError(400);
/* 176:361 */     return new ModelAndView();
/* 177:    */   }
/* 178:    */   
/* 179:    */   protected ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HttpServletResponse response, Object handler)
/* 180:    */     throws IOException
/* 181:    */   {
/* 182:376 */     response.sendError(400);
/* 183:377 */     return new ModelAndView();
/* 184:    */   }
/* 185:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 * JD-Core Version:    0.7.0.1
 */