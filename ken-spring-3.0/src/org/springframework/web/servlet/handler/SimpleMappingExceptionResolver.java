/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.Enumeration;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Properties;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.web.servlet.ModelAndView;
/*  12:    */ import org.springframework.web.util.WebUtils;
/*  13:    */ 
/*  14:    */ public class SimpleMappingExceptionResolver
/*  15:    */   extends AbstractHandlerExceptionResolver
/*  16:    */ {
/*  17:    */   public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
/*  18:    */   private Properties exceptionMappings;
/*  19:    */   private String defaultErrorView;
/*  20:    */   private Integer defaultStatusCode;
/*  21: 53 */   private Map<String, Integer> statusCodes = new HashMap();
/*  22: 55 */   private String exceptionAttribute = "exception";
/*  23:    */   
/*  24:    */   public void setExceptionMappings(Properties mappings)
/*  25:    */   {
/*  26: 73 */     this.exceptionMappings = mappings;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setDefaultErrorView(String defaultErrorView)
/*  30:    */   {
/*  31: 81 */     this.defaultErrorView = defaultErrorView;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setStatusCodes(Properties statusCodes)
/*  35:    */   {
/*  36: 93 */     for (Enumeration<?> enumeration = statusCodes.propertyNames(); enumeration.hasMoreElements();)
/*  37:    */     {
/*  38: 94 */       String viewName = (String)enumeration.nextElement();
/*  39: 95 */       Integer statusCode = new Integer(statusCodes.getProperty(viewName));
/*  40: 96 */       this.statusCodes.put(viewName, statusCode);
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Map<String, Integer> getStatusCodes()
/*  45:    */   {
/*  46:105 */     return Collections.unmodifiableMap(this.statusCodes);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setDefaultStatusCode(int defaultStatusCode)
/*  50:    */   {
/*  51:120 */     this.defaultStatusCode = Integer.valueOf(defaultStatusCode);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setExceptionAttribute(String exceptionAttribute)
/*  55:    */   {
/*  56:129 */     this.exceptionAttribute = exceptionAttribute;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*  60:    */   {
/*  61:151 */     String viewName = determineViewName(ex, request);
/*  62:152 */     if (viewName != null)
/*  63:    */     {
/*  64:155 */       Integer statusCode = determineStatusCode(request, viewName);
/*  65:156 */       if (statusCode != null) {
/*  66:157 */         applyStatusCodeIfPossible(request, response, statusCode.intValue());
/*  67:    */       }
/*  68:159 */       return getModelAndView(viewName, ex, request);
/*  69:    */     }
/*  70:162 */     return null;
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected String determineViewName(Exception ex, HttpServletRequest request)
/*  74:    */   {
/*  75:174 */     String viewName = null;
/*  76:176 */     if (this.exceptionMappings != null) {
/*  77:177 */       viewName = findMatchingViewName(this.exceptionMappings, ex);
/*  78:    */     }
/*  79:180 */     if ((viewName == null) && (this.defaultErrorView != null))
/*  80:    */     {
/*  81:181 */       if (this.logger.isDebugEnabled()) {
/*  82:182 */         this.logger.debug("Resolving to default view '" + this.defaultErrorView + "' for exception of type [" + 
/*  83:183 */           ex.getClass().getName() + "]");
/*  84:    */       }
/*  85:185 */       viewName = this.defaultErrorView;
/*  86:    */     }
/*  87:187 */     return viewName;
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected String findMatchingViewName(Properties exceptionMappings, Exception ex)
/*  91:    */   {
/*  92:198 */     String viewName = null;
/*  93:199 */     String dominantMapping = null;
/*  94:200 */     int deepest = 2147483647;
/*  95:201 */     for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements();)
/*  96:    */     {
/*  97:202 */       String exceptionMapping = (String)names.nextElement();
/*  98:203 */       int depth = getDepth(exceptionMapping, ex);
/*  99:204 */       if ((depth >= 0) && (depth < deepest))
/* 100:    */       {
/* 101:205 */         deepest = depth;
/* 102:206 */         dominantMapping = exceptionMapping;
/* 103:207 */         viewName = exceptionMappings.getProperty(exceptionMapping);
/* 104:    */       }
/* 105:    */     }
/* 106:210 */     if ((viewName != null) && (this.logger.isDebugEnabled())) {
/* 107:211 */       this.logger.debug("Resolving to view '" + viewName + "' for exception of type [" + ex.getClass().getName() + 
/* 108:212 */         "], based on exception mapping [" + dominantMapping + "]");
/* 109:    */     }
/* 110:214 */     return viewName;
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected int getDepth(String exceptionMapping, Exception ex)
/* 114:    */   {
/* 115:223 */     return getDepth(exceptionMapping, ex.getClass(), 0);
/* 116:    */   }
/* 117:    */   
/* 118:    */   private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth)
/* 119:    */   {
/* 120:227 */     if (exceptionClass.getName().contains(exceptionMapping)) {
/* 121:229 */       return depth;
/* 122:    */     }
/* 123:232 */     if (exceptionClass.equals(Throwable.class)) {
/* 124:233 */       return -1;
/* 125:    */     }
/* 126:235 */     return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected Integer determineStatusCode(HttpServletRequest request, String viewName)
/* 130:    */   {
/* 131:252 */     if (this.statusCodes.containsKey(viewName)) {
/* 132:253 */       return (Integer)this.statusCodes.get(viewName);
/* 133:    */     }
/* 134:255 */     return this.defaultStatusCode;
/* 135:    */   }
/* 136:    */   
/* 137:    */   protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode)
/* 138:    */   {
/* 139:269 */     if (!WebUtils.isIncludeRequest(request))
/* 140:    */     {
/* 141:270 */       if (this.logger.isDebugEnabled()) {
/* 142:271 */         this.logger.debug("Applying HTTP status code " + statusCode);
/* 143:    */       }
/* 144:273 */       response.setStatus(statusCode);
/* 145:274 */       request.setAttribute("javax.servlet.error.status_code", Integer.valueOf(statusCode));
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected ModelAndView getModelAndView(String viewName, Exception ex, HttpServletRequest request)
/* 150:    */   {
/* 151:287 */     return getModelAndView(viewName, ex);
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected ModelAndView getModelAndView(String viewName, Exception ex)
/* 155:    */   {
/* 156:300 */     ModelAndView mv = new ModelAndView(viewName);
/* 157:301 */     if (this.exceptionAttribute != null)
/* 158:    */     {
/* 159:302 */       if (this.logger.isDebugEnabled()) {
/* 160:303 */         this.logger.debug("Exposing Exception as model attribute '" + this.exceptionAttribute + "'");
/* 161:    */       }
/* 162:305 */       mv.addObject(this.exceptionAttribute, ex);
/* 163:    */     }
/* 164:307 */     return mv;
/* 165:    */   }
/* 166:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
 * JD-Core Version:    0.7.0.1
 */