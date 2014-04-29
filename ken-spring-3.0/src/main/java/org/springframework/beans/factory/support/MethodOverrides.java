/*   1:    */ package org.springframework.beans.factory.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ 
/*   7:    */ public class MethodOverrides
/*   8:    */ {
/*   9: 37 */   private final Set<MethodOverride> overrides = new HashSet(0);
/*  10:    */   
/*  11:    */   public MethodOverrides() {}
/*  12:    */   
/*  13:    */   public MethodOverrides(MethodOverrides other)
/*  14:    */   {
/*  15: 50 */     addOverrides(other);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void addOverrides(MethodOverrides other)
/*  19:    */   {
/*  20: 58 */     if (other != null) {
/*  21: 59 */       this.overrides.addAll(other.getOverrides());
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void addOverride(MethodOverride override)
/*  26:    */   {
/*  27: 67 */     this.overrides.add(override);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public Set<MethodOverride> getOverrides()
/*  31:    */   {
/*  32: 76 */     return this.overrides;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public boolean isEmpty()
/*  36:    */   {
/*  37: 83 */     return this.overrides.isEmpty();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public MethodOverride getOverride(Method method)
/*  41:    */   {
/*  42: 92 */     for (MethodOverride override : this.overrides) {
/*  43: 93 */       if (override.matches(method)) {
/*  44: 94 */         return override;
/*  45:    */       }
/*  46:    */     }
/*  47: 97 */     return null;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public boolean equals(Object other)
/*  51:    */   {
/*  52:102 */     if (this == other) {
/*  53:103 */       return true;
/*  54:    */     }
/*  55:105 */     if (!(other instanceof MethodOverrides)) {
/*  56:106 */       return false;
/*  57:    */     }
/*  58:108 */     MethodOverrides that = (MethodOverrides)other;
/*  59:109 */     return this.overrides.equals(that.overrides);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int hashCode()
/*  63:    */   {
/*  64:115 */     return this.overrides.hashCode();
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.MethodOverrides
 * JD-Core Version:    0.7.0.1
 */