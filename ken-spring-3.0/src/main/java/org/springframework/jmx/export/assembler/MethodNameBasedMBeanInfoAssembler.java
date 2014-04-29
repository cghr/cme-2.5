/*   1:    */ package org.springframework.jmx.export.assembler;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Enumeration;
/*   7:    */ import java.util.HashMap;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Properties;
/*  11:    */ import java.util.Set;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class MethodNameBasedMBeanInfoAssembler
/*  15:    */   extends AbstractConfigurableMBeanInfoAssembler
/*  16:    */ {
/*  17:    */   private Set<String> managedMethods;
/*  18:    */   private Map<String, Set<String>> methodMappings;
/*  19:    */   
/*  20:    */   public void setManagedMethods(String[] methodNames)
/*  21:    */   {
/*  22: 76 */     this.managedMethods = new HashSet((Collection)Arrays.asList(methodNames));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setMethodMappings(Properties mappings)
/*  26:    */   {
/*  27: 87 */     this.methodMappings = new HashMap();
/*  28: 88 */     for (Enumeration en = mappings.keys(); en.hasMoreElements();)
/*  29:    */     {
/*  30: 89 */       String beanKey = (String)en.nextElement();
/*  31: 90 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  32: 91 */       this.methodMappings.put(beanKey, new HashSet((Collection)Arrays.asList(methodNames)));
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected boolean includeReadAttribute(Method method, String beanKey)
/*  37:    */   {
/*  38: 98 */     return isMatch(method, beanKey);
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected boolean includeWriteAttribute(Method method, String beanKey)
/*  42:    */   {
/*  43:103 */     return isMatch(method, beanKey);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected boolean includeOperation(Method method, String beanKey)
/*  47:    */   {
/*  48:108 */     return isMatch(method, beanKey);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected boolean isMatch(Method method, String beanKey)
/*  52:    */   {
/*  53:112 */     if (this.methodMappings != null)
/*  54:    */     {
/*  55:113 */       Set<String> methodNames = (Set)this.methodMappings.get(beanKey);
/*  56:114 */       if (methodNames != null) {
/*  57:115 */         return methodNames.contains(method.getName());
/*  58:    */       }
/*  59:    */     }
/*  60:118 */     return (this.managedMethods != null) && (this.managedMethods.contains(method.getName()));
/*  61:    */   }
/*  62:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.MethodNameBasedMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */