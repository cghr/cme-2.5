/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.Cell;
/*   4:    */ import jxl.biff.CellReferenceHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class SharedFormulaCellReference
/*   9:    */   extends Operand
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 36 */   private static Logger logger = Logger.getLogger(SharedFormulaCellReference.class);
/*  13:    */   private boolean columnRelative;
/*  14:    */   private boolean rowRelative;
/*  15:    */   private int column;
/*  16:    */   private int row;
/*  17:    */   private Cell relativeTo;
/*  18:    */   
/*  19:    */   public SharedFormulaCellReference(Cell rt)
/*  20:    */   {
/*  21: 71 */     this.relativeTo = rt;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int read(byte[] data, int pos)
/*  25:    */   {
/*  26: 85 */     this.row = IntegerHelper.getShort(data[pos], data[(pos + 1)]);
/*  27:    */     
/*  28: 87 */     int columnMask = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  29:    */     
/*  30: 89 */     this.column = ((byte)(columnMask & 0xFF));
/*  31: 90 */     this.columnRelative = ((columnMask & 0x4000) != 0);
/*  32: 91 */     this.rowRelative = ((columnMask & 0x8000) != 0);
/*  33: 93 */     if ((this.columnRelative) && (this.relativeTo != null)) {
/*  34: 95 */       this.column = (this.relativeTo.getColumn() + this.column);
/*  35:    */     }
/*  36: 98 */     if ((this.rowRelative) && (this.relativeTo != null)) {
/*  37:100 */       this.row = (this.relativeTo.getRow() + this.row);
/*  38:    */     }
/*  39:103 */     return 4;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getColumn()
/*  43:    */   {
/*  44:108 */     return this.column;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int getRow()
/*  48:    */   {
/*  49:113 */     return this.row;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void getString(StringBuffer buf)
/*  53:    */   {
/*  54:118 */     CellReferenceHelper.getCellReference(this.column, this.row, buf);
/*  55:    */   }
/*  56:    */   
/*  57:    */   byte[] getBytes()
/*  58:    */   {
/*  59:128 */     byte[] data = new byte[5];
/*  60:129 */     data[0] = Token.REF.getCode();
/*  61:    */     
/*  62:131 */     IntegerHelper.getTwoBytes(this.row, data, 1);
/*  63:    */     
/*  64:133 */     int columnMask = this.column;
/*  65:135 */     if (this.columnRelative) {
/*  66:137 */       columnMask |= 0x4000;
/*  67:    */     }
/*  68:140 */     if (this.rowRelative) {
/*  69:142 */       columnMask |= 0x8000;
/*  70:    */     }
/*  71:145 */     IntegerHelper.getTwoBytes(columnMask, data, 3);
/*  72:    */     
/*  73:147 */     return data;
/*  74:    */   }
/*  75:    */   
/*  76:    */   void handleImportedCellReferences() {}
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.SharedFormulaCellReference
 * JD-Core Version:    0.7.0.1
 */