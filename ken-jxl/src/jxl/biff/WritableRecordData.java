/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ import jxl.read.biff.Record;
/*   5:    */ 
/*   6:    */ public abstract class WritableRecordData
/*   7:    */   extends RecordData
/*   8:    */   implements ByteData
/*   9:    */ {
/*  10: 36 */   private static Logger logger = Logger.getLogger(WritableRecordData.class);
/*  11:    */   protected static final int maxRecordLength = 8228;
/*  12:    */   
/*  13:    */   protected WritableRecordData(Type t)
/*  14:    */   {
/*  15: 49 */     super(t);
/*  16:    */   }
/*  17:    */   
/*  18:    */   protected WritableRecordData(Record t)
/*  19:    */   {
/*  20: 59 */     super(t);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public final byte[] getBytes()
/*  24:    */   {
/*  25: 71 */     byte[] data = getData();
/*  26:    */     
/*  27: 73 */     int dataLength = data.length;
/*  28: 79 */     if (data.length > 8224)
/*  29:    */     {
/*  30: 81 */       dataLength = 8224;
/*  31: 82 */       data = handleContinueRecords(data);
/*  32:    */     }
/*  33: 85 */     byte[] bytes = new byte[data.length + 4];
/*  34:    */     
/*  35: 87 */     System.arraycopy(data, 0, bytes, 4, data.length);
/*  36:    */     
/*  37: 89 */     IntegerHelper.getTwoBytes(getCode(), bytes, 0);
/*  38: 90 */     IntegerHelper.getTwoBytes(dataLength, bytes, 2);
/*  39:    */     
/*  40: 92 */     return bytes;
/*  41:    */   }
/*  42:    */   
/*  43:    */   private byte[] handleContinueRecords(byte[] data)
/*  44:    */   {
/*  45:104 */     int continuedData = data.length - 8224;
/*  46:105 */     int numContinueRecords = continuedData / 8224 + 1;
/*  47:    */     
/*  48:    */ 
/*  49:    */ 
/*  50:109 */     byte[] newdata = new byte[data.length + numContinueRecords * 4];
/*  51:    */     
/*  52:    */ 
/*  53:    */ 
/*  54:113 */     System.arraycopy(data, 0, newdata, 0, 8224);
/*  55:114 */     int oldarraypos = 8224;
/*  56:115 */     int newarraypos = 8224;
/*  57:118 */     for (int i = 0; i < numContinueRecords; i++)
/*  58:    */     {
/*  59:121 */       int length = Math.min(data.length - oldarraypos, 8224);
/*  60:    */       
/*  61:    */ 
/*  62:124 */       IntegerHelper.getTwoBytes(Type.CONTINUE.value, newdata, newarraypos);
/*  63:125 */       IntegerHelper.getTwoBytes(length, newdata, newarraypos + 2);
/*  64:    */       
/*  65:    */ 
/*  66:128 */       System.arraycopy(data, oldarraypos, newdata, newarraypos + 4, length);
/*  67:    */       
/*  68:    */ 
/*  69:131 */       oldarraypos += length;
/*  70:132 */       newarraypos += length + 4;
/*  71:    */     }
/*  72:135 */     return newdata;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected abstract byte[] getData();
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.WritableRecordData
 * JD-Core Version:    0.7.0.1
 */