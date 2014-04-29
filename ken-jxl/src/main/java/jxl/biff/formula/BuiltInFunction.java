/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ class BuiltInFunction
/*  10:    */   extends Operator
/*  11:    */   implements ParsedThing
/*  12:    */ {
/*  13: 38 */   private static Logger logger = Logger.getLogger(BuiltInFunction.class);
/*  14:    */   private Function function;
/*  15:    */   private WorkbookSettings settings;
/*  16:    */   
/*  17:    */   public BuiltInFunction(WorkbookSettings ws)
/*  18:    */   {
/*  19: 56 */     this.settings = ws;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public BuiltInFunction(Function f, WorkbookSettings ws)
/*  23:    */   {
/*  24: 67 */     this.function = f;
/*  25: 68 */     this.settings = ws;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int read(byte[] data, int pos)
/*  29:    */   {
/*  30: 80 */     int index = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  31: 81 */     this.function = Function.getFunction(index);
/*  32: 82 */     Assert.verify(this.function != Function.UNKNOWN, "function code " + index);
/*  33: 83 */     return 2;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void getOperands(Stack s)
/*  37:    */   {
/*  38: 94 */     ParseItem[] items = new ParseItem[this.function.getNumArgs()];
/*  39: 96 */     for (int i = this.function.getNumArgs() - 1; i >= 0; i--)
/*  40:    */     {
/*  41: 98 */       ParseItem pi = (ParseItem)s.pop();
/*  42:    */       
/*  43:100 */       items[i] = pi;
/*  44:    */     }
/*  45:103 */     for (int i = 0; i < this.function.getNumArgs(); i++) {
/*  46:105 */       add(items[i]);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void getString(StringBuffer buf)
/*  51:    */   {
/*  52:116 */     buf.append(this.function.getName(this.settings));
/*  53:117 */     buf.append('(');
/*  54:    */     
/*  55:119 */     int numArgs = this.function.getNumArgs();
/*  56:121 */     if (numArgs > 0)
/*  57:    */     {
/*  58:123 */       ParseItem[] operands = getOperands();
/*  59:    */       
/*  60:    */ 
/*  61:126 */       operands[0].getString(buf);
/*  62:128 */       for (int i = 1; i < numArgs; i++)
/*  63:    */       {
/*  64:130 */         buf.append(',');
/*  65:131 */         operands[i].getString(buf);
/*  66:    */       }
/*  67:    */     }
/*  68:135 */     buf.append(')');
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  72:    */   {
/*  73:147 */     ParseItem[] operands = getOperands();
/*  74:149 */     for (int i = 0; i < operands.length; i++) {
/*  75:151 */       operands[i].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  80:    */   {
/*  81:167 */     ParseItem[] operands = getOperands();
/*  82:168 */     for (int i = 0; i < operands.length; i++) {
/*  83:170 */       operands[i].columnInserted(sheetIndex, col, currentSheet);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/*  88:    */   {
/*  89:186 */     ParseItem[] operands = getOperands();
/*  90:187 */     for (int i = 0; i < operands.length; i++) {
/*  91:189 */       operands[i].columnRemoved(sheetIndex, col, currentSheet);
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/*  96:    */   {
/*  97:206 */     ParseItem[] operands = getOperands();
/*  98:207 */     for (int i = 0; i < operands.length; i++) {
/*  99:209 */       operands[i].rowInserted(sheetIndex, row, currentSheet);
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 104:    */   {
/* 105:225 */     ParseItem[] operands = getOperands();
/* 106:226 */     for (int i = 0; i < operands.length; i++) {
/* 107:228 */       operands[i].rowRemoved(sheetIndex, row, currentSheet);
/* 108:    */     }
/* 109:    */   }
/* 110:    */   
/* 111:    */   void handleImportedCellReferences()
/* 112:    */   {
/* 113:239 */     ParseItem[] operands = getOperands();
/* 114:240 */     for (int i = 0; i < operands.length; i++) {
/* 115:242 */       operands[i].handleImportedCellReferences();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   byte[] getBytes()
/* 120:    */   {
/* 121:254 */     ParseItem[] operands = getOperands();
/* 122:255 */     byte[] data = new byte[0];
/* 123:257 */     for (int i = 0; i < operands.length; i++)
/* 124:    */     {
/* 125:259 */       byte[] opdata = operands[i].getBytes();
/* 126:    */       
/* 127:    */ 
/* 128:262 */       byte[] newdata = new byte[data.length + opdata.length];
/* 129:263 */       System.arraycopy(data, 0, newdata, 0, data.length);
/* 130:264 */       System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
/* 131:265 */       data = newdata;
/* 132:    */     }
/* 133:269 */     byte[] newdata = new byte[data.length + 3];
/* 134:270 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 135:271 */     newdata[data.length] = (!useAlternateCode() ? Token.FUNCTION.getCode() : 
/* 136:272 */       Token.FUNCTION.getCode2());
/* 137:273 */     IntegerHelper.getTwoBytes(this.function.getCode(), newdata, data.length + 1);
/* 138:    */     
/* 139:275 */     return newdata;
/* 140:    */   }
/* 141:    */   
/* 142:    */   int getPrecedence()
/* 143:    */   {
/* 144:286 */     return 3;
/* 145:    */   }
/* 146:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.BuiltInFunction
 * JD-Core Version:    0.7.0.1
 */