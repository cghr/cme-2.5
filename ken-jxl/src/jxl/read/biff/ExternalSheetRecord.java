/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ public class ExternalSheetRecord
/*   9:    */   extends RecordData
/*  10:    */ {
/*  11: 37 */   private static Logger logger = Logger.getLogger(ExternalSheetRecord.class);
/*  12: 43 */   public static Biff7 biff7 = new Biff7(null);
/*  13:    */   private XTI[] xtiArray;
/*  14:    */   
/*  15:    */   private static class Biff7 {}
/*  16:    */   
/*  17:    */   private static class XTI
/*  18:    */   {
/*  19:    */     int supbookIndex;
/*  20:    */     int firstTab;
/*  21:    */     int lastTab;
/*  22:    */     
/*  23:    */     XTI(int s, int f, int l)
/*  24:    */     {
/*  25: 72 */       this.supbookIndex = s;
/*  26: 73 */       this.firstTab = f;
/*  27: 74 */       this.lastTab = l;
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   ExternalSheetRecord(Record t, WorkbookSettings ws)
/*  32:    */   {
/*  33: 91 */     super(t);
/*  34: 92 */     byte[] data = getRecord().getData();
/*  35:    */     
/*  36: 94 */     int numxtis = IntegerHelper.getInt(data[0], data[1]);
/*  37: 96 */     if (data.length < numxtis * 6 + 2)
/*  38:    */     {
/*  39: 98 */       this.xtiArray = new XTI[0];
/*  40: 99 */       logger.warn("Could not process external sheets.  Formulas may be compromised.");
/*  41:    */       
/*  42:101 */       return;
/*  43:    */     }
/*  44:104 */     this.xtiArray = new XTI[numxtis];
/*  45:    */     
/*  46:106 */     int pos = 2;
/*  47:107 */     for (int i = 0; i < numxtis; i++)
/*  48:    */     {
/*  49:109 */       int s = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  50:110 */       int f = IntegerHelper.getInt(data[(pos + 2)], data[(pos + 3)]);
/*  51:111 */       int l = IntegerHelper.getInt(data[(pos + 4)], data[(pos + 5)]);
/*  52:112 */       this.xtiArray[i] = new XTI(s, f, l);
/*  53:113 */       pos += 6;
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   ExternalSheetRecord(Record t, WorkbookSettings settings, Biff7 dummy)
/*  58:    */   {
/*  59:127 */     super(t);
/*  60:    */     
/*  61:129 */     logger.warn("External sheet record for Biff 7 not supported");
/*  62:    */   }
/*  63:    */   
/*  64:    */   public int getNumRecords()
/*  65:    */   {
/*  66:138 */     return this.xtiArray != null ? this.xtiArray.length : 0;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public int getSupbookIndex(int index)
/*  70:    */   {
/*  71:148 */     return this.xtiArray[index].supbookIndex;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int getFirstTabIndex(int index)
/*  75:    */   {
/*  76:159 */     return this.xtiArray[index].firstTab;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getLastTabIndex(int index)
/*  80:    */   {
/*  81:170 */     return this.xtiArray[index].lastTab;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public byte[] getData()
/*  85:    */   {
/*  86:180 */     return getRecord().getData();
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.ExternalSheetRecord
 * JD-Core Version:    0.7.0.1
 */