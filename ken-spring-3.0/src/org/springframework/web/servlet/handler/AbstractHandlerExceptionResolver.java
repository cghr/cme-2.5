/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.Set;
/*   4:    */ import javax.servlet.http.HttpServletRequest;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.core.Ordered;
/*   9:    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*  10:    */ import org.springframework.web.servlet.ModelAndView;
/*  11:    */ 
/*  12:    */ public abstract class AbstractHandlerExceptionResolver
/*  13:    */   implements HandlerExceptionResolver, Ordered
/*  14:    */ {
/*  15:    */   private static final String HEADER_PRAGMA = "Pragma";
/*  16:    */   private static final String HEADER_EXPIRES = "Expires";
/*  17:    */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*  18: 50 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19: 52 */   private int order = 2147483647;
/*  20:    */   private Set mappedHandlers;
/*  21:    */   private Class[] mappedHandlerClasses;
/*  22:    */   private Log warnLogger;
/*  23: 60 */   private boolean preventResponseCaching = false;
/*  24:    */   
/*  25:    */   public void setOrder(int order)
/*  26:    */   {
/*  27: 64 */     this.order = order;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int getOrder()
/*  31:    */   {
/*  32: 68 */     return this.order;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setMappedHandlers(Set mappedHandlers)
/*  36:    */   {
/*  37: 80 */     this.mappedHandlers = mappedHandlers;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setMappedHandlerClasses(Class[] mappedHandlerClasses)
/*  41:    */   {
/*  42: 93 */     this.mappedHandlerClasses = mappedHandlerClasses;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setWarnLogCategory(String loggerName)
/*  46:    */   {
/*  47:107 */     this.warnLogger = LogFactory.getLog(loggerName);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setPreventResponseCaching(boolean preventResponseCaching)
/*  51:    */   {
/*  52:117 */     this.preventResponseCaching = preventResponseCaching;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*  56:    */   {
/*  57:129 */     if (shouldApplyTo(request, handler))
/*  58:    */     {
/*  59:131 */       if (this.logger.isDebugEnabled()) {
/*  60:132 */         this.logger.debug("Resolving exception from handler [" + handler + "]: " + ex);
/*  61:    */       }
/*  62:134 */       logException(ex, request);
/*  63:135 */       prepareResponse(ex, response);
/*  64:136 */       return doResolveException(request, response, handler, ex);
/*  65:    */     }
/*  66:139 */     return null;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean shouldApplyTo(HttpServletRequest request, Object handler)
/*  70:    */   {
/*  71:156 */     if (handler != null)
/*  72:    */     {
/*  73:157 */       if ((this.mappedHandlers != null) && (this.mappedHandlers.contains(handler))) {
/*  74:158 */         return true;
/*  75:    */       }
/*  76:160 */       if (this.mappedHandlerClasses != null) {
/*  77:161 */         for (Class handlerClass : this.mappedHandlerClasses) {
/*  78:162 */           if (handlerClass.isInstance(handler)) {
/*  79:163 */             return true;
/*  80:    */           }
/*  81:    */         }
/*  82:    */       }
/*  83:    */     }
/*  84:169 */     return (this.mappedHandlers == null) && (this.mappedHandlerClasses == null);
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected void logException(Exception ex, HttpServletRequest request)
/*  88:    */   {
/*  89:184 */     if ((this.warnLogger != null) && (this.warnLogger.isWarnEnabled())) {
/*  90:185 */       this.warnLogger.warn(buildLogMessage(ex, request), ex);
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected String buildLogMessage(Exception ex, HttpServletRequest request)
/*  95:    */   {
/*  96:196 */     return "Handler execution resulted in exception";
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void prepareResponse(Exception ex, HttpServletResponse response)
/* 100:    */   {
/* 101:209 */     if (this.preventResponseCaching) {
/* 102:210 */       preventCaching(response);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected void preventCaching(HttpServletResponse response)
/* 107:    */   {
/* 108:220 */     response.setHeader("Pragma", "no-cache");
/* 109:221 */     response.setDateHeader("Expires", 1L);
/* 110:222 */     response.setHeader("Cache-Control", "no-cache");
/* 111:223 */     response.addHeader("Cache-Control", "no-store");
/* 112:    */   }
/* 113:    */   
/* 114:    */   protected abstract ModelAndView doResolveException(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Object paramObject, Exception paramException);
/* 115:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver
 * JD-Core Version:    0.7.0.1
 */