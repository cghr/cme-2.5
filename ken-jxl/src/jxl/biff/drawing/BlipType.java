/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ final class BlipType
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private String desc;
/*   7: 40 */   private static BlipType[] types = new BlipType[0];
/*   8:    */   
/*   9:    */   private BlipType(int val, String d)
/*  10:    */   {
/*  11: 50 */     this.value = val;
/*  12: 51 */     this.desc = d;
/*  13:    */     
/*  14: 53 */     BlipType[] newtypes = new BlipType[types.length + 1];
/*  15: 54 */     System.arraycopy(types, 0, newtypes, 0, types.length);
/*  16: 55 */     newtypes[types.length] = this;
/*  17: 56 */     types = newtypes;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String getDescription()
/*  21:    */   {
/*  22: 66 */     return this.desc;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public int getValue()
/*  26:    */   {
/*  27: 76 */     return this.value;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static BlipType getType(int val)
/*  31:    */   {
/*  32: 87 */     BlipType type = UNKNOWN;
/*  33: 88 */     for (int i = 0; i < types.length; i++) {
/*  34: 90 */       if (types[i].value == val)
/*  35:    */       {
/*  36: 92 */         type = types[i];
/*  37: 93 */         break;
/*  38:    */       }
/*  39:    */     }
/*  40: 97 */     return type;
/*  41:    */   }
/*  42:    */   
/*  43:100 */   public static final BlipType ERROR = new BlipType(0, "Error");
/*  44:102 */   public static final BlipType UNKNOWN = new BlipType(1, "Unknown");
/*  45:104 */   public static final BlipType EMF = new BlipType(2, "EMF");
/*  46:106 */   public static final BlipType WMF = new BlipType(3, "WMF");
/*  47:108 */   public static final BlipType PICT = new BlipType(4, "PICT");
/*  48:110 */   public static final BlipType JPEG = new BlipType(5, "JPEG");
/*  49:111 */   public static final BlipType PNG = new BlipType(6, "PNG");
/*  50:112 */   public static final BlipType DIB = new BlipType(7, "DIB");
/*  51:113 */   public static final BlipType FIRST_CLIENT = new BlipType(32, "FIRST");
/*  52:115 */   public static final BlipType LAST_CLIENT = new BlipType(255, "LAST");
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.BlipType
 * JD-Core Version:    0.7.0.1
 */