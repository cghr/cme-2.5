/*   1:    */ package org.springframework.web.servlet.mvc.method;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import org.springframework.web.bind.annotation.RequestMethod;
/*   5:    */ import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
/*   6:    */ import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
/*   7:    */ import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
/*   8:    */ import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
/*   9:    */ import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
/*  10:    */ import org.springframework.web.servlet.mvc.condition.RequestCondition;
/*  11:    */ import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
/*  12:    */ import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
/*  13:    */ 
/*  14:    */ public final class RequestMappingInfo
/*  15:    */   implements RequestCondition<RequestMappingInfo>
/*  16:    */ {
/*  17:    */   private final PatternsRequestCondition patternsCondition;
/*  18:    */   private final RequestMethodsRequestCondition methodsCondition;
/*  19:    */   private final ParamsRequestCondition paramsCondition;
/*  20:    */   private final HeadersRequestCondition headersCondition;
/*  21:    */   private final ConsumesRequestCondition consumesCondition;
/*  22:    */   private final ProducesRequestCondition producesCondition;
/*  23:    */   private final RequestConditionHolder customConditionHolder;
/*  24:    */   private int hash;
/*  25:    */   
/*  26:    */   public RequestMappingInfo(PatternsRequestCondition patterns, RequestMethodsRequestCondition methods, ParamsRequestCondition params, HeadersRequestCondition headers, ConsumesRequestCondition consumes, ProducesRequestCondition produces, RequestCondition<?> custom)
/*  27:    */   {
/*  28: 74 */     this.patternsCondition = (patterns != null ? patterns : new PatternsRequestCondition(new String[0]));
/*  29: 75 */     this.methodsCondition = (methods != null ? methods : new RequestMethodsRequestCondition(new RequestMethod[0]));
/*  30: 76 */     this.paramsCondition = (params != null ? params : new ParamsRequestCondition(new String[0]));
/*  31: 77 */     this.headersCondition = (headers != null ? headers : new HeadersRequestCondition(new String[0]));
/*  32: 78 */     this.consumesCondition = (consumes != null ? consumes : new ConsumesRequestCondition(new String[0]));
/*  33: 79 */     this.producesCondition = (produces != null ? produces : new ProducesRequestCondition(new String[0]));
/*  34: 80 */     this.customConditionHolder = new RequestConditionHolder(custom);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public RequestMappingInfo(RequestMappingInfo info, RequestCondition<?> customRequestCondition)
/*  38:    */   {
/*  39: 88 */     this(info.patternsCondition, info.methodsCondition, info.paramsCondition, info.headersCondition, info.consumesCondition, info.producesCondition, customRequestCondition);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public PatternsRequestCondition getPatternsCondition()
/*  43:    */   {
/*  44: 96 */     return this.patternsCondition;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RequestMethodsRequestCondition getMethodsCondition()
/*  48:    */   {
/*  49:104 */     return this.methodsCondition;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public ParamsRequestCondition getParamsCondition()
/*  53:    */   {
/*  54:112 */     return this.paramsCondition;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public HeadersRequestCondition getHeadersCondition()
/*  58:    */   {
/*  59:120 */     return this.headersCondition;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public ConsumesRequestCondition getConsumesCondition()
/*  63:    */   {
/*  64:128 */     return this.consumesCondition;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public ProducesRequestCondition getProducesCondition()
/*  68:    */   {
/*  69:136 */     return this.producesCondition;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public RequestCondition<?> getCustomCondition()
/*  73:    */   {
/*  74:143 */     return this.customConditionHolder.getCondition();
/*  75:    */   }
/*  76:    */   
/*  77:    */   public RequestMappingInfo combine(RequestMappingInfo other)
/*  78:    */   {
/*  79:152 */     PatternsRequestCondition patterns = this.patternsCondition.combine(other.patternsCondition);
/*  80:153 */     RequestMethodsRequestCondition methods = this.methodsCondition.combine(other.methodsCondition);
/*  81:154 */     ParamsRequestCondition params = this.paramsCondition.combine(other.paramsCondition);
/*  82:155 */     HeadersRequestCondition headers = this.headersCondition.combine(other.headersCondition);
/*  83:156 */     ConsumesRequestCondition consumes = this.consumesCondition.combine(other.consumesCondition);
/*  84:157 */     ProducesRequestCondition produces = this.producesCondition.combine(other.producesCondition);
/*  85:158 */     RequestConditionHolder custom = this.customConditionHolder.combine(other.customConditionHolder);
/*  86:    */     
/*  87:160 */     return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom.getCondition());
/*  88:    */   }
/*  89:    */   
/*  90:    */   public RequestMappingInfo getMatchingCondition(HttpServletRequest request)
/*  91:    */   {
/*  92:171 */     RequestMethodsRequestCondition methods = this.methodsCondition.getMatchingCondition(request);
/*  93:172 */     ParamsRequestCondition params = this.paramsCondition.getMatchingCondition(request);
/*  94:173 */     HeadersRequestCondition headers = this.headersCondition.getMatchingCondition(request);
/*  95:174 */     ConsumesRequestCondition consumes = this.consumesCondition.getMatchingCondition(request);
/*  96:175 */     ProducesRequestCondition produces = this.producesCondition.getMatchingCondition(request);
/*  97:177 */     if ((methods == null) || (params == null) || (headers == null) || (consumes == null) || (produces == null)) {
/*  98:178 */       return null;
/*  99:    */     }
/* 100:181 */     PatternsRequestCondition patterns = this.patternsCondition.getMatchingCondition(request);
/* 101:182 */     if (patterns == null) {
/* 102:183 */       return null;
/* 103:    */     }
/* 104:186 */     RequestConditionHolder custom = this.customConditionHolder.getMatchingCondition(request);
/* 105:187 */     if (custom == null) {
/* 106:188 */       return null;
/* 107:    */     }
/* 108:191 */     return new RequestMappingInfo(patterns, methods, params, headers, consumes, produces, custom.getCondition());
/* 109:    */   }
/* 110:    */   
/* 111:    */   public int compareTo(RequestMappingInfo other, HttpServletRequest request)
/* 112:    */   {
/* 113:201 */     int result = this.patternsCondition.compareTo(other.getPatternsCondition(), request);
/* 114:202 */     if (result != 0) {
/* 115:203 */       return result;
/* 116:    */     }
/* 117:205 */     result = this.paramsCondition.compareTo(other.getParamsCondition(), request);
/* 118:206 */     if (result != 0) {
/* 119:207 */       return result;
/* 120:    */     }
/* 121:209 */     result = this.headersCondition.compareTo(other.getHeadersCondition(), request);
/* 122:210 */     if (result != 0) {
/* 123:211 */       return result;
/* 124:    */     }
/* 125:213 */     result = this.consumesCondition.compareTo(other.getConsumesCondition(), request);
/* 126:214 */     if (result != 0) {
/* 127:215 */       return result;
/* 128:    */     }
/* 129:217 */     result = this.producesCondition.compareTo(other.getProducesCondition(), request);
/* 130:218 */     if (result != 0) {
/* 131:219 */       return result;
/* 132:    */     }
/* 133:221 */     result = this.methodsCondition.compareTo(other.getMethodsCondition(), request);
/* 134:222 */     if (result != 0) {
/* 135:223 */       return result;
/* 136:    */     }
/* 137:225 */     result = this.customConditionHolder.compareTo(other.customConditionHolder, request);
/* 138:226 */     if (result != 0) {
/* 139:227 */       return result;
/* 140:    */     }
/* 141:229 */     return 0;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean equals(Object obj)
/* 145:    */   {
/* 146:234 */     if (this == obj) {
/* 147:235 */       return true;
/* 148:    */     }
/* 149:237 */     if ((obj != null) && ((obj instanceof RequestMappingInfo)))
/* 150:    */     {
/* 151:238 */       RequestMappingInfo other = (RequestMappingInfo)obj;
/* 152:    */       
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:    */ 
/* 157:    */ 
/* 158:245 */       return (this.patternsCondition.equals(other.patternsCondition)) && (this.methodsCondition.equals(other.methodsCondition)) && (this.paramsCondition.equals(other.paramsCondition)) && (this.headersCondition.equals(other.headersCondition)) && (this.consumesCondition.equals(other.consumesCondition)) && (this.producesCondition.equals(other.producesCondition)) && (this.customConditionHolder.equals(other.customConditionHolder));
/* 159:    */     }
/* 160:247 */     return false;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public int hashCode()
/* 164:    */   {
/* 165:252 */     int result = this.hash;
/* 166:253 */     if (result == 0)
/* 167:    */     {
/* 168:254 */       result = this.patternsCondition.hashCode();
/* 169:255 */       result = 31 * result + this.methodsCondition.hashCode();
/* 170:256 */       result = 31 * result + this.paramsCondition.hashCode();
/* 171:257 */       result = 31 * result + this.headersCondition.hashCode();
/* 172:258 */       result = 31 * result + this.consumesCondition.hashCode();
/* 173:259 */       result = 31 * result + this.producesCondition.hashCode();
/* 174:260 */       result = 31 * result + this.customConditionHolder.hashCode();
/* 175:261 */       this.hash = result;
/* 176:    */     }
/* 177:263 */     return result;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public String toString()
/* 181:    */   {
/* 182:268 */     StringBuilder builder = new StringBuilder("{");
/* 183:269 */     builder.append(this.patternsCondition);
/* 184:270 */     builder.append(",methods=").append(this.methodsCondition);
/* 185:271 */     builder.append(",params=").append(this.paramsCondition);
/* 186:272 */     builder.append(",headers=").append(this.headersCondition);
/* 187:273 */     builder.append(",consumes=").append(this.consumesCondition);
/* 188:274 */     builder.append(",produces=").append(this.producesCondition);
/* 189:275 */     builder.append(",custom=").append(this.customConditionHolder);
/* 190:276 */     builder.append('}');
/* 191:277 */     return builder.toString();
/* 192:    */   }
/* 193:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.RequestMappingInfo
 * JD-Core Version:    0.7.0.1
 */