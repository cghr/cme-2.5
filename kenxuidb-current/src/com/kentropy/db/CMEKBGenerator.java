/*  1:   */ package com.kentropy.db;
/*  2:   */ 
/*  3:   */ import net.xoetrope.xui.data.XBaseModel;
/*  4:   */ import net.xoetrope.xui.data.XModel;
/*  5:   */ 
/*  6:   */ public class CMEKBGenerator
/*  7:   */ {
/*  8:   */   public void generateKBOutput(String table, String datacol, String tagcol, String outputfile)
/*  9:   */   {
/* 10:13 */     XModel xm = new XBaseModel();
/* 11:14 */     TestXUIDB.getInstance().getData(table, datacol + "," + tagcol, "", xm);
/* 12:16 */     for (int i = 0; i < xm.getNumAttributes(); i++)
/* 13:   */     {
/* 14:18 */       XModel rowM = xm.get(i);
/* 15:19 */       String data = ((XModel)rowM.get(datacol)).get().toString();
/* 16:20 */       String str1 = ((XModel)rowM.get(datacol)).get().toString();
/* 17:   */     }
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static void main(String[] args) {}
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.db.CMEKBGenerator
 * JD-Core Version:    0.7.0.1
 */