/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.EvaluationException;
/*  4:   */ import org.springframework.expression.TypedValue;
/*  5:   */ import org.springframework.expression.spel.ExpressionState;
/*  6:   */ import org.springframework.expression.spel.SpelNode;
/*  7:   */ 
/*  8:   */ public class QualifiedIdentifier
/*  9:   */   extends SpelNodeImpl
/* 10:   */ {
/* 11:   */   private TypedValue value;
/* 12:   */   
/* 13:   */   public QualifiedIdentifier(int pos, SpelNodeImpl... operands)
/* 14:   */   {
/* 15:37 */     super(pos, operands);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypedValue getValueInternal(ExpressionState state)
/* 19:   */     throws EvaluationException
/* 20:   */   {
/* 21:43 */     if (this.value == null)
/* 22:   */     {
/* 23:44 */       StringBuilder sb = new StringBuilder();
/* 24:45 */       for (int i = 0; i < getChildCount(); i++)
/* 25:   */       {
/* 26:46 */         Object value = this.children[i].getValueInternal(state).getValue();
/* 27:47 */         if ((i > 0) && (!value.toString().startsWith("$"))) {
/* 28:48 */           sb.append(".");
/* 29:   */         }
/* 30:50 */         sb.append(value);
/* 31:   */       }
/* 32:52 */       this.value = new TypedValue(sb.toString());
/* 33:   */     }
/* 34:54 */     return this.value;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String toStringAST()
/* 38:   */   {
/* 39:59 */     StringBuilder sb = new StringBuilder();
/* 40:60 */     if (this.value != null) {
/* 41:61 */       sb.append(this.value.getValue());
/* 42:   */     } else {
/* 43:63 */       for (int i = 0; i < getChildCount(); i++)
/* 44:   */       {
/* 45:64 */         if (i > 0) {
/* 46:65 */           sb.append(".");
/* 47:   */         }
/* 48:67 */         sb.append(getChild(i).toStringAST());
/* 49:   */       }
/* 50:   */     }
/* 51:70 */     return sb.toString();
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.QualifiedIdentifier
 * JD-Core Version:    0.7.0.1
 */