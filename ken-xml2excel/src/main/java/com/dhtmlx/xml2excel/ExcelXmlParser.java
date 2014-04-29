/*   1:    */ package com.dhtmlx.xml2excel;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.StringReader;
/*   5:    */ import javax.xml.parsers.DocumentBuilder;
/*   6:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*   7:    */ import javax.xml.parsers.ParserConfigurationException;
/*   8:    */ import javax.xml.xpath.XPath;
/*   9:    */ import javax.xml.xpath.XPathConstants;
/*  10:    */ import javax.xml.xpath.XPathExpressionException;
/*  11:    */ import javax.xml.xpath.XPathFactory;
/*  12:    */ import org.w3c.dom.DOMException;
/*  13:    */ import org.w3c.dom.Document;
/*  14:    */ import org.w3c.dom.Element;
/*  15:    */ import org.w3c.dom.NodeList;
/*  16:    */ import org.xml.sax.InputSource;
/*  17:    */ import org.xml.sax.SAXException;
/*  18:    */ 
/*  19:    */ public class ExcelXmlParser
/*  20:    */ {
/*  21:    */   private Element root;
/*  22:    */   private ExcelColumn[][] columns;
/*  23:    */   private ExcelRow[] rows;
/*  24:    */   private int[] widths;
/*  25: 21 */   private Boolean header = Boolean.valueOf(false);
/*  26: 22 */   private Boolean footer = Boolean.valueOf(false);
/*  27: 23 */   private Boolean without_header = Boolean.valueOf(false);
/*  28: 24 */   private String profile = "gray";
/*  29:    */   
/*  30:    */   public void setXML(String xml)
/*  31:    */     throws IOException, DOMException, ParserConfigurationException, SAXException
/*  32:    */   {
/*  33: 28 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  34: 29 */     DocumentBuilder db = dbf.newDocumentBuilder();
/*  35:    */     
/*  36: 31 */     Document dom = null;
/*  37:    */     try
/*  38:    */     {
/*  39: 33 */       dom = db.parse(new InputSource(new StringReader(xml)));
/*  40:    */     }
/*  41:    */     catch (SAXException se)
/*  42:    */     {
/*  43: 35 */       se.printStackTrace();
/*  44:    */     }
/*  45:    */     catch (IOException ioe)
/*  46:    */     {
/*  47: 37 */       ioe.printStackTrace();
/*  48:    */     }
/*  49: 39 */     this.root = dom.getDocumentElement();
/*  50:    */     
/*  51: 41 */     String header_text = this.root.getAttribute("header");
/*  52: 42 */     if ((header_text != null) && (header_text.equalsIgnoreCase("true"))) {
/*  53: 43 */       this.header = Boolean.valueOf(true);
/*  54:    */     }
/*  55: 45 */     String footer_text = this.root.getAttribute("footer");
/*  56: 46 */     if ((footer_text != null) && (footer_text.equalsIgnoreCase("true"))) {
/*  57: 47 */       this.footer = Boolean.valueOf(true);
/*  58:    */     }
/*  59: 49 */     String profile_text = this.root.getAttribute("profile");
/*  60: 50 */     if (profile_text != null) {
/*  61: 51 */       this.profile = profile_text;
/*  62:    */     }
/*  63: 53 */     String w_header = this.root.getAttribute("without_header");
/*  64: 54 */     if ((w_header != null) && (w_header.equalsIgnoreCase("true"))) {
/*  65: 55 */       this.without_header = Boolean.valueOf(true);
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public ExcelColumn[][] getColumnsInfo(String mode)
/*  70:    */   {
/*  71: 59 */     ExcelColumn[] colLine = (ExcelColumn[])null;
/*  72:    */     
/*  73: 61 */     XPathFactory xpathFactory = XPathFactory.newInstance();
/*  74: 62 */     XPath xpath = xpathFactory.newXPath();
/*  75:    */     try
/*  76:    */     {
/*  77: 64 */       String path = "/rows/".concat(mode).concat("/columns");
/*  78: 65 */       NodeList n1 = (NodeList)xpath.evaluate(path, this.root, 
/*  79: 66 */         XPathConstants.NODESET);
/*  80: 68 */       if ((n1 != null) && (n1.getLength() > 0))
/*  81:    */       {
/*  82: 70 */         this.columns = new ExcelColumn[n1.getLength()][];
/*  83: 71 */         for (int i = 0; i < n1.getLength(); i++)
/*  84:    */         {
/*  85: 72 */           Element cols = (Element)n1.item(i);
/*  86: 73 */           NodeList n2 = cols.getElementsByTagName("column");
/*  87: 74 */           if ((n2 != null) && (n2.getLength() > 0))
/*  88:    */           {
/*  89: 75 */             colLine = new ExcelColumn[n2.getLength()];
/*  90: 76 */             for (int j = 0; j < n2.getLength(); j++)
/*  91:    */             {
/*  92: 77 */               Element col_xml = (Element)n2.item(j);
/*  93: 78 */               ExcelColumn col = new ExcelColumn();
/*  94: 79 */               col.parse(col_xml);
/*  95: 80 */               colLine[j] = col;
/*  96:    */             }
/*  97:    */           }
/*  98: 83 */           this.columns[i] = colLine;
/*  99:    */         }
/* 100:    */       }
/* 101:    */     }
/* 102:    */     catch (XPathExpressionException e)
/* 103:    */     {
/* 104: 88 */       e.printStackTrace();
/* 105:    */     }
/* 106: 91 */     createWidthsArray();
/* 107: 92 */     optimizeColumns();
/* 108: 93 */     return this.columns;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private void createWidthsArray()
/* 112:    */   {
/* 113: 97 */     this.widths = new int[this.columns[0].length];
/* 114: 98 */     for (int i = 0; i < this.columns[0].length; i++) {
/* 115: 99 */       this.widths[i] = this.columns[0][i].getWidth();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   private void optimizeColumns()
/* 120:    */   {
/* 121:104 */     for (int i = 1; i < this.columns.length; i++) {
/* 122:105 */       for (int j = 0; j < this.columns[i].length; j++) {
/* 123:106 */         this.columns[i][j].setWidth(this.columns[0][j].getWidth());
/* 124:    */       }
/* 125:    */     }
/* 126:109 */     for (int i = 0; i < this.columns.length; i++) {
/* 127:110 */       for (int j = 0; j < this.columns[i].length; j++)
/* 128:    */       {
/* 129:111 */         if (this.columns[i][j].getColspan() > 0) {
/* 130:112 */           for (int k = j + 1; k < j + this.columns[i][j].getColspan(); k++)
/* 131:    */           {
/* 132:113 */             this.columns[i][j].setWidth(this.columns[i][j].getWidth() + this.columns[i][k].getWidth());
/* 133:114 */             this.columns[i][k].setWidth(0);
/* 134:    */           }
/* 135:    */         }
/* 136:117 */         if (this.columns[i][j].getRowspan() > 0) {
/* 137:118 */           for (int k = i + 1; k < i + this.columns[i][j].getRowspan(); k++)
/* 138:    */           {
/* 139:119 */             this.columns[i][j].setHeight(this.columns[i][j].getHeight() + 1);
/* 140:120 */             this.columns[k][j].setHeight(0);
/* 141:    */           }
/* 142:    */         }
/* 143:    */       }
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   public ExcelRow[] getGridContent()
/* 148:    */   {
/* 149:128 */     NodeList nodes = this.root.getElementsByTagName("row");
/* 150:129 */     if ((nodes != null) && (nodes.getLength() > 0))
/* 151:    */     {
/* 152:130 */       this.rows = new ExcelRow[nodes.getLength()];
/* 153:131 */       for (int i = 0; i < nodes.getLength(); i++)
/* 154:    */       {
/* 155:132 */         this.rows[i] = new ExcelRow();
/* 156:133 */         this.rows[i].parse(nodes.item(i));
/* 157:    */       }
/* 158:    */     }
/* 159:136 */     return this.rows;
/* 160:    */   }
/* 161:    */   
/* 162:    */   public int[] getWidths()
/* 163:    */   {
/* 164:140 */     return this.widths;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public boolean getHeader()
/* 168:    */   {
/* 169:144 */     return this.header.booleanValue();
/* 170:    */   }
/* 171:    */   
/* 172:    */   public Boolean getFooter()
/* 173:    */   {
/* 174:148 */     return this.footer;
/* 175:    */   }
/* 176:    */   
/* 177:    */   public String getProfile()
/* 178:    */   {
/* 179:152 */     return this.profile;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public boolean getWithoutHeader()
/* 183:    */   {
/* 184:156 */     return this.without_header.booleanValue();
/* 185:    */   }
/* 186:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelXmlParser
 * JD-Core Version:    0.7.0.1
 */