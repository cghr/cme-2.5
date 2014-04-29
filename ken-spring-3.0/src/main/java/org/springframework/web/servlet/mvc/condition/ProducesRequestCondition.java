/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Collections;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.LinkedHashSet;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import org.springframework.http.MediaType;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public final class ProducesRequestCondition
/*  16:    */   extends AbstractRequestCondition<ProducesRequestCondition>
/*  17:    */ {
/*  18:    */   private final List<ProduceMediaTypeExpression> expressions;
/*  19:    */   
/*  20:    */   public ProducesRequestCondition(String... produces)
/*  21:    */   {
/*  22: 56 */     this(parseExpressions(produces, null));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ProducesRequestCondition(String[] produces, String[] headers)
/*  26:    */   {
/*  27: 67 */     this(parseExpressions(produces, headers));
/*  28:    */   }
/*  29:    */   
/*  30:    */   private ProducesRequestCondition(Collection<ProduceMediaTypeExpression> expressions)
/*  31:    */   {
/*  32: 74 */     this.expressions = new ArrayList(expressions);
/*  33: 75 */     Collections.sort(this.expressions);
/*  34:    */   }
/*  35:    */   
/*  36:    */   private static Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, String[] headers)
/*  37:    */   {
/*  38: 79 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet();
/*  39: 80 */     if (headers != null) {
/*  40: 81 */       for (String header : headers)
/*  41:    */       {
/*  42: 82 */         HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
/*  43: 83 */         if ("Accept".equalsIgnoreCase(expr.name)) {
/*  44: 84 */           for (MediaType mediaType : MediaType.parseMediaTypes((String)expr.value)) {
/*  45: 85 */             result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
/*  46:    */           }
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50: 90 */     if (produces != null)
/*  51:    */     {
/*  52: 91 */       ??? = produces;??? = produces.length;
/*  53: 91 */       for (??? = 0; ??? < ???; ???++)
/*  54:    */       {
/*  55: 91 */         String produce = ???[???];
/*  56: 92 */         result.add(new ProduceMediaTypeExpression(produce));
/*  57:    */       }
/*  58:    */     }
/*  59: 95 */     return result;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Set<MediaTypeExpression> getExpressions()
/*  63:    */   {
/*  64:102 */     return new LinkedHashSet(this.expressions);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Set<MediaType> getProducibleMediaTypes()
/*  68:    */   {
/*  69:109 */     Set<MediaType> result = new LinkedHashSet();
/*  70:110 */     for (ProduceMediaTypeExpression expression : this.expressions) {
/*  71:111 */       if (!expression.isNegated()) {
/*  72:112 */         result.add(expression.getMediaType());
/*  73:    */       }
/*  74:    */     }
/*  75:115 */     return result;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean isEmpty()
/*  79:    */   {
/*  80:122 */     return this.expressions.isEmpty();
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected List<ProduceMediaTypeExpression> getContent()
/*  84:    */   {
/*  85:127 */     return this.expressions;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected String getToStringInfix()
/*  89:    */   {
/*  90:132 */     return " || ";
/*  91:    */   }
/*  92:    */   
/*  93:    */   public ProducesRequestCondition combine(ProducesRequestCondition other)
/*  94:    */   {
/*  95:141 */     return !other.expressions.isEmpty() ? other : this;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public ProducesRequestCondition getMatchingCondition(HttpServletRequest request)
/*  99:    */   {
/* 100:157 */     if (isEmpty()) {
/* 101:158 */       return this;
/* 102:    */     }
/* 103:160 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet(this.expressions);
/* 104:161 */     for (Iterator<ProduceMediaTypeExpression> iterator = result.iterator(); iterator.hasNext();)
/* 105:    */     {
/* 106:162 */       ProduceMediaTypeExpression expression = (ProduceMediaTypeExpression)iterator.next();
/* 107:163 */       if (!expression.match(request)) {
/* 108:164 */         iterator.remove();
/* 109:    */       }
/* 110:    */     }
/* 111:167 */     return result.isEmpty() ? null : new ProducesRequestCondition(result);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public int compareTo(ProducesRequestCondition other, HttpServletRequest request)
/* 115:    */   {
/* 116:190 */     List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
/* 117:191 */     MediaType.sortByQualityValue(acceptedMediaTypes);
/* 118:193 */     for (MediaType acceptedMediaType : acceptedMediaTypes)
/* 119:    */     {
/* 120:194 */       int thisIndex = indexOfEqualMediaType(acceptedMediaType);
/* 121:195 */       int otherIndex = other.indexOfEqualMediaType(acceptedMediaType);
/* 122:196 */       int result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 123:197 */       if (result != 0) {
/* 124:198 */         return result;
/* 125:    */       }
/* 126:200 */       thisIndex = indexOfIncludedMediaType(acceptedMediaType);
/* 127:201 */       otherIndex = other.indexOfIncludedMediaType(acceptedMediaType);
/* 128:202 */       result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 129:203 */       if (result != 0) {
/* 130:204 */         return result;
/* 131:    */       }
/* 132:    */     }
/* 133:208 */     return 0;
/* 134:    */   }
/* 135:    */   
/* 136:    */   private static List<MediaType> getAcceptedMediaTypes(HttpServletRequest request)
/* 137:    */   {
/* 138:212 */     String acceptHeader = request.getHeader("Accept");
/* 139:213 */     if (StringUtils.hasLength(acceptHeader)) {
/* 140:214 */       return MediaType.parseMediaTypes(acceptHeader);
/* 141:    */     }
/* 142:217 */     return Collections.singletonList(MediaType.ALL);
/* 143:    */   }
/* 144:    */   
/* 145:    */   private int indexOfEqualMediaType(MediaType mediaType)
/* 146:    */   {
/* 147:222 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 148:223 */       if (mediaType.equals(((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType())) {
/* 149:224 */         return i;
/* 150:    */       }
/* 151:    */     }
/* 152:227 */     return -1;
/* 153:    */   }
/* 154:    */   
/* 155:    */   private int indexOfIncludedMediaType(MediaType mediaType)
/* 156:    */   {
/* 157:231 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 158:232 */       if (mediaType.includes(((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType())) {
/* 159:233 */         return i;
/* 160:    */       }
/* 161:    */     }
/* 162:236 */     return -1;
/* 163:    */   }
/* 164:    */   
/* 165:    */   private static int compareMatchingMediaTypes(ProducesRequestCondition condition1, int index1, ProducesRequestCondition condition2, int index2)
/* 166:    */   {
/* 167:241 */     int result = 0;
/* 168:242 */     if (index1 != index2)
/* 169:    */     {
/* 170:243 */       result = index2 - index1;
/* 171:    */     }
/* 172:245 */     else if ((index1 != -1) && (index2 != -1))
/* 173:    */     {
/* 174:246 */       ProduceMediaTypeExpression expr1 = (ProduceMediaTypeExpression)condition1.getExpressionsToCompare().get(index1);
/* 175:247 */       ProduceMediaTypeExpression expr2 = (ProduceMediaTypeExpression)condition2.getExpressionsToCompare().get(index2);
/* 176:248 */       result = expr1.compareTo(expr2);
/* 177:249 */       result = result != 0 ? result : expr1.getMediaType().compareTo(expr2.getMediaType());
/* 178:    */     }
/* 179:251 */     return result;
/* 180:    */   }
/* 181:    */   
/* 182:    */   private List<ProduceMediaTypeExpression> getExpressionsToCompare()
/* 183:    */   {
/* 184:259 */     return this.expressions.isEmpty() ? DEFAULT_EXPRESSION_LIST : this.expressions;
/* 185:    */   }
/* 186:    */   
/* 187:263 */   private static final List<ProduceMediaTypeExpression> DEFAULT_EXPRESSION_LIST = Arrays.asList(new ProduceMediaTypeExpression[] { new ProduceMediaTypeExpression("*/*") });
/* 188:    */   
/* 189:    */   static class ProduceMediaTypeExpression
/* 190:    */     extends AbstractMediaTypeExpression
/* 191:    */   {
/* 192:    */     ProduceMediaTypeExpression(MediaType mediaType, boolean negated)
/* 193:    */     {
/* 194:272 */       super(negated);
/* 195:    */     }
/* 196:    */     
/* 197:    */     ProduceMediaTypeExpression(String expression)
/* 198:    */     {
/* 199:276 */       super();
/* 200:    */     }
/* 201:    */     
/* 202:    */     protected boolean matchMediaType(HttpServletRequest request)
/* 203:    */     {
/* 204:281 */       List<MediaType> acceptedMediaTypes = ProducesRequestCondition.access$4(request);
/* 205:282 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 206:283 */         if (getMediaType().isCompatibleWith(acceptedMediaType)) {
/* 207:284 */           return true;
/* 208:    */         }
/* 209:    */       }
/* 210:287 */       return false;
/* 211:    */     }
/* 212:    */   }
/* 213:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.ProducesRequestCondition
 * JD-Core Version:    0.7.0.1
 */