/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.LinkedHashSet;
/*   7:    */ import java.util.List;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.core.NamedThreadLocal;
/*  13:    */ import org.springframework.core.OrderComparator;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ 
/*  16:    */ public abstract class TransactionSynchronizationManager
/*  17:    */ {
/*  18: 78 */   private static final Log logger = LogFactory.getLog(TransactionSynchronizationManager.class);
/*  19: 81 */   private static final ThreadLocal<Map<Object, Object>> resources = new NamedThreadLocal("Transactional resources");
/*  20: 84 */   private static final ThreadLocal<Set<TransactionSynchronization>> synchronizations = new NamedThreadLocal("Transaction synchronizations");
/*  21: 87 */   private static final ThreadLocal<String> currentTransactionName = new NamedThreadLocal("Current transaction name");
/*  22: 90 */   private static final ThreadLocal<Boolean> currentTransactionReadOnly = new NamedThreadLocal("Current transaction read-only status");
/*  23: 93 */   private static final ThreadLocal<Integer> currentTransactionIsolationLevel = new NamedThreadLocal("Current transaction isolation level");
/*  24: 96 */   private static final ThreadLocal<Boolean> actualTransactionActive = new NamedThreadLocal("Actual transaction active");
/*  25:    */   
/*  26:    */   public static Map<Object, Object> getResourceMap()
/*  27:    */   {
/*  28:113 */     Map<Object, Object> map = (Map)resources.get();
/*  29:114 */     return map != null ? Collections.unmodifiableMap(map) : Collections.emptyMap();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static boolean hasResource(Object key)
/*  33:    */   {
/*  34:124 */     Object actualKey = TransactionSynchronizationUtils.unwrapResourceIfNecessary(key);
/*  35:125 */     Object value = doGetResource(actualKey);
/*  36:126 */     return value != null;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static Object getResource(Object key)
/*  40:    */   {
/*  41:137 */     Object actualKey = TransactionSynchronizationUtils.unwrapResourceIfNecessary(key);
/*  42:138 */     Object value = doGetResource(actualKey);
/*  43:139 */     if ((value != null) && (logger.isTraceEnabled())) {
/*  44:140 */       logger.trace("Retrieved value [" + value + "] for key [" + actualKey + "] bound to thread [" + 
/*  45:141 */         Thread.currentThread().getName() + "]");
/*  46:    */     }
/*  47:143 */     return value;
/*  48:    */   }
/*  49:    */   
/*  50:    */   private static Object doGetResource(Object actualKey)
/*  51:    */   {
/*  52:150 */     Map<Object, Object> map = (Map)resources.get();
/*  53:151 */     if (map == null) {
/*  54:152 */       return null;
/*  55:    */     }
/*  56:154 */     Object value = map.get(actualKey);
/*  57:156 */     if (((value instanceof ResourceHolder)) && (((ResourceHolder)value).isVoid()))
/*  58:    */     {
/*  59:157 */       map.remove(actualKey);
/*  60:158 */       value = null;
/*  61:    */     }
/*  62:160 */     return value;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static void bindResource(Object key, Object value)
/*  66:    */     throws IllegalStateException
/*  67:    */   {
/*  68:171 */     Object actualKey = TransactionSynchronizationUtils.unwrapResourceIfNecessary(key);
/*  69:172 */     Assert.notNull(value, "Value must not be null");
/*  70:173 */     Map<Object, Object> map = (Map)resources.get();
/*  71:175 */     if (map == null)
/*  72:    */     {
/*  73:176 */       map = new HashMap();
/*  74:177 */       resources.set(map);
/*  75:    */     }
/*  76:179 */     if (map.put(actualKey, value) != null) {
/*  77:180 */       throw new IllegalStateException("Already value [" + map.get(actualKey) + "] for key [" + 
/*  78:181 */         actualKey + "] bound to thread [" + Thread.currentThread().getName() + "]");
/*  79:    */     }
/*  80:183 */     if (logger.isTraceEnabled()) {
/*  81:184 */       logger.trace("Bound value [" + value + "] for key [" + actualKey + "] to thread [" + 
/*  82:185 */         Thread.currentThread().getName() + "]");
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static Object unbindResource(Object key)
/*  87:    */     throws IllegalStateException
/*  88:    */   {
/*  89:197 */     Object actualKey = TransactionSynchronizationUtils.unwrapResourceIfNecessary(key);
/*  90:198 */     Object value = doUnbindResource(actualKey);
/*  91:199 */     if (value == null) {
/*  92:200 */       throw new IllegalStateException(
/*  93:201 */         "No value for key [" + actualKey + "] bound to thread [" + Thread.currentThread().getName() + "]");
/*  94:    */     }
/*  95:203 */     return value;
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static Object unbindResourceIfPossible(Object key)
/*  99:    */   {
/* 100:212 */     Object actualKey = TransactionSynchronizationUtils.unwrapResourceIfNecessary(key);
/* 101:213 */     return doUnbindResource(actualKey);
/* 102:    */   }
/* 103:    */   
/* 104:    */   private static Object doUnbindResource(Object actualKey)
/* 105:    */   {
/* 106:220 */     Map<Object, Object> map = (Map)resources.get();
/* 107:221 */     if (map == null) {
/* 108:222 */       return null;
/* 109:    */     }
/* 110:224 */     Object value = map.remove(actualKey);
/* 111:226 */     if (map.isEmpty()) {
/* 112:227 */       resources.remove();
/* 113:    */     }
/* 114:229 */     if ((value != null) && (logger.isTraceEnabled())) {
/* 115:230 */       logger.trace("Removed value [" + value + "] for key [" + actualKey + "] from thread [" + 
/* 116:231 */         Thread.currentThread().getName() + "]");
/* 117:    */     }
/* 118:233 */     return value;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static boolean isSynchronizationActive()
/* 122:    */   {
/* 123:247 */     return synchronizations.get() != null;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public static void initSynchronization()
/* 127:    */     throws IllegalStateException
/* 128:    */   {
/* 129:256 */     if (isSynchronizationActive()) {
/* 130:257 */       throw new IllegalStateException("Cannot activate transaction synchronization - already active");
/* 131:    */     }
/* 132:259 */     logger.trace("Initializing transaction synchronization");
/* 133:260 */     synchronizations.set(new LinkedHashSet());
/* 134:    */   }
/* 135:    */   
/* 136:    */   public static void registerSynchronization(TransactionSynchronization synchronization)
/* 137:    */     throws IllegalStateException
/* 138:    */   {
/* 139:276 */     Assert.notNull(synchronization, "TransactionSynchronization must not be null");
/* 140:277 */     if (!isSynchronizationActive()) {
/* 141:278 */       throw new IllegalStateException("Transaction synchronization is not active");
/* 142:    */     }
/* 143:280 */     ((Set)synchronizations.get()).add(synchronization);
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static List<TransactionSynchronization> getSynchronizations()
/* 147:    */     throws IllegalStateException
/* 148:    */   {
/* 149:291 */     Set<TransactionSynchronization> synchs = (Set)synchronizations.get();
/* 150:292 */     if (synchs == null) {
/* 151:293 */       throw new IllegalStateException("Transaction synchronization is not active");
/* 152:    */     }
/* 153:298 */     if (synchs.isEmpty()) {
/* 154:299 */       return Collections.emptyList();
/* 155:    */     }
/* 156:303 */     List<TransactionSynchronization> sortedSynchs = new ArrayList(synchs);
/* 157:304 */     OrderComparator.sort(sortedSynchs);
/* 158:305 */     return Collections.unmodifiableList(sortedSynchs);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static void clearSynchronization()
/* 162:    */     throws IllegalStateException
/* 163:    */   {
/* 164:315 */     if (!isSynchronizationActive()) {
/* 165:316 */       throw new IllegalStateException("Cannot deactivate transaction synchronization - not active");
/* 166:    */     }
/* 167:318 */     logger.trace("Clearing transaction synchronization");
/* 168:319 */     synchronizations.remove();
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static void setCurrentTransactionName(String name)
/* 172:    */   {
/* 173:334 */     currentTransactionName.set(name);
/* 174:    */   }
/* 175:    */   
/* 176:    */   public static String getCurrentTransactionName()
/* 177:    */   {
/* 178:344 */     return (String)currentTransactionName.get();
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static void setCurrentTransactionReadOnly(boolean readOnly)
/* 182:    */   {
/* 183:355 */     currentTransactionReadOnly.set(readOnly ? Boolean.TRUE : null);
/* 184:    */   }
/* 185:    */   
/* 186:    */   public static boolean isCurrentTransactionReadOnly()
/* 187:    */   {
/* 188:371 */     return currentTransactionReadOnly.get() != null;
/* 189:    */   }
/* 190:    */   
/* 191:    */   public static void setCurrentTransactionIsolationLevel(Integer isolationLevel)
/* 192:    */   {
/* 193:391 */     currentTransactionIsolationLevel.set(isolationLevel);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public static Integer getCurrentTransactionIsolationLevel()
/* 197:    */   {
/* 198:412 */     return (Integer)currentTransactionIsolationLevel.get();
/* 199:    */   }
/* 200:    */   
/* 201:    */   public static void setActualTransactionActive(boolean active)
/* 202:    */   {
/* 203:422 */     actualTransactionActive.set(active ? Boolean.TRUE : null);
/* 204:    */   }
/* 205:    */   
/* 206:    */   public static boolean isActualTransactionActive()
/* 207:    */   {
/* 208:437 */     return actualTransactionActive.get() != null;
/* 209:    */   }
/* 210:    */   
/* 211:    */   public static void clear()
/* 212:    */   {
/* 213:451 */     clearSynchronization();
/* 214:452 */     setCurrentTransactionName(null);
/* 215:453 */     setCurrentTransactionReadOnly(false);
/* 216:454 */     setCurrentTransactionIsolationLevel(null);
/* 217:455 */     setActualTransactionActive(false);
/* 218:    */   }
/* 219:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionSynchronizationManager
 * JD-Core Version:    0.7.0.1
 */