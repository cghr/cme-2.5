/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ import jxl.common.Logger;
/*   7:    */ 
/*   8:    */ public final class Record
/*   9:    */ {
/*  10: 38 */   private static final Logger logger = Logger.getLogger(Record.class);
/*  11:    */   private int code;
/*  12:    */   private Type type;
/*  13:    */   private int length;
/*  14:    */   private int dataPos;
/*  15:    */   private File file;
/*  16:    */   private byte[] data;
/*  17:    */   private ArrayList continueRecords;
/*  18:    */   
/*  19:    */   Record(byte[] d, int offset, File f)
/*  20:    */   {
/*  21: 79 */     this.code = IntegerHelper.getInt(d[offset], d[(offset + 1)]);
/*  22: 80 */     this.length = IntegerHelper.getInt(d[(offset + 2)], d[(offset + 3)]);
/*  23: 81 */     this.file = f;
/*  24: 82 */     this.file.skip(4);
/*  25: 83 */     this.dataPos = f.getPos();
/*  26: 84 */     this.file.skip(this.length);
/*  27: 85 */     this.type = Type.getType(this.code);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Type getType()
/*  31:    */   {
/*  32: 95 */     return this.type;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int getLength()
/*  36:    */   {
/*  37:105 */     return this.length;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public byte[] getData()
/*  41:    */   {
/*  42:115 */     if (this.data == null) {
/*  43:117 */       this.data = this.file.read(this.dataPos, this.length);
/*  44:    */     }
/*  45:121 */     if (this.continueRecords != null)
/*  46:    */     {
/*  47:123 */       int size = 0;
/*  48:124 */       byte[][] contData = new byte[this.continueRecords.size()][];
/*  49:125 */       for (int i = 0; i < this.continueRecords.size(); i++)
/*  50:    */       {
/*  51:127 */         Record r = (Record)this.continueRecords.get(i);
/*  52:128 */         contData[i] = r.getData();
/*  53:129 */         byte[] d2 = contData[i];
/*  54:130 */         size += d2.length;
/*  55:    */       }
/*  56:133 */       byte[] d3 = new byte[this.data.length + size];
/*  57:134 */       System.arraycopy(this.data, 0, d3, 0, this.data.length);
/*  58:135 */       int pos = this.data.length;
/*  59:136 */       for (int i = 0; i < contData.length; i++)
/*  60:    */       {
/*  61:138 */         byte[] d2 = contData[i];
/*  62:139 */         System.arraycopy(d2, 0, d3, pos, d2.length);
/*  63:140 */         pos += d2.length;
/*  64:    */       }
/*  65:143 */       this.data = d3;
/*  66:    */     }
/*  67:146 */     return this.data;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int getCode()
/*  71:    */   {
/*  72:156 */     return this.code;
/*  73:    */   }
/*  74:    */   
/*  75:    */   void setType(Type t)
/*  76:    */   {
/*  77:167 */     this.type = t;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void addContinueRecord(Record d)
/*  81:    */   {
/*  82:177 */     if (this.continueRecords == null) {
/*  83:179 */       this.continueRecords = new ArrayList();
/*  84:    */     }
/*  85:182 */     this.continueRecords.add(d);
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.Record
 * JD-Core Version:    0.7.0.1
 */