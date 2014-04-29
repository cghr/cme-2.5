/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.LinkedList;
/*   6:    */ import java.util.List;
/*   7:    */ import java.util.Locale;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.MissingResourceException;
/*  10:    */ import java.util.ResourceBundle;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.beans.factory.BeanFactory;
/*  13:    */ import org.springframework.beans.factory.DisposableBean;
/*  14:    */ import org.springframework.beans.factory.InitializingBean;
/*  15:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  16:    */ import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
/*  17:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  18:    */ import org.springframework.core.Ordered;
/*  19:    */ import org.springframework.web.context.support.GenericWebApplicationContext;
/*  20:    */ import org.springframework.web.servlet.View;
/*  21:    */ 
/*  22:    */ public class ResourceBundleViewResolver
/*  23:    */   extends AbstractCachingViewResolver
/*  24:    */   implements Ordered, InitializingBean, DisposableBean
/*  25:    */ {
/*  26:    */   public static final String DEFAULT_BASENAME = "views";
/*  27: 71 */   private int order = 2147483647;
/*  28: 73 */   private String[] basenames = { "views" };
/*  29: 75 */   private ClassLoader bundleClassLoader = Thread.currentThread().getContextClassLoader();
/*  30:    */   private String defaultParentView;
/*  31:    */   private Locale[] localesToInitialize;
/*  32: 83 */   private final Map<Locale, BeanFactory> localeCache = new HashMap();
/*  33: 87 */   private final Map<List<ResourceBundle>, ConfigurableApplicationContext> bundleCache = new HashMap();
/*  34:    */   
/*  35:    */   public void setOrder(int order)
/*  36:    */   {
/*  37: 91 */     this.order = order;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public int getOrder()
/*  41:    */   {
/*  42: 95 */     return this.order;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setBasename(String basename)
/*  46:    */   {
/*  47:112 */     setBasenames(new String[] { basename });
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setBasenames(String[] basenames)
/*  51:    */   {
/*  52:133 */     this.basenames = basenames;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setBundleClassLoader(ClassLoader classLoader)
/*  56:    */   {
/*  57:141 */     this.bundleClassLoader = classLoader;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected ClassLoader getBundleClassLoader()
/*  61:    */   {
/*  62:150 */     return this.bundleClassLoader;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setDefaultParentView(String defaultParentView)
/*  66:    */   {
/*  67:167 */     this.defaultParentView = defaultParentView;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setLocalesToInitialize(Locale[] localesToInitialize)
/*  71:    */   {
/*  72:176 */     this.localesToInitialize = localesToInitialize;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void afterPropertiesSet()
/*  76:    */     throws BeansException
/*  77:    */   {
/*  78:184 */     if (this.localesToInitialize != null) {
/*  79:185 */       for (Locale locale : this.localesToInitialize) {
/*  80:186 */         initFactory(locale);
/*  81:    */       }
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected View loadView(String viewName, Locale locale)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88:194 */     BeanFactory factory = initFactory(locale);
/*  89:    */     try
/*  90:    */     {
/*  91:196 */       return (View)factory.getBean(viewName, View.class);
/*  92:    */     }
/*  93:    */     catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*  94:200 */     return null;
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected synchronized BeanFactory initFactory(Locale locale)
/*  98:    */     throws BeansException
/*  99:    */   {
/* 100:215 */     if (isCache())
/* 101:    */     {
/* 102:216 */       BeanFactory cachedFactory = (BeanFactory)this.localeCache.get(locale);
/* 103:217 */       if (cachedFactory != null) {
/* 104:218 */         return cachedFactory;
/* 105:    */       }
/* 106:    */     }
/* 107:223 */     List<ResourceBundle> bundles = new LinkedList();
/* 108:224 */     for (String basename : this.basenames)
/* 109:    */     {
/* 110:225 */       ResourceBundle bundle = getBundle(basename, locale);
/* 111:226 */       bundles.add(bundle);
/* 112:    */     }
/* 113:231 */     if (isCache())
/* 114:    */     {
/* 115:232 */       BeanFactory cachedFactory = (BeanFactory)this.bundleCache.get(bundles);
/* 116:233 */       if (cachedFactory != null)
/* 117:    */       {
/* 118:234 */         this.localeCache.put(locale, cachedFactory);
/* 119:235 */         return cachedFactory;
/* 120:    */       }
/* 121:    */     }
/* 122:240 */     GenericWebApplicationContext factory = new GenericWebApplicationContext();
/* 123:241 */     factory.setParent(getApplicationContext());
/* 124:242 */     factory.setServletContext(getServletContext());
/* 125:    */     
/* 126:    */ 
/* 127:245 */     PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(factory);
/* 128:246 */     reader.setDefaultParentBean(this.defaultParentView);
/* 129:247 */     for (??? = bundles.iterator(); ((Iterator)???).hasNext();)
/* 130:    */     {
/* 131:247 */       ResourceBundle bundle = (ResourceBundle)((Iterator)???).next();
/* 132:248 */       reader.registerBeanDefinitions(bundle);
/* 133:    */     }
/* 134:251 */     factory.refresh();
/* 135:254 */     if (isCache())
/* 136:    */     {
/* 137:255 */       this.localeCache.put(locale, factory);
/* 138:256 */       this.bundleCache.put(bundles, factory);
/* 139:    */     }
/* 140:259 */     return factory;
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected ResourceBundle getBundle(String basename, Locale locale)
/* 144:    */     throws MissingResourceException
/* 145:    */   {
/* 146:271 */     return ResourceBundle.getBundle(basename, locale, getBundleClassLoader());
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void destroy()
/* 150:    */     throws BeansException
/* 151:    */   {
/* 152:279 */     for (ConfigurableApplicationContext factory : this.bundleCache.values()) {
/* 153:280 */       factory.close();
/* 154:    */     }
/* 155:282 */     this.localeCache.clear();
/* 156:283 */     this.bundleCache.clear();
/* 157:    */   }
/* 158:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.ResourceBundleViewResolver
 * JD-Core Version:    0.7.0.1
 */