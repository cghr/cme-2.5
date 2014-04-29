/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Map.Entry;
/*  10:    */ import org.springframework.expression.EvaluationException;
/*  11:    */ import org.springframework.expression.TypedValue;
/*  12:    */ import org.springframework.expression.spel.ExpressionState;
/*  13:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  14:    */ import org.springframework.expression.spel.SpelMessage;
/*  15:    */ import org.springframework.expression.spel.SpelNode;
/*  16:    */ import org.springframework.util.ClassUtils;
/*  17:    */ import org.springframework.util.ObjectUtils;
/*  18:    */ 
/*  19:    */ public class Projection
/*  20:    */   extends SpelNodeImpl
/*  21:    */ {
/*  22:    */   private final boolean nullSafe;
/*  23:    */   
/*  24:    */   public Projection(boolean nullSafe, int pos, SpelNodeImpl expression)
/*  25:    */   {
/*  26: 48 */     super(pos, new SpelNodeImpl[] { expression });
/*  27: 49 */     this.nullSafe = nullSafe;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public TypedValue getValueInternal(ExpressionState state)
/*  31:    */     throws EvaluationException
/*  32:    */   {
/*  33: 54 */     TypedValue op = state.getActiveContextObject();
/*  34:    */     
/*  35: 56 */     Object operand = op.getValue();
/*  36: 57 */     boolean operandIsArray = ObjectUtils.isArray(operand);
/*  37: 65 */     if ((operand instanceof Map))
/*  38:    */     {
/*  39: 66 */       Map<?, ?> mapData = (Map)operand;
/*  40: 67 */       List<Object> result = new ArrayList();
/*  41: 68 */       for (Map.Entry entry : mapData.entrySet()) {
/*  42:    */         try
/*  43:    */         {
/*  44: 70 */           state.pushActiveContextObject(new TypedValue(entry));
/*  45: 71 */           result.add(this.children[0].getValueInternal(state).getValue());
/*  46:    */         }
/*  47:    */         finally
/*  48:    */         {
/*  49: 74 */           state.popActiveContextObject();
/*  50:    */         }
/*  51:    */       }
/*  52: 77 */       return new TypedValue(result);
/*  53:    */     }
/*  54: 79 */     if (((operand instanceof Collection)) || (operandIsArray))
/*  55:    */     {
/*  56: 80 */       Collection<?> data = (operand instanceof Collection) ? (Collection)operand : 
/*  57: 81 */         (Collection)Arrays.asList(ObjectUtils.toObjectArray(operand));
/*  58: 82 */       List<Object> result = new ArrayList();
/*  59: 83 */       int idx = 0;
/*  60: 84 */       Object arrayElementType = null;
/*  61: 85 */       for (Object element : data)
/*  62:    */       {
/*  63:    */         try
/*  64:    */         {
/*  65: 87 */           state.pushActiveContextObject(new TypedValue(element));
/*  66: 88 */           state.enterScope("index", Integer.valueOf(idx));
/*  67: 89 */           Object value = this.children[0].getValueInternal(state).getValue();
/*  68: 90 */           if ((value != null) && (operandIsArray)) {
/*  69: 91 */             arrayElementType = determineCommonType((Class)arrayElementType, value.getClass());
/*  70:    */           }
/*  71: 93 */           result.add(value);
/*  72:    */         }
/*  73:    */         finally
/*  74:    */         {
/*  75: 96 */           state.exitScope();
/*  76: 97 */           state.popActiveContextObject();
/*  77:    */         }
/*  78: 99 */         idx++;
/*  79:    */       }
/*  80:101 */       if (operandIsArray)
/*  81:    */       {
/*  82:102 */         if (arrayElementType == null) {
/*  83:103 */           arrayElementType = Object.class;
/*  84:    */         }
/*  85:105 */         Object resultArray = Array.newInstance((Class)arrayElementType, result.size());
/*  86:106 */         System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/*  87:107 */         return new TypedValue(resultArray);
/*  88:    */       }
/*  89:109 */       return new TypedValue(result);
/*  90:    */     }
/*  91:112 */     if (operand == null)
/*  92:    */     {
/*  93:113 */       if (this.nullSafe) {
/*  94:114 */         return TypedValue.NULL;
/*  95:    */       }
/*  96:117 */       throw new SpelEvaluationException(getStartPosition(), 
/*  97:118 */         SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { "null" });
/*  98:    */     }
/*  99:122 */     throw new SpelEvaluationException(getStartPosition(), 
/* 100:123 */       SpelMessage.PROJECTION_NOT_SUPPORTED_ON_TYPE, new Object[] { operand.getClass().getName() });
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String toStringAST()
/* 104:    */   {
/* 105:130 */     StringBuilder sb = new StringBuilder();
/* 106:131 */     return "![" + getChild(0).toStringAST() + "]";
/* 107:    */   }
/* 108:    */   
/* 109:    */   private Class<?> determineCommonType(Class<?> oldType, Class<?> newType)
/* 110:    */   {
/* 111:135 */     if (oldType == null) {
/* 112:136 */       return newType;
/* 113:    */     }
/* 114:138 */     if (oldType.isAssignableFrom(newType)) {
/* 115:139 */       return oldType;
/* 116:    */     }
/* 117:141 */     Class<?> nextType = newType;
/* 118:142 */     while (nextType != Object.class)
/* 119:    */     {
/* 120:143 */       if (nextType.isAssignableFrom(oldType)) {
/* 121:144 */         return nextType;
/* 122:    */       }
/* 123:146 */       nextType = nextType.getSuperclass();
/* 124:    */     }
/* 125:148 */     Class[] interfaces = ClassUtils.getAllInterfacesForClass(newType);
/* 126:149 */     for (Class<?> nextInterface : interfaces) {
/* 127:150 */       if (nextInterface.isAssignableFrom(oldType)) {
/* 128:151 */         return nextInterface;
/* 129:    */       }
/* 130:    */     }
/* 131:154 */     return Object.class;
/* 132:    */   }
/* 133:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Projection
 * JD-Core Version:    0.7.0.1
 */