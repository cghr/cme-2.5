/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class BStoreContainer
/*  6:   */   extends EscherContainer
/*  7:   */ {
/*  8:32 */   private static Logger logger = Logger.getLogger(BStoreContainer.class);
/*  9:   */   private int numBlips;
/* 10:   */   
/* 11:   */   public BStoreContainer(EscherRecordData erd)
/* 12:   */   {
/* 13:47 */     super(erd);
/* 14:48 */     this.numBlips = getInstance();
/* 15:   */   }
/* 16:   */   
/* 17:   */   public BStoreContainer()
/* 18:   */   {
/* 19:56 */     super(EscherRecordType.BSTORE_CONTAINER);
/* 20:   */   }
/* 21:   */   
/* 22:   */   void setNumBlips(int count)
/* 23:   */   {
/* 24:66 */     this.numBlips = count;
/* 25:67 */     setInstance(this.numBlips);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getNumBlips()
/* 29:   */   {
/* 30:77 */     return this.numBlips;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public BlipStoreEntry getDrawing(int i)
/* 34:   */   {
/* 35:88 */     EscherRecord[] children = getChildren();
/* 36:89 */     BlipStoreEntry bse = (BlipStoreEntry)children[i];
/* 37:90 */     return bse;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.BStoreContainer
 * JD-Core Version:    0.7.0.1
 */