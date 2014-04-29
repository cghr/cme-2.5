/*  1:   */ package com.kentropy.csrfGuard;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.Filter;
/*  5:   */ import javax.servlet.FilterChain;
/*  6:   */ import javax.servlet.FilterConfig;
/*  7:   */ import javax.servlet.ServletException;
/*  8:   */ import javax.servlet.ServletRequest;
/*  9:   */ import javax.servlet.ServletResponse;
/* 10:   */ import javax.servlet.http.HttpServletRequest;
/* 11:   */ import javax.servlet.http.HttpServletResponse;
/* 12:   */ import javax.servlet.http.HttpSession;
/* 13:   */ import org.apache.log4j.Logger;
/* 14:   */ 
/* 15:   */ public class CsrfFilter
/* 16:   */   implements Filter
/* 17:   */ {
/* 18:29 */   Logger log = Logger.getLogger(CsrfFilter.class);
/* 19:   */   
/* 20:   */   public void destroy() {}
/* 21:   */   
/* 22:   */   public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
/* 23:   */     throws IOException, ServletException
/* 24:   */   {
/* 25:43 */     this.log.debug("Accessing Resource protected by CSRF Guard");
/* 26:44 */     HttpServletRequest request = (HttpServletRequest)req;
/* 27:45 */     HttpServletResponse response = (HttpServletResponse)resp;
/* 28:46 */     HttpSession session = request.getSession();
/* 29:47 */     String guard = req.getParameter("csrfGuard");
/* 30:48 */     this.log.debug("csrfGuard Token ==>" + guard);
/* 31:56 */     if (guard == null)
/* 32:   */     {
/* 33:58 */       this.log.info(":::: CSRF Attack.Request without Token ::::");
/* 34:59 */       response.sendError(403);
/* 35:60 */       return;
/* 36:   */     }
/* 37:66 */     if (!CSRF.isValid(guard, session))
/* 38:   */     {
/* 39:68 */       this.log.info(":::: CSRF Attack.Invalid Token ::::");
/* 40:69 */       response.sendError(403);
/* 41:70 */       return;
/* 42:   */     }
/* 43:73 */     CSRF.deleteToken(guard, session);
/* 44:74 */     chain.doFilter(req, resp);
/* 45:   */   }
/* 46:   */   
/* 47:   */   public void init(FilterConfig arg0)
/* 48:   */     throws ServletException
/* 49:   */   {}
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-csrfGuard\ken-csrfGuard.jar
 * Qualified Name:     com.kentropy.csrfGuard.CsrfFilter
 * JD-Core Version:    0.7.0.1
 */