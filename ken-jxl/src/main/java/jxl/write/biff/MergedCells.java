/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Iterator;
/*   6:    */ import jxl.Cell;
/*   7:    */ import jxl.CellType;
/*   8:    */ import jxl.Range;
/*   9:    */ import jxl.WorkbookSettings;
/*  10:    */ import jxl.biff.SheetRangeImpl;
/*  11:    */ import jxl.common.Assert;
/*  12:    */ import jxl.common.Logger;
/*  13:    */ import jxl.write.Blank;
/*  14:    */ import jxl.write.WritableSheet;
/*  15:    */ import jxl.write.WriteException;
/*  16:    */ 
/*  17:    */ class MergedCells
/*  18:    */ {
/*  19: 47 */   private static Logger logger = Logger.getLogger(MergedCells.class);
/*  20:    */   private ArrayList ranges;
/*  21:    */   private WritableSheet sheet;
/*  22:    */   private static final int maxRangesPerSheet = 1020;
/*  23:    */   
/*  24:    */   public MergedCells(WritableSheet ws)
/*  25:    */   {
/*  26: 69 */     this.ranges = new ArrayList();
/*  27: 70 */     this.sheet = ws;
/*  28:    */   }
/*  29:    */   
/*  30:    */   void add(Range r)
/*  31:    */   {
/*  32: 81 */     this.ranges.add(r);
/*  33:    */   }
/*  34:    */   
/*  35:    */   void insertRow(int row)
/*  36:    */   {
/*  37: 90 */     SheetRangeImpl sr = null;
/*  38: 91 */     Iterator i = this.ranges.iterator();
/*  39: 92 */     while (i.hasNext())
/*  40:    */     {
/*  41: 94 */       sr = (SheetRangeImpl)i.next();
/*  42: 95 */       sr.insertRow(row);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   void insertColumn(int col)
/*  47:    */   {
/*  48:104 */     SheetRangeImpl sr = null;
/*  49:105 */     Iterator i = this.ranges.iterator();
/*  50:106 */     while (i.hasNext())
/*  51:    */     {
/*  52:108 */       sr = (SheetRangeImpl)i.next();
/*  53:109 */       sr.insertColumn(col);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   void removeColumn(int col)
/*  58:    */   {
/*  59:118 */     SheetRangeImpl sr = null;
/*  60:119 */     Iterator i = this.ranges.iterator();
/*  61:120 */     while (i.hasNext())
/*  62:    */     {
/*  63:122 */       sr = (SheetRangeImpl)i.next();
/*  64:123 */       if ((sr.getTopLeft().getColumn() == col) && 
/*  65:124 */         (sr.getBottomRight().getColumn() == col)) {
/*  66:128 */         i.remove();
/*  67:    */       } else {
/*  68:132 */         sr.removeColumn(col);
/*  69:    */       }
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   void removeRow(int row)
/*  74:    */   {
/*  75:142 */     SheetRangeImpl sr = null;
/*  76:143 */     Iterator i = this.ranges.iterator();
/*  77:144 */     while (i.hasNext())
/*  78:    */     {
/*  79:146 */       sr = (SheetRangeImpl)i.next();
/*  80:147 */       if ((sr.getTopLeft().getRow() == row) && 
/*  81:148 */         (sr.getBottomRight().getRow() == row)) {
/*  82:152 */         i.remove();
/*  83:    */       } else {
/*  84:156 */         sr.removeRow(row);
/*  85:    */       }
/*  86:    */     }
/*  87:    */   }
/*  88:    */   
/*  89:    */   Range[] getMergedCells()
/*  90:    */   {
/*  91:168 */     Range[] cells = new Range[this.ranges.size()];
/*  92:170 */     for (int i = 0; i < cells.length; i++) {
/*  93:172 */       cells[i] = ((Range)this.ranges.get(i));
/*  94:    */     }
/*  95:175 */     return cells;
/*  96:    */   }
/*  97:    */   
/*  98:    */   void unmergeCells(Range r)
/*  99:    */   {
/* 100:186 */     int index = this.ranges.indexOf(r);
/* 101:188 */     if (index != -1) {
/* 102:190 */       this.ranges.remove(index);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   private void checkIntersections()
/* 107:    */   {
/* 108:199 */     ArrayList newcells = new ArrayList(this.ranges.size());
/* 109:201 */     for (Iterator mci = this.ranges.iterator(); mci.hasNext();)
/* 110:    */     {
/* 111:203 */       SheetRangeImpl r = (SheetRangeImpl)mci.next();
/* 112:    */       
/* 113:    */ 
/* 114:206 */       Iterator i = newcells.iterator();
/* 115:207 */       SheetRangeImpl range = null;
/* 116:208 */       boolean intersects = false;
/* 117:209 */       while ((i.hasNext()) && (!intersects))
/* 118:    */       {
/* 119:211 */         range = (SheetRangeImpl)i.next();
/* 120:213 */         if (range.intersects(r))
/* 121:    */         {
/* 122:215 */           logger.warn("Could not merge cells " + r + 
/* 123:216 */             " as they clash with an existing set of merged cells.");
/* 124:    */           
/* 125:218 */           intersects = true;
/* 126:    */         }
/* 127:    */       }
/* 128:222 */       if (!intersects) {
/* 129:224 */         newcells.add(r);
/* 130:    */       }
/* 131:    */     }
/* 132:228 */     this.ranges = newcells;
/* 133:    */   }
/* 134:    */   
/* 135:    */   private void checkRanges()
/* 136:    */   {
/* 137:    */     try
/* 138:    */     {
/* 139:239 */       SheetRangeImpl range = null;
/* 140:242 */       for (int i = 0; i < this.ranges.size(); i++)
/* 141:    */       {
/* 142:244 */         range = (SheetRangeImpl)this.ranges.get(i);
/* 143:    */         
/* 144:    */ 
/* 145:247 */         Cell tl = range.getTopLeft();
/* 146:248 */         Cell br = range.getBottomRight();
/* 147:249 */         boolean found = false;
/* 148:251 */         for (int c = tl.getColumn(); c <= br.getColumn(); c++) {
/* 149:253 */           for (int r = tl.getRow(); r <= br.getRow(); r++)
/* 150:    */           {
/* 151:255 */             Cell cell = this.sheet.getCell(c, r);
/* 152:256 */             if (cell.getType() != CellType.EMPTY) {
/* 153:258 */               if (!found)
/* 154:    */               {
/* 155:260 */                 found = true;
/* 156:    */               }
/* 157:    */               else
/* 158:    */               {
/* 159:264 */                 logger.warn("Range " + range + 
/* 160:265 */                   " contains more than one data cell.  " + 
/* 161:266 */                   "Setting the other cells to blank.");
/* 162:267 */                 Blank b = new Blank(c, r);
/* 163:268 */                 this.sheet.addCell(b);
/* 164:    */               }
/* 165:    */             }
/* 166:    */           }
/* 167:    */         }
/* 168:    */       }
/* 169:    */     }
/* 170:    */     catch (WriteException e)
/* 171:    */     {
/* 172:278 */       Assert.verify(false);
/* 173:    */     }
/* 174:    */   }
/* 175:    */   
/* 176:    */   void write(File outputFile)
/* 177:    */     throws IOException
/* 178:    */   {
/* 179:284 */     if (this.ranges.size() == 0) {
/* 180:286 */       return;
/* 181:    */     }
/* 182:289 */     WorkbookSettings ws = 
/* 183:290 */       ((WritableSheetImpl)this.sheet).getWorkbookSettings();
/* 184:292 */     if (!ws.getMergedCellCheckingDisabled())
/* 185:    */     {
/* 186:294 */       checkIntersections();
/* 187:295 */       checkRanges();
/* 188:    */     }
/* 189:300 */     if (this.ranges.size() < 1020)
/* 190:    */     {
/* 191:302 */       MergedCellsRecord mcr = new MergedCellsRecord(this.ranges);
/* 192:303 */       outputFile.write(mcr);
/* 193:304 */       return;
/* 194:    */     }
/* 195:307 */     int numRecordsRequired = this.ranges.size() / 1020 + 1;
/* 196:308 */     int pos = 0;
/* 197:310 */     for (int i = 0; i < numRecordsRequired; i++)
/* 198:    */     {
/* 199:312 */       int numranges = Math.min(1020, this.ranges.size() - pos);
/* 200:    */       
/* 201:314 */       ArrayList cells = new ArrayList(numranges);
/* 202:315 */       for (int j = 0; j < numranges; j++) {
/* 203:317 */         cells.add(this.ranges.get(pos + j));
/* 204:    */       }
/* 205:320 */       MergedCellsRecord mcr = new MergedCellsRecord(cells);
/* 206:321 */       outputFile.write(mcr);
/* 207:    */       
/* 208:323 */       pos += numranges;
/* 209:    */     }
/* 210:    */   }
/* 211:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.MergedCells
 * JD-Core Version:    0.7.0.1
 */