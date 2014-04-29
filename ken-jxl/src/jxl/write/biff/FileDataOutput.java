/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ import java.io.RandomAccessFile;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ class FileDataOutput
/*  10:    */   implements ExcelDataOutput
/*  11:    */ {
/*  12: 36 */   private static Logger logger = Logger.getLogger(FileDataOutput.class);
/*  13:    */   private File temporaryFile;
/*  14:    */   private RandomAccessFile data;
/*  15:    */   
/*  16:    */   public FileDataOutput(File tmpdir)
/*  17:    */     throws IOException
/*  18:    */   {
/*  19: 56 */     this.temporaryFile = File.createTempFile("jxl", ".tmp", tmpdir);
/*  20: 57 */     this.temporaryFile.deleteOnExit();
/*  21: 58 */     this.data = new RandomAccessFile(this.temporaryFile, "rw");
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void write(byte[] bytes)
/*  25:    */     throws IOException
/*  26:    */   {
/*  27: 69 */     this.data.write(bytes);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public int getPosition()
/*  31:    */     throws IOException
/*  32:    */   {
/*  33: 81 */     return (int)this.data.getFilePointer();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setData(byte[] newdata, int pos)
/*  37:    */     throws IOException
/*  38:    */   {
/*  39: 92 */     long curpos = this.data.getFilePointer();
/*  40: 93 */     this.data.seek(pos);
/*  41: 94 */     this.data.write(newdata);
/*  42: 95 */     this.data.seek(curpos);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void writeData(OutputStream out)
/*  46:    */     throws IOException
/*  47:    */   {
/*  48:103 */     byte[] buffer = new byte[1024];
/*  49:104 */     int length = 0;
/*  50:105 */     this.data.seek(0L);
/*  51:106 */     while ((length = this.data.read(buffer)) != -1) {
/*  52:108 */       out.write(buffer, 0, length);
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void close()
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:117 */     this.data.close();
/*  60:    */     
/*  61:    */ 
/*  62:    */ 
/*  63:121 */     this.temporaryFile.delete();
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.FileDataOutput
 * JD-Core Version:    0.7.0.1
 */