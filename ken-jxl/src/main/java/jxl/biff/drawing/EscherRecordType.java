/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ final class EscherRecordType
/*   4:    */ {
/*   5:    */   private int value;
/*   6: 35 */   private static EscherRecordType[] types = new EscherRecordType[0];
/*   7:    */   
/*   8:    */   private EscherRecordType(int val)
/*   9:    */   {
/*  10: 44 */     this.value = val;
/*  11:    */     
/*  12: 46 */     EscherRecordType[] newtypes = new EscherRecordType[types.length + 1];
/*  13: 47 */     System.arraycopy(types, 0, newtypes, 0, types.length);
/*  14: 48 */     newtypes[types.length] = this;
/*  15: 49 */     types = newtypes;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public int getValue()
/*  19:    */   {
/*  20: 59 */     return this.value;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static EscherRecordType getType(int val)
/*  24:    */   {
/*  25: 71 */     EscherRecordType type = UNKNOWN;
/*  26: 73 */     for (int i = 0; i < types.length; i++) {
/*  27: 75 */       if (val == types[i].value)
/*  28:    */       {
/*  29: 77 */         type = types[i];
/*  30: 78 */         break;
/*  31:    */       }
/*  32:    */     }
/*  33: 82 */     return type;
/*  34:    */   }
/*  35:    */   
/*  36: 85 */   public static final EscherRecordType UNKNOWN = new EscherRecordType(0);
/*  37: 87 */   public static final EscherRecordType DGG_CONTAINER = new EscherRecordType(61440);
/*  38: 89 */   public static final EscherRecordType BSTORE_CONTAINER = new EscherRecordType(61441);
/*  39: 91 */   public static final EscherRecordType DG_CONTAINER = new EscherRecordType(61442);
/*  40: 93 */   public static final EscherRecordType SPGR_CONTAINER = new EscherRecordType(61443);
/*  41: 95 */   public static final EscherRecordType SP_CONTAINER = new EscherRecordType(61444);
/*  42: 97 */   public static final EscherRecordType DGG = new EscherRecordType(61446);
/*  43: 98 */   public static final EscherRecordType BSE = new EscherRecordType(61447);
/*  44: 99 */   public static final EscherRecordType DG = new EscherRecordType(61448);
/*  45:100 */   public static final EscherRecordType SPGR = new EscherRecordType(61449);
/*  46:101 */   public static final EscherRecordType SP = new EscherRecordType(61450);
/*  47:102 */   public static final EscherRecordType OPT = new EscherRecordType(61451);
/*  48:104 */   public static final EscherRecordType CLIENT_ANCHOR = new EscherRecordType(61456);
/*  49:106 */   public static final EscherRecordType CLIENT_DATA = new EscherRecordType(61457);
/*  50:108 */   public static final EscherRecordType CLIENT_TEXT_BOX = new EscherRecordType(61453);
/*  51:110 */   public static final EscherRecordType SPLIT_MENU_COLORS = new EscherRecordType(61726);
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherRecordType
 * JD-Core Version:    0.7.0.1
 */