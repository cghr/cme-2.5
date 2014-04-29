/*  1:   */ package com.kentropy.transfer;
/*  2:   */ 
/*  3:   */ import java.io.BufferedReader;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.InputStreamReader;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.util.zip.ZipInputStream;
/*  8:   */ 
/*  9:   */ public class StreamTest
/* 10:   */ {
/* 11:   */   public static void main(String[] args)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:14 */     ZipInputStream zin = new ZipInputStream(new FileInputStream("d:\\tmp\\test.zip"));
/* 15:15 */     InputStreamReader in = new InputStreamReader(zin);
/* 16:16 */     BufferedReader bin = new BufferedReader(in);
/* 17:17 */     zin.getNextEntry();
/* 18:   */     
/* 19:19 */     String line = "";
/* 20:20 */     while (line != null)
/* 21:   */     {
/* 22:22 */       line = bin.readLine();
/* 23:23 */       System.out.println(" Read Line " + line);
/* 24:   */     }
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.StreamTest
 * JD-Core Version:    0.7.0.1
 */