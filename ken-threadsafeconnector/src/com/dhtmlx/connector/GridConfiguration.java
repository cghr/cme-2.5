/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.Iterator;
/*  5:   */ 
/*  6:   */ public class GridConfiguration
/*  7:   */ {
/*  8:   */   private ArrayList<GridColumn> columns;
/*  9:   */   private ArrayList<String[]> headers;
/* 10:   */   private ArrayList<String[]> footers;
/* 11:11 */   public String delimiter = ",";
/* 12:   */   
/* 13:   */   public GridConfiguration()
/* 14:   */   {
/* 15:14 */     this.columns = new ArrayList();
/* 16:15 */     this.headers = new ArrayList();
/* 17:16 */     this.footers = new ArrayList();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void addColumn(GridColumn column)
/* 21:   */   {
/* 22:20 */     this.columns.add(column);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void addHeader(String[] columns)
/* 26:   */   {
/* 27:23 */     this.headers.add(columns);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void addFooter(String[] columns)
/* 31:   */   {
/* 32:26 */     this.footers.add(columns);
/* 33:   */   }
/* 34:   */   
/* 35:   */   private String join(String[] s)
/* 36:   */   {
/* 37:29 */     StringBuffer buffer = new StringBuffer();
/* 38:30 */     for (int i = 0; i < s.length; i++)
/* 39:   */     {
/* 40:31 */       if (i > 0) {
/* 41:32 */         buffer.append(this.delimiter);
/* 42:   */       }
/* 43:33 */       buffer.append(s[i]);
/* 44:   */     }
/* 45:35 */     return buffer.toString();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String toXML()
/* 49:   */   {
/* 50:38 */     StringBuffer out = new StringBuffer();
/* 51:39 */     out.append("<head>");
/* 52:40 */     Iterator<GridColumn> order = this.columns.iterator();
/* 53:42 */     while (order.hasNext())
/* 54:   */     {
/* 55:43 */       GridColumn col = (GridColumn)order.next();
/* 56:44 */       out.append(col.toXML());
/* 57:   */     }
/* 58:47 */     out.append("<afterInit>");
/* 59:49 */     if (this.headers != null)
/* 60:   */     {
/* 61:50 */       Iterator<String[]> horder = this.headers.iterator();
/* 62:52 */       while (horder.hasNext())
/* 63:   */       {
/* 64:53 */         String[] header = (String[])horder.next();
/* 65:54 */         out.append("<call command=\"attachHeader\"><param>");
/* 66:55 */         out.append(join(header));
/* 67:56 */         out.append("</param></call>");
/* 68:   */       }
/* 69:   */     }
/* 70:60 */     if (this.footers != null)
/* 71:   */     {
/* 72:61 */       Iterator<String[]> forder = this.footers.iterator();
/* 73:63 */       while (forder.hasNext())
/* 74:   */       {
/* 75:64 */         String[] footer = (String[])forder.next();
/* 76:65 */         out.append("<call command=\"attachFooter\"><param>");
/* 77:66 */         out.append(join(footer));
/* 78:67 */         out.append("</param></call>");
/* 79:   */       }
/* 80:   */     }
/* 81:71 */     out.append("</afterInit>");
/* 82:72 */     out.append("</head>");
/* 83:   */     
/* 84:74 */     return out.toString();
/* 85:   */   }
/* 86:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.GridConfiguration
 * JD-Core Version:    0.7.0.1
 */