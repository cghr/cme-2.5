/*  1:   */ package jxl.read.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.RecordData;
/*  4:   */ import jxl.common.Logger;
/*  5:   */ 
/*  6:   */ class TemplateRecord
/*  7:   */   extends RecordData
/*  8:   */ {
/*  9:33 */   private static Logger logger = Logger.getLogger(TemplateRecord.class);
/* 10:   */   private boolean template;
/* 11:   */   
/* 12:   */   public TemplateRecord(Record t)
/* 13:   */   {
/* 14:47 */     super(t);
/* 15:48 */     this.template = true;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean getTemplate()
/* 19:   */   {
/* 20:58 */     return this.template;
/* 21:   */   }
/* 22:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.TemplateRecord
 * JD-Core Version:    0.7.0.1
 */