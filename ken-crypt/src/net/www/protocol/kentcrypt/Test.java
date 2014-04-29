/*  1:   */ package net.www.protocol.kentcrypt;
/*  2:   */ 
/*  3:   */ import java.io.FileOutputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import java.net.URL;
/*  7:   */ 
/*  8:   */ public class Test
/*  9:   */ {
/* 10:   */   public static void main(String[] args)
/* 11:   */     throws IOException
/* 12:   */   {
/* 13:10 */     URL.setURLStreamHandlerFactory(new KentCryptURLStreamHandlerFactory());
/* 14:   */     
/* 15:12 */     URL url = new URL("kentcrypt:file:/E:/encryption/03300118_01.enc.tif");
/* 16:13 */     InputStream inStream = url.openStream();
/* 17:14 */     FileOutputStream fos = new FileOutputStream("E:/encryption/03300118_01.dec.tif");
/* 18:   */     
/* 19:16 */     byte[] b = new byte[''];
/* 20:17 */     for (int n = inStream.read(b); n != -1; n = inStream.read(b)) {
/* 21:18 */       fos.write(b, 0, n);
/* 22:   */     }
/* 23:   */   }
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     net.www.protocol.kentcrypt.Test
 * JD-Core Version:    0.7.0.1
 */