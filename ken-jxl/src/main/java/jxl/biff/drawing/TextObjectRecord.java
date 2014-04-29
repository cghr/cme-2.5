/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ import jxl.read.biff.Record;
/*   8:    */ 
/*   9:    */ public class TextObjectRecord
/*  10:    */   extends WritableRecordData
/*  11:    */ {
/*  12: 37 */   private static Logger logger = Logger.getLogger(TextObjectRecord.class);
/*  13:    */   private byte[] data;
/*  14:    */   private int textLength;
/*  15:    */   
/*  16:    */   TextObjectRecord(String t)
/*  17:    */   {
/*  18: 56 */     super(Type.TXO);
/*  19:    */     
/*  20: 58 */     this.textLength = t.length();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public TextObjectRecord(Record t)
/*  24:    */   {
/*  25: 68 */     super(t);
/*  26: 69 */     this.data = getRecord().getData();
/*  27: 70 */     this.textLength = IntegerHelper.getInt(this.data[10], this.data[11]);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public TextObjectRecord(byte[] d)
/*  31:    */   {
/*  32: 80 */     super(Type.TXO);
/*  33: 81 */     this.data = d;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getTextLength()
/*  37:    */   {
/*  38: 92 */     return this.textLength;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public byte[] getData()
/*  42:    */   {
/*  43:102 */     if (this.data != null) {
/*  44:104 */       return this.data;
/*  45:    */     }
/*  46:107 */     this.data = new byte[18];
/*  47:    */     
/*  48:    */ 
/*  49:110 */     int options = 0;
/*  50:111 */     options |= 0x2;
/*  51:112 */     options |= 0x10;
/*  52:113 */     options |= 0x200;
/*  53:114 */     IntegerHelper.getTwoBytes(options, this.data, 0);
/*  54:    */     
/*  55:    */ 
/*  56:    */ 
/*  57:    */ 
/*  58:    */ 
/*  59:120 */     IntegerHelper.getTwoBytes(this.textLength, this.data, 10);
/*  60:    */     
/*  61:    */ 
/*  62:123 */     IntegerHelper.getTwoBytes(16, this.data, 12);
/*  63:    */     
/*  64:125 */     return this.data;
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.TextObjectRecord
 * JD-Core Version:    0.7.0.1
 */