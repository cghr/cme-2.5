/*   1:    */ package org.springframework.web.context.request;
/*   2:    */ 
/*   3:    */ import javax.faces.context.FacesContext;
/*   4:    */ import org.springframework.core.NamedInheritableThreadLocal;
/*   5:    */ import org.springframework.core.NamedThreadLocal;
/*   6:    */ import org.springframework.util.ClassUtils;
/*   7:    */ 
/*   8:    */ public abstract class RequestContextHolder
/*   9:    */ {
/*  10: 49 */   private static final boolean jsfPresent = ClassUtils.isPresent("javax.faces.context.FacesContext", RequestContextHolder.class.getClassLoader());
/*  11: 52 */   private static final ThreadLocal<RequestAttributes> requestAttributesHolder = new NamedThreadLocal("Request attributes");
/*  12: 55 */   private static final ThreadLocal<RequestAttributes> inheritableRequestAttributesHolder = new NamedInheritableThreadLocal("Request context");
/*  13:    */   
/*  14:    */   public static void resetRequestAttributes()
/*  15:    */   {
/*  16: 62 */     requestAttributesHolder.remove();
/*  17: 63 */     inheritableRequestAttributesHolder.remove();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static void setRequestAttributes(RequestAttributes attributes)
/*  21:    */   {
/*  22: 73 */     setRequestAttributes(attributes, false);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static void setRequestAttributes(RequestAttributes attributes, boolean inheritable)
/*  26:    */   {
/*  27: 84 */     if (attributes == null)
/*  28:    */     {
/*  29: 85 */       resetRequestAttributes();
/*  30:    */     }
/*  31: 88 */     else if (inheritable)
/*  32:    */     {
/*  33: 89 */       inheritableRequestAttributesHolder.set(attributes);
/*  34: 90 */       requestAttributesHolder.remove();
/*  35:    */     }
/*  36:    */     else
/*  37:    */     {
/*  38: 93 */       requestAttributesHolder.set(attributes);
/*  39: 94 */       inheritableRequestAttributesHolder.remove();
/*  40:    */     }
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static RequestAttributes getRequestAttributes()
/*  44:    */   {
/*  45:105 */     RequestAttributes attributes = (RequestAttributes)requestAttributesHolder.get();
/*  46:106 */     if (attributes == null) {
/*  47:107 */       attributes = (RequestAttributes)inheritableRequestAttributesHolder.get();
/*  48:    */     }
/*  49:109 */     return attributes;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static RequestAttributes currentRequestAttributes()
/*  53:    */     throws IllegalStateException
/*  54:    */   {
/*  55:125 */     RequestAttributes attributes = getRequestAttributes();
/*  56:126 */     if (attributes == null)
/*  57:    */     {
/*  58:127 */       if (jsfPresent) {
/*  59:128 */         attributes = FacesRequestAttributesFactory.getFacesRequestAttributes();
/*  60:    */       }
/*  61:130 */       if (attributes == null) {
/*  62:131 */         throw new IllegalStateException("No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread? If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet/DispatcherPortlet: In this case, use RequestContextListener or RequestContextFilter to expose the current request.");
/*  63:    */       }
/*  64:    */     }
/*  65:139 */     return attributes;
/*  66:    */   }
/*  67:    */   
/*  68:    */   private static class FacesRequestAttributesFactory
/*  69:    */   {
/*  70:    */     public static RequestAttributes getFacesRequestAttributes()
/*  71:    */     {
/*  72:149 */       FacesContext facesContext = FacesContext.getCurrentInstance();
/*  73:150 */       return facesContext != null ? new FacesRequestAttributes(facesContext) : null;
/*  74:    */     }
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.request.RequestContextHolder
 * JD-Core Version:    0.7.0.1
 */