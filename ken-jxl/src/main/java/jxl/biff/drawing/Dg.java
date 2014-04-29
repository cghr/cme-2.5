/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ 
/*   5:    */ class Dg
/*   6:    */   extends EscherAtom
/*   7:    */ {
/*   8:    */   private byte[] data;
/*   9:    */   private int drawingId;
/*  10:    */   private int shapeCount;
/*  11:    */   private int seed;
/*  12:    */   
/*  13:    */   public Dg(EscherRecordData erd)
/*  14:    */   {
/*  15: 56 */     super(erd);
/*  16: 57 */     this.drawingId = getInstance();
/*  17:    */     
/*  18: 59 */     byte[] bytes = getBytes();
/*  19: 60 */     this.shapeCount = IntegerHelper.getInt(bytes[0], bytes[1], bytes[2], bytes[3]);
/*  20: 61 */     this.seed = IntegerHelper.getInt(bytes[4], bytes[5], bytes[6], bytes[7]);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Dg(int numDrawings)
/*  24:    */   {
/*  25: 71 */     super(EscherRecordType.DG);
/*  26: 72 */     this.drawingId = 1;
/*  27: 73 */     this.shapeCount = (numDrawings + 1);
/*  28: 74 */     this.seed = (1024 + this.shapeCount + 1);
/*  29: 75 */     setInstance(this.drawingId);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getDrawingId()
/*  33:    */   {
/*  34: 85 */     return this.drawingId;
/*  35:    */   }
/*  36:    */   
/*  37:    */   int getShapeCount()
/*  38:    */   {
/*  39: 95 */     return this.shapeCount;
/*  40:    */   }
/*  41:    */   
/*  42:    */   byte[] getData()
/*  43:    */   {
/*  44:105 */     this.data = new byte[8];
/*  45:106 */     IntegerHelper.getFourBytes(this.shapeCount, this.data, 0);
/*  46:107 */     IntegerHelper.getFourBytes(this.seed, this.data, 4);
/*  47:    */     
/*  48:109 */     return setHeaderData(this.data);
/*  49:    */   }
/*  50:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Dg
 * JD-Core Version:    0.7.0.1
 */