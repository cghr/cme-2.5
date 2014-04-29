/*   1:    */ package org.springframework.context.event;
/*   2:    */ 
/*   3:    */ import java.util.Collection;
/*   4:    */ import java.util.LinkedHashSet;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import java.util.concurrent.ConcurrentHashMap;
/*   9:    */ import org.springframework.beans.factory.BeanFactory;
/*  10:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  11:    */ import org.springframework.context.ApplicationEvent;
/*  12:    */ import org.springframework.context.ApplicationListener;
/*  13:    */ import org.springframework.core.OrderComparator;
/*  14:    */ 
/*  15:    */ public abstract class AbstractApplicationEventMulticaster
/*  16:    */   implements ApplicationEventMulticaster, BeanFactoryAware
/*  17:    */ {
/*  18: 53 */   private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
/*  19: 56 */   private final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap();
/*  20:    */   private BeanFactory beanFactory;
/*  21:    */   
/*  22:    */   public void addApplicationListener(ApplicationListener listener)
/*  23:    */   {
/*  24: 62 */     synchronized (this.defaultRetriever)
/*  25:    */     {
/*  26: 63 */       this.defaultRetriever.applicationListeners.add(listener);
/*  27: 64 */       this.retrieverCache.clear();
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void addApplicationListenerBean(String listenerBeanName)
/*  32:    */   {
/*  33: 69 */     synchronized (this.defaultRetriever)
/*  34:    */     {
/*  35: 70 */       this.defaultRetriever.applicationListenerBeans.add(listenerBeanName);
/*  36: 71 */       this.retrieverCache.clear();
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void removeApplicationListener(ApplicationListener listener)
/*  41:    */   {
/*  42: 76 */     synchronized (this.defaultRetriever)
/*  43:    */     {
/*  44: 77 */       this.defaultRetriever.applicationListeners.remove(listener);
/*  45: 78 */       this.retrieverCache.clear();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void removeApplicationListenerBean(String listenerBeanName)
/*  50:    */   {
/*  51: 83 */     synchronized (this.defaultRetriever)
/*  52:    */     {
/*  53: 84 */       this.defaultRetriever.applicationListenerBeans.remove(listenerBeanName);
/*  54: 85 */       this.retrieverCache.clear();
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void removeAllListeners()
/*  59:    */   {
/*  60: 90 */     synchronized (this.defaultRetriever)
/*  61:    */     {
/*  62: 91 */       this.defaultRetriever.applicationListeners.clear();
/*  63: 92 */       this.defaultRetriever.applicationListenerBeans.clear();
/*  64: 93 */       this.retrieverCache.clear();
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public final void setBeanFactory(BeanFactory beanFactory)
/*  69:    */   {
/*  70: 98 */     this.beanFactory = beanFactory;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private BeanFactory getBeanFactory()
/*  74:    */   {
/*  75:102 */     if (this.beanFactory == null) {
/*  76:103 */       throw new IllegalStateException("ApplicationEventMulticaster cannot retrieve listener beans because it is not associated with a BeanFactory");
/*  77:    */     }
/*  78:106 */     return this.beanFactory;
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected Collection<ApplicationListener> getApplicationListeners()
/*  82:    */   {
/*  83:116 */     return this.defaultRetriever.getApplicationListeners();
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event)
/*  87:    */   {
/*  88:128 */     Class<? extends ApplicationEvent> eventType = event.getClass();
/*  89:129 */     Class sourceType = event.getSource().getClass();
/*  90:130 */     ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
/*  91:131 */     ListenerRetriever retriever = (ListenerRetriever)this.retrieverCache.get(cacheKey);
/*  92:132 */     if (retriever != null) {
/*  93:133 */       return retriever.getApplicationListeners();
/*  94:    */     }
/*  95:136 */     retriever = new ListenerRetriever(true);
/*  96:137 */     LinkedList<ApplicationListener> allListeners = new LinkedList();
/*  97:138 */     synchronized (this.defaultRetriever)
/*  98:    */     {
/*  99:139 */       for (ApplicationListener listener : this.defaultRetriever.applicationListeners) {
/* 100:140 */         if (supportsEvent(listener, eventType, sourceType))
/* 101:    */         {
/* 102:141 */           retriever.applicationListeners.add(listener);
/* 103:142 */           allListeners.add(listener);
/* 104:    */         }
/* 105:    */       }
/* 106:145 */       if (!this.defaultRetriever.applicationListenerBeans.isEmpty())
/* 107:    */       {
/* 108:146 */         BeanFactory beanFactory = getBeanFactory();
/* 109:147 */         for (String listenerBeanName : this.defaultRetriever.applicationListenerBeans)
/* 110:    */         {
/* 111:148 */           ApplicationListener listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 112:149 */           if ((!allListeners.contains(listener)) && (supportsEvent(listener, eventType, sourceType)))
/* 113:    */           {
/* 114:150 */             retriever.applicationListenerBeans.add(listenerBeanName);
/* 115:151 */             allListeners.add(listener);
/* 116:    */           }
/* 117:    */         }
/* 118:    */       }
/* 119:155 */       OrderComparator.sort(allListeners);
/* 120:156 */       this.retrieverCache.put(cacheKey, retriever);
/* 121:    */     }
/* 122:158 */     return allListeners;
/* 123:    */   }
/* 124:    */   
/* 125:    */   protected boolean supportsEvent(ApplicationListener listener, Class<? extends ApplicationEvent> eventType, Class sourceType)
/* 126:    */   {
/* 127:177 */     SmartApplicationListener smartListener = (listener instanceof SmartApplicationListener) ? 
/* 128:178 */       (SmartApplicationListener)listener : new GenericApplicationListenerAdapter(listener);
/* 129:179 */     return (smartListener.supportsEventType(eventType)) && (smartListener.supportsSourceType(sourceType));
/* 130:    */   }
/* 131:    */   
/* 132:    */   private static class ListenerCacheKey
/* 133:    */   {
/* 134:    */     private final Class eventType;
/* 135:    */     private final Class sourceType;
/* 136:    */     
/* 137:    */     public ListenerCacheKey(Class eventType, Class sourceType)
/* 138:    */     {
/* 139:193 */       this.eventType = eventType;
/* 140:194 */       this.sourceType = sourceType;
/* 141:    */     }
/* 142:    */     
/* 143:    */     public boolean equals(Object other)
/* 144:    */     {
/* 145:199 */       if (this == other) {
/* 146:200 */         return true;
/* 147:    */       }
/* 148:202 */       ListenerCacheKey otherKey = (ListenerCacheKey)other;
/* 149:203 */       return (this.eventType.equals(otherKey.eventType)) && (this.sourceType.equals(otherKey.sourceType));
/* 150:    */     }
/* 151:    */     
/* 152:    */     public int hashCode()
/* 153:    */     {
/* 154:208 */       return this.eventType.hashCode() * 29 + this.sourceType.hashCode();
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   private class ListenerRetriever
/* 159:    */   {
/* 160:    */     public final Set<ApplicationListener> applicationListeners;
/* 161:    */     public final Set<String> applicationListenerBeans;
/* 162:    */     private final boolean preFiltered;
/* 163:    */     
/* 164:    */     public ListenerRetriever(boolean preFiltered)
/* 165:    */     {
/* 166:227 */       this.applicationListeners = new LinkedHashSet();
/* 167:228 */       this.applicationListenerBeans = new LinkedHashSet();
/* 168:229 */       this.preFiltered = preFiltered;
/* 169:    */     }
/* 170:    */     
/* 171:    */     public Collection<ApplicationListener> getApplicationListeners()
/* 172:    */     {
/* 173:233 */       LinkedList<ApplicationListener> allListeners = new LinkedList();
/* 174:234 */       for (ApplicationListener listener : this.applicationListeners) {
/* 175:235 */         allListeners.add(listener);
/* 176:    */       }
/* 177:237 */       if (!this.applicationListenerBeans.isEmpty())
/* 178:    */       {
/* 179:238 */         BeanFactory beanFactory = AbstractApplicationEventMulticaster.this.getBeanFactory();
/* 180:239 */         for (String listenerBeanName : this.applicationListenerBeans)
/* 181:    */         {
/* 182:240 */           ApplicationListener listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 183:241 */           if ((this.preFiltered) || (!allListeners.contains(listener))) {
/* 184:242 */             allListeners.add(listener);
/* 185:    */           }
/* 186:    */         }
/* 187:    */       }
/* 188:246 */       OrderComparator.sort(allListeners);
/* 189:247 */       return allListeners;
/* 190:    */     }
/* 191:    */   }
/* 192:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.event.AbstractApplicationEventMulticaster
 * JD-Core Version:    0.7.0.1
 */