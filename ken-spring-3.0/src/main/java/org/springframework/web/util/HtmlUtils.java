/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ public abstract class HtmlUtils
/*   4:    */ {
/*   5: 44 */   private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();
/*   6:    */   
/*   7:    */   public static String htmlEscape(String input)
/*   8:    */   {
/*   9: 60 */     if (input == null) {
/*  10: 61 */       return null;
/*  11:    */     }
/*  12: 63 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/*  13: 64 */     for (int i = 0; i < input.length(); i++)
/*  14:    */     {
/*  15: 65 */       char character = input.charAt(i);
/*  16: 66 */       String reference = characterEntityReferences.convertToReference(character);
/*  17: 67 */       if (reference != null) {
/*  18: 68 */         escaped.append(reference);
/*  19:    */       } else {
/*  20: 71 */         escaped.append(character);
/*  21:    */       }
/*  22:    */     }
/*  23: 74 */     return escaped.toString();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static String htmlEscapeDecimal(String input)
/*  27:    */   {
/*  28: 90 */     if (input == null) {
/*  29: 91 */       return null;
/*  30:    */     }
/*  31: 93 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/*  32: 94 */     for (int i = 0; i < input.length(); i++)
/*  33:    */     {
/*  34: 95 */       char character = input.charAt(i);
/*  35: 96 */       if (characterEntityReferences.isMappedToReference(character))
/*  36:    */       {
/*  37: 97 */         escaped.append("&#");
/*  38: 98 */         escaped.append(character);
/*  39: 99 */         escaped.append(';');
/*  40:    */       }
/*  41:    */       else
/*  42:    */       {
/*  43:102 */         escaped.append(character);
/*  44:    */       }
/*  45:    */     }
/*  46:105 */     return escaped.toString();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static String htmlEscapeHex(String input)
/*  50:    */   {
/*  51:121 */     if (input == null) {
/*  52:122 */       return null;
/*  53:    */     }
/*  54:124 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/*  55:125 */     for (int i = 0; i < input.length(); i++)
/*  56:    */     {
/*  57:126 */       char character = input.charAt(i);
/*  58:127 */       if (characterEntityReferences.isMappedToReference(character))
/*  59:    */       {
/*  60:128 */         escaped.append("&#x");
/*  61:129 */         escaped.append(Integer.toString(character, 16));
/*  62:130 */         escaped.append(';');
/*  63:    */       }
/*  64:    */       else
/*  65:    */       {
/*  66:133 */         escaped.append(character);
/*  67:    */       }
/*  68:    */     }
/*  69:136 */     return escaped.toString();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static String htmlUnescape(String input)
/*  73:    */   {
/*  74:159 */     if (input == null) {
/*  75:160 */       return null;
/*  76:    */     }
/*  77:162 */     return new HtmlCharacterEntityDecoder(characterEntityReferences, input).decode();
/*  78:    */   }
/*  79:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.HtmlUtils
 * JD-Core Version:    0.7.0.1
 */