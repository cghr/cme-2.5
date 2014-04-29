/*  1:   */ package org.springframework.web.servlet.mvc.multiaction;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import org.springframework.core.style.StylerUtils;
/*  7:   */ import org.springframework.web.util.UrlPathHelper;
/*  8:   */ 
/*  9:   */ public class NoSuchRequestHandlingMethodException
/* 10:   */   extends ServletException
/* 11:   */ {
/* 12:   */   private String methodName;
/* 13:   */   
/* 14:   */   public NoSuchRequestHandlingMethodException(HttpServletRequest request)
/* 15:   */   {
/* 16:45 */     this(new UrlPathHelper().getRequestUri(request), request.getMethod(), request.getParameterMap());
/* 17:   */   }
/* 18:   */   
/* 19:   */   public NoSuchRequestHandlingMethodException(String urlPath, String method, Map parameterMap)
/* 20:   */   {
/* 21:56 */     super("No matching handler method found for servlet request: path '" + urlPath + "', method '" + method + "', parameters " + StylerUtils.style(parameterMap));
/* 22:   */   }
/* 23:   */   
/* 24:   */   public NoSuchRequestHandlingMethodException(String methodName, Class controllerClass)
/* 25:   */   {
/* 26:66 */     super("No request handling method with name '" + methodName + "' in class [" + controllerClass.getName() + "]");
/* 27:67 */     this.methodName = methodName;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String getMethodName()
/* 31:   */   {
/* 32:75 */     return this.methodName;
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException
 * JD-Core Version:    0.7.0.1
 */