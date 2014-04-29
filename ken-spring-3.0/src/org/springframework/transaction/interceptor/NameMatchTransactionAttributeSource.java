/*   1:    */ package org.springframework.transaction.interceptor;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Map.Entry;
/*   9:    */ import java.util.Properties;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.util.ObjectUtils;
/*  13:    */ import org.springframework.util.PatternMatchUtils;
/*  14:    */ 
/*  15:    */ public class NameMatchTransactionAttributeSource
/*  16:    */   implements TransactionAttributeSource, Serializable
/*  17:    */ {
/*  18: 47 */   protected static final Log logger = LogFactory.getLog(NameMatchTransactionAttributeSource.class);
/*  19: 50 */   private Map<String, TransactionAttribute> nameMap = new HashMap();
/*  20:    */   
/*  21:    */   public void setNameMap(Map<String, TransactionAttribute> nameMap)
/*  22:    */   {
/*  23: 61 */     for (Map.Entry<String, TransactionAttribute> entry : nameMap.entrySet()) {
/*  24: 62 */       addTransactionalMethod((String)entry.getKey(), (TransactionAttribute)entry.getValue());
/*  25:    */     }
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setProperties(Properties transactionAttributes)
/*  29:    */   {
/*  30: 74 */     TransactionAttributeEditor tae = new TransactionAttributeEditor();
/*  31: 75 */     Enumeration propNames = transactionAttributes.propertyNames();
/*  32: 76 */     while (propNames.hasMoreElements())
/*  33:    */     {
/*  34: 77 */       String methodName = (String)propNames.nextElement();
/*  35: 78 */       String value = transactionAttributes.getProperty(methodName);
/*  36: 79 */       tae.setAsText(value);
/*  37: 80 */       TransactionAttribute attr = (TransactionAttribute)tae.getValue();
/*  38: 81 */       addTransactionalMethod(methodName, attr);
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void addTransactionalMethod(String methodName, TransactionAttribute attr)
/*  43:    */   {
/*  44: 93 */     if (logger.isDebugEnabled()) {
/*  45: 94 */       logger.debug("Adding transactional method [" + methodName + "] with attribute [" + attr + "]");
/*  46:    */     }
/*  47: 96 */     this.nameMap.put(methodName, attr);
/*  48:    */   }
/*  49:    */   
/*  50:    */   public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
/*  51:    */   {
/*  52:102 */     String methodName = method.getName();
/*  53:103 */     TransactionAttribute attr = (TransactionAttribute)this.nameMap.get(methodName);
/*  54:105 */     if (attr == null)
/*  55:    */     {
/*  56:107 */       String bestNameMatch = null;
/*  57:108 */       for (String mappedName : this.nameMap.keySet()) {
/*  58:109 */         if ((isMatch(methodName, mappedName)) && (
/*  59:110 */           (bestNameMatch == null) || (bestNameMatch.length() <= mappedName.length())))
/*  60:    */         {
/*  61:111 */           attr = (TransactionAttribute)this.nameMap.get(mappedName);
/*  62:112 */           bestNameMatch = mappedName;
/*  63:    */         }
/*  64:    */       }
/*  65:    */     }
/*  66:117 */     return attr;
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected boolean isMatch(String methodName, String mappedName)
/*  70:    */   {
/*  71:130 */     return PatternMatchUtils.simpleMatch(mappedName, methodName);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean equals(Object other)
/*  75:    */   {
/*  76:136 */     if (this == other) {
/*  77:137 */       return true;
/*  78:    */     }
/*  79:139 */     if (!(other instanceof NameMatchTransactionAttributeSource)) {
/*  80:140 */       return false;
/*  81:    */     }
/*  82:142 */     NameMatchTransactionAttributeSource otherTas = (NameMatchTransactionAttributeSource)other;
/*  83:143 */     return ObjectUtils.nullSafeEquals(this.nameMap, otherTas.nameMap);
/*  84:    */   }
/*  85:    */   
/*  86:    */   public int hashCode()
/*  87:    */   {
/*  88:148 */     return NameMatchTransactionAttributeSource.class.hashCode();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public String toString()
/*  92:    */   {
/*  93:153 */     return getClass().getName() + ": " + this.nameMap;
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */