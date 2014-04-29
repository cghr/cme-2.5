/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellType;
/*   4:    */ import jxl.LabelCell;
/*   5:    */ import jxl.biff.FormattingRecords;
/*   6:    */ import jxl.biff.IntegerHelper;
/*   7:    */ import jxl.biff.Type;
/*   8:    */ import jxl.common.Assert;
/*   9:    */ import jxl.common.Logger;
/*  10:    */ import jxl.format.CellFormat;
/*  11:    */ 
/*  12:    */ public abstract class LabelRecord
/*  13:    */   extends CellValue
/*  14:    */ {
/*  15: 40 */   private static Logger logger = Logger.getLogger(LabelRecord.class);
/*  16:    */   private String contents;
/*  17:    */   private SharedStrings sharedStrings;
/*  18:    */   private int index;
/*  19:    */   
/*  20:    */   protected LabelRecord(int c, int r, String cont)
/*  21:    */   {
/*  22: 66 */     super(Type.LABELSST, c, r);
/*  23: 67 */     this.contents = cont;
/*  24: 68 */     if (this.contents == null) {
/*  25: 70 */       this.contents = "";
/*  26:    */     }
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected LabelRecord(int c, int r, String cont, CellFormat st)
/*  30:    */   {
/*  31: 85 */     super(Type.LABELSST, c, r, st);
/*  32: 86 */     this.contents = cont;
/*  33: 88 */     if (this.contents == null) {
/*  34: 90 */       this.contents = "";
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected LabelRecord(int c, int r, LabelRecord lr)
/*  39:    */   {
/*  40:104 */     super(Type.LABELSST, c, r, lr);
/*  41:105 */     this.contents = lr.contents;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected LabelRecord(LabelCell lc)
/*  45:    */   {
/*  46:116 */     super(Type.LABELSST, lc);
/*  47:117 */     this.contents = lc.getString();
/*  48:118 */     if (this.contents == null) {
/*  49:120 */       this.contents = "";
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public CellType getType()
/*  54:    */   {
/*  55:131 */     return CellType.LABEL;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public byte[] getData()
/*  59:    */   {
/*  60:141 */     byte[] celldata = super.getData();
/*  61:142 */     byte[] data = new byte[celldata.length + 4];
/*  62:143 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  63:144 */     IntegerHelper.getFourBytes(this.index, data, celldata.length);
/*  64:    */     
/*  65:146 */     return data;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getContents()
/*  69:    */   {
/*  70:158 */     return this.contents;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String getString()
/*  74:    */   {
/*  75:169 */     return this.contents;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected void setString(String s)
/*  79:    */   {
/*  80:179 */     if (s == null) {
/*  81:181 */       s = "";
/*  82:    */     }
/*  83:184 */     this.contents = s;
/*  84:188 */     if (!isReferenced()) {
/*  85:190 */       return;
/*  86:    */     }
/*  87:193 */     Assert.verify(this.sharedStrings != null);
/*  88:    */     
/*  89:    */ 
/*  90:196 */     this.index = this.sharedStrings.getIndex(this.contents);
/*  91:    */     
/*  92:    */ 
/*  93:    */ 
/*  94:    */ 
/*  95:201 */     this.contents = this.sharedStrings.get(this.index);
/*  96:    */   }
/*  97:    */   
/*  98:    */   void setCellDetails(FormattingRecords fr, SharedStrings ss, WritableSheetImpl s)
/*  99:    */   {
/* 100:216 */     super.setCellDetails(fr, ss, s);
/* 101:    */     
/* 102:218 */     this.sharedStrings = ss;
/* 103:    */     
/* 104:220 */     this.index = this.sharedStrings.getIndex(this.contents);
/* 105:    */     
/* 106:    */ 
/* 107:    */ 
/* 108:    */ 
/* 109:225 */     this.contents = this.sharedStrings.get(this.index);
/* 110:    */   }
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.LabelRecord
 * JD-Core Version:    0.7.0.1
 */