/*  1:   */ package org.springframework.jdbc.datasource.embedded;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.OutputStream;
/*  5:   */ 
/*  6:   */ public class OutputStreamFactory
/*  7:   */ {
/*  8:   */   public static OutputStream getNoopOutputStream()
/*  9:   */   {
/* 10:35 */     new OutputStream()
/* 11:   */     {
/* 12:   */       public void write(int b)
/* 13:   */         throws IOException
/* 14:   */       {}
/* 15:   */     };
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.embedded.OutputStreamFactory
 * JD-Core Version:    0.7.0.1
 */