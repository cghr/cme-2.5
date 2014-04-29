/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletContext;
/*   4:    */ import javax.servlet.jsp.JspException;
/*   5:    */ import javax.servlet.jsp.PageContext;
/*   6:    */ import javax.servlet.jsp.el.ELException;
/*   7:    */ import javax.servlet.jsp.el.ExpressionEvaluator;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public abstract class ExpressionEvaluationUtils
/*  11:    */ {
/*  12:    */   public static final String EXPRESSION_SUPPORT_CONTEXT_PARAM = "springJspExpressionSupport";
/*  13:    */   public static final String EXPRESSION_PREFIX = "${";
/*  14:    */   public static final String EXPRESSION_SUFFIX = "}";
/*  15:    */   
/*  16:    */   public static boolean isSpringJspExpressionSupportActive(PageContext pageContext)
/*  17:    */   {
/*  18: 80 */     ServletContext sc = pageContext.getServletContext();
/*  19: 81 */     String springJspExpressionSupport = sc.getInitParameter("springJspExpressionSupport");
/*  20: 82 */     if (springJspExpressionSupport != null) {
/*  21: 83 */       return Boolean.valueOf(springJspExpressionSupport).booleanValue();
/*  22:    */     }
/*  23: 85 */     if (sc.getMajorVersion() >= 3) {
/*  24: 87 */       if ((sc.getEffectiveMajorVersion() > 2) || (sc.getEffectiveMinorVersion() > 3)) {
/*  25: 90 */         return false;
/*  26:    */       }
/*  27:    */     }
/*  28: 93 */     return true;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public static boolean isExpressionLanguage(String value)
/*  32:    */   {
/*  33:103 */     return (value != null) && (value.contains("${"));
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
/*  37:    */     throws JspException
/*  38:    */   {
/*  39:121 */     if ((isSpringJspExpressionSupportActive(pageContext)) && (isExpressionLanguage(attrValue))) {
/*  40:122 */       return doEvaluate(attrName, attrValue, resultClass, pageContext);
/*  41:    */     }
/*  42:124 */     if ((attrValue != null) && (resultClass != null) && (!resultClass.isInstance(attrValue))) {
/*  43:125 */       throw new JspException("Attribute value \"" + attrValue + "\" is neither a JSP EL expression nor " + 
/*  44:126 */         "assignable to result class [" + resultClass.getName() + "]");
/*  45:    */     }
/*  46:129 */     return attrValue;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static Object evaluate(String attrName, String attrValue, PageContext pageContext)
/*  50:    */     throws JspException
/*  51:    */   {
/*  52:144 */     if ((isSpringJspExpressionSupportActive(pageContext)) && (isExpressionLanguage(attrValue))) {
/*  53:145 */       return doEvaluate(attrName, attrValue, Object.class, pageContext);
/*  54:    */     }
/*  55:148 */     return attrValue;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public static String evaluateString(String attrName, String attrValue, PageContext pageContext)
/*  59:    */     throws JspException
/*  60:    */   {
/*  61:163 */     if ((isSpringJspExpressionSupportActive(pageContext)) && (isExpressionLanguage(attrValue))) {
/*  62:164 */       return (String)doEvaluate(attrName, attrValue, String.class, pageContext);
/*  63:    */     }
/*  64:167 */     return attrValue;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static int evaluateInteger(String attrName, String attrValue, PageContext pageContext)
/*  68:    */     throws JspException
/*  69:    */   {
/*  70:182 */     if ((isSpringJspExpressionSupportActive(pageContext)) && (isExpressionLanguage(attrValue))) {
/*  71:183 */       return ((Integer)doEvaluate(attrName, attrValue, Integer.class, pageContext)).intValue();
/*  72:    */     }
/*  73:186 */     return Integer.parseInt(attrValue);
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static boolean evaluateBoolean(String attrName, String attrValue, PageContext pageContext)
/*  77:    */     throws JspException
/*  78:    */   {
/*  79:201 */     if ((isSpringJspExpressionSupportActive(pageContext)) && (isExpressionLanguage(attrValue))) {
/*  80:202 */       return ((Boolean)doEvaluate(attrName, attrValue, Boolean.class, pageContext)).booleanValue();
/*  81:    */     }
/*  82:205 */     return Boolean.valueOf(attrValue).booleanValue();
/*  83:    */   }
/*  84:    */   
/*  85:    */   private static Object doEvaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
/*  86:    */     throws JspException
/*  87:    */   {
/*  88:223 */     Assert.notNull(attrValue, "Attribute value must not be null");
/*  89:224 */     Assert.notNull(resultClass, "Result class must not be null");
/*  90:225 */     Assert.notNull(pageContext, "PageContext must not be null");
/*  91:    */     try
/*  92:    */     {
/*  93:228 */       if (resultClass.isAssignableFrom(String.class))
/*  94:    */       {
/*  95:229 */         StringBuilder resultValue = null;
/*  96:    */         
/*  97:231 */         int exprSuffixIndex = 0;
/*  98:    */         int exprPrefixIndex;
/*  99:    */         do
/* 100:    */         {
/* 101:233 */           exprPrefixIndex = attrValue.indexOf("${", exprSuffixIndex);
/* 102:234 */           if (exprPrefixIndex != -1)
/* 103:    */           {
/* 104:235 */             int prevExprSuffixIndex = exprSuffixIndex;
/* 105:236 */             exprSuffixIndex = attrValue.indexOf("}", exprPrefixIndex + "${".length());
/* 106:    */             String expr;
/* 107:    */             String expr;
/* 108:238 */             if (exprSuffixIndex != -1)
/* 109:    */             {
/* 110:239 */               exprSuffixIndex += "}".length();
/* 111:240 */               expr = attrValue.substring(exprPrefixIndex, exprSuffixIndex);
/* 112:    */             }
/* 113:    */             else
/* 114:    */             {
/* 115:243 */               expr = attrValue.substring(exprPrefixIndex);
/* 116:    */             }
/* 117:245 */             if (expr.length() == attrValue.length()) {
/* 118:248 */               return evaluateExpression(attrValue, resultClass, pageContext);
/* 119:    */             }
/* 120:252 */             if (resultValue == null) {
/* 121:253 */               resultValue = new StringBuilder();
/* 122:    */             }
/* 123:255 */             resultValue.append(attrValue.substring(prevExprSuffixIndex, exprPrefixIndex));
/* 124:256 */             resultValue.append(evaluateExpression(expr, String.class, pageContext));
/* 125:    */           }
/* 126:    */           else
/* 127:    */           {
/* 128:260 */             if (resultValue == null) {
/* 129:261 */               resultValue = new StringBuilder();
/* 130:    */             }
/* 131:263 */             resultValue.append(attrValue.substring(exprSuffixIndex));
/* 132:    */           }
/* 133:266 */         } while ((exprPrefixIndex != -1) && (exprSuffixIndex != -1));
/* 134:267 */         return resultValue.toString();
/* 135:    */       }
/* 136:270 */       return evaluateExpression(attrValue, resultClass, pageContext);
/* 137:    */     }
/* 138:    */     catch (ELException ex)
/* 139:    */     {
/* 140:274 */       throw new JspException("Parsing of JSP EL expression failed for attribute '" + attrName + "'", ex);
/* 141:    */     }
/* 142:    */   }
/* 143:    */   
/* 144:    */   private static Object evaluateExpression(String exprValue, Class resultClass, PageContext pageContext)
/* 145:    */     throws ELException
/* 146:    */   {
/* 147:281 */     return pageContext.getExpressionEvaluator().evaluate(
/* 148:282 */       exprValue, resultClass, pageContext.getVariableResolver(), null);
/* 149:    */   }
/* 150:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.ExpressionEvaluationUtils
 * JD-Core Version:    0.7.0.1
 */