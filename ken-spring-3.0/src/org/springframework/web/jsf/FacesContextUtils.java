/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import javax.faces.context.ExternalContext;
/*   5:    */ import javax.faces.context.FacesContext;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.web.context.WebApplicationContext;
/*   8:    */ import org.springframework.web.util.WebUtils;
/*   9:    */ 
/*  10:    */ public abstract class FacesContextUtils
/*  11:    */ {
/*  12:    */   public static WebApplicationContext getWebApplicationContext(FacesContext fc)
/*  13:    */   {
/*  14: 50 */     Assert.notNull(fc, "FacesContext must not be null");
/*  15: 51 */     Object attr = fc.getExternalContext().getApplicationMap().get(
/*  16: 52 */       WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/*  17: 53 */     if (attr == null) {
/*  18: 54 */       return null;
/*  19:    */     }
/*  20: 56 */     if ((attr instanceof RuntimeException)) {
/*  21: 57 */       throw ((RuntimeException)attr);
/*  22:    */     }
/*  23: 59 */     if ((attr instanceof Error)) {
/*  24: 60 */       throw ((Error)attr);
/*  25:    */     }
/*  26: 62 */     if (!(attr instanceof WebApplicationContext)) {
/*  27: 63 */       throw new IllegalStateException("Root context attribute is not of type WebApplicationContext: " + attr);
/*  28:    */     }
/*  29: 65 */     return (WebApplicationContext)attr;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static WebApplicationContext getRequiredWebApplicationContext(FacesContext fc)
/*  33:    */     throws IllegalStateException
/*  34:    */   {
/*  35: 81 */     WebApplicationContext wac = getWebApplicationContext(fc);
/*  36: 82 */     if (wac == null) {
/*  37: 83 */       throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/*  38:    */     }
/*  39: 85 */     return wac;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static Object getSessionMutex(FacesContext fc)
/*  43:    */   {
/*  44:109 */     Assert.notNull(fc, "FacesContext must not be null");
/*  45:110 */     ExternalContext ec = fc.getExternalContext();
/*  46:111 */     Object mutex = ec.getSessionMap().get(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/*  47:112 */     if (mutex == null) {
/*  48:113 */       mutex = ec.getSession(true);
/*  49:    */     }
/*  50:115 */     return mutex;
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.FacesContextUtils
 * JD-Core Version:    0.7.0.1
 */