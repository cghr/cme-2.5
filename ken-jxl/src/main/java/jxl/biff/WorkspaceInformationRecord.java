/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ import jxl.read.biff.Record;
/*   5:    */ 
/*   6:    */ public class WorkspaceInformationRecord
/*   7:    */   extends WritableRecordData
/*   8:    */ {
/*   9: 32 */   private static Logger logger = Logger.getLogger(WorkspaceInformationRecord.class);
/*  10:    */   private int wsoptions;
/*  11:    */   private boolean rowOutlines;
/*  12:    */   private boolean columnOutlines;
/*  13:    */   private boolean fitToPages;
/*  14:    */   private static final int FIT_TO_PAGES = 256;
/*  15:    */   private static final int SHOW_ROW_OUTLINE_SYMBOLS = 1024;
/*  16:    */   private static final int SHOW_COLUMN_OUTLINE_SYMBOLS = 2048;
/*  17:    */   private static final int DEFAULT_OPTIONS = 1217;
/*  18:    */   
/*  19:    */   public WorkspaceInformationRecord(Record t)
/*  20:    */   {
/*  21: 68 */     super(t);
/*  22: 69 */     byte[] data = getRecord().getData();
/*  23:    */     
/*  24: 71 */     this.wsoptions = IntegerHelper.getInt(data[0], data[1]);
/*  25: 72 */     this.fitToPages = ((this.wsoptions | 0x100) != 0);
/*  26: 73 */     this.rowOutlines = ((this.wsoptions | 0x400) != 0);
/*  27: 74 */     this.columnOutlines = ((this.wsoptions | 0x800) != 0);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public WorkspaceInformationRecord()
/*  31:    */   {
/*  32: 82 */     super(Type.WSBOOL);
/*  33: 83 */     this.wsoptions = 1217;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean getFitToPages()
/*  37:    */   {
/*  38: 93 */     return this.fitToPages;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setFitToPages(boolean b)
/*  42:    */   {
/*  43:103 */     this.fitToPages = b;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setRowOutlines(boolean ro)
/*  47:    */   {
/*  48:111 */     this.rowOutlines = true;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setColumnOutlines(boolean ro)
/*  52:    */   {
/*  53:119 */     this.rowOutlines = true;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public byte[] getData()
/*  57:    */   {
/*  58:129 */     byte[] data = new byte[2];
/*  59:131 */     if (this.fitToPages) {
/*  60:133 */       this.wsoptions |= 0x100;
/*  61:    */     }
/*  62:136 */     if (this.rowOutlines) {
/*  63:138 */       this.wsoptions |= 0x400;
/*  64:    */     }
/*  65:141 */     if (this.columnOutlines) {
/*  66:143 */       this.wsoptions |= 0x800;
/*  67:    */     }
/*  68:146 */     IntegerHelper.getTwoBytes(this.wsoptions, data, 0);
/*  69:    */     
/*  70:148 */     return data;
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.WorkspaceInformationRecord
 * JD-Core Version:    0.7.0.1
 */