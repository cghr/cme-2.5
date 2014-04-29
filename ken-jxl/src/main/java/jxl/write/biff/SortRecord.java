/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.StringHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class SortRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private String column1Name;
/*  11:    */   private String column2Name;
/*  12:    */   private String column3Name;
/*  13:    */   private boolean sortColumns;
/*  14:    */   private boolean sortKey1Desc;
/*  15:    */   private boolean sortKey2Desc;
/*  16:    */   private boolean sortKey3Desc;
/*  17:    */   private boolean sortCaseSensitive;
/*  18:    */   
/*  19:    */   public SortRecord(String a, String b, String c, boolean sc, boolean sk1d, boolean sk2d, boolean sk3d, boolean scs)
/*  20:    */   {
/*  21: 56 */     super(Type.SORT);
/*  22:    */     
/*  23: 58 */     this.column1Name = a;
/*  24: 59 */     this.column2Name = b;
/*  25: 60 */     this.column3Name = c;
/*  26: 61 */     this.sortColumns = sc;
/*  27: 62 */     this.sortKey1Desc = sk1d;
/*  28: 63 */     this.sortKey2Desc = sk2d;
/*  29: 64 */     this.sortKey3Desc = sk3d;
/*  30: 65 */     this.sortCaseSensitive = scs;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public byte[] getData()
/*  34:    */   {
/*  35: 75 */     int byteCount = 5 + this.column1Name.length() * 2 + 1;
/*  36: 76 */     if (this.column2Name.length() > 0) {
/*  37: 77 */       byteCount += this.column2Name.length() * 2 + 1;
/*  38:    */     }
/*  39: 78 */     if (this.column3Name.length() > 0) {
/*  40: 79 */       byteCount += this.column3Name.length() * 2 + 1;
/*  41:    */     }
/*  42: 80 */     byte[] data = new byte[byteCount + 1];
/*  43:    */     
/*  44: 82 */     int optionFlag = 0;
/*  45: 83 */     if (this.sortColumns) {
/*  46: 84 */       optionFlag |= 0x1;
/*  47:    */     }
/*  48: 85 */     if (this.sortKey1Desc) {
/*  49: 86 */       optionFlag |= 0x2;
/*  50:    */     }
/*  51: 87 */     if (this.sortKey2Desc) {
/*  52: 88 */       optionFlag |= 0x4;
/*  53:    */     }
/*  54: 89 */     if (this.sortKey3Desc) {
/*  55: 90 */       optionFlag |= 0x8;
/*  56:    */     }
/*  57: 91 */     if (this.sortCaseSensitive) {
/*  58: 92 */       optionFlag |= 0x10;
/*  59:    */     }
/*  60: 94 */     data[0] = ((byte)optionFlag);
/*  61:    */     
/*  62: 96 */     data[2] = ((byte)this.column1Name.length());
/*  63: 97 */     data[3] = ((byte)this.column2Name.length());
/*  64: 98 */     data[4] = ((byte)this.column3Name.length());
/*  65:    */     
/*  66:100 */     data[5] = 1;
/*  67:101 */     StringHelper.getUnicodeBytes(this.column1Name, data, 6);
/*  68:102 */     int curPos = 6 + this.column1Name.length() * 2;
/*  69:103 */     if (this.column2Name.length() > 0)
/*  70:    */     {
/*  71:105 */       data[(curPos++)] = 1;
/*  72:106 */       StringHelper.getUnicodeBytes(this.column2Name, data, curPos);
/*  73:107 */       curPos += this.column2Name.length() * 2;
/*  74:    */     }
/*  75:109 */     if (this.column3Name.length() > 0)
/*  76:    */     {
/*  77:111 */       data[(curPos++)] = 1;
/*  78:112 */       StringHelper.getUnicodeBytes(this.column3Name, data, curPos);
/*  79:113 */       curPos += this.column3Name.length() * 2;
/*  80:    */     }
/*  81:116 */     return data;
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SortRecord
 * JD-Core Version:    0.7.0.1
 */