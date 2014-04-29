/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ public class ConnectorUtils
/*  4:   */ {
/*  5:   */   public static String join(Object[] strings, String separator)
/*  6:   */   {
/*  7:22 */     StringBuffer sb = new StringBuffer();
/*  8:23 */     for (int i = 0; i < strings.length; i++)
/*  9:   */     {
/* 10:24 */       if (i != 0) {
/* 11:24 */         sb.append(separator);
/* 12:   */       }
/* 13:25 */       sb.append(strings[i].toString());
/* 14:   */     }
/* 15:27 */     return sb.toString();
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorUtils
 * JD-Core Version:    0.7.0.1
 */