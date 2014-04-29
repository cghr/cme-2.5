/*  1:   */ package net.www.protocol.kentcrypt;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.net.URL;
/*  7:   */ import java.net.URLConnection;
/*  8:   */ import java.security.NoSuchAlgorithmException;
/*  9:   */ 
/* 10:   */ public class KentCryptURLConnection
/* 11:   */   extends URLConnection
/* 12:   */ {
/* 13:   */   KentCryptInputStream cis;
/* 14:14 */   static int defaultPort = 80;
/* 15:   */   
/* 16:   */   KentCryptURLConnection(URL url)
/* 17:   */     throws IOException
/* 18:   */   {
/* 19:17 */     super(url);
/* 20:   */     try
/* 21:   */     {
/* 22:19 */       String name = "net.www.protocol.kentcrypt.KentCryptInputStream";
/* 23:20 */       this.cis = ((KentCryptInputStream)Class.forName(name).newInstance());
/* 24:   */     }
/* 25:   */     catch (Exception e)
/* 26:   */     {
/* 27:22 */       e.printStackTrace();
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public synchronized void connect()
/* 32:   */     throws IOException
/* 33:   */   {
/* 34:   */     try
/* 35:   */     {
/* 36:31 */       if (this.cis == null) {
/* 37:32 */         this.cis = ((KentCryptInputStream)Class.forName("net.www.protocol.kentcrypt.KentCryptInputStream").newInstance());
/* 38:   */       }
/* 39:   */       int port;
/* 40:34 */       if ((port = this.url.getPort()) == -1) {
/* 41:35 */         port = defaultPort;
/* 42:   */       }
/* 43:36 */       String urlString = this.url.toString();
/* 44:37 */       urlString = urlString.substring(10);
/* 45:38 */       System.out.println("URLString" + urlString);
/* 46:39 */       URL url1 = new URL(urlString);
/* 47:40 */       System.out.println("QueryURL:" + this.url.toString());
/* 48:   */       
/* 49:   */ 
/* 50:   */ 
/* 51:   */ 
/* 52:   */ 
/* 53:46 */       this.cis.set(url1.openStream());
/* 54:47 */       this.connected = true;
/* 55:   */     }
/* 56:   */     catch (NoSuchAlgorithmException e)
/* 57:   */     {
/* 58:50 */       this.connected = false;
/* 59:51 */       e.printStackTrace();
/* 60:   */     }
/* 61:   */     catch (ClassNotFoundException e)
/* 62:   */     {
/* 63:54 */       e.printStackTrace();
/* 64:   */     }
/* 65:   */     catch (InstantiationException e)
/* 66:   */     {
/* 67:57 */       e.printStackTrace();
/* 68:   */     }
/* 69:   */     catch (IllegalAccessException e)
/* 70:   */     {
/* 71:60 */       e.printStackTrace();
/* 72:   */     }
/* 73:   */   }
/* 74:   */   
/* 75:   */   public synchronized InputStream getInputStream()
/* 76:   */     throws IOException
/* 77:   */   {
/* 78:65 */     if (!this.connected) {
/* 79:66 */       connect();
/* 80:   */     }
/* 81:67 */     return this.cis;
/* 82:   */   }
/* 83:   */   
/* 84:   */   public String getContentType()
/* 85:   */   {
/* 86:71 */     return guessContentTypeFromName(this.url.getFile());
/* 87:   */   }
/* 88:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     net.www.protocol.kentcrypt.KentCryptURLConnection
 * JD-Core Version:    0.7.0.1
 */