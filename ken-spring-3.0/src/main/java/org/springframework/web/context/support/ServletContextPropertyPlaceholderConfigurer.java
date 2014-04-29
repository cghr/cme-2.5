/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/*   6:    */ import org.springframework.web.context.ServletContextAware;
/*   7:    */ 
/*   8:    */ @Deprecated
/*   9:    */ public class ServletContextPropertyPlaceholderConfigurer
/*  10:    */   extends PropertyPlaceholderConfigurer
/*  11:    */   implements ServletContextAware
/*  12:    */ {
/*  13: 68 */   private boolean contextOverride = false;
/*  14: 70 */   private boolean searchContextAttributes = false;
/*  15:    */   private ServletContext servletContext;
/*  16:    */   
/*  17:    */   public void setContextOverride(boolean contextOverride)
/*  18:    */   {
/*  19: 86 */     this.contextOverride = contextOverride;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setSearchContextAttributes(boolean searchContextAttributes)
/*  23:    */   {
/*  24:102 */     this.searchContextAttributes = searchContextAttributes;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setServletContext(ServletContext servletContext)
/*  28:    */   {
/*  29:113 */     this.servletContext = servletContext;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected String resolvePlaceholder(String placeholder, Properties props)
/*  33:    */   {
/*  34:119 */     String value = null;
/*  35:120 */     if ((this.contextOverride) && (this.servletContext != null)) {
/*  36:121 */       value = resolvePlaceholder(placeholder, this.servletContext, this.searchContextAttributes);
/*  37:    */     }
/*  38:123 */     if (value == null) {
/*  39:124 */       value = super.resolvePlaceholder(placeholder, props);
/*  40:    */     }
/*  41:126 */     if ((value == null) && (this.servletContext != null)) {
/*  42:127 */       value = resolvePlaceholder(placeholder, this.servletContext, this.searchContextAttributes);
/*  43:    */     }
/*  44:129 */     return value;
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected String resolvePlaceholder(String placeholder, ServletContext servletContext, boolean searchContextAttributes)
/*  48:    */   {
/*  49:150 */     String value = null;
/*  50:151 */     if (searchContextAttributes)
/*  51:    */     {
/*  52:152 */       Object attrValue = servletContext.getAttribute(placeholder);
/*  53:153 */       if (attrValue != null) {
/*  54:154 */         value = attrValue.toString();
/*  55:    */       }
/*  56:    */     }
/*  57:157 */     if (value == null) {
/*  58:158 */       value = servletContext.getInitParameter(placeholder);
/*  59:    */     }
/*  60:160 */     return value;
/*  61:    */   }
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletContextPropertyPlaceholderConfigurer
 * JD-Core Version:    0.7.0.1
 */