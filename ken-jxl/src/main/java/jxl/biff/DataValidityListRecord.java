/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ import jxl.read.biff.Record;
/*   5:    */ 
/*   6:    */ public class DataValidityListRecord
/*   7:    */   extends WritableRecordData
/*   8:    */ {
/*   9: 30 */   private static Logger logger = Logger.getLogger(
/*  10: 31 */     DataValidityListRecord.class);
/*  11:    */   private int numSettings;
/*  12:    */   private int objectId;
/*  13:    */   private DValParser dvalParser;
/*  14:    */   private byte[] data;
/*  15:    */   
/*  16:    */   public DataValidityListRecord(Record t)
/*  17:    */   {
/*  18: 58 */     super(t);
/*  19:    */     
/*  20: 60 */     this.data = getRecord().getData();
/*  21: 61 */     this.objectId = IntegerHelper.getInt(this.data[10], this.data[11], this.data[12], this.data[13]);
/*  22: 62 */     this.numSettings = IntegerHelper.getInt(this.data[14], this.data[15], this.data[16], this.data[17]);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public DataValidityListRecord(DValParser dval)
/*  26:    */   {
/*  27: 70 */     super(Type.DVAL);
/*  28:    */     
/*  29: 72 */     this.dvalParser = dval;
/*  30:    */   }
/*  31:    */   
/*  32:    */   DataValidityListRecord(DataValidityListRecord dvlr)
/*  33:    */   {
/*  34: 82 */     super(Type.DVAL);
/*  35:    */     
/*  36: 84 */     this.data = dvlr.getData();
/*  37:    */   }
/*  38:    */   
/*  39:    */   int getNumberOfSettings()
/*  40:    */   {
/*  41: 92 */     return this.numSettings;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public byte[] getData()
/*  45:    */   {
/*  46:102 */     if (this.dvalParser == null) {
/*  47:104 */       return this.data;
/*  48:    */     }
/*  49:107 */     return this.dvalParser.getData();
/*  50:    */   }
/*  51:    */   
/*  52:    */   void dvRemoved()
/*  53:    */   {
/*  54:116 */     if (this.dvalParser == null) {
/*  55:118 */       this.dvalParser = new DValParser(this.data);
/*  56:    */     }
/*  57:121 */     this.dvalParser.dvRemoved();
/*  58:    */   }
/*  59:    */   
/*  60:    */   void dvAdded()
/*  61:    */   {
/*  62:129 */     if (this.dvalParser == null) {
/*  63:131 */       this.dvalParser = new DValParser(this.data);
/*  64:    */     }
/*  65:134 */     this.dvalParser.dvAdded();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean hasDVRecords()
/*  69:    */   {
/*  70:144 */     if (this.dvalParser == null) {
/*  71:146 */       return true;
/*  72:    */     }
/*  73:149 */     return this.dvalParser.getNumberOfDVRecords() > 0;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getObjectId()
/*  77:    */   {
/*  78:159 */     if (this.dvalParser == null) {
/*  79:161 */       return this.objectId;
/*  80:    */     }
/*  81:164 */     return this.dvalParser.getObjectId();
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DataValidityListRecord
 * JD-Core Version:    0.7.0.1
 */