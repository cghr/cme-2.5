/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.DoubleHelper;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ class DoubleValue
/*   7:    */   extends NumberValue
/*   8:    */   implements ParsedThing
/*   9:    */ {
/*  10: 34 */   private static Logger logger = Logger.getLogger(DoubleValue.class);
/*  11:    */   private double value;
/*  12:    */   
/*  13:    */   public DoubleValue() {}
/*  14:    */   
/*  15:    */   DoubleValue(double v)
/*  16:    */   {
/*  17: 56 */     this.value = v;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public DoubleValue(String s)
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 68 */       this.value = Double.parseDouble(s);
/*  25:    */     }
/*  26:    */     catch (NumberFormatException e)
/*  27:    */     {
/*  28: 72 */       logger.warn(e, e);
/*  29: 73 */       this.value = 0.0D;
/*  30:    */     }
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int read(byte[] data, int pos)
/*  34:    */   {
/*  35: 86 */     this.value = DoubleHelper.getIEEEDouble(data, pos);
/*  36:    */     
/*  37: 88 */     return 8;
/*  38:    */   }
/*  39:    */   
/*  40:    */   byte[] getBytes()
/*  41:    */   {
/*  42: 98 */     byte[] data = new byte[9];
/*  43: 99 */     data[0] = Token.DOUBLE.getCode();
/*  44:    */     
/*  45:101 */     DoubleHelper.getIEEEBytes(this.value, data, 1);
/*  46:    */     
/*  47:103 */     return data;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public double getValue()
/*  51:    */   {
/*  52:113 */     return this.value;
/*  53:    */   }
/*  54:    */   
/*  55:    */   void handleImportedCellReferences() {}
/*  56:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.DoubleValue
 * JD-Core Version:    0.7.0.1
 */