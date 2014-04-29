/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletContext;
/*   4:    */ import javax.servlet.http.HttpServletRequest;
/*   5:    */ import org.springframework.context.MessageSource;
/*   6:    */ import org.springframework.web.servlet.support.JstlUtils;
/*   7:    */ import org.springframework.web.servlet.support.RequestContext;
/*   8:    */ 
/*   9:    */ public class JstlView
/*  10:    */   extends InternalResourceView
/*  11:    */ {
/*  12:    */   private MessageSource messageSource;
/*  13:    */   
/*  14:    */   public JstlView() {}
/*  15:    */   
/*  16:    */   public JstlView(String url)
/*  17:    */   {
/*  18: 94 */     super(url);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public JstlView(String url, MessageSource messageSource)
/*  22:    */   {
/*  23:106 */     this(url);
/*  24:107 */     this.messageSource = messageSource;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected void initServletContext(ServletContext servletContext)
/*  28:    */   {
/*  29:119 */     if (this.messageSource != null) {
/*  30:120 */       this.messageSource = JstlUtils.getJstlAwareMessageSource(servletContext, this.messageSource);
/*  31:    */     }
/*  32:122 */     super.initServletContext(servletContext);
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected void exposeHelpers(HttpServletRequest request)
/*  36:    */     throws Exception
/*  37:    */   {
/*  38:131 */     if (this.messageSource != null) {
/*  39:132 */       JstlUtils.exposeLocalizationContext(request, this.messageSource);
/*  40:    */     } else {
/*  41:135 */       JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
/*  42:    */     }
/*  43:    */   }
/*  44:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.JstlView
 * JD-Core Version:    0.7.0.1
 */