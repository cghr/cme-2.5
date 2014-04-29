/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.util.CollectionUtils;
/*   8:    */ 
/*   9:    */ public class HandlerExecutionChain
/*  10:    */ {
/*  11:    */   private final Object handler;
/*  12:    */   private HandlerInterceptor[] interceptors;
/*  13:    */   private List<HandlerInterceptor> interceptorList;
/*  14:    */   
/*  15:    */   public HandlerExecutionChain(Object handler)
/*  16:    */   {
/*  17: 47 */     this(handler, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public HandlerExecutionChain(Object handler, HandlerInterceptor[] interceptors)
/*  21:    */   {
/*  22: 57 */     if ((handler instanceof HandlerExecutionChain))
/*  23:    */     {
/*  24: 58 */       HandlerExecutionChain originalChain = (HandlerExecutionChain)handler;
/*  25: 59 */       this.handler = originalChain.getHandler();
/*  26: 60 */       this.interceptorList = new ArrayList();
/*  27: 61 */       CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
/*  28: 62 */       CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
/*  29:    */     }
/*  30:    */     else
/*  31:    */     {
/*  32: 65 */       this.handler = handler;
/*  33: 66 */       this.interceptors = interceptors;
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object getHandler()
/*  38:    */   {
/*  39: 76 */     return this.handler;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void addInterceptor(HandlerInterceptor interceptor)
/*  43:    */   {
/*  44: 80 */     initInterceptorList();
/*  45: 81 */     this.interceptorList.add(interceptor);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void addInterceptors(HandlerInterceptor[] interceptors)
/*  49:    */   {
/*  50: 85 */     if (interceptors != null)
/*  51:    */     {
/*  52: 86 */       initInterceptorList();
/*  53: 87 */       this.interceptorList.addAll((Collection)Arrays.asList(interceptors));
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   private void initInterceptorList()
/*  58:    */   {
/*  59: 92 */     if (this.interceptorList == null) {
/*  60: 93 */       this.interceptorList = new ArrayList();
/*  61:    */     }
/*  62: 95 */     if (this.interceptors != null)
/*  63:    */     {
/*  64: 96 */       this.interceptorList.addAll((Collection)Arrays.asList(this.interceptors));
/*  65: 97 */       this.interceptors = null;
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public HandlerInterceptor[] getInterceptors()
/*  70:    */   {
/*  71:106 */     if ((this.interceptors == null) && (this.interceptorList != null)) {
/*  72:107 */       this.interceptors = ((HandlerInterceptor[])this.interceptorList.toArray(new HandlerInterceptor[this.interceptorList.size()]));
/*  73:    */     }
/*  74:109 */     return this.interceptors;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public String toString()
/*  78:    */   {
/*  79:118 */     if (this.handler == null) {
/*  80:119 */       return "HandlerExecutionChain with no handler";
/*  81:    */     }
/*  82:121 */     StringBuilder sb = new StringBuilder();
/*  83:122 */     sb.append("HandlerExecutionChain with handler [").append(this.handler).append("]");
/*  84:123 */     if (!CollectionUtils.isEmpty(this.interceptorList))
/*  85:    */     {
/*  86:124 */       sb.append(" and ").append(this.interceptorList.size()).append(" interceptor");
/*  87:125 */       if (this.interceptorList.size() > 1) {
/*  88:126 */         sb.append("s");
/*  89:    */       }
/*  90:    */     }
/*  91:129 */     return sb.toString();
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.HandlerExecutionChain
 * JD-Core Version:    0.7.0.1
 */