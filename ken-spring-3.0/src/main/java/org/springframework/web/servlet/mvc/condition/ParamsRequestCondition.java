/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import org.springframework.web.util.WebUtils;
/*   9:    */ 
/*  10:    */ public final class ParamsRequestCondition
/*  11:    */   extends AbstractRequestCondition<ParamsRequestCondition>
/*  12:    */ {
/*  13:    */   private final Set<ParamExpression> expressions;
/*  14:    */   
/*  15:    */   public ParamsRequestCondition(String... params)
/*  16:    */   {
/*  17: 47 */     this(parseExpressions(params));
/*  18:    */   }
/*  19:    */   
/*  20:    */   private ParamsRequestCondition(Collection<ParamExpression> conditions)
/*  21:    */   {
/*  22: 51 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet(conditions));
/*  23:    */   }
/*  24:    */   
/*  25:    */   private static Collection<ParamExpression> parseExpressions(String... params)
/*  26:    */   {
/*  27: 55 */     Set<ParamExpression> expressions = new LinkedHashSet();
/*  28: 56 */     if (params != null)
/*  29:    */     {
/*  30: 57 */       String[] arrayOfString = params;int j = params.length;
/*  31: 57 */       for (int i = 0; i < j; i++)
/*  32:    */       {
/*  33: 57 */         String param = arrayOfString[i];
/*  34: 58 */         expressions.add(new ParamExpression(param));
/*  35:    */       }
/*  36:    */     }
/*  37: 61 */     return expressions;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public Set<NameValueExpression<String>> getExpressions()
/*  41:    */   {
/*  42: 68 */     return new LinkedHashSet(this.expressions);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected Collection<ParamExpression> getContent()
/*  46:    */   {
/*  47: 73 */     return this.expressions;
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected String getToStringInfix()
/*  51:    */   {
/*  52: 78 */     return " && ";
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ParamsRequestCondition combine(ParamsRequestCondition other)
/*  56:    */   {
/*  57: 86 */     Set<ParamExpression> set = new LinkedHashSet(this.expressions);
/*  58: 87 */     set.addAll(other.expressions);
/*  59: 88 */     return new ParamsRequestCondition(set);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public ParamsRequestCondition getMatchingCondition(HttpServletRequest request)
/*  63:    */   {
/*  64: 96 */     for (ParamExpression expression : this.expressions) {
/*  65: 97 */       if (!expression.match(request)) {
/*  66: 98 */         return null;
/*  67:    */       }
/*  68:    */     }
/*  69:101 */     return this;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public int compareTo(ParamsRequestCondition other, HttpServletRequest request)
/*  73:    */   {
/*  74:117 */     return other.expressions.size() - this.expressions.size();
/*  75:    */   }
/*  76:    */   
/*  77:    */   static class ParamExpression
/*  78:    */     extends AbstractNameValueExpression<String>
/*  79:    */   {
/*  80:    */     ParamExpression(String expression)
/*  81:    */     {
/*  82:126 */       super();
/*  83:    */     }
/*  84:    */     
/*  85:    */     protected String parseValue(String valueExpression)
/*  86:    */     {
/*  87:131 */       return valueExpression;
/*  88:    */     }
/*  89:    */     
/*  90:    */     protected boolean matchName(HttpServletRequest request)
/*  91:    */     {
/*  92:136 */       return WebUtils.hasSubmitParameter(request, this.name);
/*  93:    */     }
/*  94:    */     
/*  95:    */     protected boolean matchValue(HttpServletRequest request)
/*  96:    */     {
/*  97:141 */       return ((String)this.value).equals(request.getParameter(this.name));
/*  98:    */     }
/*  99:    */   }
/* 100:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.ParamsRequestCondition
 * JD-Core Version:    0.7.0.1
 */