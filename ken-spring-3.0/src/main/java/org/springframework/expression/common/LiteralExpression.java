/*   1:    */ package org.springframework.expression.common;
/*   2:    */ 
/*   3:    */ import org.springframework.core.convert.TypeDescriptor;
/*   4:    */ import org.springframework.expression.EvaluationContext;
/*   5:    */ import org.springframework.expression.EvaluationException;
/*   6:    */ import org.springframework.expression.Expression;
/*   7:    */ 
/*   8:    */ public class LiteralExpression
/*   9:    */   implements Expression
/*  10:    */ {
/*  11:    */   private final String literalValue;
/*  12:    */   
/*  13:    */   public LiteralExpression(String literalValue)
/*  14:    */   {
/*  15: 40 */     this.literalValue = literalValue;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public final String getExpressionString()
/*  19:    */   {
/*  20: 45 */     return this.literalValue;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getValue()
/*  24:    */   {
/*  25: 49 */     return this.literalValue;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getValue(EvaluationContext context)
/*  29:    */   {
/*  30: 53 */     return this.literalValue;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getValue(Object rootObject)
/*  34:    */   {
/*  35: 57 */     return this.literalValue;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public Class getValueType(EvaluationContext context)
/*  39:    */   {
/*  40: 61 */     return String.class;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context)
/*  44:    */   {
/*  45: 65 */     return TypeDescriptor.valueOf(String.class);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public TypeDescriptor getValueTypeDescriptor()
/*  49:    */   {
/*  50: 69 */     return TypeDescriptor.valueOf(String.class);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setValue(EvaluationContext context, Object value)
/*  54:    */     throws EvaluationException
/*  55:    */   {
/*  56: 73 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/*  57:    */   }
/*  58:    */   
/*  59:    */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType)
/*  60:    */     throws EvaluationException
/*  61:    */   {
/*  62: 77 */     Object value = getValue(context);
/*  63: 78 */     return ExpressionUtils.convert(context, value, expectedResultType);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public <T> T getValue(Class<T> expectedResultType)
/*  67:    */     throws EvaluationException
/*  68:    */   {
/*  69: 82 */     Object value = getValue();
/*  70: 83 */     return ExpressionUtils.convert(null, value, expectedResultType);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean isWritable(EvaluationContext context)
/*  74:    */   {
/*  75: 87 */     return false;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Class getValueType()
/*  79:    */   {
/*  80: 91 */     return String.class;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public <T> T getValue(Object rootObject, Class<T> desiredResultType)
/*  84:    */     throws EvaluationException
/*  85:    */   {
/*  86: 95 */     Object value = getValue(rootObject);
/*  87: 96 */     return ExpressionUtils.convert(null, value, desiredResultType);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public String getValue(EvaluationContext context, Object rootObject)
/*  91:    */     throws EvaluationException
/*  92:    */   {
/*  93:100 */     return this.literalValue;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> desiredResultType)
/*  97:    */     throws EvaluationException
/*  98:    */   {
/*  99:104 */     Object value = getValue(context, rootObject);
/* 100:105 */     return ExpressionUtils.convert(null, value, desiredResultType);
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Class getValueType(Object rootObject)
/* 104:    */     throws EvaluationException
/* 105:    */   {
/* 106:109 */     return String.class;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public Class getValueType(EvaluationContext context, Object rootObject)
/* 110:    */     throws EvaluationException
/* 111:    */   {
/* 112:113 */     return String.class;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public TypeDescriptor getValueTypeDescriptor(Object rootObject)
/* 116:    */     throws EvaluationException
/* 117:    */   {
/* 118:117 */     return TypeDescriptor.valueOf(String.class);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject)
/* 122:    */     throws EvaluationException
/* 123:    */   {
/* 124:121 */     return TypeDescriptor.valueOf(String.class);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public boolean isWritable(EvaluationContext context, Object rootObject)
/* 128:    */     throws EvaluationException
/* 129:    */   {
/* 130:125 */     return false;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public void setValue(EvaluationContext context, Object rootObject, Object value)
/* 134:    */     throws EvaluationException
/* 135:    */   {
/* 136:129 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean isWritable(Object rootObject)
/* 140:    */     throws EvaluationException
/* 141:    */   {
/* 142:133 */     return false;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public void setValue(Object rootObject, Object value)
/* 146:    */     throws EvaluationException
/* 147:    */   {
/* 148:137 */     throw new EvaluationException(this.literalValue, "Cannot call setValue() on a LiteralExpression");
/* 149:    */   }
/* 150:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.common.LiteralExpression
 * JD-Core Version:    0.7.0.1
 */