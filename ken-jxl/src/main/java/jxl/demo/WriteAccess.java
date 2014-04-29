/*  1:   */ package jxl.demo;
/*  2:   */ 
/*  3:   */ import java.io.FileInputStream;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import jxl.WorkbookSettings;
/*  7:   */ import jxl.biff.StringHelper;
/*  8:   */ import jxl.biff.Type;
/*  9:   */ import jxl.read.biff.BiffException;
/* 10:   */ import jxl.read.biff.BiffRecordReader;
/* 11:   */ import jxl.read.biff.Record;
/* 12:   */ 
/* 13:   */ class WriteAccess
/* 14:   */ {
/* 15:   */   private BiffRecordReader reader;
/* 16:   */   
/* 17:   */   public WriteAccess(java.io.File file)
/* 18:   */     throws IOException, BiffException
/* 19:   */   {
/* 20:43 */     WorkbookSettings ws = new WorkbookSettings();
/* 21:44 */     FileInputStream fis = new FileInputStream(file);
/* 22:45 */     jxl.read.biff.File f = new jxl.read.biff.File(fis, ws);
/* 23:46 */     this.reader = new BiffRecordReader(f);
/* 24:   */     
/* 25:48 */     display(ws);
/* 26:49 */     fis.close();
/* 27:   */   }
/* 28:   */   
/* 29:   */   private void display(WorkbookSettings ws)
/* 30:   */     throws IOException
/* 31:   */   {
/* 32:57 */     Record r = null;
/* 33:58 */     boolean found = false;
/* 34:59 */     while ((this.reader.hasNext()) && (!found))
/* 35:   */     {
/* 36:61 */       r = this.reader.next();
/* 37:62 */       if (r.getType() == Type.WRITEACCESS) {
/* 38:64 */         found = true;
/* 39:   */       }
/* 40:   */     }
/* 41:68 */     if (!found)
/* 42:   */     {
/* 43:70 */       System.err.println("Warning:  could not find write access record");
/* 44:71 */       return;
/* 45:   */     }
/* 46:74 */     byte[] data = r.getData();
/* 47:   */     
/* 48:76 */     String s = null;
/* 49:   */     
/* 50:78 */     s = StringHelper.getString(data, data.length, 0, ws);
/* 51:   */     
/* 52:80 */     System.out.println(s);
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.demo.WriteAccess
 * JD-Core Version:    0.7.0.1
 */