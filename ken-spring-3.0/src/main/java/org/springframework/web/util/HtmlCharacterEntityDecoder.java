/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ class HtmlCharacterEntityDecoder
/*   4:    */ {
/*   5:    */   private static final int MAX_REFERENCE_SIZE = 10;
/*   6:    */   private final HtmlCharacterEntityReferences characterEntityReferences;
/*   7:    */   private final String originalMessage;
/*   8:    */   private final StringBuilder decodedMessage;
/*   9: 38 */   private int currentPosition = 0;
/*  10: 40 */   private int nextPotentialReferencePosition = -1;
/*  11: 42 */   private int nextSemicolonPosition = -2;
/*  12:    */   
/*  13:    */   public HtmlCharacterEntityDecoder(HtmlCharacterEntityReferences characterEntityReferences, String original)
/*  14:    */   {
/*  15: 46 */     this.characterEntityReferences = characterEntityReferences;
/*  16: 47 */     this.originalMessage = original;
/*  17: 48 */     this.decodedMessage = new StringBuilder(this.originalMessage.length());
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String decode()
/*  21:    */   {
/*  22: 52 */     while (this.currentPosition < this.originalMessage.length())
/*  23:    */     {
/*  24: 53 */       findNextPotentialReference(this.currentPosition);
/*  25: 54 */       copyCharactersTillPotentialReference();
/*  26: 55 */       processPossibleReference();
/*  27:    */     }
/*  28: 57 */     return this.decodedMessage.toString();
/*  29:    */   }
/*  30:    */   
/*  31:    */   private void findNextPotentialReference(int startPosition)
/*  32:    */   {
/*  33: 61 */     this.nextPotentialReferencePosition = Math.max(startPosition, this.nextSemicolonPosition - 10);
/*  34:    */     do
/*  35:    */     {
/*  36: 64 */       this.nextPotentialReferencePosition = 
/*  37: 65 */         this.originalMessage.indexOf('&', this.nextPotentialReferencePosition);
/*  38: 67 */       if ((this.nextSemicolonPosition != -1) && 
/*  39: 68 */         (this.nextSemicolonPosition < this.nextPotentialReferencePosition)) {
/*  40: 69 */         this.nextSemicolonPosition = this.originalMessage.indexOf(';', this.nextPotentialReferencePosition + 1);
/*  41:    */       }
/*  42: 71 */       boolean isPotentialReference = 
/*  43: 72 */         (this.nextPotentialReferencePosition != -1) && 
/*  44: 73 */         (this.nextSemicolonPosition != -1) && 
/*  45: 74 */         (this.nextPotentialReferencePosition - this.nextSemicolonPosition < 10);
/*  46: 76 */       if (isPotentialReference) {
/*  47:    */         break;
/*  48:    */       }
/*  49: 79 */       if (this.nextPotentialReferencePosition == -1) {
/*  50:    */         break;
/*  51:    */       }
/*  52: 82 */       if (this.nextSemicolonPosition == -1)
/*  53:    */       {
/*  54: 83 */         this.nextPotentialReferencePosition = -1;
/*  55: 84 */         break;
/*  56:    */       }
/*  57: 87 */       this.nextPotentialReferencePosition += 1;
/*  58: 89 */     } while (this.nextPotentialReferencePosition != -1);
/*  59:    */   }
/*  60:    */   
/*  61:    */   private void copyCharactersTillPotentialReference()
/*  62:    */   {
/*  63: 94 */     if (this.nextPotentialReferencePosition != this.currentPosition)
/*  64:    */     {
/*  65: 95 */       int skipUntilIndex = this.nextPotentialReferencePosition != -1 ? 
/*  66: 96 */         this.nextPotentialReferencePosition : this.originalMessage.length();
/*  67: 97 */       if (skipUntilIndex - this.currentPosition > 3)
/*  68:    */       {
/*  69: 98 */         this.decodedMessage.append(this.originalMessage.substring(this.currentPosition, skipUntilIndex));
/*  70: 99 */         this.currentPosition = skipUntilIndex;
/*  71:    */       }
/*  72:    */       else
/*  73:    */       {
/*  74:102 */         while (this.currentPosition < skipUntilIndex) {
/*  75:103 */           this.decodedMessage.append(this.originalMessage.charAt(this.currentPosition++));
/*  76:    */         }
/*  77:    */       }
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   private void processPossibleReference()
/*  82:    */   {
/*  83:109 */     if (this.nextPotentialReferencePosition != -1)
/*  84:    */     {
/*  85:110 */       boolean isNumberedReference = this.originalMessage.charAt(this.currentPosition + 1) == '#';
/*  86:111 */       boolean wasProcessable = isNumberedReference ? processNumberedReference() : processNamedReference();
/*  87:112 */       if (wasProcessable)
/*  88:    */       {
/*  89:113 */         this.currentPosition = (this.nextSemicolonPosition + 1);
/*  90:    */       }
/*  91:    */       else
/*  92:    */       {
/*  93:116 */         char currentChar = this.originalMessage.charAt(this.currentPosition);
/*  94:117 */         this.decodedMessage.append(currentChar);
/*  95:118 */         this.currentPosition += 1;
/*  96:    */       }
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   private boolean processNumberedReference()
/* 101:    */   {
/* 102:124 */     boolean isHexNumberedReference = 
/* 103:125 */       (this.originalMessage.charAt(this.nextPotentialReferencePosition + 2) == 'x') || 
/* 104:126 */       (this.originalMessage.charAt(this.nextPotentialReferencePosition + 2) == 'X');
/* 105:    */     try
/* 106:    */     {
/* 107:128 */       int value = !isHexNumberedReference ? 
/* 108:129 */         Integer.parseInt(getReferenceSubstring(2)) : 
/* 109:130 */         Integer.parseInt(getReferenceSubstring(3), 16);
/* 110:131 */       this.decodedMessage.append((char)value);
/* 111:132 */       return true;
/* 112:    */     }
/* 113:    */     catch (NumberFormatException localNumberFormatException) {}
/* 114:135 */     return false;
/* 115:    */   }
/* 116:    */   
/* 117:    */   private boolean processNamedReference()
/* 118:    */   {
/* 119:140 */     String referenceName = getReferenceSubstring(1);
/* 120:141 */     char mappedCharacter = this.characterEntityReferences.convertToCharacter(referenceName);
/* 121:142 */     if (mappedCharacter != 65535)
/* 122:    */     {
/* 123:143 */       this.decodedMessage.append(mappedCharacter);
/* 124:144 */       return true;
/* 125:    */     }
/* 126:146 */     return false;
/* 127:    */   }
/* 128:    */   
/* 129:    */   private String getReferenceSubstring(int referenceOffset)
/* 130:    */   {
/* 131:150 */     return this.originalMessage.substring(this.nextPotentialReferencePosition + referenceOffset, this.nextSemicolonPosition);
/* 132:    */   }
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.HtmlCharacterEntityDecoder
 * JD-Core Version:    0.7.0.1
 */