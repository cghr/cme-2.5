/*   1:    */ package org.springframework.mail;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.PrintWriter;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.LinkedHashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import org.springframework.util.ObjectUtils;
/*   9:    */ 
/*  10:    */ public class MailSendException
/*  11:    */   extends MailException
/*  12:    */ {
/*  13:    */   private final transient Map<Object, Exception> failedMessages;
/*  14:    */   private Exception[] messageExceptions;
/*  15:    */   
/*  16:    */   public MailSendException(String msg)
/*  17:    */   {
/*  18: 45 */     this(msg, null);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public MailSendException(String msg, Throwable cause)
/*  22:    */   {
/*  23: 54 */     super(msg, cause);
/*  24: 55 */     this.failedMessages = new LinkedHashMap();
/*  25:    */   }
/*  26:    */   
/*  27:    */   public MailSendException(String msg, Throwable cause, Map<Object, Exception> failedMessages)
/*  28:    */   {
/*  29: 69 */     super(msg, cause);
/*  30: 70 */     this.failedMessages = new LinkedHashMap(failedMessages);
/*  31: 71 */     this.messageExceptions = ((Exception[])failedMessages.values().toArray(new Exception[failedMessages.size()]));
/*  32:    */   }
/*  33:    */   
/*  34:    */   public MailSendException(Map<Object, Exception> failedMessages)
/*  35:    */   {
/*  36: 83 */     this(null, null, failedMessages);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public final Map<Object, Exception> getFailedMessages()
/*  40:    */   {
/*  41:106 */     return this.failedMessages;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public final Exception[] getMessageExceptions()
/*  45:    */   {
/*  46:118 */     return this.messageExceptions != null ? this.messageExceptions : new Exception[0];
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getMessage()
/*  50:    */   {
/*  51:124 */     if (ObjectUtils.isEmpty(this.messageExceptions)) {
/*  52:125 */       return super.getMessage();
/*  53:    */     }
/*  54:128 */     StringBuilder sb = new StringBuilder();
/*  55:129 */     String baseMessage = super.getMessage();
/*  56:130 */     if (baseMessage != null) {
/*  57:131 */       sb.append(baseMessage).append(". ");
/*  58:    */     }
/*  59:133 */     sb.append("Failed messages: ");
/*  60:134 */     for (int i = 0; i < this.messageExceptions.length; i++)
/*  61:    */     {
/*  62:135 */       Exception subEx = this.messageExceptions[i];
/*  63:136 */       sb.append(subEx.toString());
/*  64:137 */       if (i < this.messageExceptions.length - 1) {
/*  65:138 */         sb.append("; ");
/*  66:    */       }
/*  67:    */     }
/*  68:141 */     return sb.toString();
/*  69:    */   }
/*  70:    */   
/*  71:    */   public String toString()
/*  72:    */   {
/*  73:147 */     if (ObjectUtils.isEmpty(this.messageExceptions)) {
/*  74:148 */       return super.toString();
/*  75:    */     }
/*  76:151 */     StringBuilder sb = new StringBuilder(super.toString());
/*  77:152 */     sb.append("; message exceptions (").append(this.messageExceptions.length).append(") are:");
/*  78:153 */     for (int i = 0; i < this.messageExceptions.length; i++)
/*  79:    */     {
/*  80:154 */       Exception subEx = this.messageExceptions[i];
/*  81:155 */       sb.append('\n').append("Failed message ").append(i + 1).append(": ");
/*  82:156 */       sb.append(subEx);
/*  83:    */     }
/*  84:158 */     return sb.toString();
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void printStackTrace(PrintStream ps)
/*  88:    */   {
/*  89:164 */     if (ObjectUtils.isEmpty(this.messageExceptions))
/*  90:    */     {
/*  91:165 */       super.printStackTrace(ps);
/*  92:    */     }
/*  93:    */     else
/*  94:    */     {
/*  95:168 */       ps.println(super.toString() + "; message exception details (" + 
/*  96:169 */         this.messageExceptions.length + ") are:");
/*  97:170 */       for (int i = 0; i < this.messageExceptions.length; i++)
/*  98:    */       {
/*  99:171 */         Exception subEx = this.messageExceptions[i];
/* 100:172 */         ps.println("Failed message " + (i + 1) + ":");
/* 101:173 */         subEx.printStackTrace(ps);
/* 102:    */       }
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void printStackTrace(PrintWriter pw)
/* 107:    */   {
/* 108:180 */     if (ObjectUtils.isEmpty(this.messageExceptions))
/* 109:    */     {
/* 110:181 */       super.printStackTrace(pw);
/* 111:    */     }
/* 112:    */     else
/* 113:    */     {
/* 114:184 */       pw.println(super.toString() + "; message exception details (" + 
/* 115:185 */         this.messageExceptions.length + ") are:");
/* 116:186 */       for (int i = 0; i < this.messageExceptions.length; i++)
/* 117:    */       {
/* 118:187 */         Exception subEx = this.messageExceptions[i];
/* 119:188 */         pw.println("Failed message " + (i + 1) + ":");
/* 120:189 */         subEx.printStackTrace(pw);
/* 121:    */       }
/* 122:    */     }
/* 123:    */   }
/* 124:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.MailSendException
 * JD-Core Version:    0.7.0.1
 */