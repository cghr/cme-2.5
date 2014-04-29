/*   1:    */ package org.springframework.expression.spel;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Stack;
/*   7:    */ import org.springframework.core.convert.TypeDescriptor;
/*   8:    */ import org.springframework.expression.EvaluationContext;
/*   9:    */ import org.springframework.expression.EvaluationException;
/*  10:    */ import org.springframework.expression.Operation;
/*  11:    */ import org.springframework.expression.OperatorOverloader;
/*  12:    */ import org.springframework.expression.PropertyAccessor;
/*  13:    */ import org.springframework.expression.TypeComparator;
/*  14:    */ import org.springframework.expression.TypeConverter;
/*  15:    */ import org.springframework.expression.TypeLocator;
/*  16:    */ import org.springframework.expression.TypedValue;
/*  17:    */ 
/*  18:    */ public class ExpressionState
/*  19:    */ {
/*  20:    */   private final EvaluationContext relatedContext;
/*  21:    */   private Stack<VariableScope> variableScopes;
/*  22:    */   private Stack<TypedValue> contextObjects;
/*  23:    */   private final TypedValue rootObject;
/*  24:    */   private SpelParserConfiguration configuration;
/*  25:    */   
/*  26:    */   public ExpressionState(EvaluationContext context)
/*  27:    */   {
/*  28: 58 */     this.relatedContext = context;
/*  29: 59 */     this.rootObject = context.getRootObject();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration)
/*  33:    */   {
/*  34: 63 */     this.relatedContext = context;
/*  35: 64 */     this.configuration = configuration;
/*  36: 65 */     this.rootObject = context.getRootObject();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ExpressionState(EvaluationContext context, TypedValue rootObject)
/*  40:    */   {
/*  41: 69 */     this.relatedContext = context;
/*  42: 70 */     this.rootObject = rootObject;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration)
/*  46:    */   {
/*  47: 74 */     this.relatedContext = context;
/*  48: 75 */     this.configuration = configuration;
/*  49: 76 */     this.rootObject = rootObject;
/*  50:    */   }
/*  51:    */   
/*  52:    */   private void ensureVariableScopesInitialized()
/*  53:    */   {
/*  54: 81 */     if (this.variableScopes == null)
/*  55:    */     {
/*  56: 82 */       this.variableScopes = new Stack();
/*  57:    */       
/*  58: 84 */       this.variableScopes.add(new VariableScope());
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public TypedValue getActiveContextObject()
/*  63:    */   {
/*  64: 92 */     if ((this.contextObjects == null) || (this.contextObjects.isEmpty())) {
/*  65: 93 */       return this.rootObject;
/*  66:    */     }
/*  67: 96 */     return (TypedValue)this.contextObjects.peek();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void pushActiveContextObject(TypedValue obj)
/*  71:    */   {
/*  72:100 */     if (this.contextObjects == null) {
/*  73:101 */       this.contextObjects = new Stack();
/*  74:    */     }
/*  75:103 */     this.contextObjects.push(obj);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void popActiveContextObject()
/*  79:    */   {
/*  80:107 */     if (this.contextObjects == null) {
/*  81:108 */       this.contextObjects = new Stack();
/*  82:    */     }
/*  83:110 */     this.contextObjects.pop();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public TypedValue getRootContextObject()
/*  87:    */   {
/*  88:114 */     return this.rootObject;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setVariable(String name, Object value)
/*  92:    */   {
/*  93:118 */     this.relatedContext.setVariable(name, value);
/*  94:    */   }
/*  95:    */   
/*  96:    */   public TypedValue lookupVariable(String name)
/*  97:    */   {
/*  98:122 */     Object value = this.relatedContext.lookupVariable(name);
/*  99:123 */     if (value == null) {
/* 100:124 */       return TypedValue.NULL;
/* 101:    */     }
/* 102:127 */     return new TypedValue(value);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public TypeComparator getTypeComparator()
/* 106:    */   {
/* 107:132 */     return this.relatedContext.getTypeComparator();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public Class<?> findType(String type)
/* 111:    */     throws EvaluationException
/* 112:    */   {
/* 113:136 */     return this.relatedContext.getTypeLocator().findType(type);
/* 114:    */   }
/* 115:    */   
/* 116:    */   public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor)
/* 117:    */     throws EvaluationException
/* 118:    */   {
/* 119:140 */     return this.relatedContext.getTypeConverter().convertValue(value, TypeDescriptor.forObject(value), targetTypeDescriptor);
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor)
/* 123:    */     throws EvaluationException
/* 124:    */   {
/* 125:144 */     Object val = value.getValue();
/* 126:145 */     return this.relatedContext.getTypeConverter().convertValue(val, TypeDescriptor.forObject(val), targetTypeDescriptor);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void enterScope(Map<String, Object> argMap)
/* 130:    */   {
/* 131:153 */     ensureVariableScopesInitialized();
/* 132:154 */     this.variableScopes.push(new VariableScope(argMap));
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void enterScope(String name, Object value)
/* 136:    */   {
/* 137:158 */     ensureVariableScopesInitialized();
/* 138:159 */     this.variableScopes.push(new VariableScope(name, value));
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void exitScope()
/* 142:    */   {
/* 143:163 */     ensureVariableScopesInitialized();
/* 144:164 */     this.variableScopes.pop();
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void setLocalVariable(String name, Object value)
/* 148:    */   {
/* 149:168 */     ensureVariableScopesInitialized();
/* 150:169 */     ((VariableScope)this.variableScopes.peek()).setVariable(name, value);
/* 151:    */   }
/* 152:    */   
/* 153:    */   public Object lookupLocalVariable(String name)
/* 154:    */   {
/* 155:173 */     ensureVariableScopesInitialized();
/* 156:174 */     int scopeNumber = this.variableScopes.size() - 1;
/* 157:175 */     for (int i = scopeNumber; i >= 0; i--) {
/* 158:176 */       if (((VariableScope)this.variableScopes.get(i)).definesVariable(name)) {
/* 159:177 */         return ((VariableScope)this.variableScopes.get(i)).lookupVariable(name);
/* 160:    */       }
/* 161:    */     }
/* 162:180 */     return null;
/* 163:    */   }
/* 164:    */   
/* 165:    */   public TypedValue operate(Operation op, Object left, Object right)
/* 166:    */     throws EvaluationException
/* 167:    */   {
/* 168:184 */     OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
/* 169:185 */     if (overloader.overridesOperation(op, left, right))
/* 170:    */     {
/* 171:186 */       Object returnValue = overloader.operate(op, left, right);
/* 172:187 */       return new TypedValue(returnValue);
/* 173:    */     }
/* 174:190 */     String leftType = left == null ? "null" : left.getClass().getName();
/* 175:191 */     String rightType = right == null ? "null" : right.getClass().getName();
/* 176:192 */     throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, new Object[] { op, leftType, rightType });
/* 177:    */   }
/* 178:    */   
/* 179:    */   public List<PropertyAccessor> getPropertyAccessors()
/* 180:    */   {
/* 181:197 */     return this.relatedContext.getPropertyAccessors();
/* 182:    */   }
/* 183:    */   
/* 184:    */   public EvaluationContext getEvaluationContext()
/* 185:    */   {
/* 186:201 */     return this.relatedContext;
/* 187:    */   }
/* 188:    */   
/* 189:    */   public SpelParserConfiguration getConfiguration()
/* 190:    */   {
/* 191:205 */     return this.configuration;
/* 192:    */   }
/* 193:    */   
/* 194:    */   private static class VariableScope
/* 195:    */   {
/* 196:215 */     private final Map<String, Object> vars = new HashMap();
/* 197:    */     
/* 198:    */     public VariableScope() {}
/* 199:    */     
/* 200:    */     public VariableScope(Map<String, Object> arguments)
/* 201:    */     {
/* 202:220 */       if (arguments != null) {
/* 203:221 */         this.vars.putAll(arguments);
/* 204:    */       }
/* 205:    */     }
/* 206:    */     
/* 207:    */     public VariableScope(String name, Object value)
/* 208:    */     {
/* 209:226 */       this.vars.put(name, value);
/* 210:    */     }
/* 211:    */     
/* 212:    */     public Object lookupVariable(String name)
/* 213:    */     {
/* 214:230 */       return this.vars.get(name);
/* 215:    */     }
/* 216:    */     
/* 217:    */     public void setVariable(String name, Object value)
/* 218:    */     {
/* 219:234 */       this.vars.put(name, value);
/* 220:    */     }
/* 221:    */     
/* 222:    */     public boolean definesVariable(String name)
/* 223:    */     {
/* 224:238 */       return this.vars.containsKey(name);
/* 225:    */     }
/* 226:    */   }
/* 227:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ExpressionState
 * JD-Core Version:    0.7.0.1
 */