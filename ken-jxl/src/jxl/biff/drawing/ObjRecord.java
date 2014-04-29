/*   1:    */ package jxl.biff.drawing;
/*   2:    */ 
/*   3:    */ import jxl.biff.IntegerHelper;
/*   4:    */ import jxl.biff.Type;
/*   5:    */ import jxl.biff.WritableRecordData;
/*   6:    */ import jxl.common.Assert;
/*   7:    */ import jxl.common.Logger;
/*   8:    */ import jxl.read.biff.Record;
/*   9:    */ 
/*  10:    */ public class ObjRecord
/*  11:    */   extends WritableRecordData
/*  12:    */ {
/*  13: 39 */   private static final Logger logger = Logger.getLogger(ObjRecord.class);
/*  14:    */   private ObjType type;
/*  15:    */   private boolean read;
/*  16:    */   private int objectId;
/*  17:    */   
/*  18:    */   private static final class ObjType
/*  19:    */   {
/*  20:    */     public int value;
/*  21:    */     public String desc;
/*  22: 64 */     private static ObjType[] types = new ObjType[0];
/*  23:    */     
/*  24:    */     ObjType(int v, String d)
/*  25:    */     {
/*  26: 68 */       this.value = v;
/*  27: 69 */       this.desc = d;
/*  28:    */       
/*  29: 71 */       ObjType[] oldtypes = types;
/*  30: 72 */       types = new ObjType[types.length + 1];
/*  31: 73 */       System.arraycopy(oldtypes, 0, types, 0, oldtypes.length);
/*  32: 74 */       types[oldtypes.length] = this;
/*  33:    */     }
/*  34:    */     
/*  35:    */     public String toString()
/*  36:    */     {
/*  37: 79 */       return this.desc;
/*  38:    */     }
/*  39:    */     
/*  40:    */     public static ObjType getType(int val)
/*  41:    */     {
/*  42: 84 */       ObjType retval = ObjRecord.UNKNOWN;
/*  43: 85 */       for (int i = 0; (i < types.length) && (retval == ObjRecord.UNKNOWN); i++) {
/*  44: 87 */         if (types[i].value == val) {
/*  45: 89 */           retval = types[i];
/*  46:    */         }
/*  47:    */       }
/*  48: 92 */       return retval;
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52: 97 */   public static final ObjType GROUP = new ObjType(0, "Group");
/*  53: 98 */   public static final ObjType LINE = new ObjType(1, "Line");
/*  54: 99 */   public static final ObjType RECTANGLE = new ObjType(2, "Rectangle");
/*  55:100 */   public static final ObjType OVAL = new ObjType(3, "Oval");
/*  56:101 */   public static final ObjType ARC = new ObjType(4, "Arc");
/*  57:102 */   public static final ObjType CHART = new ObjType(5, "Chart");
/*  58:103 */   public static final ObjType TEXT = new ObjType(6, "Text");
/*  59:104 */   public static final ObjType BUTTON = new ObjType(7, "Button");
/*  60:105 */   public static final ObjType PICTURE = new ObjType(8, "Picture");
/*  61:106 */   public static final ObjType POLYGON = new ObjType(9, "Polygon");
/*  62:107 */   public static final ObjType CHECKBOX = new ObjType(11, "Checkbox");
/*  63:108 */   public static final ObjType OPTION = new ObjType(12, "Option");
/*  64:109 */   public static final ObjType EDITBOX = new ObjType(13, "Edit Box");
/*  65:110 */   public static final ObjType LABEL = new ObjType(14, "Label");
/*  66:111 */   public static final ObjType DIALOGUEBOX = new ObjType(15, "Dialogue Box");
/*  67:112 */   public static final ObjType SPINBOX = new ObjType(16, "Spin Box");
/*  68:113 */   public static final ObjType SCROLLBAR = new ObjType(17, "Scrollbar");
/*  69:114 */   public static final ObjType LISTBOX = new ObjType(18, "List Box");
/*  70:115 */   public static final ObjType GROUPBOX = new ObjType(19, "Group Box");
/*  71:116 */   public static final ObjType COMBOBOX = new ObjType(20, "Combo Box");
/*  72:117 */   public static final ObjType MSOFFICEDRAWING = new ObjType(
/*  73:118 */     30, "MS Office Drawing");
/*  74:120 */   public static final ObjType FORMCONTROL = new ObjType(20, "Form Combo Box");
/*  75:122 */   public static final ObjType EXCELNOTE = new ObjType(25, "Excel Note");
/*  76:124 */   public static final ObjType UNKNOWN = new ObjType(255, "Unknown");
/*  77:    */   private static final int COMMON_DATA_LENGTH = 22;
/*  78:    */   private static final int CLIPBOARD_FORMAT_LENGTH = 6;
/*  79:    */   private static final int PICTURE_OPTION_LENGTH = 6;
/*  80:    */   private static final int NOTE_STRUCTURE_LENGTH = 26;
/*  81:    */   private static final int COMBOBOX_STRUCTURE_LENGTH = 44;
/*  82:    */   private static final int END_LENGTH = 4;
/*  83:    */   
/*  84:    */   public ObjRecord(Record t)
/*  85:    */   {
/*  86:141 */     super(t);
/*  87:142 */     byte[] data = t.getData();
/*  88:143 */     int objtype = IntegerHelper.getInt(data[4], data[5]);
/*  89:144 */     this.read = true;
/*  90:145 */     this.type = ObjType.getType(objtype);
/*  91:147 */     if (this.type == UNKNOWN) {
/*  92:149 */       logger.warn("unknown object type code " + objtype);
/*  93:    */     }
/*  94:152 */     this.objectId = IntegerHelper.getInt(data[6], data[7]);
/*  95:    */   }
/*  96:    */   
/*  97:    */   ObjRecord(int objId, ObjType t)
/*  98:    */   {
/*  99:163 */     super(Type.OBJ);
/* 100:164 */     this.objectId = objId;
/* 101:165 */     this.type = t;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public byte[] getData()
/* 105:    */   {
/* 106:175 */     if (this.read) {
/* 107:177 */       return getRecord().getData();
/* 108:    */     }
/* 109:180 */     if ((this.type == PICTURE) || (this.type == CHART)) {
/* 110:182 */       return getPictureData();
/* 111:    */     }
/* 112:184 */     if (this.type == EXCELNOTE) {
/* 113:186 */       return getNoteData();
/* 114:    */     }
/* 115:188 */     if (this.type == COMBOBOX) {
/* 116:190 */       return getComboBoxData();
/* 117:    */     }
/* 118:194 */     Assert.verify(false);
/* 119:    */     
/* 120:196 */     return null;
/* 121:    */   }
/* 122:    */   
/* 123:    */   private byte[] getPictureData()
/* 124:    */   {
/* 125:206 */     int dataLength = 38;
/* 126:    */     
/* 127:    */ 
/* 128:    */ 
/* 129:210 */     int pos = 0;
/* 130:211 */     byte[] data = new byte[dataLength];
/* 131:    */     
/* 132:    */ 
/* 133:    */ 
/* 134:215 */     IntegerHelper.getTwoBytes(21, data, pos);
/* 135:    */     
/* 136:    */ 
/* 137:218 */     IntegerHelper.getTwoBytes(18, data, pos + 2);
/* 138:    */     
/* 139:    */ 
/* 140:221 */     IntegerHelper.getTwoBytes(this.type.value, data, pos + 4);
/* 141:    */     
/* 142:    */ 
/* 143:224 */     IntegerHelper.getTwoBytes(this.objectId, data, pos + 6);
/* 144:    */     
/* 145:    */ 
/* 146:227 */     IntegerHelper.getTwoBytes(24593, data, pos + 8);
/* 147:228 */     pos += 22;
/* 148:    */     
/* 149:    */ 
/* 150:    */ 
/* 151:232 */     IntegerHelper.getTwoBytes(7, data, pos);
/* 152:    */     
/* 153:    */ 
/* 154:235 */     IntegerHelper.getTwoBytes(2, data, pos + 2);
/* 155:    */     
/* 156:    */ 
/* 157:238 */     IntegerHelper.getTwoBytes(65535, data, pos + 4);
/* 158:239 */     pos += 6;
/* 159:    */     
/* 160:    */ 
/* 161:    */ 
/* 162:243 */     IntegerHelper.getTwoBytes(8, data, pos);
/* 163:    */     
/* 164:    */ 
/* 165:246 */     IntegerHelper.getTwoBytes(2, data, pos + 2);
/* 166:    */     
/* 167:    */ 
/* 168:249 */     IntegerHelper.getTwoBytes(1, data, pos + 4);
/* 169:250 */     pos += 6;
/* 170:    */     
/* 171:    */ 
/* 172:253 */     IntegerHelper.getTwoBytes(0, data, pos);
/* 173:    */     
/* 174:    */ 
/* 175:256 */     IntegerHelper.getTwoBytes(0, data, pos + 2);
/* 176:    */     
/* 177:    */ 
/* 178:259 */     pos += 4;
/* 179:    */     
/* 180:261 */     return data;
/* 181:    */   }
/* 182:    */   
/* 183:    */   private byte[] getNoteData()
/* 184:    */   {
/* 185:271 */     int dataLength = 52;
/* 186:    */     
/* 187:    */ 
/* 188:274 */     int pos = 0;
/* 189:275 */     byte[] data = new byte[dataLength];
/* 190:    */     
/* 191:    */ 
/* 192:    */ 
/* 193:279 */     IntegerHelper.getTwoBytes(21, data, pos);
/* 194:    */     
/* 195:    */ 
/* 196:282 */     IntegerHelper.getTwoBytes(18, data, pos + 2);
/* 197:    */     
/* 198:    */ 
/* 199:285 */     IntegerHelper.getTwoBytes(this.type.value, data, pos + 4);
/* 200:    */     
/* 201:    */ 
/* 202:288 */     IntegerHelper.getTwoBytes(this.objectId, data, pos + 6);
/* 203:    */     
/* 204:    */ 
/* 205:291 */     IntegerHelper.getTwoBytes(16401, data, pos + 8);
/* 206:292 */     pos += 22;
/* 207:    */     
/* 208:    */ 
/* 209:    */ 
/* 210:296 */     IntegerHelper.getTwoBytes(13, data, pos);
/* 211:    */     
/* 212:    */ 
/* 213:299 */     IntegerHelper.getTwoBytes(22, data, pos + 2);
/* 214:    */     
/* 215:    */ 
/* 216:302 */     pos += 26;
/* 217:    */     
/* 218:    */ 
/* 219:    */ 
/* 220:306 */     IntegerHelper.getTwoBytes(0, data, pos);
/* 221:    */     
/* 222:    */ 
/* 223:309 */     IntegerHelper.getTwoBytes(0, data, pos + 2);
/* 224:    */     
/* 225:    */ 
/* 226:312 */     pos += 4;
/* 227:    */     
/* 228:314 */     return data;
/* 229:    */   }
/* 230:    */   
/* 231:    */   private byte[] getComboBoxData()
/* 232:    */   {
/* 233:324 */     int dataLength = 70;
/* 234:    */     
/* 235:    */ 
/* 236:327 */     int pos = 0;
/* 237:328 */     byte[] data = new byte[dataLength];
/* 238:    */     
/* 239:    */ 
/* 240:    */ 
/* 241:332 */     IntegerHelper.getTwoBytes(21, data, pos);
/* 242:    */     
/* 243:    */ 
/* 244:335 */     IntegerHelper.getTwoBytes(18, data, pos + 2);
/* 245:    */     
/* 246:    */ 
/* 247:338 */     IntegerHelper.getTwoBytes(this.type.value, data, pos + 4);
/* 248:    */     
/* 249:    */ 
/* 250:341 */     IntegerHelper.getTwoBytes(this.objectId, data, pos + 6);
/* 251:    */     
/* 252:    */ 
/* 253:344 */     IntegerHelper.getTwoBytes(0, data, pos + 8);
/* 254:345 */     pos += 22;
/* 255:    */     
/* 256:    */ 
/* 257:    */ 
/* 258:349 */     IntegerHelper.getTwoBytes(12, data, pos);
/* 259:    */     
/* 260:    */ 
/* 261:352 */     IntegerHelper.getTwoBytes(20, data, pos + 2);
/* 262:    */     
/* 263:    */ 
/* 264:355 */     data[(pos + 14)] = 1;
/* 265:356 */     data[(pos + 16)] = 4;
/* 266:357 */     data[(pos + 20)] = 16;
/* 267:358 */     data[(pos + 24)] = 19;
/* 268:359 */     data[(pos + 26)] = -18;
/* 269:360 */     data[(pos + 27)] = 31;
/* 270:361 */     data[(pos + 30)] = 4;
/* 271:362 */     data[(pos + 34)] = 1;
/* 272:363 */     data[(pos + 35)] = 6;
/* 273:364 */     data[(pos + 38)] = 2;
/* 274:365 */     data[(pos + 40)] = 8;
/* 275:366 */     data[(pos + 42)] = 64;
/* 276:    */     
/* 277:368 */     pos += 44;
/* 278:    */     
/* 279:    */ 
/* 280:    */ 
/* 281:372 */     IntegerHelper.getTwoBytes(0, data, pos);
/* 282:    */     
/* 283:    */ 
/* 284:375 */     IntegerHelper.getTwoBytes(0, data, pos + 2);
/* 285:    */     
/* 286:    */ 
/* 287:378 */     pos += 4;
/* 288:    */     
/* 289:380 */     return data;
/* 290:    */   }
/* 291:    */   
/* 292:    */   public Record getRecord()
/* 293:    */   {
/* 294:391 */     return super.getRecord();
/* 295:    */   }
/* 296:    */   
/* 297:    */   public ObjType getType()
/* 298:    */   {
/* 299:401 */     return this.type;
/* 300:    */   }
/* 301:    */   
/* 302:    */   public int getObjectId()
/* 303:    */   {
/* 304:411 */     return this.objectId;
/* 305:    */   }
/* 306:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.drawing.ObjRecord
 * JD-Core Version:    0.7.0.1
 */