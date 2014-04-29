/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ 
/*   5:    */ abstract class ParseItem
/*   6:    */ {
/*   7: 30 */   private static Logger logger = Logger.getLogger(ParseItem.class);
/*   8:    */   private ParseItem parent;
/*   9:    */   private boolean volatileFunction;
/*  10:    */   /**
/*  11:    */    * @deprecated
/*  12:    */    */
/*  13:    */   private boolean alternateCode;
/*  14:    */   private ParseContext parseContext;
/*  15:    */   private boolean valid;
/*  16:    */   
/*  17:    */   public ParseItem()
/*  18:    */   {
/*  19: 64 */     this.volatileFunction = false;
/*  20: 65 */     this.alternateCode = false;
/*  21: 66 */     this.valid = true;
/*  22: 67 */     this.parseContext = ParseContext.DEFAULT;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected void setParent(ParseItem p)
/*  26:    */   {
/*  27: 75 */     this.parent = p;
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected void setVolatile()
/*  31:    */   {
/*  32: 83 */     this.volatileFunction = true;
/*  33: 84 */     if ((this.parent != null) && (!this.parent.isVolatile())) {
/*  34: 86 */       this.parent.setVolatile();
/*  35:    */     }
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected final void setInvalid()
/*  39:    */   {
/*  40: 95 */     this.valid = false;
/*  41: 96 */     if (this.parent != null) {
/*  42: 98 */       this.parent.setInvalid();
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   final boolean isVolatile()
/*  47:    */   {
/*  48:109 */     return this.volatileFunction;
/*  49:    */   }
/*  50:    */   
/*  51:    */   final boolean isValid()
/*  52:    */   {
/*  53:119 */     return this.valid;
/*  54:    */   }
/*  55:    */   
/*  56:    */   abstract void getString(StringBuffer paramStringBuffer);
/*  57:    */   
/*  58:    */   abstract byte[] getBytes();
/*  59:    */   
/*  60:    */   abstract void adjustRelativeCellReferences(int paramInt1, int paramInt2);
/*  61:    */   
/*  62:    */   abstract void columnInserted(int paramInt1, int paramInt2, boolean paramBoolean);
/*  63:    */   
/*  64:    */   abstract void columnRemoved(int paramInt1, int paramInt2, boolean paramBoolean);
/*  65:    */   
/*  66:    */   abstract void rowInserted(int paramInt1, int paramInt2, boolean paramBoolean);
/*  67:    */   
/*  68:    */   abstract void rowRemoved(int paramInt1, int paramInt2, boolean paramBoolean);
/*  69:    */   
/*  70:    */   abstract void handleImportedCellReferences();
/*  71:    */   
/*  72:    */   /**
/*  73:    */    * @deprecated
/*  74:    */    */
/*  75:    */   protected void setAlternateCode()
/*  76:    */   {
/*  77:205 */     this.alternateCode = true;
/*  78:    */   }
/*  79:    */   
/*  80:    */   /**
/*  81:    */    * @deprecated
/*  82:    */    */
/*  83:    */   protected final boolean useAlternateCode()
/*  84:    */   {
/*  85:216 */     return this.alternateCode;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void setParseContext(ParseContext pc)
/*  89:    */   {
/*  90:226 */     this.parseContext = pc;
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected final ParseContext getParseContext()
/*  94:    */   {
/*  95:236 */     return this.parseContext;
/*  96:    */   }
/*  97:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.ParseItem
 * JD-Core Version:    0.7.0.1
 */