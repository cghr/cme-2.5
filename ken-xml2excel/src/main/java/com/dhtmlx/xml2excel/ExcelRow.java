/*  1:   */ package com.dhtmlx.xml2excel;
/*  2:   */ 
/*  3:   */ import org.w3c.dom.Element;
/*  4:   */ import org.w3c.dom.Node;
/*  5:   */ import org.w3c.dom.NodeList;
/*  6:   */ 
/*  7:   */ public class ExcelRow
/*  8:   */ {
/*  9:   */   private ExcelCell[] cells;
/* 10:   */   
/* 11:   */   public void parse(Node parent)
/* 12:   */   {
/* 13:12 */     NodeList nodes = ((Element)parent).getElementsByTagName("cell");
/* 14:14 */     if ((nodes != null) && (nodes.getLength() > 0))
/* 15:   */     {
/* 16:16 */       this.cells = new ExcelCell[nodes.getLength()];
/* 17:17 */       for (int i = 0; i < nodes.getLength(); i++)
/* 18:   */       {
/* 19:18 */         Node text_node = nodes.item(i);
/* 20:19 */         ExcelCell cell = new ExcelCell();
/* 21:20 */         if (text_node != null) {
/* 22:21 */           cell.parse(text_node);
/* 23:   */         }
/* 24:22 */         this.cells[i] = cell;
/* 25:   */       }
/* 26:   */     }
/* 27:   */   }
/* 28:   */   
/* 29:   */   public ExcelCell[] getCells()
/* 30:   */   {
/* 31:28 */     return this.cells;
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelRow
 * JD-Core Version:    0.7.0.1
 */