/*   1:    */ package org.springframework.web.method.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.concurrent.ConcurrentHashMap;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.core.MethodParameter;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.web.context.request.NativeWebRequest;
/*  13:    */ 
/*  14:    */ public class HandlerMethodReturnValueHandlerComposite
/*  15:    */   implements HandlerMethodReturnValueHandler
/*  16:    */ {
/*  17: 40 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18: 43 */   private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList();
/*  19: 46 */   private final Map<MethodParameter, HandlerMethodReturnValueHandler> returnValueHandlerCache = new ConcurrentHashMap();
/*  20:    */   
/*  21:    */   public List<HandlerMethodReturnValueHandler> getHandlers()
/*  22:    */   {
/*  23: 52 */     return Collections.unmodifiableList(this.returnValueHandlers);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public boolean supportsReturnType(MethodParameter returnType)
/*  27:    */   {
/*  28: 60 */     return getReturnValueHandler(returnType) != null;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 71 */     HandlerMethodReturnValueHandler handler = getReturnValueHandler(returnType);
/*  35: 72 */     Assert.notNull(handler, "Unknown return value type [" + returnType.getParameterType().getName() + "]");
/*  36: 73 */     handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
/*  37:    */   }
/*  38:    */   
/*  39:    */   private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType)
/*  40:    */   {
/*  41: 80 */     HandlerMethodReturnValueHandler result = (HandlerMethodReturnValueHandler)this.returnValueHandlerCache.get(returnType);
/*  42: 81 */     if (result == null) {
/*  43: 82 */       for (HandlerMethodReturnValueHandler returnValueHandler : this.returnValueHandlers)
/*  44:    */       {
/*  45: 83 */         if (this.logger.isTraceEnabled()) {
/*  46: 84 */           this.logger.trace("Testing if return value handler [" + returnValueHandler + "] supports [" + 
/*  47: 85 */             returnType.getGenericParameterType() + "]");
/*  48:    */         }
/*  49: 87 */         if (returnValueHandler.supportsReturnType(returnType))
/*  50:    */         {
/*  51: 88 */           result = returnValueHandler;
/*  52: 89 */           this.returnValueHandlerCache.put(returnType, returnValueHandler);
/*  53: 90 */           break;
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57: 94 */     return result;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public HandlerMethodReturnValueHandlerComposite addHandler(HandlerMethodReturnValueHandler returnValuehandler)
/*  61:    */   {
/*  62:101 */     this.returnValueHandlers.add(returnValuehandler);
/*  63:102 */     return this;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public HandlerMethodReturnValueHandlerComposite addHandlers(List<? extends HandlerMethodReturnValueHandler> returnValueHandlers)
/*  67:    */   {
/*  68:110 */     if (returnValueHandlers != null) {
/*  69:111 */       for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
/*  70:112 */         this.returnValueHandlers.add(handler);
/*  71:    */       }
/*  72:    */     }
/*  73:115 */     return this;
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite
 * JD-Core Version:    0.7.0.1
 */