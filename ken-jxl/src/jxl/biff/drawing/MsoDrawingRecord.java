/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.Type;
/*   4:    */ import jxl.biff.WritableRecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ import jxl.read.biff.Record;
/*   7:    */ 
/*   8:    */ public class MsoDrawingRecord
/*   9:    */   extends WritableRecordData
/*  10:    */ {
/*  11: 37 */   private static Logger logger = Logger.getLogger(MsoDrawingRecord.class);
/*  12:    */   private boolean first;
/*  13:    */   private byte[] data;
/*  14:    */   
/*  15:    */   public MsoDrawingRecord(Record t)
/*  16:    */   {
/*  17: 56 */     super(t);
/*  18: 57 */     this.data = getRecord().getData();
/*  19: 58 */     this.first = false;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public MsoDrawingRecord(byte[] d)
/*  23:    */   {
/*  24: 68 */     super(Type.MSODRAWING);
/*  25: 69 */     this.data = d;
/*  26: 70 */     this.first = false;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public byte[] getData()
/*  30:    */   {
/*  31: 80 */     return this.data;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public Record getRecord()
/*  35:    */   {
/*  36: 90 */     return super.getRecord();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setFirst()
/*  40:    */   {
/*  41: 98 */     this.first = true;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean isFirst()
/*  45:    */   {
/*  46:110 */     return this.first;
/*  47:    */   }
/*  48:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.MsoDrawingRecord
 * JD-Core Version:    0.7.0.1
 */