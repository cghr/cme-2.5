/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.WorkbookSettings;
/*   7:    */ import jxl.biff.IntegerHelper;
/*   8:    */ import jxl.common.Logger;
/*   9:    */ import jxl.write.biff.File;
/*  10:    */ 
/*  11:    */ public class SheetDrawingWriter
/*  12:    */ {
/*  13: 41 */   private static Logger logger = Logger.getLogger(SheetDrawingWriter.class);
/*  14:    */   private ArrayList drawings;
/*  15:    */   private boolean drawingsModified;
/*  16:    */   private Chart[] charts;
/*  17:    */   private WorkbookSettings workbookSettings;
/*  18:    */   
/*  19:    */   public SheetDrawingWriter(WorkbookSettings ws)
/*  20:    */   {
/*  21: 70 */     this.charts = new Chart[0];
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setDrawings(ArrayList dr, boolean mod)
/*  25:    */   {
/*  26: 81 */     this.drawings = dr;
/*  27: 82 */     this.drawingsModified = mod;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void write(File outputFile)
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 95 */     if ((this.drawings.size() == 0) && (this.charts.length == 0)) {
/*  34: 97 */       return;
/*  35:    */     }
/*  36:101 */     boolean modified = this.drawingsModified;
/*  37:102 */     int numImages = this.drawings.size();
/*  38:104 */     for (Iterator i = this.drawings.iterator(); (i.hasNext()) && (!modified);)
/*  39:    */     {
/*  40:106 */       DrawingGroupObject d = (DrawingGroupObject)i.next();
/*  41:107 */       if (d.getOrigin() != Origin.READ) {
/*  42:109 */         modified = true;
/*  43:    */       }
/*  44:    */     }
/*  45:115 */     if ((numImages > 0) && (!modified))
/*  46:    */     {
/*  47:117 */       DrawingGroupObject d2 = (DrawingGroupObject)this.drawings.get(0);
/*  48:118 */       if (!d2.isFirst()) {
/*  49:120 */         modified = true;
/*  50:    */       }
/*  51:    */     }
/*  52:126 */     if ((numImages == 0) && 
/*  53:127 */       (this.charts.length == 1) && 
/*  54:128 */       (this.charts[0].getMsoDrawingRecord() == null)) {
/*  55:130 */       modified = false;
/*  56:    */     }
/*  57:135 */     if (!modified)
/*  58:    */     {
/*  59:137 */       writeUnmodified(outputFile);
/*  60:138 */       return;
/*  61:    */     }
/*  62:141 */     Object[] spContainerData = new Object[numImages + this.charts.length];
/*  63:142 */     int length = 0;
/*  64:143 */     EscherContainer firstSpContainer = null;
/*  65:147 */     for (int i = 0; i < numImages; i++)
/*  66:    */     {
/*  67:149 */       DrawingGroupObject drawing = (DrawingGroupObject)this.drawings.get(i);
/*  68:    */       
/*  69:151 */       EscherContainer spc = drawing.getSpContainer();
/*  70:153 */       if (spc != null)
/*  71:    */       {
/*  72:155 */         byte[] data = spc.getData();
/*  73:156 */         spContainerData[i] = data;
/*  74:158 */         if (i == 0) {
/*  75:160 */           firstSpContainer = spc;
/*  76:    */         } else {
/*  77:164 */           length += data.length;
/*  78:    */         }
/*  79:    */       }
/*  80:    */     }
/*  81:170 */     for (int i = 0; i < this.charts.length; i++)
/*  82:    */     {
/*  83:172 */       EscherContainer spContainer = this.charts[i].getSpContainer();
/*  84:173 */       byte[] data = spContainer.getBytes();
/*  85:174 */       data = spContainer.setHeaderData(data);
/*  86:175 */       spContainerData[(i + numImages)] = data;
/*  87:177 */       if ((i == 0) && (numImages == 0)) {
/*  88:179 */         firstSpContainer = spContainer;
/*  89:    */       } else {
/*  90:183 */         length += data.length;
/*  91:    */       }
/*  92:    */     }
/*  93:188 */     DgContainer dgContainer = new DgContainer();
/*  94:189 */     Dg dg = new Dg(numImages + this.charts.length);
/*  95:190 */     dgContainer.add(dg);
/*  96:    */     
/*  97:192 */     SpgrContainer spgrContainer = new SpgrContainer();
/*  98:    */     
/*  99:194 */     SpContainer spContainer = new SpContainer();
/* 100:195 */     Spgr spgr = new Spgr();
/* 101:196 */     spContainer.add(spgr);
/* 102:197 */     Sp sp = new Sp(ShapeType.MIN, 1024, 5);
/* 103:198 */     spContainer.add(sp);
/* 104:199 */     spgrContainer.add(spContainer);
/* 105:    */     
/* 106:201 */     spgrContainer.add(firstSpContainer);
/* 107:    */     
/* 108:203 */     dgContainer.add(spgrContainer);
/* 109:    */     
/* 110:205 */     byte[] firstMsoData = dgContainer.getData();
/* 111:    */     
/* 112:    */ 
/* 113:208 */     int len = IntegerHelper.getInt(firstMsoData[4], 
/* 114:209 */       firstMsoData[5], 
/* 115:210 */       firstMsoData[6], 
/* 116:211 */       firstMsoData[7]);
/* 117:212 */     IntegerHelper.getFourBytes(len + length, firstMsoData, 4);
/* 118:    */     
/* 119:    */ 
/* 120:215 */     len = IntegerHelper.getInt(firstMsoData[28], 
/* 121:216 */       firstMsoData[29], 
/* 122:217 */       firstMsoData[30], 
/* 123:218 */       firstMsoData[31]);
/* 124:219 */     IntegerHelper.getFourBytes(len + length, firstMsoData, 28);
/* 125:226 */     if ((numImages > 0) && 
/* 126:227 */       (((DrawingGroupObject)this.drawings.get(0)).isFormObject()))
/* 127:    */     {
/* 128:229 */       byte[] msodata2 = new byte[firstMsoData.length - 8];
/* 129:230 */       System.arraycopy(firstMsoData, 0, msodata2, 0, msodata2.length);
/* 130:231 */       firstMsoData = msodata2;
/* 131:    */     }
/* 132:234 */     MsoDrawingRecord msoDrawingRecord = new MsoDrawingRecord(firstMsoData);
/* 133:235 */     outputFile.write(msoDrawingRecord);
/* 134:237 */     if (numImages > 0)
/* 135:    */     {
/* 136:239 */       DrawingGroupObject firstDrawing = (DrawingGroupObject)this.drawings.get(0);
/* 137:240 */       firstDrawing.writeAdditionalRecords(outputFile);
/* 138:    */     }
/* 139:    */     else
/* 140:    */     {
/* 141:245 */       Chart chart = this.charts[0];
/* 142:246 */       ObjRecord objRecord = chart.getObjRecord();
/* 143:247 */       outputFile.write(objRecord);
/* 144:248 */       outputFile.write(chart);
/* 145:    */     }
/* 146:252 */     for (int i = 1; i < spContainerData.length; i++)
/* 147:    */     {
/* 148:254 */       byte[] bytes = (byte[])spContainerData[i];
/* 149:258 */       if ((i < numImages) && 
/* 150:259 */         (((DrawingGroupObject)this.drawings.get(i)).isFormObject()))
/* 151:    */       {
/* 152:261 */         byte[] bytes2 = new byte[bytes.length - 8];
/* 153:262 */         System.arraycopy(bytes, 0, bytes2, 0, bytes2.length);
/* 154:263 */         bytes = bytes2;
/* 155:    */       }
/* 156:266 */       msoDrawingRecord = new MsoDrawingRecord(bytes);
/* 157:267 */       outputFile.write(msoDrawingRecord);
/* 158:269 */       if (i < numImages)
/* 159:    */       {
/* 160:272 */         DrawingGroupObject d = (DrawingGroupObject)this.drawings.get(i);
/* 161:273 */         d.writeAdditionalRecords(outputFile);
/* 162:    */       }
/* 163:    */       else
/* 164:    */       {
/* 165:277 */         Chart chart = this.charts[(i - numImages)];
/* 166:278 */         ObjRecord objRecord = chart.getObjRecord();
/* 167:279 */         outputFile.write(objRecord);
/* 168:280 */         outputFile.write(chart);
/* 169:    */       }
/* 170:    */     }
/* 171:285 */     for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 172:    */     {
/* 173:287 */       DrawingGroupObject dgo2 = (DrawingGroupObject)i.next();
/* 174:288 */       dgo2.writeTailRecords(outputFile);
/* 175:    */     }
/* 176:    */   }
/* 177:    */   
/* 178:    */   private void writeUnmodified(File outputFile)
/* 179:    */     throws IOException
/* 180:    */   {
/* 181:300 */     if ((this.charts.length == 0) && (this.drawings.size() == 0)) {
/* 182:303 */       return;
/* 183:    */     }
/* 184:305 */     if ((this.charts.length == 0) && (this.drawings.size() != 0))
/* 185:    */     {
/* 186:308 */       for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 187:    */       {
/* 188:310 */         DrawingGroupObject d = (DrawingGroupObject)i.next();
/* 189:311 */         outputFile.write(d.getMsoDrawingRecord());
/* 190:312 */         d.writeAdditionalRecords(outputFile);
/* 191:    */       }
/* 192:315 */       for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 193:    */       {
/* 194:317 */         DrawingGroupObject d = (DrawingGroupObject)i.next();
/* 195:318 */         d.writeTailRecords(outputFile);
/* 196:    */       }
/* 197:320 */       return;
/* 198:    */     }
/* 199:322 */     if ((this.drawings.size() == 0) && (this.charts.length != 0))
/* 200:    */     {
/* 201:325 */       Chart curChart = null;
/* 202:326 */       for (int i = 0; i < this.charts.length; i++)
/* 203:    */       {
/* 204:328 */         curChart = this.charts[i];
/* 205:329 */         if (curChart.getMsoDrawingRecord() != null) {
/* 206:331 */           outputFile.write(curChart.getMsoDrawingRecord());
/* 207:    */         }
/* 208:334 */         if (curChart.getObjRecord() != null) {
/* 209:336 */           outputFile.write(curChart.getObjRecord());
/* 210:    */         }
/* 211:339 */         outputFile.write(curChart);
/* 212:    */       }
/* 213:342 */       return;
/* 214:    */     }
/* 215:349 */     int numDrawings = this.drawings.size();
/* 216:350 */     int length = 0;
/* 217:351 */     EscherContainer[] spContainers = 
/* 218:352 */       new EscherContainer[numDrawings + this.charts.length];
/* 219:353 */     boolean[] isFormObject = new boolean[numDrawings + this.charts.length];
/* 220:355 */     for (int i = 0; i < numDrawings; i++)
/* 221:    */     {
/* 222:357 */       DrawingGroupObject d = (DrawingGroupObject)this.drawings.get(i);
/* 223:358 */       spContainers[i] = d.getSpContainer();
/* 224:360 */       if (i > 0) {
/* 225:362 */         length += spContainers[i].getLength();
/* 226:    */       }
/* 227:365 */       if (d.isFormObject()) {
/* 228:367 */         isFormObject[i] = true;
/* 229:    */       }
/* 230:    */     }
/* 231:371 */     for (int i = 0; i < this.charts.length; i++)
/* 232:    */     {
/* 233:373 */       spContainers[(i + numDrawings)] = this.charts[i].getSpContainer();
/* 234:374 */       length += spContainers[(i + numDrawings)].getLength();
/* 235:    */     }
/* 236:378 */     DgContainer dgContainer = new DgContainer();
/* 237:379 */     Dg dg = new Dg(numDrawings + this.charts.length);
/* 238:380 */     dgContainer.add(dg);
/* 239:    */     
/* 240:382 */     SpgrContainer spgrContainer = new SpgrContainer();
/* 241:    */     
/* 242:384 */     SpContainer spContainer = new SpContainer();
/* 243:385 */     Spgr spgr = new Spgr();
/* 244:386 */     spContainer.add(spgr);
/* 245:387 */     Sp sp = new Sp(ShapeType.MIN, 1024, 5);
/* 246:388 */     spContainer.add(sp);
/* 247:389 */     spgrContainer.add(spContainer);
/* 248:    */     
/* 249:391 */     spgrContainer.add(spContainers[0]);
/* 250:    */     
/* 251:393 */     dgContainer.add(spgrContainer);
/* 252:    */     
/* 253:395 */     byte[] firstMsoData = dgContainer.getData();
/* 254:    */     
/* 255:    */ 
/* 256:398 */     int len = IntegerHelper.getInt(firstMsoData[4], 
/* 257:399 */       firstMsoData[5], 
/* 258:400 */       firstMsoData[6], 
/* 259:401 */       firstMsoData[7]);
/* 260:402 */     IntegerHelper.getFourBytes(len + length, firstMsoData, 4);
/* 261:    */     
/* 262:    */ 
/* 263:405 */     len = IntegerHelper.getInt(firstMsoData[28], 
/* 264:406 */       firstMsoData[29], 
/* 265:407 */       firstMsoData[30], 
/* 266:408 */       firstMsoData[31]);
/* 267:409 */     IntegerHelper.getFourBytes(len + length, firstMsoData, 28);
/* 268:415 */     if (isFormObject[0] != 0)
/* 269:    */     {
/* 270:417 */       byte[] cbytes = new byte[firstMsoData.length - 8];
/* 271:418 */       System.arraycopy(firstMsoData, 0, cbytes, 0, cbytes.length);
/* 272:419 */       firstMsoData = cbytes;
/* 273:    */     }
/* 274:423 */     MsoDrawingRecord msoDrawingRecord = new MsoDrawingRecord(firstMsoData);
/* 275:424 */     outputFile.write(msoDrawingRecord);
/* 276:    */     
/* 277:426 */     DrawingGroupObject dgo = (DrawingGroupObject)this.drawings.get(0);
/* 278:427 */     dgo.writeAdditionalRecords(outputFile);
/* 279:430 */     for (int i = 1; i < spContainers.length; i++)
/* 280:    */     {
/* 281:432 */       byte[] bytes = spContainers[i].getBytes();
/* 282:433 */       byte[] bytes2 = spContainers[i].setHeaderData(bytes);
/* 283:437 */       if (isFormObject[i] != 0)
/* 284:    */       {
/* 285:439 */         byte[] cbytes = new byte[bytes2.length - 8];
/* 286:440 */         System.arraycopy(bytes2, 0, cbytes, 0, cbytes.length);
/* 287:441 */         bytes2 = cbytes;
/* 288:    */       }
/* 289:444 */       msoDrawingRecord = new MsoDrawingRecord(bytes2);
/* 290:445 */       outputFile.write(msoDrawingRecord);
/* 291:447 */       if (i < numDrawings)
/* 292:    */       {
/* 293:449 */         dgo = (DrawingGroupObject)this.drawings.get(i);
/* 294:450 */         dgo.writeAdditionalRecords(outputFile);
/* 295:    */       }
/* 296:    */       else
/* 297:    */       {
/* 298:454 */         Chart chart = this.charts[(i - numDrawings)];
/* 299:455 */         ObjRecord objRecord = chart.getObjRecord();
/* 300:456 */         outputFile.write(objRecord);
/* 301:457 */         outputFile.write(chart);
/* 302:    */       }
/* 303:    */     }
/* 304:462 */     for (Iterator i = this.drawings.iterator(); i.hasNext();)
/* 305:    */     {
/* 306:464 */       DrawingGroupObject dgo2 = (DrawingGroupObject)i.next();
/* 307:465 */       dgo2.writeTailRecords(outputFile);
/* 308:    */     }
/* 309:    */   }
/* 310:    */   
/* 311:    */   public void setCharts(Chart[] ch)
/* 312:    */   {
/* 313:476 */     this.charts = ch;
/* 314:    */   }
/* 315:    */   
/* 316:    */   public Chart[] getCharts()
/* 317:    */   {
/* 318:486 */     return this.charts;
/* 319:    */   }
/* 320:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.SheetDrawingWriter
 * JD-Core Version:    0.7.0.1
 */