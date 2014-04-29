/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import org.springframework.expression.EvaluationException;
/*   4:    */ import org.springframework.expression.TypedValue;
/*   5:    */ import org.springframework.expression.spel.ExpressionState;
/*   6:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*   7:    */ import org.springframework.expression.spel.SpelNode;
/*   8:    */ 
/*   9:    */ public class CompoundExpression
/*  10:    */   extends SpelNodeImpl
/*  11:    */ {
/*  12:    */   public CompoundExpression(int pos, SpelNodeImpl... expressionComponents)
/*  13:    */   {
/*  14: 33 */     super(pos, expressionComponents);
/*  15: 34 */     if (expressionComponents.length < 2) {
/*  16: 35 */       throw new IllegalStateException("Dont build compound expression less than one entry: " + expressionComponents.length);
/*  17:    */     }
/*  18:    */   }
/*  19:    */   
/*  20:    */   public TypedValue getValueInternal(ExpressionState state)
/*  21:    */     throws EvaluationException
/*  22:    */   {
/*  23: 48 */     TypedValue result = null;
/*  24: 49 */     SpelNodeImpl nextNode = null;
/*  25:    */     try
/*  26:    */     {
/*  27: 51 */       nextNode = this.children[0];
/*  28: 52 */       result = nextNode.getValueInternal(state);
/*  29: 53 */       for (int i = 1; i < getChildCount(); i++) {
/*  30:    */         try
/*  31:    */         {
/*  32: 55 */           state.pushActiveContextObject(result);
/*  33: 56 */           nextNode = this.children[i];
/*  34: 57 */           result = nextNode.getValueInternal(state);
/*  35:    */         }
/*  36:    */         finally
/*  37:    */         {
/*  38: 59 */           state.popActiveContextObject();
/*  39:    */         }
/*  40:    */       }
/*  41:    */     }
/*  42:    */     catch (SpelEvaluationException ee)
/*  43:    */     {
/*  44: 64 */       ee.setPosition(nextNode.getStartPosition());
/*  45: 65 */       throw ee;
/*  46:    */     }
/*  47: 67 */     return result;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setValue(ExpressionState state, Object value)
/*  51:    */     throws EvaluationException
/*  52:    */   {
/*  53: 72 */     if (getChildCount() == 1)
/*  54:    */     {
/*  55: 73 */       getChild(0).setValue(state, value);
/*  56: 74 */       return;
/*  57:    */     }
/*  58: 76 */     TypedValue ctx = this.children[0].getValueInternal(state);
/*  59: 77 */     for (i = 1; i < getChildCount() - 1; i++) {
/*  60:    */       try
/*  61:    */       {
/*  62: 79 */         state.pushActiveContextObject(ctx);
/*  63: 80 */         ctx = this.children[i].getValueInternal(state);
/*  64:    */       }
/*  65:    */       finally
/*  66:    */       {
/*  67: 82 */         state.popActiveContextObject();
/*  68:    */       }
/*  69:    */     }
/*  70:    */     try
/*  71:    */     {
/*  72: 86 */       state.pushActiveContextObject(ctx);
/*  73: 87 */       getChild(getChildCount() - 1).setValue(state, value);
/*  74:    */     }
/*  75:    */     finally
/*  76:    */     {
/*  77: 89 */       state.popActiveContextObject();
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean isWritable(ExpressionState state)
/*  82:    */     throws EvaluationException
/*  83:    */   {
/*  84: 95 */     if (getChildCount() == 1) {
/*  85: 96 */       return getChild(0).isWritable(state);
/*  86:    */     }
/*  87: 98 */     TypedValue ctx = this.children[0].getValueInternal(state);
/*  88: 99 */     for (i = 1; i < getChildCount() - 1; i++) {
/*  89:    */       try
/*  90:    */       {
/*  91:101 */         state.pushActiveContextObject(ctx);
/*  92:102 */         ctx = this.children[i].getValueInternal(state);
/*  93:    */       }
/*  94:    */       finally
/*  95:    */       {
/*  96:104 */         state.popActiveContextObject();
/*  97:    */       }
/*  98:    */     }
/*  99:    */     try
/* 100:    */     {
/* 101:108 */       state.pushActiveContextObject(ctx);
/* 102:109 */       return getChild(getChildCount() - 1).isWritable(state);
/* 103:    */     }
/* 104:    */     finally
/* 105:    */     {
/* 106:111 */       state.popActiveContextObject();
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String toStringAST()
/* 111:    */   {
/* 112:117 */     StringBuilder sb = new StringBuilder();
/* 113:118 */     for (int i = 0; i < getChildCount(); i++)
/* 114:    */     {
/* 115:119 */       if (i > 0) {
/* 116:119 */         sb.append(".");
/* 117:    */       }
/* 118:120 */       sb.append(getChild(i).toStringAST());
/* 119:    */     }
/* 120:122 */     return sb.toString();
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.CompoundExpression
 * JD-Core Version:    0.7.0.1
 */