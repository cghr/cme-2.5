/*   1:    */ package org.springframework.expression.spel.ast;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.lang.reflect.Modifier;
/*   5:    */ import org.springframework.core.MethodParameter;
/*   6:    */ import org.springframework.core.convert.TypeDescriptor;
/*   7:    */ import org.springframework.expression.EvaluationContext;
/*   8:    */ import org.springframework.expression.EvaluationException;
/*   9:    */ import org.springframework.expression.TypeConverter;
/*  10:    */ import org.springframework.expression.TypedValue;
/*  11:    */ import org.springframework.expression.spel.ExpressionState;
/*  12:    */ import org.springframework.expression.spel.SpelEvaluationException;
/*  13:    */ import org.springframework.expression.spel.SpelMessage;
/*  14:    */ import org.springframework.expression.spel.SpelNode;
/*  15:    */ import org.springframework.expression.spel.support.ReflectionHelper;
/*  16:    */ import org.springframework.util.ReflectionUtils;
/*  17:    */ 
/*  18:    */ public class FunctionReference
/*  19:    */   extends SpelNodeImpl
/*  20:    */ {
/*  21:    */   private final String name;
/*  22:    */   
/*  23:    */   public FunctionReference(String functionName, int pos, SpelNodeImpl... arguments)
/*  24:    */   {
/*  25: 51 */     super(pos, arguments);
/*  26: 52 */     this.name = functionName;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public TypedValue getValueInternal(ExpressionState state)
/*  30:    */     throws EvaluationException
/*  31:    */   {
/*  32: 57 */     TypedValue o = state.lookupVariable(this.name);
/*  33: 58 */     if (o == null) {
/*  34: 59 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_NOT_DEFINED, new Object[] { this.name });
/*  35:    */     }
/*  36: 63 */     if (!(o.getValue() instanceof Method)) {
/*  37: 64 */       throw new SpelEvaluationException(SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, new Object[] { this.name, o.getClass() });
/*  38:    */     }
/*  39:    */     try
/*  40:    */     {
/*  41: 67 */       return executeFunctionJLRMethod(state, (Method)o.getValue());
/*  42:    */     }
/*  43:    */     catch (SpelEvaluationException se)
/*  44:    */     {
/*  45: 70 */       se.setPosition(getStartPosition());
/*  46: 71 */       throw se;
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   private TypedValue executeFunctionJLRMethod(ExpressionState state, Method method)
/*  51:    */     throws EvaluationException
/*  52:    */   {
/*  53: 84 */     Object[] functionArgs = getArguments(state);
/*  54: 86 */     if ((!method.isVarArgs()) && (method.getParameterTypes().length != functionArgs.length)) {
/*  55: 87 */       throw new SpelEvaluationException(SpelMessage.INCORRECT_NUMBER_OF_ARGUMENTS_TO_FUNCTION, new Object[] {
/*  56: 88 */         Integer.valueOf(functionArgs.length), Integer.valueOf(method.getParameterTypes().length) });
/*  57:    */     }
/*  58: 91 */     if (!Modifier.isStatic(method.getModifiers())) {
/*  59: 92 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_MUST_BE_STATIC, new Object[] {
/*  60: 93 */         method.getDeclaringClass().getName() + 
/*  61: 94 */         "." + method.getName(), this.name });
/*  62:    */     }
/*  63: 98 */     if (functionArgs != null)
/*  64:    */     {
/*  65: 99 */       TypeConverter converter = state.getEvaluationContext().getTypeConverter();
/*  66:100 */       ReflectionHelper.convertAllArguments(converter, functionArgs, method);
/*  67:    */     }
/*  68:102 */     if (method.isVarArgs()) {
/*  69:103 */       functionArgs = ReflectionHelper.setupArgumentsForVarargsInvocation(method.getParameterTypes(), functionArgs);
/*  70:    */     }
/*  71:    */     try
/*  72:    */     {
/*  73:107 */       ReflectionUtils.makeAccessible(method);
/*  74:108 */       Object result = method.invoke(method.getClass(), functionArgs);
/*  75:109 */       return new TypedValue(result, new TypeDescriptor(new MethodParameter(method, -1)).narrow(result));
/*  76:    */     }
/*  77:    */     catch (Exception ex)
/*  78:    */     {
/*  79:112 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_FUNCTION_CALL, new Object[] {
/*  80:113 */         this.name, ex.getMessage() });
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public String toStringAST()
/*  85:    */   {
/*  86:119 */     StringBuilder sb = new StringBuilder("#").append(this.name);
/*  87:120 */     sb.append("(");
/*  88:121 */     for (int i = 0; i < getChildCount(); i++)
/*  89:    */     {
/*  90:122 */       if (i > 0) {
/*  91:123 */         sb.append(",");
/*  92:    */       }
/*  93:124 */       sb.append(getChild(i).toStringAST());
/*  94:    */     }
/*  95:126 */     sb.append(")");
/*  96:127 */     return sb.toString();
/*  97:    */   }
/*  98:    */   
/*  99:    */   private Object[] getArguments(ExpressionState state)
/* 100:    */     throws EvaluationException
/* 101:    */   {
/* 102:138 */     Object[] arguments = new Object[getChildCount()];
/* 103:139 */     for (int i = 0; i < arguments.length; i++) {
/* 104:140 */       arguments[i] = this.children[i].getValueInternal(state).getValue();
/* 105:    */     }
/* 106:142 */     return arguments;
/* 107:    */   }
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.FunctionReference
 * JD-Core Version:    0.7.0.1
 */