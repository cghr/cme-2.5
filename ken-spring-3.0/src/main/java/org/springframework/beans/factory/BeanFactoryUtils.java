/*   1:    */ package org.springframework.beans.factory;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Map.Entry;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public abstract class BeanFactoryUtils
/*  16:    */ {
/*  17:    */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*  18:    */   
/*  19:    */   public static boolean isFactoryDereference(String name)
/*  20:    */   {
/*  21: 59 */     return (name != null) && (name.startsWith("&"));
/*  22:    */   }
/*  23:    */   
/*  24:    */   public static String transformedBeanName(String name)
/*  25:    */   {
/*  26: 70 */     Assert.notNull(name, "'name' must not be null");
/*  27: 71 */     String beanName = name;
/*  28: 72 */     while (beanName.startsWith("&")) {
/*  29: 73 */       beanName = beanName.substring("&".length());
/*  30:    */     }
/*  31: 75 */     return beanName;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static boolean isGeneratedBeanName(String name)
/*  35:    */   {
/*  36: 88 */     return (name != null) && (name.contains("#"));
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static String originalBeanName(String name)
/*  40:    */   {
/*  41: 99 */     Assert.notNull(name, "'name' must not be null");
/*  42:100 */     int separatorIndex = name.indexOf("#");
/*  43:101 */     return separatorIndex != -1 ? name.substring(0, separatorIndex) : name;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static int countBeansIncludingAncestors(ListableBeanFactory lbf)
/*  47:    */   {
/*  48:114 */     return beanNamesIncludingAncestors(lbf).length;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf)
/*  52:    */   {
/*  53:124 */     return beanNamesForTypeIncludingAncestors(lbf, Object.class);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class type)
/*  57:    */   {
/*  58:141 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/*  59:142 */     String[] result = lbf.getBeanNamesForType(type);
/*  60:143 */     if ((lbf instanceof HierarchicalBeanFactory))
/*  61:    */     {
/*  62:144 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/*  63:145 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory))
/*  64:    */       {
/*  65:146 */         String[] parentResult = beanNamesForTypeIncludingAncestors(
/*  66:147 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type);
/*  67:148 */         List<String> resultList = new ArrayList();
/*  68:149 */         resultList.addAll((Collection)Arrays.asList(result));
/*  69:150 */         for (String beanName : parentResult) {
/*  70:151 */           if ((!resultList.contains(beanName)) && (!hbf.containsLocalBean(beanName))) {
/*  71:152 */             resultList.add(beanName);
/*  72:    */           }
/*  73:    */         }
/*  74:155 */         result = StringUtils.toStringArray(resultList);
/*  75:    */       }
/*  76:    */     }
/*  77:158 */     return result;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class type, boolean includeNonSingletons, boolean allowEagerInit)
/*  81:    */   {
/*  82:184 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/*  83:185 */     String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  84:186 */     if ((lbf instanceof HierarchicalBeanFactory))
/*  85:    */     {
/*  86:187 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/*  87:188 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory))
/*  88:    */       {
/*  89:189 */         String[] parentResult = beanNamesForTypeIncludingAncestors(
/*  90:190 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/*  91:191 */         List<String> resultList = new ArrayList();
/*  92:192 */         resultList.addAll((Collection)Arrays.asList(result));
/*  93:193 */         for (String beanName : parentResult) {
/*  94:194 */           if ((!resultList.contains(beanName)) && (!hbf.containsLocalBean(beanName))) {
/*  95:195 */             resultList.add(beanName);
/*  96:    */           }
/*  97:    */         }
/*  98:198 */         result = StringUtils.toStringArray(resultList);
/*  99:    */       }
/* 100:    */     }
/* 101:201 */     return result;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
/* 105:    */     throws BeansException
/* 106:    */   {
/* 107:224 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 108:225 */     Map<String, T> result = new LinkedHashMap(4);
/* 109:226 */     result.putAll(lbf.getBeansOfType(type));
/* 110:227 */     if ((lbf instanceof HierarchicalBeanFactory))
/* 111:    */     {
/* 112:228 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 113:229 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory))
/* 114:    */       {
/* 115:230 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors(
/* 116:231 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type);
/* 117:232 */         for (Map.Entry<String, T> entry : parentResult.entrySet())
/* 118:    */         {
/* 119:233 */           String beanName = (String)entry.getKey();
/* 120:234 */           if ((!result.containsKey(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 121:235 */             result.put(beanName, entry.getValue());
/* 122:    */           }
/* 123:    */         }
/* 124:    */       }
/* 125:    */     }
/* 126:240 */     return result;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/* 130:    */     throws BeansException
/* 131:    */   {
/* 132:273 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 133:274 */     Map<String, T> result = new LinkedHashMap(4);
/* 134:275 */     result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
/* 135:276 */     if ((lbf instanceof HierarchicalBeanFactory))
/* 136:    */     {
/* 137:277 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 138:278 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory))
/* 139:    */       {
/* 140:279 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors(
/* 141:280 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 142:281 */         for (Map.Entry<String, T> entry : parentResult.entrySet())
/* 143:    */         {
/* 144:282 */           String beanName = (String)entry.getKey();
/* 145:283 */           if ((!result.containsKey(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 146:284 */             result.put(beanName, entry.getValue());
/* 147:    */           }
/* 148:    */         }
/* 149:    */       }
/* 150:    */     }
/* 151:289 */     return result;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
/* 155:    */     throws BeansException
/* 156:    */   {
/* 157:318 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
/* 158:319 */     if (beansOfType.size() == 1) {
/* 159:320 */       return beansOfType.values().iterator().next();
/* 160:    */     }
/* 161:323 */     throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
/* 162:    */   }
/* 163:    */   
/* 164:    */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/* 165:    */     throws BeansException
/* 166:    */   {
/* 167:360 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
/* 168:361 */     if (beansOfType.size() == 1) {
/* 169:362 */       return beansOfType.values().iterator().next();
/* 170:    */     }
/* 171:365 */     throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
/* 172:    */   }
/* 173:    */   
/* 174:    */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type)
/* 175:    */     throws BeansException
/* 176:    */   {
/* 177:386 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 178:387 */     Map<String, T> beansOfType = lbf.getBeansOfType(type);
/* 179:388 */     if (beansOfType.size() == 1) {
/* 180:389 */       return beansOfType.values().iterator().next();
/* 181:    */     }
/* 182:392 */     throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
/* 183:    */   }
/* 184:    */   
/* 185:    */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/* 186:    */     throws BeansException
/* 187:    */   {
/* 188:424 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 189:425 */     Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
/* 190:426 */     if (beansOfType.size() == 1) {
/* 191:427 */       return beansOfType.values().iterator().next();
/* 192:    */     }
/* 193:430 */     throw new NoSuchBeanDefinitionException(type, "expected single bean but found " + beansOfType.size());
/* 194:    */   }
/* 195:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.BeanFactoryUtils
 * JD-Core Version:    0.7.0.1
 */