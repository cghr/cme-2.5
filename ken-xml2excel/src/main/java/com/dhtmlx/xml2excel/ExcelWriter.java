/*   1:    */ package com.dhtmlx.xml2excel;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ import jxl.SheetSettings;
/*   7:    */ import jxl.Workbook;
/*   8:    */ import jxl.format.Alignment;
/*   9:    */ import jxl.format.Border;
/*  10:    */ import jxl.format.BorderLineStyle;
/*  11:    */ import jxl.format.Colour;
/*  12:    */ import jxl.format.VerticalAlignment;
/*  13:    */ import jxl.write.Label;
/*  14:    */ import jxl.write.Number;
/*  15:    */ import jxl.write.WritableCellFormat;
/*  16:    */ import jxl.write.WritableFont;
/*  17:    */ import jxl.write.WritableImage;
/*  18:    */ import jxl.write.WritableSheet;
/*  19:    */ import jxl.write.WritableWorkbook;
/*  20:    */ import jxl.write.WriteException;
/*  21:    */ import jxl.write.biff.RowsExceededException;
/*  22:    */ 
/*  23:    */ public class ExcelWriter
/*  24:    */ {
/*  25:    */   private WritableWorkbook wb;
/*  26:    */   private WritableSheet sheet;
/*  27:    */   private ExcelColumn[][] cols;
/*  28: 23 */   private int colsNumber = 0;
/*  29:    */   private ExcelXmlParser parser;
/*  30: 26 */   public int headerOffset = 0;
/*  31: 27 */   public int scale = 6;
/*  32: 28 */   public String pathToImgs = "";
/*  33: 30 */   String bgColor = "";
/*  34: 31 */   String lineColor = "";
/*  35: 32 */   String headerTextColor = "";
/*  36: 33 */   String scaleOneColor = "";
/*  37: 34 */   String scaleTwoColor = "";
/*  38: 35 */   String gridTextColor = "";
/*  39: 36 */   String watermarkTextColor = "";
/*  40:    */   private int cols_stat;
/*  41:    */   private int rows_stat;
/*  42:    */   RGBColor colors;
/*  43: 41 */   private String watermark = null;
/*  44:    */   
/*  45:    */   public void generate(String xml, HttpServletResponse resp)
/*  46:    */   {
/*  47: 44 */     this.parser = new ExcelXmlParser();
/*  48:    */     try
/*  49:    */     {
/*  50: 46 */       this.parser.setXML(xml);
/*  51: 47 */       createExcel(resp);
/*  52: 48 */       setColorProfile();
/*  53: 49 */       headerPrint(this.parser);
/*  54: 50 */       rowsPrint(this.parser, resp);
/*  55: 51 */       footerPrint(this.parser);
/*  56: 52 */       insertHeader(this.parser, resp);
/*  57: 53 */       insertFooter(this.parser, resp);
/*  58: 54 */       watermarkPrint(this.parser);
/*  59: 55 */       outputExcel(resp);
/*  60:    */     }
/*  61:    */     catch (Throwable e)
/*  62:    */     {
/*  63: 57 */       e.printStackTrace();
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   private void createExcel(HttpServletResponse resp)
/*  68:    */     throws IOException
/*  69:    */   {
/*  70: 69 */     this.wb = Workbook.createWorkbook(resp.getOutputStream());
/*  71: 70 */     this.sheet = this.wb.createSheet("First Sheet", 0);
/*  72: 71 */     this.colors = new RGBColor();
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void outputExcel(HttpServletResponse resp)
/*  76:    */     throws IOException, WriteException
/*  77:    */   {
/*  78: 75 */     resp.setContentType("application/vnd.ms-excel");
/*  79: 76 */     resp.setCharacterEncoding("UTF-8");
/*  80: 77 */     resp.setHeader("Content-Disposition", "attachment;filename=grid.xls");
/*  81: 78 */     resp.setHeader("Cache-Control", "max-age=0");
/*  82: 79 */     this.wb.write();
/*  83: 80 */     this.wb.close();
/*  84:    */   }
/*  85:    */   
/*  86:    */   private void headerPrint(ExcelXmlParser parser)
/*  87:    */     throws RowsExceededException, WriteException, IOException
/*  88:    */   {
/*  89: 84 */     this.cols = parser.getColumnsInfo("head");
/*  90:    */     
/*  91: 86 */     int[] widths = parser.getWidths();
/*  92: 87 */     this.cols_stat = widths.length;
/*  93:    */     
/*  94: 89 */     int sumWidth = 0;
/*  95: 90 */     for (int i = 0; i < widths.length; i++) {
/*  96: 91 */       sumWidth += widths[i];
/*  97:    */     }
/*  98: 93 */     if (!parser.getWithoutHeader())
/*  99:    */     {
/* 100: 94 */       for (int i = 0; i < this.cols.length; i++)
/* 101:    */       {
/* 102: 95 */         this.sheet.setRowView(i, 450);
/* 103: 96 */         this.sheet.getSettings().setVerticalFreeze(i + 1);
/* 104: 97 */         for (int j = 0; j < this.cols[i].length; j++)
/* 105:    */         {
/* 106: 98 */           this.sheet.setColumnView(j, widths[j] / this.scale);
/* 107: 99 */           WritableFont font = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
/* 108:100 */           font.setColour(this.colors.getColor(this.headerTextColor, this.wb));
/* 109:101 */           WritableCellFormat f = new WritableCellFormat(font);
/* 110:102 */           f.setBackground(this.colors.getColor(this.bgColor, this.wb));
/* 111:103 */           f.setBorder(Border.ALL, BorderLineStyle.THIN, this.colors.getColor(this.lineColor, this.wb));
/* 112:104 */           f.setVerticalAlignment(VerticalAlignment.CENTRE);
/* 113:    */           
/* 114:106 */           f.setAlignment(Alignment.CENTRE);
/* 115:107 */           String name = this.cols[i][j].getName();
/* 116:108 */           Label label = new Label(j, i, name, f);
/* 117:109 */           this.sheet.addCell(label);
/* 118:110 */           this.colsNumber = j;
/* 119:    */         }
/* 120:    */       }
/* 121:113 */       this.headerOffset = this.cols.length;
/* 122:114 */       for (int i = 0; i < this.cols.length; i++) {
/* 123:115 */         for (int j = 0; j < this.cols[i].length; j++)
/* 124:    */         {
/* 125:116 */           int cspan = this.cols[i][j].getColspan();
/* 126:117 */           if (cspan > 0) {
/* 127:118 */             this.sheet.mergeCells(j, i, j + cspan - 1, i);
/* 128:    */           }
/* 129:120 */           int rspan = this.cols[i][j].getRowspan();
/* 130:121 */           if (rspan > 0) {
/* 131:122 */             this.sheet.mergeCells(j, i, j, i + rspan - 1);
/* 132:    */           }
/* 133:    */         }
/* 134:    */       }
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   private void footerPrint(ExcelXmlParser parser)
/* 139:    */     throws RowsExceededException, WriteException, IOException
/* 140:    */   {
/* 141:130 */     this.cols = parser.getColumnsInfo("foot");
/* 142:131 */     if (!parser.getWithoutHeader())
/* 143:    */     {
/* 144:132 */       for (int i = 0; i < this.cols.length; i++)
/* 145:    */       {
/* 146:133 */         this.sheet.setRowView(i + this.headerOffset, 450);
/* 147:134 */         for (int j = 0; j < this.cols[i].length; j++)
/* 148:    */         {
/* 149:135 */           WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
/* 150:136 */           font.setColour(this.colors.getColor(this.headerTextColor, this.wb));
/* 151:137 */           WritableCellFormat f = new WritableCellFormat(font);
/* 152:138 */           f.setBackground(this.colors.getColor(this.bgColor, this.wb));
/* 153:139 */           f.setBorder(Border.ALL, BorderLineStyle.THIN, this.colors.getColor(this.lineColor, this.wb));
/* 154:140 */           f.setVerticalAlignment(VerticalAlignment.CENTRE);
/* 155:    */           
/* 156:142 */           f.setAlignment(Alignment.CENTRE);
/* 157:143 */           String name = this.cols[i][j].getName();
/* 158:144 */           Label label = new Label(j, i + this.headerOffset, name, f);
/* 159:145 */           this.sheet.addCell(label);
/* 160:    */         }
/* 161:    */       }
/* 162:148 */       for (int i = 0; i < this.cols.length; i++) {
/* 163:149 */         for (int j = 0; j < this.cols[i].length; j++)
/* 164:    */         {
/* 165:150 */           int cspan = this.cols[i][j].getColspan();
/* 166:151 */           if (cspan > 0) {
/* 167:152 */             this.sheet.mergeCells(j, this.headerOffset + i, j + cspan - 1, this.headerOffset + i);
/* 168:    */           }
/* 169:154 */           int rspan = this.cols[i][j].getRowspan();
/* 170:155 */           if (rspan > 0) {
/* 171:156 */             this.sheet.mergeCells(j, this.headerOffset + i, j, this.headerOffset + i + rspan - 1);
/* 172:    */           }
/* 173:    */         }
/* 174:    */       }
/* 175:    */     }
/* 176:161 */     this.headerOffset += this.cols.length;
/* 177:    */   }
/* 178:    */   
/* 179:    */   private void watermarkPrint(ExcelXmlParser parser)
/* 180:    */     throws WriteException
/* 181:    */   {
/* 182:165 */     if (this.watermark == null) {
/* 183:165 */       return;
/* 184:    */     }
/* 185:167 */     WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
/* 186:168 */     font.setColour(this.colors.getColor(this.watermarkTextColor, this.wb));
/* 187:169 */     WritableCellFormat f = new WritableCellFormat(font);
/* 188:170 */     f.setBorder(Border.ALL, BorderLineStyle.THIN, this.colors.getColor(this.lineColor, this.wb));
/* 189:171 */     f.setVerticalAlignment(VerticalAlignment.CENTRE);
/* 190:    */     
/* 191:173 */     f.setAlignment(Alignment.CENTRE);
/* 192:174 */     Label label = new Label(0, this.headerOffset, this.watermark, f);
/* 193:175 */     this.sheet.addCell(label);
/* 194:176 */     this.sheet.mergeCells(0, this.headerOffset, this.colsNumber, this.headerOffset);
/* 195:    */   }
/* 196:    */   
/* 197:    */   private void rowsPrint(ExcelXmlParser parser, HttpServletResponse resp)
/* 198:    */     throws WriteException, IOException
/* 199:    */   {
/* 200:181 */     ExcelRow[] rows = parser.getGridContent();
/* 201:182 */     this.rows_stat = rows.length;
/* 202:184 */     for (int i = 0; i < rows.length; i++)
/* 203:    */     {
/* 204:185 */       ExcelCell[] cells = rows[i].getCells();
/* 205:186 */       this.sheet.setRowView(i + this.headerOffset, 400);
/* 206:187 */       for (int j = 0; j < cells.length; j++)
/* 207:    */       {
/* 208:189 */         WritableFont font = new WritableFont(WritableFont.ARIAL, 10, cells[j].getBold().booleanValue() ? WritableFont.BOLD : WritableFont.NO_BOLD, cells[j].getItalic().booleanValue());
/* 209:190 */         if ((!cells[j].getTextColor().equals("")) && (parser.getProfile().equals("full_color"))) {
/* 210:191 */           font.setColour(this.colors.getColor(cells[j].getTextColor(), this.wb));
/* 211:    */         } else {
/* 212:193 */           font.setColour(this.colors.getColor(this.gridTextColor, this.wb));
/* 213:    */         }
/* 214:194 */         WritableCellFormat f = new WritableCellFormat(font);
/* 215:197 */         if ((!cells[j].getBgColor().equals("")) && (parser.getProfile().equals("full_color")))
/* 216:    */         {
/* 217:198 */           Colour col = this.colors.getColor(cells[j].getBgColor(), this.wb);
/* 218:199 */           f.setBackground(col);
/* 219:    */         }
/* 220:    */         else
/* 221:    */         {
/* 222:    */           Colour bg;
/* 223:    */           Colour bg;
/* 224:202 */           if (i % 2 == 1) {
/* 225:203 */             bg = this.colors.getColor(this.scaleTwoColor, this.wb);
/* 226:    */           } else {
/* 227:206 */             bg = this.colors.getColor(this.scaleOneColor, this.wb);
/* 228:    */           }
/* 229:208 */           f.setBackground(bg);
/* 230:    */         }
/* 231:211 */         f.setBorder(Border.ALL, BorderLineStyle.THIN, this.colors.getColor(this.lineColor, this.wb));
/* 232:212 */         f.setVerticalAlignment(VerticalAlignment.CENTRE);
/* 233:    */         
/* 234:214 */         String al = cells[j].getAlign();
/* 235:215 */         if (al == "") {
/* 236:216 */           al = this.cols[0][j].getAlign();
/* 237:    */         }
/* 238:217 */         if (al.equalsIgnoreCase("left")) {
/* 239:218 */           f.setAlignment(Alignment.LEFT);
/* 240:220 */         } else if (al.equalsIgnoreCase("right")) {
/* 241:221 */           f.setAlignment(Alignment.RIGHT);
/* 242:    */         } else {
/* 243:223 */           f.setAlignment(Alignment.CENTRE);
/* 244:    */         }
/* 245:    */         try
/* 246:    */         {
/* 247:227 */           double name = Double.parseDouble(cells[j].getValue());
/* 248:228 */           Number label = new Number(j, i + this.headerOffset, name, f);
/* 249:229 */           this.sheet.addCell(label);
/* 250:    */         }
/* 251:    */         catch (Exception e)
/* 252:    */         {
/* 253:231 */           String name = cells[j].getValue();
/* 254:232 */           Label label = new Label(j, i + this.headerOffset, name, f);
/* 255:233 */           this.sheet.addCell(label);
/* 256:    */         }
/* 257:    */       }
/* 258:    */     }
/* 259:237 */     this.headerOffset += rows.length;
/* 260:    */   }
/* 261:    */   
/* 262:    */   private void insertHeader(ExcelXmlParser parser, HttpServletResponse resp)
/* 263:    */     throws IOException, RowsExceededException
/* 264:    */   {
/* 265:241 */     if (parser.getHeader())
/* 266:    */     {
/* 267:242 */       this.sheet.insertRow(0);
/* 268:243 */       this.sheet.setRowView(0, 5000);
/* 269:244 */       File imgFile = new File(this.pathToImgs + "/header.png");
/* 270:245 */       WritableImage img = new WritableImage(0.0D, 0.0D, this.cols[0].length, 1.0D, imgFile);
/* 271:246 */       this.sheet.addImage(img);
/* 272:247 */       this.headerOffset += 1;
/* 273:    */     }
/* 274:    */   }
/* 275:    */   
/* 276:    */   private void insertFooter(ExcelXmlParser parser, HttpServletResponse resp)
/* 277:    */     throws IOException, RowsExceededException
/* 278:    */   {
/* 279:252 */     if (parser.getFooter().booleanValue())
/* 280:    */     {
/* 281:253 */       this.sheet.setRowView(this.headerOffset, 5000);
/* 282:254 */       File imgFile = new File(this.pathToImgs + "/footer.png");
/* 283:255 */       WritableImage img = new WritableImage(0.0D, this.headerOffset, this.cols[0].length, 1.0D, imgFile);
/* 284:256 */       this.sheet.addImage(img);
/* 285:    */     }
/* 286:    */   }
/* 287:    */   
/* 288:    */   public int getColsStat()
/* 289:    */   {
/* 290:261 */     return this.cols_stat;
/* 291:    */   }
/* 292:    */   
/* 293:    */   public int getRowsStat()
/* 294:    */   {
/* 295:265 */     return this.rows_stat;
/* 296:    */   }
/* 297:    */   
/* 298:    */   private void setColorProfile()
/* 299:    */   {
/* 300:269 */     String profile = this.parser.getProfile();
/* 301:270 */     if ((profile.equalsIgnoreCase("color")) || (profile.equalsIgnoreCase("full_color")))
/* 302:    */     {
/* 303:271 */       this.bgColor = "D1E5FE";
/* 304:272 */       this.lineColor = "A4BED4";
/* 305:273 */       this.headerTextColor = "000000";
/* 306:274 */       this.scaleOneColor = "FFFFFF";
/* 307:275 */       this.scaleTwoColor = "E3EFFF";
/* 308:276 */       this.gridTextColor = "000000";
/* 309:277 */       this.watermarkTextColor = "8b8b8b";
/* 310:    */     }
/* 311:279 */     else if (profile.equalsIgnoreCase("gray"))
/* 312:    */     {
/* 313:280 */       this.bgColor = "E3E3E3";
/* 314:281 */       this.lineColor = "B8B8B8";
/* 315:282 */       this.headerTextColor = "000000";
/* 316:283 */       this.scaleOneColor = "FFFFFF";
/* 317:284 */       this.scaleTwoColor = "EDEDED";
/* 318:285 */       this.gridTextColor = "000000";
/* 319:286 */       this.watermarkTextColor = "8b8b8b";
/* 320:    */     }
/* 321:    */     else
/* 322:    */     {
/* 323:288 */       this.bgColor = "FFFFFF";
/* 324:289 */       this.lineColor = "000000";
/* 325:290 */       this.headerTextColor = "000000";
/* 326:291 */       this.scaleOneColor = "FFFFFF";
/* 327:292 */       this.scaleTwoColor = "FFFFFF";
/* 328:293 */       this.gridTextColor = "000000";
/* 329:294 */       this.watermarkTextColor = "000000";
/* 330:    */     }
/* 331:    */   }
/* 332:    */   
/* 333:    */   public void setWatermark(String mark)
/* 334:    */   {
/* 335:300 */     this.watermark = mark;
/* 336:    */   }
/* 337:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelWriter
 * JD-Core Version:    0.7.0.1
 */