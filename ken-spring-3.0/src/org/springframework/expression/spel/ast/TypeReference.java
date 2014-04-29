/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelNode;
/*  7:   */ 
/*  8:   */ public class TypeReference
/*  9:   */   extends SpelNodeImpl
/* 10:   */ {
/* 11:   */   public TypeReference(int pos, SpelNodeImpl qualifiedId)
/* 12:   */   {
/* 13:31 */     super(pos, new SpelNodeImpl[] { qualifiedId });
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TypedValue getValueInternal(ExpressionState state)
/* 17:   */     throws EvaluationException
/* 18:   */   {
/* 19:37 */     String typename = (String)this.children[0].getValueInternal(state).getValue();
/* 20:38 */     if ((typename.indexOf(".") == -1) && (Character.isLowerCase(typename.charAt(0))))
/* 21:   */     {
/* 22:39 */       TypeCode tc = TypeCode.valueOf(typename.toUpperCase());
/* 23:40 */       if (tc != TypeCode.OBJECT) {
/* 24:42 */         return new TypedValue(tc.getType());
/* 25:   */       }
/* 26:   */     }
/* 27:45 */     return new TypedValue(state.findType(typename));
/* 28:   */   }
/* 29:   */   
/* 30:   */   public String toStringAST()
/* 31:   */   {
/* 32:50 */     StringBuilder sb = new StringBuilder();
/* 33:51 */     sb.append("T(");
/* 34:52 */     sb.append(getChild(0).toStringAST());
/* 35:53 */     sb.append(")");
/* 36:54 */     return sb.toString();
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.TypeReference
 * JD-Core Version:    0.7.0.1
 */