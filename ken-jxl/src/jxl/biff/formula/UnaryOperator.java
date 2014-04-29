/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ 
/*   5:    */ abstract class UnaryOperator
/*   6:    */   extends Operator
/*   7:    */   implements ParsedThing
/*   8:    */ {
/*   9:    */   public int read(byte[] data, int pos)
/*  10:    */   {
/*  11: 45 */     return 0;
/*  12:    */   }
/*  13:    */   
/*  14:    */   public void getOperands(Stack s)
/*  15:    */   {
/*  16: 53 */     ParseItem o1 = (ParseItem)s.pop();
/*  17:    */     
/*  18: 55 */     add(o1);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void getString(StringBuffer buf)
/*  22:    */   {
/*  23: 65 */     ParseItem[] operands = getOperands();
/*  24: 66 */     buf.append(getSymbol());
/*  25: 67 */     operands[0].getString(buf);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  29:    */   {
/*  30: 79 */     ParseItem[] operands = getOperands();
/*  31: 80 */     operands[0].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  32:    */   }
/*  33:    */   
/*  34:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  35:    */   {
/*  36: 95 */     ParseItem[] operands = getOperands();
/*  37: 96 */     operands[0].columnInserted(sheetIndex, col, currentSheet);
/*  38:    */   }
/*  39:    */   
/*  40:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  41:    */   {
/*  42:111 */     ParseItem[] operands = getOperands();
/*  43:112 */     operands[0].columnRemoved(sheetIndex, col, currentSheet);
/*  44:    */   }
/*  45:    */   
/*  46:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  47:    */   {
/*  48:127 */     ParseItem[] operands = getOperands();
/*  49:128 */     operands[0].rowInserted(sheetIndex, row, currentSheet);
/*  50:    */   }
/*  51:    */   
/*  52:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/*  53:    */   {
/*  54:143 */     ParseItem[] operands = getOperands();
/*  55:144 */     operands[0].rowRemoved(sheetIndex, row, currentSheet);
/*  56:    */   }
/*  57:    */   
/*  58:    */   byte[] getBytes()
/*  59:    */   {
/*  60:155 */     ParseItem[] operands = getOperands();
/*  61:156 */     byte[] data = operands[0].getBytes();
/*  62:    */     
/*  63:    */ 
/*  64:159 */     byte[] newdata = new byte[data.length + 1];
/*  65:160 */     System.arraycopy(data, 0, newdata, 0, data.length);
/*  66:161 */     newdata[data.length] = getToken().getCode();
/*  67:    */     
/*  68:163 */     return newdata;
/*  69:    */   }
/*  70:    */   
/*  71:    */   abstract String getSymbol();
/*  72:    */   
/*  73:    */   abstract Token getToken();
/*  74:    */   
/*  75:    */   void handleImportedCellReferences()
/*  76:    */   {
/*  77:187 */     ParseItem[] operands = getOperands();
/*  78:188 */     operands[0].handleImportedCellReferences();
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.UnaryOperator
 * JD-Core Version:    0.7.0.1
 */