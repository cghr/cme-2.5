/*   1:    */ package jxl.demo;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileOutputStream;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import jxl.Cell;
/*   8:    */ import jxl.Range;
/*   9:    */ import jxl.Workbook;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ public class Demo
/*  13:    */ {
/*  14:    */   private static final int CSVFormat = 13;
/*  15:    */   private static final int XMLFormat = 14;
/*  16: 45 */   private static Logger logger = Logger.getLogger(Demo.class);
/*  17:    */   
/*  18:    */   private static void displayHelp()
/*  19:    */   {
/*  20: 52 */     System.err.println(
/*  21: 53 */       "Command format:  Demo [-unicode] [-csv] [-hide] excelfile");
/*  22: 54 */     System.err.println("                 Demo -xml [-format]  excelfile");
/*  23: 55 */     System.err.println("                 Demo -readwrite|-rw excelfile output");
/*  24: 56 */     System.err.println("                 Demo -biffdump | -bd | -wa | -write | -formulas | -features | -escher | -escherdg excelfile");
/*  25: 57 */     System.err.println("                 Demo -ps excelfile [property] [output]");
/*  26: 58 */     System.err.println("                 Demo -version | -logtest | -h | -help");
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static void main(String[] args)
/*  30:    */   {
/*  31: 71 */     if (args.length == 0)
/*  32:    */     {
/*  33: 73 */       displayHelp();
/*  34: 74 */       System.exit(1);
/*  35:    */     }
/*  36: 77 */     if ((args[0].equals("-help")) || (args[0].equals("-h")))
/*  37:    */     {
/*  38: 79 */       displayHelp();
/*  39: 80 */       System.exit(1);
/*  40:    */     }
/*  41: 83 */     if (args[0].equals("-version"))
/*  42:    */     {
/*  43: 85 */       System.out.println("v" + Workbook.getVersion());
/*  44: 86 */       System.exit(0);
/*  45:    */     }
/*  46: 89 */     if (args[0].equals("-logtest"))
/*  47:    */     {
/*  48: 91 */       logger.debug("A sample \"debug\" message");
/*  49: 92 */       logger.info("A sample \"info\" message");
/*  50: 93 */       logger.warn("A sample \"warning\" message");
/*  51: 94 */       logger.error("A sample \"error\" message");
/*  52: 95 */       logger.fatal("A sample \"fatal\" message");
/*  53: 96 */       System.exit(0);
/*  54:    */     }
/*  55: 99 */     boolean write = false;
/*  56:100 */     boolean readwrite = false;
/*  57:101 */     boolean formulas = false;
/*  58:102 */     boolean biffdump = false;
/*  59:103 */     boolean jxlversion = false;
/*  60:104 */     boolean propertysets = false;
/*  61:105 */     boolean features = false;
/*  62:106 */     boolean escher = false;
/*  63:107 */     boolean escherdg = false;
/*  64:108 */     String file = args[0];
/*  65:109 */     String outputFile = null;
/*  66:110 */     String propertySet = null;
/*  67:112 */     if (args[0].equals("-write"))
/*  68:    */     {
/*  69:114 */       write = true;
/*  70:115 */       file = args[1];
/*  71:    */     }
/*  72:117 */     else if (args[0].equals("-formulas"))
/*  73:    */     {
/*  74:119 */       formulas = true;
/*  75:120 */       file = args[1];
/*  76:    */     }
/*  77:122 */     else if (args[0].equals("-features"))
/*  78:    */     {
/*  79:124 */       features = true;
/*  80:125 */       file = args[1];
/*  81:    */     }
/*  82:127 */     else if (args[0].equals("-escher"))
/*  83:    */     {
/*  84:129 */       escher = true;
/*  85:130 */       file = args[1];
/*  86:    */     }
/*  87:132 */     else if (args[0].equals("-escherdg"))
/*  88:    */     {
/*  89:134 */       escherdg = true;
/*  90:135 */       file = args[1];
/*  91:    */     }
/*  92:137 */     else if ((args[0].equals("-biffdump")) || (args[0].equals("-bd")))
/*  93:    */     {
/*  94:139 */       biffdump = true;
/*  95:140 */       file = args[1];
/*  96:    */     }
/*  97:142 */     else if (args[0].equals("-wa"))
/*  98:    */     {
/*  99:144 */       jxlversion = true;
/* 100:145 */       file = args[1];
/* 101:    */     }
/* 102:147 */     else if (args[0].equals("-ps"))
/* 103:    */     {
/* 104:149 */       propertysets = true;
/* 105:150 */       file = args[1];
/* 106:152 */       if (args.length > 2) {
/* 107:154 */         propertySet = args[2];
/* 108:    */       }
/* 109:157 */       if (args.length == 4) {
/* 110:159 */         outputFile = args[3];
/* 111:    */       }
/* 112:    */     }
/* 113:162 */     else if ((args[0].equals("-readwrite")) || (args[0].equals("-rw")))
/* 114:    */     {
/* 115:164 */       readwrite = true;
/* 116:165 */       file = args[1];
/* 117:166 */       outputFile = args[2];
/* 118:    */     }
/* 119:    */     else
/* 120:    */     {
/* 121:170 */       file = args[(args.length - 1)];
/* 122:    */     }
/* 123:173 */     String encoding = "UTF8";
/* 124:174 */     int format = 13;
/* 125:175 */     boolean formatInfo = false;
/* 126:176 */     boolean hideCells = false;
/* 127:178 */     if ((!write) && 
/* 128:179 */       (!readwrite) && 
/* 129:180 */       (!formulas) && 
/* 130:181 */       (!biffdump) && 
/* 131:182 */       (!jxlversion) && 
/* 132:183 */       (!propertysets) && 
/* 133:184 */       (!features) && 
/* 134:185 */       (!escher) && 
/* 135:186 */       (!escherdg)) {
/* 136:188 */       for (int i = 0; i < args.length - 1; i++) {
/* 137:190 */         if (args[i].equals("-unicode"))
/* 138:    */         {
/* 139:192 */           encoding = "UnicodeBig";
/* 140:    */         }
/* 141:194 */         else if (args[i].equals("-xml"))
/* 142:    */         {
/* 143:196 */           format = 14;
/* 144:    */         }
/* 145:198 */         else if (args[i].equals("-csv"))
/* 146:    */         {
/* 147:200 */           format = 13;
/* 148:    */         }
/* 149:202 */         else if (args[i].equals("-format"))
/* 150:    */         {
/* 151:204 */           formatInfo = true;
/* 152:    */         }
/* 153:206 */         else if (args[i].equals("-hide"))
/* 154:    */         {
/* 155:208 */           hideCells = true;
/* 156:    */         }
/* 157:    */         else
/* 158:    */         {
/* 159:212 */           System.err.println(
/* 160:213 */             "Command format:  CSV [-unicode] [-xml|-csv] excelfile");
/* 161:214 */           System.exit(1);
/* 162:    */         }
/* 163:    */       }
/* 164:    */     }
/* 165:    */     try
/* 166:    */     {
/* 167:221 */       if (write)
/* 168:    */       {
/* 169:223 */         Write w = new Write(file);
/* 170:224 */         w.write();
/* 171:    */       }
/* 172:226 */       else if (readwrite)
/* 173:    */       {
/* 174:228 */         ReadWrite rw = new ReadWrite(file, outputFile);
/* 175:229 */         rw.readWrite();
/* 176:    */       }
/* 177:231 */       else if (formulas)
/* 178:    */       {
/* 179:233 */         Workbook w = Workbook.getWorkbook(new File(file));
/* 180:234 */         Formulas f = new Formulas(w, System.out, encoding);
/* 181:235 */         w.close();
/* 182:    */       }
/* 183:237 */       else if (features)
/* 184:    */       {
/* 185:239 */         Workbook w = Workbook.getWorkbook(new File(file));
/* 186:240 */         Features f = new Features(w, System.out, encoding);
/* 187:241 */         w.close();
/* 188:    */       }
/* 189:243 */       else if (escher)
/* 190:    */       {
/* 191:245 */         Workbook w = Workbook.getWorkbook(new File(file));
/* 192:246 */         Escher f = new Escher(w, System.out, encoding);
/* 193:247 */         w.close();
/* 194:    */       }
/* 195:249 */       else if (escherdg)
/* 196:    */       {
/* 197:251 */         Workbook w = Workbook.getWorkbook(new File(file));
/* 198:252 */         EscherDrawingGroup f = new EscherDrawingGroup(w, System.out, encoding);
/* 199:253 */         w.close();
/* 200:    */       }
/* 201:    */       else
/* 202:    */       {
/* 203:    */         BiffDump bd;
/* 204:255 */         if (biffdump)
/* 205:    */         {
/* 206:257 */           bd = new BiffDump(new File(file), System.out);
/* 207:    */         }
/* 208:    */         else
/* 209:    */         {
/* 210:    */           WriteAccess bd;
/* 211:259 */           if (jxlversion)
/* 212:    */           {
/* 213:261 */             bd = new WriteAccess(new File(file));
/* 214:    */           }
/* 215:    */           else
/* 216:    */           {
/* 217:    */             PropertySetsReader psr;
/* 218:263 */             if (propertysets)
/* 219:    */             {
/* 220:265 */               OutputStream os = System.out;
/* 221:266 */               if (outputFile != null) {
/* 222:268 */                 os = new FileOutputStream(outputFile);
/* 223:    */               }
/* 224:270 */               psr = new PropertySetsReader(new File(file), 
/* 225:271 */                 propertySet, 
/* 226:272 */                 os);
/* 227:    */             }
/* 228:    */             else
/* 229:    */             {
/* 230:276 */               Workbook w = Workbook.getWorkbook(new File(file));
/* 231:    */               CSV csv;
/* 232:280 */               if (format == 13) {
/* 233:282 */                 csv = new CSV(w, System.out, encoding, hideCells);
/* 234:284 */               } else if (format == 14) {
/* 235:286 */                 csv = new XML(w, System.out, encoding, formatInfo);
/* 236:    */               }
/* 237:289 */               w.close();
/* 238:    */             }
/* 239:    */           }
/* 240:    */         }
/* 241:    */       }
/* 242:    */     }
/* 243:    */     catch (Throwable t)
/* 244:    */     {
/* 245:294 */       System.out.println(t.toString());
/* 246:295 */       t.printStackTrace();
/* 247:    */     }
/* 248:    */   }
/* 249:    */   
/* 250:    */   private static void findTest(Workbook w)
/* 251:    */   {
/* 252:304 */     logger.info("Find test");
/* 253:    */     
/* 254:306 */     Cell c = w.findCellByName("named1");
/* 255:307 */     if (c != null) {
/* 256:309 */       logger.info("named1 contents:  " + c.getContents());
/* 257:    */     }
/* 258:312 */     c = w.findCellByName("named2");
/* 259:313 */     if (c != null) {
/* 260:315 */       logger.info("named2 contents:  " + c.getContents());
/* 261:    */     }
/* 262:318 */     c = w.findCellByName("namedrange");
/* 263:319 */     if (c != null) {
/* 264:321 */       logger.info("named2 contents:  " + c.getContents());
/* 265:    */     }
/* 266:324 */     Range[] range = w.findByName("namedrange");
/* 267:325 */     if (range != null)
/* 268:    */     {
/* 269:327 */       c = range[0].getTopLeft();
/* 270:328 */       logger.info("namedrange top left contents:  " + c.getContents());
/* 271:    */       
/* 272:330 */       c = range[0].getBottomRight();
/* 273:331 */       logger.info("namedrange bottom right contents:  " + c.getContents());
/* 274:    */     }
/* 275:334 */     range = w.findByName("nonadjacentrange");
/* 276:335 */     if (range != null) {
/* 277:337 */       for (int i = 0; i < range.length; i++)
/* 278:    */       {
/* 279:339 */         c = range[i].getTopLeft();
/* 280:340 */         logger.info("nonadjacent top left contents:  " + c.getContents());
/* 281:    */         
/* 282:342 */         c = range[i].getBottomRight();
/* 283:343 */         logger.info("nonadjacent bottom right contents:  " + c.getContents());
/* 284:    */       }
/* 285:    */     }
/* 286:347 */     range = w.findByName("horizontalnonadjacentrange");
/* 287:348 */     if (range != null) {
/* 288:350 */       for (int i = 0; i < range.length; i++)
/* 289:    */       {
/* 290:352 */         c = range[i].getTopLeft();
/* 291:353 */         logger.info("horizontalnonadjacent top left contents:  " + 
/* 292:354 */           c.getContents());
/* 293:    */         
/* 294:356 */         c = range[i].getBottomRight();
/* 295:357 */         logger.info("horizontalnonadjacent bottom right contents:  " + 
/* 296:358 */           c.getContents());
/* 297:    */       }
/* 298:    */     }
/* 299:    */   }
/* 300:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.Demo
 * JD-Core Version:    0.7.0.1
 */