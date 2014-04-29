/*  1:   */ package com.kentropy.data;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import net.xoetrope.xui.data.XBaseModel;
/*  5:   */ import net.xoetrope.xui.data.XModel;
/*  6:   */ 
/*  7:   */ public class DummyDataHandler
/*  8:   */   implements DataHandler
/*  9:   */ {
/* 10:   */   public XModel getData(String type, String subtype, XModel context, String fields)
/* 11:   */     throws Exception
/* 12:   */   {
/* 13:13 */     XModel xm = new XBaseModel();
/* 14:14 */     if (subtype.equals("client"))
/* 15:   */     {
/* 16:16 */       String where = "id='" + ((XModel)context.get("client")).get() + "'";
/* 17:17 */       TestXUIDB.getInstance().getData("client", fields, where, xm);
/* 18:   */     }
/* 19:20 */     return xm;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public XModel getChildren(String type, String subtype, XModel context, String fields, String constraints)
/* 23:   */     throws Exception
/* 24:   */   {
/* 25:26 */     return null;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void saveData(String type, String subtype, XModel context, XModel dataM)
/* 29:   */     throws Exception
/* 30:   */   {
/* 31:32 */     if (type.equals("client"))
/* 32:   */     {
/* 33:34 */       String where = "";
/* 34:35 */       if (!((XModel)context.get("client")).get().equals("new")) {}
/* 35:36 */       where = "clientId='" + ((XModel)context.get("client")).get() + "'";
/* 36:37 */       TestXUIDB.getInstance().saveDataM2("client", where, dataM);
/* 37:   */     }
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static void main(String[] args) {}
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.DummyDataHandler
 * JD-Core Version:    0.7.0.1
 */