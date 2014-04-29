/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.net.MalformedURLException;
/*   5:    */ import java.net.URL;
/*   6:    */ import jxl.CellReferenceHelper;
/*   7:    */ import jxl.Hyperlink;
/*   8:    */ import jxl.Range;
/*   9:    */ import jxl.Sheet;
/*  10:    */ import jxl.WorkbookSettings;
/*  11:    */ import jxl.biff.IntegerHelper;
/*  12:    */ import jxl.biff.RecordData;
/*  13:    */ import jxl.biff.SheetRangeImpl;
/*  14:    */ import jxl.biff.StringHelper;
/*  15:    */ import jxl.common.Logger;
/*  16:    */ 
/*  17:    */ public class HyperlinkRecord
/*  18:    */   extends RecordData
/*  19:    */   implements Hyperlink
/*  20:    */ {
/*  21: 47 */   private static Logger logger = Logger.getLogger(HyperlinkRecord.class);
/*  22:    */   private int firstRow;
/*  23:    */   private int lastRow;
/*  24:    */   private int firstColumn;
/*  25:    */   private int lastColumn;
/*  26:    */   private URL url;
/*  27:    */   private File file;
/*  28:    */   private String location;
/*  29:    */   private SheetRangeImpl range;
/*  30:    */   private LinkType linkType;
/*  31: 96 */   private static final LinkType urlLink = new LinkType(null);
/*  32: 97 */   private static final LinkType fileLink = new LinkType(null);
/*  33: 98 */   private static final LinkType workbookLink = new LinkType(null);
/*  34: 99 */   private static final LinkType unknown = new LinkType(null);
/*  35:    */   
/*  36:    */   HyperlinkRecord(Record t, Sheet s, WorkbookSettings ws)
/*  37:    */   {
/*  38:110 */     super(t);
/*  39:    */     
/*  40:112 */     this.linkType = unknown;
/*  41:    */     
/*  42:114 */     byte[] data = getRecord().getData();
/*  43:    */     
/*  44:    */ 
/*  45:117 */     this.firstRow = IntegerHelper.getInt(data[0], data[1]);
/*  46:118 */     this.lastRow = IntegerHelper.getInt(data[2], data[3]);
/*  47:119 */     this.firstColumn = IntegerHelper.getInt(data[4], data[5]);
/*  48:120 */     this.lastColumn = IntegerHelper.getInt(data[6], data[7]);
/*  49:121 */     this.range = new SheetRangeImpl(s, 
/*  50:122 */       this.firstColumn, this.firstRow, 
/*  51:123 */       this.lastColumn, this.lastRow);
/*  52:    */     
/*  53:125 */     int options = IntegerHelper.getInt(data[28], data[29], data[30], data[31]);
/*  54:    */     
/*  55:127 */     boolean description = (options & 0x14) != 0;
/*  56:128 */     int startpos = 32;
/*  57:129 */     int descbytes = 0;
/*  58:130 */     if (description)
/*  59:    */     {
/*  60:132 */       int descchars = IntegerHelper.getInt(
/*  61:133 */         data[startpos], data[(startpos + 1)], 
/*  62:134 */         data[(startpos + 2)], data[(startpos + 3)]);
/*  63:135 */       descbytes = descchars * 2 + 4;
/*  64:    */     }
/*  65:138 */     startpos += descbytes;
/*  66:    */     
/*  67:140 */     boolean targetFrame = (options & 0x80) != 0;
/*  68:141 */     int targetbytes = 0;
/*  69:142 */     if (targetFrame)
/*  70:    */     {
/*  71:144 */       int targetchars = IntegerHelper.getInt(
/*  72:145 */         data[startpos], data[(startpos + 1)], 
/*  73:146 */         data[(startpos + 2)], data[(startpos + 3)]);
/*  74:147 */       targetbytes = targetchars * 2 + 4;
/*  75:    */     }
/*  76:150 */     startpos += targetbytes;
/*  77:153 */     if ((options & 0x3) == 3)
/*  78:    */     {
/*  79:155 */       this.linkType = urlLink;
/*  80:158 */       if (data[startpos] == 3) {
/*  81:160 */         this.linkType = fileLink;
/*  82:    */       }
/*  83:    */     }
/*  84:163 */     else if ((options & 0x1) != 0)
/*  85:    */     {
/*  86:165 */       this.linkType = fileLink;
/*  87:167 */       if (data[startpos] == -32) {
/*  88:169 */         this.linkType = urlLink;
/*  89:    */       }
/*  90:    */     }
/*  91:172 */     else if ((options & 0x8) != 0)
/*  92:    */     {
/*  93:174 */       this.linkType = workbookLink;
/*  94:    */     }
/*  95:178 */     if (this.linkType == urlLink)
/*  96:    */     {
/*  97:180 */       String urlString = null;
/*  98:    */       try
/*  99:    */       {
/* 100:183 */         startpos += 16;
/* 101:    */         
/* 102:    */ 
/* 103:186 */         int bytes = IntegerHelper.getInt(data[startpos], 
/* 104:187 */           data[(startpos + 1)], 
/* 105:188 */           data[(startpos + 2)], 
/* 106:189 */           data[(startpos + 3)]);
/* 107:    */         
/* 108:191 */         urlString = StringHelper.getUnicodeString(data, bytes / 2 - 1, 
/* 109:192 */           startpos + 4);
/* 110:193 */         this.url = new URL(urlString);
/* 111:    */       }
/* 112:    */       catch (MalformedURLException e)
/* 113:    */       {
/* 114:197 */         logger.warn("URL " + urlString + " is malformed.  Trying a file");
/* 115:    */         try
/* 116:    */         {
/* 117:200 */           this.linkType = fileLink;
/* 118:201 */           this.file = new File(urlString);
/* 119:    */         }
/* 120:    */         catch (Exception e3)
/* 121:    */         {
/* 122:205 */           logger.warn("Cannot set to file.  Setting a default URL");
/* 123:    */           try
/* 124:    */           {
/* 125:210 */             this.linkType = urlLink;
/* 126:211 */             this.url = new URL("http://www.andykhan.com/jexcelapi/index.html");
/* 127:    */           }
/* 128:    */           catch (MalformedURLException localMalformedURLException1) {}
/* 129:    */         }
/* 130:    */       }
/* 131:    */       catch (Throwable e)
/* 132:    */       {
/* 133:221 */         StringBuffer sb1 = new StringBuffer();
/* 134:222 */         StringBuffer sb2 = new StringBuffer();
/* 135:223 */         CellReferenceHelper.getCellReference(this.firstColumn, this.firstRow, sb1);
/* 136:224 */         CellReferenceHelper.getCellReference(this.lastColumn, this.lastRow, sb2);
/* 137:225 */         sb1.insert(0, "Exception when parsing URL ");
/* 138:226 */         sb1.append('"').append(sb2.toString()).append("\".  Using default.");
/* 139:227 */         logger.warn(sb1, e);
/* 140:    */         try
/* 141:    */         {
/* 142:232 */           this.url = new URL("http://www.andykhan.com/jexcelapi/index.html");
/* 143:    */         }
/* 144:    */         catch (MalformedURLException localMalformedURLException2) {}
/* 145:    */       }
/* 146:    */     }
/* 147:240 */     else if (this.linkType == fileLink)
/* 148:    */     {
/* 149:    */       try
/* 150:    */       {
/* 151:244 */         startpos += 16;
/* 152:    */         
/* 153:    */ 
/* 154:    */ 
/* 155:248 */         int upLevelCount = IntegerHelper.getInt(data[startpos], 
/* 156:249 */           data[(startpos + 1)]);
/* 157:250 */         int chars = IntegerHelper.getInt(data[(startpos + 2)], 
/* 158:251 */           data[(startpos + 3)], 
/* 159:252 */           data[(startpos + 4)], 
/* 160:253 */           data[(startpos + 5)]);
/* 161:254 */         String fileName = StringHelper.getString(data, chars - 1, 
/* 162:255 */           startpos + 6, ws);
/* 163:    */         
/* 164:257 */         StringBuffer sb = new StringBuffer();
/* 165:259 */         for (int i = 0; i < upLevelCount; i++) {
/* 166:261 */           sb.append("..\\");
/* 167:    */         }
/* 168:264 */         sb.append(fileName);
/* 169:    */         
/* 170:266 */         this.file = new File(sb.toString());
/* 171:    */       }
/* 172:    */       catch (Throwable e)
/* 173:    */       {
/* 174:270 */         logger.warn("Exception when parsing file " + 
/* 175:271 */           e.getClass().getName() + ".");
/* 176:272 */         this.file = new File(".");
/* 177:    */       }
/* 178:    */     }
/* 179:275 */     else if (this.linkType == workbookLink)
/* 180:    */     {
/* 181:277 */       int chars = IntegerHelper.getInt(data[32], data[33], data[34], data[35]);
/* 182:278 */       this.location = StringHelper.getUnicodeString(data, chars - 1, 36);
/* 183:    */     }
/* 184:    */     else
/* 185:    */     {
/* 186:283 */       logger.warn("Cannot determine link type");
/* 187:284 */       return;
/* 188:    */     }
/* 189:    */   }
/* 190:    */   
/* 191:    */   public boolean isFile()
/* 192:    */   {
/* 193:295 */     return this.linkType == fileLink;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public boolean isURL()
/* 197:    */   {
/* 198:305 */     return this.linkType == urlLink;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean isLocation()
/* 202:    */   {
/* 203:315 */     return this.linkType == workbookLink;
/* 204:    */   }
/* 205:    */   
/* 206:    */   public int getRow()
/* 207:    */   {
/* 208:325 */     return this.firstRow;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public int getColumn()
/* 212:    */   {
/* 213:335 */     return this.firstColumn;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public int getLastRow()
/* 217:    */   {
/* 218:345 */     return this.lastRow;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public int getLastColumn()
/* 222:    */   {
/* 223:355 */     return this.lastColumn;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public URL getURL()
/* 227:    */   {
/* 228:365 */     return this.url;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public File getFile()
/* 232:    */   {
/* 233:375 */     return this.file;
/* 234:    */   }
/* 235:    */   
/* 236:    */   public Record getRecord()
/* 237:    */   {
/* 238:385 */     return super.getRecord();
/* 239:    */   }
/* 240:    */   
/* 241:    */   public Range getRange()
/* 242:    */   {
/* 243:397 */     return this.range;
/* 244:    */   }
/* 245:    */   
/* 246:    */   public String getLocation()
/* 247:    */   {
/* 248:407 */     return this.location;
/* 249:    */   }
/* 250:    */   
/* 251:    */   private static class LinkType {}
/* 252:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.HyperlinkRecord
 * JD-Core Version:    0.7.0.1
 */