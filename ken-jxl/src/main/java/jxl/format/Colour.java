/*   1:    */ package jxl.format;
/*   2:    */ 
/*   3:    */ public class Colour
/*   4:    */ {
/*   5:    */   private int value;
/*   6:    */   private RGB rgb;
/*   7:    */   private String string;
/*   8: 48 */   private static Colour[] colours = new Colour[0];
/*   9:    */   
/*  10:    */   public Colour(int val, String s, int r, int g, int b)
/*  11:    */   {
/*  12: 61 */     this.value = val;
/*  13: 62 */     this.string = s;
/*  14: 63 */     this.rgb = new RGB(r, g, b);
/*  15:    */     
/*  16: 65 */     Colour[] oldcols = colours;
/*  17: 66 */     colours = new Colour[oldcols.length + 1];
/*  18: 67 */     System.arraycopy(oldcols, 0, colours, 0, oldcols.length);
/*  19: 68 */     colours[oldcols.length] = this;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int getValue()
/*  23:    */   {
/*  24: 79 */     return this.value;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public String getDescription()
/*  28:    */   {
/*  29: 89 */     return this.string;
/*  30:    */   }
/*  31:    */   
/*  32:    */   /**
/*  33:    */    * @deprecated
/*  34:    */    */
/*  35:    */   public int getDefaultRed()
/*  36:    */   {
/*  37:101 */     return this.rgb.getRed();
/*  38:    */   }
/*  39:    */   
/*  40:    */   /**
/*  41:    */    * @deprecated
/*  42:    */    */
/*  43:    */   public int getDefaultGreen()
/*  44:    */   {
/*  45:113 */     return this.rgb.getGreen();
/*  46:    */   }
/*  47:    */   
/*  48:    */   /**
/*  49:    */    * @deprecated
/*  50:    */    */
/*  51:    */   public int getDefaultBlue()
/*  52:    */   {
/*  53:125 */     return this.rgb.getBlue();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public RGB getDefaultRGB()
/*  57:    */   {
/*  58:135 */     return this.rgb;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static Colour getInternalColour(int val)
/*  62:    */   {
/*  63:146 */     for (int i = 0; i < colours.length; i++) {
/*  64:148 */       if (colours[i].getValue() == val) {
/*  65:150 */         return colours[i];
/*  66:    */       }
/*  67:    */     }
/*  68:154 */     return UNKNOWN;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public static Colour[] getAllColours()
/*  72:    */   {
/*  73:164 */     return colours;
/*  74:    */   }
/*  75:    */   
/*  76:169 */   public static final Colour UNKNOWN = new Colour(32750, "unknown", 0, 0, 0);
/*  77:171 */   public static final Colour BLACK = new Colour(32767, "black", 0, 0, 0);
/*  78:173 */   public static final Colour WHITE = new Colour(9, "white", 255, 255, 255);
/*  79:175 */   public static final Colour DEFAULT_BACKGROUND1 = new Colour(0, "default background", 255, 255, 255);
/*  80:177 */   public static final Colour DEFAULT_BACKGROUND = new Colour(192, "default background", 255, 255, 255);
/*  81:179 */   public static final Colour PALETTE_BLACK = new Colour(8, "black", 1, 0, 0);
/*  82:183 */   public static final Colour RED = new Colour(10, "red", 255, 0, 0);
/*  83:184 */   public static final Colour BRIGHT_GREEN = new Colour(11, "bright green", 0, 255, 0);
/*  84:185 */   public static final Colour BLUE = new Colour(12, "blue", 0, 0, 255);
/*  85:185 */   public static final Colour YELLOW = new Colour(13, "yellow", 255, 255, 0);
/*  86:186 */   public static final Colour PINK = new Colour(14, "pink", 255, 0, 255);
/*  87:187 */   public static final Colour TURQUOISE = new Colour(15, "turquoise", 0, 255, 255);
/*  88:188 */   public static final Colour DARK_RED = new Colour(16, "dark red", 128, 0, 0);
/*  89:189 */   public static final Colour GREEN = new Colour(17, "green", 0, 128, 0);
/*  90:190 */   public static final Colour DARK_BLUE = new Colour(18, "dark blue", 0, 0, 128);
/*  91:191 */   public static final Colour DARK_YELLOW = new Colour(19, "dark yellow", 128, 128, 0);
/*  92:192 */   public static final Colour VIOLET = new Colour(20, "violet", 128, 128, 0);
/*  93:193 */   public static final Colour TEAL = new Colour(21, "teal", 0, 128, 128);
/*  94:194 */   public static final Colour GREY_25_PERCENT = new Colour(22, "grey 25%", 192, 192, 192);
/*  95:195 */   public static final Colour GREY_50_PERCENT = new Colour(23, "grey 50%", 128, 128, 128);
/*  96:196 */   public static final Colour PERIWINKLE = new Colour(24, "periwinkle%", 153, 153, 255);
/*  97:197 */   public static final Colour PLUM2 = new Colour(25, "plum", 153, 51, 102);
/*  98:198 */   public static final Colour IVORY = new Colour(26, "ivory", 255, 255, 204);
/*  99:199 */   public static final Colour LIGHT_TURQUOISE2 = new Colour(27, "light turquoise", 204, 255, 255);
/* 100:200 */   public static final Colour DARK_PURPLE = new Colour(28, "dark purple", 102, 0, 102);
/* 101:201 */   public static final Colour CORAL = new Colour(29, "coral", 255, 128, 128);
/* 102:202 */   public static final Colour OCEAN_BLUE = new Colour(30, "ocean blue", 0, 102, 204);
/* 103:203 */   public static final Colour ICE_BLUE = new Colour(31, "ice blue", 204, 204, 255);
/* 104:204 */   public static final Colour DARK_BLUE2 = new Colour(32, "dark blue", 0, 0, 128);
/* 105:205 */   public static final Colour PINK2 = new Colour(33, "pink", 255, 0, 255);
/* 106:206 */   public static final Colour YELLOW2 = new Colour(34, "yellow", 255, 255, 0);
/* 107:207 */   public static final Colour TURQOISE2 = new Colour(35, "turqoise", 0, 255, 255);
/* 108:208 */   public static final Colour VIOLET2 = new Colour(36, "violet", 128, 0, 128);
/* 109:209 */   public static final Colour DARK_RED2 = new Colour(37, "dark red", 128, 0, 0);
/* 110:210 */   public static final Colour TEAL2 = new Colour(38, "teal", 0, 128, 128);
/* 111:211 */   public static final Colour BLUE2 = new Colour(39, "blue", 0, 0, 255);
/* 112:212 */   public static final Colour SKY_BLUE = new Colour(40, "sky blue", 0, 204, 255);
/* 113:214 */   public static final Colour LIGHT_TURQUOISE = new Colour(41, "light turquoise", 204, 255, 255);
/* 114:215 */   public static final Colour LIGHT_GREEN = new Colour(42, "light green", 204, 255, 204);
/* 115:217 */   public static final Colour VERY_LIGHT_YELLOW = new Colour(43, "very light yellow", 255, 255, 153);
/* 116:218 */   public static final Colour PALE_BLUE = new Colour(44, "pale blue", 153, 204, 255);
/* 117:219 */   public static final Colour ROSE = new Colour(45, "rose", 255, 153, 204);
/* 118:220 */   public static final Colour LAVENDER = new Colour(46, "lavender", 204, 153, 255);
/* 119:221 */   public static final Colour TAN = new Colour(47, "tan", 255, 204, 153);
/* 120:222 */   public static final Colour LIGHT_BLUE = new Colour(48, "light blue", 51, 102, 255);
/* 121:223 */   public static final Colour AQUA = new Colour(49, "aqua", 51, 204, 204);
/* 122:224 */   public static final Colour LIME = new Colour(50, "lime", 153, 204, 0);
/* 123:225 */   public static final Colour GOLD = new Colour(51, "gold", 255, 204, 0);
/* 124:227 */   public static final Colour LIGHT_ORANGE = new Colour(52, "light orange", 255, 153, 0);
/* 125:228 */   public static final Colour ORANGE = new Colour(53, "orange", 255, 102, 0);
/* 126:229 */   public static final Colour BLUE_GREY = new Colour(54, "blue grey", 102, 102, 204);
/* 127:230 */   public static final Colour GREY_40_PERCENT = new Colour(55, "grey 40%", 150, 150, 150);
/* 128:231 */   public static final Colour DARK_TEAL = new Colour(56, "dark teal", 0, 51, 102);
/* 129:232 */   public static final Colour SEA_GREEN = new Colour(57, "sea green", 51, 153, 102);
/* 130:233 */   public static final Colour DARK_GREEN = new Colour(58, "dark green", 0, 51, 0);
/* 131:234 */   public static final Colour OLIVE_GREEN = new Colour(59, "olive green", 51, 51, 0);
/* 132:235 */   public static final Colour BROWN = new Colour(60, "brown", 153, 51, 0);
/* 133:236 */   public static final Colour PLUM = new Colour(61, "plum", 153, 51, 102);
/* 134:237 */   public static final Colour INDIGO = new Colour(62, "indigo", 51, 51, 153);
/* 135:238 */   public static final Colour GREY_80_PERCENT = new Colour(63, "grey 80%", 51, 51, 51);
/* 136:239 */   public static final Colour AUTOMATIC = new Colour(64, "automatic", 255, 255, 255);
/* 137:242 */   public static final Colour GRAY_80 = GREY_80_PERCENT;
/* 138:243 */   public static final Colour GRAY_50 = GREY_50_PERCENT;
/* 139:244 */   public static final Colour GRAY_25 = GREY_25_PERCENT;
/* 140:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.format.Colour
 * JD-Core Version:    0.7.0.1
 */