/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.biff.WritableRecordData;
/*   8:    */ 
/*   9:    */ class DBCellRecord
/*  10:    */   extends WritableRecordData
/*  11:    */ {
/*  12:    */   private int rowPos;
/*  13:    */   private int cellOffset;
/*  14:    */   private ArrayList cellRowPositions;
/*  15:    */   private int position;
/*  16:    */   
/*  17:    */   public DBCellRecord(int rp)
/*  18:    */   {
/*  19: 63 */     super(Type.DBCELL);
/*  20: 64 */     this.rowPos = rp;
/*  21: 65 */     this.cellRowPositions = new ArrayList(10);
/*  22:    */   }
/*  23:    */   
/*  24:    */   void setCellOffset(int pos)
/*  25:    */   {
/*  26: 75 */     this.cellOffset = pos;
/*  27:    */   }
/*  28:    */   
/*  29:    */   void addCellRowPosition(int pos)
/*  30:    */   {
/*  31: 85 */     this.cellRowPositions.add(new Integer(pos));
/*  32:    */   }
/*  33:    */   
/*  34:    */   void setPosition(int pos)
/*  35:    */   {
/*  36: 95 */     this.position = pos;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected byte[] getData()
/*  40:    */   {
/*  41:105 */     byte[] data = new byte[4 + 2 * this.cellRowPositions.size()];
/*  42:    */     
/*  43:    */ 
/*  44:108 */     IntegerHelper.getFourBytes(this.position - this.rowPos, data, 0);
/*  45:    */     
/*  46:    */ 
/*  47:111 */     int pos = 4;
/*  48:112 */     int lastCellPos = this.cellOffset;
/*  49:113 */     Iterator i = this.cellRowPositions.iterator();
/*  50:114 */     while (i.hasNext())
/*  51:    */     {
/*  52:116 */       int cellPos = ((Integer)i.next()).intValue();
/*  53:117 */       IntegerHelper.getTwoBytes(cellPos - lastCellPos, data, pos);
/*  54:118 */       lastCellPos = cellPos;
/*  55:119 */       pos += 2;
/*  56:    */     }
/*  57:122 */     return data;
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.DBCellRecord
 * JD-Core Version:    0.7.0.1
 */