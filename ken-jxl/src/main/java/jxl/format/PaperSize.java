/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public final class PaperSize
/*   4:    */ {
/*   5:    */   private static final int LAST_PAPER_SIZE = 89;
/*   6:    */   private int val;
/*   7: 37 */   private static PaperSize[] paperSizes = new PaperSize[90];
/*   8:    */   
/*   9:    */   private PaperSize(int v, boolean growArray)
/*  10:    */   {
/*  11: 44 */     this.val = v;
/*  12: 46 */     if ((v >= paperSizes.length) && (growArray))
/*  13:    */     {
/*  14: 49 */       PaperSize[] newarray = new PaperSize[v + 1];
/*  15: 50 */       System.arraycopy(paperSizes, 0, newarray, 0, paperSizes.length);
/*  16: 51 */       paperSizes = newarray;
/*  17:    */     }
/*  18: 53 */     if (v < paperSizes.length) {
/*  19: 55 */       paperSizes[v] = this;
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   private PaperSize(int v)
/*  24:    */   {
/*  25: 64 */     this(v, true);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public int getValue()
/*  29:    */   {
/*  30: 74 */     return this.val;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public static PaperSize getPaperSize(int val)
/*  34:    */   {
/*  35: 85 */     PaperSize p = val > paperSizes.length - 1 ? null : paperSizes[val];
/*  36: 86 */     return p == null ? new PaperSize(val, false) : p;
/*  37:    */   }
/*  38:    */   
/*  39: 90 */   public static final PaperSize UNDEFINED = new PaperSize(0);
/*  40: 93 */   public static final PaperSize LETTER = new PaperSize(1);
/*  41: 96 */   public static final PaperSize LETTER_SMALL = new PaperSize(2);
/*  42: 99 */   public static final PaperSize TABLOID = new PaperSize(3);
/*  43:102 */   public static final PaperSize LEDGER = new PaperSize(4);
/*  44:105 */   public static final PaperSize LEGAL = new PaperSize(5);
/*  45:108 */   public static final PaperSize STATEMENT = new PaperSize(6);
/*  46:111 */   public static final PaperSize EXECUTIVE = new PaperSize(7);
/*  47:114 */   public static final PaperSize A3 = new PaperSize(8);
/*  48:117 */   public static final PaperSize A4 = new PaperSize(9);
/*  49:120 */   public static final PaperSize A4_SMALL = new PaperSize(10);
/*  50:123 */   public static final PaperSize A5 = new PaperSize(11);
/*  51:126 */   public static final PaperSize B4 = new PaperSize(12);
/*  52:129 */   public static final PaperSize B5 = new PaperSize(13);
/*  53:132 */   public static final PaperSize FOLIO = new PaperSize(14);
/*  54:135 */   public static final PaperSize QUARTO = new PaperSize(15);
/*  55:138 */   public static final PaperSize SIZE_10x14 = new PaperSize(16);
/*  56:141 */   public static final PaperSize SIZE_10x17 = new PaperSize(17);
/*  57:144 */   public static final PaperSize NOTE = new PaperSize(18);
/*  58:147 */   public static final PaperSize ENVELOPE_9 = new PaperSize(19);
/*  59:150 */   public static final PaperSize ENVELOPE_10 = new PaperSize(20);
/*  60:153 */   public static final PaperSize ENVELOPE_11 = new PaperSize(21);
/*  61:156 */   public static final PaperSize ENVELOPE_12 = new PaperSize(22);
/*  62:159 */   public static final PaperSize ENVELOPE_14 = new PaperSize(23);
/*  63:162 */   public static final PaperSize C = new PaperSize(24);
/*  64:165 */   public static final PaperSize D = new PaperSize(25);
/*  65:168 */   public static final PaperSize E = new PaperSize(26);
/*  66:171 */   public static final PaperSize ENVELOPE_DL = new PaperSize(27);
/*  67:174 */   public static final PaperSize ENVELOPE_C5 = new PaperSize(28);
/*  68:177 */   public static final PaperSize ENVELOPE_C3 = new PaperSize(29);
/*  69:180 */   public static final PaperSize ENVELOPE_C4 = new PaperSize(30);
/*  70:183 */   public static final PaperSize ENVELOPE_C6 = new PaperSize(31);
/*  71:186 */   public static final PaperSize ENVELOPE_C6_C5 = new PaperSize(32);
/*  72:189 */   public static final PaperSize B4_ISO = new PaperSize(33);
/*  73:192 */   public static final PaperSize B5_ISO = new PaperSize(34);
/*  74:195 */   public static final PaperSize B6_ISO = new PaperSize(35);
/*  75:198 */   public static final PaperSize ENVELOPE_ITALY = new PaperSize(36);
/*  76:201 */   public static final PaperSize ENVELOPE_MONARCH = new PaperSize(37);
/*  77:204 */   public static final PaperSize ENVELOPE_6_75 = new PaperSize(38);
/*  78:207 */   public static final PaperSize US_FANFOLD = new PaperSize(39);
/*  79:210 */   public static final PaperSize GERMAN_FANFOLD = new PaperSize(40);
/*  80:213 */   public static final PaperSize GERMAN_LEGAL_FANFOLD = new PaperSize(41);
/*  81:216 */   public static final PaperSize B4_ISO_2 = new PaperSize(42);
/*  82:219 */   public static final PaperSize JAPANESE_POSTCARD = new PaperSize(43);
/*  83:222 */   public static final PaperSize SIZE_9x11 = new PaperSize(44);
/*  84:225 */   public static final PaperSize SIZE_10x11 = new PaperSize(45);
/*  85:228 */   public static final PaperSize SIZE_15x11 = new PaperSize(46);
/*  86:231 */   public static final PaperSize ENVELOPE_INVITE = new PaperSize(47);
/*  87:236 */   public static final PaperSize LETTER_EXTRA = new PaperSize(50);
/*  88:239 */   public static final PaperSize LEGAL_EXTRA = new PaperSize(51);
/*  89:242 */   public static final PaperSize TABLOID_EXTRA = new PaperSize(52);
/*  90:245 */   public static final PaperSize A4_EXTRA = new PaperSize(53);
/*  91:248 */   public static final PaperSize LETTER_TRANSVERSE = new PaperSize(54);
/*  92:251 */   public static final PaperSize A4_TRANSVERSE = new PaperSize(55);
/*  93:254 */   public static final PaperSize LETTER_EXTRA_TRANSVERSE = new PaperSize(56);
/*  94:257 */   public static final PaperSize SUPER_A_A4 = new PaperSize(57);
/*  95:260 */   public static final PaperSize SUPER_B_A3 = new PaperSize(58);
/*  96:263 */   public static final PaperSize LETTER_PLUS = new PaperSize(59);
/*  97:266 */   public static final PaperSize A4_PLUS = new PaperSize(60);
/*  98:269 */   public static final PaperSize A5_TRANSVERSE = new PaperSize(61);
/*  99:272 */   public static final PaperSize B5_TRANSVERSE = new PaperSize(62);
/* 100:275 */   public static final PaperSize A3_EXTRA = new PaperSize(63);
/* 101:278 */   public static final PaperSize A5_EXTRA = new PaperSize(64);
/* 102:281 */   public static final PaperSize B5_EXTRA = new PaperSize(65);
/* 103:284 */   public static final PaperSize A2 = new PaperSize(66);
/* 104:287 */   public static final PaperSize A3_TRANSVERSE = new PaperSize(67);
/* 105:290 */   public static final PaperSize A3_EXTRA_TRANSVERSE = new PaperSize(68);
/* 106:293 */   public static final PaperSize DOUBLE_JAPANESE_POSTCARD = new PaperSize(69);
/* 107:296 */   public static final PaperSize A6 = new PaperSize(70);
/* 108:301 */   public static final PaperSize LETTER_ROTATED = new PaperSize(75);
/* 109:304 */   public static final PaperSize A3_ROTATED = new PaperSize(76);
/* 110:307 */   public static final PaperSize A4_ROTATED = new PaperSize(77);
/* 111:310 */   public static final PaperSize A5_ROTATED = new PaperSize(78);
/* 112:313 */   public static final PaperSize B4_ROTATED = new PaperSize(79);
/* 113:316 */   public static final PaperSize B5_ROTATED = new PaperSize(80);
/* 114:319 */   public static final PaperSize JAPANESE_POSTCARD_ROTATED = new PaperSize(81);
/* 115:322 */   public static final PaperSize DOUBLE_JAPANESE_POSTCARD_ROTATED = new PaperSize(82);
/* 116:325 */   public static final PaperSize A6_ROTATED = new PaperSize(83);
/* 117:330 */   public static final PaperSize B6 = new PaperSize(88);
/* 118:333 */   public static final PaperSize B6_ROTATED = new PaperSize(89);
/* 119:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.PaperSize
 * JD-Core Version:    0.7.0.1
 */