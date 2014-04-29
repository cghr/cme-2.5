/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.context.ApplicationContext;
/*   6:    */ import org.springframework.context.support.ApplicationObjectSupport;
/*   7:    */ import org.springframework.web.context.ServletContextAware;
/*   8:    */ import org.springframework.web.context.WebApplicationContext;
/*   9:    */ import org.springframework.web.util.WebUtils;
/*  10:    */ 
/*  11:    */ public abstract class WebApplicationObjectSupport
/*  12:    */   extends ApplicationObjectSupport
/*  13:    */   implements ServletContextAware
/*  14:    */ {
/*  15:    */   private ServletContext servletContext;
/*  16:    */   
/*  17:    */   public final void setServletContext(ServletContext servletContext)
/*  18:    */   {
/*  19: 44 */     if (servletContext != this.servletContext)
/*  20:    */     {
/*  21: 45 */       this.servletContext = servletContext;
/*  22: 46 */       if (servletContext != null) {
/*  23: 47 */         initServletContext(servletContext);
/*  24:    */       }
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected boolean isContextRequired()
/*  29:    */   {
/*  30: 63 */     return true;
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected void initApplicationContext(ApplicationContext context)
/*  34:    */   {
/*  35: 72 */     super.initApplicationContext(context);
/*  36: 73 */     if ((this.servletContext == null) && ((context instanceof WebApplicationContext)))
/*  37:    */     {
/*  38: 74 */       this.servletContext = ((WebApplicationContext)context).getServletContext();
/*  39: 75 */       if (this.servletContext != null) {
/*  40: 76 */         initServletContext(this.servletContext);
/*  41:    */       }
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void initServletContext(ServletContext servletContext) {}
/*  46:    */   
/*  47:    */   protected final WebApplicationContext getWebApplicationContext()
/*  48:    */     throws IllegalStateException
/*  49:    */   {
/*  50:103 */     ApplicationContext ctx = getApplicationContext();
/*  51:104 */     if ((ctx instanceof WebApplicationContext)) {
/*  52:105 */       return (WebApplicationContext)getApplicationContext();
/*  53:    */     }
/*  54:107 */     if (isContextRequired()) {
/*  55:108 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + 
/*  56:109 */         "] does not run in a WebApplicationContext but in: " + ctx);
/*  57:    */     }
/*  58:112 */     return null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected final ServletContext getServletContext()
/*  62:    */     throws IllegalStateException
/*  63:    */   {
/*  64:121 */     if (this.servletContext != null) {
/*  65:122 */       return this.servletContext;
/*  66:    */     }
/*  67:124 */     ServletContext servletContext = getWebApplicationContext().getServletContext();
/*  68:125 */     if ((servletContext == null) && (isContextRequired())) {
/*  69:126 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + 
/*  70:127 */         "] does not run within a ServletContext. Make sure the object is fully configured!");
/*  71:    */     }
/*  72:129 */     return servletContext;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected final File getTempDir()
/*  76:    */     throws IllegalStateException
/*  77:    */   {
/*  78:140 */     return WebUtils.getTempDir(getServletContext());
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.WebApplicationObjectSupport
 * JD-Core Version:    0.7.0.1
 */