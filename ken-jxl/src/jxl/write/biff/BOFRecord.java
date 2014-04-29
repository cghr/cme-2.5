/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.Type;
/*   4:    */ import jxl.biff.WritableRecordData;
/*   5:    */ 
/*   6:    */ class BOFRecord
/*   7:    */   extends WritableRecordData
/*   8:    */ {
/*   9:    */   private byte[] data;
/*  10: 41 */   public static final WorkbookGlobalsBOF workbookGlobals = new WorkbookGlobalsBOF(null);
/*  11: 42 */   public static final SheetBOF sheet = new SheetBOF(null);
/*  12:    */   
/*  13:    */   public BOFRecord(WorkbookGlobalsBOF dummy)
/*  14:    */   {
/*  15: 51 */     super(Type.BOF);
/*  16:    */     
/*  17:    */ 
/*  18:    */ 
/*  19: 55 */     this.data = 
/*  20: 56 */       new byte[] {
/*  21: 57 */       0, 6, 
/*  22: 58 */       5, 
/*  23:    */       
/*  24: 60 */       0, -14, 
/*  25: 61 */       21, 
/*  26: 62 */       -52, 
/*  27: 63 */       7, 
/*  28:    */       
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32: 68 */       0, 0, 0, 0, 6 };
/*  33:    */   }
/*  34:    */   
/*  35:    */   public BOFRecord(SheetBOF dummy)
/*  36:    */   {
/*  37: 82 */     super(Type.BOF);
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41: 86 */     this.data = 
/*  42: 87 */       new byte[] {
/*  43: 88 */       0, 6, 
/*  44: 89 */       16, 
/*  45:    */       
/*  46: 91 */       0, -14, 
/*  47: 92 */       21, 
/*  48: 93 */       -52, 
/*  49: 94 */       7, 
/*  50:    */       
/*  51:    */ 
/*  52:    */ 
/*  53:    */ 
/*  54: 99 */       0, 0, 0, 0, 6 };
/*  55:    */   }
/*  56:    */   
/*  57:    */   public byte[] getData()
/*  58:    */   {
/*  59:113 */     return this.data;
/*  60:    */   }
/*  61:    */   
/*  62:    */   private static class SheetBOF {}
/*  63:    */   
/*  64:    */   private static class WorkbookGlobalsBOF {}
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.BOFRecord
 * JD-Core Version:    0.7.0.1
 */