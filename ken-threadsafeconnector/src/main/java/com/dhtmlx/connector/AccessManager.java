/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ 
/*  5:   */ public class AccessManager
/*  6:   */ {
/*  7:   */   private HashMap<OperationType, Boolean> rules;
/*  8:   */   
/*  9:   */   public AccessManager()
/* 10:   */   {
/* 11:23 */     this.rules = new HashMap();
/* 12:   */   }
/* 13:   */   
/* 14:   */   public boolean check(OperationType mode)
/* 15:   */   {
/* 16:34 */     Boolean test = (Boolean)this.rules.get(mode);
/* 17:35 */     if (test == null) {
/* 18:35 */       return true;
/* 19:   */     }
/* 20:36 */     return test.booleanValue();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void allow(OperationType mode)
/* 24:   */   {
/* 25:45 */     this.rules.put(mode, Boolean.valueOf(true));
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void deny(OperationType mode)
/* 29:   */   {
/* 30:54 */     this.rules.put(mode, Boolean.valueOf(false));
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void deny_all()
/* 34:   */   {
/* 35:61 */     this.rules.put(OperationType.Read, Boolean.valueOf(false));
/* 36:62 */     this.rules.put(OperationType.Insert, Boolean.valueOf(false));
/* 37:63 */     this.rules.put(OperationType.Update, Boolean.valueOf(false));
/* 38:64 */     this.rules.put(OperationType.Delete, Boolean.valueOf(false));
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.AccessManager
 * JD-Core Version:    0.7.0.1
 */