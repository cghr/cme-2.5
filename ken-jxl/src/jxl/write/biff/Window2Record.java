/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.SheetSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ import jxl.biff.WritableRecordData;
/*   7:    */ 
/*   8:    */ class Window2Record
/*   9:    */   extends WritableRecordData
/*  10:    */ {
/*  11:    */   private byte[] data;
/*  12:    */   
/*  13:    */   public Window2Record(SheetSettings settings)
/*  14:    */   {
/*  15: 42 */     super(Type.WINDOW2);
/*  16:    */     
/*  17: 44 */     int options = 0;
/*  18:    */     
/*  19: 46 */     options |= 0x0;
/*  20: 48 */     if (settings.getShowGridLines()) {
/*  21: 50 */       options |= 0x2;
/*  22:    */     }
/*  23: 53 */     options |= 0x4;
/*  24:    */     
/*  25: 55 */     options |= 0x0;
/*  26: 57 */     if (settings.getDisplayZeroValues()) {
/*  27: 59 */       options |= 0x10;
/*  28:    */     }
/*  29: 62 */     options |= 0x20;
/*  30:    */     
/*  31: 64 */     options |= 0x80;
/*  32: 67 */     if ((settings.getHorizontalFreeze() != 0) || 
/*  33: 68 */       (settings.getVerticalFreeze() != 0))
/*  34:    */     {
/*  35: 70 */       options |= 0x8;
/*  36: 71 */       options |= 0x100;
/*  37:    */     }
/*  38: 75 */     if (settings.isSelected()) {
/*  39: 77 */       options |= 0x600;
/*  40:    */     }
/*  41: 81 */     if (settings.getPageBreakPreviewMode()) {
/*  42: 83 */       options |= 0x800;
/*  43:    */     }
/*  44: 87 */     this.data = new byte[18];
/*  45: 88 */     IntegerHelper.getTwoBytes(options, this.data, 0);
/*  46: 89 */     IntegerHelper.getTwoBytes(64, this.data, 6);
/*  47: 90 */     IntegerHelper.getTwoBytes(settings.getPageBreakPreviewMagnification(), 
/*  48: 91 */       this.data, 10);
/*  49: 92 */     IntegerHelper.getTwoBytes(settings.getNormalMagnification(), 
/*  50: 93 */       this.data, 12);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public byte[] getData()
/*  54:    */   {
/*  55:104 */     return this.data;
/*  56:    */   }
/*  57:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.Window2Record
 * JD-Core Version:    0.7.0.1
 */