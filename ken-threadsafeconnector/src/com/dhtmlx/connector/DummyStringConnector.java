/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class DummyStringConnector
/*  4:   */   extends BaseConnector
/*  5:   */ {
/*  6:   */   private String ready_data;
/*  7:   */   
/*  8:   */   public DummyStringConnector(String data)
/*  9:   */   {
/* 10:24 */     super(null);
/* 11:25 */     this.ready_data = data;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public String render()
/* 15:   */   {
/* 16:33 */     return this.ready_data;
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.DummyStringConnector
 * JD-Core Version:    0.7.0.1
 */