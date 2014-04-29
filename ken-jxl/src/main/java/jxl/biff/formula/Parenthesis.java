/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ 
/*   5:    */ class Parenthesis
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
/*  16: 53 */     ParseItem pi = (ParseItem)s.pop();
/*  17:    */     
/*  18: 55 */     add(pi);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void getString(StringBuffer buf)
/*  22:    */   {
/*  23: 60 */     ParseItem[] operands = getOperands();
/*  24: 61 */     buf.append('(');
/*  25: 62 */     operands[0].getString(buf);
/*  26: 63 */     buf.append(')');
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  30:    */   {
/*  31: 75 */     ParseItem[] operands = getOperands();
/*  32: 76 */     operands[0].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  33:    */   }
/*  34:    */   
/*  35:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  36:    */   {
/*  37: 91 */     ParseItem[] operands = getOperands();
/*  38: 92 */     operands[0].columnInserted(sheetIndex, col, currentSheet);
/*  39:    */   }
/*  40:    */   
/*  41:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  42:    */   {
/*  43:107 */     ParseItem[] operands = getOperands();
/*  44:108 */     operands[0].columnRemoved(sheetIndex, col, currentSheet);
/*  45:    */   }
/*  46:    */   
/*  47:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  48:    */   {
/*  49:123 */     ParseItem[] operands = getOperands();
/*  50:124 */     operands[0].rowInserted(sheetIndex, row, currentSheet);
/*  51:    */   }
/*  52:    */   
/*  53:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/*  54:    */   {
/*  55:139 */     ParseItem[] operands = getOperands();
/*  56:140 */     operands[0].rowRemoved(sheetIndex, row, currentSheet);
/*  57:    */   }
/*  58:    */   
/*  59:    */   void handleImportedCellReferences()
/*  60:    */   {
/*  61:150 */     ParseItem[] operands = getOperands();
/*  62:151 */     operands[0].handleImportedCellReferences();
/*  63:    */   }
/*  64:    */   
/*  65:    */   Token getToken()
/*  66:    */   {
/*  67:161 */     return Token.PARENTHESIS;
/*  68:    */   }
/*  69:    */   
/*  70:    */   byte[] getBytes()
/*  71:    */   {
/*  72:172 */     ParseItem[] operands = getOperands();
/*  73:173 */     byte[] data = operands[0].getBytes();
/*  74:    */     
/*  75:    */ 
/*  76:176 */     byte[] newdata = new byte[data.length + 1];
/*  77:177 */     System.arraycopy(data, 0, newdata, 0, data.length);
/*  78:178 */     newdata[data.length] = getToken().getCode();
/*  79:    */     
/*  80:180 */     return newdata;
/*  81:    */   }
/*  82:    */   
/*  83:    */   int getPrecedence()
/*  84:    */   {
/*  85:191 */     return 4;
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Parenthesis
 * JD-Core Version:    0.7.0.1
 */