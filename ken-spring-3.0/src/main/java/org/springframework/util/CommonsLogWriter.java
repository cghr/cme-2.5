/*  1:   */ package org.springframework.util;
/*  2:   */ 
/*  3:   */ import java.io.Writer;
/*  4:   */ import org.apache.commons.logging.Log;
/*  5:   */ 
/*  6:   */ public class CommonsLogWriter
/*  7:   */   extends Writer
/*  8:   */ {
/*  9:   */   private final Log logger;
/* 10:33 */   private final StringBuilder buffer = new StringBuilder();
/* 11:   */   
/* 12:   */   public CommonsLogWriter(Log logger)
/* 13:   */   {
/* 14:41 */     Assert.notNull(logger, "Logger must not be null");
/* 15:42 */     this.logger = logger;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void write(char ch)
/* 19:   */   {
/* 20:47 */     if ((ch == '\n') && (this.buffer.length() > 0))
/* 21:   */     {
/* 22:48 */       this.logger.debug(this.buffer.toString());
/* 23:49 */       this.buffer.setLength(0);
/* 24:   */     }
/* 25:   */     else
/* 26:   */     {
/* 27:52 */       this.buffer.append(ch);
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void write(char[] buffer, int offset, int length)
/* 32:   */   {
/* 33:58 */     for (int i = 0; i < length; i++)
/* 34:   */     {
/* 35:59 */       char ch = buffer[(offset + i)];
/* 36:60 */       if ((ch == '\n') && (this.buffer.length() > 0))
/* 37:   */       {
/* 38:61 */         this.logger.debug(this.buffer.toString());
/* 39:62 */         this.buffer.setLength(0);
/* 40:   */       }
/* 41:   */       else
/* 42:   */       {
/* 43:65 */         this.buffer.append(ch);
/* 44:   */       }
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void flush() {}
/* 49:   */   
/* 50:   */   public void close() {}
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.CommonsLogWriter
 * JD-Core Version:    0.7.0.1
 */