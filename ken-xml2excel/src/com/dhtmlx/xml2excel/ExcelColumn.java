/*  1:   */ package com.dhtmlx.xml2excel;
/*  2:   */ 
/*  3:   */ import org.w3c.dom.Element;
/*  4:   */ import org.w3c.dom.Node;
/*  5:   */ 
/*  6:   */ public class ExcelColumn
/*  7:   */ {
/*  8:   */   private String colName;
/*  9:   */   private String type;
/* 10:   */   private String align;
/* 11:   */   private int colspan;
/* 12:   */   private int rowspan;
/* 13:12 */   private int width = 0;
/* 14:13 */   private int height = 1;
/* 15:14 */   private boolean is_footer = false;
/* 16:   */   
/* 17:   */   public void parse(Element parent)
/* 18:   */   {
/* 19:17 */     this.is_footer = parent.getParentNode().getParentNode().getNodeName().equals("foot");
/* 20:   */     
/* 21:19 */     Node text_node = parent.getFirstChild();
/* 22:20 */     if (text_node != null) {
/* 23:21 */       this.colName = text_node.getNodeValue();
/* 24:   */     } else {
/* 25:23 */       this.colName = "";
/* 26:   */     }
/* 27:25 */     String width_string = parent.getAttribute("width");
/* 28:26 */     if (width_string.length() > 0) {
/* 29:27 */       this.width = Integer.parseInt(width_string);
/* 30:   */     }
/* 31:29 */     this.type = parent.getAttribute("type");
/* 32:30 */     this.align = parent.getAttribute("align");
/* 33:31 */     String colspan_string = parent.getAttribute("colspan");
/* 34:32 */     if (colspan_string.length() > 0) {
/* 35:33 */       this.colspan = Integer.parseInt(colspan_string);
/* 36:   */     }
/* 37:35 */     String rowspan_string = parent.getAttribute("rowspan");
/* 38:36 */     if (rowspan_string.length() > 0) {
/* 39:37 */       this.rowspan = Integer.parseInt(rowspan_string);
/* 40:   */     }
/* 41:   */   }
/* 42:   */   
/* 43:   */   public int getWidth()
/* 44:   */   {
/* 45:42 */     return this.width;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public boolean isFooter()
/* 49:   */   {
/* 50:46 */     return this.is_footer;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setWidth(int width)
/* 54:   */   {
/* 55:50 */     this.width = width;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int getColspan()
/* 59:   */   {
/* 60:54 */     return this.colspan;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public int getRowspan()
/* 64:   */   {
/* 65:58 */     return this.rowspan;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public int getHeight()
/* 69:   */   {
/* 70:62 */     return this.height;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public void setHeight(int height)
/* 74:   */   {
/* 75:66 */     this.height = height;
/* 76:   */   }
/* 77:   */   
/* 78:   */   public String getName()
/* 79:   */   {
/* 80:70 */     return this.colName;
/* 81:   */   }
/* 82:   */   
/* 83:   */   public String getAlign()
/* 84:   */   {
/* 85:74 */     return this.align;
/* 86:   */   }
/* 87:   */   
/* 88:   */   public String getType()
/* 89:   */   {
/* 90:78 */     return this.type;
/* 91:   */   }
/* 92:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelColumn
 * JD-Core Version:    0.7.0.1
 */