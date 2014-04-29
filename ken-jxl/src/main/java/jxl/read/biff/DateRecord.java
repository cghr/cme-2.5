/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.text.SimpleDateFormat;
/*   5:    */ import java.util.Date;
/*   6:    */ import java.util.TimeZone;
/*   7:    */ import jxl.CellFeatures;
/*   8:    */ import jxl.CellType;
/*   9:    */ import jxl.DateCell;
/*  10:    */ import jxl.NumberCell;
/*  11:    */ import jxl.biff.FormattingRecords;
/*  12:    */ import jxl.common.Assert;
/*  13:    */ import jxl.common.Logger;
/*  14:    */ import jxl.format.CellFormat;
/*  15:    */ 
/*  16:    */ class DateRecord
/*  17:    */   implements DateCell, CellFeaturesAccessor
/*  18:    */ {
/*  19: 45 */   private static Logger logger = Logger.getLogger(DateRecord.class);
/*  20:    */   private Date date;
/*  21:    */   private int row;
/*  22:    */   private int column;
/*  23:    */   private boolean time;
/*  24:    */   private DateFormat format;
/*  25:    */   private CellFormat cellFormat;
/*  26:    */   private int xfIndex;
/*  27:    */   private FormattingRecords formattingRecords;
/*  28:    */   private SheetImpl sheet;
/*  29:    */   private CellFeatures features;
/*  30:    */   private boolean initialized;
/*  31:104 */   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
/*  32:107 */   private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
/*  33:    */   private static final int nonLeapDay = 61;
/*  34:115 */   private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");
/*  35:    */   private static final int utcOffsetDays = 25569;
/*  36:    */   private static final int utcOffsetDays1904 = 24107;
/*  37:    */   private static final long secondsInADay = 86400L;
/*  38:    */   private static final long msInASecond = 1000L;
/*  39:    */   private static final long msInADay = 86400000L;
/*  40:    */   
/*  41:    */   public DateRecord(NumberCell num, int xfi, FormattingRecords fr, boolean nf, SheetImpl si)
/*  42:    */   {
/*  43:144 */     this.row = num.getRow();
/*  44:145 */     this.column = num.getColumn();
/*  45:146 */     this.xfIndex = xfi;
/*  46:147 */     this.formattingRecords = fr;
/*  47:148 */     this.sheet = si;
/*  48:149 */     this.initialized = false;
/*  49:    */     
/*  50:151 */     this.format = this.formattingRecords.getDateFormat(this.xfIndex);
/*  51:    */     
/*  52:    */ 
/*  53:154 */     double numValue = num.getValue();
/*  54:156 */     if (Math.abs(numValue) < 1.0D)
/*  55:    */     {
/*  56:158 */       if (this.format == null) {
/*  57:160 */         this.format = timeFormat;
/*  58:    */       }
/*  59:162 */       this.time = true;
/*  60:    */     }
/*  61:    */     else
/*  62:    */     {
/*  63:166 */       if (this.format == null) {
/*  64:168 */         this.format = dateFormat;
/*  65:    */       }
/*  66:170 */       this.time = false;
/*  67:    */     }
/*  68:177 */     if ((!nf) && (!this.time) && (numValue < 61.0D)) {
/*  69:179 */       numValue += 1.0D;
/*  70:    */     }
/*  71:184 */     this.format.setTimeZone(gmtZone);
/*  72:    */     
/*  73:    */ 
/*  74:187 */     int offsetDays = nf ? 24107 : 25569;
/*  75:188 */     double utcDays = numValue - offsetDays;
/*  76:    */     
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:193 */     long utcValue = Math.round(utcDays * 86400.0D) * 1000L;
/*  81:    */     
/*  82:195 */     this.date = new Date(utcValue);
/*  83:    */   }
/*  84:    */   
/*  85:    */   public final int getRow()
/*  86:    */   {
/*  87:205 */     return this.row;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public final int getColumn()
/*  91:    */   {
/*  92:215 */     return this.column;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Date getDate()
/*  96:    */   {
/*  97:225 */     return this.date;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String getContents()
/* 101:    */   {
/* 102:236 */     return this.format.format(this.date);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public CellType getType()
/* 106:    */   {
/* 107:246 */     return CellType.DATE;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isTime()
/* 111:    */   {
/* 112:257 */     return this.time;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public DateFormat getDateFormat()
/* 116:    */   {
/* 117:270 */     Assert.verify(this.format != null);
/* 118:    */     
/* 119:272 */     return this.format;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public CellFormat getCellFormat()
/* 123:    */   {
/* 124:283 */     if (!this.initialized)
/* 125:    */     {
/* 126:285 */       this.cellFormat = this.formattingRecords.getXFRecord(this.xfIndex);
/* 127:286 */       this.initialized = true;
/* 128:    */     }
/* 129:289 */     return this.cellFormat;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public boolean isHidden()
/* 133:    */   {
/* 134:299 */     ColumnInfoRecord cir = this.sheet.getColumnInfo(this.column);
/* 135:301 */     if ((cir != null) && (cir.getWidth() == 0)) {
/* 136:303 */       return true;
/* 137:    */     }
/* 138:306 */     RowRecord rr = this.sheet.getRowInfo(this.row);
/* 139:308 */     if ((rr != null) && ((rr.getRowHeight() == 0) || (rr.isCollapsed()))) {
/* 140:310 */       return true;
/* 141:    */     }
/* 142:313 */     return false;
/* 143:    */   }
/* 144:    */   
/* 145:    */   protected final SheetImpl getSheet()
/* 146:    */   {
/* 147:323 */     return this.sheet;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public CellFeatures getCellFeatures()
/* 151:    */   {
/* 152:333 */     return this.features;
/* 153:    */   }
/* 154:    */   
/* 155:    */   public void setCellFeatures(CellFeatures cf)
/* 156:    */   {
/* 157:343 */     this.features = cf;
/* 158:    */   }
/* 159:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.DateRecord
 * JD-Core Version:    0.7.0.1
 */