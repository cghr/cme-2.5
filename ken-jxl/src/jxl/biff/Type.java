/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ public final class Type
/*   4:    */ {
/*   5:    */   public final int value;
/*   6: 34 */   private static Type[] types = new Type[0];
/*   7:    */   
/*   8:    */   private Type(int v)
/*   9:    */   {
/*  10: 44 */     this.value = v;
/*  11:    */     
/*  12:    */ 
/*  13: 47 */     Type[] newTypes = new Type[types.length + 1];
/*  14: 48 */     System.arraycopy(types, 0, newTypes, 0, types.length);
/*  15: 49 */     newTypes[types.length] = this;
/*  16: 50 */     types = newTypes;
/*  17:    */   }
/*  18:    */   
/*  19: 54 */   private static ArbitraryType arbitrary = new ArbitraryType(null);
/*  20:    */   
/*  21:    */   private Type(int v, ArbitraryType arb)
/*  22:    */   {
/*  23: 61 */     this.value = v;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public int hashCode()
/*  27:    */   {
/*  28: 70 */     return this.value;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public boolean equals(Object o)
/*  32:    */   {
/*  33: 80 */     if (o == this) {
/*  34: 82 */       return true;
/*  35:    */     }
/*  36: 85 */     if (!(o instanceof Type)) {
/*  37: 87 */       return false;
/*  38:    */     }
/*  39: 90 */     Type t = (Type)o;
/*  40:    */     
/*  41: 92 */     return this.value == t.value;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static Type getType(int v)
/*  45:    */   {
/*  46:102 */     for (int i = 0; i < types.length; i++) {
/*  47:104 */       if (types[i].value == v) {
/*  48:106 */         return types[i];
/*  49:    */       }
/*  50:    */     }
/*  51:110 */     return UNKNOWN;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static Type createType(int v)
/*  55:    */   {
/*  56:120 */     return new Type(v, arbitrary);
/*  57:    */   }
/*  58:    */   
/*  59:125 */   public static final Type BOF = new Type(2057);
/*  60:128 */   public static final Type EOF = new Type(10);
/*  61:131 */   public static final Type BOUNDSHEET = new Type(133);
/*  62:134 */   public static final Type SUPBOOK = new Type(430);
/*  63:137 */   public static final Type EXTERNSHEET = new Type(23);
/*  64:140 */   public static final Type DIMENSION = new Type(512);
/*  65:143 */   public static final Type BLANK = new Type(513);
/*  66:146 */   public static final Type MULBLANK = new Type(190);
/*  67:149 */   public static final Type ROW = new Type(520);
/*  68:152 */   public static final Type NOTE = new Type(28);
/*  69:155 */   public static final Type TXO = new Type(438);
/*  70:158 */   public static final Type RK = new Type(126);
/*  71:161 */   public static final Type RK2 = new Type(638);
/*  72:164 */   public static final Type MULRK = new Type(189);
/*  73:167 */   public static final Type INDEX = new Type(523);
/*  74:170 */   public static final Type DBCELL = new Type(215);
/*  75:173 */   public static final Type SST = new Type(252);
/*  76:176 */   public static final Type COLINFO = new Type(125);
/*  77:179 */   public static final Type EXTSST = new Type(255);
/*  78:182 */   public static final Type CONTINUE = new Type(60);
/*  79:185 */   public static final Type LABEL = new Type(516);
/*  80:188 */   public static final Type RSTRING = new Type(214);
/*  81:191 */   public static final Type LABELSST = new Type(253);
/*  82:194 */   public static final Type NUMBER = new Type(515);
/*  83:197 */   public static final Type NAME = new Type(24);
/*  84:200 */   public static final Type TABID = new Type(317);
/*  85:203 */   public static final Type ARRAY = new Type(545);
/*  86:206 */   public static final Type STRING = new Type(519);
/*  87:209 */   public static final Type FORMULA = new Type(1030);
/*  88:212 */   public static final Type FORMULA2 = new Type(6);
/*  89:215 */   public static final Type SHAREDFORMULA = new Type(1212);
/*  90:218 */   public static final Type FORMAT = new Type(1054);
/*  91:221 */   public static final Type XF = new Type(224);
/*  92:224 */   public static final Type BOOLERR = new Type(517);
/*  93:227 */   public static final Type INTERFACEHDR = new Type(225);
/*  94:230 */   public static final Type SAVERECALC = new Type(95);
/*  95:233 */   public static final Type INTERFACEEND = new Type(226);
/*  96:236 */   public static final Type XCT = new Type(89);
/*  97:239 */   public static final Type CRN = new Type(90);
/*  98:242 */   public static final Type DEFCOLWIDTH = new Type(85);
/*  99:245 */   public static final Type DEFAULTROWHEIGHT = new Type(549);
/* 100:248 */   public static final Type WRITEACCESS = new Type(92);
/* 101:251 */   public static final Type WSBOOL = new Type(129);
/* 102:254 */   public static final Type CODEPAGE = new Type(66);
/* 103:257 */   public static final Type DSF = new Type(353);
/* 104:260 */   public static final Type FNGROUPCOUNT = new Type(156);
/* 105:263 */   public static final Type FILTERMODE = new Type(155);
/* 106:266 */   public static final Type AUTOFILTERINFO = new Type(157);
/* 107:269 */   public static final Type AUTOFILTER = new Type(158);
/* 108:272 */   public static final Type COUNTRY = new Type(140);
/* 109:275 */   public static final Type PROTECT = new Type(18);
/* 110:278 */   public static final Type SCENPROTECT = new Type(221);
/* 111:281 */   public static final Type OBJPROTECT = new Type(99);
/* 112:284 */   public static final Type PRINTHEADERS = new Type(42);
/* 113:287 */   public static final Type HEADER = new Type(20);
/* 114:290 */   public static final Type FOOTER = new Type(21);
/* 115:293 */   public static final Type HCENTER = new Type(131);
/* 116:296 */   public static final Type VCENTER = new Type(132);
/* 117:299 */   public static final Type FILEPASS = new Type(47);
/* 118:302 */   public static final Type SETUP = new Type(161);
/* 119:305 */   public static final Type PRINTGRIDLINES = new Type(43);
/* 120:308 */   public static final Type GRIDSET = new Type(130);
/* 121:311 */   public static final Type GUTS = new Type(128);
/* 122:314 */   public static final Type WINDOWPROTECT = new Type(25);
/* 123:317 */   public static final Type PROT4REV = new Type(431);
/* 124:320 */   public static final Type PROT4REVPASS = new Type(444);
/* 125:323 */   public static final Type PASSWORD = new Type(19);
/* 126:326 */   public static final Type REFRESHALL = new Type(439);
/* 127:329 */   public static final Type WINDOW1 = new Type(61);
/* 128:332 */   public static final Type WINDOW2 = new Type(574);
/* 129:335 */   public static final Type BACKUP = new Type(64);
/* 130:338 */   public static final Type HIDEOBJ = new Type(141);
/* 131:341 */   public static final Type NINETEENFOUR = new Type(34);
/* 132:344 */   public static final Type PRECISION = new Type(14);
/* 133:347 */   public static final Type BOOKBOOL = new Type(218);
/* 134:350 */   public static final Type FONT = new Type(49);
/* 135:353 */   public static final Type MMS = new Type(193);
/* 136:356 */   public static final Type CALCMODE = new Type(13);
/* 137:359 */   public static final Type CALCCOUNT = new Type(12);
/* 138:362 */   public static final Type REFMODE = new Type(15);
/* 139:365 */   public static final Type TEMPLATE = new Type(96);
/* 140:368 */   public static final Type OBJPROJ = new Type(211);
/* 141:371 */   public static final Type DELTA = new Type(16);
/* 142:374 */   public static final Type MERGEDCELLS = new Type(229);
/* 143:377 */   public static final Type ITERATION = new Type(17);
/* 144:380 */   public static final Type STYLE = new Type(659);
/* 145:383 */   public static final Type USESELFS = new Type(352);
/* 146:386 */   public static final Type VERTICALPAGEBREAKS = new Type(26);
/* 147:389 */   public static final Type HORIZONTALPAGEBREAKS = new Type(27);
/* 148:392 */   public static final Type SELECTION = new Type(29);
/* 149:395 */   public static final Type HLINK = new Type(440);
/* 150:398 */   public static final Type OBJ = new Type(93);
/* 151:401 */   public static final Type MSODRAWING = new Type(236);
/* 152:404 */   public static final Type MSODRAWINGGROUP = new Type(235);
/* 153:407 */   public static final Type LEFTMARGIN = new Type(38);
/* 154:410 */   public static final Type RIGHTMARGIN = new Type(39);
/* 155:413 */   public static final Type TOPMARGIN = new Type(40);
/* 156:416 */   public static final Type BOTTOMMARGIN = new Type(41);
/* 157:419 */   public static final Type EXTERNNAME = new Type(35);
/* 158:422 */   public static final Type PALETTE = new Type(146);
/* 159:425 */   public static final Type PLS = new Type(77);
/* 160:428 */   public static final Type SCL = new Type(160);
/* 161:431 */   public static final Type PANE = new Type(65);
/* 162:434 */   public static final Type WEIRD1 = new Type(239);
/* 163:437 */   public static final Type SORT = new Type(144);
/* 164:440 */   public static final Type CONDFMT = new Type(432);
/* 165:443 */   public static final Type CF = new Type(433);
/* 166:446 */   public static final Type DV = new Type(446);
/* 167:449 */   public static final Type DVAL = new Type(434);
/* 168:452 */   public static final Type BUTTONPROPERTYSET = new Type(442);
/* 169:456 */   public static final Type EXCEL9FILE = new Type(448);
/* 170:461 */   public static final Type FONTX = new Type(4134);
/* 171:464 */   public static final Type IFMT = new Type(4174);
/* 172:467 */   public static final Type FBI = new Type(4192);
/* 173:470 */   public static final Type ALRUNS = new Type(4176);
/* 174:473 */   public static final Type SERIES = new Type(4099);
/* 175:476 */   public static final Type SERIESLIST = new Type(4118);
/* 176:479 */   public static final Type SBASEREF = new Type(4168);
/* 177:482 */   public static final Type UNKNOWN = new Type(65535);
/* 178:490 */   public static final Type U1C0 = new Type(448);
/* 179:491 */   public static final Type U1C1 = new Type(449);
/* 180:    */   
/* 181:    */   private static class ArbitraryType {}
/* 182:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.Type
 * JD-Core Version:    0.7.0.1
 */