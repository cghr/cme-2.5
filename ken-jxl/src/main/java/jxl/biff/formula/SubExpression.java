/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ 
/*   6:    */ abstract class SubExpression
/*   7:    */   extends Operand
/*   8:    */   implements ParsedThing
/*   9:    */ {
/*  10:    */   private int length;
/*  11:    */   private ParseItem[] subExpression;
/*  12:    */   
/*  13:    */   public int read(byte[] data, int pos)
/*  14:    */   {
/*  15: 57 */     this.length = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  16: 58 */     return 2;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void getOperands(Stack s) {}
/*  20:    */   
/*  21:    */   byte[] getBytes()
/*  22:    */   {
/*  23: 77 */     return null;
/*  24:    */   }
/*  25:    */   
/*  26:    */   int getPrecedence()
/*  27:    */   {
/*  28: 89 */     return 5;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int getLength()
/*  32:    */   {
/*  33: 99 */     return this.length;
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected final void setLength(int l)
/*  37:    */   {
/*  38:104 */     this.length = l;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setSubExpression(ParseItem[] pi)
/*  42:    */   {
/*  43:109 */     this.subExpression = pi;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected ParseItem[] getSubExpression()
/*  47:    */   {
/*  48:114 */     return this.subExpression;
/*  49:    */   }
/*  50:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.SubExpression
 * JD-Core Version:    0.7.0.1
 */