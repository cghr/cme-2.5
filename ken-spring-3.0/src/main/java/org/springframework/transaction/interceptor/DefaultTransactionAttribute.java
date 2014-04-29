/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import org.springframework.transaction.support.DefaultTransactionDefinition;
/*   4:    */ 
/*   5:    */ public class DefaultTransactionAttribute
/*   6:    */   extends DefaultTransactionDefinition
/*   7:    */   implements TransactionAttribute
/*   8:    */ {
/*   9:    */   private String qualifier;
/*  10:    */   
/*  11:    */   public DefaultTransactionAttribute() {}
/*  12:    */   
/*  13:    */   public DefaultTransactionAttribute(TransactionAttribute other)
/*  14:    */   {
/*  15: 55 */     super(other);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public DefaultTransactionAttribute(int propagationBehavior)
/*  19:    */   {
/*  20: 68 */     super(propagationBehavior);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setQualifier(String qualifier)
/*  24:    */   {
/*  25: 78 */     this.qualifier = qualifier;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public String getQualifier()
/*  29:    */   {
/*  30: 85 */     return this.qualifier;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean rollbackOn(Throwable ex)
/*  34:    */   {
/*  35: 94 */     return ((ex instanceof RuntimeException)) || ((ex instanceof Error));
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected final StringBuilder getAttributeDescription()
/*  39:    */   {
/*  40:103 */     StringBuilder result = getDefinitionDescription();
/*  41:104 */     if (this.qualifier != null) {
/*  42:105 */       result.append("; '").append(this.qualifier).append("'");
/*  43:    */     }
/*  44:107 */     return result;
/*  45:    */   }
/*  46:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.DefaultTransactionAttribute
 * JD-Core Version:    0.7.0.1
 */