/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Array;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Collection;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import org.springframework.core.convert.TypeDescriptor;
/*  12:    */ import org.springframework.expression.EvaluationException;
/*  13:    */ import org.springframework.expression.TypedValue;
/*  14:    */ import org.springframework.expression.spel.ExpressionState;
/*  15:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  16:    */ import org.springframework.expression.spel.SpelMessage;
/*  17:    */ import org.springframework.expression.spel.SpelNode;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ import org.springframework.util.ObjectUtils;
/*  20:    */ 
/*  21:    */ public class Selection
/*  22:    */   extends SpelNodeImpl
/*  23:    */ {
/*  24:    */   public static final int ALL = 0;
/*  25:    */   public static final int FIRST = 1;
/*  26:    */   public static final int LAST = 2;
/*  27:    */   private final int variant;
/*  28:    */   private final boolean nullSafe;
/*  29:    */   
/*  30:    */   public Selection(boolean nullSafe, int variant, int pos, SpelNodeImpl expression)
/*  31:    */   {
/*  32: 57 */     super(pos, new SpelNodeImpl[] { expression });
/*  33: 58 */     this.nullSafe = nullSafe;
/*  34: 59 */     this.variant = variant;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public TypedValue getValueInternal(ExpressionState state)
/*  38:    */     throws EvaluationException
/*  39:    */   {
/*  40: 65 */     TypedValue op = state.getActiveContextObject();
/*  41: 66 */     Object operand = op.getValue();
/*  42:    */     
/*  43: 68 */     SpelNodeImpl selectionCriteria = this.children[0];
/*  44:    */     TypedValue kvpair;
/*  45:    */     TypedValue localTypedValue1;
/*  46: 69 */     if ((operand instanceof Map))
/*  47:    */     {
/*  48: 70 */       Map<?, ?> mapdata = (Map)operand;
/*  49:    */       
/*  50: 72 */       Map<Object, Object> result = new HashMap();
/*  51: 73 */       Object lastKey = null;
/*  52: 74 */       for (Map.Entry entry : mapdata.entrySet())
/*  53:    */       {
/*  54:    */         try
/*  55:    */         {
/*  56: 76 */           kvpair = new TypedValue(entry);
/*  57: 77 */           state.pushActiveContextObject(kvpair);
/*  58: 78 */           Object o = selectionCriteria.getValueInternal(state).getValue();
/*  59: 79 */           if ((o instanceof Boolean))
/*  60:    */           {
/*  61: 80 */             if (((Boolean)o).booleanValue())
/*  62:    */             {
/*  63: 81 */               if (this.variant == 1)
/*  64:    */               {
/*  65: 82 */                 result.put(entry.getKey(), entry.getValue());
/*  66: 83 */                 return new TypedValue(result);
/*  67:    */               }
/*  68: 85 */               result.put(entry.getKey(), entry.getValue());
/*  69: 86 */               lastKey = entry.getKey();
/*  70:    */             }
/*  71:    */           }
/*  72:    */           else {
/*  73: 89 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), 
/*  74: 90 */               SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/*  75:    */           }
/*  76:    */         }
/*  77:    */         finally
/*  78:    */         {
/*  79: 93 */           state.popActiveContextObject();
/*  80:    */         }
/*  81: 93 */         state.popActiveContextObject();
/*  82:    */       }
/*  83: 96 */       if (((this.variant == 1) || (this.variant == 2)) && (result.size() == 0)) {
/*  84: 97 */         return new TypedValue(null);
/*  85:    */       }
/*  86: 99 */       if (this.variant == 2)
/*  87:    */       {
/*  88:100 */         Map resultMap = new HashMap();
/*  89:101 */         Object lastValue = result.get(lastKey);
/*  90:102 */         resultMap.put(lastKey, lastValue);
/*  91:103 */         return new TypedValue(resultMap);
/*  92:    */       }
/*  93:105 */       return new TypedValue(result);
/*  94:    */     }
/*  95:106 */     if (((operand instanceof Collection)) || (ObjectUtils.isArray(operand)))
/*  96:    */     {
/*  97:107 */       List<Object> data = new ArrayList();
/*  98:108 */       Collection<?> c = (operand instanceof Collection) ? 
/*  99:109 */         (Collection)operand : (Collection)Arrays.asList(ObjectUtils.toObjectArray(operand));
/* 100:110 */       data.addAll(c);
/* 101:111 */       List<Object> result = new ArrayList();
/* 102:112 */       int idx = 0;
/* 103:113 */       for (Object element : data)
/* 104:    */       {
/* 105:    */         try
/* 106:    */         {
/* 107:115 */           state.pushActiveContextObject(new TypedValue(element));
/* 108:116 */           state.enterScope("index", Integer.valueOf(idx));
/* 109:117 */           Object o = selectionCriteria.getValueInternal(state).getValue();
/* 110:118 */           if ((o instanceof Boolean))
/* 111:    */           {
/* 112:119 */             if (((Boolean)o).booleanValue())
/* 113:    */             {
/* 114:120 */               if (this.variant == 1) {
/* 115:121 */                 return new TypedValue(element);
/* 116:    */               }
/* 117:123 */               result.add(element);
/* 118:    */             }
/* 119:    */           }
/* 120:    */           else {
/* 121:126 */             throw new SpelEvaluationException(selectionCriteria.getStartPosition(), 
/* 122:127 */               SpelMessage.RESULT_OF_SELECTION_CRITERIA_IS_NOT_BOOLEAN, new Object[0]);
/* 123:    */           }
/* 124:129 */           idx++;
/* 125:    */         }
/* 126:    */         finally
/* 127:    */         {
/* 128:131 */           state.exitScope();
/* 129:132 */           state.popActiveContextObject();
/* 130:    */         }
/* 131:131 */         state.exitScope();
/* 132:132 */         state.popActiveContextObject();
/* 133:    */       }
/* 134:135 */       if (((this.variant == 1) || (this.variant == 2)) && (result.size() == 0)) {
/* 135:136 */         return TypedValue.NULL;
/* 136:    */       }
/* 137:138 */       if (this.variant == 2) {
/* 138:139 */         return new TypedValue(result.get(result.size() - 1));
/* 139:    */       }
/* 140:141 */       if ((operand instanceof Collection)) {
/* 141:142 */         return new TypedValue(result);
/* 142:    */       }
/* 143:145 */       Object elementType = ClassUtils.resolvePrimitiveIfNecessary(op.getTypeDescriptor().getElementTypeDescriptor().getType());
/* 144:146 */       Object resultArray = Array.newInstance((Class)elementType, result.size());
/* 145:147 */       System.arraycopy(result.toArray(), 0, resultArray, 0, result.size());
/* 146:148 */       return new TypedValue(resultArray);
/* 147:    */     }
/* 148:151 */     if (operand == null)
/* 149:    */     {
/* 150:152 */       if (this.nullSafe) {
/* 151:153 */         return TypedValue.NULL;
/* 152:    */       }
/* 153:155 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] {
/* 154:156 */         "null" });
/* 155:    */     }
/* 156:159 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.INVALID_TYPE_FOR_SELECTION, new Object[] {
/* 157:160 */       operand.getClass().getName() });
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String toStringAST()
/* 161:    */   {
/* 162:167 */     StringBuilder sb = new StringBuilder();
/* 163:168 */     switch (this.variant)
/* 164:    */     {
/* 165:    */     case 0: 
/* 166:170 */       sb.append("?[");
/* 167:171 */       break;
/* 168:    */     case 1: 
/* 169:173 */       sb.append("^[");
/* 170:174 */       break;
/* 171:    */     case 2: 
/* 172:176 */       sb.append("$[");
/* 173:    */     }
/* 174:179 */     return getChild(0).toStringAST() + "]";
/* 175:    */   }
/* 176:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.Selection
 * JD-Core Version:    0.7.0.1
 */