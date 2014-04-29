/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.util.Locale;
/*  5:   */ import javax.servlet.FilterChain;
/*  6:   */ import javax.servlet.ServletException;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletRequestWrapper;
/*  9:   */ import javax.servlet.http.HttpServletResponse;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ import org.springframework.util.StringUtils;
/* 12:   */ 
/* 13:   */ public class HiddenHttpMethodFilter
/* 14:   */   extends OncePerRequestFilter
/* 15:   */ {
/* 16:   */   public static final String DEFAULT_METHOD_PARAM = "_method";
/* 17:54 */   private String methodParam = "_method";
/* 18:   */   
/* 19:   */   public void setMethodParam(String methodParam)
/* 20:   */   {
/* 21:62 */     Assert.hasText(methodParam, "'methodParam' must not be empty");
/* 22:63 */     this.methodParam = methodParam;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/* 26:   */     throws ServletException, IOException
/* 27:   */   {
/* 28:70 */     String paramValue = request.getParameter(this.methodParam);
/* 29:71 */     if (("POST".equals(request.getMethod())) && (StringUtils.hasLength(paramValue)))
/* 30:   */     {
/* 31:72 */       String method = paramValue.toUpperCase(Locale.ENGLISH);
/* 32:73 */       HttpServletRequest wrapper = new HttpMethodRequestWrapper(request, method);
/* 33:74 */       filterChain.doFilter(wrapper, response);
/* 34:   */     }
/* 35:   */     else
/* 36:   */     {
/* 37:77 */       filterChain.doFilter(request, response);
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   private static class HttpMethodRequestWrapper
/* 42:   */     extends HttpServletRequestWrapper
/* 43:   */   {
/* 44:   */     private final String method;
/* 45:   */     
/* 46:   */     public HttpMethodRequestWrapper(HttpServletRequest request, String method)
/* 47:   */     {
/* 48:91 */       super();
/* 49:92 */       this.method = method;
/* 50:   */     }
/* 51:   */     
/* 52:   */     public String getMethod()
/* 53:   */     {
/* 54:97 */       return this.method;
/* 55:   */     }
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.HiddenHttpMethodFilter
 * JD-Core Version:    0.7.0.1
 */