/*   1:    */ package com.kentropy.crypt;
/*   2:    */ 
/*   3:    */ import java.io.FileInputStream;
/*   4:    */ import java.io.FileNotFoundException;
/*   5:    */ import java.io.FileOutputStream;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.ObjectInputStream;
/*   9:    */ import java.io.ObjectOutputStream;
/*  10:    */ import java.io.OutputStream;
/*  11:    */ import java.security.InvalidKeyException;
/*  12:    */ import java.security.KeyStoreException;
/*  13:    */ import java.security.NoSuchAlgorithmException;
/*  14:    */ import java.security.cert.CertificateException;
/*  15:    */ import javax.crypto.Cipher;
/*  16:    */ import javax.crypto.CipherInputStream;
/*  17:    */ import javax.crypto.CipherOutputStream;
/*  18:    */ import javax.crypto.KeyGenerator;
/*  19:    */ import javax.crypto.NoSuchPaddingException;
/*  20:    */ import javax.crypto.SecretKey;
/*  21:    */ 
/*  22:    */ public class DesEncryptor
/*  23:    */ {
/*  24:    */   Cipher ecipher;
/*  25:    */   Cipher dcipher;
/*  26:    */   
/*  27:    */   public DesEncryptor()
/*  28:    */     throws FileNotFoundException, IOException, ClassNotFoundException
/*  29:    */   {
/*  30: 28 */     SecretKey key = readKey();
/*  31: 29 */     init(key);
/*  32:    */   }
/*  33:    */   
/*  34:    */   private void init(SecretKey key)
/*  35:    */   {
/*  36:    */     try
/*  37:    */     {
/*  38: 35 */       this.ecipher = Cipher.getInstance("DES");
/*  39: 36 */       this.dcipher = Cipher.getInstance("DES");
/*  40:    */       
/*  41:    */ 
/*  42: 39 */       this.ecipher.init(1, key);
/*  43: 40 */       this.dcipher.init(2, key);
/*  44:    */     }
/*  45:    */     catch (NoSuchPaddingException e)
/*  46:    */     {
/*  47: 42 */       e.printStackTrace();
/*  48:    */     }
/*  49:    */     catch (NoSuchAlgorithmException e)
/*  50:    */     {
/*  51: 44 */       e.printStackTrace();
/*  52:    */     }
/*  53:    */     catch (InvalidKeyException e)
/*  54:    */     {
/*  55: 46 */       e.printStackTrace();
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59: 51 */   byte[] buf = new byte[1024];
/*  60:    */   
/*  61:    */   public void encrypt(InputStream in, OutputStream out)
/*  62:    */   {
/*  63:    */     try
/*  64:    */     {
/*  65: 56 */       out = new CipherOutputStream(out, this.ecipher);
/*  66:    */       
/*  67:    */ 
/*  68: 59 */       int numRead = 0;
/*  69: 60 */       while ((numRead = in.read(this.buf)) >= 0) {
/*  70: 61 */         out.write(this.buf, 0, numRead);
/*  71:    */       }
/*  72: 63 */       out.close();
/*  73:    */     }
/*  74:    */     catch (IOException e)
/*  75:    */     {
/*  76: 65 */       e.printStackTrace();
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void decrypt(InputStream in, OutputStream out)
/*  81:    */   {
/*  82:    */     try
/*  83:    */     {
/*  84: 72 */       in = new CipherInputStream(in, this.dcipher);
/*  85:    */       
/*  86:    */ 
/*  87: 75 */       int numRead = 0;
/*  88: 76 */       while ((numRead = in.read(this.buf)) >= 0) {
/*  89: 77 */         out.write(this.buf, 0, numRead);
/*  90:    */       }
/*  91: 79 */       out.close();
/*  92:    */     }
/*  93:    */     catch (IOException e)
/*  94:    */     {
/*  95: 81 */       e.printStackTrace();
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static SecretKey generateKey()
/* 100:    */     throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException
/* 101:    */   {
/* 102: 87 */     KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
/* 103: 88 */     SecretKey key = keyGenerator.generateKey();
/* 104:    */     
/* 105:    */ 
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112: 97 */     OutputStream fos = new FileOutputStream("E:/encryption/keystore");
/* 113: 98 */     ObjectOutputStream oos = new ObjectOutputStream(fos);
/* 114: 99 */     oos.writeObject(key);
/* 115:    */     
/* 116:101 */     return key;
/* 117:    */   }
/* 118:    */   
/* 119:    */   private static SecretKey readKey()
/* 120:    */     throws FileNotFoundException, IOException, ClassNotFoundException
/* 121:    */   {
/* 122:107 */     ObjectInputStream ois = new ObjectInputStream(DesEncryptor.class.getResourceAsStream("keystore"));
/* 123:108 */     SecretKey key = (SecretKey)ois.readObject();
/* 124:109 */     return key;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static void main(String[] args)
/* 128:    */     throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, ClassNotFoundException
/* 129:    */   {
/* 130:116 */     DesEncryptor encryptor = new DesEncryptor();
/* 131:117 */     encryptor.encrypt(new FileInputStream("E:/encryption/test/03300096_01_01_cod.png"), new FileOutputStream("E:/encryption/test/03300096_01_01_cod.enc.png"));
/* 132:118 */     encryptor.encrypt(new FileInputStream("E:/encryption/test/03300096_01_01_0_blank.png"), new FileOutputStream("E:/encryption/test/03300096_01_01_0_blank.enc.png"));
/* 133:119 */     encryptor.encrypt(new FileInputStream("E:/encryption/test/03300096_01_01_1_blank.png"), new FileOutputStream("E:/encryption/test/03300096_01_01_1_blank.enc.png"));
/* 134:    */   }
/* 135:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-crypt\ken-crypt.jar
 * Qualified Name:     com.kentropy.crypt.DesEncryptor
 * JD-Core Version:    0.7.0.1
 */