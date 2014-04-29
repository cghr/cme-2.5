/*   1:    */ package org.springframework.util;
/*   2:    */ 
/*   3:    */ import java.math.BigDecimal;
/*   4:    */ import java.math.BigInteger;
/*   5:    */ import java.text.DecimalFormat;
/*   6:    */ import java.text.NumberFormat;
/*   7:    */ import java.text.ParseException;
/*   8:    */ 
/*   9:    */ public abstract class NumberUtils
/*  10:    */ {
/*  11:    */   public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
/*  12:    */     throws IllegalArgumentException
/*  13:    */   {
/*  14: 56 */     Assert.notNull(number, "Number must not be null");
/*  15: 57 */     Assert.notNull(targetClass, "Target class must not be null");
/*  16: 59 */     if (targetClass.isInstance(number)) {
/*  17: 60 */       return number;
/*  18:    */     }
/*  19: 62 */     if (targetClass.equals(Byte.class))
/*  20:    */     {
/*  21: 63 */       long value = number.longValue();
/*  22: 64 */       if ((value < -128L) || (value > 127L)) {
/*  23: 65 */         raiseOverflowException(number, targetClass);
/*  24:    */       }
/*  25: 67 */       return new Byte(number.byteValue());
/*  26:    */     }
/*  27: 69 */     if (targetClass.equals(Short.class))
/*  28:    */     {
/*  29: 70 */       long value = number.longValue();
/*  30: 71 */       if ((value < -32768L) || (value > 32767L)) {
/*  31: 72 */         raiseOverflowException(number, targetClass);
/*  32:    */       }
/*  33: 74 */       return new Short(number.shortValue());
/*  34:    */     }
/*  35: 76 */     if (targetClass.equals(Integer.class))
/*  36:    */     {
/*  37: 77 */       long value = number.longValue();
/*  38: 78 */       if ((value < -2147483648L) || (value > 2147483647L)) {
/*  39: 79 */         raiseOverflowException(number, targetClass);
/*  40:    */       }
/*  41: 81 */       return new Integer(number.intValue());
/*  42:    */     }
/*  43: 83 */     if (targetClass.equals(Long.class)) {
/*  44: 84 */       return new Long(number.longValue());
/*  45:    */     }
/*  46: 86 */     if (targetClass.equals(BigInteger.class))
/*  47:    */     {
/*  48: 87 */       if ((number instanceof BigDecimal)) {
/*  49: 89 */         return ((BigDecimal)number).toBigInteger();
/*  50:    */       }
/*  51: 93 */       return BigInteger.valueOf(number.longValue());
/*  52:    */     }
/*  53: 96 */     if (targetClass.equals(Float.class)) {
/*  54: 97 */       return new Float(number.floatValue());
/*  55:    */     }
/*  56: 99 */     if (targetClass.equals(Double.class)) {
/*  57:100 */       return new Double(number.doubleValue());
/*  58:    */     }
/*  59:102 */     if (targetClass.equals(BigDecimal.class)) {
/*  60:105 */       return new BigDecimal(number.toString());
/*  61:    */     }
/*  62:108 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + 
/*  63:109 */       number.getClass().getName() + "] to unknown target class [" + targetClass.getName() + "]");
/*  64:    */   }
/*  65:    */   
/*  66:    */   private static void raiseOverflowException(Number number, Class targetClass)
/*  67:    */   {
/*  68:119 */     throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" + 
/*  69:120 */       number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass)
/*  73:    */   {
/*  74:144 */     Assert.notNull(text, "Text must not be null");
/*  75:145 */     Assert.notNull(targetClass, "Target class must not be null");
/*  76:146 */     String trimmed = StringUtils.trimAllWhitespace(text);
/*  77:148 */     if (targetClass.equals(Byte.class)) {
/*  78:149 */       return isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed);
/*  79:    */     }
/*  80:151 */     if (targetClass.equals(Short.class)) {
/*  81:152 */       return isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed);
/*  82:    */     }
/*  83:154 */     if (targetClass.equals(Integer.class)) {
/*  84:155 */       return isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed);
/*  85:    */     }
/*  86:157 */     if (targetClass.equals(Long.class)) {
/*  87:158 */       return isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed);
/*  88:    */     }
/*  89:160 */     if (targetClass.equals(BigInteger.class)) {
/*  90:161 */       return isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed);
/*  91:    */     }
/*  92:163 */     if (targetClass.equals(Float.class)) {
/*  93:164 */       return Float.valueOf(trimmed);
/*  94:    */     }
/*  95:166 */     if (targetClass.equals(Double.class)) {
/*  96:167 */       return Double.valueOf(trimmed);
/*  97:    */     }
/*  98:169 */     if ((targetClass.equals(BigDecimal.class)) || (targetClass.equals(Number.class))) {
/*  99:170 */       return new BigDecimal(trimmed);
/* 100:    */     }
/* 101:173 */     throw new IllegalArgumentException(
/* 102:174 */       "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static <T extends Number> T parseNumber(String text, Class<T> targetClass, NumberFormat numberFormat)
/* 106:    */   {
/* 107:194 */     if (numberFormat != null)
/* 108:    */     {
/* 109:195 */       Assert.notNull(text, "Text must not be null");
/* 110:196 */       Assert.notNull(targetClass, "Target class must not be null");
/* 111:197 */       DecimalFormat decimalFormat = null;
/* 112:198 */       boolean resetBigDecimal = false;
/* 113:199 */       if ((numberFormat instanceof DecimalFormat))
/* 114:    */       {
/* 115:200 */         decimalFormat = (DecimalFormat)numberFormat;
/* 116:201 */         if ((BigDecimal.class.equals(targetClass)) && (!decimalFormat.isParseBigDecimal()))
/* 117:    */         {
/* 118:202 */           decimalFormat.setParseBigDecimal(true);
/* 119:203 */           resetBigDecimal = true;
/* 120:    */         }
/* 121:    */       }
/* 122:    */       try
/* 123:    */       {
/* 124:207 */         Number number = numberFormat.parse(StringUtils.trimAllWhitespace(text));
/* 125:208 */         return convertNumberToTargetClass(number, targetClass);
/* 126:    */       }
/* 127:    */       catch (ParseException ex)
/* 128:    */       {
/* 129:211 */         throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
/* 130:    */       }
/* 131:    */       finally
/* 132:    */       {
/* 133:214 */         if (resetBigDecimal) {
/* 134:215 */           decimalFormat.setParseBigDecimal(false);
/* 135:    */         }
/* 136:    */       }
/* 137:    */     }
/* 138:220 */     return parseNumber(text, targetClass);
/* 139:    */   }
/* 140:    */   
/* 141:    */   private static boolean isHexNumber(String value)
/* 142:    */   {
/* 143:229 */     int index = value.startsWith("-") ? 1 : 0;
/* 144:230 */     return (value.startsWith("0x", index)) || (value.startsWith("0X", index)) || (value.startsWith("#", index));
/* 145:    */   }
/* 146:    */   
/* 147:    */   private static BigInteger decodeBigInteger(String value)
/* 148:    */   {
/* 149:239 */     int radix = 10;
/* 150:240 */     int index = 0;
/* 151:241 */     boolean negative = false;
/* 152:244 */     if (value.startsWith("-"))
/* 153:    */     {
/* 154:245 */       negative = true;
/* 155:246 */       index++;
/* 156:    */     }
/* 157:250 */     if ((value.startsWith("0x", index)) || (value.startsWith("0X", index)))
/* 158:    */     {
/* 159:251 */       index += 2;
/* 160:252 */       radix = 16;
/* 161:    */     }
/* 162:254 */     else if (value.startsWith("#", index))
/* 163:    */     {
/* 164:255 */       index++;
/* 165:256 */       radix = 16;
/* 166:    */     }
/* 167:258 */     else if ((value.startsWith("0", index)) && (value.length() > 1 + index))
/* 168:    */     {
/* 169:259 */       index++;
/* 170:260 */       radix = 8;
/* 171:    */     }
/* 172:263 */     BigInteger result = new BigInteger(value.substring(index), radix);
/* 173:264 */     return negative ? result.negate() : result;
/* 174:    */   }
/* 175:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.NumberUtils
 * JD-Core Version:    0.7.0.1
 */