/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ class Window2Record
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 35 */   private static Logger logger = Logger.getLogger(Window2Record.class);
/*  11:    */   private boolean selected;
/*  12:    */   private boolean showGridLines;
/*  13:    */   private boolean displayZeroValues;
/*  14:    */   private boolean frozenPanes;
/*  15:    */   private boolean frozenNotSplit;
/*  16:    */   private boolean pageBreakPreviewMode;
/*  17:    */   private int pageBreakPreviewMagnification;
/*  18:    */   private int normalMagnification;
/*  19: 74 */   public static final Biff7 biff7 = new Biff7(null);
/*  20:    */   
/*  21:    */   public Window2Record(Record t)
/*  22:    */   {
/*  23: 83 */     super(t);
/*  24: 84 */     byte[] data = t.getData();
/*  25:    */     
/*  26: 86 */     int options = IntegerHelper.getInt(data[0], data[1]);
/*  27:    */     
/*  28: 88 */     this.selected = ((options & 0x200) != 0);
/*  29: 89 */     this.showGridLines = ((options & 0x2) != 0);
/*  30: 90 */     this.frozenPanes = ((options & 0x8) != 0);
/*  31: 91 */     this.displayZeroValues = ((options & 0x10) != 0);
/*  32: 92 */     this.frozenNotSplit = ((options & 0x100) != 0);
/*  33: 93 */     this.pageBreakPreviewMode = ((options & 0x800) != 0);
/*  34:    */     
/*  35: 95 */     this.pageBreakPreviewMagnification = IntegerHelper.getInt(data[10], data[11]);
/*  36: 96 */     this.normalMagnification = IntegerHelper.getInt(data[12], data[13]);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Window2Record(Record t, Biff7 biff7)
/*  40:    */   {
/*  41:108 */     super(t);
/*  42:109 */     byte[] data = t.getData();
/*  43:    */     
/*  44:111 */     int options = IntegerHelper.getInt(data[0], data[1]);
/*  45:    */     
/*  46:113 */     this.selected = ((options & 0x200) != 0);
/*  47:114 */     this.showGridLines = ((options & 0x2) != 0);
/*  48:115 */     this.frozenPanes = ((options & 0x8) != 0);
/*  49:116 */     this.displayZeroValues = ((options & 0x10) != 0);
/*  50:117 */     this.frozenNotSplit = ((options & 0x100) != 0);
/*  51:118 */     this.pageBreakPreviewMode = ((options & 0x800) != 0);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isSelected()
/*  55:    */   {
/*  56:128 */     return this.selected;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean getShowGridLines()
/*  60:    */   {
/*  61:138 */     return this.showGridLines;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean getDisplayZeroValues()
/*  65:    */   {
/*  66:148 */     return this.displayZeroValues;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean getFrozen()
/*  70:    */   {
/*  71:158 */     return this.frozenPanes;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean getFrozenNotSplit()
/*  75:    */   {
/*  76:168 */     return this.frozenNotSplit;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public boolean isPageBreakPreview()
/*  80:    */   {
/*  81:178 */     return this.pageBreakPreviewMode;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public int getPageBreakPreviewMagnificaiton()
/*  85:    */   {
/*  86:188 */     return this.pageBreakPreviewMagnification;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getNormalMagnificaiton()
/*  90:    */   {
/*  91:198 */     return this.normalMagnification;
/*  92:    */   }
/*  93:    */   
/*  94:    */   private static class Biff7 {}
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.Window2Record
 * JD-Core Version:    0.7.0.1
 */