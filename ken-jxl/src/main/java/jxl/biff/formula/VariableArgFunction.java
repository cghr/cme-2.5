/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.Stack;
/*   4:    */ import jxl.WorkbookSettings;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ class VariableArgFunction
/*   9:    */   extends Operator
/*  10:    */   implements ParsedThing
/*  11:    */ {
/*  12: 38 */   private static Logger logger = Logger.getLogger(VariableArgFunction.class);
/*  13:    */   private Function function;
/*  14:    */   private int arguments;
/*  15:    */   private boolean readFromSheet;
/*  16:    */   private WorkbookSettings settings;
/*  17:    */   
/*  18:    */   public VariableArgFunction(WorkbookSettings ws)
/*  19:    */   {
/*  20: 66 */     this.readFromSheet = true;
/*  21: 67 */     this.settings = ws;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public VariableArgFunction(Function f, int a, WorkbookSettings ws)
/*  25:    */   {
/*  26: 78 */     this.function = f;
/*  27: 79 */     this.arguments = a;
/*  28: 80 */     this.readFromSheet = false;
/*  29: 81 */     this.settings = ws;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int read(byte[] data, int pos)
/*  33:    */     throws FormulaException
/*  34:    */   {
/*  35: 94 */     this.arguments = data[pos];
/*  36: 95 */     int index = IntegerHelper.getInt(data[(pos + 1)], data[(pos + 2)]);
/*  37: 96 */     this.function = Function.getFunction(index);
/*  38: 98 */     if (this.function == Function.UNKNOWN) {
/*  39:100 */       throw new FormulaException(FormulaException.UNRECOGNIZED_FUNCTION, 
/*  40:101 */         index);
/*  41:    */     }
/*  42:104 */     return 3;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void getOperands(Stack s)
/*  46:    */   {
/*  47:113 */     ParseItem[] items = new ParseItem[this.arguments];
/*  48:115 */     for (int i = this.arguments - 1; i >= 0; i--)
/*  49:    */     {
/*  50:117 */       ParseItem pi = (ParseItem)s.pop();
/*  51:    */       
/*  52:119 */       items[i] = pi;
/*  53:    */     }
/*  54:122 */     for (int i = 0; i < this.arguments; i++) {
/*  55:124 */       add(items[i]);
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void getString(StringBuffer buf)
/*  60:    */   {
/*  61:130 */     buf.append(this.function.getName(this.settings));
/*  62:131 */     buf.append('(');
/*  63:133 */     if (this.arguments > 0)
/*  64:    */     {
/*  65:135 */       ParseItem[] operands = getOperands();
/*  66:136 */       if (this.readFromSheet)
/*  67:    */       {
/*  68:139 */         operands[0].getString(buf);
/*  69:141 */         for (int i = 1; i < this.arguments; i++)
/*  70:    */         {
/*  71:143 */           buf.append(',');
/*  72:144 */           operands[i].getString(buf);
/*  73:    */         }
/*  74:    */       }
/*  75:    */       else
/*  76:    */       {
/*  77:151 */         operands[(this.arguments - 1)].getString(buf);
/*  78:153 */         for (int i = this.arguments - 2; i >= 0; i--)
/*  79:    */         {
/*  80:155 */           buf.append(',');
/*  81:156 */           operands[i].getString(buf);
/*  82:    */         }
/*  83:    */       }
/*  84:    */     }
/*  85:161 */     buf.append(')');
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void adjustRelativeCellReferences(int colAdjust, int rowAdjust)
/*  89:    */   {
/*  90:173 */     ParseItem[] operands = getOperands();
/*  91:175 */     for (int i = 0; i < operands.length; i++) {
/*  92:177 */       operands[i].adjustRelativeCellReferences(colAdjust, rowAdjust);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   void columnInserted(int sheetIndex, int col, boolean currentSheet)
/*  97:    */   {
/*  98:193 */     ParseItem[] operands = getOperands();
/*  99:194 */     for (int i = 0; i < operands.length; i++) {
/* 100:196 */       operands[i].columnInserted(sheetIndex, col, currentSheet);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   void columnRemoved(int sheetIndex, int col, boolean currentSheet)
/* 105:    */   {
/* 106:212 */     ParseItem[] operands = getOperands();
/* 107:213 */     for (int i = 0; i < operands.length; i++) {
/* 108:215 */       operands[i].columnRemoved(sheetIndex, col, currentSheet);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   void rowInserted(int sheetIndex, int row, boolean currentSheet)
/* 113:    */   {
/* 114:231 */     ParseItem[] operands = getOperands();
/* 115:232 */     for (int i = 0; i < operands.length; i++) {
/* 116:234 */       operands[i].rowInserted(sheetIndex, row, currentSheet);
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   void rowRemoved(int sheetIndex, int row, boolean currentSheet)
/* 121:    */   {
/* 122:250 */     ParseItem[] operands = getOperands();
/* 123:251 */     for (int i = 0; i < operands.length; i++) {
/* 124:253 */       operands[i].rowRemoved(sheetIndex, row, currentSheet);
/* 125:    */     }
/* 126:    */   }
/* 127:    */   
/* 128:    */   void handleImportedCellReferences()
/* 129:    */   {
/* 130:264 */     ParseItem[] operands = getOperands();
/* 131:265 */     for (int i = 0; i < operands.length; i++) {
/* 132:267 */       operands[i].handleImportedCellReferences();
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   Function getFunction()
/* 137:    */   {
/* 138:276 */     return this.function;
/* 139:    */   }
/* 140:    */   
/* 141:    */   byte[] getBytes()
/* 142:    */   {
/* 143:286 */     handleSpecialCases();
/* 144:    */     
/* 145:    */ 
/* 146:289 */     ParseItem[] operands = getOperands();
/* 147:290 */     byte[] data = new byte[0];
/* 148:292 */     for (int i = 0; i < operands.length; i++)
/* 149:    */     {
/* 150:294 */       byte[] opdata = operands[i].getBytes();
/* 151:    */       
/* 152:    */ 
/* 153:297 */       byte[] newdata = new byte[data.length + opdata.length];
/* 154:298 */       System.arraycopy(data, 0, newdata, 0, data.length);
/* 155:299 */       System.arraycopy(opdata, 0, newdata, data.length, opdata.length);
/* 156:300 */       data = newdata;
/* 157:    */     }
/* 158:304 */     byte[] newdata = new byte[data.length + 4];
/* 159:305 */     System.arraycopy(data, 0, newdata, 0, data.length);
/* 160:306 */     newdata[data.length] = (!useAlternateCode() ? 
/* 161:307 */       Token.FUNCTIONVARARG.getCode() : Token.FUNCTIONVARARG.getCode2());
/* 162:308 */     newdata[(data.length + 1)] = ((byte)this.arguments);
/* 163:309 */     IntegerHelper.getTwoBytes(this.function.getCode(), newdata, data.length + 2);
/* 164:    */     
/* 165:311 */     return newdata;
/* 166:    */   }
/* 167:    */   
/* 168:    */   int getPrecedence()
/* 169:    */   {
/* 170:322 */     return 3;
/* 171:    */   }
/* 172:    */   
/* 173:    */   private void handleSpecialCases()
/* 174:    */   {
/* 175:332 */     if (this.function == Function.SUMPRODUCT)
/* 176:    */     {
/* 177:335 */       ParseItem[] operands = getOperands();
/* 178:337 */       for (int i = operands.length - 1; i >= 0; i--) {
/* 179:339 */         if ((operands[i] instanceof Area)) {
/* 180:341 */           operands[i].setAlternateCode();
/* 181:    */         }
/* 182:    */       }
/* 183:    */     }
/* 184:    */   }
/* 185:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.VariableArgFunction
 * JD-Core Version:    0.7.0.1
 */