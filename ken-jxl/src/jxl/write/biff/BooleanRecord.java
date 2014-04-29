/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.BooleanCell;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ import jxl.format.CellFormat;
/*   7:    */ 
/*   8:    */ public abstract class BooleanRecord
/*   9:    */   extends CellValue
/*  10:    */ {
/*  11:    */   private boolean value;
/*  12:    */   
/*  13:    */   protected BooleanRecord(int c, int r, boolean val)
/*  14:    */   {
/*  15: 49 */     super(Type.BOOLERR, c, r);
/*  16: 50 */     this.value = val;
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected BooleanRecord(int c, int r, boolean val, CellFormat st)
/*  20:    */   {
/*  21: 64 */     super(Type.BOOLERR, c, r, st);
/*  22: 65 */     this.value = val;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected BooleanRecord(BooleanCell nc)
/*  26:    */   {
/*  27: 75 */     super(Type.BOOLERR, nc);
/*  28: 76 */     this.value = nc.getValue();
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected BooleanRecord(int c, int r, BooleanRecord br)
/*  32:    */   {
/*  33: 88 */     super(Type.BOOLERR, c, r, br);
/*  34: 89 */     this.value = br.value;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean getValue()
/*  38:    */   {
/*  39:102 */     return this.value;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getContents()
/*  43:    */   {
/*  44:113 */     return new Boolean(this.value).toString();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public CellType getType()
/*  48:    */   {
/*  49:123 */     return CellType.BOOLEAN;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected void setValue(boolean val)
/*  53:    */   {
/*  54:133 */     this.value = val;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public byte[] getData()
/*  58:    */   {
/*  59:143 */     byte[] celldata = super.getData();
/*  60:144 */     byte[] data = new byte[celldata.length + 2];
/*  61:145 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  62:147 */     if (this.value) {
/*  63:149 */       data[celldata.length] = 1;
/*  64:    */     }
/*  65:152 */     return data;
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BooleanRecord
 * JD-Core Version:    0.7.0.1
 */