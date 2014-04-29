/*  1:   */ package org.springframework.util;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.IOException;
/*  5:   */ 
/*  6:   */ public abstract class FileSystemUtils
/*  7:   */ {
/*  8:   */   public static boolean deleteRecursively(File root)
/*  9:   */   {
/* 10:39 */     if ((root != null) && (root.exists()))
/* 11:   */     {
/* 12:40 */       if (root.isDirectory())
/* 13:   */       {
/* 14:41 */         File[] children = root.listFiles();
/* 15:42 */         if (children != null) {
/* 16:43 */           for (File child : children) {
/* 17:44 */             deleteRecursively(child);
/* 18:   */           }
/* 19:   */         }
/* 20:   */       }
/* 21:48 */       return root.delete();
/* 22:   */     }
/* 23:50 */     return false;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static void copyRecursively(File src, File dest)
/* 27:   */     throws IOException
/* 28:   */   {
/* 29:61 */     Assert.isTrue((src != null) && ((src.isDirectory()) || (src.isFile())), "Source File must denote a directory or file");
/* 30:62 */     Assert.notNull(dest, "Destination File must not be null");
/* 31:63 */     doCopyRecursively(src, dest);
/* 32:   */   }
/* 33:   */   
/* 34:   */   private static void doCopyRecursively(File src, File dest)
/* 35:   */     throws IOException
/* 36:   */   {
/* 37:74 */     if (src.isDirectory())
/* 38:   */     {
/* 39:75 */       dest.mkdir();
/* 40:76 */       File[] entries = src.listFiles();
/* 41:77 */       if (entries == null) {
/* 42:78 */         throw new IOException("Could not list files in directory: " + src);
/* 43:   */       }
/* 44:80 */       for (File entry : entries) {
/* 45:81 */         doCopyRecursively(entry, new File(dest, entry.getName()));
/* 46:   */       }
/* 47:   */     }
/* 48:84 */     else if (src.isFile())
/* 49:   */     {
/* 50:   */       try
/* 51:   */       {
/* 52:86 */         dest.createNewFile();
/* 53:   */       }
/* 54:   */       catch (IOException ex)
/* 55:   */       {
/* 56:89 */         IOException ioex = new IOException("Failed to create file: " + dest);
/* 57:90 */         ioex.initCause(ex);
/* 58:91 */         throw ioex;
/* 59:   */       }
/* 60:93 */       FileCopyUtils.copy(src, dest);
/* 61:   */     }
/* 62:   */   }
/* 63:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.FileSystemUtils
 * JD-Core Version:    0.7.0.1
 */