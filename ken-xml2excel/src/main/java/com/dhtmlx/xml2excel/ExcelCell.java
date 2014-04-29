/*  1:   */ package com.dhtmlx.xml2excel;
/*  2:   */ 
/*  3:   */ import org.w3c.dom.Element;
/*  4:   */ import org.w3c.dom.Node;
/*  5:   */ 
/*  6:   */ public class ExcelCell
/*  7:   */ {
/*  8: 8 */   private String value = "";
/*  9: 9 */   private String bgColor = "";
/* 10:10 */   private String textColor = "";
/* 11:11 */   private String bold = "";
/* 12:12 */   private String italic = "";
/* 13:13 */   private String align = "";
/* 14:   */   
/* 15:   */   public void parse(Node parent)
/* 16:   */   {
/* 17:17 */     this.value = parent.getFirstChild().getNodeValue();
/* 18:18 */     Element el = (Element)parent;
/* 19:19 */     this.bgColor = (el.hasAttribute("bgColor") ? el.getAttribute("bgColor") : "");
/* 20:20 */     this.textColor = (el.hasAttribute("textColor") ? el.getAttribute("textColor") : "");
/* 21:21 */     this.bold = (el.hasAttribute("bold") ? el.getAttribute("bold") : "");
/* 22:22 */     this.italic = (el.hasAttribute("italic") ? el.getAttribute("italic") : "");
/* 23:23 */     this.align = (el.hasAttribute("align") ? el.getAttribute("align") : "");
/* 24:   */   }
/* 25:   */   
/* 26:   */   public String getValue()
/* 27:   */   {
/* 28:27 */     return this.value;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String getBgColor()
/* 32:   */   {
/* 33:31 */     return this.bgColor;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getTextColor()
/* 37:   */   {
/* 38:35 */     return this.textColor;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public Boolean getBold()
/* 42:   */   {
/* 43:39 */     if (this.bold.equals("bold")) {
/* 44:40 */       return Boolean.valueOf(true);
/* 45:   */     }
/* 46:42 */     return Boolean.valueOf(false);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public Boolean getItalic()
/* 50:   */   {
/* 51:46 */     if (this.italic.equals("italic")) {
/* 52:47 */       return Boolean.valueOf(true);
/* 53:   */     }
/* 54:49 */     return Boolean.valueOf(false);
/* 55:   */   }
/* 56:   */   
/* 57:   */   public String getAlign()
/* 58:   */   {
/* 59:53 */     return this.align;
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelCell
 * JD-Core Version:    0.7.0.1
 */