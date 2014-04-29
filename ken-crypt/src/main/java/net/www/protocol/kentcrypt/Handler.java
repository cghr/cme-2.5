/*  1:   */ package net.www.protocol.kentcrypt;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import java.net.URL;
/*  6:   */ import java.net.URLConnection;
/*  7:   */ import java.net.URLStreamHandler;
/*  8:   */ 
/*  9:   */ public class Handler
/* 10:   */   extends URLStreamHandler
/* 11:   */ {
/* 12:   */   protected void parseURL(URL u, String spec, int start, int end)
/* 13:   */   {
/* 14:12 */     String urlString = spec.substring(spec.indexOf(':'));
/* 15:13 */     System.out.println("urlString" + urlString);
/* 16:   */     
/* 17:   */ 
/* 18:16 */     System.out.println("spec:" + spec);
/* 19:17 */     System.out.println("start:" + start);
/* 20:18 */     System.out.println("end:" + end);
/* 21:   */     
/* 22:   */ 
/* 23:21 */     start = 10;
/* 24:22 */     System.out.println("URL:" + spec.substring(start, end));
/* 25:23 */     super.parseURL(u, spec, start, end);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected URLConnection openConnection(URL url)
/* 29:   */     throws IOException
/* 30:   */   {
/* 31:27 */     System.out.println("In openConnection");
/* 32:   */     
/* 33:29 */     return new KentCryptURLConnection(url);
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     net.www.protocol.kentcrypt.Handler
 * JD-Core Version:    0.7.0.1
 */