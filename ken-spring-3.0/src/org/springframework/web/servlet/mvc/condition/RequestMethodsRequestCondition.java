/*   1:    */ package org.springframework.web.servlet.mvc.condition;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import org.springframework.web.bind.annotation.RequestMethod;
/*  11:    */ 
/*  12:    */ public final class RequestMethodsRequestCondition
/*  13:    */   extends AbstractRequestCondition<RequestMethodsRequestCondition>
/*  14:    */ {
/*  15:    */   private final Set<RequestMethod> methods;
/*  16:    */   
/*  17:    */   public RequestMethodsRequestCondition(RequestMethod... requestMethods)
/*  18:    */   {
/*  19: 48 */     this(asList(requestMethods));
/*  20:    */   }
/*  21:    */   
/*  22:    */   private static List<RequestMethod> asList(RequestMethod... requestMethods)
/*  23:    */   {
/*  24: 52 */     return requestMethods != null ? Arrays.asList(requestMethods) : Collections.emptyList();
/*  25:    */   }
/*  26:    */   
/*  27:    */   private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods)
/*  28:    */   {
/*  29: 59 */     this.methods = Collections.unmodifiableSet(new LinkedHashSet(requestMethods));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public Set<RequestMethod> getMethods()
/*  33:    */   {
/*  34: 66 */     return this.methods;
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected Collection<RequestMethod> getContent()
/*  38:    */   {
/*  39: 71 */     return this.methods;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected String getToStringInfix()
/*  43:    */   {
/*  44: 76 */     return " || ";
/*  45:    */   }
/*  46:    */   
/*  47:    */   public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other)
/*  48:    */   {
/*  49: 84 */     Set<RequestMethod> set = new LinkedHashSet(this.methods);
/*  50: 85 */     set.addAll(other.methods);
/*  51: 86 */     return new RequestMethodsRequestCondition(set);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request)
/*  55:    */   {
/*  56: 98 */     if (this.methods.isEmpty()) {
/*  57: 99 */       return this;
/*  58:    */     }
/*  59:101 */     RequestMethod incomingRequestMethod = RequestMethod.valueOf(request.getMethod());
/*  60:102 */     for (RequestMethod method : this.methods) {
/*  61:103 */       if (method.equals(incomingRequestMethod)) {
/*  62:104 */         return new RequestMethodsRequestCondition(new RequestMethod[] { method });
/*  63:    */       }
/*  64:    */     }
/*  65:107 */     return null;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int compareTo(RequestMethodsRequestCondition other, HttpServletRequest request)
/*  69:    */   {
/*  70:123 */     return other.methods.size() - this.methods.size();
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition
 * JD-Core Version:    0.7.0.1
 */