/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.Type;
/*   7:    */ import jxl.biff.WritableRecordData;
/*   8:    */ 
/*   9:    */ class ExternalSheetRecord
/*  10:    */   extends WritableRecordData
/*  11:    */ {
/*  12:    */   private byte[] data;
/*  13:    */   private ArrayList xtis;
/*  14:    */   
/*  15:    */   private static class XTI
/*  16:    */   {
/*  17:    */     int supbookIndex;
/*  18:    */     int firstTab;
/*  19:    */     int lastTab;
/*  20:    */     
/*  21:    */     XTI(int s, int f, int l)
/*  22:    */     {
/*  23: 56 */       this.supbookIndex = s;
/*  24: 57 */       this.firstTab = f;
/*  25: 58 */       this.lastTab = l;
/*  26:    */     }
/*  27:    */     
/*  28:    */     void sheetInserted(int index)
/*  29:    */     {
/*  30: 63 */       if (this.firstTab >= index) {
/*  31: 65 */         this.firstTab += 1;
/*  32:    */       }
/*  33: 68 */       if (this.lastTab >= index) {
/*  34: 70 */         this.lastTab += 1;
/*  35:    */       }
/*  36:    */     }
/*  37:    */     
/*  38:    */     void sheetRemoved(int index)
/*  39:    */     {
/*  40: 76 */       if (this.firstTab == index) {
/*  41: 78 */         this.firstTab = 0;
/*  42:    */       }
/*  43: 81 */       if (this.lastTab == index) {
/*  44: 83 */         this.lastTab = 0;
/*  45:    */       }
/*  46: 86 */       if (this.firstTab > index) {
/*  47: 88 */         this.firstTab -= 1;
/*  48:    */       }
/*  49: 91 */       if (this.lastTab > index) {
/*  50: 93 */         this.lastTab -= 1;
/*  51:    */       }
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ExternalSheetRecord(jxl.read.biff.ExternalSheetRecord esf)
/*  56:    */   {
/*  57:105 */     super(Type.EXTERNSHEET);
/*  58:    */     
/*  59:107 */     this.xtis = new ArrayList(esf.getNumRecords());
/*  60:108 */     XTI xti = null;
/*  61:109 */     for (int i = 0; i < esf.getNumRecords(); i++)
/*  62:    */     {
/*  63:111 */       xti = new XTI(esf.getSupbookIndex(i), 
/*  64:112 */         esf.getFirstTabIndex(i), 
/*  65:113 */         esf.getLastTabIndex(i));
/*  66:114 */       this.xtis.add(xti);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public ExternalSheetRecord()
/*  71:    */   {
/*  72:123 */     super(Type.EXTERNSHEET);
/*  73:124 */     this.xtis = new ArrayList();
/*  74:    */   }
/*  75:    */   
/*  76:    */   int getIndex(int supbookind, int sheetind)
/*  77:    */   {
/*  78:135 */     Iterator i = this.xtis.iterator();
/*  79:136 */     XTI xti = null;
/*  80:137 */     boolean found = false;
/*  81:138 */     int pos = 0;
/*  82:139 */     while ((i.hasNext()) && (!found))
/*  83:    */     {
/*  84:141 */       xti = (XTI)i.next();
/*  85:143 */       if ((xti.supbookIndex == supbookind) && 
/*  86:144 */         (xti.firstTab == sheetind)) {
/*  87:146 */         found = true;
/*  88:    */       } else {
/*  89:150 */         pos++;
/*  90:    */       }
/*  91:    */     }
/*  92:154 */     if (!found)
/*  93:    */     {
/*  94:156 */       xti = new XTI(supbookind, sheetind, sheetind);
/*  95:157 */       this.xtis.add(xti);
/*  96:158 */       pos = this.xtis.size() - 1;
/*  97:    */     }
/*  98:161 */     return pos;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public byte[] getData()
/* 102:    */   {
/* 103:171 */     byte[] data = new byte[2 + this.xtis.size() * 6];
/* 104:    */     
/* 105:173 */     int pos = 0;
/* 106:174 */     IntegerHelper.getTwoBytes(this.xtis.size(), data, 0);
/* 107:175 */     pos += 2;
/* 108:    */     
/* 109:177 */     Iterator i = this.xtis.iterator();
/* 110:178 */     XTI xti = null;
/* 111:179 */     while (i.hasNext())
/* 112:    */     {
/* 113:181 */       xti = (XTI)i.next();
/* 114:182 */       IntegerHelper.getTwoBytes(xti.supbookIndex, data, pos);
/* 115:183 */       IntegerHelper.getTwoBytes(xti.firstTab, data, pos + 2);
/* 116:184 */       IntegerHelper.getTwoBytes(xti.lastTab, data, pos + 4);
/* 117:185 */       pos += 6;
/* 118:    */     }
/* 119:188 */     return data;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int getSupbookIndex(int index)
/* 123:    */   {
/* 124:199 */     return ((XTI)this.xtis.get(index)).supbookIndex;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public int getFirstTabIndex(int index)
/* 128:    */   {
/* 129:210 */     return ((XTI)this.xtis.get(index)).firstTab;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public int getLastTabIndex(int index)
/* 133:    */   {
/* 134:221 */     return ((XTI)this.xtis.get(index)).lastTab;
/* 135:    */   }
/* 136:    */   
/* 137:    */   void sheetInserted(int index)
/* 138:    */   {
/* 139:230 */     XTI xti = null;
/* 140:231 */     for (Iterator i = this.xtis.iterator(); i.hasNext();)
/* 141:    */     {
/* 142:233 */       xti = (XTI)i.next();
/* 143:234 */       xti.sheetInserted(index);
/* 144:    */     }
/* 145:    */   }
/* 146:    */   
/* 147:    */   void sheetRemoved(int index)
/* 148:    */   {
/* 149:244 */     XTI xti = null;
/* 150:245 */     for (Iterator i = this.xtis.iterator(); i.hasNext();)
/* 151:    */     {
/* 152:247 */       xti = (XTI)i.next();
/* 153:248 */       xti.sheetRemoved(index);
/* 154:    */     }
/* 155:    */   }
/* 156:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.ExternalSheetRecord
 * JD-Core Version:    0.7.0.1
 */