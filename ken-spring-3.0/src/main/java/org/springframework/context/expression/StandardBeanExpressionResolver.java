/*   1:    */ package org.springframework.context.expression;
/*   2:    */ 
/*   3:    */ import java.util.Map;
/*   4:    */ import java.util.concurrent.ConcurrentHashMap;
/*   5:    */ import org.springframework.beans.BeansException;
/*   6:    */ import org.springframework.beans.factory.BeanExpressionException;
/*   7:    */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*   8:    */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*   9:    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*  10:    */ import org.springframework.core.convert.ConversionService;
/*  11:    */ import org.springframework.expression.Expression;
/*  12:    */ import org.springframework.expression.ExpressionParser;
/*  13:    */ import org.springframework.expression.ParserContext;
/*  14:    */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*  15:    */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*  16:    */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*  17:    */ import org.springframework.expression.spel.support.StandardTypeLocator;
/*  18:    */ import org.springframework.util.Assert;
/*  19:    */ import org.springframework.util.StringUtils;
/*  20:    */ 
/*  21:    */ public class StandardBeanExpressionResolver
/*  22:    */   implements BeanExpressionResolver
/*  23:    */ {
/*  24:    */   public static final String DEFAULT_EXPRESSION_PREFIX = "#{";
/*  25:    */   public static final String DEFAULT_EXPRESSION_SUFFIX = "}";
/*  26: 57 */   private String expressionPrefix = "#{";
/*  27: 59 */   private String expressionSuffix = "}";
/*  28: 61 */   private ExpressionParser expressionParser = new SpelExpressionParser();
/*  29: 63 */   private final Map<String, Expression> expressionCache = new ConcurrentHashMap();
/*  30: 66 */   private final Map<BeanExpressionContext, StandardEvaluationContext> evaluationCache = new ConcurrentHashMap();
/*  31: 68 */   private final ParserContext beanExpressionParserContext = new ParserContext()
/*  32:    */   {
/*  33:    */     public boolean isTemplate()
/*  34:    */     {
/*  35: 70 */       return true;
/*  36:    */     }
/*  37:    */     
/*  38:    */     public String getExpressionPrefix()
/*  39:    */     {
/*  40: 73 */       return StandardBeanExpressionResolver.this.expressionPrefix;
/*  41:    */     }
/*  42:    */     
/*  43:    */     public String getExpressionSuffix()
/*  44:    */     {
/*  45: 76 */       return StandardBeanExpressionResolver.this.expressionSuffix;
/*  46:    */     }
/*  47:    */   };
/*  48:    */   
/*  49:    */   public void setExpressionPrefix(String expressionPrefix)
/*  50:    */   {
/*  51: 87 */     Assert.hasText(expressionPrefix, "Expression prefix must not be empty");
/*  52: 88 */     this.expressionPrefix = expressionPrefix;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setExpressionSuffix(String expressionSuffix)
/*  56:    */   {
/*  57: 97 */     Assert.hasText(expressionSuffix, "Expression suffix must not be empty");
/*  58: 98 */     this.expressionSuffix = expressionSuffix;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setExpressionParser(ExpressionParser expressionParser)
/*  62:    */   {
/*  63:107 */     Assert.notNull(expressionParser, "ExpressionParser must not be null");
/*  64:108 */     this.expressionParser = expressionParser;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Object evaluate(String value, BeanExpressionContext evalContext)
/*  68:    */     throws BeansException
/*  69:    */   {
/*  70:113 */     if (!StringUtils.hasLength(value)) {
/*  71:114 */       return value;
/*  72:    */     }
/*  73:    */     try
/*  74:    */     {
/*  75:117 */       Expression expr = (Expression)this.expressionCache.get(value);
/*  76:118 */       if (expr == null)
/*  77:    */       {
/*  78:119 */         expr = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
/*  79:120 */         this.expressionCache.put(value, expr);
/*  80:    */       }
/*  81:122 */       StandardEvaluationContext sec = (StandardEvaluationContext)this.evaluationCache.get(evalContext);
/*  82:123 */       if (sec == null)
/*  83:    */       {
/*  84:124 */         sec = new StandardEvaluationContext();
/*  85:125 */         sec.setRootObject(evalContext);
/*  86:126 */         sec.addPropertyAccessor(new BeanExpressionContextAccessor());
/*  87:127 */         sec.addPropertyAccessor(new BeanFactoryAccessor());
/*  88:128 */         sec.addPropertyAccessor(new MapAccessor());
/*  89:129 */         sec.addPropertyAccessor(new EnvironmentAccessor());
/*  90:130 */         sec.setBeanResolver(new BeanFactoryResolver(evalContext.getBeanFactory()));
/*  91:131 */         sec.setTypeLocator(new StandardTypeLocator(evalContext.getBeanFactory().getBeanClassLoader()));
/*  92:132 */         ConversionService conversionService = evalContext.getBeanFactory().getConversionService();
/*  93:133 */         if (conversionService != null) {
/*  94:134 */           sec.setTypeConverter(new StandardTypeConverter(conversionService));
/*  95:    */         }
/*  96:136 */         customizeEvaluationContext(sec);
/*  97:137 */         this.evaluationCache.put(evalContext, sec);
/*  98:    */       }
/*  99:139 */       return expr.getValue(sec);
/* 100:    */     }
/* 101:    */     catch (Exception ex)
/* 102:    */     {
/* 103:142 */       throw new BeanExpressionException("Expression parsing failed", ex);
/* 104:    */     }
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected void customizeEvaluationContext(StandardEvaluationContext evalContext) {}
/* 108:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.expression.StandardBeanExpressionResolver
 * JD-Core Version:    0.7.0.1
 */