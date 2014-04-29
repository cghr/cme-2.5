/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class EscherAtom
/*  6:   */   extends EscherRecord
/*  7:   */ {
/*  8:33 */   private static Logger logger = Logger.getLogger(EscherAtom.class);
/*  9:   */   
/* 10:   */   public EscherAtom(EscherRecordData erd)
/* 11:   */   {
/* 12:42 */     super(erd);
/* 13:   */   }
/* 14:   */   
/* 15:   */   protected EscherAtom(EscherRecordType type)
/* 16:   */   {
/* 17:52 */     super(type);
/* 18:   */   }
/* 19:   */   
/* 20:   */   byte[] getData()
/* 21:   */   {
/* 22:62 */     logger.warn("escher atom getData called on object of type " + 
/* 23:63 */       getClass().getName() + " code " + 
/* 24:64 */       Integer.toString(getType().getValue(), 16));
/* 25:65 */     return null;
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherAtom
 * JD-Core Version:    0.7.0.1
 */