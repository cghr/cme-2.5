/*  1:   */ package net.www.protocol.kentcrypt;
/*  2:   */ 
/*  3:   */ import java.net.URLStreamHandler;
/*  4:   */ import java.net.URLStreamHandlerFactory;
/*  5:   */ 
/*  6:   */ public class KentCryptURLStreamHandlerFactory
/*  7:   */   implements URLStreamHandlerFactory
/*  8:   */ {
/*  9:   */   public URLStreamHandler createURLStreamHandler(String protocol)
/* 10:   */   {
/* 11:10 */     if (protocol.equalsIgnoreCase("kentcrypt")) {
/* 12:11 */       return new Handler();
/* 13:   */     }
/* 14:13 */     return null;
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     net.www.protocol.kentcrypt.KentCryptURLStreamHandlerFactory
 * JD-Core Version:    0.7.0.1
 */