/*  1:   */ package net.www.protocol.kentcrypt;
/*  2:   */ 
/*  3:   */ import java.io.FileInputStream;
/*  4:   */ import java.io.FileNotFoundException;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.InputStream;
/*  7:   */ import java.io.ObjectInputStream;
/*  8:   */ import java.security.InvalidKeyException;
/*  9:   */ import java.security.NoSuchAlgorithmException;
/* 10:   */ import java.security.spec.AlgorithmParameterSpec;
/* 11:   */ import javax.crypto.Cipher;
/* 12:   */ import javax.crypto.CipherInputStream;
/* 13:   */ import javax.crypto.NoSuchPaddingException;
/* 14:   */ import javax.crypto.SecretKey;
/* 15:   */ import javax.crypto.spec.IvParameterSpec;
/* 16:   */ 
/* 17:   */ public class KentCryptInputStream
/* 18:   */   extends InputStream
/* 19:   */ {
/* 20:18 */   InputStream in = null;
/* 21:   */   Cipher ecipher;
/* 22:   */   Cipher dcipher;
/* 23:   */   
/* 24:   */   public void set(InputStream in)
/* 25:   */     throws NoSuchAlgorithmException, FileNotFoundException, IOException, ClassNotFoundException
/* 26:   */   {
/* 27:25 */     ObjectInputStream ois = new ObjectInputStream(new FileInputStream("E:/encryption/keystore"));
/* 28:26 */     SecretKey key = (SecretKey)ois.readObject();
/* 29:27 */     init(key);
/* 30:28 */     this.in = new CipherInputStream(in, this.dcipher);
/* 31:   */   }
/* 32:   */   
/* 33:   */   private void init(SecretKey key)
/* 34:   */   {
/* 35:34 */     byte[] iv = {
/* 36:35 */       -114, 18, 57, -100, 
/* 37:36 */       7, 114, 111, 90 };
/* 38:   */     
/* 39:38 */     AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
/* 40:   */     try
/* 41:   */     {
/* 42:40 */       this.ecipher = Cipher.getInstance("DES");
/* 43:41 */       this.dcipher = Cipher.getInstance("DES");
/* 44:   */       
/* 45:   */ 
/* 46:44 */       this.ecipher.init(1, key);
/* 47:45 */       this.dcipher.init(2, key);
/* 48:   */     }
/* 49:   */     catch (NoSuchPaddingException e)
/* 50:   */     {
/* 51:47 */       e.printStackTrace();
/* 52:   */     }
/* 53:   */     catch (NoSuchAlgorithmException e)
/* 54:   */     {
/* 55:49 */       e.printStackTrace();
/* 56:   */     }
/* 57:   */     catch (InvalidKeyException e)
/* 58:   */     {
/* 59:51 */       e.printStackTrace();
/* 60:   */     }
/* 61:   */   }
/* 62:   */   
/* 63:   */   public int read()
/* 64:   */     throws IOException
/* 65:   */   {
/* 66:57 */     if (this.in == null) {
/* 67:58 */       throw new IOException("No Stream");
/* 68:   */     }
/* 69:60 */     return this.in.read();
/* 70:   */   }
/* 71:   */   
/* 72:   */   public static void main(String[] args)
/* 73:   */     throws NoSuchAlgorithmException
/* 74:   */   {}
/* 75:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     net.www.protocol.kentcrypt.KentCryptInputStream
 * JD-Core Version:    0.7.0.1
 */