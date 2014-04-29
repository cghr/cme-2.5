/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.util.ObjectUtils;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ final class PropertyMatches
/*  11:    */ {
/*  12:    */   public static final int DEFAULT_MAX_DISTANCE = 2;
/*  13:    */   private final String propertyName;
/*  14:    */   private String[] possibleMatches;
/*  15:    */   
/*  16:    */   public static PropertyMatches forProperty(String propertyName, Class beanClass)
/*  17:    */   {
/*  18: 53 */     return forProperty(propertyName, beanClass, 2);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static PropertyMatches forProperty(String propertyName, Class beanClass, int maxDistance)
/*  22:    */   {
/*  23: 63 */     return new PropertyMatches(propertyName, beanClass, maxDistance);
/*  24:    */   }
/*  25:    */   
/*  26:    */   private PropertyMatches(String propertyName, Class beanClass, int maxDistance)
/*  27:    */   {
/*  28: 80 */     this.propertyName = propertyName;
/*  29: 81 */     this.possibleMatches = calculateMatches(BeanUtils.getPropertyDescriptors(beanClass), maxDistance);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String[] getPossibleMatches()
/*  33:    */   {
/*  34: 89 */     return this.possibleMatches;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public String buildErrorMessage()
/*  38:    */   {
/*  39: 97 */     StringBuilder msg = new StringBuilder();
/*  40: 98 */     msg.append("Bean property '");
/*  41: 99 */     msg.append(this.propertyName);
/*  42:100 */     msg.append("' is not writable or has an invalid setter method. ");
/*  43:102 */     if (ObjectUtils.isEmpty(this.possibleMatches))
/*  44:    */     {
/*  45:103 */       msg.append("Does the parameter type of the setter match the return type of the getter?");
/*  46:    */     }
/*  47:    */     else
/*  48:    */     {
/*  49:106 */       msg.append("Did you mean ");
/*  50:107 */       for (int i = 0; i < this.possibleMatches.length; i++)
/*  51:    */       {
/*  52:108 */         msg.append('\'');
/*  53:109 */         msg.append(this.possibleMatches[i]);
/*  54:110 */         if (i < this.possibleMatches.length - 2) {
/*  55:111 */           msg.append("', ");
/*  56:113 */         } else if (i == this.possibleMatches.length - 2) {
/*  57:114 */           msg.append("', or ");
/*  58:    */         }
/*  59:    */       }
/*  60:117 */       msg.append("'?");
/*  61:    */     }
/*  62:119 */     return msg.toString();
/*  63:    */   }
/*  64:    */   
/*  65:    */   private String[] calculateMatches(PropertyDescriptor[] propertyDescriptors, int maxDistance)
/*  66:    */   {
/*  67:132 */     List<String> candidates = new ArrayList();
/*  68:133 */     for (PropertyDescriptor pd : propertyDescriptors) {
/*  69:134 */       if (pd.getWriteMethod() != null)
/*  70:    */       {
/*  71:135 */         String possibleAlternative = pd.getName();
/*  72:136 */         if (calculateStringDistance(this.propertyName, possibleAlternative) <= maxDistance) {
/*  73:137 */           candidates.add(possibleAlternative);
/*  74:    */         }
/*  75:    */       }
/*  76:    */     }
/*  77:141 */     Collections.sort(candidates);
/*  78:142 */     return StringUtils.toStringArray(candidates);
/*  79:    */   }
/*  80:    */   
/*  81:    */   private int calculateStringDistance(String s1, String s2)
/*  82:    */   {
/*  83:153 */     if (s1.length() == 0) {
/*  84:154 */       return s2.length();
/*  85:    */     }
/*  86:156 */     if (s2.length() == 0) {
/*  87:157 */       return s1.length();
/*  88:    */     }
/*  89:159 */     int[][] d = new int[s1.length() + 1][s2.length() + 1];
/*  90:161 */     for (int i = 0; i <= s1.length(); i++) {
/*  91:162 */       d[i][0] = i;
/*  92:    */     }
/*  93:164 */     for (int j = 0; j <= s2.length(); j++) {
/*  94:165 */       d[0][j] = j;
/*  95:    */     }
/*  96:168 */     for (int i = 1; i <= s1.length(); i++)
/*  97:    */     {
/*  98:169 */       char s_i = s1.charAt(i - 1);
/*  99:170 */       for (int j = 1; j <= s2.length(); j++)
/* 100:    */       {
/* 101:172 */         char t_j = s2.charAt(j - 1);
/* 102:    */         int cost;
/* 103:    */         int cost;
/* 104:173 */         if (s_i == t_j) {
/* 105:174 */           cost = 0;
/* 106:    */         } else {
/* 107:176 */           cost = 1;
/* 108:    */         }
/* 109:178 */         d[i][j] = 
/* 110:179 */           Math.min(Math.min(d[(i - 1)][j] + 1, d[i][(j - 1)] + 1), d[(i - 1)][(j - 1)] + cost);
/* 111:    */       }
/* 112:    */     }
/* 113:183 */     return d[s1.length()][s2.length()];
/* 114:    */   }
/* 115:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.PropertyMatches
 * JD-Core Version:    0.7.0.1
 */