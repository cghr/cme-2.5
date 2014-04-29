/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.util.StringUtils;
/*  6:   */ 
/*  7:   */ public class TransactionAttributeEditor
/*  8:   */   extends PropertyEditorSupport
/*  9:   */ {
/* 10:   */   public void setAsText(String text)
/* 11:   */     throws IllegalArgumentException
/* 12:   */   {
/* 13:52 */     if (StringUtils.hasLength(text))
/* 14:   */     {
/* 15:54 */       String[] tokens = StringUtils.commaDelimitedListToStringArray(text);
/* 16:55 */       RuleBasedTransactionAttribute attr = new RuleBasedTransactionAttribute();
/* 17:56 */       for (int i = 0; i < tokens.length; i++)
/* 18:   */       {
/* 19:58 */         String token = StringUtils.trimWhitespace(tokens[i].trim());
/* 20:60 */         if (StringUtils.containsWhitespace(token)) {
/* 21:61 */           throw new IllegalArgumentException(
/* 22:62 */             "Transaction attribute token contains illegal whitespace: [" + token + "]");
/* 23:   */         }
/* 24:65 */         if (token.startsWith("PROPAGATION_"))
/* 25:   */         {
/* 26:66 */           attr.setPropagationBehaviorName(token);
/* 27:   */         }
/* 28:68 */         else if (token.startsWith("ISOLATION_"))
/* 29:   */         {
/* 30:69 */           attr.setIsolationLevelName(token);
/* 31:   */         }
/* 32:71 */         else if (token.startsWith("timeout_"))
/* 33:   */         {
/* 34:72 */           String value = token.substring("timeout_".length());
/* 35:73 */           attr.setTimeout(Integer.parseInt(value));
/* 36:   */         }
/* 37:75 */         else if (token.equals("readOnly"))
/* 38:   */         {
/* 39:76 */           attr.setReadOnly(true);
/* 40:   */         }
/* 41:78 */         else if (token.startsWith("+"))
/* 42:   */         {
/* 43:79 */           attr.getRollbackRules().add(new NoRollbackRuleAttribute(token.substring(1)));
/* 44:   */         }
/* 45:81 */         else if (token.startsWith("-"))
/* 46:   */         {
/* 47:82 */           attr.getRollbackRules().add(new RollbackRuleAttribute(token.substring(1)));
/* 48:   */         }
/* 49:   */         else
/* 50:   */         {
/* 51:85 */           throw new IllegalArgumentException("Invalid transaction attribute token: [" + token + "]");
/* 52:   */         }
/* 53:   */       }
/* 54:88 */       setValue(attr);
/* 55:   */     }
/* 56:   */     else
/* 57:   */     {
/* 58:91 */       setValue(null);
/* 59:   */     }
/* 60:   */   }
/* 61:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttributeEditor
 * JD-Core Version:    0.7.0.1
 */