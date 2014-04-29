/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.biff.RecordData;
/*   4:    */ import jxl.biff.StringHelper;
/*   5:    */ import jxl.biff.Type;
/*   6:    */ 
/*   7:    */ public class SortRecord
/*   8:    */   extends RecordData
/*   9:    */ {
/*  10:    */   private int col1Size;
/*  11:    */   private int col2Size;
/*  12:    */   private int col3Size;
/*  13:    */   private String col1Name;
/*  14:    */   private String col2Name;
/*  15:    */   private String col3Name;
/*  16:    */   private byte optionFlags;
/*  17: 38 */   private boolean sortColumns = false;
/*  18: 39 */   private boolean sortKey1Desc = false;
/*  19: 40 */   private boolean sortKey2Desc = false;
/*  20: 41 */   private boolean sortKey3Desc = false;
/*  21: 42 */   private boolean sortCaseSensitive = false;
/*  22:    */   
/*  23:    */   public SortRecord(Record r)
/*  24:    */   {
/*  25: 50 */     super(Type.SORT);
/*  26:    */     
/*  27: 52 */     byte[] data = r.getData();
/*  28:    */     
/*  29: 54 */     this.optionFlags = data[0];
/*  30:    */     
/*  31: 56 */     this.sortColumns = ((this.optionFlags & 0x1) != 0);
/*  32: 57 */     this.sortKey1Desc = ((this.optionFlags & 0x2) != 0);
/*  33: 58 */     this.sortKey2Desc = ((this.optionFlags & 0x4) != 0);
/*  34: 59 */     this.sortKey3Desc = ((this.optionFlags & 0x8) != 0);
/*  35: 60 */     this.sortCaseSensitive = ((this.optionFlags & 0x10) != 0);
/*  36:    */     
/*  37:    */ 
/*  38:    */ 
/*  39: 64 */     this.col1Size = data[2];
/*  40: 65 */     this.col2Size = data[3];
/*  41: 66 */     this.col3Size = data[4];
/*  42: 67 */     int curPos = 5;
/*  43: 68 */     if (data[(curPos++)] == 0)
/*  44:    */     {
/*  45: 70 */       this.col1Name = new String(data, curPos, this.col1Size);
/*  46: 71 */       curPos += this.col1Size;
/*  47:    */     }
/*  48:    */     else
/*  49:    */     {
/*  50: 75 */       this.col1Name = StringHelper.getUnicodeString(data, this.col1Size, curPos);
/*  51: 76 */       curPos += this.col1Size * 2;
/*  52:    */     }
/*  53: 79 */     if (this.col2Size > 0)
/*  54:    */     {
/*  55: 81 */       if (data[(curPos++)] == 0)
/*  56:    */       {
/*  57: 83 */         this.col2Name = new String(data, curPos, this.col2Size);
/*  58: 84 */         curPos += this.col2Size;
/*  59:    */       }
/*  60:    */       else
/*  61:    */       {
/*  62: 88 */         this.col2Name = StringHelper.getUnicodeString(data, this.col2Size, curPos);
/*  63: 89 */         curPos += this.col2Size * 2;
/*  64:    */       }
/*  65:    */     }
/*  66:    */     else {
/*  67: 94 */       this.col2Name = "";
/*  68:    */     }
/*  69: 96 */     if (this.col3Size > 0)
/*  70:    */     {
/*  71: 98 */       if (data[(curPos++)] == 0)
/*  72:    */       {
/*  73:100 */         this.col3Name = new String(data, curPos, this.col3Size);
/*  74:101 */         curPos += this.col3Size;
/*  75:    */       }
/*  76:    */       else
/*  77:    */       {
/*  78:105 */         this.col3Name = StringHelper.getUnicodeString(data, this.col3Size, curPos);
/*  79:106 */         curPos += this.col3Size * 2;
/*  80:    */       }
/*  81:    */     }
/*  82:    */     else {
/*  83:111 */       this.col3Name = "";
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public String getSortCol1Name()
/*  88:    */   {
/*  89:121 */     return this.col1Name;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getSortCol2Name()
/*  93:    */   {
/*  94:129 */     return this.col2Name;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public String getSortCol3Name()
/*  98:    */   {
/*  99:137 */     return this.col3Name;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean getSortColumns()
/* 103:    */   {
/* 104:145 */     return this.sortColumns;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean getSortKey1Desc()
/* 108:    */   {
/* 109:153 */     return this.sortKey1Desc;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean getSortKey2Desc()
/* 113:    */   {
/* 114:161 */     return this.sortKey2Desc;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean getSortKey3Desc()
/* 118:    */   {
/* 119:169 */     return this.sortKey3Desc;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public boolean getSortCaseSensitive()
/* 123:    */   {
/* 124:177 */     return this.sortCaseSensitive;
/* 125:    */   }
/* 126:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SortRecord
 * JD-Core Version:    0.7.0.1
 */