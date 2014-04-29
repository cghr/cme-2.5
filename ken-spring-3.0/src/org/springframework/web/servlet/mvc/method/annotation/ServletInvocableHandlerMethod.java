/*   1:    */ package org.springframework.web.servlet.mvc.method.annotation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.http.HttpStatus;
/*   9:    */ import org.springframework.util.StringUtils;
/*  10:    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*  11:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  12:    */ import org.springframework.web.context.request.ServletWebRequest;
/*  13:    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*  14:    */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*  15:    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*  16:    */ import org.springframework.web.servlet.View;
/*  17:    */ 
/*  18:    */ public class ServletInvocableHandlerMethod
/*  19:    */   extends InvocableHandlerMethod
/*  20:    */ {
/*  21:    */   private HttpStatus responseStatus;
/*  22:    */   private String responseReason;
/*  23:    */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*  24:    */   
/*  25:    */   public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers)
/*  26:    */   {
/*  27: 55 */     this.returnValueHandlers = returnValueHandlers;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public ServletInvocableHandlerMethod(Object handler, Method method)
/*  31:    */   {
/*  32: 64 */     super(handler, method);
/*  33:    */     
/*  34: 66 */     ResponseStatus annotation = (ResponseStatus)getMethodAnnotation(ResponseStatus.class);
/*  35: 67 */     if (annotation != null)
/*  36:    */     {
/*  37: 68 */       this.responseStatus = annotation.value();
/*  38: 69 */       this.responseReason = annotation.reason();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public final void invokeAndHandle(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45: 96 */     Object returnValue = invokeForRequest(request, mavContainer, providedArgs);
/*  46:    */     
/*  47: 98 */     setResponseStatus((ServletWebRequest)request);
/*  48:100 */     if ((returnValue == null) && (
/*  49:101 */       (isRequestNotModified(request)) || (hasResponseStatus()) || (mavContainer.isRequestHandled())))
/*  50:    */     {
/*  51:102 */       mavContainer.setRequestHandled(true);
/*  52:103 */       return;
/*  53:    */     }
/*  54:107 */     mavContainer.setRequestHandled(false);
/*  55:    */     try
/*  56:    */     {
/*  57:110 */       this.returnValueHandlers.handleReturnValue(returnValue, getReturnType(), mavContainer, request);
/*  58:    */     }
/*  59:    */     catch (Exception ex)
/*  60:    */     {
/*  61:112 */       if (this.logger.isTraceEnabled()) {
/*  62:113 */         this.logger.trace(getReturnValueHandlingErrorMessage("Error handling return value", returnValue), ex);
/*  63:    */       }
/*  64:115 */       throw ex;
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   private String getReturnValueHandlingErrorMessage(String message, Object returnValue)
/*  69:    */   {
/*  70:120 */     StringBuilder sb = new StringBuilder(message);
/*  71:121 */     if (returnValue != null) {
/*  72:122 */       sb.append(" [type=" + returnValue.getClass().getName() + "] ");
/*  73:    */     }
/*  74:124 */     sb.append("[value=" + returnValue + "]");
/*  75:125 */     return getDetailedErrorMessage(sb.toString());
/*  76:    */   }
/*  77:    */   
/*  78:    */   private void setResponseStatus(ServletWebRequest webRequest)
/*  79:    */     throws IOException
/*  80:    */   {
/*  81:132 */     if (this.responseStatus != null)
/*  82:    */     {
/*  83:133 */       if (StringUtils.hasText(this.responseReason)) {
/*  84:134 */         webRequest.getResponse().sendError(this.responseStatus.value(), this.responseReason);
/*  85:    */       } else {
/*  86:137 */         webRequest.getResponse().setStatus(this.responseStatus.value());
/*  87:    */       }
/*  88:141 */       webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, this.responseStatus);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   private boolean isRequestNotModified(NativeWebRequest request)
/*  93:    */   {
/*  94:151 */     return ((ServletWebRequest)request).isNotModified();
/*  95:    */   }
/*  96:    */   
/*  97:    */   private boolean hasResponseStatus()
/*  98:    */   {
/*  99:158 */     return this.responseStatus != null;
/* 100:    */   }
/* 101:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
 * JD-Core Version:    0.7.0.1
 */