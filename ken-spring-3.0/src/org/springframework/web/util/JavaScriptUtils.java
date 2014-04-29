/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ public class JavaScriptUtils
/*  4:   */ {
/*  5:   */   public static String javaScriptEscape(String input)
/*  6:   */   {
/*  7:41 */     if (input == null) {
/*  8:42 */       return input;
/*  9:   */     }
/* 10:45 */     StringBuilder filtered = new StringBuilder(input.length());
/* 11:46 */     char prevChar = '\000';
/* 12:48 */     for (int i = 0; i < input.length(); i++)
/* 13:   */     {
/* 14:49 */       char c = input.charAt(i);
/* 15:50 */       if (c == '"') {
/* 16:51 */         filtered.append("\\\"");
/* 17:53 */       } else if (c == '\'') {
/* 18:54 */         filtered.append("\\'");
/* 19:56 */       } else if (c == '\\') {
/* 20:57 */         filtered.append("\\\\");
/* 21:59 */       } else if (c == '/') {
/* 22:60 */         filtered.append("\\/");
/* 23:62 */       } else if (c == '\t') {
/* 24:63 */         filtered.append("\\t");
/* 25:65 */       } else if (c == '\n')
/* 26:   */       {
/* 27:66 */         if (prevChar != '\r') {
/* 28:67 */           filtered.append("\\n");
/* 29:   */         }
/* 30:   */       }
/* 31:70 */       else if (c == '\r') {
/* 32:71 */         filtered.append("\\n");
/* 33:73 */       } else if (c == '\f') {
/* 34:74 */         filtered.append("\\f");
/* 35:   */       } else {
/* 36:77 */         filtered.append(c);
/* 37:   */       }
/* 38:79 */       prevChar = c;
/* 39:   */     }
/* 40:82 */     return filtered.toString();
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.JavaScriptUtils
 * JD-Core Version:    0.7.0.1
 */