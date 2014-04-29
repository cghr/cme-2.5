/*   1:    */ package org.springframework.cache.interceptor;
/*   2:    */ 
/*   3:    */ import java.util.Collections;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ 
/*   8:    */ public abstract class CacheOperation
/*   9:    */ {
/*  10: 32 */   private Set<String> cacheNames = Collections.emptySet();
/*  11: 33 */   private String condition = "";
/*  12: 34 */   private String key = "";
/*  13: 35 */   private String name = "";
/*  14:    */   
/*  15:    */   public Set<String> getCacheNames()
/*  16:    */   {
/*  17: 39 */     return this.cacheNames;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public String getCondition()
/*  21:    */   {
/*  22: 43 */     return this.condition;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getKey()
/*  26:    */   {
/*  27: 47 */     return this.key;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public String getName()
/*  31:    */   {
/*  32: 51 */     return this.name;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setCacheName(String cacheName)
/*  36:    */   {
/*  37: 55 */     Assert.hasText(cacheName);
/*  38: 56 */     this.cacheNames = Collections.singleton(cacheName);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setCacheNames(String[] cacheNames)
/*  42:    */   {
/*  43: 60 */     Assert.notEmpty(cacheNames);
/*  44: 61 */     this.cacheNames = new LinkedHashSet(cacheNames.length);
/*  45: 62 */     for (String string : cacheNames) {
/*  46: 63 */       this.cacheNames.add(string);
/*  47:    */     }
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setCondition(String condition)
/*  51:    */   {
/*  52: 68 */     Assert.notNull(condition);
/*  53: 69 */     this.condition = condition;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setKey(String key)
/*  57:    */   {
/*  58: 73 */     Assert.notNull(key);
/*  59: 74 */     this.key = key;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setName(String name)
/*  63:    */   {
/*  64: 78 */     Assert.hasText(name);
/*  65: 79 */     this.name = name;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public boolean equals(Object other)
/*  69:    */   {
/*  70: 88 */     return ((other instanceof CacheOperation)) && (toString().equals(other.toString()));
/*  71:    */   }
/*  72:    */   
/*  73:    */   public int hashCode()
/*  74:    */   {
/*  75: 97 */     return toString().hashCode();
/*  76:    */   }
/*  77:    */   
/*  78:    */   public String toString()
/*  79:    */   {
/*  80:108 */     return getOperationDescription().toString();
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected StringBuilder getOperationDescription()
/*  84:    */   {
/*  85:116 */     StringBuilder result = new StringBuilder();
/*  86:117 */     result.append("CacheDefinition[");
/*  87:118 */     result.append(this.name);
/*  88:119 */     result.append("] caches=");
/*  89:120 */     result.append(this.cacheNames);
/*  90:121 */     result.append(" | condition='");
/*  91:122 */     result.append(this.condition);
/*  92:123 */     result.append("' | key='");
/*  93:124 */     result.append(this.key);
/*  94:125 */     result.append("'");
/*  95:126 */     return result;
/*  96:    */   }
/*  97:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheOperation
 * JD-Core Version:    0.7.0.1
 */