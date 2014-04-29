/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ 
/*   7:    */ class ExtendedSSTRecord
/*   8:    */   extends WritableRecordData
/*   9:    */ {
/*  10:    */   private static final int infoRecordSize = 8;
/*  11:    */   private int numberOfStrings;
/*  12:    */   private int[] absoluteStreamPositions;
/*  13:    */   private int[] relativeStreamPositions;
/*  14: 39 */   private int currentStringIndex = 0;
/*  15:    */   
/*  16:    */   public ExtendedSSTRecord(int newNumberOfStrings)
/*  17:    */   {
/*  18: 50 */     super(Type.EXTSST);
/*  19: 51 */     this.numberOfStrings = newNumberOfStrings;
/*  20: 52 */     int numberOfBuckets = getNumberOfBuckets();
/*  21: 53 */     this.absoluteStreamPositions = new int[numberOfBuckets];
/*  22: 54 */     this.relativeStreamPositions = new int[numberOfBuckets];
/*  23: 55 */     this.currentStringIndex = 0;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int getNumberOfBuckets()
/*  27:    */   {
/*  28: 60 */     int numberOfStringsPerBucket = getNumberOfStringsPerBucket();
/*  29: 61 */     return numberOfStringsPerBucket != 0 ? 
/*  30: 62 */       (this.numberOfStrings + numberOfStringsPerBucket - 1) / 
/*  31: 63 */       numberOfStringsPerBucket : 0;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public int getNumberOfStringsPerBucket()
/*  35:    */   {
/*  36: 73 */     int bucketLimit = 128;
/*  37: 74 */     return (this.numberOfStrings + 128 - 1) / 128;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void addString(int absoluteStreamPosition, int relativeStreamPosition)
/*  41:    */   {
/*  42: 80 */     this.absoluteStreamPositions[this.currentStringIndex] = 
/*  43: 81 */       (absoluteStreamPosition + relativeStreamPosition);
/*  44: 82 */     this.relativeStreamPositions[this.currentStringIndex] = relativeStreamPosition;
/*  45: 83 */     this.currentStringIndex += 1;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public byte[] getData()
/*  49:    */   {
/*  50: 93 */     int numberOfBuckets = getNumberOfBuckets();
/*  51: 94 */     byte[] data = new byte[2 + 8 * numberOfBuckets];
/*  52:    */     
/*  53: 96 */     IntegerHelper.getTwoBytes(getNumberOfStringsPerBucket(), data, 0);
/*  54: 98 */     for (int i = 0; i < numberOfBuckets; i++)
/*  55:    */     {
/*  56:101 */       IntegerHelper.getFourBytes(this.absoluteStreamPositions[i], 
/*  57:102 */         data, 
/*  58:103 */         2 + i * 8);
/*  59:    */       
/*  60:105 */       IntegerHelper.getTwoBytes(this.relativeStreamPositions[i], 
/*  61:106 */         data, 
/*  62:107 */         6 + i * 8);
/*  63:    */     }
/*  64:112 */     return data;
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ExtendedSSTRecord
 * JD-Core Version:    0.7.0.1
 */