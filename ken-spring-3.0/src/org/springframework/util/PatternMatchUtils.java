/*  1:   */ package org.springframework.util;
/*  2:   */ 
/*  3:   */ public abstract class PatternMatchUtils
/*  4:   */ {
/*  5:   */   public static boolean simpleMatch(String pattern, String str)
/*  6:   */   {
/*  7:37 */     if ((pattern == null) || (str == null)) {
/*  8:38 */       return false;
/*  9:   */     }
/* 10:40 */     int firstIndex = pattern.indexOf('*');
/* 11:41 */     if (firstIndex == -1) {
/* 12:42 */       return pattern.equals(str);
/* 13:   */     }
/* 14:44 */     if (firstIndex == 0)
/* 15:   */     {
/* 16:45 */       if (pattern.length() == 1) {
/* 17:46 */         return true;
/* 18:   */       }
/* 19:48 */       int nextIndex = pattern.indexOf('*', firstIndex + 1);
/* 20:49 */       if (nextIndex == -1) {
/* 21:50 */         return str.endsWith(pattern.substring(1));
/* 22:   */       }
/* 23:52 */       String part = pattern.substring(1, nextIndex);
/* 24:53 */       int partIndex = str.indexOf(part);
/* 25:54 */       while (partIndex != -1)
/* 26:   */       {
/* 27:55 */         if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
/* 28:56 */           return true;
/* 29:   */         }
/* 30:58 */         partIndex = str.indexOf(part, partIndex + 1);
/* 31:   */       }
/* 32:60 */       return false;
/* 33:   */     }
/* 34:64 */     return (str.length() >= firstIndex) && (pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex))) && (simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static boolean simpleMatch(String[] patterns, String str)
/* 38:   */   {
/* 39:76 */     if (patterns != null) {
/* 40:77 */       for (int i = 0; i < patterns.length; i++) {
/* 41:78 */         if (simpleMatch(patterns[i], str)) {
/* 42:79 */           return true;
/* 43:   */         }
/* 44:   */       }
/* 45:   */     }
/* 46:83 */     return false;
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.PatternMatchUtils
 * JD-Core Version:    0.7.0.1
 */