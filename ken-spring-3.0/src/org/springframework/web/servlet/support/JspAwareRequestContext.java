/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import javax.servlet.jsp.jstl.core.Config;
/*   9:    */ 
/*  10:    */ public class JspAwareRequestContext
/*  11:    */   extends RequestContext
/*  12:    */ {
/*  13:    */   private PageContext pageContext;
/*  14:    */   
/*  15:    */   public JspAwareRequestContext(PageContext pageContext)
/*  16:    */   {
/*  17: 48 */     initContext(pageContext, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public JspAwareRequestContext(PageContext pageContext, Map<String, Object> model)
/*  21:    */   {
/*  22: 59 */     initContext(pageContext, model);
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected void initContext(PageContext pageContext, Map<String, Object> model)
/*  26:    */   {
/*  27: 70 */     if (!(pageContext.getRequest() instanceof HttpServletRequest)) {
/*  28: 71 */       throw new IllegalArgumentException("RequestContext only supports HTTP requests");
/*  29:    */     }
/*  30: 73 */     this.pageContext = pageContext;
/*  31: 74 */     initContext((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse(), 
/*  32: 75 */       pageContext.getServletContext(), model);
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected final PageContext getPageContext()
/*  36:    */   {
/*  37: 84 */     return this.pageContext;
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected Locale getFallbackLocale()
/*  41:    */   {
/*  42: 94 */     if (jstlPresent)
/*  43:    */     {
/*  44: 95 */       Locale locale = JstlPageLocaleResolver.getJstlLocale(getPageContext());
/*  45: 96 */       if (locale != null) {
/*  46: 97 */         return locale;
/*  47:    */       }
/*  48:    */     }
/*  49:100 */     return getRequest().getLocale();
/*  50:    */   }
/*  51:    */   
/*  52:    */   private static class JstlPageLocaleResolver
/*  53:    */   {
/*  54:    */     public static Locale getJstlLocale(PageContext pageContext)
/*  55:    */     {
/*  56:111 */       Object localeObject = Config.find(pageContext, "javax.servlet.jsp.jstl.fmt.locale");
/*  57:112 */       return (localeObject instanceof Locale) ? (Locale)localeObject : null;
/*  58:    */     }
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.JspAwareRequestContext
 * JD-Core Version:    0.7.0.1
 */