/*  1:   */ package jxl.biff;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import jxl.write.biff.File;
/*  5:   */ 
/*  6:   */ public class AutoFilter
/*  7:   */ {
/*  8:   */   private FilterModeRecord filterMode;
/*  9:   */   private AutoFilterInfoRecord autoFilterInfo;
/* 10:   */   private AutoFilterRecord autoFilter;
/* 11:   */   
/* 12:   */   public AutoFilter(FilterModeRecord fmr, AutoFilterInfoRecord afir)
/* 13:   */   {
/* 14:41 */     this.filterMode = fmr;
/* 15:42 */     this.autoFilterInfo = afir;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void add(AutoFilterRecord af)
/* 19:   */   {
/* 20:47 */     this.autoFilter = af;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void write(File outputFile)
/* 24:   */     throws IOException
/* 25:   */   {
/* 26:58 */     if (this.filterMode != null) {
/* 27:60 */       outputFile.write(this.filterMode);
/* 28:   */     }
/* 29:63 */     if (this.autoFilterInfo != null) {
/* 30:65 */       outputFile.write(this.autoFilterInfo);
/* 31:   */     }
/* 32:68 */     if (this.autoFilter != null) {
/* 33:70 */       outputFile.write(this.autoFilter);
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.AutoFilter
 * JD-Core Version:    0.7.0.1
 */