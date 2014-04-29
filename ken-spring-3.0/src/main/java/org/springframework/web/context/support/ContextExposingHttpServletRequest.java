/*  1:   */ package org.springframework.web.context.support;
/*  2:   */ 
/*  3:   */ import java.util.HashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletRequestWrapper;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ import org.springframework.web.context.WebApplicationContext;
/*  9:   */ 
/* 10:   */ public class ContextExposingHttpServletRequest
/* 11:   */   extends HttpServletRequestWrapper
/* 12:   */ {
/* 13:   */   private final WebApplicationContext webApplicationContext;
/* 14:   */   private final Set<String> exposedContextBeanNames;
/* 15:   */   private Set<String> explicitAttributes;
/* 16:   */   
/* 17:   */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context)
/* 18:   */   {
/* 19:51 */     this(originalRequest, context, null);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ContextExposingHttpServletRequest(HttpServletRequest originalRequest, WebApplicationContext context, Set<String> exposedContextBeanNames)
/* 23:   */   {
/* 24:65 */     super(originalRequest);
/* 25:66 */     Assert.notNull(context, "WebApplicationContext must not be null");
/* 26:67 */     this.webApplicationContext = context;
/* 27:68 */     this.exposedContextBeanNames = exposedContextBeanNames;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public final WebApplicationContext getWebApplicationContext()
/* 31:   */   {
/* 32:76 */     return this.webApplicationContext;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Object getAttribute(String name)
/* 36:   */   {
/* 37:82 */     if (((this.explicitAttributes == null) || (!this.explicitAttributes.contains(name))) && 
/* 38:83 */       ((this.exposedContextBeanNames == null) || (this.exposedContextBeanNames.contains(name))) && 
/* 39:84 */       (this.webApplicationContext.containsBean(name))) {
/* 40:85 */       return this.webApplicationContext.getBean(name);
/* 41:   */     }
/* 42:88 */     return super.getAttribute(name);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void setAttribute(String name, Object value)
/* 46:   */   {
/* 47:94 */     super.setAttribute(name, value);
/* 48:95 */     if (this.explicitAttributes == null) {
/* 49:96 */       this.explicitAttributes = new HashSet(8);
/* 50:   */     }
/* 51:98 */     this.explicitAttributes.add(name);
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ContextExposingHttpServletRequest
 * JD-Core Version:    0.7.0.1
 */