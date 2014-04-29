/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class SpgrContainer
/*  6:   */   extends EscherContainer
/*  7:   */ {
/*  8:32 */   private static final Logger logger = Logger.getLogger(SpgrContainer.class);
/*  9:   */   
/* 10:   */   public SpgrContainer()
/* 11:   */   {
/* 12:39 */     super(EscherRecordType.SPGR_CONTAINER);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public SpgrContainer(EscherRecordData erd)
/* 16:   */   {
/* 17:49 */     super(erd);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.SpgrContainer
 * JD-Core Version:    0.7.0.1
 */