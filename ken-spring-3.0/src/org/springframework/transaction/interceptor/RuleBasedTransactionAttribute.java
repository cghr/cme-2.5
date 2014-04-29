/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ 
/*  10:    */ public class RuleBasedTransactionAttribute
/*  11:    */   extends DefaultTransactionAttribute
/*  12:    */   implements Serializable
/*  13:    */ {
/*  14:    */   public static final String PREFIX_ROLLBACK_RULE = "-";
/*  15:    */   public static final String PREFIX_COMMIT_RULE = "+";
/*  16: 50 */   private static final Log logger = LogFactory.getLog(RuleBasedTransactionAttribute.class);
/*  17:    */   private List<RollbackRuleAttribute> rollbackRules;
/*  18:    */   
/*  19:    */   public RuleBasedTransactionAttribute() {}
/*  20:    */   
/*  21:    */   public RuleBasedTransactionAttribute(RuleBasedTransactionAttribute other)
/*  22:    */   {
/*  23: 79 */     super(other);
/*  24: 80 */     this.rollbackRules = new ArrayList(other.rollbackRules);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public RuleBasedTransactionAttribute(int propagationBehavior, List<RollbackRuleAttribute> rollbackRules)
/*  28:    */   {
/*  29: 94 */     super(propagationBehavior);
/*  30: 95 */     this.rollbackRules = rollbackRules;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setRollbackRules(List<RollbackRuleAttribute> rollbackRules)
/*  34:    */   {
/*  35:106 */     this.rollbackRules = rollbackRules;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public List<RollbackRuleAttribute> getRollbackRules()
/*  39:    */   {
/*  40:114 */     if (this.rollbackRules == null) {
/*  41:115 */       this.rollbackRules = new LinkedList();
/*  42:    */     }
/*  43:117 */     return this.rollbackRules;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean rollbackOn(Throwable ex)
/*  47:    */   {
/*  48:129 */     if (logger.isTraceEnabled()) {
/*  49:130 */       logger.trace("Applying rules to determine whether transaction should rollback on " + ex);
/*  50:    */     }
/*  51:133 */     RollbackRuleAttribute winner = null;
/*  52:134 */     int deepest = 2147483647;
/*  53:136 */     if (this.rollbackRules != null) {
/*  54:137 */       for (RollbackRuleAttribute rule : this.rollbackRules)
/*  55:    */       {
/*  56:138 */         int depth = rule.getDepth(ex);
/*  57:139 */         if ((depth >= 0) && (depth < deepest))
/*  58:    */         {
/*  59:140 */           deepest = depth;
/*  60:141 */           winner = rule;
/*  61:    */         }
/*  62:    */       }
/*  63:    */     }
/*  64:146 */     if (logger.isTraceEnabled()) {
/*  65:147 */       logger.trace("Winning rollback rule is: " + winner);
/*  66:    */     }
/*  67:151 */     if (winner == null)
/*  68:    */     {
/*  69:152 */       logger.trace("No relevant rollback rule found: applying default rules");
/*  70:153 */       return super.rollbackOn(ex);
/*  71:    */     }
/*  72:156 */     return !(winner instanceof NoRollbackRuleAttribute);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String toString()
/*  76:    */   {
/*  77:162 */     StringBuilder result = getAttributeDescription();
/*  78:163 */     if (this.rollbackRules != null) {
/*  79:164 */       for (RollbackRuleAttribute rule : this.rollbackRules)
/*  80:    */       {
/*  81:165 */         String sign = (rule instanceof NoRollbackRuleAttribute) ? "+" : "-";
/*  82:166 */         result.append(',').append(sign).append(rule.getExceptionName());
/*  83:    */       }
/*  84:    */     }
/*  85:169 */     return result.toString();
/*  86:    */   }
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
 * JD-Core Version:    0.7.0.1
 */