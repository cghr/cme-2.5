/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ class IntegerValue
/*   7:    */   extends NumberValue
/*   8:    */   implements ParsedThing
/*   9:    */ {
/*  10: 34 */   private static Logger logger = Logger.getLogger(IntegerValue.class);
/*  11:    */   private double value;
/*  12:    */   private boolean outOfRange;
/*  13:    */   
/*  14:    */   public IntegerValue()
/*  15:    */   {
/*  16: 51 */     this.outOfRange = false;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public IntegerValue(String s)
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 61 */       this.value = Integer.parseInt(s);
/*  24:    */     }
/*  25:    */     catch (NumberFormatException e)
/*  26:    */     {
/*  27: 65 */       logger.warn(e, e);
/*  28: 66 */       this.value = 0.0D;
/*  29:    */     }
/*  30: 69 */     short v = (short)(int)this.value;
/*  31: 70 */     this.outOfRange = (this.value != v);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int read(byte[] data, int pos)
/*  35:    */   {
/*  36: 82 */     this.value = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  37:    */     
/*  38: 84 */     return 2;
/*  39:    */   }
/*  40:    */   
/*  41:    */   byte[] getBytes()
/*  42:    */   {
/*  43: 94 */     byte[] data = new byte[3];
/*  44: 95 */     data[0] = Token.INTEGER.getCode();
/*  45:    */     
/*  46: 97 */     IntegerHelper.getTwoBytes((int)this.value, data, 1);
/*  47:    */     
/*  48: 99 */     return data;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public double getValue()
/*  52:    */   {
/*  53:109 */     return this.value;
/*  54:    */   }
/*  55:    */   
/*  56:    */   boolean isOutOfRange()
/*  57:    */   {
/*  58:119 */     return this.outOfRange;
/*  59:    */   }
/*  60:    */   
/*  61:    */   void handleImportedCellReferences() {}
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.IntegerValue
 * JD-Core Version:    0.7.0.1
 */