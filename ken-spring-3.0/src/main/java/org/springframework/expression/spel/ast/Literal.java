/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.TypedValue;
/*  4:   */ import org.springframework.expression.spel.ExpressionState;
/*  5:   */ import org.springframework.expression.spel.InternalParseException;
/*  6:   */ import org.springframework.expression.spel.SpelEvaluationException;
/*  7:   */ import org.springframework.expression.spel.SpelMessage;
/*  8:   */ import org.springframework.expression.spel.SpelParseException;
/*  9:   */ 
/* 10:   */ public abstract class Literal
/* 11:   */   extends SpelNodeImpl
/* 12:   */ {
/* 13:   */   protected String literalValue;
/* 14:   */   
/* 15:   */   public Literal(String payload, int pos)
/* 16:   */   {
/* 17:36 */     super(pos, new SpelNodeImpl[0]);
/* 18:37 */     this.literalValue = payload;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public abstract TypedValue getLiteralValue();
/* 22:   */   
/* 23:   */   public final TypedValue getValueInternal(ExpressionState state)
/* 24:   */     throws SpelEvaluationException
/* 25:   */   {
/* 26:44 */     return getLiteralValue();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String toString()
/* 30:   */   {
/* 31:49 */     return getLiteralValue().getValue().toString();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String toStringAST()
/* 35:   */   {
/* 36:54 */     return toString();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public static Literal getIntLiteral(String numberToken, int pos, int radix)
/* 40:   */   {
/* 41:   */     try
/* 42:   */     {
/* 43:67 */       int value = Integer.parseInt(numberToken, radix);
/* 44:68 */       return new IntLiteral(numberToken, pos, value);
/* 45:   */     }
/* 46:   */     catch (NumberFormatException nfe)
/* 47:   */     {
/* 48:70 */       throw new InternalParseException(new SpelParseException(pos >> 16, nfe, SpelMessage.NOT_AN_INTEGER, new Object[] { numberToken }));
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   public static Literal getLongLiteral(String numberToken, int pos, int radix)
/* 53:   */   {
/* 54:   */     try
/* 55:   */     {
/* 56:76 */       long value = Long.parseLong(numberToken, radix);
/* 57:77 */       return new LongLiteral(numberToken, pos, value);
/* 58:   */     }
/* 59:   */     catch (NumberFormatException nfe)
/* 60:   */     {
/* 61:79 */       throw new InternalParseException(new SpelParseException(pos >> 16, nfe, SpelMessage.NOT_A_LONG, new Object[] { numberToken }));
/* 62:   */     }
/* 63:   */   }
/* 64:   */   
/* 65:   */   public static Literal getRealLiteral(String numberToken, int pos, boolean isFloat)
/* 66:   */   {
/* 67:   */     try
/* 68:   */     {
/* 69:86 */       if (isFloat)
/* 70:   */       {
/* 71:87 */         float value = Float.parseFloat(numberToken);
/* 72:88 */         return new RealLiteral(numberToken, pos, value);
/* 73:   */       }
/* 74:90 */       double value = Double.parseDouble(numberToken);
/* 75:91 */       return new RealLiteral(numberToken, pos, value);
/* 76:   */     }
/* 77:   */     catch (NumberFormatException nfe)
/* 78:   */     {
/* 79:94 */       throw new InternalParseException(new SpelParseException(pos >> 16, nfe, SpelMessage.NOT_A_REAL, new Object[] { numberToken }));
/* 80:   */     }
/* 81:   */   }
/* 82:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Literal
 * JD-Core Version:    0.7.0.1
 */