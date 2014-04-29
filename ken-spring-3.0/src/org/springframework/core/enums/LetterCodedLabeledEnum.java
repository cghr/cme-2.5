/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ @Deprecated
/*  6:   */ public class LetterCodedLabeledEnum
/*  7:   */   extends AbstractGenericLabeledEnum
/*  8:   */ {
/*  9:   */   private final Character code;
/* 10:   */   
/* 11:   */   public LetterCodedLabeledEnum(char code, String label)
/* 12:   */   {
/* 13:47 */     super(label);
/* 14:48 */     Assert.isTrue(Character.isLetter(code), 
/* 15:49 */       "The code '" + code + "' is invalid: it must be a letter");
/* 16:50 */     this.code = new Character(code);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public Comparable getCode()
/* 20:   */   {
/* 21:55 */     return this.code;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public char getLetterCode()
/* 25:   */   {
/* 26:62 */     return ((Character)getCode()).charValue();
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.LetterCodedLabeledEnum
 * JD-Core Version:    0.7.0.1
 */