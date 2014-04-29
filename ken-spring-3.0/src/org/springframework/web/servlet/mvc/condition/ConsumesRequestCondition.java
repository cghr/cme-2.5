/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.LinkedHashSet;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import org.springframework.http.MediaType;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public final class ConsumesRequestCondition
/*  15:    */   extends AbstractRequestCondition<ConsumesRequestCondition>
/*  16:    */ {
/*  17:    */   private final List<ConsumeMediaTypeExpression> expressions;
/*  18:    */   
/*  19:    */   public ConsumesRequestCondition(String... consumes)
/*  20:    */   {
/*  21: 57 */     this(consumes, null);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public ConsumesRequestCondition(String[] consumes, String[] headers)
/*  25:    */   {
/*  26: 69 */     this(parseExpressions(consumes, headers));
/*  27:    */   }
/*  28:    */   
/*  29:    */   private ConsumesRequestCondition(Collection<ConsumeMediaTypeExpression> expressions)
/*  30:    */   {
/*  31: 76 */     this.expressions = new ArrayList(expressions);
/*  32: 77 */     Collections.sort(this.expressions);
/*  33:    */   }
/*  34:    */   
/*  35:    */   private static Set<ConsumeMediaTypeExpression> parseExpressions(String[] consumes, String[] headers)
/*  36:    */   {
/*  37: 81 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet();
/*  38: 82 */     if (headers != null) {
/*  39: 83 */       for (String header : headers)
/*  40:    */       {
/*  41: 84 */         HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
/*  42: 85 */         if ("Content-Type".equalsIgnoreCase(expr.name)) {
/*  43: 86 */           for (MediaType mediaType : MediaType.parseMediaTypes((String)expr.value)) {
/*  44: 87 */             result.add(new ConsumeMediaTypeExpression(mediaType, expr.isNegated));
/*  45:    */           }
/*  46:    */         }
/*  47:    */       }
/*  48:    */     }
/*  49: 92 */     if (consumes != null)
/*  50:    */     {
/*  51: 93 */       ??? = consumes;??? = consumes.length;
/*  52: 93 */       for (??? = 0; ??? < ???; ???++)
/*  53:    */       {
/*  54: 93 */         String consume = ???[???];
/*  55: 94 */         result.add(new ConsumeMediaTypeExpression(consume));
/*  56:    */       }
/*  57:    */     }
/*  58: 97 */     return result;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Set<MediaTypeExpression> getExpressions()
/*  62:    */   {
/*  63:104 */     return new LinkedHashSet(this.expressions);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public Set<MediaType> getConsumableMediaTypes()
/*  67:    */   {
/*  68:111 */     Set<MediaType> result = new LinkedHashSet();
/*  69:112 */     for (ConsumeMediaTypeExpression expression : this.expressions) {
/*  70:113 */       if (!expression.isNegated()) {
/*  71:114 */         result.add(expression.getMediaType());
/*  72:    */       }
/*  73:    */     }
/*  74:117 */     return result;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public boolean isEmpty()
/*  78:    */   {
/*  79:124 */     return this.expressions.isEmpty();
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected Collection<ConsumeMediaTypeExpression> getContent()
/*  83:    */   {
/*  84:129 */     return this.expressions;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected String getToStringInfix()
/*  88:    */   {
/*  89:134 */     return " || ";
/*  90:    */   }
/*  91:    */   
/*  92:    */   public ConsumesRequestCondition combine(ConsumesRequestCondition other)
/*  93:    */   {
/*  94:143 */     return !other.expressions.isEmpty() ? other : this;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public ConsumesRequestCondition getMatchingCondition(HttpServletRequest request)
/*  98:    */   {
/*  99:159 */     if (isEmpty()) {
/* 100:160 */       return this;
/* 101:    */     }
/* 102:162 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet(this.expressions);
/* 103:163 */     for (Iterator<ConsumeMediaTypeExpression> iterator = result.iterator(); iterator.hasNext();)
/* 104:    */     {
/* 105:164 */       ConsumeMediaTypeExpression expression = (ConsumeMediaTypeExpression)iterator.next();
/* 106:165 */       if (!expression.match(request)) {
/* 107:166 */         iterator.remove();
/* 108:    */       }
/* 109:    */     }
/* 110:169 */     return result.isEmpty() ? null : new ConsumesRequestCondition(result);
/* 111:    */   }
/* 112:    */   
/* 113:    */   public int compareTo(ConsumesRequestCondition other, HttpServletRequest request)
/* 114:    */   {
/* 115:185 */     if ((this.expressions.isEmpty()) && (other.expressions.isEmpty())) {
/* 116:186 */       return 0;
/* 117:    */     }
/* 118:188 */     if (this.expressions.isEmpty()) {
/* 119:189 */       return 1;
/* 120:    */     }
/* 121:191 */     if (other.expressions.isEmpty()) {
/* 122:192 */       return -1;
/* 123:    */     }
/* 124:195 */     return ((ConsumeMediaTypeExpression)this.expressions.get(0)).compareTo((AbstractMediaTypeExpression)other.expressions.get(0));
/* 125:    */   }
/* 126:    */   
/* 127:    */   static class ConsumeMediaTypeExpression
/* 128:    */     extends AbstractMediaTypeExpression
/* 129:    */   {
/* 130:    */     ConsumeMediaTypeExpression(String expression)
/* 131:    */     {
/* 132:205 */       super();
/* 133:    */     }
/* 134:    */     
/* 135:    */     ConsumeMediaTypeExpression(MediaType mediaType, boolean negated)
/* 136:    */     {
/* 137:209 */       super(negated);
/* 138:    */     }
/* 139:    */     
/* 140:    */     protected boolean matchMediaType(HttpServletRequest request)
/* 141:    */     {
/* 142:214 */       MediaType contentType = StringUtils.hasLength(request.getContentType()) ? 
/* 143:215 */         MediaType.parseMediaType(request.getContentType()) : 
/* 144:216 */         MediaType.APPLICATION_OCTET_STREAM;
/* 145:217 */       return getMediaType().includes(contentType);
/* 146:    */     }
/* 147:    */   }
/* 148:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition
 * JD-Core Version:    0.7.0.1
 */