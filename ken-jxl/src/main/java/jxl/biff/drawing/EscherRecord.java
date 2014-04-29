/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ 
/*   5:    */ abstract class EscherRecord
/*   6:    */ {
/*   7: 34 */   private static Logger logger = Logger.getLogger(EscherRecord.class);
/*   8:    */   private EscherRecordData data;
/*   9:    */   protected static final int HEADER_LENGTH = 8;
/*  10:    */   
/*  11:    */   protected EscherRecord(EscherRecordData erd)
/*  12:    */   {
/*  13: 54 */     this.data = erd;
/*  14:    */   }
/*  15:    */   
/*  16:    */   protected EscherRecord(EscherRecordType type)
/*  17:    */   {
/*  18: 64 */     this.data = new EscherRecordData(type);
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected void setContainer(boolean cont)
/*  22:    */   {
/*  23: 74 */     this.data.setContainer(cont);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int getLength()
/*  27:    */   {
/*  28: 84 */     return this.data.getLength() + 8;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected final EscherStream getEscherStream()
/*  32:    */   {
/*  33: 94 */     return this.data.getEscherStream();
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected final int getPos()
/*  37:    */   {
/*  38:104 */     return this.data.getPos();
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected final int getInstance()
/*  42:    */   {
/*  43:114 */     return this.data.getInstance();
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected final void setInstance(int i)
/*  47:    */   {
/*  48:124 */     this.data.setInstance(i);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected final void setVersion(int v)
/*  52:    */   {
/*  53:134 */     this.data.setVersion(v);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public EscherRecordType getType()
/*  57:    */   {
/*  58:144 */     return this.data.getType();
/*  59:    */   }
/*  60:    */   
/*  61:    */   abstract byte[] getData();
/*  62:    */   
/*  63:    */   final byte[] setHeaderData(byte[] d)
/*  64:    */   {
/*  65:164 */     return this.data.setHeaderData(d);
/*  66:    */   }
/*  67:    */   
/*  68:    */   byte[] getBytes()
/*  69:    */   {
/*  70:174 */     return this.data.getBytes();
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected int getStreamLength()
/*  74:    */   {
/*  75:184 */     return this.data.getStreamLength();
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected EscherRecordData getEscherData()
/*  79:    */   {
/*  80:194 */     return this.data;
/*  81:    */   }
/*  82:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.EscherRecord
 * JD-Core Version:    0.7.0.1
 */