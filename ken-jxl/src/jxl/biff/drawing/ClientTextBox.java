/*  1:   */ package jxl.biff.drawing;
/*  2:   */ 
/*  3:   */ import jxl.common.Logger;
/*  4:   */ 
/*  5:   */ class ClientTextBox
/*  6:   */   extends EscherAtom
/*  7:   */ {
/*  8:32 */   private static Logger logger = Logger.getLogger(ClientTextBox.class);
/*  9:   */   private byte[] data;
/* 10:   */   
/* 11:   */   public ClientTextBox(EscherRecordData erd)
/* 12:   */   {
/* 13:46 */     super(erd);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ClientTextBox()
/* 17:   */   {
/* 18:54 */     super(EscherRecordType.CLIENT_TEXT_BOX);
/* 19:   */   }
/* 20:   */   
/* 21:   */   byte[] getData()
/* 22:   */   {
/* 23:64 */     this.data = new byte[0];
/* 24:65 */     return setHeaderData(this.data);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ClientTextBox
 * JD-Core Version:    0.7.0.1
 */