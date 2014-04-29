/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.common.Logger;
/*   5:    */ 
/*   6:    */ class Sp
/*   7:    */   extends EscherAtom
/*   8:    */ {
/*   9: 34 */   private static Logger logger = Logger.getLogger(Sp.class);
/*  10:    */   private byte[] data;
/*  11:    */   private int shapeType;
/*  12:    */   private int shapeId;
/*  13:    */   private int persistenceFlags;
/*  14:    */   
/*  15:    */   public Sp(EscherRecordData erd)
/*  16:    */   {
/*  17: 63 */     super(erd);
/*  18: 64 */     this.shapeType = getInstance();
/*  19: 65 */     byte[] bytes = getBytes();
/*  20: 66 */     this.shapeId = IntegerHelper.getInt(bytes[0], bytes[1], bytes[2], bytes[3]);
/*  21: 67 */     this.persistenceFlags = IntegerHelper.getInt(bytes[4], bytes[5], 
/*  22: 68 */       bytes[6], bytes[7]);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public Sp(ShapeType st, int sid, int p)
/*  26:    */   {
/*  27: 80 */     super(EscherRecordType.SP);
/*  28: 81 */     setVersion(2);
/*  29: 82 */     this.shapeType = st.getValue();
/*  30: 83 */     this.shapeId = sid;
/*  31: 84 */     this.persistenceFlags = p;
/*  32: 85 */     setInstance(this.shapeType);
/*  33:    */   }
/*  34:    */   
/*  35:    */   int getShapeId()
/*  36:    */   {
/*  37: 95 */     return this.shapeId;
/*  38:    */   }
/*  39:    */   
/*  40:    */   int getShapeType()
/*  41:    */   {
/*  42:105 */     return this.shapeType;
/*  43:    */   }
/*  44:    */   
/*  45:    */   byte[] getData()
/*  46:    */   {
/*  47:115 */     this.data = new byte[8];
/*  48:116 */     IntegerHelper.getFourBytes(this.shapeId, this.data, 0);
/*  49:117 */     IntegerHelper.getFourBytes(this.persistenceFlags, this.data, 4);
/*  50:118 */     return setHeaderData(this.data);
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.Sp
 * JD-Core Version:    0.7.0.1
 */