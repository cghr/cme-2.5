/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.biff.Type;
/*   8:    */ import jxl.biff.WritableRecordData;
/*   9:    */ 
/*  10:    */ class SSTRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13:    */   private int numReferences;
/*  14:    */   private int numStrings;
/*  15:    */   private ArrayList strings;
/*  16:    */   private ArrayList stringLengths;
/*  17:    */   private byte[] data;
/*  18:    */   private int byteCount;
/*  19: 63 */   private static int maxBytes = 8216;
/*  20:    */   
/*  21:    */   public SSTRecord(int numRefs, int s)
/*  22:    */   {
/*  23: 75 */     super(Type.SST);
/*  24:    */     
/*  25: 77 */     this.numReferences = numRefs;
/*  26: 78 */     this.numStrings = s;
/*  27: 79 */     this.byteCount = 0;
/*  28: 80 */     this.strings = new ArrayList(50);
/*  29: 81 */     this.stringLengths = new ArrayList(50);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int add(String s)
/*  33:    */   {
/*  34: 94 */     int bytes = s.length() * 2 + 3;
/*  35: 98 */     if (this.byteCount >= maxBytes - 5) {
/*  36:100 */       return s.length() > 0 ? s.length() : -1;
/*  37:    */     }
/*  38:104 */     this.stringLengths.add(new Integer(s.length()));
/*  39:106 */     if (bytes + this.byteCount < maxBytes)
/*  40:    */     {
/*  41:109 */       this.strings.add(s);
/*  42:110 */       this.byteCount += bytes;
/*  43:111 */       return 0;
/*  44:    */     }
/*  45:115 */     int bytesLeft = maxBytes - 3 - this.byteCount;
/*  46:116 */     int charsAvailable = bytesLeft % 2 == 0 ? bytesLeft / 2 : 
/*  47:117 */       (bytesLeft - 1) / 2;
/*  48:    */     
/*  49:    */ 
/*  50:120 */     this.strings.add(s.substring(0, charsAvailable));
/*  51:121 */     this.byteCount += charsAvailable * 2 + 3;
/*  52:    */     
/*  53:123 */     return s.length() - charsAvailable;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public int getOffset()
/*  57:    */   {
/*  58:133 */     return this.byteCount + 8;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public byte[] getData()
/*  62:    */   {
/*  63:143 */     this.data = new byte[this.byteCount + 8];
/*  64:144 */     IntegerHelper.getFourBytes(this.numReferences, this.data, 0);
/*  65:145 */     IntegerHelper.getFourBytes(this.numStrings, this.data, 4);
/*  66:    */     
/*  67:147 */     int pos = 8;
/*  68:148 */     int count = 0;
/*  69:    */     
/*  70:150 */     Iterator i = this.strings.iterator();
/*  71:151 */     String s = null;
/*  72:152 */     int length = 0;
/*  73:153 */     while (i.hasNext())
/*  74:    */     {
/*  75:155 */       s = (String)i.next();
/*  76:156 */       length = ((Integer)this.stringLengths.get(count)).intValue();
/*  77:157 */       IntegerHelper.getTwoBytes(length, this.data, pos);
/*  78:158 */       this.data[(pos + 2)] = 1;
/*  79:159 */       StringHelper.getUnicodeBytes(s, this.data, pos + 3);
/*  80:160 */       pos += s.length() * 2 + 3;
/*  81:161 */       count++;
/*  82:    */     }
/*  83:164 */     return this.data;
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SSTRecord
 * JD-Core Version:    0.7.0.1
 */