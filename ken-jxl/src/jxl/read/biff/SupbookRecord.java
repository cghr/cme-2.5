/*   1:    */ package jxl.read.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.IntegerHelper;
/*   5:    */ import jxl.biff.RecordData;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ 
/*   9:    */ public class SupbookRecord
/*  10:    */   extends RecordData
/*  11:    */ {
/*  12: 38 */   private static Logger logger = Logger.getLogger(SupbookRecord.class);
/*  13:    */   private Type type;
/*  14:    */   private int numSheets;
/*  15:    */   private String fileName;
/*  16:    */   private String[] sheetNames;
/*  17: 66 */   public static final Type INTERNAL = new Type(null);
/*  18: 67 */   public static final Type EXTERNAL = new Type(null);
/*  19: 68 */   public static final Type ADDIN = new Type(null);
/*  20: 69 */   public static final Type LINK = new Type(null);
/*  21: 70 */   public static final Type UNKNOWN = new Type(null);
/*  22:    */   
/*  23:    */   SupbookRecord(Record t, WorkbookSettings ws)
/*  24:    */   {
/*  25: 80 */     super(t);
/*  26: 81 */     byte[] data = getRecord().getData();
/*  27: 84 */     if (data.length == 4)
/*  28:    */     {
/*  29: 86 */       if ((data[2] == 1) && (data[3] == 4)) {
/*  30: 88 */         this.type = INTERNAL;
/*  31: 90 */       } else if ((data[2] == 1) && (data[3] == 58)) {
/*  32: 92 */         this.type = ADDIN;
/*  33:    */       } else {
/*  34: 96 */         this.type = UNKNOWN;
/*  35:    */       }
/*  36:    */     }
/*  37: 99 */     else if ((data[0] == 0) && (data[1] == 0)) {
/*  38:101 */       this.type = LINK;
/*  39:    */     } else {
/*  40:105 */       this.type = EXTERNAL;
/*  41:    */     }
/*  42:108 */     if (this.type == INTERNAL) {
/*  43:110 */       this.numSheets = IntegerHelper.getInt(data[0], data[1]);
/*  44:    */     }
/*  45:113 */     if (this.type == EXTERNAL) {
/*  46:115 */       readExternal(data, ws);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   private void readExternal(byte[] data, WorkbookSettings ws)
/*  51:    */   {
/*  52:127 */     this.numSheets = IntegerHelper.getInt(data[0], data[1]);
/*  53:    */     
/*  54:    */ 
/*  55:130 */     int ln = IntegerHelper.getInt(data[2], data[3]) - 1;
/*  56:131 */     int pos = 0;
/*  57:133 */     if (data[4] == 0)
/*  58:    */     {
/*  59:136 */       int encoding = data[5];
/*  60:137 */       pos = 6;
/*  61:138 */       if (encoding == 0)
/*  62:    */       {
/*  63:140 */         this.fileName = StringHelper.getString(data, ln, pos, ws);
/*  64:141 */         pos += ln;
/*  65:    */       }
/*  66:    */       else
/*  67:    */       {
/*  68:145 */         this.fileName = getEncodedFilename(data, ln, pos);
/*  69:146 */         pos += ln;
/*  70:    */       }
/*  71:    */     }
/*  72:    */     else
/*  73:    */     {
/*  74:152 */       int encoding = IntegerHelper.getInt(data[5], data[6]);
/*  75:153 */       pos = 7;
/*  76:154 */       if (encoding == 0)
/*  77:    */       {
/*  78:156 */         this.fileName = StringHelper.getUnicodeString(data, ln, pos);
/*  79:157 */         pos += ln * 2;
/*  80:    */       }
/*  81:    */       else
/*  82:    */       {
/*  83:161 */         this.fileName = getUnicodeEncodedFilename(data, ln, pos);
/*  84:162 */         pos += ln * 2;
/*  85:    */       }
/*  86:    */     }
/*  87:166 */     this.sheetNames = new String[this.numSheets];
/*  88:168 */     for (int i = 0; i < this.sheetNames.length; i++)
/*  89:    */     {
/*  90:170 */       ln = IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/*  91:172 */       if (data[(pos + 2)] == 0)
/*  92:    */       {
/*  93:174 */         this.sheetNames[i] = StringHelper.getString(data, ln, pos + 3, ws);
/*  94:175 */         pos += ln + 3;
/*  95:    */       }
/*  96:177 */       else if (data[(pos + 2)] == 1)
/*  97:    */       {
/*  98:179 */         this.sheetNames[i] = StringHelper.getUnicodeString(data, ln, pos + 3);
/*  99:180 */         pos += ln * 2 + 3;
/* 100:    */       }
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public Type getType()
/* 105:    */   {
/* 106:192 */     return this.type;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public int getNumberOfSheets()
/* 110:    */   {
/* 111:203 */     return this.numSheets;
/* 112:    */   }
/* 113:    */   
/* 114:    */   public String getFileName()
/* 115:    */   {
/* 116:213 */     return this.fileName;
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String getSheetName(int i)
/* 120:    */   {
/* 121:224 */     return this.sheetNames[i];
/* 122:    */   }
/* 123:    */   
/* 124:    */   public byte[] getData()
/* 125:    */   {
/* 126:234 */     return getRecord().getData();
/* 127:    */   }
/* 128:    */   
/* 129:    */   private String getEncodedFilename(byte[] data, int ln, int pos)
/* 130:    */   {
/* 131:247 */     StringBuffer buf = new StringBuffer();
/* 132:248 */     int endpos = pos + ln;
/* 133:249 */     while (pos < endpos)
/* 134:    */     {
/* 135:251 */       char c = (char)data[pos];
/* 136:253 */       if (c == '\001')
/* 137:    */       {
/* 138:256 */         pos++;
/* 139:257 */         c = (char)data[pos];
/* 140:258 */         buf.append(c);
/* 141:259 */         buf.append(":\\\\");
/* 142:    */       }
/* 143:261 */       else if (c == '\002')
/* 144:    */       {
/* 145:264 */         buf.append('\\');
/* 146:    */       }
/* 147:266 */       else if (c == '\003')
/* 148:    */       {
/* 149:269 */         buf.append('\\');
/* 150:    */       }
/* 151:271 */       else if (c == '\004')
/* 152:    */       {
/* 153:274 */         buf.append("..\\");
/* 154:    */       }
/* 155:    */       else
/* 156:    */       {
/* 157:279 */         buf.append(c);
/* 158:    */       }
/* 159:282 */       pos++;
/* 160:    */     }
/* 161:285 */     return buf.toString();
/* 162:    */   }
/* 163:    */   
/* 164:    */   private String getUnicodeEncodedFilename(byte[] data, int ln, int pos)
/* 165:    */   {
/* 166:298 */     StringBuffer buf = new StringBuffer();
/* 167:299 */     int endpos = pos + ln * 2;
/* 168:300 */     while (pos < endpos)
/* 169:    */     {
/* 170:302 */       char c = (char)IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/* 171:304 */       if (c == '\001')
/* 172:    */       {
/* 173:307 */         pos += 2;
/* 174:308 */         c = (char)IntegerHelper.getInt(data[pos], data[(pos + 1)]);
/* 175:309 */         buf.append(c);
/* 176:310 */         buf.append(":\\\\");
/* 177:    */       }
/* 178:312 */       else if (c == '\002')
/* 179:    */       {
/* 180:315 */         buf.append('\\');
/* 181:    */       }
/* 182:317 */       else if (c == '\003')
/* 183:    */       {
/* 184:320 */         buf.append('\\');
/* 185:    */       }
/* 186:322 */       else if (c == '\004')
/* 187:    */       {
/* 188:325 */         buf.append("..\\");
/* 189:    */       }
/* 190:    */       else
/* 191:    */       {
/* 192:330 */         buf.append(c);
/* 193:    */       }
/* 194:333 */       pos += 2;
/* 195:    */     }
/* 196:336 */     return buf.toString();
/* 197:    */   }
/* 198:    */   
/* 199:    */   private static class Type {}
/* 200:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.read.biff.SupbookRecord
 * JD-Core Version:    0.7.0.1
 */