/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import org.json.simple.JSONArray;
/*  6:   */ import org.json.simple.JSONObject;
/*  7:   */ 
/*  8:   */ public class JSONCommonDataItem
/*  9:   */   extends DataItem
/* 10:   */ {
/* 11:   */   public JSONCommonDataItem(HashMap<String, String> data, DataConfig config, int index)
/* 12:   */   {
/* 13:12 */     super(data, config, index);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void to_json(JSONArray output)
/* 17:   */   {
/* 18:17 */     JSONObject record = new JSONObject();
/* 19:18 */     for (int i = 0; i < this.config.data.size(); i++) {
/* 20:19 */       record.put(((ConnectorField)this.config.data.get(i)).name, get_value(((ConnectorField)this.config.data.get(i)).name));
/* 21:   */     }
/* 22:21 */     record.put("id", get_id());
/* 23:22 */     output.add(record);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.JSONCommonDataItem
 * JD-Core Version:    0.7.0.1
 */