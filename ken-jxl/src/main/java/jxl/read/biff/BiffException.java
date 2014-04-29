/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.JXLException;
/*  4:   */ 
/*  5:   */ public class BiffException
/*  6:   */   extends JXLException
/*  7:   */ {
/*  8:   */   private static class BiffMessage
/*  9:   */   {
/* 10:   */     public String message;
/* 11:   */     
/* 12:   */     BiffMessage(String m)
/* 13:   */     {
/* 14:45 */       this.message = m;
/* 15:   */     }
/* 16:   */   }
/* 17:   */   
/* 18:52 */   static final BiffMessage unrecognizedBiffVersion = new BiffMessage("Unrecognized biff version");
/* 19:57 */   static final BiffMessage expectedGlobals = new BiffMessage("Expected globals");
/* 20:62 */   static final BiffMessage excelFileTooBig = new BiffMessage("Not all of the excel file could be read");
/* 21:67 */   static final BiffMessage excelFileNotFound = new BiffMessage("The input file was not found");
/* 22:72 */   static final BiffMessage unrecognizedOLEFile = new BiffMessage("Unable to recognize OLE stream");
/* 23:77 */   static final BiffMessage streamNotFound = new BiffMessage("Compound file does not contain the specified stream");
/* 24:82 */   static final BiffMessage passwordProtected = new BiffMessage("The workbook is password protected");
/* 25:87 */   static final BiffMessage corruptFileFormat = new BiffMessage("The file format is corrupt");
/* 26:   */   
/* 27:   */   public BiffException(BiffMessage m)
/* 28:   */   {
/* 29:96 */     super(m.message);
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.BiffException
 * JD-Core Version:    0.7.0.1
 */