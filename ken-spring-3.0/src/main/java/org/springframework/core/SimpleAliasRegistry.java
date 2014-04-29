/*   1:    */ package org.springframework.core;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.concurrent.ConcurrentHashMap;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.util.StringUtils;
/*  11:    */ import org.springframework.util.StringValueResolver;
/*  12:    */ 
/*  13:    */ public class SimpleAliasRegistry
/*  14:    */   implements AliasRegistry
/*  15:    */ {
/*  16: 41 */   private final Map<String, String> aliasMap = new ConcurrentHashMap();
/*  17:    */   
/*  18:    */   public void registerAlias(String name, String alias)
/*  19:    */   {
/*  20: 45 */     Assert.hasText(name, "'name' must not be empty");
/*  21: 46 */     Assert.hasText(alias, "'alias' must not be empty");
/*  22: 47 */     if (alias.equals(name))
/*  23:    */     {
/*  24: 48 */       this.aliasMap.remove(alias);
/*  25:    */     }
/*  26:    */     else
/*  27:    */     {
/*  28: 51 */       if (!allowAliasOverriding())
/*  29:    */       {
/*  30: 52 */         String registeredName = (String)this.aliasMap.get(alias);
/*  31: 53 */         if ((registeredName != null) && (!registeredName.equals(name))) {
/*  32: 54 */           throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + 
/*  33: 55 */             name + "': It is already registered for name '" + registeredName + "'.");
/*  34:    */         }
/*  35:    */       }
/*  36: 58 */       checkForAliasCircle(name, alias);
/*  37: 59 */       this.aliasMap.put(alias, name);
/*  38:    */     }
/*  39:    */   }
/*  40:    */   
/*  41:    */   protected boolean allowAliasOverriding()
/*  42:    */   {
/*  43: 68 */     return true;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void removeAlias(String alias)
/*  47:    */   {
/*  48: 72 */     String name = (String)this.aliasMap.remove(alias);
/*  49: 73 */     if (name == null) {
/*  50: 74 */       throw new IllegalStateException("No alias '" + alias + "' registered");
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isAlias(String name)
/*  55:    */   {
/*  56: 79 */     return this.aliasMap.containsKey(name);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String[] getAliases(String name)
/*  60:    */   {
/*  61: 83 */     List<String> result = new ArrayList();
/*  62: 84 */     synchronized (this.aliasMap)
/*  63:    */     {
/*  64: 85 */       retrieveAliases(name, result);
/*  65:    */     }
/*  66: 87 */     return StringUtils.toStringArray(result);
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void retrieveAliases(String name, List<String> result)
/*  70:    */   {
/*  71: 96 */     for (Map.Entry<String, String> entry : this.aliasMap.entrySet())
/*  72:    */     {
/*  73: 97 */       String registeredName = (String)entry.getValue();
/*  74: 98 */       if (registeredName.equals(name))
/*  75:    */       {
/*  76: 99 */         String alias = (String)entry.getKey();
/*  77:100 */         result.add(alias);
/*  78:101 */         retrieveAliases(alias, result);
/*  79:    */       }
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void resolveAliases(StringValueResolver valueResolver)
/*  84:    */   {
/*  85:114 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  86:115 */     synchronized (this.aliasMap)
/*  87:    */     {
/*  88:116 */       Map<String, String> aliasCopy = new HashMap(this.aliasMap);
/*  89:117 */       for (String alias : aliasCopy.keySet())
/*  90:    */       {
/*  91:118 */         String registeredName = (String)aliasCopy.get(alias);
/*  92:119 */         String resolvedAlias = valueResolver.resolveStringValue(alias);
/*  93:120 */         String resolvedName = valueResolver.resolveStringValue(registeredName);
/*  94:121 */         if (resolvedAlias.equals(resolvedName))
/*  95:    */         {
/*  96:122 */           this.aliasMap.remove(alias);
/*  97:    */         }
/*  98:124 */         else if (!resolvedAlias.equals(alias))
/*  99:    */         {
/* 100:125 */           String existingName = (String)this.aliasMap.get(resolvedAlias);
/* 101:126 */           if ((existingName != null) && (!existingName.equals(resolvedName))) {
/* 102:127 */             throw new IllegalStateException(
/* 103:128 */               "Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + 
/* 104:129 */               "') for name '" + resolvedName + "': It is already registered for name '" + 
/* 105:130 */               registeredName + "'.");
/* 106:    */           }
/* 107:132 */           checkForAliasCircle(resolvedName, resolvedAlias);
/* 108:133 */           this.aliasMap.remove(alias);
/* 109:134 */           this.aliasMap.put(resolvedAlias, resolvedName);
/* 110:    */         }
/* 111:136 */         else if (!registeredName.equals(resolvedName))
/* 112:    */         {
/* 113:137 */           this.aliasMap.put(alias, resolvedName);
/* 114:    */         }
/* 115:    */       }
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public String canonicalName(String name)
/* 120:    */   {
/* 121:149 */     String canonicalName = name;
/* 122:    */     String resolvedName;
/* 123:    */     do
/* 124:    */     {
/* 125:153 */       resolvedName = (String)this.aliasMap.get(canonicalName);
/* 126:154 */       if (resolvedName != null) {
/* 127:155 */         canonicalName = resolvedName;
/* 128:    */       }
/* 129:158 */     } while (resolvedName != null);
/* 130:159 */     return canonicalName;
/* 131:    */   }
/* 132:    */   
/* 133:    */   protected void checkForAliasCircle(String name, String alias)
/* 134:    */   {
/* 135:171 */     if (alias.equals(canonicalName(name))) {
/* 136:172 */       throw new IllegalStateException("Cannot register alias '" + alias + 
/* 137:173 */         "' for name '" + name + "': Circular reference - '" + 
/* 138:174 */         name + "' is a direct or indirect alias for '" + alias + "' already");
/* 139:    */     }
/* 140:    */   }
/* 141:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.SimpleAliasRegistry
 * JD-Core Version:    0.7.0.1
 */