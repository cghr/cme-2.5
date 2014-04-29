/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import org.springframework.expression.EvaluationException;
/*   4:    */ import org.springframework.expression.TypedValue;
/*   5:    */ import org.springframework.expression.common.ExpressionUtils;
/*   6:    */ import org.springframework.expression.spel.ExpressionState;
/*   7:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*   8:    */ import org.springframework.expression.spel.SpelMessage;
/*   9:    */ import org.springframework.expression.spel.SpelNode;
/*  10:    */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ 
/*  13:    */ public abstract class SpelNodeImpl
/*  14:    */   implements SpelNode
/*  15:    */ {
/*  16: 37 */   private static SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
/*  17:    */   protected int pos;
/*  18: 40 */   protected SpelNodeImpl[] children = NO_CHILDREN;
/*  19:    */   private SpelNodeImpl parent;
/*  20:    */   
/*  21:    */   public SpelNodeImpl(int pos, SpelNodeImpl... operands)
/*  22:    */   {
/*  23: 44 */     this.pos = pos;
/*  24:    */     
/*  25: 46 */     Assert.isTrue(pos != 0);
/*  26: 47 */     if ((operands != null) && (operands.length > 0))
/*  27:    */     {
/*  28: 48 */       this.children = operands;
/*  29: 49 */       for (SpelNodeImpl childnode : operands) {
/*  30: 50 */         childnode.parent = this;
/*  31:    */       }
/*  32:    */     }
/*  33:    */   }
/*  34:    */   
/*  35:    */   protected SpelNodeImpl getPreviousChild()
/*  36:    */   {
/*  37: 56 */     SpelNodeImpl result = null;
/*  38: 57 */     if (this.parent != null) {
/*  39: 58 */       for (SpelNodeImpl child : this.parent.children)
/*  40:    */       {
/*  41: 59 */         if (this == child) {
/*  42:    */           break;
/*  43:    */         }
/*  44: 60 */         result = child;
/*  45:    */       }
/*  46:    */     }
/*  47: 63 */     return result;
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected boolean nextChildIs(Class... clazzes)
/*  51:    */   {
/*  52: 70 */     if (this.parent != null)
/*  53:    */     {
/*  54: 71 */       SpelNodeImpl[] peers = this.parent.children;
/*  55: 72 */       int i = 0;
/*  56: 72 */       for (int max = peers.length; i < max; i++) {
/*  57: 73 */         if (peers[i] == this)
/*  58:    */         {
/*  59: 74 */           if (i + 1 >= max) {
/*  60: 75 */             return false;
/*  61:    */           }
/*  62: 77 */           Class clazz = peers[(i + 1)].getClass();
/*  63: 78 */           for (Class desiredClazz : clazzes) {
/*  64: 79 */             if (clazz.equals(desiredClazz)) {
/*  65: 80 */               return true;
/*  66:    */             }
/*  67:    */           }
/*  68: 83 */           return false;
/*  69:    */         }
/*  70:    */       }
/*  71:    */     }
/*  72: 88 */     return false;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public final Object getValue(ExpressionState expressionState)
/*  76:    */     throws EvaluationException
/*  77:    */   {
/*  78: 92 */     if (expressionState != null) {
/*  79: 93 */       return getValueInternal(expressionState).getValue();
/*  80:    */     }
/*  81: 96 */     return getValue(new ExpressionState(new StandardEvaluationContext()));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final TypedValue getTypedValue(ExpressionState expressionState)
/*  85:    */     throws EvaluationException
/*  86:    */   {
/*  87:101 */     if (expressionState != null) {
/*  88:102 */       return getValueInternal(expressionState);
/*  89:    */     }
/*  90:105 */     return getTypedValue(new ExpressionState(new StandardEvaluationContext()));
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean isWritable(ExpressionState expressionState)
/*  94:    */     throws EvaluationException
/*  95:    */   {
/*  96:111 */     return false;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setValue(ExpressionState expressionState, Object newValue)
/* 100:    */     throws EvaluationException
/* 101:    */   {
/* 102:115 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, new Object[] { getClass() });
/* 103:    */   }
/* 104:    */   
/* 105:    */   public SpelNode getChild(int index)
/* 106:    */   {
/* 107:119 */     return this.children[index];
/* 108:    */   }
/* 109:    */   
/* 110:    */   public int getChildCount()
/* 111:    */   {
/* 112:123 */     return this.children.length;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public Class<?> getObjectClass(Object obj)
/* 116:    */   {
/* 117:127 */     if (obj == null) {
/* 118:128 */       return null;
/* 119:    */     }
/* 120:130 */     return (obj instanceof Class) ? (Class)obj : obj.getClass();
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected final <T> T getValue(ExpressionState state, Class<T> desiredReturnType)
/* 124:    */     throws EvaluationException
/* 125:    */   {
/* 126:135 */     Object result = getValueInternal(state).getValue();
/* 127:136 */     if ((result != null) && (desiredReturnType != null))
/* 128:    */     {
/* 129:137 */       Class<?> resultType = result.getClass();
/* 130:138 */       if (desiredReturnType.isAssignableFrom(resultType)) {
/* 131:139 */         return result;
/* 132:    */       }
/* 133:142 */       return ExpressionUtils.convert(state.getEvaluationContext(), result, desiredReturnType);
/* 134:    */     }
/* 135:144 */     return result;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public abstract TypedValue getValueInternal(ExpressionState paramExpressionState)
/* 139:    */     throws EvaluationException;
/* 140:    */   
/* 141:    */   public abstract String toStringAST();
/* 142:    */   
/* 143:    */   public int getStartPosition()
/* 144:    */   {
/* 145:152 */     return this.pos >> 16;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public int getEndPosition()
/* 149:    */   {
/* 150:156 */     return this.pos & 0xFFFF;
/* 151:    */   }
/* 152:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.SpelNodeImpl
 * JD-Core Version:    0.7.0.1
 */