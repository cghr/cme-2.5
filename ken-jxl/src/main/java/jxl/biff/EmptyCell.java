/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.CellFeatures;
/*   4:    */ import jxl.CellType;
/*   5:    */ import jxl.format.Alignment;
/*   6:    */ import jxl.format.Border;
/*   7:    */ import jxl.format.BorderLineStyle;
/*   8:    */ import jxl.format.VerticalAlignment;
/*   9:    */ import jxl.write.WritableCell;
/*  10:    */ import jxl.write.WritableCellFeatures;
/*  11:    */ 
/*  12:    */ public class EmptyCell
/*  13:    */   implements WritableCell
/*  14:    */ {
/*  15:    */   private int row;
/*  16:    */   private int col;
/*  17:    */   
/*  18:    */   public EmptyCell(int c, int r)
/*  19:    */   {
/*  20: 55 */     this.row = r;
/*  21: 56 */     this.col = c;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public int getRow()
/*  25:    */   {
/*  26: 66 */     return this.row;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public int getColumn()
/*  30:    */   {
/*  31: 76 */     return this.col;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public CellType getType()
/*  35:    */   {
/*  36: 86 */     return CellType.EMPTY;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getContents()
/*  40:    */   {
/*  41: 96 */     return "";
/*  42:    */   }
/*  43:    */   
/*  44:    */   public jxl.format.CellFormat getCellFormat()
/*  45:    */   {
/*  46:106 */     return null;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setHidden(boolean flag) {}
/*  50:    */   
/*  51:    */   public void setLocked(boolean flag) {}
/*  52:    */   
/*  53:    */   public void setAlignment(Alignment align) {}
/*  54:    */   
/*  55:    */   public void setVerticalAlignment(VerticalAlignment valign) {}
/*  56:    */   
/*  57:    */   public void setBorder(Border border, BorderLineStyle line) {}
/*  58:    */   
/*  59:    */   public void setCellFormat(jxl.format.CellFormat cf) {}
/*  60:    */   
/*  61:    */   /**
/*  62:    */    * @deprecated
/*  63:    */    */
/*  64:    */   public void setCellFormat(jxl.CellFormat cf) {}
/*  65:    */   
/*  66:    */   public boolean isHidden()
/*  67:    */   {
/*  68:175 */     return false;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public WritableCell copyTo(int c, int r)
/*  72:    */   {
/*  73:187 */     return new EmptyCell(c, r);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public CellFeatures getCellFeatures()
/*  77:    */   {
/*  78:197 */     return null;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public WritableCellFeatures getWritableCellFeatures()
/*  82:    */   {
/*  83:207 */     return null;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setCellFeatures(WritableCellFeatures wcf) {}
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.EmptyCell
 * JD-Core Version:    0.7.0.1
 */