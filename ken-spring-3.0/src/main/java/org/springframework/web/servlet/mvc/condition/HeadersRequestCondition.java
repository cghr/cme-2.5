/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ 
/*   9:    */ public final class HeadersRequestCondition
/*  10:    */   extends AbstractRequestCondition<HeadersRequestCondition>
/*  11:    */ {
/*  12:    */   private final Set<HeaderExpression> expressions;
/*  13:    */   
/*  14:    */   public HeadersRequestCondition(String... headers)
/*  15:    */   {
/*  16: 52 */     this(parseExpressions(headers));
/*  17:    */   }
/*  18:    */   
/*  19:    */   private HeadersRequestCondition(Collection<HeaderExpression> conditions)
/*  20:    */   {
/*  21: 56 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet(conditions));
/*  22:    */   }
/*  23:    */   
/*  24:    */   private static Collection<HeaderExpression> parseExpressions(String... headers)
/*  25:    */   {
/*  26: 60 */     Set<HeaderExpression> expressions = new LinkedHashSet();
/*  27: 61 */     if (headers != null)
/*  28:    */     {
/*  29: 62 */       String[] arrayOfString = headers;int j = headers.length;
/*  30: 62 */       for (int i = 0; i < j; i++)
/*  31:    */       {
/*  32: 62 */         String header = arrayOfString[i];
/*  33: 63 */         HeaderExpression expr = new HeaderExpression(header);
/*  34: 64 */         if ((!"Accept".equalsIgnoreCase(expr.name)) && (!"Content-Type".equalsIgnoreCase(expr.name))) {
/*  35: 67 */           expressions.add(expr);
/*  36:    */         }
/*  37:    */       }
/*  38:    */     }
/*  39: 70 */     return expressions;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Set<NameValueExpression<String>> getExpressions()
/*  43:    */   {
/*  44: 77 */     return new LinkedHashSet(this.expressions);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Collection<HeaderExpression> getContent()
/*  48:    */   {
/*  49: 82 */     return this.expressions;
/*  50:    */   }
/*  51:    */   
/*  52:    */   protected String getToStringInfix()
/*  53:    */   {
/*  54: 87 */     return " && ";
/*  55:    */   }
/*  56:    */   
/*  57:    */   public HeadersRequestCondition combine(HeadersRequestCondition other)
/*  58:    */   {
/*  59: 95 */     Set<HeaderExpression> set = new LinkedHashSet(this.expressions);
/*  60: 96 */     set.addAll(other.expressions);
/*  61: 97 */     return new HeadersRequestCondition(set);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public HeadersRequestCondition getMatchingCondition(HttpServletRequest request)
/*  65:    */   {
/*  66:105 */     for (HeaderExpression expression : this.expressions) {
/*  67:106 */       if (!expression.match(request)) {
/*  68:107 */         return null;
/*  69:    */       }
/*  70:    */     }
/*  71:110 */     return this;
/*  72:    */   }
/*  73:    */   
/*  74:    */   public int compareTo(HeadersRequestCondition other, HttpServletRequest request)
/*  75:    */   {
/*  76:126 */     return other.expressions.size() - this.expressions.size();
/*  77:    */   }
/*  78:    */   
/*  79:    */   static class HeaderExpression
/*  80:    */     extends AbstractNameValueExpression<String>
/*  81:    */   {
/*  82:    */     public HeaderExpression(String expression)
/*  83:    */     {
/*  84:135 */       super();
/*  85:    */     }
/*  86:    */     
/*  87:    */     protected String parseValue(String valueExpression)
/*  88:    */     {
/*  89:140 */       return valueExpression;
/*  90:    */     }
/*  91:    */     
/*  92:    */     protected boolean matchName(HttpServletRequest request)
/*  93:    */     {
/*  94:145 */       return request.getHeader(this.name) != null;
/*  95:    */     }
/*  96:    */     
/*  97:    */     protected boolean matchValue(HttpServletRequest request)
/*  98:    */     {
/*  99:150 */       return ((String)this.value).equals(request.getHeader(this.name));
/* 100:    */     }
/* 101:    */     
/* 102:    */     public int hashCode()
/* 103:    */     {
/* 104:155 */       int result = this.name.toLowerCase().hashCode();
/* 105:156 */       result = 31 * result + (this.value != null ? ((String)this.value).hashCode() : 0);
/* 106:157 */       result = 31 * result + (this.isNegated ? 1 : 0);
/* 107:158 */       return result;
/* 108:    */     }
/* 109:    */   }
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.HeadersRequestCondition
 * JD-Core Version:    0.7.0.1
 */