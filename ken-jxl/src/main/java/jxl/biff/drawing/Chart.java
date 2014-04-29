/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.ByteData;
/*   5:    */ import jxl.biff.IndexMapping;
/*   6:    */ import jxl.biff.IntegerHelper;
/*   7:    */ import jxl.biff.Type;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.read.biff.File;
/*  11:    */ import jxl.read.biff.Record;
/*  12:    */ 
/*  13:    */ public class Chart
/*  14:    */   implements ByteData, EscherStream
/*  15:    */ {
/*  16: 41 */   private static final Logger logger = Logger.getLogger(Chart.class);
/*  17:    */   private MsoDrawingRecord msoDrawingRecord;
/*  18:    */   private ObjRecord objRecord;
/*  19:    */   private int startpos;
/*  20:    */   private int endpos;
/*  21:    */   private File file;
/*  22:    */   private DrawingData drawingData;
/*  23:    */   private int drawingNumber;
/*  24:    */   private byte[] data;
/*  25:    */   private boolean initialized;
/*  26:    */   private WorkbookSettings workbookSettings;
/*  27:    */   
/*  28:    */   public Chart(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, int sp, int ep, File f, WorkbookSettings ws)
/*  29:    */   {
/*  30:109 */     this.msoDrawingRecord = mso;
/*  31:110 */     this.objRecord = obj;
/*  32:111 */     this.startpos = sp;
/*  33:112 */     this.endpos = ep;
/*  34:113 */     this.file = f;
/*  35:114 */     this.workbookSettings = ws;
/*  36:119 */     if (this.msoDrawingRecord != null)
/*  37:    */     {
/*  38:121 */       this.drawingData = dd;
/*  39:122 */       this.drawingData.addData(this.msoDrawingRecord.getRecord().getData());
/*  40:123 */       this.drawingNumber = (this.drawingData.getNumDrawings() - 1);
/*  41:    */     }
/*  42:126 */     this.initialized = false;
/*  43:    */     
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47:131 */     Assert.verify(((mso != null) && (obj != null)) || (
/*  48:132 */       (mso == null) && (obj == null)));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public byte[] getBytes()
/*  52:    */   {
/*  53:142 */     if (!this.initialized) {
/*  54:144 */       initialize();
/*  55:    */     }
/*  56:147 */     return this.data;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public byte[] getData()
/*  60:    */   {
/*  61:157 */     return this.msoDrawingRecord.getRecord().getData();
/*  62:    */   }
/*  63:    */   
/*  64:    */   private void initialize()
/*  65:    */   {
/*  66:165 */     this.data = this.file.read(this.startpos, this.endpos - this.startpos);
/*  67:166 */     this.initialized = true;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void rationalize(IndexMapping xfMapping, IndexMapping fontMapping, IndexMapping formatMapping)
/*  71:    */   {
/*  72:179 */     if (!this.initialized) {
/*  73:181 */       initialize();
/*  74:    */     }
/*  75:187 */     int pos = 0;
/*  76:188 */     int code = 0;
/*  77:189 */     int length = 0;
/*  78:190 */     Type type = null;
/*  79:191 */     while (pos < this.data.length)
/*  80:    */     {
/*  81:193 */       code = IntegerHelper.getInt(this.data[pos], this.data[(pos + 1)]);
/*  82:194 */       length = IntegerHelper.getInt(this.data[(pos + 2)], this.data[(pos + 3)]);
/*  83:    */       
/*  84:196 */       type = Type.getType(code);
/*  85:198 */       if (type == Type.FONTX)
/*  86:    */       {
/*  87:200 */         int fontind = IntegerHelper.getInt(this.data[(pos + 4)], this.data[(pos + 5)]);
/*  88:201 */         IntegerHelper.getTwoBytes(fontMapping.getNewIndex(fontind), 
/*  89:202 */           this.data, pos + 4);
/*  90:    */       }
/*  91:204 */       else if (type == Type.FBI)
/*  92:    */       {
/*  93:206 */         int fontind = IntegerHelper.getInt(this.data[(pos + 12)], this.data[(pos + 13)]);
/*  94:207 */         IntegerHelper.getTwoBytes(fontMapping.getNewIndex(fontind), 
/*  95:208 */           this.data, pos + 12);
/*  96:    */       }
/*  97:210 */       else if (type == Type.IFMT)
/*  98:    */       {
/*  99:212 */         int formind = IntegerHelper.getInt(this.data[(pos + 4)], this.data[(pos + 5)]);
/* 100:213 */         IntegerHelper.getTwoBytes(formatMapping.getNewIndex(formind), 
/* 101:214 */           this.data, pos + 4);
/* 102:    */       }
/* 103:216 */       else if (type == Type.ALRUNS)
/* 104:    */       {
/* 105:218 */         int numRuns = IntegerHelper.getInt(this.data[(pos + 4)], this.data[(pos + 5)]);
/* 106:219 */         int fontPos = pos + 6;
/* 107:220 */         for (int i = 0; i < numRuns; i++)
/* 108:    */         {
/* 109:222 */           int fontind = IntegerHelper.getInt(this.data[(fontPos + 2)], 
/* 110:223 */             this.data[(fontPos + 3)]);
/* 111:224 */           IntegerHelper.getTwoBytes(fontMapping.getNewIndex(fontind), 
/* 112:225 */             this.data, fontPos + 2);
/* 113:226 */           fontPos += 4;
/* 114:    */         }
/* 115:    */       }
/* 116:230 */       pos += length + 4;
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   EscherContainer getSpContainer()
/* 121:    */   {
/* 122:241 */     EscherContainer spContainer = this.drawingData.getSpContainer(this.drawingNumber);
/* 123:    */     
/* 124:243 */     return spContainer;
/* 125:    */   }
/* 126:    */   
/* 127:    */   MsoDrawingRecord getMsoDrawingRecord()
/* 128:    */   {
/* 129:253 */     return this.msoDrawingRecord;
/* 130:    */   }
/* 131:    */   
/* 132:    */   ObjRecord getObjRecord()
/* 133:    */   {
/* 134:263 */     return this.objRecord;
/* 135:    */   }
/* 136:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Chart
 * JD-Core Version:    0.7.0.1
 */