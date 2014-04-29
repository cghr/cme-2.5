/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.text.NumberFormat;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import jxl.Cell;
/*   6:    */ import jxl.CellType;
/*   7:    */ import jxl.biff.FormattingRecords;
/*   8:    */ import jxl.biff.IntegerHelper;
/*   9:    */ import jxl.biff.WorkbookMethods;
/*  10:    */ import jxl.biff.formula.ExternalSheet;
/*  11:    */ import jxl.common.Logger;
/*  12:    */ 
/*  13:    */ class SharedFormulaRecord
/*  14:    */ {
/*  15: 42 */   private static Logger logger = Logger.getLogger(SharedFormulaRecord.class);
/*  16:    */   private int firstRow;
/*  17:    */   private int lastRow;
/*  18:    */   private int firstCol;
/*  19:    */   private int lastCol;
/*  20:    */   private BaseSharedFormulaRecord templateFormula;
/*  21:    */   private ArrayList formulas;
/*  22:    */   private byte[] tokens;
/*  23:    */   private ExternalSheet externalSheet;
/*  24:    */   private SheetImpl sheet;
/*  25:    */   
/*  26:    */   public SharedFormulaRecord(Record t, BaseSharedFormulaRecord fr, ExternalSheet es, WorkbookMethods nt, SheetImpl si)
/*  27:    */   {
/*  28:105 */     this.sheet = si;
/*  29:106 */     byte[] data = t.getData();
/*  30:    */     
/*  31:108 */     this.firstRow = IntegerHelper.getInt(data[0], data[1]);
/*  32:109 */     this.lastRow = IntegerHelper.getInt(data[2], data[3]);
/*  33:110 */     this.firstCol = (data[4] & 0xFF);
/*  34:111 */     this.lastCol = (data[5] & 0xFF);
/*  35:    */     
/*  36:113 */     this.formulas = new ArrayList();
/*  37:    */     
/*  38:115 */     this.templateFormula = fr;
/*  39:    */     
/*  40:117 */     this.tokens = new byte[data.length - 10];
/*  41:118 */     System.arraycopy(data, 10, this.tokens, 0, this.tokens.length);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean add(BaseSharedFormulaRecord fr)
/*  45:    */   {
/*  46:130 */     boolean added = false;
/*  47:131 */     int r = fr.getRow();
/*  48:132 */     if ((r >= this.firstRow) && (r <= this.lastRow))
/*  49:    */     {
/*  50:134 */       int c = fr.getColumn();
/*  51:135 */       if ((c >= this.firstCol) && (c <= this.lastCol))
/*  52:    */       {
/*  53:137 */         this.formulas.add(fr);
/*  54:138 */         added = true;
/*  55:    */       }
/*  56:    */     }
/*  57:143 */     return added;
/*  58:    */   }
/*  59:    */   
/*  60:    */   Cell[] getFormulas(FormattingRecords fr, boolean nf)
/*  61:    */   {
/*  62:156 */     Cell[] sfs = new Cell[this.formulas.size() + 1];
/*  63:160 */     if (this.templateFormula == null)
/*  64:    */     {
/*  65:162 */       logger.warn("Shared formula template formula is null");
/*  66:163 */       return new Cell[0];
/*  67:    */     }
/*  68:166 */     this.templateFormula.setTokens(this.tokens);
/*  69:167 */     NumberFormat templateNumberFormat = null;
/*  70:170 */     if (this.templateFormula.getType() == CellType.NUMBER_FORMULA)
/*  71:    */     {
/*  72:172 */       SharedNumberFormulaRecord snfr = (SharedNumberFormulaRecord)
/*  73:173 */         this.templateFormula;
/*  74:174 */       templateNumberFormat = snfr.getNumberFormat();
/*  75:176 */       if (fr.isDate(this.templateFormula.getXFIndex()))
/*  76:    */       {
/*  77:178 */         this.templateFormula = new SharedDateFormulaRecord(snfr, fr, nf, this.sheet, 
/*  78:179 */           snfr.getFilePos());
/*  79:180 */         this.templateFormula.setTokens(snfr.getTokens());
/*  80:    */       }
/*  81:    */     }
/*  82:184 */     sfs[0] = this.templateFormula;
/*  83:    */     
/*  84:186 */     BaseSharedFormulaRecord f = null;
/*  85:188 */     for (int i = 0; i < this.formulas.size(); i++)
/*  86:    */     {
/*  87:190 */       f = (BaseSharedFormulaRecord)this.formulas.get(i);
/*  88:193 */       if (f.getType() == CellType.NUMBER_FORMULA)
/*  89:    */       {
/*  90:195 */         SharedNumberFormulaRecord snfr = (SharedNumberFormulaRecord)f;
/*  91:197 */         if (fr.isDate(f.getXFIndex())) {
/*  92:199 */           f = new SharedDateFormulaRecord(snfr, fr, nf, this.sheet, 
/*  93:200 */             snfr.getFilePos());
/*  94:    */         }
/*  95:    */       }
/*  96:208 */       f.setTokens(this.tokens);
/*  97:209 */       sfs[(i + 1)] = f;
/*  98:    */     }
/*  99:212 */     return sfs;
/* 100:    */   }
/* 101:    */   
/* 102:    */   BaseSharedFormulaRecord getTemplateFormula()
/* 103:    */   {
/* 104:222 */     return this.templateFormula;
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SharedFormulaRecord
 * JD-Core Version:    0.7.0.1
 */