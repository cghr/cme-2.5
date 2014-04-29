/*  1:   */ package jxl.biff.formula;
/*  2:   */ 
/*  3:   */ import java.util.Stack;
/*  4:   */ 
/*  5:   */ abstract class Operator
/*  6:   */   extends ParseItem
/*  7:   */ {
/*  8:   */   private ParseItem[] operands;
/*  9:   */   
/* 10:   */   public Operator()
/* 11:   */   {
/* 12:41 */     this.operands = new ParseItem[0];
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected void setOperandAlternateCode()
/* 16:   */   {
/* 17:49 */     for (int i = 0; i < this.operands.length; i++) {
/* 18:51 */       this.operands[i].setAlternateCode();
/* 19:   */     }
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected void add(ParseItem n)
/* 23:   */   {
/* 24:60 */     n.setParent(this);
/* 25:   */     
/* 26:   */ 
/* 27:63 */     ParseItem[] newOperands = new ParseItem[this.operands.length + 1];
/* 28:64 */     System.arraycopy(this.operands, 0, newOperands, 0, this.operands.length);
/* 29:65 */     newOperands[this.operands.length] = n;
/* 30:66 */     this.operands = newOperands;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public abstract void getOperands(Stack paramStack);
/* 34:   */   
/* 35:   */   protected ParseItem[] getOperands()
/* 36:   */   {
/* 37:79 */     return this.operands;
/* 38:   */   }
/* 39:   */   
/* 40:   */   abstract int getPrecedence();
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Operator
 * JD-Core Version:    0.7.0.1
 */