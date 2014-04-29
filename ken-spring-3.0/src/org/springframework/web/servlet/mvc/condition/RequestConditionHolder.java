/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.Collections;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ 
/*   7:    */ public final class RequestConditionHolder
/*   8:    */   extends AbstractRequestCondition<RequestConditionHolder>
/*   9:    */ {
/*  10:    */   private final RequestCondition condition;
/*  11:    */   
/*  12:    */   public RequestConditionHolder(RequestCondition<?> requestCondition)
/*  13:    */   {
/*  14: 47 */     this.condition = requestCondition;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public RequestCondition<?> getCondition()
/*  18:    */   {
/*  19: 54 */     return this.condition;
/*  20:    */   }
/*  21:    */   
/*  22:    */   protected Collection<?> getContent()
/*  23:    */   {
/*  24: 59 */     return this.condition != null ? (Collection)Collections.singleton(this.condition) : (Collection)Collections.emptyList();
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected String getToStringInfix()
/*  28:    */   {
/*  29: 64 */     return " ";
/*  30:    */   }
/*  31:    */   
/*  32:    */   public RequestConditionHolder combine(RequestConditionHolder other)
/*  33:    */   {
/*  34: 74 */     if ((this.condition == null) && (other.condition == null)) {
/*  35: 75 */       return this;
/*  36:    */     }
/*  37: 77 */     if (this.condition == null) {
/*  38: 78 */       return other;
/*  39:    */     }
/*  40: 80 */     if (other.condition == null) {
/*  41: 81 */       return this;
/*  42:    */     }
/*  43: 84 */     assertIsCompatible(other);
/*  44: 85 */     RequestCondition<?> combined = (RequestCondition)this.condition.combine(other.condition);
/*  45: 86 */     return new RequestConditionHolder(combined);
/*  46:    */   }
/*  47:    */   
/*  48:    */   private void assertIsCompatible(RequestConditionHolder other)
/*  49:    */   {
/*  50: 94 */     Class<?> clazz = this.condition.getClass();
/*  51: 95 */     Class<?> otherClazz = other.condition.getClass();
/*  52: 96 */     if (!clazz.equals(otherClazz)) {
/*  53: 97 */       throw new ClassCastException("Incompatible request conditions: " + clazz + " and " + otherClazz);
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public RequestConditionHolder getMatchingCondition(HttpServletRequest request)
/*  58:    */   {
/*  59:107 */     if (this.condition == null) {
/*  60:108 */       return this;
/*  61:    */     }
/*  62:110 */     RequestCondition<?> match = (RequestCondition)this.condition.getMatchingCondition(request);
/*  63:111 */     return new RequestConditionHolder(match);
/*  64:    */   }
/*  65:    */   
/*  66:    */   public int compareTo(RequestConditionHolder other, HttpServletRequest request)
/*  67:    */   {
/*  68:121 */     if ((this.condition == null) && (other.condition == null)) {
/*  69:122 */       return 0;
/*  70:    */     }
/*  71:124 */     if (this.condition == null) {
/*  72:125 */       return 1;
/*  73:    */     }
/*  74:127 */     if (other.condition == null) {
/*  75:128 */       return -1;
/*  76:    */     }
/*  77:131 */     assertIsCompatible(other);
/*  78:132 */     return this.condition.compareTo(other.condition, request);
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.RequestConditionHolder
 * JD-Core Version:    0.7.0.1
 */