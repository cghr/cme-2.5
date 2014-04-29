/*   1:    */ package jxl.biff;
/*   2:    */ 
/*   3:    */ import jxl.common.Logger;
/*   4:    */ 
/*   5:    */ public class CountryCode
/*   6:    */ {
/*   7: 32 */   private static Logger logger = Logger.getLogger(CountryCode.class);
/*   8:    */   private int value;
/*   9:    */   private String code;
/*  10:    */   private String description;
/*  11: 52 */   private static CountryCode[] codes = new CountryCode[0];
/*  12:    */   
/*  13:    */   private CountryCode(int v, String c, String d)
/*  14:    */   {
/*  15: 59 */     this.value = v;
/*  16: 60 */     this.code = c;
/*  17: 61 */     this.description = d;
/*  18:    */     
/*  19: 63 */     CountryCode[] newcodes = new CountryCode[codes.length + 1];
/*  20: 64 */     System.arraycopy(codes, 0, newcodes, 0, codes.length);
/*  21: 65 */     newcodes[codes.length] = this;
/*  22: 66 */     codes = newcodes;
/*  23:    */   }
/*  24:    */   
/*  25:    */   private CountryCode(int v)
/*  26:    */   {
/*  27: 75 */     this.value = v;
/*  28: 76 */     this.description = "Arbitrary";
/*  29: 77 */     this.code = "??";
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getValue()
/*  33:    */   {
/*  34: 87 */     return this.value;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String getCode()
/*  38:    */   {
/*  39: 97 */     return this.code;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static CountryCode getCountryCode(String s)
/*  43:    */   {
/*  44:105 */     if ((s == null) || (s.length() != 2))
/*  45:    */     {
/*  46:107 */       logger.warn("Please specify two character ISO 3166 country code");
/*  47:108 */       return USA;
/*  48:    */     }
/*  49:111 */     CountryCode code = UNKNOWN;
/*  50:112 */     for (int i = 0; (i < codes.length) && (code == UNKNOWN); i++) {
/*  51:114 */       if (codes[i].code.equals(s)) {
/*  52:116 */         code = codes[i];
/*  53:    */       }
/*  54:    */     }
/*  55:120 */     return code;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static CountryCode createArbitraryCode(int i)
/*  59:    */   {
/*  60:130 */     return new CountryCode(i);
/*  61:    */   }
/*  62:    */   
/*  63:134 */   public static final CountryCode USA = new CountryCode(1, "US", "USA");
/*  64:136 */   public static final CountryCode CANADA = new CountryCode(2, "CA", "Canada");
/*  65:138 */   public static final CountryCode GREECE = new CountryCode(30, "GR", "Greece");
/*  66:140 */   public static final CountryCode NETHERLANDS = new CountryCode(31, "NE", "Netherlands");
/*  67:142 */   public static final CountryCode BELGIUM = new CountryCode(32, "BE", "Belgium");
/*  68:144 */   public static final CountryCode FRANCE = new CountryCode(33, "FR", "France");
/*  69:145 */   public static final CountryCode SPAIN = new CountryCode(34, "ES", "Spain");
/*  70:146 */   public static final CountryCode ITALY = new CountryCode(39, "IT", "Italy");
/*  71:148 */   public static final CountryCode SWITZERLAND = new CountryCode(41, "CH", "Switzerland");
/*  72:150 */   public static final CountryCode UK = new CountryCode(44, "UK", "United Kingdowm");
/*  73:152 */   public static final CountryCode DENMARK = new CountryCode(45, "DK", "Denmark");
/*  74:154 */   public static final CountryCode SWEDEN = new CountryCode(46, "SE", "Sweden");
/*  75:156 */   public static final CountryCode NORWAY = new CountryCode(47, "NO", "Norway");
/*  76:158 */   public static final CountryCode GERMANY = new CountryCode(49, "DE", "Germany");
/*  77:160 */   public static final CountryCode PHILIPPINES = new CountryCode(63, "PH", "Philippines");
/*  78:162 */   public static final CountryCode CHINA = new CountryCode(86, "CN", "China");
/*  79:164 */   public static final CountryCode INDIA = new CountryCode(91, "IN", "India");
/*  80:166 */   public static final CountryCode UNKNOWN = new CountryCode(65535, "??", "Unknown");
/*  81:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.CountryCode
 * JD-Core Version:    0.7.0.1
 */