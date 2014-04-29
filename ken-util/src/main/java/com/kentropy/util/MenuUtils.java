/*  1:   */ package com.kentropy.util;
/*  2:   */ 
/*  3:   */ import java.io.BufferedReader;
/*  4:   */ import java.io.IOException;
/*  5:   */ import org.json.simple.JSONArray;
/*  6:   */ import org.json.simple.JSONObject;
/*  7:   */ import org.json.simple.parser.JSONParser;
/*  8:   */ import org.json.simple.parser.ParseException;
/*  9:   */ 
/* 10:   */ public class MenuUtils
/* 11:   */ {
/* 12:   */   public StringBuffer getMenuAsJson(BufferedReader br, String role)
/* 13:   */     throws ParseException, IOException
/* 14:   */   {
/* 15:19 */     StringBuffer menuJson = new StringBuffer();
/* 16:20 */     menuJson.append("[");
/* 17:   */     
/* 18:22 */     JSONParser parser = new JSONParser();
/* 19:   */     
/* 20:24 */     JSONArray array = (JSONArray)parser.parse(br);
/* 21:26 */     for (int i = 0; i < array.size(); i++)
/* 22:   */     {
/* 23:28 */       JSONObject obj = (JSONObject)array.get(i);
/* 24:29 */       menuJson.append(authorise(obj, role) ? obj + "," : "");
/* 25:   */     }
/* 26:32 */     menuJson.deleteCharAt(menuJson.length() - 1);
/* 27:33 */     menuJson.append("]");
/* 28:   */     
/* 29:   */ 
/* 30:   */ 
/* 31:   */ 
/* 32:38 */     return menuJson;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public boolean authorise(JSONObject jsonObj, String role)
/* 36:   */   {
/* 37:45 */     JSONParser parser = new JSONParser();
/* 38:46 */     JSONArray roles = (JSONArray)jsonObj.get("roles");
/* 39:47 */     if (roles != null) {
/* 40:49 */       for (int i = 0; i < roles.size(); i++)
/* 41:   */       {
/* 42:51 */         String tmp = roles.get(i).toString();
/* 43:52 */         if (role.equals(tmp)) {
/* 44:53 */           return true;
/* 45:   */         }
/* 46:   */       }
/* 47:   */     } else {
/* 48:59 */       return true;
/* 49:   */     }
/* 50:61 */     return false;
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.MenuUtils
 * JD-Core Version:    0.7.0.1
 */