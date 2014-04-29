/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.core.convert.TypeDescriptor;
/*   7:    */ import org.springframework.expression.AccessException;
/*   8:    */ import org.springframework.expression.EvaluationContext;
/*   9:    */ import org.springframework.expression.EvaluationException;
/*  10:    */ import org.springframework.expression.ExpressionInvocationTargetException;
/*  11:    */ import org.springframework.expression.MethodExecutor;
/*  12:    */ import org.springframework.expression.MethodResolver;
/*  13:    */ import org.springframework.expression.TypedValue;
/*  14:    */ import org.springframework.expression.spel.ExpressionState;
/*  15:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  16:    */ import org.springframework.expression.spel.SpelMessage;
/*  17:    */ import org.springframework.expression.spel.SpelNode;
/*  18:    */ 
/*  19:    */ public class MethodReference
/*  20:    */   extends SpelNodeImpl
/*  21:    */ {
/*  22:    */   private final String name;
/*  23:    */   private final boolean nullSafe;
/*  24:    */   private volatile MethodExecutor cachedExecutor;
/*  25:    */   
/*  26:    */   public MethodReference(boolean nullSafe, String methodName, int pos, SpelNodeImpl... arguments)
/*  27:    */   {
/*  28: 50 */     super(pos, arguments);
/*  29: 51 */     this.name = methodName;
/*  30: 52 */     this.nullSafe = nullSafe;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public TypedValue getValueInternal(ExpressionState state)
/*  34:    */     throws EvaluationException
/*  35:    */   {
/*  36: 58 */     TypedValue currentContext = state.getActiveContextObject();
/*  37: 59 */     Object[] arguments = new Object[getChildCount()];
/*  38: 60 */     for (int i = 0; i < arguments.length; i++) {
/*  39:    */       try
/*  40:    */       {
/*  41: 64 */         state.pushActiveContextObject(state.getRootContextObject());
/*  42: 65 */         arguments[i] = this.children[i].getValueInternal(state).getValue();
/*  43:    */       }
/*  44:    */       finally
/*  45:    */       {
/*  46: 68 */         state.popActiveContextObject();
/*  47:    */       }
/*  48:    */     }
/*  49: 71 */     if (currentContext.getValue() == null)
/*  50:    */     {
/*  51: 72 */       if (this.nullSafe) {
/*  52: 73 */         return TypedValue.NULL;
/*  53:    */       }
/*  54: 76 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED, new Object[] {
/*  55: 77 */         FormatHelper.formatMethodForMessage(this.name, getTypes(arguments)) });
/*  56:    */     }
/*  57: 81 */     MethodExecutor executorToUse = this.cachedExecutor;
/*  58: 82 */     if (executorToUse != null) {
/*  59:    */       try
/*  60:    */       {
/*  61: 84 */         return executorToUse.execute(
/*  62: 85 */           state.getEvaluationContext(), state.getActiveContextObject().getValue(), arguments);
/*  63:    */       }
/*  64:    */       catch (AccessException ae)
/*  65:    */       {
/*  66: 98 */         throwSimpleExceptionIfPossible(state, ae);
/*  67:    */         
/*  68:    */ 
/*  69:101 */         this.cachedExecutor = null;
/*  70:    */       }
/*  71:    */     }
/*  72:106 */     executorToUse = findAccessorForMethod(this.name, getTypes(arguments), state);
/*  73:107 */     this.cachedExecutor = executorToUse;
/*  74:    */     try
/*  75:    */     {
/*  76:109 */       return executorToUse.execute(
/*  77:110 */         state.getEvaluationContext(), state.getActiveContextObject().getValue(), arguments);
/*  78:    */     }
/*  79:    */     catch (AccessException ae)
/*  80:    */     {
/*  81:113 */       throwSimpleExceptionIfPossible(state, ae);
/*  82:114 */       throw new SpelEvaluationException(getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, new Object[] {
/*  83:115 */         this.name, state.getActiveContextObject().getValue().getClass().getName(), ae.getMessage() });
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   private void throwSimpleExceptionIfPossible(ExpressionState state, AccessException ae)
/*  88:    */   {
/*  89:125 */     if ((ae.getCause() instanceof InvocationTargetException))
/*  90:    */     {
/*  91:126 */       Throwable rootCause = ae.getCause().getCause();
/*  92:127 */       if ((rootCause instanceof RuntimeException)) {
/*  93:128 */         throw ((RuntimeException)rootCause);
/*  94:    */       }
/*  95:131 */       throw new ExpressionInvocationTargetException(getStartPosition(), 
/*  96:132 */         "A problem occurred when trying to execute method '" + this.name + 
/*  97:133 */         "' on object of type '" + state.getActiveContextObject().getValue().getClass().getName() + "'", 
/*  98:134 */         rootCause);
/*  99:    */     }
/* 100:    */   }
/* 101:    */   
/* 102:    */   private List<TypeDescriptor> getTypes(Object... arguments)
/* 103:    */   {
/* 104:140 */     List<TypeDescriptor> descriptors = new ArrayList(arguments.length);
/* 105:141 */     for (Object argument : arguments) {
/* 106:142 */       descriptors.add(TypeDescriptor.forObject(argument));
/* 107:    */     }
/* 108:144 */     return descriptors;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public String toStringAST()
/* 112:    */   {
/* 113:149 */     StringBuilder sb = new StringBuilder();
/* 114:150 */     sb.append(this.name).append("(");
/* 115:151 */     for (int i = 0; i < getChildCount(); i++)
/* 116:    */     {
/* 117:152 */       if (i > 0) {
/* 118:153 */         sb.append(",");
/* 119:    */       }
/* 120:154 */       sb.append(getChild(i).toStringAST());
/* 121:    */     }
/* 122:156 */     sb.append(")");
/* 123:157 */     return sb.toString();
/* 124:    */   }
/* 125:    */   
/* 126:    */   private MethodExecutor findAccessorForMethod(String name, List<TypeDescriptor> argumentTypes, ExpressionState state)
/* 127:    */     throws SpelEvaluationException
/* 128:    */   {
/* 129:163 */     TypedValue context = state.getActiveContextObject();
/* 130:164 */     Object contextObject = context.getValue();
/* 131:165 */     EvaluationContext eContext = state.getEvaluationContext();
/* 132:    */     
/* 133:167 */     List<MethodResolver> mResolvers = eContext.getMethodResolvers();
/* 134:168 */     if (mResolvers != null) {
/* 135:169 */       for (MethodResolver methodResolver : mResolvers) {
/* 136:    */         try
/* 137:    */         {
/* 138:171 */           MethodExecutor cEx = methodResolver.resolve(
/* 139:172 */             state.getEvaluationContext(), contextObject, name, argumentTypes);
/* 140:173 */           if (cEx != null) {
/* 141:174 */             return cEx;
/* 142:    */           }
/* 143:    */         }
/* 144:    */         catch (AccessException ex)
/* 145:    */         {
/* 146:178 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.PROBLEM_LOCATING_METHOD, new Object[] { name, contextObject.getClass() });
/* 147:    */         }
/* 148:    */       }
/* 149:    */     }
/* 150:182 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_NOT_FOUND, new Object[] { FormatHelper.formatMethodForMessage(name, argumentTypes), 
/* 151:183 */       FormatHelper.formatClassNameForMessage((contextObject instanceof Class) ? (Class)contextObject : contextObject.getClass()) });
/* 152:    */   }
/* 153:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.MethodReference
 * JD-Core Version:    0.7.0.1
 */