/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.OutputStream;
/*   5:    */ import jxl.WorkbookSettings;
/*   6:    */ import jxl.biff.ByteData;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ public final class File
/*  10:    */ {
/*  11: 41 */   private static Logger logger = Logger.getLogger(File.class);
/*  12:    */   private ExcelDataOutput data;
/*  13:    */   private int pos;
/*  14:    */   private OutputStream outputStream;
/*  15:    */   private int initialFileSize;
/*  16:    */   private int arrayGrowSize;
/*  17:    */   private WorkbookSettings workbookSettings;
/*  18:    */   jxl.read.biff.CompoundFile readCompoundFile;
/*  19:    */   
/*  20:    */   File(OutputStream os, WorkbookSettings ws, jxl.read.biff.CompoundFile rcf)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23: 83 */     this.outputStream = os;
/*  24: 84 */     this.workbookSettings = ws;
/*  25: 85 */     this.readCompoundFile = rcf;
/*  26: 86 */     createDataOutput();
/*  27:    */   }
/*  28:    */   
/*  29:    */   private void createDataOutput()
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 91 */     if (this.workbookSettings.getUseTemporaryFileDuringWrite())
/*  33:    */     {
/*  34: 93 */       this.data = new FileDataOutput(
/*  35: 94 */         this.workbookSettings.getTemporaryFileDuringWriteDirectory());
/*  36:    */     }
/*  37:    */     else
/*  38:    */     {
/*  39: 98 */       this.initialFileSize = this.workbookSettings.getInitialFileSize();
/*  40: 99 */       this.arrayGrowSize = this.workbookSettings.getArrayGrowSize();
/*  41:    */       
/*  42:101 */       this.data = new MemoryDataOutput(this.initialFileSize, this.arrayGrowSize);
/*  43:    */     }
/*  44:    */   }
/*  45:    */   
/*  46:    */   void close(boolean cs)
/*  47:    */     throws IOException, JxlWriteException
/*  48:    */   {
/*  49:116 */     CompoundFile cf = new CompoundFile(this.data, 
/*  50:117 */       this.data.getPosition(), 
/*  51:118 */       this.outputStream, 
/*  52:119 */       this.readCompoundFile);
/*  53:120 */     cf.write();
/*  54:    */     
/*  55:122 */     this.outputStream.flush();
/*  56:123 */     this.data.close();
/*  57:125 */     if (cs) {
/*  58:127 */       this.outputStream.close();
/*  59:    */     }
/*  60:131 */     this.data = null;
/*  61:133 */     if (!this.workbookSettings.getGCDisabled()) {
/*  62:135 */       System.gc();
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void write(ByteData record)
/*  67:    */     throws IOException
/*  68:    */   {
/*  69:147 */     byte[] bytes = record.getBytes();
/*  70:    */     
/*  71:149 */     this.data.write(bytes);
/*  72:    */   }
/*  73:    */   
/*  74:    */   int getPos()
/*  75:    */     throws IOException
/*  76:    */   {
/*  77:159 */     return this.data.getPosition();
/*  78:    */   }
/*  79:    */   
/*  80:    */   void setData(byte[] newdata, int pos)
/*  81:    */     throws IOException
/*  82:    */   {
/*  83:171 */     this.data.setData(newdata, pos);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setOutputFile(OutputStream os)
/*  87:    */     throws IOException
/*  88:    */   {
/*  89:183 */     if (this.data != null) {
/*  90:185 */       logger.warn("Rewriting a workbook with non-empty data");
/*  91:    */     }
/*  92:188 */     this.outputStream = os;
/*  93:189 */     createDataOutput();
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.File
 * JD-Core Version:    0.7.0.1
 */