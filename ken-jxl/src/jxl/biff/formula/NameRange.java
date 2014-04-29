/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.NameRangeException;
/*   5:    */ import jxl.biff.WorkbookMethods;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ class NameRange
/*  10:    */   extends Operand
/*  11:    */   implements ParsedThing
/*  12:    */ {
/*  13: 37 */   private static Logger logger = Logger.getLogger(NameRange.class);
/*  14:    */   private WorkbookMethods nameTable;
/*  15:    */   private String name;
/*  16:    */   private int index;
/*  17:    */   
/*  18:    */   public NameRange(WorkbookMethods nt)
/*  19:    */   {
/*  20: 59 */     this.nameTable = nt;
/*  21: 60 */     Assert.verify(this.nameTable != null);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public NameRange(String nm, WorkbookMethods nt)
/*  25:    */     throws FormulaException
/*  26:    */   {
/*  27: 71 */     this.name = nm;
/*  28: 72 */     this.nameTable = nt;
/*  29:    */     
/*  30: 74 */     this.index = this.nameTable.getNameIndex(this.name);
/*  31: 76 */     if (this.index < 0) {
/*  32: 78 */       throw new FormulaException(FormulaException.CELL_NAME_NOT_FOUND, this.name);
/*  33:    */     }
/*  34: 81 */     this.index += 1;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int read(byte[] data, int pos)
/*  38:    */     throws FormulaException
/*  39:    */   {
/*  40:    */     try
/*  41:    */     {
/*  42: 95 */       this.index = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  43:    */       
/*  44: 97 */       this.name = this.nameTable.getName(this.index - 1);
/*  45:    */       
/*  46: 99 */       return 4;
/*  47:    */     }
/*  48:    */     catch (NameRangeException e)
/*  49:    */     {
/*  50:103 */       throw new FormulaException(FormulaException.CELL_NAME_NOT_FOUND, "");
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   byte[] getBytes()
/*  55:    */   {
/*  56:114 */     byte[] data = new byte[5];
/*  57:    */     
/*  58:116 */     data[0] = Token.NAMED_RANGE.getValueCode();
/*  59:118 */     if (getParseContext() == ParseContext.DATA_VALIDATION) {
/*  60:120 */       data[0] = Token.NAMED_RANGE.getReferenceCode();
/*  61:    */     }
/*  62:123 */     IntegerHelper.getTwoBytes(this.index, data, 1);
/*  63:    */     
/*  64:125 */     return data;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void getString(StringBuffer buf)
/*  68:    */   {
/*  69:136 */     buf.append(this.name);
/*  70:    */   }
/*  71:    */   
/*  72:    */   void handleImportedCellReferences()
/*  73:    */   {
/*  74:147 */     setInvalid();
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.NameRange
 * JD-Core Version:    0.7.0.1
 */