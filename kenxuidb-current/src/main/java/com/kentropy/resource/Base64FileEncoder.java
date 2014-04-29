/*  1:   */ package com.kentropy.resource;
/*  2:   */ 
/*  3:   */ import biz.source_code.base64Coder.Base64Coder;
/*  4:   */ import java.io.BufferedInputStream;
/*  5:   */ import java.io.BufferedWriter;
/*  6:   */ import java.io.FileInputStream;
/*  7:   */ import java.io.FileWriter;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.io.InputStream;
/* 10:   */ import java.io.PrintStream;
/* 11:   */ 
/* 12:   */ public class Base64FileEncoder
/* 13:   */ {
/* 14:   */   public static void main(String[] args)
/* 15:   */     throws IOException
/* 16:   */   {
/* 17:17 */     if (args.length != 2)
/* 18:   */     {
/* 19:18 */       System.out.println("Command line parameters: inputFileName outputFileName");
/* 20:19 */       System.exit(9);
/* 21:   */     }
/* 22:20 */     encodeFile(args[0], args[1]);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void encodeFile(String inputFileName, String outputFileName)
/* 26:   */     throws IOException
/* 27:   */   {
/* 28:23 */     BufferedInputStream in = null;
/* 29:24 */     BufferedWriter out = null;
/* 30:   */     try
/* 31:   */     {
/* 32:26 */       in = new BufferedInputStream(new FileInputStream(inputFileName));
/* 33:27 */       out = new BufferedWriter(new FileWriter(outputFileName));
/* 34:28 */       encodeStream(in, out);
/* 35:29 */       out.flush();
/* 36:   */     }
/* 37:   */     finally
/* 38:   */     {
/* 39:31 */       if (in != null) {
/* 40:31 */         in.close();
/* 41:   */       }
/* 42:32 */       if (out != null) {
/* 43:32 */         out.close();
/* 44:   */       }
/* 45:   */     }
/* 46:   */   }
/* 47:   */   
/* 48:   */   private static void encodeStream(InputStream in, BufferedWriter out)
/* 49:   */     throws IOException
/* 50:   */   {
/* 51:35 */     int lineLength = 72;
/* 52:36 */     byte[] buf = new byte[lineLength / 4 * 3];
/* 53:   */     for (;;)
/* 54:   */     {
/* 55:38 */       int len = in.read(buf);
/* 56:39 */       if (len <= 0) {
/* 57:   */         break;
/* 58:   */       }
/* 59:40 */       out.write(Base64Coder.encode(buf, 0, len));
/* 60:41 */       out.newLine();
/* 61:   */     }
/* 62:   */   }
/* 63:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.resource.Base64FileEncoder
 * JD-Core Version:    0.7.0.1
 */