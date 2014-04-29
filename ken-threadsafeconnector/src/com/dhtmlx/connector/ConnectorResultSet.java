/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*   5:    */ import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
/*   6:    */ 
/*   7:    */ public class ConnectorResultSet
/*   8:    */ {
/*   9: 23 */   private SqlRowSet result = null;
/*  10:    */   
/*  11:    */   public ConnectorResultSet(SqlRowSet external_result)
/*  12:    */   {
/*  13: 33 */     this.result = external_result;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public String get_last_id()
/*  17:    */     throws ConnectorOperationException
/*  18:    */   {
/*  19: 44 */     if (this.result != null) {
/*  20: 45 */       return this.result.getString(1);
/*  21:    */     }
/*  22: 46 */     return null;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public HashMap<String, String> get_next()
/*  26:    */     throws ConnectorOperationException
/*  27:    */   {
/*  28: 57 */     if (this.result != null)
/*  29:    */     {
/*  30: 58 */       HashMap<String, String> data = new HashMap();
/*  31:    */       
/*  32:    */ 
/*  33: 61 */       SqlRowSetMetaData rd = this.result.getMetaData();
/*  34: 62 */       int max = rd.getColumnCount();
/*  35: 64 */       for (int i = 1; i <= max; i++) {
/*  36: 65 */         data.put(rd.getColumnLabel(i), this.result.getString(i));
/*  37:    */       }
/*  38: 67 */       if (!this.result.next()) {
/*  39: 69 */         this.result = null;
/*  40:    */       }
/*  41: 72 */       return data;
/*  42:    */     }
/*  43: 74 */     return null;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String get(String name)
/*  47:    */     throws ConnectorOperationException
/*  48:    */   {
/*  49: 88 */     String label = null;
/*  50: 89 */     if (this.result != null) {
/*  51: 90 */       label = this.result.getString(name);
/*  52:    */     }
/*  53: 98 */     return label;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void jump_to(int start_from)
/*  57:    */     throws ConnectorOperationException
/*  58:    */   {
/*  59:109 */     if (this.result != null) {
/*  60:110 */       this.result.absolute(start_from + 1);
/*  61:    */     }
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorResultSet
 * JD-Core Version:    0.7.0.1
 */