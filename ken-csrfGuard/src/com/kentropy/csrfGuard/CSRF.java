/*  1:   */ package com.kentropy.csrfGuard;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import javax.servlet.http.HttpSession;
/*  5:   */ 
/*  6:   */ public class CSRF
/*  7:   */ {
/*  8:   */   public static boolean isValid(String token, HttpSession session)
/*  9:   */   {
/* 10:16 */     List<String> tokens = (List)session.getAttribute("tokens");
/* 11:   */     
/* 12:18 */     return tokens.contains(token);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static boolean deleteToken(String token, HttpSession session)
/* 16:   */   {
/* 17:25 */     List<String> tokens = (List)session.getAttribute("tokens");
/* 18:   */     
/* 19:27 */     tokens.remove(token);
/* 20:   */     
/* 21:29 */     return false;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public static void addToken(String token, HttpSession session)
/* 25:   */   {
/* 26:35 */     List<String> tokens = (List)session.getAttribute("tokens");
/* 27:   */     
/* 28:37 */     tokens.add(token);
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-csrfGuard\ken-csrfGuard.jar
 * Qualified Name:     com.kentropy.csrfGuard.CSRF
 * JD-Core Version:    0.7.0.1
 */