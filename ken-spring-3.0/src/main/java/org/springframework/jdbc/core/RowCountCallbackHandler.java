/*   1:    */ package org.springframework.jdbc.core;
/*   2:    */ 
/*   3:    */ import java.sql.ResultSet;
/*   4:    */ import java.sql.ResultSetMetaData;
/*   5:    */ import java.sql.SQLException;
/*   6:    */ import org.springframework.jdbc.support.JdbcUtils;
/*   7:    */ 
/*   8:    */ public class RowCountCallbackHandler
/*   9:    */   implements RowCallbackHandler
/*  10:    */ {
/*  11:    */   private int rowCount;
/*  12:    */   private int columnCount;
/*  13:    */   private int[] columnTypes;
/*  14:    */   private String[] columnNames;
/*  15:    */   
/*  16:    */   public final void processRow(ResultSet rs)
/*  17:    */     throws SQLException
/*  18:    */   {
/*  19: 73 */     if (this.rowCount == 0)
/*  20:    */     {
/*  21: 74 */       ResultSetMetaData rsmd = rs.getMetaData();
/*  22: 75 */       this.columnCount = rsmd.getColumnCount();
/*  23: 76 */       this.columnTypes = new int[this.columnCount];
/*  24: 77 */       this.columnNames = new String[this.columnCount];
/*  25: 78 */       for (int i = 0; i < this.columnCount; i++)
/*  26:    */       {
/*  27: 79 */         this.columnTypes[i] = rsmd.getColumnType(i + 1);
/*  28: 80 */         this.columnNames[i] = JdbcUtils.lookupColumnName(rsmd, i + 1);
/*  29:    */       }
/*  30:    */     }
/*  31: 84 */     processRow(rs, this.rowCount++);
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected void processRow(ResultSet rs, int rowNum)
/*  35:    */     throws SQLException
/*  36:    */   {}
/*  37:    */   
/*  38:    */   public final int[] getColumnTypes()
/*  39:    */   {
/*  40:105 */     return this.columnTypes;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public final String[] getColumnNames()
/*  44:    */   {
/*  45:115 */     return this.columnNames;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public final int getRowCount()
/*  49:    */   {
/*  50:124 */     return this.rowCount;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public final int getColumnCount()
/*  54:    */   {
/*  55:134 */     return this.columnCount;
/*  56:    */   }
/*  57:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.RowCountCallbackHandler
 * JD-Core Version:    0.7.0.1
 */