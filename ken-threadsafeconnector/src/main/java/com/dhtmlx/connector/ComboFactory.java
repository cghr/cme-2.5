/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.HashMap;
/*  4:   */ 
/*  5:   */ public class ComboFactory
/*  6:   */   extends BaseFactory
/*  7:   */ {
/*  8:   */   public DataItem createDataItem(HashMap<String, String> data, DataConfig config, int index)
/*  9:   */   {
/* 10:19 */     return new ComboDataItem(data, config, index);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ComboFactory
 * JD-Core Version:    0.7.0.1
 */