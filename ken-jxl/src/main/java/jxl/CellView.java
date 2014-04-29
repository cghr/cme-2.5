/*   1:    */ package jxl;
/*   2:    */ 
/*   3:    */ import jxl.format.CellFormat;
/*   4:    */ 
/*   5:    */ public final class CellView
/*   6:    */ {
/*   7:    */   private int dimension;
/*   8:    */   private int size;
/*   9:    */   private boolean depUsed;
/*  10:    */   private boolean hidden;
/*  11:    */   private CellFormat format;
/*  12:    */   private boolean autosize;
/*  13:    */   
/*  14:    */   public CellView()
/*  15:    */   {
/*  16: 70 */     this.hidden = false;
/*  17: 71 */     this.depUsed = false;
/*  18: 72 */     this.dimension = 1;
/*  19: 73 */     this.size = 1;
/*  20: 74 */     this.autosize = false;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public CellView(CellView cv)
/*  24:    */   {
/*  25: 82 */     this.hidden = cv.hidden;
/*  26: 83 */     this.depUsed = cv.depUsed;
/*  27: 84 */     this.dimension = cv.dimension;
/*  28: 85 */     this.size = cv.size;
/*  29: 86 */     this.autosize = cv.autosize;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setHidden(boolean h)
/*  33:    */   {
/*  34: 96 */     this.hidden = h;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean isHidden()
/*  38:    */   {
/*  39:106 */     return this.hidden;
/*  40:    */   }
/*  41:    */   
/*  42:    */   /**
/*  43:    */    * @deprecated
/*  44:    */    */
/*  45:    */   public void setDimension(int d)
/*  46:    */   {
/*  47:118 */     this.dimension = d;
/*  48:119 */     this.depUsed = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setSize(int d)
/*  52:    */   {
/*  53:130 */     this.size = d;
/*  54:131 */     this.depUsed = false;
/*  55:    */   }
/*  56:    */   
/*  57:    */   /**
/*  58:    */    * @deprecated
/*  59:    */    */
/*  60:    */   public int getDimension()
/*  61:    */   {
/*  62:143 */     return this.dimension;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getSize()
/*  66:    */   {
/*  67:154 */     return this.size;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setFormat(CellFormat cf)
/*  71:    */   {
/*  72:164 */     this.format = cf;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public CellFormat getFormat()
/*  76:    */   {
/*  77:175 */     return this.format;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public boolean depUsed()
/*  81:    */   {
/*  82:186 */     return this.depUsed;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setAutosize(boolean a)
/*  86:    */   {
/*  87:196 */     this.autosize = a;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isAutosize()
/*  91:    */   {
/*  92:208 */     return this.autosize;
/*  93:    */   }
/*  94:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.CellView
 * JD-Core Version:    0.7.0.1
 */