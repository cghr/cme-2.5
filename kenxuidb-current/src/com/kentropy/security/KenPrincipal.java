/*  1:   */ package com.kentropy.security;
/*  2:   */ 
/*  3:   */ import java.security.Principal;
/*  4:   */ 
/*  5:   */ public class KenPrincipal
/*  6:   */   implements Principal
/*  7:   */ {
/*  8: 8 */   public String user = null;
/*  9: 9 */   public String id = null;
/* 10:11 */   public String password = null;
/* 11:12 */   public String sessionID = null;
/* 12:14 */   String name = "";
/* 13:   */   String teamId;
/* 14:   */   
/* 15:   */   public String getId()
/* 16:   */   {
/* 17:19 */     return this.id;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setId(String id)
/* 21:   */   {
/* 22:24 */     this.id = id;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean equals(Object obj)
/* 26:   */   {
/* 27:29 */     if ((obj instanceof KenPrincipal)) {
/* 28:31 */       return true;
/* 29:   */     }
/* 30:34 */     return false;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public String getUser()
/* 34:   */   {
/* 35:39 */     return this.user;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void setUser(String user)
/* 39:   */   {
/* 40:44 */     this.user = user;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getPassword()
/* 44:   */   {
/* 45:49 */     return this.password;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public void setPassword(String password)
/* 49:   */   {
/* 50:54 */     this.password = password;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String getSessionID()
/* 54:   */   {
/* 55:59 */     return this.sessionID;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void setSessionID(String sessionID)
/* 59:   */   {
/* 60:64 */     this.sessionID = sessionID;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public static void main(String[] args) {}
/* 64:   */   
/* 65:   */   public void setName(String name)
/* 66:   */   {
/* 67:73 */     this.name = name;
/* 68:   */   }
/* 69:   */   
/* 70:   */   public void setTeamId(String teamId)
/* 71:   */   {
/* 72:78 */     this.teamId = teamId;
/* 73:   */   }
/* 74:   */   
/* 75:   */   public String getName()
/* 76:   */   {
/* 77:83 */     return this.name;
/* 78:   */   }
/* 79:   */   
/* 80:   */   public String getTeamId()
/* 81:   */   {
/* 82:88 */     return this.teamId;
/* 83:   */   }
/* 84:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.KenPrincipal
 * JD-Core Version:    0.7.0.1
 */