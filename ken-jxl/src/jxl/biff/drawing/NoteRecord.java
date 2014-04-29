/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ import jxl.read.biff.Record;
/*   8:    */ 
/*   9:    */ public class NoteRecord
/*  10:    */   extends WritableRecordData
/*  11:    */ {
/*  12: 37 */   private static Logger logger = Logger.getLogger(NoteRecord.class);
/*  13:    */   private byte[] data;
/*  14:    */   private int row;
/*  15:    */   private int column;
/*  16:    */   private int objectId;
/*  17:    */   
/*  18:    */   public NoteRecord(Record t)
/*  19:    */   {
/*  20: 66 */     super(t);
/*  21: 67 */     this.data = getRecord().getData();
/*  22: 68 */     this.row = IntegerHelper.getInt(this.data[0], this.data[1]);
/*  23: 69 */     this.column = IntegerHelper.getInt(this.data[2], this.data[3]);
/*  24: 70 */     this.objectId = IntegerHelper.getInt(this.data[6], this.data[7]);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public NoteRecord(byte[] d)
/*  28:    */   {
/*  29: 80 */     super(Type.NOTE);
/*  30: 81 */     this.data = d;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public NoteRecord(int c, int r, int id)
/*  34:    */   {
/*  35: 93 */     super(Type.NOTE);
/*  36: 94 */     this.row = r;
/*  37: 95 */     this.column = c;
/*  38: 96 */     this.objectId = id;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public byte[] getData()
/*  42:    */   {
/*  43:106 */     if (this.data != null) {
/*  44:108 */       return this.data;
/*  45:    */     }
/*  46:111 */     String author = "";
/*  47:112 */     this.data = new byte[8 + author.length() + 4];
/*  48:    */     
/*  49:    */ 
/*  50:115 */     IntegerHelper.getTwoBytes(this.row, this.data, 0);
/*  51:    */     
/*  52:    */ 
/*  53:118 */     IntegerHelper.getTwoBytes(this.column, this.data, 2);
/*  54:    */     
/*  55:    */ 
/*  56:121 */     IntegerHelper.getTwoBytes(this.objectId, this.data, 6);
/*  57:    */     
/*  58:    */ 
/*  59:124 */     IntegerHelper.getTwoBytes(author.length(), this.data, 8);
/*  60:    */     
/*  61:    */ 
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:131 */     return this.data;
/*  67:    */   }
/*  68:    */   
/*  69:    */   int getRow()
/*  70:    */   {
/*  71:141 */     return this.row;
/*  72:    */   }
/*  73:    */   
/*  74:    */   int getColumn()
/*  75:    */   {
/*  76:151 */     return this.column;
/*  77:    */   }
/*  78:    */   
/*  79:    */   public int getObjectId()
/*  80:    */   {
/*  81:161 */     return this.objectId;
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.NoteRecord
 * JD-Core Version:    0.7.0.1
 */