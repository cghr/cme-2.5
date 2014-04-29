/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.RecordData;
/*   5:    */ import jxl.common.Logger;
/*   6:    */ 
/*   7:    */ public class BOFRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10: 36 */   private static Logger logger = Logger.getLogger(BOFRecord.class);
/*  11:    */   private static final int Biff8 = 1536;
/*  12:    */   private static final int Biff7 = 1280;
/*  13:    */   private static final int WorkbookGlobals = 5;
/*  14:    */   private static final int Worksheet = 16;
/*  15:    */   private static final int Chart = 32;
/*  16:    */   private static final int MacroSheet = 64;
/*  17:    */   private int version;
/*  18:    */   private int substreamType;
/*  19:    */   
/*  20:    */   BOFRecord(Record t)
/*  21:    */   {
/*  22: 79 */     super(t);
/*  23: 80 */     byte[] data = getRecord().getData();
/*  24: 81 */     this.version = IntegerHelper.getInt(data[0], data[1]);
/*  25: 82 */     this.substreamType = IntegerHelper.getInt(data[2], data[3]);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public boolean isBiff8()
/*  29:    */   {
/*  30: 92 */     return this.version == 1536;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean isBiff7()
/*  34:    */   {
/*  35:102 */     return this.version == 1280;
/*  36:    */   }
/*  37:    */   
/*  38:    */   boolean isWorkbookGlobals()
/*  39:    */   {
/*  40:115 */     return this.substreamType == 5;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isWorksheet()
/*  44:    */   {
/*  45:126 */     return this.substreamType == 16;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean isMacroSheet()
/*  49:    */   {
/*  50:137 */     return this.substreamType == 64;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean isChart()
/*  54:    */   {
/*  55:148 */     return this.substreamType == 32;
/*  56:    */   }
/*  57:    */   
/*  58:    */   int getLength()
/*  59:    */   {
/*  60:158 */     return getRecord().getLength();
/*  61:    */   }
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BOFRecord
 * JD-Core Version:    0.7.0.1
 */