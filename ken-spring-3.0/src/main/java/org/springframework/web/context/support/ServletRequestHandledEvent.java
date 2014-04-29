/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ public class ServletRequestHandledEvent
/*   4:    */   extends RequestHandledEvent
/*   5:    */ {
/*   6:    */   private final String requestUrl;
/*   7:    */   private final String clientAddress;
/*   8:    */   private final String method;
/*   9:    */   private final String servletName;
/*  10:    */   
/*  11:    */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, String sessionId, String userName, long processingTimeMillis)
/*  12:    */   {
/*  13: 59 */     super(source, sessionId, userName, processingTimeMillis);
/*  14: 60 */     this.requestUrl = requestUrl;
/*  15: 61 */     this.clientAddress = clientAddress;
/*  16: 62 */     this.method = method;
/*  17: 63 */     this.servletName = servletName;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public ServletRequestHandledEvent(Object source, String requestUrl, String clientAddress, String method, String servletName, String sessionId, String userName, long processingTimeMillis, Throwable failureCause)
/*  21:    */   {
/*  22: 83 */     super(source, sessionId, userName, processingTimeMillis, failureCause);
/*  23: 84 */     this.requestUrl = requestUrl;
/*  24: 85 */     this.clientAddress = clientAddress;
/*  25: 86 */     this.method = method;
/*  26: 87 */     this.servletName = servletName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public String getRequestUrl()
/*  30:    */   {
/*  31: 95 */     return this.requestUrl;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getClientAddress()
/*  35:    */   {
/*  36:102 */     return this.clientAddress;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getMethod()
/*  40:    */   {
/*  41:109 */     return this.method;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getServletName()
/*  45:    */   {
/*  46:116 */     return this.servletName;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getShortDescription()
/*  50:    */   {
/*  51:122 */     StringBuilder sb = new StringBuilder();
/*  52:123 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/*  53:124 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/*  54:125 */     sb.append(super.getShortDescription());
/*  55:126 */     return sb.toString();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getDescription()
/*  59:    */   {
/*  60:131 */     StringBuilder sb = new StringBuilder();
/*  61:132 */     sb.append("url=[").append(getRequestUrl()).append("]; ");
/*  62:133 */     sb.append("client=[").append(getClientAddress()).append("]; ");
/*  63:134 */     sb.append("method=[").append(getMethod()).append("]; ");
/*  64:135 */     sb.append("servlet=[").append(getServletName()).append("]; ");
/*  65:136 */     sb.append(super.getDescription());
/*  66:137 */     return sb.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String toString()
/*  70:    */   {
/*  71:142 */     return "ServletRequestHandledEvent: " + getDescription();
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.ServletRequestHandledEvent
 * JD-Core Version:    0.7.0.1
 */