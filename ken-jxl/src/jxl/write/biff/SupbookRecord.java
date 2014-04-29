/*   1:    */ package jxl.write.biff;
/*   2:    */ 
/*   3:    */ import jxl.WorkbookSettings;
/*   4:    */ import jxl.biff.EncodedURLHelper;
/*   5:    */ import jxl.biff.IntegerHelper;
/*   6:    */ import jxl.biff.StringHelper;
/*   7:    */ import jxl.biff.Type;
/*   8:    */ import jxl.biff.WritableRecordData;
/*   9:    */ import jxl.common.Assert;
/*  10:    */ import jxl.common.Logger;
/*  11:    */ 
/*  12:    */ class SupbookRecord
/*  13:    */   extends WritableRecordData
/*  14:    */ {
/*  15: 41 */   private static Logger logger = Logger.getLogger(SupbookRecord.class);
/*  16:    */   private SupbookType type;
/*  17:    */   private byte[] data;
/*  18:    */   private int numSheets;
/*  19:    */   private String fileName;
/*  20:    */   private String[] sheetNames;
/*  21:    */   private WorkbookSettings workbookSettings;
/*  22: 78 */   public static final SupbookType INTERNAL = new SupbookType(null);
/*  23: 79 */   public static final SupbookType EXTERNAL = new SupbookType(null);
/*  24: 80 */   public static final SupbookType ADDIN = new SupbookType(null);
/*  25: 81 */   public static final SupbookType LINK = new SupbookType(null);
/*  26: 82 */   public static final SupbookType UNKNOWN = new SupbookType(null);
/*  27:    */   
/*  28:    */   public SupbookRecord()
/*  29:    */   {
/*  30: 89 */     super(Type.SUPBOOK);
/*  31: 90 */     this.type = ADDIN;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public SupbookRecord(int sheets, WorkbookSettings ws)
/*  35:    */   {
/*  36: 98 */     super(Type.SUPBOOK);
/*  37:    */     
/*  38:100 */     this.numSheets = sheets;
/*  39:101 */     this.type = INTERNAL;
/*  40:102 */     this.workbookSettings = ws;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public SupbookRecord(String fn, WorkbookSettings ws)
/*  44:    */   {
/*  45:113 */     super(Type.SUPBOOK);
/*  46:    */     
/*  47:115 */     this.fileName = fn;
/*  48:116 */     this.numSheets = 1;
/*  49:117 */     this.sheetNames = new String[0];
/*  50:118 */     this.workbookSettings = ws;
/*  51:    */     
/*  52:120 */     this.type = EXTERNAL;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public SupbookRecord(jxl.read.biff.SupbookRecord sr, WorkbookSettings ws)
/*  56:    */   {
/*  57:128 */     super(Type.SUPBOOK);
/*  58:    */     
/*  59:130 */     this.workbookSettings = ws;
/*  60:131 */     if (sr.getType() == jxl.read.biff.SupbookRecord.INTERNAL)
/*  61:    */     {
/*  62:133 */       this.type = INTERNAL;
/*  63:134 */       this.numSheets = sr.getNumberOfSheets();
/*  64:    */     }
/*  65:136 */     else if (sr.getType() == jxl.read.biff.SupbookRecord.EXTERNAL)
/*  66:    */     {
/*  67:138 */       this.type = EXTERNAL;
/*  68:139 */       this.numSheets = sr.getNumberOfSheets();
/*  69:140 */       this.fileName = sr.getFileName();
/*  70:141 */       this.sheetNames = new String[this.numSheets];
/*  71:143 */       for (int i = 0; i < this.numSheets; i++) {
/*  72:145 */         this.sheetNames[i] = sr.getSheetName(i);
/*  73:    */       }
/*  74:    */     }
/*  75:149 */     if (sr.getType() == jxl.read.biff.SupbookRecord.ADDIN) {
/*  76:151 */       logger.warn("Supbook type is addin");
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   private void initInternal(jxl.read.biff.SupbookRecord sr)
/*  81:    */   {
/*  82:162 */     this.numSheets = sr.getNumberOfSheets();
/*  83:163 */     initInternal();
/*  84:    */   }
/*  85:    */   
/*  86:    */   private void initInternal()
/*  87:    */   {
/*  88:171 */     this.data = new byte[4];
/*  89:    */     
/*  90:173 */     IntegerHelper.getTwoBytes(this.numSheets, this.data, 0);
/*  91:174 */     this.data[2] = 1;
/*  92:175 */     this.data[3] = 4;
/*  93:176 */     this.type = INTERNAL;
/*  94:    */   }
/*  95:    */   
/*  96:    */   void adjustInternal(int sheets)
/*  97:    */   {
/*  98:187 */     Assert.verify(this.type == INTERNAL);
/*  99:188 */     this.numSheets = sheets;
/* 100:189 */     initInternal();
/* 101:    */   }
/* 102:    */   
/* 103:    */   private void initExternal()
/* 104:    */   {
/* 105:197 */     int totalSheetNameLength = 0;
/* 106:198 */     for (int i = 0; i < this.numSheets; i++) {
/* 107:200 */       totalSheetNameLength += this.sheetNames[i].length();
/* 108:    */     }
/* 109:203 */     byte[] fileNameData = EncodedURLHelper.getEncodedURL(this.fileName, 
/* 110:204 */       this.workbookSettings);
/* 111:205 */     int dataLength = 6 + 
/* 112:206 */       fileNameData.length + 
/* 113:207 */       this.numSheets * 3 + totalSheetNameLength * 2;
/* 114:    */     
/* 115:209 */     this.data = new byte[dataLength];
/* 116:    */     
/* 117:211 */     IntegerHelper.getTwoBytes(this.numSheets, this.data, 0);
/* 118:    */     
/* 119:    */ 
/* 120:    */ 
/* 121:215 */     int pos = 2;
/* 122:216 */     IntegerHelper.getTwoBytes(fileNameData.length + 1, this.data, pos);
/* 123:217 */     this.data[(pos + 2)] = 0;
/* 124:218 */     this.data[(pos + 3)] = 1;
/* 125:219 */     System.arraycopy(fileNameData, 0, this.data, pos + 4, fileNameData.length);
/* 126:    */     
/* 127:221 */     pos += 4 + fileNameData.length;
/* 128:224 */     for (int i = 0; i < this.sheetNames.length; i++)
/* 129:    */     {
/* 130:226 */       IntegerHelper.getTwoBytes(this.sheetNames[i].length(), this.data, pos);
/* 131:227 */       this.data[(pos + 2)] = 1;
/* 132:228 */       StringHelper.getUnicodeBytes(this.sheetNames[i], this.data, pos + 3);
/* 133:229 */       pos += 3 + this.sheetNames[i].length() * 2;
/* 134:    */     }
/* 135:    */   }
/* 136:    */   
/* 137:    */   private void initAddin()
/* 138:    */   {
/* 139:238 */     this.data = new byte[] { 1, 0, 1, 58 };
/* 140:    */   }
/* 141:    */   
/* 142:    */   public byte[] getData()
/* 143:    */   {
/* 144:248 */     if (this.type == INTERNAL)
/* 145:    */     {
/* 146:250 */       initInternal();
/* 147:    */     }
/* 148:252 */     else if (this.type == EXTERNAL)
/* 149:    */     {
/* 150:254 */       initExternal();
/* 151:    */     }
/* 152:256 */     else if (this.type == ADDIN)
/* 153:    */     {
/* 154:258 */       initAddin();
/* 155:    */     }
/* 156:    */     else
/* 157:    */     {
/* 158:262 */       logger.warn("unsupported supbook type - defaulting to internal");
/* 159:263 */       initInternal();
/* 160:    */     }
/* 161:266 */     return this.data;
/* 162:    */   }
/* 163:    */   
/* 164:    */   public SupbookType getType()
/* 165:    */   {
/* 166:276 */     return this.type;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public int getNumberOfSheets()
/* 170:    */   {
/* 171:287 */     return this.numSheets;
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getFileName()
/* 175:    */   {
/* 176:297 */     return this.fileName;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int getSheetIndex(String s)
/* 180:    */   {
/* 181:308 */     boolean found = false;
/* 182:309 */     int sheetIndex = 0;
/* 183:310 */     for (int i = 0; (i < this.sheetNames.length) && (!found); i++) {
/* 184:312 */       if (this.sheetNames[i].equals(s))
/* 185:    */       {
/* 186:314 */         found = true;
/* 187:315 */         sheetIndex = 0;
/* 188:    */       }
/* 189:    */     }
/* 190:319 */     if (found) {
/* 191:321 */       return sheetIndex;
/* 192:    */     }
/* 193:325 */     String[] names = new String[this.sheetNames.length + 1];
/* 194:326 */     System.arraycopy(this.sheetNames, 0, names, 0, this.sheetNames.length);
/* 195:327 */     names[this.sheetNames.length] = s;
/* 196:328 */     this.sheetNames = names;
/* 197:329 */     return this.sheetNames.length - 1;
/* 198:    */   }
/* 199:    */   
/* 200:    */   public String getSheetName(int s)
/* 201:    */   {
/* 202:339 */     return this.sheetNames[s];
/* 203:    */   }
/* 204:    */   
/* 205:    */   private static class SupbookType {}
/* 206:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.SupbookRecord
 * JD-Core Version:    0.7.0.1
 */