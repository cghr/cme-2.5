/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ abstract class BinaryOperator
/*   7:    */   extends Operator
/*   8:    */   implements ParsedThing
/*   9:    */ {
/*  10: 32 */   private static final Logger logger = Logger.getLogger(BinaryOperator.class);
/*  11:    */   
/*  12:    */   public int read(byte[] data, int pos)
/*  13:    */   {
/*  14: 50 */     return 0;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public void getOperands(Stack s)
/*  18:    */   {
/*  19: 60 */     ParseItem o1 = (ParseItem)s.pop();
/*  20: 61 */     ParseItem o2 = (ParseItem)s.pop();
/*  21:    */     
/*  22: 63 */     add(o1);
/*  23: 64 */     add(o2);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void getString(StringBuffer buf)
/*  27:    */   {
/*  28: 74 */     ParseItem[] operands = getOperands();
/*  29: 75 */     operands[1].getString(buf);
/*  30: 76 */     buf.append(getSymbol());
/*  31: 77 */     operands[0].getString(buf);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  35:    */   {
/*  36: 89 */     ParseItem[] operands = getOperands();
/*  37: 90 */     operands[1].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  38: 91 */     operands[0].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  39:    */   }
/*  40:    */   
/*  41:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  42:    */   {
/*  43:106 */     ParseItem[] operands = getOperands();
/*  44:107 */     operands[1].columnInserted(sheetIndex, col, currentSheet);
/*  45:108 */     operands[0].columnInserted(sheetIndex, col, currentSheet);
/*  46:    */   }
/*  47:    */   
/*  48:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  49:    */   {
/*  50:123 */     ParseItem[] operands = getOperands();
/*  51:124 */     operands[1].columnRemoved(sheetIndex, col, currentSheet);
/*  52:125 */     operands[0].columnRemoved(sheetIndex, col, currentSheet);
/*  53:    */   }
/*  54:    */   
/*  55:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  56:    */   {
/*  57:140 */     ParseItem[] operands = getOperands();
/*  58:141 */     operands[1].rowInserted(sheetIndex, row, currentSheet);
/*  59:142 */     operands[0].rowInserted(sheetIndex, row, currentSheet);
/*  60:    */   }
/*  61:    */   
/*  62:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/*  63:    */   {
/*  64:157 */     ParseItem[] operands = getOperands();
/*  65:158 */     operands[1].rowRemoved(sheetIndex, row, currentSheet);
/*  66:159 */     operands[0].rowRemoved(sheetIndex, row, currentSheet);
/*  67:    */   }
/*  68:    */   
/*  69:    */   byte[] getBytes()
/*  70:    */   {
/*  71:170 */     ParseItem[] operands = getOperands();
/*  72:171 */     byte[] data = new byte[0];
/*  73:174 */     for (int i = operands.length - 1; i >= 0; i--)
/*  74:    */     {
/*  75:176 */       byte[] opdata = operands[i].getBytes();
/*  76:    */       
/*  77:    */ 
/*  78:179 */       byte[] newdata = new byte[data.length + opdata.length];
/*  79:180 */       System.arraycopy(data, 0, newdata, 0, data.length);
/*  80:181 */       System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
/*  81:182 */       data = newdata;
/*  82:    */     }
/*  83:186 */     byte[] newdata = new byte[data.length + 1];
/*  84:187 */     System.arraycopy(data, 0, newdata, 0, data.length);
/*  85:188 */     newdata[data.length] = getToken().getCode();
/*  86:    */     
/*  87:190 */     return newdata;
/*  88:    */   }
/*  89:    */   
/*  90:    */   abstract String getSymbol();
/*  91:    */   
/*  92:    */   abstract Token getToken();
/*  93:    */   
/*  94:    */   void handleImportedCellReferences()
/*  95:    */   {
/*  96:214 */     ParseItem[] operands = getOperands();
/*  97:215 */     operands[0].handleImportedCellReferences();
/*  98:216 */     operands[1].handleImportedCellReferences();
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.BinaryOperator
 * JD-Core Version:    0.7.0.1
 */