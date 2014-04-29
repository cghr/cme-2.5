/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.biff.CellReferenceHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ 
/*   7:    */ class SharedFormulaArea
/*   8:    */   extends Operand
/*   9:    */   implements ParsedThing
/*  10:    */ {
/*  11:    */   private int columnFirst;
/*  12:    */   private int rowFirst;
/*  13:    */   private int columnLast;
/*  14:    */   private int rowLast;
/*  15:    */   private boolean columnFirstRelative;
/*  16:    */   private boolean rowFirstRelative;
/*  17:    */   private boolean columnLastRelative;
/*  18:    */   private boolean rowLastRelative;
/*  19:    */   private Cell relativeTo;
/*  20:    */   
/*  21:    */   public SharedFormulaArea(Cell rt)
/*  22:    */   {
/*  23: 54 */     this.relativeTo = rt;
/*  24:    */   }
/*  25:    */   
/*  26:    */   int getFirstColumn()
/*  27:    */   {
/*  28: 59 */     return this.columnFirst;
/*  29:    */   }
/*  30:    */   
/*  31:    */   int getFirstRow()
/*  32:    */   {
/*  33: 64 */     return this.rowFirst;
/*  34:    */   }
/*  35:    */   
/*  36:    */   int getLastColumn()
/*  37:    */   {
/*  38: 69 */     return this.columnLast;
/*  39:    */   }
/*  40:    */   
/*  41:    */   int getLastRow()
/*  42:    */   {
/*  43: 74 */     return this.rowLast;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public int read(byte[] data, int pos)
/*  47:    */   {
/*  48: 89 */     this.rowFirst = IntegerHelper.getShort(data[pos], data[(pos + 1)]);
/*  49: 90 */     this.rowLast = IntegerHelper.getShort(data[(pos + 2)], data[(pos + 3)]);
/*  50:    */     
/*  51: 92 */     int columnMask = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/*  52: 93 */     this.columnFirst = (columnMask & 0xFF);
/*  53: 94 */     this.columnFirstRelative = ((columnMask & 0x4000) != 0);
/*  54: 95 */     this.rowFirstRelative = ((columnMask & 0x8000) != 0);
/*  55: 97 */     if (this.columnFirstRelative) {
/*  56: 99 */       this.columnFirst = (this.relativeTo.getColumn() + this.columnFirst);
/*  57:    */     }
/*  58:102 */     if (this.rowFirstRelative) {
/*  59:104 */       this.rowFirst = (this.relativeTo.getRow() + this.rowFirst);
/*  60:    */     }
/*  61:107 */     columnMask = IntegerHelper.getInt(data[(pos + 6)], data[(pos + 7)]);
/*  62:108 */     this.columnLast = (columnMask & 0xFF);
/*  63:    */     
/*  64:110 */     this.columnLastRelative = ((columnMask & 0x4000) != 0);
/*  65:111 */     this.rowLastRelative = ((columnMask & 0x8000) != 0);
/*  66:113 */     if (this.columnLastRelative) {
/*  67:115 */       this.columnLast = (this.relativeTo.getColumn() + this.columnLast);
/*  68:    */     }
/*  69:118 */     if (this.rowLastRelative) {
/*  70:120 */       this.rowLast = (this.relativeTo.getRow() + this.rowLast);
/*  71:    */     }
/*  72:124 */     return 8;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void getString(StringBuffer buf)
/*  76:    */   {
/*  77:129 */     CellReferenceHelper.getCellReference(this.columnFirst, this.rowFirst, buf);
/*  78:130 */     buf.append(':');
/*  79:131 */     CellReferenceHelper.getCellReference(this.columnLast, this.rowLast, buf);
/*  80:    */   }
/*  81:    */   
/*  82:    */   byte[] getBytes()
/*  83:    */   {
/*  84:141 */     byte[] data = new byte[9];
/*  85:142 */     data[0] = Token.AREA.getCode();
/*  86:    */     
/*  87:    */ 
/*  88:    */ 
/*  89:146 */     IntegerHelper.getTwoBytes(this.rowFirst, data, 1);
/*  90:147 */     IntegerHelper.getTwoBytes(this.rowLast, data, 3);
/*  91:148 */     IntegerHelper.getTwoBytes(this.columnFirst, data, 5);
/*  92:149 */     IntegerHelper.getTwoBytes(this.columnLast, data, 7);
/*  93:    */     
/*  94:151 */     return data;
/*  95:    */   }
/*  96:    */   
/*  97:    */   void handleImportedCellReferences() {}
/*  98:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.SharedFormulaArea
 * JD-Core Version:    0.7.0.1
 */