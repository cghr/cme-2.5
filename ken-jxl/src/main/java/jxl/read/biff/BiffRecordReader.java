/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ public class BiffRecordReader
/*  4:   */ {
/*  5:   */   private File file;
/*  6:   */   private Record record;
/*  7:   */   
/*  8:   */   public BiffRecordReader(File f)
/*  9:   */   {
/* 10:46 */     this.file = f;
/* 11:   */   }
/* 12:   */   
/* 13:   */   public boolean hasNext()
/* 14:   */   {
/* 15:56 */     return this.file.hasNext();
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Record next()
/* 19:   */   {
/* 20:66 */     this.record = this.file.next();
/* 21:67 */     return this.record;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getPos()
/* 25:   */   {
/* 26:77 */     return this.file.getPos() - this.record.getLength() - 4;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BiffRecordReader
 * JD-Core Version:    0.7.0.1
 */