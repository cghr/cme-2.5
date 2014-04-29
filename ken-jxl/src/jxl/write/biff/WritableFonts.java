/*  1:   */ package jxl.write.biff;
/*  2:   */ 
/*  3:   */ import jxl.biff.Fonts;
/*  4:   */ import jxl.write.WritableFont;
/*  5:   */ 
/*  6:   */ public class WritableFonts
/*  7:   */   extends Fonts
/*  8:   */ {
/*  9:   */   public WritableFonts(WritableWorkbookImpl w)
/* 10:   */   {
/* 11:39 */     addFont(w.getStyles().getArial10Pt());
/* 12:   */     
/* 13:   */ 
/* 14:42 */     WritableFont f = new WritableFont(WritableFont.ARIAL);
/* 15:43 */     addFont(f);
/* 16:   */     
/* 17:45 */     f = new WritableFont(WritableFont.ARIAL);
/* 18:46 */     addFont(f);
/* 19:   */     
/* 20:48 */     f = new WritableFont(WritableFont.ARIAL);
/* 21:49 */     addFont(f);
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.WritableFonts
 * JD-Core Version:    0.7.0.1
 */