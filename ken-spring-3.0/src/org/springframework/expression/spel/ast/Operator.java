/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.spel.SpelNode;
/*  4:   */ 
/*  5:   */ public abstract class Operator
/*  6:   */   extends SpelNodeImpl
/*  7:   */ {
/*  8:   */   String operatorName;
/*  9:   */   
/* 10:   */   public Operator(String payload, int pos, SpelNodeImpl... operands)
/* 11:   */   {
/* 12:32 */     super(pos, operands);
/* 13:33 */     this.operatorName = payload;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public SpelNodeImpl getLeftOperand()
/* 17:   */   {
/* 18:37 */     return this.children[0];
/* 19:   */   }
/* 20:   */   
/* 21:   */   public SpelNodeImpl getRightOperand()
/* 22:   */   {
/* 23:41 */     return this.children[1];
/* 24:   */   }
/* 25:   */   
/* 26:   */   public final String getOperatorName()
/* 27:   */   {
/* 28:45 */     return this.operatorName;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public String toStringAST()
/* 32:   */   {
/* 33:53 */     StringBuilder sb = new StringBuilder();
/* 34:54 */     sb.append("(");
/* 35:55 */     sb.append(getChild(0).toStringAST());
/* 36:56 */     for (int i = 1; i < getChildCount(); i++)
/* 37:   */     {
/* 38:57 */       sb.append(" ").append(getOperatorName()).append(" ");
/* 39:58 */       sb.append(getChild(i).toStringAST());
/* 40:   */     }
/* 41:60 */     sb.append(")");
/* 42:61 */     return sb.toString();
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Operator
 * JD-Core Version:    0.7.0.1
 */