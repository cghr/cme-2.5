/*   1:    */ package org.springframework.web.servlet.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.ResourceBundle;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpSession;
/*   8:    */ import javax.servlet.jsp.jstl.core.Config;
/*   9:    */ import javax.servlet.jsp.jstl.fmt.LocalizationContext;
/*  10:    */ import org.springframework.context.MessageSource;
/*  11:    */ import org.springframework.context.support.MessageSourceResourceBundle;
/*  12:    */ import org.springframework.context.support.ResourceBundleMessageSource;
/*  13:    */ 
/*  14:    */ public abstract class JstlUtils
/*  15:    */ {
/*  16:    */   public static MessageSource getJstlAwareMessageSource(ServletContext servletContext, MessageSource messageSource)
/*  17:    */   {
/*  18: 56 */     if (servletContext != null)
/*  19:    */     {
/*  20: 57 */       String jstlInitParam = servletContext.getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
/*  21: 58 */       if (jstlInitParam != null)
/*  22:    */       {
/*  23: 62 */         ResourceBundleMessageSource jstlBundleWrapper = new ResourceBundleMessageSource();
/*  24: 63 */         jstlBundleWrapper.setBasename(jstlInitParam);
/*  25: 64 */         jstlBundleWrapper.setParentMessageSource(messageSource);
/*  26: 65 */         return jstlBundleWrapper;
/*  27:    */       }
/*  28:    */     }
/*  29: 68 */     return messageSource;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static void exposeLocalizationContext(HttpServletRequest request, MessageSource messageSource)
/*  33:    */   {
/*  34: 81 */     Locale jstlLocale = RequestContextUtils.getLocale(request);
/*  35: 82 */     Config.set(request, "javax.servlet.jsp.jstl.fmt.locale", jstlLocale);
/*  36: 83 */     if (messageSource != null)
/*  37:    */     {
/*  38: 84 */       LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, request);
/*  39: 85 */       Config.set(request, "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void exposeLocalizationContext(RequestContext requestContext)
/*  44:    */   {
/*  45: 97 */     Config.set(requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.locale", requestContext.getLocale());
/*  46: 98 */     MessageSource messageSource = getJstlAwareMessageSource(
/*  47: 99 */       requestContext.getServletContext(), requestContext.getMessageSource());
/*  48:100 */     LocalizationContext jstlContext = new SpringLocalizationContext(messageSource, requestContext.getRequest());
/*  49:101 */     Config.set(requestContext.getRequest(), "javax.servlet.jsp.jstl.fmt.localizationContext", jstlContext);
/*  50:    */   }
/*  51:    */   
/*  52:    */   private static class SpringLocalizationContext
/*  53:    */     extends LocalizationContext
/*  54:    */   {
/*  55:    */     private final MessageSource messageSource;
/*  56:    */     private final HttpServletRequest request;
/*  57:    */     
/*  58:    */     public SpringLocalizationContext(MessageSource messageSource, HttpServletRequest request)
/*  59:    */     {
/*  60:116 */       this.messageSource = messageSource;
/*  61:117 */       this.request = request;
/*  62:    */     }
/*  63:    */     
/*  64:    */     public ResourceBundle getResourceBundle()
/*  65:    */     {
/*  66:122 */       HttpSession session = this.request.getSession(false);
/*  67:123 */       if (session != null)
/*  68:    */       {
/*  69:124 */         Object lcObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.localizationContext");
/*  70:125 */         if ((lcObject instanceof LocalizationContext))
/*  71:    */         {
/*  72:126 */           ResourceBundle lcBundle = ((LocalizationContext)lcObject).getResourceBundle();
/*  73:127 */           return new MessageSourceResourceBundle(this.messageSource, getLocale(), lcBundle);
/*  74:    */         }
/*  75:    */       }
/*  76:130 */       return new MessageSourceResourceBundle(this.messageSource, getLocale());
/*  77:    */     }
/*  78:    */     
/*  79:    */     public Locale getLocale()
/*  80:    */     {
/*  81:135 */       HttpSession session = this.request.getSession(false);
/*  82:136 */       if (session != null)
/*  83:    */       {
/*  84:137 */         Object localeObject = Config.get(session, "javax.servlet.jsp.jstl.fmt.locale");
/*  85:138 */         if ((localeObject instanceof Locale)) {
/*  86:139 */           return (Locale)localeObject;
/*  87:    */         }
/*  88:    */       }
/*  89:142 */       return RequestContextUtils.getLocale(this.request);
/*  90:    */     }
/*  91:    */   }
/*  92:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.support.JstlUtils
 * JD-Core Version:    0.7.0.1
 */