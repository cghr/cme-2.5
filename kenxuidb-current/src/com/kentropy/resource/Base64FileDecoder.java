/*  1:   */ package com.kentropy.resource;
/*  2:   */ 
/*  3:   */ import biz.source_code.base64Coder.Base64Coder;
/*  4:   */ import java.io.BufferedOutputStream;
/*  5:   */ import java.io.BufferedReader;
/*  6:   */ import java.io.FileOutputStream;
/*  7:   */ import java.io.FileReader;
/*  8:   */ import java.io.IOException;
/*  9:   */ import java.io.OutputStream;
/* 10:   */ import java.io.PrintStream;
/* 11:   */ 
/* 12:   */ public class Base64FileDecoder
/* 13:   */ {
/* 14:   */   public static void main(String[] args)
/* 15:   */     throws IOException
/* 16:   */   {
/* 17:17 */     if (args.length != 2)
/* 18:   */     {
/* 19:18 */       System.out.println("Command line parameters: inputFileName outputFileName");
/* 20:19 */       System.exit(9);
/* 21:   */     }
/* 22:20 */     decodeFile(args[0], args[1]);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void decodeFile(String inputFileName, String outputFileName)
/* 26:   */     throws IOException
/* 27:   */   {
/* 28:23 */     BufferedReader in = null;
/* 29:24 */     BufferedOutputStream out = null;
/* 30:   */     try
/* 31:   */     {
/* 32:26 */       in = new BufferedReader(new FileReader(inputFileName));
/* 33:27 */       out = new BufferedOutputStream(new FileOutputStream(outputFileName));
/* 34:28 */       decodeStream(in, out);
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
/* 48:   */   private static void decodeStream(BufferedReader in, OutputStream out)
/* 49:   */     throws IOException
/* 50:   */   {
/* 51:   */     for (;;)
/* 52:   */     {
/* 53:36 */       String s = in.readLine();
/* 54:37 */       if (s == null) {
/* 55:   */         break;
/* 56:   */       }
/* 57:38 */       byte[] buf = Base64Coder.decodeLines(s);
/* 58:39 */       out.write(buf);
/* 59:   */     }
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.resource.Base64FileDecoder
 * JD-Core Version:    0.7.0.1
 */