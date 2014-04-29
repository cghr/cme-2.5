/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.AccessException;
/*  4:   */ import org.springframework.expression.BeanResolver;
/*  5:   */ import org.springframework.expression.EvaluationContext;
/*  6:   */ import org.springframework.expression.EvaluationException;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ import org.springframework.expression.spel.ExpressionState;
/*  9:   */ import org.springframework.expression.spel.SpelEvaluationException;
/* 10:   */ import org.springframework.expression.spel.SpelMessage;
/* 11:   */ 
/* 12:   */ public class BeanReference
/* 13:   */   extends SpelNodeImpl
/* 14:   */ {
/* 15:   */   private String beanname;
/* 16:   */   
/* 17:   */   public BeanReference(int pos, String beanname)
/* 18:   */   {
/* 19:37 */     super(pos, new SpelNodeImpl[0]);
/* 20:38 */     this.beanname = beanname;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public TypedValue getValueInternal(ExpressionState state)
/* 24:   */     throws EvaluationException
/* 25:   */   {
/* 26:43 */     BeanResolver beanResolver = state.getEvaluationContext().getBeanResolver();
/* 27:44 */     if (beanResolver == null) {
/* 28:45 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.NO_BEAN_RESOLVER_REGISTERED, new Object[] { this.beanname });
/* 29:   */     }
/* 30:   */     try
/* 31:   */     {
/* 32:48 */       return new TypedValue(beanResolver.resolve(state.getEvaluationContext(), this.beanname));
/* 33:   */     }
/* 34:   */     catch (AccessException ae)
/* 35:   */     {
/* 36:51 */       throw new SpelEvaluationException(getStartPosition(), ae, SpelMessage.EXCEPTION_DURING_BEAN_RESOLUTION, new Object[] {
/* 37:52 */         this.beanname, ae.getMessage() });
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public String toStringAST()
/* 42:   */   {
/* 43:58 */     StringBuilder sb = new StringBuilder();
/* 44:59 */     sb.append("@");
/* 45:60 */     if (this.beanname.indexOf('.') == -1) {
/* 46:61 */       sb.append(this.beanname);
/* 47:   */     } else {
/* 48:63 */       sb.append("'").append(this.beanname).append("'");
/* 49:   */     }
/* 50:65 */     return sb.toString();
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.BeanReference
 * JD-Core Version:    0.7.0.1
 */