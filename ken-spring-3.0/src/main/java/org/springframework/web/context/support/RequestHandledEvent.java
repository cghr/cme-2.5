/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import org.springframework.context.ApplicationEvent;
/*   4:    */ 
/*   5:    */ public class RequestHandledEvent
/*   6:    */   extends ApplicationEvent
/*   7:    */ {
/*   8:    */   private String sessionId;
/*   9:    */   private String userName;
/*  10:    */   private final long processingTimeMillis;
/*  11:    */   private Throwable failureCause;
/*  12:    */   
/*  13:    */   public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis)
/*  14:    */   {
/*  15: 60 */     super(source);
/*  16: 61 */     this.sessionId = sessionId;
/*  17: 62 */     this.userName = userName;
/*  18: 63 */     this.processingTimeMillis = processingTimeMillis;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis, Throwable failureCause)
/*  22:    */   {
/*  23: 78 */     this(source, sessionId, userName, processingTimeMillis);
/*  24: 79 */     this.failureCause = failureCause;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public long getProcessingTimeMillis()
/*  28:    */   {
/*  29: 87 */     return this.processingTimeMillis;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String getSessionId()
/*  33:    */   {
/*  34: 94 */     return this.sessionId;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getUserName()
/*  38:    */   {
/*  39:103 */     return this.userName;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public boolean wasFailure()
/*  43:    */   {
/*  44:110 */     return this.failureCause != null;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Throwable getFailureCause()
/*  48:    */   {
/*  49:117 */     return this.failureCause;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getShortDescription()
/*  53:    */   {
/*  54:126 */     StringBuilder sb = new StringBuilder();
/*  55:127 */     sb.append("session=[").append(this.sessionId).append("]; ");
/*  56:128 */     sb.append("user=[").append(this.userName).append("]; ");
/*  57:129 */     return sb.toString();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public String getDescription()
/*  61:    */   {
/*  62:137 */     StringBuilder sb = new StringBuilder();
/*  63:138 */     sb.append("session=[").append(this.sessionId).append("]; ");
/*  64:139 */     sb.append("user=[").append(this.userName).append("]; ");
/*  65:140 */     sb.append("time=[").append(this.processingTimeMillis).append("ms]; ");
/*  66:141 */     sb.append("status=[");
/*  67:142 */     if (!wasFailure()) {
/*  68:143 */       sb.append("OK");
/*  69:    */     } else {
/*  70:146 */       sb.append("failed: ").append(this.failureCause);
/*  71:    */     }
/*  72:148 */     sb.append(']');
/*  73:149 */     return sb.toString();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String toString()
/*  77:    */   {
/*  78:154 */     return "RequestHandledEvent: " + getDescription();
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.RequestHandledEvent
 * JD-Core Version:    0.7.0.1
 */