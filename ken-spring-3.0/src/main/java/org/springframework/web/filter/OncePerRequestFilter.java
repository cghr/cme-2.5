/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.FilterChain;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.ServletRequest;
/*   7:    */ import javax.servlet.ServletResponse;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ 
/*  11:    */ public abstract class OncePerRequestFilter
/*  12:    */   extends GenericFilterBean
/*  13:    */ {
/*  14:    */   public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
/*  15:    */   
/*  16:    */   public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
/*  17:    */     throws ServletException, IOException
/*  18:    */   {
/*  19: 61 */     if ((!(request instanceof HttpServletRequest)) || (!(response instanceof HttpServletResponse))) {
/*  20: 62 */       throw new ServletException("OncePerRequestFilter just supports HTTP requests");
/*  21:    */     }
/*  22: 64 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*  23: 65 */     HttpServletResponse httpResponse = (HttpServletResponse)response;
/*  24:    */     
/*  25: 67 */     String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
/*  26: 68 */     if ((request.getAttribute(alreadyFilteredAttributeName) != null) || (shouldNotFilter(httpRequest)))
/*  27:    */     {
/*  28: 70 */       filterChain.doFilter(request, response);
/*  29:    */     }
/*  30:    */     else
/*  31:    */     {
/*  32: 74 */       request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
/*  33:    */       try
/*  34:    */       {
/*  35: 76 */         doFilterInternal(httpRequest, httpResponse, filterChain);
/*  36:    */       }
/*  37:    */       finally
/*  38:    */       {
/*  39: 80 */         request.removeAttribute(alreadyFilteredAttributeName);
/*  40:    */       }
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected String getAlreadyFilteredAttributeName()
/*  45:    */   {
/*  46: 95 */     String name = getFilterName();
/*  47: 96 */     if (name == null) {
/*  48: 97 */       name = getClass().getName();
/*  49:    */     }
/*  50: 99 */     return name + ".FILTERED";
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected boolean shouldNotFilter(HttpServletRequest request)
/*  54:    */     throws ServletException
/*  55:    */   {
/*  56:111 */     return false;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected abstract void doFilterInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, FilterChain paramFilterChain)
/*  60:    */     throws ServletException, IOException;
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.OncePerRequestFilter
 * JD-Core Version:    0.7.0.1
 */