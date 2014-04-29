/*   1:    */ package org.springframework.expression.common;
/*   2:    */ 
/*   3:    */ import org.springframework.core.convert.TypeDescriptor;
/*   4:    */ import org.springframework.expression.EvaluationContext;
/*   5:    */ import org.springframework.expression.EvaluationException;
/*   6:    */ import org.springframework.expression.Expression;
/*   7:    */ 
/*   8:    */ public class CompositeStringExpression
/*   9:    */   implements Expression
/*  10:    */ {
/*  11:    */   private final String expressionString;
/*  12:    */   private final Expression[] expressions;
/*  13:    */   
/*  14:    */   public CompositeStringExpression(String expressionString, Expression[] expressions)
/*  15:    */   {
/*  16: 48 */     this.expressionString = expressionString;
/*  17: 49 */     this.expressions = expressions;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public final String getExpressionString()
/*  21:    */   {
/*  22: 54 */     return this.expressionString;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getValue()
/*  26:    */     throws EvaluationException
/*  27:    */   {
/*  28: 58 */     StringBuilder sb = new StringBuilder();
/*  29: 59 */     for (Expression expression : this.expressions)
/*  30:    */     {
/*  31: 60 */       String value = (String)expression.getValue(String.class);
/*  32: 61 */       if (value != null) {
/*  33: 62 */         sb.append(value);
/*  34:    */       }
/*  35:    */     }
/*  36: 65 */     return sb.toString();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getValue(Object rootObject)
/*  40:    */     throws EvaluationException
/*  41:    */   {
/*  42: 69 */     StringBuilder sb = new StringBuilder();
/*  43: 70 */     for (Expression expression : this.expressions)
/*  44:    */     {
/*  45: 71 */       String value = (String)expression.getValue(rootObject, String.class);
/*  46: 72 */       if (value != null) {
/*  47: 73 */         sb.append(value);
/*  48:    */       }
/*  49:    */     }
/*  50: 76 */     return sb.toString();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getValue(EvaluationContext context)
/*  54:    */     throws EvaluationException
/*  55:    */   {
/*  56: 80 */     StringBuilder sb = new StringBuilder();
/*  57: 81 */     for (Expression expression : this.expressions)
/*  58:    */     {
/*  59: 82 */       String value = (String)expression.getValue(context, String.class);
/*  60: 83 */       if (value != null) {
/*  61: 84 */         sb.append(value);
/*  62:    */       }
/*  63:    */     }
/*  64: 87 */     return sb.toString();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String getValue(EvaluationContext context, Object rootObject)
/*  68:    */     throws EvaluationException
/*  69:    */   {
/*  70: 91 */     StringBuilder sb = new StringBuilder();
/*  71: 92 */     for (Expression expression : this.expressions)
/*  72:    */     {
/*  73: 93 */       String value = (String)expression.getValue(context, rootObject, String.class);
/*  74: 94 */       if (value != null) {
/*  75: 95 */         sb.append(value);
/*  76:    */       }
/*  77:    */     }
/*  78: 98 */     return sb.toString();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public Class getValueType(EvaluationContext context)
/*  82:    */   {
/*  83:102 */     return String.class;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public Class getValueType()
/*  87:    */   {
/*  88:106 */     return String.class;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context)
/*  92:    */   {
/*  93:110 */     return TypeDescriptor.valueOf(String.class);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public TypeDescriptor getValueTypeDescriptor()
/*  97:    */   {
/*  98:114 */     return TypeDescriptor.valueOf(String.class);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setValue(EvaluationContext context, Object value)
/* 102:    */     throws EvaluationException
/* 103:    */   {
/* 104:118 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/* 105:    */   }
/* 106:    */   
/* 107:    */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType)
/* 108:    */     throws EvaluationException
/* 109:    */   {
/* 110:122 */     Object value = getValue(context);
/* 111:123 */     return ExpressionUtils.convert(context, value, expectedResultType);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public <T> T getValue(Class<T> expectedResultType)
/* 115:    */     throws EvaluationException
/* 116:    */   {
/* 117:127 */     Object value = getValue();
/* 118:128 */     return ExpressionUtils.convert(null, value, expectedResultType);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public boolean isWritable(EvaluationContext context)
/* 122:    */   {
/* 123:132 */     return false;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public Expression[] getExpressions()
/* 127:    */   {
/* 128:136 */     return this.expressions;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public <T> T getValue(Object rootObject, Class<T> desiredResultType)
/* 132:    */     throws EvaluationException
/* 133:    */   {
/* 134:141 */     Object value = getValue(rootObject);
/* 135:142 */     return ExpressionUtils.convert(null, value, desiredResultType);
/* 136:    */   }
/* 137:    */   
/* 138:    */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> desiredResultType)
/* 139:    */     throws EvaluationException
/* 140:    */   {
/* 141:147 */     Object value = getValue(context, rootObject);
/* 142:148 */     return ExpressionUtils.convert(context, value, desiredResultType);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public Class getValueType(Object rootObject)
/* 146:    */     throws EvaluationException
/* 147:    */   {
/* 148:152 */     return String.class;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public Class getValueType(EvaluationContext context, Object rootObject)
/* 152:    */     throws EvaluationException
/* 153:    */   {
/* 154:156 */     return String.class;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public TypeDescriptor getValueTypeDescriptor(Object rootObject)
/* 158:    */     throws EvaluationException
/* 159:    */   {
/* 160:160 */     return TypeDescriptor.valueOf(String.class);
/* 161:    */   }
/* 162:    */   
/* 163:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject)
/* 164:    */     throws EvaluationException
/* 165:    */   {
/* 166:164 */     return TypeDescriptor.valueOf(String.class);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public boolean isWritable(EvaluationContext context, Object rootObject)
/* 170:    */     throws EvaluationException
/* 171:    */   {
/* 172:168 */     return false;
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setValue(EvaluationContext context, Object rootObject, Object value)
/* 176:    */     throws EvaluationException
/* 177:    */   {
/* 178:172 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/* 179:    */   }
/* 180:    */   
/* 181:    */   public boolean isWritable(Object rootObject)
/* 182:    */     throws EvaluationException
/* 183:    */   {
/* 184:176 */     return false;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void setValue(Object rootObject, Object value)
/* 188:    */     throws EvaluationException
/* 189:    */   {
/* 190:180 */     throw new EvaluationException(this.expressionString, "Cannot call setValue on a composite expression");
/* 191:    */   }
/* 192:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.common.CompositeStringExpression
 * JD-Core Version:    0.7.0.1
 */