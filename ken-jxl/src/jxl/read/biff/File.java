/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.InterruptedIOException;
/*   6:    */ import jxl.WorkbookSettings;
/*   7:    */ import jxl.biff.BaseCompoundFile;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.biff.Type;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ public class File
/*  13:    */ {
/*  14: 42 */   private static Logger logger = Logger.getLogger(File.class);
/*  15:    */   private byte[] data;
/*  16:    */   private int filePos;
/*  17:    */   private int oldPos;
/*  18:    */   private int initialFileSize;
/*  19:    */   private int arrayGrowSize;
/*  20:    */   private CompoundFile compoundFile;
/*  21:    */   private WorkbookSettings workbookSettings;
/*  22:    */   
/*  23:    */   public File(InputStream is, WorkbookSettings ws)
/*  24:    */     throws IOException, BiffException
/*  25:    */   {
/*  26: 86 */     this.workbookSettings = ws;
/*  27: 87 */     this.initialFileSize = this.workbookSettings.getInitialFileSize();
/*  28: 88 */     this.arrayGrowSize = this.workbookSettings.getArrayGrowSize();
/*  29:    */     
/*  30: 90 */     byte[] d = new byte[this.initialFileSize];
/*  31: 91 */     int bytesRead = is.read(d);
/*  32: 92 */     int pos = bytesRead;
/*  33: 96 */     if (Thread.currentThread().isInterrupted()) {
/*  34: 98 */       throw new InterruptedIOException();
/*  35:    */     }
/*  36:101 */     while (bytesRead != -1)
/*  37:    */     {
/*  38:103 */       if (pos >= d.length)
/*  39:    */       {
/*  40:106 */         byte[] newArray = new byte[d.length + this.arrayGrowSize];
/*  41:107 */         System.arraycopy(d, 0, newArray, 0, d.length);
/*  42:108 */         d = newArray;
/*  43:    */       }
/*  44:110 */       bytesRead = is.read(d, pos, d.length - pos);
/*  45:111 */       pos += bytesRead;
/*  46:113 */       if (Thread.currentThread().isInterrupted()) {
/*  47:115 */         throw new InterruptedIOException();
/*  48:    */       }
/*  49:    */     }
/*  50:119 */     bytesRead = pos + 1;
/*  51:122 */     if (bytesRead == 0) {
/*  52:124 */       throw new BiffException(BiffException.excelFileNotFound);
/*  53:    */     }
/*  54:127 */     CompoundFile cf = new CompoundFile(d, ws);
/*  55:    */     try
/*  56:    */     {
/*  57:130 */       this.data = cf.getStream("workbook");
/*  58:    */     }
/*  59:    */     catch (BiffException e)
/*  60:    */     {
/*  61:135 */       this.data = cf.getStream("book");
/*  62:    */     }
/*  63:138 */     if (!this.workbookSettings.getPropertySetsDisabled()) {
/*  64:140 */       if (cf.getNumberOfPropertySets() > BaseCompoundFile.STANDARD_PROPERTY_SETS.length) {
/*  65:142 */         this.compoundFile = cf;
/*  66:    */       }
/*  67:    */     }
/*  68:145 */     cf = null;
/*  69:147 */     if (!this.workbookSettings.getGCDisabled()) {
/*  70:149 */       System.gc();
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public File(byte[] d)
/*  75:    */   {
/*  76:171 */     this.data = d;
/*  77:    */   }
/*  78:    */   
/*  79:    */   Record next()
/*  80:    */   {
/*  81:181 */     Record r = new Record(this.data, this.filePos, this);
/*  82:182 */     return r;
/*  83:    */   }
/*  84:    */   
/*  85:    */   Record peek()
/*  86:    */   {
/*  87:192 */     int tempPos = this.filePos;
/*  88:193 */     Record r = new Record(this.data, this.filePos, this);
/*  89:194 */     this.filePos = tempPos;
/*  90:195 */     return r;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void skip(int bytes)
/*  94:    */   {
/*  95:205 */     this.filePos += bytes;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public byte[] read(int pos, int length)
/*  99:    */   {
/* 100:217 */     byte[] ret = new byte[length];
/* 101:    */     try
/* 102:    */     {
/* 103:220 */       System.arraycopy(this.data, pos, ret, 0, length);
/* 104:    */     }
/* 105:    */     catch (ArrayIndexOutOfBoundsException e)
/* 106:    */     {
/* 107:224 */       logger.error("Array index out of bounds at position " + pos + 
/* 108:225 */         " record length " + length);
/* 109:226 */       throw e;
/* 110:    */     }
/* 111:228 */     return ret;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int getPos()
/* 115:    */   {
/* 116:238 */     return this.filePos;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void setPos(int p)
/* 120:    */   {
/* 121:254 */     this.oldPos = this.filePos;
/* 122:255 */     this.filePos = p;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void restorePos()
/* 126:    */   {
/* 127:266 */     this.filePos = this.oldPos;
/* 128:    */   }
/* 129:    */   
/* 130:    */   private void moveToFirstBof()
/* 131:    */   {
/* 132:274 */     boolean bofFound = false;
/* 133:275 */     while (!bofFound)
/* 134:    */     {
/* 135:277 */       int code = IntegerHelper.getInt(this.data[this.filePos], this.data[(this.filePos + 1)]);
/* 136:278 */       if (code == Type.BOF.value) {
/* 137:280 */         bofFound = true;
/* 138:    */       } else {
/* 139:284 */         skip(128);
/* 140:    */       }
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   /**
/* 145:    */    * @deprecated
/* 146:    */    */
/* 147:    */   public void close() {}
/* 148:    */   
/* 149:    */   public void clear()
/* 150:    */   {
/* 151:303 */     this.data = null;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public boolean hasNext()
/* 155:    */   {
/* 156:314 */     return this.filePos < this.data.length - 4;
/* 157:    */   }
/* 158:    */   
/* 159:    */   CompoundFile getCompoundFile()
/* 160:    */   {
/* 161:326 */     return this.compoundFile;
/* 162:    */   }
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.File
 * JD-Core Version:    0.7.0.1
 */