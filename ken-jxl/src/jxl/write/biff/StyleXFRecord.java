/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.DisplayFormat;
/*  4:   */ import jxl.biff.FontRecord;
/*  5:   */ import jxl.biff.XFRecord;
/*  6:   */ 
/*  7:   */ public class StyleXFRecord
/*  8:   */   extends XFRecord
/*  9:   */ {
/* 10:   */   public StyleXFRecord(FontRecord fnt, DisplayFormat form)
/* 11:   */   {
/* 12:39 */     super(fnt, form);
/* 13:   */     
/* 14:41 */     setXFDetails(XFRecord.style, 65520);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public final void setCellOptions(int opt)
/* 18:   */   {
/* 19:53 */     super.setXFCellOptions(opt);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setLocked(boolean l)
/* 23:   */   {
/* 24:64 */     super.setXFLocked(l);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.StyleXFRecord
 * JD-Core Version:    0.7.0.1
 */