/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ 
/*  5:   */ public class FileUtils
/*  6:   */ {
/*  7:   */   public static void deleteFile(String path)
/*  8:   */   {
/*  9:13 */     File file = new File(path);
/* 10:   */     
/* 11:15 */     file.delete();
/* 12:   */   }
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.FileUtils
 * JD-Core Version:    0.7.0.1
 */