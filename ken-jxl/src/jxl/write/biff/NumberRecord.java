/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.text.DecimalFormat;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import jxl.CellType;
/*   6:    */ import jxl.NumberCell;
/*   7:    */ import jxl.biff.DoubleHelper;
/*   8:    */ import jxl.biff.Type;
/*   9:    */ import jxl.biff.XFRecord;
/*  10:    */ import jxl.format.CellFormat;
/*  11:    */ 
/*  12:    */ public abstract class NumberRecord
/*  13:    */   extends CellValue
/*  14:    */ {
/*  15:    */   private double value;
/*  16:    */   private NumberFormat format;
/*  17: 51 */   private static DecimalFormat defaultFormat = new DecimalFormat("#.###");
/*  18:    */   
/*  19:    */   protected NumberRecord(int c, int r, double val)
/*  20:    */   {
/*  21: 62 */     super(Type.NUMBER, c, r);
/*  22: 63 */     this.value = val;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected NumberRecord(int c, int r, double val, CellFormat st)
/*  26:    */   {
/*  27: 77 */     super(Type.NUMBER, c, r, st);
/*  28: 78 */     this.value = val;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected NumberRecord(NumberCell nc)
/*  32:    */   {
/*  33: 88 */     super(Type.NUMBER, nc);
/*  34: 89 */     this.value = nc.getValue();
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected NumberRecord(int c, int r, NumberRecord nr)
/*  38:    */   {
/*  39:101 */     super(Type.NUMBER, c, r, nr);
/*  40:102 */     this.value = nr.value;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public CellType getType()
/*  44:    */   {
/*  45:112 */     return CellType.NUMBER;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public byte[] getData()
/*  49:    */   {
/*  50:122 */     byte[] celldata = super.getData();
/*  51:123 */     byte[] data = new byte[celldata.length + 8];
/*  52:124 */     System.arraycopy(celldata, 0, data, 0, celldata.length);
/*  53:125 */     DoubleHelper.getIEEEBytes(this.value, data, celldata.length);
/*  54:    */     
/*  55:127 */     return data;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getContents()
/*  59:    */   {
/*  60:139 */     if (this.format == null)
/*  61:    */     {
/*  62:141 */       this.format = ((XFRecord)getCellFormat()).getNumberFormat();
/*  63:142 */       if (this.format == null) {
/*  64:144 */         this.format = defaultFormat;
/*  65:    */       }
/*  66:    */     }
/*  67:147 */     return this.format.format(this.value);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public double getValue()
/*  71:    */   {
/*  72:157 */     return this.value;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setValue(double val)
/*  76:    */   {
/*  77:167 */     this.value = val;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public NumberFormat getNumberFormat()
/*  81:    */   {
/*  82:178 */     return null;
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.NumberRecord
 * JD-Core Version:    0.7.0.1
 */