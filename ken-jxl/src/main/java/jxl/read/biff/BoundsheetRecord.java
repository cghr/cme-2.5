/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ 
/*   8:    */ class BoundsheetRecord
/*   9:    */   extends RecordData
/*  10:    */ {
/*  11:    */   private int offset;
/*  12:    */   private byte typeFlag;
/*  13:    */   private byte visibilityFlag;
/*  14:    */   private int length;
/*  15:    */   private String name;
/*  16: 57 */   public static Biff7 biff7 = new Biff7(null);
/*  17:    */   
/*  18:    */   public BoundsheetRecord(Record t, WorkbookSettings s)
/*  19:    */   {
/*  20: 67 */     super(t);
/*  21: 68 */     byte[] data = getRecord().getData();
/*  22: 69 */     this.offset = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
/*  23: 70 */     this.typeFlag = data[5];
/*  24: 71 */     this.visibilityFlag = data[4];
/*  25: 72 */     this.length = data[6];
/*  26: 74 */     if (data[7] == 0)
/*  27:    */     {
/*  28: 77 */       byte[] bytes = new byte[this.length];
/*  29: 78 */       System.arraycopy(data, 8, bytes, 0, this.length);
/*  30: 79 */       this.name = StringHelper.getString(bytes, this.length, 0, s);
/*  31:    */     }
/*  32:    */     else
/*  33:    */     {
/*  34: 84 */       byte[] bytes = new byte[this.length * 2];
/*  35: 85 */       System.arraycopy(data, 8, bytes, 0, this.length * 2);
/*  36: 86 */       this.name = StringHelper.getUnicodeString(bytes, this.length, 0);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public BoundsheetRecord(Record t, Biff7 biff7)
/*  41:    */   {
/*  42:100 */     super(t);
/*  43:101 */     byte[] data = getRecord().getData();
/*  44:102 */     this.offset = IntegerHelper.getInt(data[0], data[1], data[2], data[3]);
/*  45:103 */     this.typeFlag = data[5];
/*  46:104 */     this.visibilityFlag = data[4];
/*  47:105 */     this.length = data[6];
/*  48:106 */     byte[] bytes = new byte[this.length];
/*  49:107 */     System.arraycopy(data, 7, bytes, 0, this.length);
/*  50:108 */     this.name = new String(bytes);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getName()
/*  54:    */   {
/*  55:118 */     return this.name;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public boolean isHidden()
/*  59:    */   {
/*  60:128 */     return this.visibilityFlag != 0;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public boolean isSheet()
/*  64:    */   {
/*  65:139 */     return this.typeFlag == 0;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean isChart()
/*  69:    */   {
/*  70:149 */     return this.typeFlag == 2;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private static class Biff7 {}
/*  74:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BoundsheetRecord
 * JD-Core Version:    0.7.0.1
 */