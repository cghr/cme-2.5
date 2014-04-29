/*  1:   */ package org.springframework.mail.javamail;
/*  2:   */ 
/*  3:   */ import javax.activation.FileTypeMap;
/*  4:   */ import javax.mail.Session;
/*  5:   */ import javax.mail.internet.MimeMessage;
/*  6:   */ 
/*  7:   */ class SmartMimeMessage
/*  8:   */   extends MimeMessage
/*  9:   */ {
/* 10:   */   private final String defaultEncoding;
/* 11:   */   private final FileTypeMap defaultFileTypeMap;
/* 12:   */   
/* 13:   */   public SmartMimeMessage(Session session, String defaultEncoding, FileTypeMap defaultFileTypeMap)
/* 14:   */   {
/* 15:52 */     super(session);
/* 16:53 */     this.defaultEncoding = defaultEncoding;
/* 17:54 */     this.defaultFileTypeMap = defaultFileTypeMap;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public final String getDefaultEncoding()
/* 21:   */   {
/* 22:62 */     return this.defaultEncoding;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public final FileTypeMap getDefaultFileTypeMap()
/* 26:   */   {
/* 27:69 */     return this.defaultFileTypeMap;
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.SmartMimeMessage
 * JD-Core Version:    0.7.0.1
 */