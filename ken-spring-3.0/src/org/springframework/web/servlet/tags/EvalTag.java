/*   1:    */ package org.springframework.web.servlet.tags;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.ServletRequest;
/*   5:    */ import javax.servlet.jsp.JspException;
/*   6:    */ import javax.servlet.jsp.JspWriter;
/*   7:    */ import javax.servlet.jsp.PageContext;
/*   8:    */ import javax.servlet.jsp.el.VariableResolver;
/*   9:    */ import org.springframework.context.expression.BeanFactoryResolver;
/*  10:    */ import org.springframework.context.expression.EnvironmentAccessor;
/*  11:    */ import org.springframework.context.expression.MapAccessor;
/*  12:    */ import org.springframework.core.convert.ConversionService;
/*  13:    */ import org.springframework.expression.AccessException;
/*  14:    */ import org.springframework.expression.EvaluationContext;
/*  15:    */ import org.springframework.expression.Expression;
/*  16:    */ import org.springframework.expression.ExpressionParser;
/*  17:    */ import org.springframework.expression.PropertyAccessor;
/*  18:    */ import org.springframework.expression.TypedValue;
/*  19:    */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*  20:    */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*  21:    */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*  22:    */ import org.springframework.util.ObjectUtils;
/*  23:    */ import org.springframework.web.servlet.support.RequestContext;
/*  24:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*  25:    */ import org.springframework.web.util.HtmlUtils;
/*  26:    */ import org.springframework.web.util.JavaScriptUtils;
/*  27:    */ import org.springframework.web.util.TagUtils;
/*  28:    */ 
/*  29:    */ public class EvalTag
/*  30:    */   extends HtmlEscapingAwareTag
/*  31:    */ {
/*  32:    */   private static final String EVALUATION_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.EVALUATION_CONTEXT";
/*  33: 62 */   private final ExpressionParser expressionParser = new SpelExpressionParser();
/*  34:    */   private Expression expression;
/*  35:    */   private String var;
/*  36: 68 */   private int scope = 1;
/*  37: 70 */   private boolean javaScriptEscape = false;
/*  38:    */   
/*  39:    */   public void setExpression(String expression)
/*  40:    */   {
/*  41: 77 */     this.expression = this.expressionParser.parseExpression(expression);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setVar(String var)
/*  45:    */   {
/*  46: 85 */     this.var = var;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setScope(String scope)
/*  50:    */   {
/*  51: 93 */     this.scope = TagUtils.getScope(scope);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setJavaScriptEscape(String javaScriptEscape)
/*  55:    */     throws JspException
/*  56:    */   {
/*  57:101 */     this.javaScriptEscape = 
/*  58:102 */       ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, this.pageContext);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public int doStartTagInternal()
/*  62:    */     throws JspException
/*  63:    */   {
/*  64:108 */     return 1;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int doEndTag()
/*  68:    */     throws JspException
/*  69:    */   {
/*  70:113 */     EvaluationContext evaluationContext = 
/*  71:114 */       (EvaluationContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT");
/*  72:115 */     if (evaluationContext == null)
/*  73:    */     {
/*  74:116 */       evaluationContext = createEvaluationContext(this.pageContext);
/*  75:117 */       this.pageContext.setAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT", evaluationContext);
/*  76:    */     }
/*  77:119 */     if (this.var != null)
/*  78:    */     {
/*  79:120 */       Object result = this.expression.getValue(evaluationContext);
/*  80:121 */       this.pageContext.setAttribute(this.var, result, this.scope);
/*  81:    */     }
/*  82:    */     else
/*  83:    */     {
/*  84:    */       try
/*  85:    */       {
/*  86:125 */         String result = (String)this.expression.getValue(evaluationContext, String.class);
/*  87:126 */         result = ObjectUtils.getDisplayString(result);
/*  88:127 */         result = isHtmlEscape() ? HtmlUtils.htmlEscape(result) : result;
/*  89:128 */         result = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(result) : result;
/*  90:129 */         this.pageContext.getOut().print(result);
/*  91:    */       }
/*  92:    */       catch (IOException ex)
/*  93:    */       {
/*  94:132 */         throw new JspException(ex);
/*  95:    */       }
/*  96:    */     }
/*  97:135 */     return 6;
/*  98:    */   }
/*  99:    */   
/* 100:    */   private EvaluationContext createEvaluationContext(PageContext pageContext)
/* 101:    */   {
/* 102:139 */     StandardEvaluationContext context = new StandardEvaluationContext();
/* 103:140 */     context.addPropertyAccessor(new JspPropertyAccessor(pageContext));
/* 104:141 */     context.addPropertyAccessor(new MapAccessor());
/* 105:142 */     context.addPropertyAccessor(new EnvironmentAccessor());
/* 106:143 */     context.setBeanResolver(new BeanFactoryResolver(getRequestContext().getWebApplicationContext()));
/* 107:144 */     ConversionService conversionService = getConversionService(pageContext);
/* 108:145 */     if (conversionService != null) {
/* 109:146 */       context.setTypeConverter(new StandardTypeConverter(conversionService));
/* 110:    */     }
/* 111:148 */     return context;
/* 112:    */   }
/* 113:    */   
/* 114:    */   private ConversionService getConversionService(PageContext pageContext)
/* 115:    */   {
/* 116:152 */     return (ConversionService)pageContext.getRequest().getAttribute(ConversionService.class.getName());
/* 117:    */   }
/* 118:    */   
/* 119:    */   private static class JspPropertyAccessor
/* 120:    */     implements PropertyAccessor
/* 121:    */   {
/* 122:    */     private final PageContext pageContext;
/* 123:    */     private final VariableResolver variableResolver;
/* 124:    */     
/* 125:    */     public JspPropertyAccessor(PageContext pageContext)
/* 126:    */     {
/* 127:163 */       this.pageContext = pageContext;
/* 128:164 */       this.variableResolver = pageContext.getVariableResolver();
/* 129:    */     }
/* 130:    */     
/* 131:    */     public Class<?>[] getSpecificTargetClasses()
/* 132:    */     {
/* 133:168 */       return null;
/* 134:    */     }
/* 135:    */     
/* 136:    */     public boolean canRead(EvaluationContext context, Object target, String name)
/* 137:    */       throws AccessException
/* 138:    */     {
/* 139:173 */       return (target == null) && ((resolveImplicitVariable(name) != null) || (this.pageContext.findAttribute(name) != null));
/* 140:    */     }
/* 141:    */     
/* 142:    */     public TypedValue read(EvaluationContext context, Object target, String name)
/* 143:    */       throws AccessException
/* 144:    */     {
/* 145:177 */       Object implicitVar = resolveImplicitVariable(name);
/* 146:178 */       if (implicitVar != null) {
/* 147:179 */         return new TypedValue(implicitVar);
/* 148:    */       }
/* 149:181 */       return new TypedValue(this.pageContext.findAttribute(name));
/* 150:    */     }
/* 151:    */     
/* 152:    */     public boolean canWrite(EvaluationContext context, Object target, String name)
/* 153:    */     {
/* 154:185 */       return false;
/* 155:    */     }
/* 156:    */     
/* 157:    */     public void write(EvaluationContext context, Object target, String name, Object newValue)
/* 158:    */     {
/* 159:189 */       throw new UnsupportedOperationException();
/* 160:    */     }
/* 161:    */     
/* 162:    */     private Object resolveImplicitVariable(String name)
/* 163:    */       throws AccessException
/* 164:    */     {
/* 165:193 */       if (this.variableResolver == null) {
/* 166:194 */         return null;
/* 167:    */       }
/* 168:    */       try
/* 169:    */       {
/* 170:197 */         return this.variableResolver.resolveVariable(name);
/* 171:    */       }
/* 172:    */       catch (Exception ex)
/* 173:    */       {
/* 174:200 */         throw new AccessException(
/* 175:201 */           "Unexpected exception occurred accessing '" + name + "' as an implicit variable", ex);
/* 176:    */       }
/* 177:    */     }
/* 178:    */   }
/* 179:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.EvalTag
 * JD-Core Version:    0.7.0.1
 */