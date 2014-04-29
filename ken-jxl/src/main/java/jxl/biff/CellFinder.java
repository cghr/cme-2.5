/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import java.util.regex.Matcher;
/*   4:    */ import java.util.regex.Pattern;
/*   5:    */ import jxl.Cell;
/*   6:    */ import jxl.CellType;
/*   7:    */ import jxl.LabelCell;
/*   8:    */ import jxl.Sheet;
/*   9:    */ 
/*  10:    */ public class CellFinder
/*  11:    */ {
/*  12:    */   private Sheet sheet;
/*  13:    */   
/*  14:    */   public CellFinder(Sheet s)
/*  15:    */   {
/*  16: 40 */     this.sheet = s;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public Cell findCell(String contents, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  20:    */   {
/*  21: 64 */     Cell cell = null;
/*  22: 65 */     boolean found = false;
/*  23:    */     
/*  24: 67 */     int numCols = lastCol - firstCol;
/*  25: 68 */     int numRows = lastRow - firstRow;
/*  26:    */     
/*  27: 70 */     int row1 = reverse ? lastRow : firstRow;
/*  28: 71 */     int row2 = reverse ? firstRow : lastRow;
/*  29: 72 */     int col1 = reverse ? lastCol : firstCol;
/*  30: 73 */     int col2 = reverse ? firstCol : lastCol;
/*  31: 74 */     int inc = reverse ? -1 : 1;
/*  32: 76 */     for (int i = 0; (i <= numCols) && (!found); i++) {
/*  33: 78 */       for (int j = 0; (j <= numRows) && (!found); j++)
/*  34:    */       {
/*  35: 80 */         int curCol = col1 + i * inc;
/*  36: 81 */         int curRow = row1 + j * inc;
/*  37: 82 */         if ((curCol < this.sheet.getColumns()) && (curRow < this.sheet.getRows()))
/*  38:    */         {
/*  39: 84 */           Cell c = this.sheet.getCell(curCol, curRow);
/*  40: 85 */           if (c.getType() != CellType.EMPTY) {
/*  41: 87 */             if (c.getContents().equals(contents))
/*  42:    */             {
/*  43: 89 */               cell = c;
/*  44: 90 */               found = true;
/*  45:    */             }
/*  46:    */           }
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50: 97 */     return cell;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public Cell findCell(String contents)
/*  54:    */   {
/*  55:108 */     Cell cell = null;
/*  56:109 */     boolean found = false;
/*  57:111 */     for (int i = 0; (i < this.sheet.getRows()) && (!found); i++)
/*  58:    */     {
/*  59:113 */       Cell[] row = this.sheet.getRow(i);
/*  60:114 */       for (int j = 0; (j < row.length) && (!found); j++) {
/*  61:116 */         if (row[j].getContents().equals(contents))
/*  62:    */         {
/*  63:118 */           cell = row[j];
/*  64:119 */           found = true;
/*  65:    */         }
/*  66:    */       }
/*  67:    */     }
/*  68:124 */     return cell;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public Cell findCell(Pattern pattern, int firstCol, int firstRow, int lastCol, int lastRow, boolean reverse)
/*  72:    */   {
/*  73:148 */     Cell cell = null;
/*  74:149 */     boolean found = false;
/*  75:    */     
/*  76:151 */     int numCols = lastCol - firstCol;
/*  77:152 */     int numRows = lastRow - firstRow;
/*  78:    */     
/*  79:154 */     int row1 = reverse ? lastRow : firstRow;
/*  80:155 */     int row2 = reverse ? firstRow : lastRow;
/*  81:156 */     int col1 = reverse ? lastCol : firstCol;
/*  82:157 */     int col2 = reverse ? firstCol : lastCol;
/*  83:158 */     int inc = reverse ? -1 : 1;
/*  84:160 */     for (int i = 0; (i <= numCols) && (!found); i++) {
/*  85:162 */       for (int j = 0; (j <= numRows) && (!found); j++)
/*  86:    */       {
/*  87:164 */         int curCol = col1 + i * inc;
/*  88:165 */         int curRow = row1 + j * inc;
/*  89:166 */         if ((curCol < this.sheet.getColumns()) && (curRow < this.sheet.getRows()))
/*  90:    */         {
/*  91:168 */           Cell c = this.sheet.getCell(curCol, curRow);
/*  92:169 */           if (c.getType() != CellType.EMPTY)
/*  93:    */           {
/*  94:171 */             Matcher m = pattern.matcher(c.getContents());
/*  95:172 */             if (m.matches())
/*  96:    */             {
/*  97:174 */               cell = c;
/*  98:175 */               found = true;
/*  99:    */             }
/* 100:    */           }
/* 101:    */         }
/* 102:    */       }
/* 103:    */     }
/* 104:182 */     return cell;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public LabelCell findLabelCell(String contents)
/* 108:    */   {
/* 109:199 */     LabelCell cell = null;
/* 110:200 */     boolean found = false;
/* 111:202 */     for (int i = 0; (i < this.sheet.getRows()) && (!found); i++)
/* 112:    */     {
/* 113:204 */       Cell[] row = this.sheet.getRow(i);
/* 114:205 */       for (int j = 0; (j < row.length) && (!found); j++) {
/* 115:207 */         if (((row[j].getType() == CellType.LABEL) || 
/* 116:208 */           (row[j].getType() == CellType.STRING_FORMULA)) && 
/* 117:209 */           (row[j].getContents().equals(contents)))
/* 118:    */         {
/* 119:211 */           cell = (LabelCell)row[j];
/* 120:212 */           found = true;
/* 121:    */         }
/* 122:    */       }
/* 123:    */     }
/* 124:217 */     return cell;
/* 125:    */   }
/* 126:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.CellFinder
 * JD-Core Version:    0.7.0.1
 */