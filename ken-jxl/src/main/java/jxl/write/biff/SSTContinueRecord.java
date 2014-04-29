/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.biff.Type;
/*   8:    */ import jxl.biff.WritableRecordData;
/*   9:    */ 
/*  10:    */ class SSTContinueRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13:    */   private String firstString;
/*  14:    */   private boolean includeLength;
/*  15:    */   private int firstStringLength;
/*  16:    */   private ArrayList strings;
/*  17:    */   private ArrayList stringLengths;
/*  18:    */   private byte[] data;
/*  19:    */   private int byteCount;
/*  20: 68 */   private static int maxBytes = 8224;
/*  21:    */   
/*  22:    */   public SSTContinueRecord()
/*  23:    */   {
/*  24: 79 */     super(Type.CONTINUE);
/*  25:    */     
/*  26: 81 */     this.byteCount = 0;
/*  27: 82 */     this.strings = new ArrayList(50);
/*  28: 83 */     this.stringLengths = new ArrayList(50);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public int setFirstString(String s, boolean b)
/*  32:    */   {
/*  33: 95 */     this.includeLength = b;
/*  34: 96 */     this.firstStringLength = s.length();
/*  35:    */     
/*  36: 98 */     int bytes = 0;
/*  37:100 */     if (!this.includeLength) {
/*  38:102 */       bytes = s.length() * 2 + 1;
/*  39:    */     } else {
/*  40:106 */       bytes = s.length() * 2 + 3;
/*  41:    */     }
/*  42:109 */     if (bytes <= maxBytes)
/*  43:    */     {
/*  44:111 */       this.firstString = s;
/*  45:112 */       this.byteCount += bytes;
/*  46:113 */       return 0;
/*  47:    */     }
/*  48:118 */     int charsAvailable = this.includeLength ? (maxBytes - 4) / 2 : 
/*  49:119 */       (maxBytes - 2) / 2;
/*  50:    */     
/*  51:    */ 
/*  52:122 */     this.firstString = s.substring(0, charsAvailable);
/*  53:123 */     this.byteCount = (maxBytes - 1);
/*  54:    */     
/*  55:125 */     return s.length() - charsAvailable;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public int getOffset()
/*  59:    */   {
/*  60:135 */     return this.byteCount;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int add(String s)
/*  64:    */   {
/*  65:148 */     int bytes = s.length() * 2 + 3;
/*  66:152 */     if (this.byteCount >= maxBytes - 5) {
/*  67:154 */       return s.length();
/*  68:    */     }
/*  69:157 */     this.stringLengths.add(new Integer(s.length()));
/*  70:159 */     if (bytes + this.byteCount < maxBytes)
/*  71:    */     {
/*  72:162 */       this.strings.add(s);
/*  73:163 */       this.byteCount += bytes;
/*  74:164 */       return 0;
/*  75:    */     }
/*  76:168 */     int bytesLeft = maxBytes - 3 - this.byteCount;
/*  77:169 */     int charsAvailable = bytesLeft % 2 == 0 ? bytesLeft / 2 : 
/*  78:170 */       (bytesLeft - 1) / 2;
/*  79:    */     
/*  80:    */ 
/*  81:173 */     this.strings.add(s.substring(0, charsAvailable));
/*  82:174 */     this.byteCount += charsAvailable * 2 + 3;
/*  83:    */     
/*  84:176 */     return s.length() - charsAvailable;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public byte[] getData()
/*  88:    */   {
/*  89:186 */     this.data = new byte[this.byteCount];
/*  90:    */     
/*  91:188 */     int pos = 0;
/*  92:191 */     if (this.includeLength)
/*  93:    */     {
/*  94:193 */       IntegerHelper.getTwoBytes(this.firstStringLength, this.data, 0);
/*  95:194 */       this.data[2] = 1;
/*  96:195 */       pos = 3;
/*  97:    */     }
/*  98:    */     else
/*  99:    */     {
/* 100:200 */       this.data[0] = 1;
/* 101:201 */       pos = 1;
/* 102:    */     }
/* 103:204 */     StringHelper.getUnicodeBytes(this.firstString, this.data, pos);
/* 104:205 */     pos += this.firstString.length() * 2;
/* 105:    */     
/* 106:    */ 
/* 107:208 */     Iterator i = this.strings.iterator();
/* 108:209 */     String s = null;
/* 109:210 */     int length = 0;
/* 110:211 */     int count = 0;
/* 111:212 */     while (i.hasNext())
/* 112:    */     {
/* 113:214 */       s = (String)i.next();
/* 114:215 */       length = ((Integer)this.stringLengths.get(count)).intValue();
/* 115:216 */       IntegerHelper.getTwoBytes(length, this.data, pos);
/* 116:217 */       this.data[(pos + 2)] = 1;
/* 117:218 */       StringHelper.getUnicodeBytes(s, this.data, pos + 3);
/* 118:219 */       pos += s.length() * 2 + 3;
/* 119:220 */       count++;
/* 120:    */     }
/* 121:223 */     return this.data;
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SSTContinueRecord
 * JD-Core Version:    0.7.0.1
 */