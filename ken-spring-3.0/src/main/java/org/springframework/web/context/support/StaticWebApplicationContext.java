/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletConfig;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   6:    */ import org.springframework.context.support.StaticApplicationContext;
/*   7:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*   8:    */ import org.springframework.core.io.Resource;
/*   9:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  10:    */ import org.springframework.ui.context.Theme;
/*  11:    */ import org.springframework.ui.context.ThemeSource;
/*  12:    */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*  13:    */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*  14:    */ import org.springframework.web.context.ServletConfigAware;
/*  15:    */ import org.springframework.web.context.ServletContextAware;
/*  16:    */ 
/*  17:    */ public class StaticWebApplicationContext
/*  18:    */   extends StaticApplicationContext
/*  19:    */   implements ConfigurableWebApplicationContext, ThemeSource
/*  20:    */ {
/*  21:    */   private ServletContext servletContext;
/*  22:    */   private ServletConfig servletConfig;
/*  23:    */   private String namespace;
/*  24:    */   private ThemeSource themeSource;
/*  25:    */   
/*  26:    */   public StaticWebApplicationContext()
/*  27:    */   {
/*  28: 69 */     setDisplayName("Root WebApplicationContext");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setServletContext(ServletContext servletContext)
/*  32:    */   {
/*  33: 77 */     this.servletContext = servletContext;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ServletContext getServletContext()
/*  37:    */   {
/*  38: 81 */     return this.servletContext;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setServletConfig(ServletConfig servletConfig)
/*  42:    */   {
/*  43: 85 */     this.servletConfig = servletConfig;
/*  44: 86 */     if ((servletConfig != null) && (this.servletContext == null)) {
/*  45: 87 */       this.servletContext = servletConfig.getServletContext();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ServletConfig getServletConfig()
/*  50:    */   {
/*  51: 92 */     return this.servletConfig;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setNamespace(String namespace)
/*  55:    */   {
/*  56: 96 */     this.namespace = namespace;
/*  57: 97 */     if (namespace != null) {
/*  58: 98 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getNamespace()
/*  63:    */   {
/*  64:103 */     return this.namespace;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setConfigLocation(String configLocation)
/*  68:    */   {
/*  69:111 */     if (configLocation != null) {
/*  70:112 */       throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   public void setConfigLocations(String[] configLocations)
/*  75:    */   {
/*  76:121 */     if (configLocations != null) {
/*  77:122 */       throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String[] getConfigLocations()
/*  82:    */   {
/*  83:127 */     return null;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  87:    */   {
/*  88:136 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/*  89:137 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*  90:138 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*  91:    */     
/*  92:140 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/*  93:141 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected Resource getResourceByPath(String path)
/*  97:    */   {
/*  98:150 */     return new ServletContextResource(this.servletContext, path);
/*  99:    */   }
/* 100:    */   
/* 101:    */   protected ResourcePatternResolver getResourcePatternResolver()
/* 102:    */   {
/* 103:159 */     return new ServletContextResourcePatternResolver(this);
/* 104:    */   }
/* 105:    */   
/* 106:    */   protected ConfigurableEnvironment createEnvironment()
/* 107:    */   {
/* 108:167 */     return new StandardServletEnvironment();
/* 109:    */   }
/* 110:    */   
/* 111:    */   protected void onRefresh()
/* 112:    */   {
/* 113:175 */     this.themeSource = UiApplicationContextUtils.initThemeSource(this);
/* 114:    */   }
/* 115:    */   
/* 116:    */   protected void initPropertySources()
/* 117:    */   {
/* 118:180 */     super.initPropertySources();
/* 119:181 */     WebApplicationContextUtils.initServletPropertySources(
/* 120:182 */       getEnvironment().getPropertySources(), this.servletContext, this.servletConfig);
/* 121:    */   }
/* 122:    */   
/* 123:    */   public Theme getTheme(String themeName)
/* 124:    */   {
/* 125:186 */     return this.themeSource.getTheme(themeName);
/* 126:    */   }
/* 127:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.StaticWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */