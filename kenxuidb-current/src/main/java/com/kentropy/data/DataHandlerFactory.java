/*  1:   */ package com.kentropy.data;
/*  2:   */ 
/*  3:   */ public class DataHandlerFactory
/*  4:   */ {
/*  5:   */   public DataHandler getDefaultDataHandler()
/*  6:   */   {
/*  7: 7 */     return new DummyDataHandler();
/*  8:   */   }
/*  9:   */   
/* 10:   */   public DataHandler getDataHandler(String survey)
/* 11:   */   {
/* 12:12 */     if (survey.equals("istp")) {
/* 13:13 */       return new ISTPDataHandler();
/* 14:   */     }
/* 15:15 */     return getDefaultDataHandler();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public static void main(String[] args) {}
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.data.DataHandlerFactory
 * JD-Core Version:    0.7.0.1
 */