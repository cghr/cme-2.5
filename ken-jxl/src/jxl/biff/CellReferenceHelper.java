/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.formula.ExternalSheet;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ public final class CellReferenceHelper
/*   7:    */ {
/*   8: 39 */   private static Logger logger = Logger.getLogger(CellReferenceHelper.class);
/*   9:    */   private static final char fixedInd = '$';
/*  10:    */   private static final char sheetInd = '!';
/*  11:    */   
/*  12:    */   public static void getCellReference(int column, int row, StringBuffer buf)
/*  13:    */   {
/*  14: 68 */     getColumnReference(column, buf);
/*  15:    */     
/*  16:    */ 
/*  17: 71 */     buf.append(Integer.toString(row + 1));
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static void getCellReference(int column, boolean colabs, int row, boolean rowabs, StringBuffer buf)
/*  21:    */   {
/*  22: 87 */     if (colabs) {
/*  23: 89 */       buf.append('$');
/*  24:    */     }
/*  25: 93 */     getColumnReference(column, buf);
/*  26: 95 */     if (rowabs) {
/*  27: 97 */       buf.append('$');
/*  28:    */     }
/*  29:101 */     buf.append(Integer.toString(row + 1));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static String getColumnReference(int column)
/*  33:    */   {
/*  34:112 */     StringBuffer buf = new StringBuffer();
/*  35:113 */     getColumnReference(column, buf);
/*  36:114 */     return buf.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static void getColumnReference(int column, StringBuffer buf)
/*  40:    */   {
/*  41:125 */     int v = column / 26;
/*  42:126 */     int r = column % 26;
/*  43:    */     
/*  44:128 */     StringBuffer tmp = new StringBuffer();
/*  45:129 */     while (v != 0)
/*  46:    */     {
/*  47:131 */       char col = (char)(65 + r);
/*  48:    */       
/*  49:133 */       tmp.append(col);
/*  50:    */       
/*  51:135 */       r = v % 26 - 1;
/*  52:136 */       v /= 26;
/*  53:    */     }
/*  54:139 */     char col = (char)(65 + r);
/*  55:140 */     tmp.append(col);
/*  56:143 */     for (int i = tmp.length() - 1; i >= 0; i--) {
/*  57:145 */       buf.append(tmp.charAt(i));
/*  58:    */     }
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static void getCellReference(int sheet, int column, int row, ExternalSheet workbook, StringBuffer buf)
/*  62:    */   {
/*  63:164 */     String name = workbook.getExternalSheetName(sheet);
/*  64:165 */     buf.append(StringHelper.replace(name, "'", "''"));
/*  65:166 */     buf.append('!');
/*  66:167 */     getCellReference(column, row, buf);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static void getCellReference(int sheet, int column, boolean colabs, int row, boolean rowabs, ExternalSheet workbook, StringBuffer buf)
/*  70:    */   {
/*  71:188 */     String name = workbook.getExternalSheetName(sheet);
/*  72:189 */     buf.append(name);
/*  73:190 */     buf.append('!');
/*  74:191 */     getCellReference(column, colabs, row, rowabs, buf);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static String getCellReference(int sheet, int column, int row, ExternalSheet workbook)
/*  78:    */   {
/*  79:208 */     StringBuffer sb = new StringBuffer();
/*  80:209 */     getCellReference(sheet, column, row, workbook, sb);
/*  81:210 */     return sb.toString();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static String getCellReference(int column, int row)
/*  85:    */   {
/*  86:223 */     StringBuffer buf = new StringBuffer();
/*  87:224 */     getCellReference(column, row, buf);
/*  88:225 */     return buf.toString();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static int getColumn(String s)
/*  92:    */   {
/*  93:236 */     int colnum = 0;
/*  94:237 */     int numindex = getNumberIndex(s);
/*  95:    */     
/*  96:239 */     String s2 = s.toUpperCase();
/*  97:    */     
/*  98:241 */     int startPos = s.lastIndexOf('!') + 1;
/*  99:242 */     if (s.charAt(startPos) == '$') {
/* 100:244 */       startPos++;
/* 101:    */     }
/* 102:247 */     int endPos = numindex;
/* 103:248 */     if (s.charAt(numindex - 1) == '$') {
/* 104:250 */       endPos--;
/* 105:    */     }
/* 106:253 */     for (int i = startPos; i < endPos; i++)
/* 107:    */     {
/* 108:256 */       if (i != startPos) {
/* 109:258 */         colnum = (colnum + 1) * 26;
/* 110:    */       }
/* 111:260 */       colnum += s2.charAt(i) - 'A';
/* 112:    */     }
/* 113:263 */     return colnum;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static int getRow(String s)
/* 117:    */   {
/* 118:    */     try
/* 119:    */     {
/* 120:273 */       return Integer.parseInt(s.substring(getNumberIndex(s))) - 1;
/* 121:    */     }
/* 122:    */     catch (NumberFormatException e)
/* 123:    */     {
/* 124:277 */       logger.warn(e, e);
/* 125:    */     }
/* 126:278 */     return 65535;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private static int getNumberIndex(String s)
/* 130:    */   {
/* 131:288 */     boolean numberFound = false;
/* 132:289 */     int pos = s.lastIndexOf('!') + 1;
/* 133:290 */     char c = '\000';
/* 134:292 */     while ((!numberFound) && (pos < s.length()))
/* 135:    */     {
/* 136:294 */       c = s.charAt(pos);
/* 137:296 */       if ((c >= '0') && (c <= '9')) {
/* 138:298 */         numberFound = true;
/* 139:    */       } else {
/* 140:302 */         pos++;
/* 141:    */       }
/* 142:    */     }
/* 143:306 */     return pos;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static boolean isColumnRelative(String s)
/* 147:    */   {
/* 148:317 */     return s.charAt(0) != '$';
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static boolean isRowRelative(String s)
/* 152:    */   {
/* 153:328 */     return s.charAt(getNumberIndex(s) - 1) != '$';
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static String getSheet(String ref)
/* 157:    */   {
/* 158:339 */     int sheetPos = ref.lastIndexOf('!');
/* 159:340 */     if (sheetPos == -1) {
/* 160:342 */       return "";
/* 161:    */     }
/* 162:345 */     return ref.substring(0, sheetPos);
/* 163:    */   }
/* 164:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.CellReferenceHelper
 * JD-Core Version:    0.7.0.1
 */