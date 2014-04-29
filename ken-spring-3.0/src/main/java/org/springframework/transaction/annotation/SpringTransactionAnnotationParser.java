/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.AnnotatedElement;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.List;
/*  7:   */ import org.springframework.core.annotation.AnnotationUtils;
/*  8:   */ import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
/*  9:   */ import org.springframework.transaction.interceptor.RollbackRuleAttribute;
/* 10:   */ import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
/* 11:   */ import org.springframework.transaction.interceptor.TransactionAttribute;
/* 12:   */ 
/* 13:   */ public class SpringTransactionAnnotationParser
/* 14:   */   implements TransactionAnnotationParser, Serializable
/* 15:   */ {
/* 16:   */   public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae)
/* 17:   */   {
/* 18:38 */     Transactional ann = (Transactional)AnnotationUtils.getAnnotation(ae, Transactional.class);
/* 19:39 */     if (ann != null) {
/* 20:40 */       return parseTransactionAnnotation(ann);
/* 21:   */     }
/* 22:43 */     return null;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public TransactionAttribute parseTransactionAnnotation(Transactional ann)
/* 26:   */   {
/* 27:48 */     RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
/* 28:49 */     rbta.setPropagationBehavior(ann.propagation().value());
/* 29:50 */     rbta.setIsolationLevel(ann.isolation().value());
/* 30:51 */     rbta.setTimeout(ann.timeout());
/* 31:52 */     rbta.setReadOnly(ann.readOnly());
/* 32:53 */     rbta.setQualifier(ann.value());
/* 33:54 */     ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList();
/* 34:55 */     Class[] rbf = ann.rollbackFor();
/* 35:   */     Class[] arrayOfClass1;
/* 36:56 */     RollbackRuleAttribute localRollbackRuleAttribute2 = (arrayOfClass1 = rbf).length;
/* 37:56 */     for (RollbackRuleAttribute localRollbackRuleAttribute1 = 0; localRollbackRuleAttribute1 < localRollbackRuleAttribute2; localRollbackRuleAttribute1++)
/* 38:   */     {
/* 39:56 */       Class rbRule = arrayOfClass1[localRollbackRuleAttribute1];
/* 40:57 */       rule = new RollbackRuleAttribute(rbRule);
/* 41:58 */       rollBackRules.add(rule);
/* 42:   */     }
/* 43:60 */     String[] rbfc = ann.rollbackForClassName();
/* 44:61 */     RollbackRuleAttribute localRollbackRuleAttribute3 = (rule = rbfc).length;
/* 45:61 */     for (localRollbackRuleAttribute2 = 0; localRollbackRuleAttribute2 < localRollbackRuleAttribute3; localRollbackRuleAttribute2++)
/* 46:   */     {
/* 47:61 */       String rbRule = rule[localRollbackRuleAttribute2];
/* 48:62 */       rule = new RollbackRuleAttribute(rbRule);
/* 49:63 */       rollBackRules.add(rule);
/* 50:   */     }
/* 51:65 */     Class[] nrbf = ann.noRollbackFor();
/* 52:66 */     RollbackRuleAttribute rule = (rule = nrbf).length;
/* 53:   */     NoRollbackRuleAttribute rule;
/* 54:66 */     for (localRollbackRuleAttribute3 = 0; localRollbackRuleAttribute3 < rule; localRollbackRuleAttribute3++)
/* 55:   */     {
/* 56:66 */       Class rbRule = rule[localRollbackRuleAttribute3];
/* 57:67 */       rule = new NoRollbackRuleAttribute(rbRule);
/* 58:68 */       rollBackRules.add(rule);
/* 59:   */     }
/* 60:70 */     String[] nrbfc = ann.noRollbackForClassName();
/* 61:71 */     RollbackRuleAttribute rule = (rule = nrbfc).length;
/* 62:71 */     for (rule = 0; rule < rule; rule++)
/* 63:   */     {
/* 64:71 */       String rbRule = rule[rule];
/* 65:72 */       NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
/* 66:73 */       rollBackRules.add(rule);
/* 67:   */     }
/* 68:75 */     rbta.getRollbackRules().addAll(rollBackRules);
/* 69:76 */     return rbta;
/* 70:   */   }
/* 71:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.SpringTransactionAnnotationParser
 * JD-Core Version:    0.7.0.1
 */