/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ 
/*  7:   */ public class IntegrationAuth
/*  8:   */ {
/*  9:   */   public String getClientIpAddr(HttpServletRequest request)
/* 10:   */   {
/* 11:14 */     String ip = request.getHeader("X-Forwarded-For");
/* 12:15 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/* 13:16 */       ip = request.getHeader("Proxy-Client-IP");
/* 14:   */     }
/* 15:18 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/* 16:19 */       ip = request.getHeader("WL-Proxy-Client-IP");
/* 17:   */     }
/* 18:21 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/* 19:22 */       ip = request.getHeader("HTTP_CLIENT_IP");
/* 20:   */     }
/* 21:24 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/* 22:25 */       ip = request.getHeader("HTTP_X_FORWARDED_FOR");
/* 23:   */     }
/* 24:27 */     if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
/* 25:28 */       ip = request.getRemoteAddr();
/* 26:   */     }
/* 27:30 */     return ip;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public boolean checkAllowedHost(String host)
/* 31:   */   {
/* 32:36 */     String[] allowedHosts = TestXUIDB.getInstance().getProperty("allowed_hosts").split(",");
/* 33:37 */     for (int i = 0; i < allowedHosts.length; i++) {
/* 34:39 */       if (allowedHosts[i].equals(host)) {
/* 35:40 */         return true;
/* 36:   */       }
/* 37:   */     }
/* 38:43 */     return false;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static void main(String[] args) {}
/* 42:   */   
/* 43:   */   public void authenticate(HttpServletRequest request)
/* 44:   */     throws SecurityException
/* 45:   */   {
/* 46:56 */     String host = getClientIpAddr(request);
/* 47:57 */     System.out.println(" Host " + host);
/* 48:58 */     if (!checkAllowedHost(host)) {
/* 49:59 */       throw new SecurityException("Not allowed");
/* 50:   */     }
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.IntegrationAuth
 * JD-Core Version:    0.7.0.1
 */