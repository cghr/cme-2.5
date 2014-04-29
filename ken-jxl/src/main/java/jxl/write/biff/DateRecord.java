/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.text.DateFormat;
/*   4:    */ import java.util.Calendar;
/*   5:    */ import java.util.Date;
/*   6:    */ import jxl.CellType;
/*   7:    */ import jxl.DateCell;
/*   8:    */ import jxl.biff.DoubleHelper;
/*   9:    */ import jxl.biff.Type;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ import jxl.format.CellFormat;
/*  12:    */ import jxl.write.DateFormats;
/*  13:    */ import jxl.write.WritableCellFormat;
/*  14:    */ 
/*  15:    */ public abstract class DateRecord
/*  16:    */   extends CellValue
/*  17:    */ {
/*  18: 44 */   private static Logger logger = Logger.getLogger(DateRecord.class);
/*  19:    */   private double value;
/*  20:    */   private Date date;
/*  21:    */   private boolean time;
/*  22:    */   private static final int utcOffsetDays = 25569;
/*  23:    */   private static final long msInADay = 86400000L;
/*  24: 76 */   static final WritableCellFormat defaultDateFormat = new WritableCellFormat(DateFormats.DEFAULT);
/*  25:    */   private static final int nonLeapDay = 61;
/*  26:    */   
/*  27:    */   protected DateRecord(int c, int r, Date d)
/*  28:    */   {
/*  29:103 */     this(c, r, d, defaultDateFormat, false);
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected DateRecord(int c, int r, Date d, GMTDate a)
/*  33:    */   {
/*  34:116 */     this(c, r, d, defaultDateFormat, false);
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected DateRecord(int c, int r, Date d, CellFormat st)
/*  38:    */   {
/*  39:129 */     super(Type.NUMBER, c, r, st);
/*  40:130 */     this.date = d;
/*  41:131 */     calculateValue(true);
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected DateRecord(int c, int r, Date d, CellFormat st, GMTDate a)
/*  45:    */   {
/*  46:145 */     super(Type.NUMBER, c, r, st);
/*  47:146 */     this.date = d;
/*  48:147 */     calculateValue(false);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected DateRecord(int c, int r, Date d, CellFormat st, boolean tim)
/*  52:    */   {
/*  53:161 */     super(Type.NUMBER, c, r, st);
/*  54:162 */     this.date = d;
/*  55:163 */     this.time = tim;
/*  56:164 */     calculateValue(false);
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected DateRecord(DateCell dc)
/*  60:    */   {
/*  61:174 */     super(Type.NUMBER, dc);
/*  62:175 */     this.date = dc.getDate();
/*  63:176 */     this.time = dc.isTime();
/*  64:177 */     calculateValue(false);
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected DateRecord(int c, int r, DateRecord dr)
/*  68:    */   {
/*  69:189 */     super(Type.NUMBER, c, r, dr);
/*  70:190 */     this.value = dr.value;
/*  71:191 */     this.time = dr.time;
/*  72:192 */     this.date = dr.date;
/*  73:    */   }
/*  74:    */   
/*  75:    */   private void calculateValue(boolean adjust)
/*  76:    */   {
/*  77:205 */     long zoneOffset = 0L;
/*  78:206 */     long dstOffset = 0L;
/*  79:210 */     if (adjust)
/*  80:    */     {
/*  81:213 */       Calendar cal = Calendar.getInstance();
/*  82:214 */       cal.setTime(this.date);
/*  83:    */       
/*  84:216 */       zoneOffset = cal.get(15);
/*  85:217 */       dstOffset = cal.get(16);
/*  86:    */     }
/*  87:220 */     long utcValue = this.date.getTime() + zoneOffset + dstOffset;
/*  88:    */     
/*  89:    */ 
/*  90:    */ 
/*  91:224 */     double utcDays = utcValue / 86400000.0D;
/*  92:    */     
/*  93:    */ 
/*  94:227 */     this.value = (utcDays + 25569.0D);
/*  95:233 */     if ((!this.time) && (this.value < 61.0D)) {
/*  96:235 */       this.value -= 1.0D;
/*  97:    */     }
/*  98:239 */     if (this.time) {
/*  99:241 */       this.value -= (int)this.value;
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   public CellType getType()
/* 104:    */   {
/* 105:252 */     return CellType.DATE;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public byte[] getData()
/* 109:    */   {
/* 110:262 */     byte[] celldata = super.getData();
/* 111:263 */     byte[] data = new byte[celldata.length + 8];
/* 112:264 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/* 113:265 */     DoubleHelper.getIEEEBytes(this.value, data, celldata.length);
/* 114:    */     
/* 115:267 */     return data;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public String getContents()
/* 119:    */   {
/* 120:279 */     return this.date.toString();
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected void setDate(Date d)
/* 124:    */   {
/* 125:289 */     this.date = d;
/* 126:290 */     calculateValue(true);
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected void setDate(Date d, GMTDate a)
/* 130:    */   {
/* 131:301 */     this.date = d;
/* 132:302 */     calculateValue(false);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public Date getDate()
/* 136:    */   {
/* 137:313 */     return this.date;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isTime()
/* 141:    */   {
/* 142:325 */     return this.time;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public DateFormat getDateFormat()
/* 146:    */   {
/* 147:338 */     return null;
/* 148:    */   }
/* 149:    */   
/* 150:    */   protected static final class GMTDate {}
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DateRecord
 * JD-Core Version:    0.7.0.1
 */