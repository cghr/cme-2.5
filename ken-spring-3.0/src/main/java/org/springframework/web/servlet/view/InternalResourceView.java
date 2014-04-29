/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashSet;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import javax.servlet.RequestDispatcher;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import javax.servlet.ServletException;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import javax.servlet.http.HttpServletResponse;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ import org.springframework.web.context.support.ContextExposingHttpServletRequest;
/*  16:    */ import org.springframework.web.util.WebUtils;
/*  17:    */ 
/*  18:    */ public class InternalResourceView
/*  19:    */   extends AbstractUrlBasedView
/*  20:    */ {
/*  21: 70 */   private boolean alwaysInclude = false;
/*  22:    */   private volatile Boolean exposeForwardAttributes;
/*  23: 74 */   private boolean exposeContextBeansAsAttributes = false;
/*  24:    */   private Set<String> exposedContextBeanNames;
/*  25: 78 */   private boolean preventDispatchLoop = false;
/*  26:    */   
/*  27:    */   public InternalResourceView() {}
/*  28:    */   
/*  29:    */   public InternalResourceView(String url)
/*  30:    */   {
/*  31: 95 */     super(url);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public InternalResourceView(String url, boolean alwaysInclude)
/*  35:    */   {
/*  36:104 */     super(url);
/*  37:105 */     this.alwaysInclude = alwaysInclude;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setAlwaysInclude(boolean alwaysInclude)
/*  41:    */   {
/*  42:118 */     this.alwaysInclude = alwaysInclude;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setExposeForwardAttributes(boolean exposeForwardAttributes)
/*  46:    */   {
/*  47:130 */     this.exposeForwardAttributes = Boolean.valueOf(exposeForwardAttributes);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes)
/*  51:    */   {
/*  52:148 */     this.exposeContextBeansAsAttributes = exposeContextBeansAsAttributes;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setExposedContextBeanNames(String[] exposedContextBeanNames)
/*  56:    */   {
/*  57:160 */     this.exposedContextBeanNames = new HashSet((Collection)Arrays.asList(exposedContextBeanNames));
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setPreventDispatchLoop(boolean preventDispatchLoop)
/*  61:    */   {
/*  62:171 */     this.preventDispatchLoop = preventDispatchLoop;
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected boolean isContextRequired()
/*  66:    */   {
/*  67:179 */     return false;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected void initServletContext(ServletContext sc)
/*  71:    */   {
/*  72:190 */     if ((this.exposeForwardAttributes == null) && (sc.getMajorVersion() == 2) && (sc.getMinorVersion() < 5)) {
/*  73:191 */       this.exposeForwardAttributes = Boolean.TRUE;
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80:205 */     HttpServletRequest requestToExpose = getRequestToExpose(request);
/*  81:    */     
/*  82:    */ 
/*  83:208 */     exposeModelAsRequestAttributes(model, requestToExpose);
/*  84:    */     
/*  85:    */ 
/*  86:211 */     exposeHelpers(requestToExpose);
/*  87:    */     
/*  88:    */ 
/*  89:214 */     String dispatcherPath = prepareForRendering(requestToExpose, response);
/*  90:    */     
/*  91:    */ 
/*  92:217 */     RequestDispatcher rd = getRequestDispatcher(requestToExpose, dispatcherPath);
/*  93:218 */     if (rd == null) {
/*  94:219 */       throw new ServletException("Could not get RequestDispatcher for [" + getUrl() + 
/*  95:220 */         "]: Check that the corresponding file exists within your web application archive!");
/*  96:    */     }
/*  97:224 */     if (useInclude(requestToExpose, response))
/*  98:    */     {
/*  99:225 */       response.setContentType(getContentType());
/* 100:226 */       if (this.logger.isDebugEnabled()) {
/* 101:227 */         this.logger.debug("Including resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
/* 102:    */       }
/* 103:229 */       rd.include(requestToExpose, response);
/* 104:    */     }
/* 105:    */     else
/* 106:    */     {
/* 107:234 */       exposeForwardRequestAttributes(requestToExpose);
/* 108:235 */       if (this.logger.isDebugEnabled()) {
/* 109:236 */         this.logger.debug("Forwarding to resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
/* 110:    */       }
/* 111:238 */       rd.forward(requestToExpose, response);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest)
/* 116:    */   {
/* 117:252 */     if ((this.exposeContextBeansAsAttributes) || (this.exposedContextBeanNames != null)) {
/* 118:253 */       return new ContextExposingHttpServletRequest(
/* 119:254 */         originalRequest, getWebApplicationContext(), this.exposedContextBeanNames);
/* 120:    */     }
/* 121:256 */     return originalRequest;
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected void exposeHelpers(HttpServletRequest request)
/* 125:    */     throws Exception
/* 126:    */   {}
/* 127:    */   
/* 128:    */   protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response)
/* 129:    */     throws Exception
/* 130:    */   {
/* 131:288 */     String path = getUrl();
/* 132:289 */     if (this.preventDispatchLoop)
/* 133:    */     {
/* 134:290 */       String uri = request.getRequestURI();
/* 135:291 */       if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtils.applyRelativePath(uri, path))) {
/* 136:292 */         throw new ServletException("Circular view path [" + path + "]: would dispatch back " + 
/* 137:293 */           "to the current handler URL [" + uri + "] again. Check your ViewResolver setup! " + 
/* 138:294 */           "(Hint: This may be the result of an unspecified view, due to default view name generation.)");
/* 139:    */       }
/* 140:    */     }
/* 141:297 */     return path;
/* 142:    */   }
/* 143:    */   
/* 144:    */   protected RequestDispatcher getRequestDispatcher(HttpServletRequest request, String path)
/* 145:    */   {
/* 146:310 */     return request.getRequestDispatcher(path);
/* 147:    */   }
/* 148:    */   
/* 149:    */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response)
/* 150:    */   {
/* 151:328 */     return (this.alwaysInclude) || (WebUtils.isIncludeRequest(request)) || (response.isCommitted());
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected void exposeForwardRequestAttributes(HttpServletRequest request)
/* 155:    */   {
/* 156:344 */     if ((this.exposeForwardAttributes != null) && (this.exposeForwardAttributes.booleanValue())) {
/* 157:    */       try
/* 158:    */       {
/* 159:346 */         WebUtils.exposeForwardRequestAttributes(request);
/* 160:    */       }
/* 161:    */       catch (Exception localException)
/* 162:    */       {
/* 163:350 */         this.exposeForwardAttributes = Boolean.FALSE;
/* 164:    */       }
/* 165:    */     }
/* 166:    */   }
/* 167:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.InternalResourceView
 * JD-Core Version:    0.7.0.1
 */