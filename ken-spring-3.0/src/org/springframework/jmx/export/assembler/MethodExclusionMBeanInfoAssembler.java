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
/*  14:    */ public class MethodExclusionMBeanInfoAssembler
/*  15:    */   extends AbstractConfigurableMBeanInfoAssembler
/*  16:    */ {
/*  17:    */   private Set<String> ignoredMethods;
/*  18:    */   private Map<String, Set<String>> ignoredMethodMappings;
/*  19:    */   
/*  20:    */   public void setIgnoredMethods(String[] ignoredMethodNames)
/*  21:    */   {
/*  22: 72 */     this.ignoredMethods = new HashSet((Collection)Arrays.asList(ignoredMethodNames));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void setIgnoredMethodMappings(Properties mappings)
/*  26:    */   {
/*  27: 83 */     this.ignoredMethodMappings = new HashMap();
/*  28: 84 */     for (Enumeration en = mappings.keys(); en.hasMoreElements();)
/*  29:    */     {
/*  30: 85 */       String beanKey = (String)en.nextElement();
/*  31: 86 */       String[] methodNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/*  32: 87 */       this.ignoredMethodMappings.put(beanKey, new HashSet((Collection)Arrays.asList(methodNames)));
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected boolean includeReadAttribute(Method method, String beanKey)
/*  37:    */   {
/*  38: 94 */     return isNotIgnored(method, beanKey);
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected boolean includeWriteAttribute(Method method, String beanKey)
/*  42:    */   {
/*  43: 99 */     return isNotIgnored(method, beanKey);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected boolean includeOperation(Method method, String beanKey)
/*  47:    */   {
/*  48:104 */     return isNotIgnored(method, beanKey);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected boolean isNotIgnored(Method method, String beanKey)
/*  52:    */   {
/*  53:115 */     if (this.ignoredMethodMappings != null)
/*  54:    */     {
/*  55:116 */       Set<String> methodNames = (Set)this.ignoredMethodMappings.get(beanKey);
/*  56:117 */       if (methodNames != null) {
/*  57:118 */         return !methodNames.contains(method.getName());
/*  58:    */       }
/*  59:    */     }
/*  60:121 */     if (this.ignoredMethods != null) {
/*  61:122 */       return !this.ignoredMethods.contains(method.getName());
/*  62:    */     }
/*  63:124 */     return true;
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.MethodExclusionMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */