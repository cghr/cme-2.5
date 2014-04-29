/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStreamReader;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.net.MalformedURLException;
/*   8:    */ import java.net.URL;
/*   9:    */ import java.security.KeyManagementException;
/*  10:    */ import java.security.PublicKey;
/*  11:    */ import java.security.SecureRandom;
/*  12:    */ import java.security.cert.Certificate;
/*  13:    */ import java.security.cert.X509Certificate;
/*  14:    */ import javax.net.ssl.HostnameVerifier;
/*  15:    */ import javax.net.ssl.HttpsURLConnection;
/*  16:    */ import javax.net.ssl.SSLContext;
/*  17:    */ import javax.net.ssl.SSLPeerUnverifiedException;
/*  18:    */ import javax.net.ssl.SSLSession;
/*  19:    */ import javax.net.ssl.TrustManager;
/*  20:    */ import javax.net.ssl.X509TrustManager;
/*  21:    */ 
/*  22:    */ public class TestHttps
/*  23:    */ {
/*  24:    */   private void testIt()
/*  25:    */     throws KeyManagementException, Exception
/*  26:    */   {
/*  27: 29 */     String https_url = "https://www.cghr.org:8443/";
/*  28:    */     try
/*  29:    */     {
/*  30: 33 */       URL url = new URL(https_url);
/*  31: 34 */       HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
/*  32:    */       
/*  33:    */ 
/*  34:    */ 
/*  35:    */ 
/*  36: 39 */       print_https_cert(con);
/*  37:    */       
/*  38:    */ 
/*  39: 42 */       print_content(con);
/*  40:    */     }
/*  41:    */     catch (MalformedURLException e)
/*  42:    */     {
/*  43: 45 */       e.printStackTrace();
/*  44:    */     }
/*  45:    */     catch (IOException e)
/*  46:    */     {
/*  47: 47 */       e.printStackTrace();
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   private void print_https_cert(HttpsURLConnection con)
/*  52:    */   {
/*  53: 54 */     if (con != null) {
/*  54:    */       try
/*  55:    */       {
/*  56: 58 */         System.out.println("Response Code : " + con.getResponseCode());
/*  57: 59 */         System.out.println("Cipher Suite : " + con.getCipherSuite());
/*  58: 60 */         System.out.println("\n");
/*  59:    */         
/*  60: 62 */         Certificate[] certs = con.getServerCertificates();
/*  61: 63 */         for (Certificate cert : certs)
/*  62:    */         {
/*  63: 64 */           System.out.println("Cert Type : " + cert.getType());
/*  64: 65 */           System.out.println("Cert Hash Code : " + cert.hashCode());
/*  65: 66 */           System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
/*  66: 67 */           System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
/*  67: 68 */           System.out.println("\n");
/*  68:    */         }
/*  69:    */       }
/*  70:    */       catch (SSLPeerUnverifiedException e)
/*  71:    */       {
/*  72: 72 */         e.printStackTrace();
/*  73:    */       }
/*  74:    */       catch (IOException e)
/*  75:    */       {
/*  76: 74 */         e.printStackTrace();
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void print_content(HttpsURLConnection con)
/*  82:    */   {
/*  83: 82 */     if (con != null) {
/*  84:    */       try
/*  85:    */       {
/*  86: 86 */         System.out.println("****** Content of the URL ********");
/*  87: 87 */         BufferedReader br = 
/*  88: 88 */           new BufferedReader(
/*  89: 89 */           new InputStreamReader(con.getInputStream()));
/*  90:    */         String input;
/*  91: 93 */         while ((input = br.readLine()) != null)
/*  92:    */         {
/*  93:    */           String input;
/*  94: 94 */           System.out.println(input);
/*  95:    */         }
/*  96: 96 */         br.close();
/*  97:    */       }
/*  98:    */       catch (IOException e)
/*  99:    */       {
/* 100: 99 */         e.printStackTrace();
/* 101:    */       }
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static void main(String[] args)
/* 106:    */     throws KeyManagementException, Exception
/* 107:    */   {
/* 108:117 */     TrustManager[] trustAllCerts = {
/* 109:118 */       new X509TrustManager()
/* 110:    */       {
/* 111:    */         public X509Certificate[] getAcceptedIssuers()
/* 112:    */         {
/* 113:121 */           return null;
/* 114:    */         }
/* 115:    */         
/* 116:    */         public void checkClientTrusted(X509Certificate[] certs, String authType) {}
/* 117:    */         
/* 118:    */         public void checkServerTrusted(X509Certificate[] certs, String authType) {}
/* 119:124 */       } };
/* 120:125 */     SSLContext sslContext = SSLContext.getInstance("SSL");
/* 121:126 */     sslContext.init(null, trustAllCerts, new SecureRandom());
/* 122:127 */     HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
/* 123:128 */     HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
/* 124:    */     {
/* 125:    */       public boolean verify(String arg0, SSLSession arg1)
/* 126:    */       {
/* 127:132 */         System.out.println(arg0);
/* 128:133 */         System.out.println(arg1.getPeerHost());
/* 129:134 */         if (arg0.endsWith("cghr.org")) {
/* 130:135 */           return true;
/* 131:    */         }
/* 132:137 */         return false;
/* 133:    */       }
/* 134:137 */     });
/* 135:138 */     new TestHttps().testIt();
/* 136:    */   }
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.TestHttps
 * JD-Core Version:    0.7.0.1
 */