/*   1:    */ package org.springframework.expression.spel.standard;
/*   2:    */ 
/*   3:    */ import org.springframework.core.convert.TypeDescriptor;
/*   4:    */ import org.springframework.expression.EvaluationContext;
/*   5:    */ import org.springframework.expression.EvaluationException;
/*   6:    */ import org.springframework.expression.Expression;
/*   7:    */ import org.springframework.expression.TypedValue;
/*   8:    */ import org.springframework.expression.common.ExpressionUtils;
/*   9:    */ import org.springframework.expression.spel.ExpressionState;
/*  10:    */ import org.springframework.expression.spel.SpelNode;
/*  11:    */ import org.springframework.expression.spel.SpelParserConfiguration;
/*  12:    */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*  13:    */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public class SpelExpression
/*  17:    */   implements Expression
/*  18:    */ {
/*  19:    */   private final String expression;
/*  20:    */   private final SpelNodeImpl ast;
/*  21:    */   private final SpelParserConfiguration configuration;
/*  22:    */   private EvaluationContext defaultContext;
/*  23:    */   
/*  24:    */   public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration)
/*  25:    */   {
/*  26: 56 */     this.expression = expression;
/*  27: 57 */     this.ast = ast;
/*  28: 58 */     this.configuration = configuration;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Object getValue()
/*  32:    */     throws EvaluationException
/*  33:    */   {
/*  34: 65 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/*  35: 66 */     return this.ast.getValue(expressionState);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Object getValue(Object rootObject)
/*  39:    */     throws EvaluationException
/*  40:    */   {
/*  41: 70 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/*  42: 71 */     return this.ast.getValue(expressionState);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public <T> T getValue(Class<T> expectedResultType)
/*  46:    */     throws EvaluationException
/*  47:    */   {
/*  48: 75 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/*  49: 76 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/*  50: 77 */     return ExpressionUtils.convertTypedValue(expressionState.getEvaluationContext(), typedResultValue, expectedResultType);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public <T> T getValue(Object rootObject, Class<T> expectedResultType)
/*  54:    */     throws EvaluationException
/*  55:    */   {
/*  56: 81 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/*  57: 82 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/*  58: 83 */     return ExpressionUtils.convertTypedValue(expressionState.getEvaluationContext(), typedResultValue, expectedResultType);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Object getValue(EvaluationContext context)
/*  62:    */     throws EvaluationException
/*  63:    */   {
/*  64: 87 */     Assert.notNull(context, "The EvaluationContext is required");
/*  65: 88 */     return this.ast.getValue(new ExpressionState(context, this.configuration));
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Object getValue(EvaluationContext context, Object rootObject)
/*  69:    */     throws EvaluationException
/*  70:    */   {
/*  71: 92 */     Assert.notNull(context, "The EvaluationContext is required");
/*  72: 93 */     return this.ast.getValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*  73:    */   }
/*  74:    */   
/*  75:    */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType)
/*  76:    */     throws EvaluationException
/*  77:    */   {
/*  78: 97 */     TypedValue typedResultValue = this.ast.getTypedValue(new ExpressionState(context, this.configuration));
/*  79: 98 */     return ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> expectedResultType)
/*  83:    */     throws EvaluationException
/*  84:    */   {
/*  85:102 */     TypedValue typedResultValue = this.ast.getTypedValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*  86:103 */     return ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public Class getValueType()
/*  90:    */     throws EvaluationException
/*  91:    */   {
/*  92:107 */     return getValueType(getEvaluationContext());
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Class getValueType(Object rootObject)
/*  96:    */     throws EvaluationException
/*  97:    */   {
/*  98:111 */     return getValueType(getEvaluationContext(), rootObject);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Class getValueType(EvaluationContext context)
/* 102:    */     throws EvaluationException
/* 103:    */   {
/* 104:115 */     Assert.notNull(context, "The EvaluationContext is required");
/* 105:116 */     ExpressionState eState = new ExpressionState(context, this.configuration);
/* 106:117 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(eState).getTypeDescriptor();
/* 107:118 */     return typeDescriptor != null ? typeDescriptor.getType() : null;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public Class getValueType(EvaluationContext context, Object rootObject)
/* 111:    */     throws EvaluationException
/* 112:    */   {
/* 113:122 */     ExpressionState eState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 114:123 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(eState).getTypeDescriptor();
/* 115:124 */     return typeDescriptor != null ? typeDescriptor.getType() : null;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public TypeDescriptor getValueTypeDescriptor()
/* 119:    */     throws EvaluationException
/* 120:    */   {
/* 121:128 */     return getValueTypeDescriptor(getEvaluationContext());
/* 122:    */   }
/* 123:    */   
/* 124:    */   public TypeDescriptor getValueTypeDescriptor(Object rootObject)
/* 125:    */     throws EvaluationException
/* 126:    */   {
/* 127:132 */     ExpressionState eState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 128:133 */     return this.ast.getValueInternal(eState).getTypeDescriptor();
/* 129:    */   }
/* 130:    */   
/* 131:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context)
/* 132:    */     throws EvaluationException
/* 133:    */   {
/* 134:137 */     Assert.notNull(context, "The EvaluationContext is required");
/* 135:138 */     ExpressionState eState = new ExpressionState(context, this.configuration);
/* 136:139 */     return this.ast.getValueInternal(eState).getTypeDescriptor();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject)
/* 140:    */     throws EvaluationException
/* 141:    */   {
/* 142:143 */     Assert.notNull(context, "The EvaluationContext is required");
/* 143:144 */     ExpressionState eState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 144:145 */     return this.ast.getValueInternal(eState).getTypeDescriptor();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public String getExpressionString()
/* 148:    */   {
/* 149:149 */     return this.expression;
/* 150:    */   }
/* 151:    */   
/* 152:    */   public boolean isWritable(EvaluationContext context)
/* 153:    */     throws EvaluationException
/* 154:    */   {
/* 155:153 */     Assert.notNull(context, "The EvaluationContext is required");
/* 156:154 */     return this.ast.isWritable(new ExpressionState(context, this.configuration));
/* 157:    */   }
/* 158:    */   
/* 159:    */   public boolean isWritable(Object rootObject)
/* 160:    */     throws EvaluationException
/* 161:    */   {
/* 162:158 */     return this.ast.isWritable(new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration));
/* 163:    */   }
/* 164:    */   
/* 165:    */   public boolean isWritable(EvaluationContext context, Object rootObject)
/* 166:    */     throws EvaluationException
/* 167:    */   {
/* 168:162 */     Assert.notNull(context, "The EvaluationContext is required");
/* 169:163 */     return this.ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/* 170:    */   }
/* 171:    */   
/* 172:    */   public void setValue(EvaluationContext context, Object value)
/* 173:    */     throws EvaluationException
/* 174:    */   {
/* 175:167 */     Assert.notNull(context, "The EvaluationContext is required");
/* 176:168 */     this.ast.setValue(new ExpressionState(context, this.configuration), value);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public void setValue(Object rootObject, Object value)
/* 180:    */     throws EvaluationException
/* 181:    */   {
/* 182:172 */     this.ast.setValue(new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration), value);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public void setValue(EvaluationContext context, Object rootObject, Object value)
/* 186:    */     throws EvaluationException
/* 187:    */   {
/* 188:176 */     Assert.notNull(context, "The EvaluationContext is required");
/* 189:177 */     this.ast.setValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration), value);
/* 190:    */   }
/* 191:    */   
/* 192:    */   public SpelNode getAST()
/* 193:    */   {
/* 194:186 */     return this.ast;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public String toStringAST()
/* 198:    */   {
/* 199:196 */     return this.ast.toStringAST();
/* 200:    */   }
/* 201:    */   
/* 202:    */   public EvaluationContext getEvaluationContext()
/* 203:    */   {
/* 204:204 */     if (this.defaultContext == null) {
/* 205:205 */       this.defaultContext = new StandardEvaluationContext();
/* 206:    */     }
/* 207:207 */     return this.defaultContext;
/* 208:    */   }
/* 209:    */   
/* 210:    */   public void setEvaluationContext(EvaluationContext context)
/* 211:    */   {
/* 212:215 */     this.defaultContext = context;
/* 213:    */   }
/* 214:    */   
/* 215:    */   private TypedValue toTypedValue(Object object)
/* 216:    */   {
/* 217:219 */     if (object == null) {
/* 218:220 */       return TypedValue.NULL;
/* 219:    */     }
/* 220:223 */     return new TypedValue(object);
/* 221:    */   }
/* 222:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.SpelExpression
 * JD-Core Version:    0.7.0.1
 */