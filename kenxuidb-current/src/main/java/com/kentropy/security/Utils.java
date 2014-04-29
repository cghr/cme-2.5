/*  1:   */ package com.kentropy.security;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import de.schlund.pfixxml.util.MD5Utils;
/*  5:   */ import net.xoetrope.xui.data.XBaseModel;
/*  6:   */ import net.xoetrope.xui.data.XModel;
/*  7:   */ 
/*  8:   */ public class Utils
/*  9:   */ {
/* 10:   */   public static void savePrincipal(KenPrincipal p)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:13 */     String[] tt = { "id", "fullname", "username", "password", "teamId" };
/* 14:14 */     String[] tt1 = { p.getId(), p.getName(), p.getUser(), MD5Utils.hex_md5(p.getPassword()), p.getTeamId() };
/* 15:15 */     for (int i = 0; i < tt.length; i++)
/* 16:   */     {
/* 17:17 */       TestXUIDB.getInstance().execSQL("delete from configuration where property='" + tt[i] + "'", new StringBuffer());
/* 18:18 */       if (tt1[i] != null)
/* 19:   */       {
/* 20:20 */         XModel xm = new XBaseModel();
/* 21:21 */         xm.set("property", tt[i]);
/* 22:22 */         xm.set("value", tt1[i]);
/* 23:23 */         TestXUIDB.getInstance().saveDataM2("configuration", "property='" + tt[i] + "'", xm);
/* 24:   */       }
/* 25:   */     }
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static void main(String[] args) {}
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.Utils
 * JD-Core Version:    0.7.0.1
 */