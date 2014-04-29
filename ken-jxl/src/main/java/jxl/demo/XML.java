/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.BufferedWriter;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.OutputStreamWriter;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import jxl.Cell;
/*  10:    */ import jxl.CellType;
/*  11:    */ import jxl.Sheet;
/*  12:    */ import jxl.Workbook;
/*  13:    */ import jxl.format.Alignment;
/*  14:    */ import jxl.format.Border;
/*  15:    */ import jxl.format.BorderLineStyle;
/*  16:    */ import jxl.format.CellFormat;
/*  17:    */ import jxl.format.Colour;
/*  18:    */ import jxl.format.Font;
/*  19:    */ import jxl.format.Format;
/*  20:    */ import jxl.format.Orientation;
/*  21:    */ import jxl.format.Pattern;
/*  22:    */ import jxl.format.ScriptStyle;
/*  23:    */ import jxl.format.UnderlineStyle;
/*  24:    */ import jxl.format.VerticalAlignment;
/*  25:    */ 
/*  26:    */ public class XML
/*  27:    */ {
/*  28:    */   private OutputStream out;
/*  29:    */   private String encoding;
/*  30:    */   private Workbook workbook;
/*  31:    */   
/*  32:    */   public XML(Workbook w, OutputStream out, String enc, boolean f)
/*  33:    */     throws IOException
/*  34:    */   {
/*  35: 75 */     this.encoding = enc;
/*  36: 76 */     this.workbook = w;
/*  37: 77 */     this.out = out;
/*  38: 79 */     if ((this.encoding == null) || (!this.encoding.equals("UnicodeBig"))) {
/*  39: 81 */       this.encoding = "UTF8";
/*  40:    */     }
/*  41: 84 */     if (f) {
/*  42: 86 */       writeFormattedXML();
/*  43:    */     } else {
/*  44: 90 */       writeXML();
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   private void writeXML()
/*  49:    */     throws IOException
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53:102 */       OutputStreamWriter osw = new OutputStreamWriter(this.out, this.encoding);
/*  54:103 */       BufferedWriter bw = new BufferedWriter(osw);
/*  55:    */       
/*  56:105 */       bw.write("<?xml version=\"1.0\" ?>");
/*  57:106 */       bw.newLine();
/*  58:107 */       bw.write("<!DOCTYPE workbook SYSTEM \"workbook.dtd\">");
/*  59:108 */       bw.newLine();
/*  60:109 */       bw.newLine();
/*  61:110 */       bw.write("<workbook>");
/*  62:111 */       bw.newLine();
/*  63:112 */       for (int sheet = 0; sheet < this.workbook.getNumberOfSheets(); sheet++)
/*  64:    */       {
/*  65:114 */         Sheet s = this.workbook.getSheet(sheet);
/*  66:    */         
/*  67:116 */         bw.write("  <sheet>");
/*  68:117 */         bw.newLine();
/*  69:118 */         bw.write("    <name><![CDATA[" + s.getName() + "]]></name>");
/*  70:119 */         bw.newLine();
/*  71:    */         
/*  72:121 */         Cell[] row = (Cell[])null;
/*  73:123 */         for (int i = 0; i < s.getRows(); i++)
/*  74:    */         {
/*  75:125 */           bw.write("    <row number=\"" + i + "\">");
/*  76:126 */           bw.newLine();
/*  77:127 */           row = s.getRow(i);
/*  78:129 */           for (int j = 0; j < row.length; j++) {
/*  79:131 */             if (row[j].getType() != CellType.EMPTY)
/*  80:    */             {
/*  81:133 */               bw.write("      <col number=\"" + j + "\">");
/*  82:134 */               bw.write("<![CDATA[" + row[j].getContents() + "]]>");
/*  83:135 */               bw.write("</col>");
/*  84:136 */               bw.newLine();
/*  85:    */             }
/*  86:    */           }
/*  87:139 */           bw.write("    </row>");
/*  88:140 */           bw.newLine();
/*  89:    */         }
/*  90:142 */         bw.write("  </sheet>");
/*  91:143 */         bw.newLine();
/*  92:    */       }
/*  93:146 */       bw.write("</workbook>");
/*  94:147 */       bw.newLine();
/*  95:    */       
/*  96:149 */       bw.flush();
/*  97:150 */       bw.close();
/*  98:    */     }
/*  99:    */     catch (UnsupportedEncodingException e)
/* 100:    */     {
/* 101:154 */       System.err.println(e.toString());
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   private void writeFormattedXML()
/* 106:    */     throws IOException
/* 107:    */   {
/* 108:    */     try
/* 109:    */     {
/* 110:165 */       OutputStreamWriter osw = new OutputStreamWriter(this.out, this.encoding);
/* 111:166 */       BufferedWriter bw = new BufferedWriter(osw);
/* 112:    */       
/* 113:168 */       bw.write("<?xml version=\"1.0\" ?>");
/* 114:169 */       bw.newLine();
/* 115:170 */       bw.write("<!DOCTYPE workbook SYSTEM \"formatworkbook.dtd\">");
/* 116:171 */       bw.newLine();
/* 117:172 */       bw.newLine();
/* 118:173 */       bw.write("<workbook>");
/* 119:174 */       bw.newLine();
/* 120:175 */       for (int sheet = 0; sheet < this.workbook.getNumberOfSheets(); sheet++)
/* 121:    */       {
/* 122:177 */         Sheet s = this.workbook.getSheet(sheet);
/* 123:    */         
/* 124:179 */         bw.write("  <sheet>");
/* 125:180 */         bw.newLine();
/* 126:181 */         bw.write("    <name><![CDATA[" + s.getName() + "]]></name>");
/* 127:182 */         bw.newLine();
/* 128:    */         
/* 129:184 */         Cell[] row = (Cell[])null;
/* 130:185 */         CellFormat format = null;
/* 131:186 */         Font font = null;
/* 132:188 */         for (int i = 0; i < s.getRows(); i++)
/* 133:    */         {
/* 134:190 */           bw.write("    <row number=\"" + i + "\">");
/* 135:191 */           bw.newLine();
/* 136:192 */           row = s.getRow(i);
/* 137:194 */           for (int j = 0; j < row.length; j++) {
/* 138:197 */             if ((row[j].getType() != CellType.EMPTY) || 
/* 139:198 */               (row[j].getCellFormat() != null))
/* 140:    */             {
/* 141:200 */               format = row[j].getCellFormat();
/* 142:201 */               bw.write("      <col number=\"" + j + "\">");
/* 143:202 */               bw.newLine();
/* 144:203 */               bw.write("        <data>");
/* 145:204 */               bw.write("<![CDATA[" + row[j].getContents() + "]]>");
/* 146:205 */               bw.write("</data>");
/* 147:206 */               bw.newLine();
/* 148:208 */               if (row[j].getCellFormat() != null)
/* 149:    */               {
/* 150:210 */                 bw.write("        <format wrap=\"" + format.getWrap() + "\"");
/* 151:211 */                 bw.newLine();
/* 152:212 */                 bw.write("                align=\"" + 
/* 153:213 */                   format.getAlignment().getDescription() + "\"");
/* 154:214 */                 bw.newLine();
/* 155:215 */                 bw.write("                valign=\"" + 
/* 156:216 */                   format.getVerticalAlignment().getDescription() + "\"");
/* 157:217 */                 bw.newLine();
/* 158:218 */                 bw.write("                orientation=\"" + 
/* 159:219 */                   format.getOrientation().getDescription() + "\"");
/* 160:220 */                 bw.write(">");
/* 161:221 */                 bw.newLine();
/* 162:    */                 
/* 163:    */ 
/* 164:224 */                 font = format.getFont();
/* 165:225 */                 bw.write("          <font name=\"" + font.getName() + "\"");
/* 166:226 */                 bw.newLine();
/* 167:227 */                 bw.write("                point_size=\"" + 
/* 168:228 */                   font.getPointSize() + "\"");
/* 169:229 */                 bw.newLine();
/* 170:230 */                 bw.write("                bold_weight=\"" + 
/* 171:231 */                   font.getBoldWeight() + "\"");
/* 172:232 */                 bw.newLine();
/* 173:233 */                 bw.write("                italic=\"" + font.isItalic() + "\"");
/* 174:234 */                 bw.newLine();
/* 175:235 */                 bw.write("                underline=\"" + 
/* 176:236 */                   font.getUnderlineStyle().getDescription() + "\"");
/* 177:237 */                 bw.newLine();
/* 178:238 */                 bw.write("                colour=\"" + 
/* 179:239 */                   font.getColour().getDescription() + "\"");
/* 180:240 */                 bw.newLine();
/* 181:241 */                 bw.write("                script=\"" + 
/* 182:242 */                   font.getScriptStyle().getDescription() + "\"");
/* 183:243 */                 bw.write(" />");
/* 184:244 */                 bw.newLine();
/* 185:248 */                 if ((format.getBackgroundColour() != Colour.DEFAULT_BACKGROUND) || 
/* 186:249 */                   (format.getPattern() != Pattern.NONE))
/* 187:    */                 {
/* 188:251 */                   bw.write("          <background colour=\"" + 
/* 189:252 */                     format.getBackgroundColour().getDescription() + "\"");
/* 190:253 */                   bw.newLine();
/* 191:254 */                   bw.write("                      pattern=\"" + 
/* 192:255 */                     format.getPattern().getDescription() + "\"");
/* 193:256 */                   bw.write(" />");
/* 194:257 */                   bw.newLine();
/* 195:    */                 }
/* 196:262 */                 if ((format.getBorder(Border.TOP) != BorderLineStyle.NONE) || 
/* 197:263 */                   (format.getBorder(Border.BOTTOM) != BorderLineStyle.NONE) || 
/* 198:264 */                   (format.getBorder(Border.LEFT) != BorderLineStyle.NONE) || 
/* 199:265 */                   (format.getBorder(Border.RIGHT) != BorderLineStyle.NONE))
/* 200:    */                 {
/* 201:268 */                   bw.write("          <border top=\"" + 
/* 202:269 */                     format.getBorder(Border.TOP).getDescription() + "\"");
/* 203:270 */                   bw.newLine();
/* 204:271 */                   bw.write("                  bottom=\"" + 
/* 205:272 */                     format.getBorder(Border.BOTTOM).getDescription() + 
/* 206:273 */                     "\"");
/* 207:274 */                   bw.newLine();
/* 208:275 */                   bw.write("                  left=\"" + 
/* 209:276 */                     format.getBorder(Border.LEFT).getDescription() + "\"");
/* 210:277 */                   bw.newLine();
/* 211:278 */                   bw.write("                  right=\"" + 
/* 212:279 */                     format.getBorder(Border.RIGHT).getDescription() + "\"");
/* 213:280 */                   bw.write(" />");
/* 214:281 */                   bw.newLine();
/* 215:    */                 }
/* 216:285 */                 if (!format.getFormat().getFormatString().equals(""))
/* 217:    */                 {
/* 218:287 */                   bw.write("          <format_string string=\"");
/* 219:288 */                   bw.write(format.getFormat().getFormatString());
/* 220:289 */                   bw.write("\" />");
/* 221:290 */                   bw.newLine();
/* 222:    */                 }
/* 223:293 */                 bw.write("        </format>");
/* 224:294 */                 bw.newLine();
/* 225:    */               }
/* 226:297 */               bw.write("      </col>");
/* 227:298 */               bw.newLine();
/* 228:    */             }
/* 229:    */           }
/* 230:301 */           bw.write("    </row>");
/* 231:302 */           bw.newLine();
/* 232:    */         }
/* 233:304 */         bw.write("  </sheet>");
/* 234:305 */         bw.newLine();
/* 235:    */       }
/* 236:308 */       bw.write("</workbook>");
/* 237:309 */       bw.newLine();
/* 238:    */       
/* 239:311 */       bw.flush();
/* 240:312 */       bw.close();
/* 241:    */     }
/* 242:    */     catch (UnsupportedEncodingException e)
/* 243:    */     {
/* 244:316 */       System.err.println(e.toString());
/* 245:    */     }
/* 246:    */   }
/* 247:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.XML
 * JD-Core Version:    0.7.0.1
 */