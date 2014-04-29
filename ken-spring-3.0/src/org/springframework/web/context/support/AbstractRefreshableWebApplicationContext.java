/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletConfig;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   6:    */ import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
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
/*  17:    */ public abstract class AbstractRefreshableWebApplicationContext
/*  18:    */   extends AbstractRefreshableConfigApplicationContext
/*  19:    */   implements ConfigurableWebApplicationContext, ThemeSource
/*  20:    */ {
/*  21:    */   private ServletContext servletContext;
/*  22:    */   private ServletConfig servletConfig;
/*  23:    */   private String namespace;
/*  24:    */   private ThemeSource themeSource;
/*  25:    */   
/*  26:    */   public AbstractRefreshableWebApplicationContext()
/*  27:    */   {
/*  28: 95 */     setDisplayName("Root WebApplicationContext");
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setServletContext(ServletContext servletContext)
/*  32:    */   {
/*  33:100 */     this.servletContext = servletContext;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ServletContext getServletContext()
/*  37:    */   {
/*  38:104 */     return this.servletContext;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setServletConfig(ServletConfig servletConfig)
/*  42:    */   {
/*  43:108 */     this.servletConfig = servletConfig;
/*  44:109 */     if ((servletConfig != null) && (this.servletContext == null)) {
/*  45:110 */       setServletContext(servletConfig.getServletContext());
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ServletConfig getServletConfig()
/*  50:    */   {
/*  51:115 */     return this.servletConfig;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setNamespace(String namespace)
/*  55:    */   {
/*  56:119 */     this.namespace = namespace;
/*  57:120 */     if (namespace != null) {
/*  58:121 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getNamespace()
/*  63:    */   {
/*  64:126 */     return this.namespace;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String[] getConfigLocations()
/*  68:    */   {
/*  69:131 */     return super.getConfigLocations();
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected ConfigurableEnvironment createEnvironment()
/*  73:    */   {
/*  74:139 */     return new StandardServletEnvironment();
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  78:    */   {
/*  79:148 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/*  80:149 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*  81:150 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*  82:    */     
/*  83:152 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/*  84:153 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected Resource getResourceByPath(String path)
/*  88:    */   {
/*  89:162 */     return new ServletContextResource(this.servletContext, path);
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected ResourcePatternResolver getResourcePatternResolver()
/*  93:    */   {
/*  94:171 */     return new ServletContextResourcePatternResolver(this);
/*  95:    */   }
/*  96:    */   
/*  97:    */   protected void onRefresh()
/*  98:    */   {
/*  99:179 */     this.themeSource = UiApplicationContextUtils.initThemeSource(this);
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected void initPropertySources()
/* 103:    */   {
/* 104:188 */     super.initPropertySources();
/* 105:189 */     WebApplicationContextUtils.initServletPropertySources(
/* 106:190 */       getEnvironment().getPropertySources(), this.servletContext, 
/* 107:191 */       this.servletConfig);
/* 108:    */   }
/* 109:    */   
/* 110:    */   public Theme getTheme(String themeName)
/* 111:    */   {
/* 112:195 */     return this.themeSource.getTheme(themeName);
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.AbstractRefreshableWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */