/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ 
/*   5:    */ public class DValParser
/*   6:    */ {
/*   7: 35 */   private static Logger logger = Logger.getLogger(DValParser.class);
/*   8: 38 */   private static int PROMPT_BOX_VISIBLE_MASK = 1;
/*   9: 39 */   private static int PROMPT_BOX_AT_CELL_MASK = 2;
/*  10: 40 */   private static int VALIDITY_DATA_CACHED_MASK = 4;
/*  11:    */   private boolean promptBoxVisible;
/*  12:    */   private boolean promptBoxAtCell;
/*  13:    */   private boolean validityDataCached;
/*  14:    */   private int numDVRecords;
/*  15:    */   private int objectId;
/*  16:    */   
/*  17:    */   public DValParser(byte[] data)
/*  18:    */   {
/*  19: 72 */     int options = IntegerHelper.getInt(data[0], data[1]);
/*  20:    */     
/*  21: 74 */     this.promptBoxVisible = ((options & PROMPT_BOX_VISIBLE_MASK) != 0);
/*  22: 75 */     this.promptBoxAtCell = ((options & PROMPT_BOX_AT_CELL_MASK) != 0);
/*  23: 76 */     this.validityDataCached = ((options & VALIDITY_DATA_CACHED_MASK) != 0);
/*  24:    */     
/*  25: 78 */     this.objectId = IntegerHelper.getInt(data[10], data[11], data[12], data[13]);
/*  26: 79 */     this.numDVRecords = IntegerHelper.getInt(data[14], data[15], 
/*  27: 80 */       data[16], data[17]);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DValParser(int objid, int num)
/*  31:    */   {
/*  32: 88 */     this.objectId = objid;
/*  33: 89 */     this.numDVRecords = num;
/*  34: 90 */     this.validityDataCached = true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public byte[] getData()
/*  38:    */   {
/*  39: 98 */     byte[] data = new byte[18];
/*  40:    */     
/*  41:100 */     int options = 0;
/*  42:102 */     if (this.promptBoxVisible) {
/*  43:104 */       options |= PROMPT_BOX_VISIBLE_MASK;
/*  44:    */     }
/*  45:107 */     if (this.promptBoxAtCell) {
/*  46:109 */       options |= PROMPT_BOX_AT_CELL_MASK;
/*  47:    */     }
/*  48:112 */     if (this.validityDataCached) {
/*  49:114 */       options |= VALIDITY_DATA_CACHED_MASK;
/*  50:    */     }
/*  51:117 */     IntegerHelper.getTwoBytes(options, data, 0);
/*  52:    */     
/*  53:119 */     IntegerHelper.getFourBytes(this.objectId, data, 10);
/*  54:    */     
/*  55:121 */     IntegerHelper.getFourBytes(this.numDVRecords, data, 14);
/*  56:    */     
/*  57:123 */     return data;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void dvRemoved()
/*  61:    */   {
/*  62:132 */     this.numDVRecords -= 1;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public int getNumberOfDVRecords()
/*  66:    */   {
/*  67:142 */     return this.numDVRecords;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getObjectId()
/*  71:    */   {
/*  72:152 */     return this.objectId;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void dvAdded()
/*  76:    */   {
/*  77:160 */     this.numDVRecords += 1;
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.DValParser
 * JD-Core Version:    0.7.0.1
 */