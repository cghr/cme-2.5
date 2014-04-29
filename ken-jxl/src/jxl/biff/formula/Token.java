/*   1:    */ package jxl.biff.formula;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ 
/*   5:    */ class Token
/*   6:    */ {
/*   7:    */   public final int[] value;
/*   8: 38 */   private static HashMap tokens = new HashMap(20);
/*   9:    */   
/*  10:    */   private Token(int v)
/*  11:    */   {
/*  12: 48 */     this.value = new int[] { v };
/*  13:    */     
/*  14: 50 */     tokens.put(new Integer(v), this);
/*  15:    */   }
/*  16:    */   
/*  17:    */   private Token(int v1, int v2)
/*  18:    */   {
/*  19: 61 */     this.value = new int[] { v1, v2 };
/*  20:    */     
/*  21: 63 */     tokens.put(new Integer(v1), this);
/*  22: 64 */     tokens.put(new Integer(v2), this);
/*  23:    */   }
/*  24:    */   
/*  25:    */   private Token(int v1, int v2, int v3)
/*  26:    */   {
/*  27: 75 */     this.value = new int[] { v1, v2, v3 };
/*  28:    */     
/*  29: 77 */     tokens.put(new Integer(v1), this);
/*  30: 78 */     tokens.put(new Integer(v2), this);
/*  31: 79 */     tokens.put(new Integer(v3), this);
/*  32:    */   }
/*  33:    */   
/*  34:    */   private Token(int v1, int v2, int v3, int v4)
/*  35:    */   {
/*  36: 90 */     this.value = new int[] { v1, v2, v3, v4 };
/*  37:    */     
/*  38: 92 */     tokens.put(new Integer(v1), this);
/*  39: 93 */     tokens.put(new Integer(v2), this);
/*  40: 94 */     tokens.put(new Integer(v3), this);
/*  41: 95 */     tokens.put(new Integer(v4), this);
/*  42:    */   }
/*  43:    */   
/*  44:    */   private Token(int v1, int v2, int v3, int v4, int v5)
/*  45:    */   {
/*  46:106 */     this.value = new int[] { v1, v2, v3, v4, v5 };
/*  47:    */     
/*  48:108 */     tokens.put(new Integer(v1), this);
/*  49:109 */     tokens.put(new Integer(v2), this);
/*  50:110 */     tokens.put(new Integer(v3), this);
/*  51:111 */     tokens.put(new Integer(v4), this);
/*  52:112 */     tokens.put(new Integer(v5), this);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public byte getCode()
/*  56:    */   {
/*  57:122 */     return (byte)this.value[0];
/*  58:    */   }
/*  59:    */   
/*  60:    */   public byte getReferenceCode()
/*  61:    */   {
/*  62:133 */     return (byte)this.value[0];
/*  63:    */   }
/*  64:    */   
/*  65:    */   public byte getCode2()
/*  66:    */   {
/*  67:144 */     return (byte)(this.value.length > 0 ? this.value[1] : this.value[0]);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public byte getValueCode()
/*  71:    */   {
/*  72:155 */     return (byte)(this.value.length > 0 ? this.value[1] : this.value[0]);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static Token getToken(int v)
/*  76:    */   {
/*  77:163 */     Token t = (Token)tokens.get(new Integer(v));
/*  78:    */     
/*  79:165 */     return t != null ? t : UNKNOWN;
/*  80:    */   }
/*  81:    */   
/*  82:169 */   public static final Token REF = new Token(68, 36, 100);
/*  83:170 */   public static final Token REF3D = new Token(90, 58, 122);
/*  84:171 */   public static final Token MISSING_ARG = new Token(22);
/*  85:172 */   public static final Token STRING = new Token(23);
/*  86:173 */   public static final Token ERR = new Token(28);
/*  87:174 */   public static final Token BOOL = new Token(29);
/*  88:175 */   public static final Token INTEGER = new Token(30);
/*  89:176 */   public static final Token DOUBLE = new Token(31);
/*  90:177 */   public static final Token REFERR = new Token(42, 74, 106);
/*  91:178 */   public static final Token REFV = new Token(44, 76, 108);
/*  92:179 */   public static final Token AREAV = new Token(45, 77, 109);
/*  93:180 */   public static final Token MEM_AREA = new Token(38, 70, 102);
/*  94:181 */   public static final Token AREA = new Token(37, 101, 69);
/*  95:182 */   public static final Token NAMED_RANGE = new Token(35, 67, 99);
/*  96:184 */   public static final Token NAME = new Token(57, 89);
/*  97:185 */   public static final Token AREA3D = new Token(59, 91);
/*  98:188 */   public static final Token UNARY_PLUS = new Token(18);
/*  99:189 */   public static final Token UNARY_MINUS = new Token(19);
/* 100:190 */   public static final Token PERCENT = new Token(20);
/* 101:191 */   public static final Token PARENTHESIS = new Token(21);
/* 102:194 */   public static final Token ADD = new Token(3);
/* 103:195 */   public static final Token SUBTRACT = new Token(4);
/* 104:196 */   public static final Token MULTIPLY = new Token(5);
/* 105:197 */   public static final Token DIVIDE = new Token(6);
/* 106:198 */   public static final Token POWER = new Token(7);
/* 107:199 */   public static final Token CONCAT = new Token(8);
/* 108:200 */   public static final Token LESS_THAN = new Token(9);
/* 109:201 */   public static final Token LESS_EQUAL = new Token(10);
/* 110:202 */   public static final Token EQUAL = new Token(11);
/* 111:203 */   public static final Token GREATER_EQUAL = new Token(12);
/* 112:204 */   public static final Token GREATER_THAN = new Token(13);
/* 113:205 */   public static final Token NOT_EQUAL = new Token(14);
/* 114:206 */   public static final Token UNION = new Token(16);
/* 115:207 */   public static final Token RANGE = new Token(17);
/* 116:210 */   public static final Token FUNCTION = new Token(65, 33, 97);
/* 117:211 */   public static final Token FUNCTIONVARARG = new Token(66, 34, 98);
/* 118:214 */   public static final Token ATTRIBUTE = new Token(25);
/* 119:215 */   public static final Token MEM_FUNC = new Token(41, 73, 105);
/* 120:218 */   public static final Token UNKNOWN = new Token(65535);
/* 121:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.biff.formula.Token
 * JD-Core Version:    0.7.0.1
 */