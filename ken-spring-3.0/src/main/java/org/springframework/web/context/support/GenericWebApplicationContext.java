/*   1:    */ package org.springframework.web.context.support;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletConfig;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*   6:    */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*   7:    */ import org.springframework.context.support.GenericApplicationContext;
/*   8:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*   9:    */ import org.springframework.core.io.Resource;
/*  10:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  11:    */ import org.springframework.ui.context.Theme;
/*  12:    */ import org.springframework.ui.context.ThemeSource;
/*  13:    */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*  14:    */ import org.springframework.util.ObjectUtils;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*  17:    */ import org.springframework.web.context.ServletContextAware;
/*  18:    */ 
/*  19:    */ public class GenericWebApplicationContext
/*  20:    */   extends GenericApplicationContext
/*  21:    */   implements ConfigurableWebApplicationContext, ThemeSource
/*  22:    */ {
/*  23:    */   private ServletContext servletContext;
/*  24:    */   private ThemeSource themeSource;
/*  25:    */   
/*  26:    */   public GenericWebApplicationContext() {}
/*  27:    */   
/*  28:    */   public GenericWebApplicationContext(ServletContext servletContext)
/*  29:    */   {
/*  30: 86 */     this.servletContext = servletContext;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory)
/*  34:    */   {
/*  35: 97 */     super(beanFactory);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory, ServletContext servletContext)
/*  39:    */   {
/*  40:108 */     super(beanFactory);
/*  41:109 */     this.servletContext = servletContext;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setServletContext(ServletContext servletContext)
/*  45:    */   {
/*  46:117 */     this.servletContext = servletContext;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public ServletContext getServletContext()
/*  50:    */   {
/*  51:121 */     return this.servletContext;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected ConfigurableEnvironment createEnvironment()
/*  55:    */   {
/*  56:130 */     return new StandardServletEnvironment();
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  60:    */   {
/*  61:139 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext));
/*  62:140 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*  63:    */     
/*  64:142 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/*  65:143 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext);
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected Resource getResourceByPath(String path)
/*  69:    */   {
/*  70:152 */     return new ServletContextResource(this.servletContext, path);
/*  71:    */   }
/*  72:    */   
/*  73:    */   protected ResourcePatternResolver getResourcePatternResolver()
/*  74:    */   {
/*  75:161 */     return new ServletContextResourcePatternResolver(this);
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected void onRefresh()
/*  79:    */   {
/*  80:169 */     this.themeSource = UiApplicationContextUtils.initThemeSource(this);
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected void initPropertySources()
/*  84:    */   {
/*  85:178 */     super.initPropertySources();
/*  86:179 */     WebApplicationContextUtils.initServletPropertySources(
/*  87:180 */       getEnvironment().getPropertySources(), this.servletContext);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Theme getTheme(String themeName)
/*  91:    */   {
/*  92:184 */     return this.themeSource.getTheme(themeName);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void setServletConfig(ServletConfig servletConfig) {}
/*  96:    */   
/*  97:    */   public ServletConfig getServletConfig()
/*  98:    */   {
/*  99:197 */     throw new UnsupportedOperationException();
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void setNamespace(String namespace) {}
/* 103:    */   
/* 104:    */   public String getNamespace()
/* 105:    */   {
/* 106:205 */     throw new UnsupportedOperationException();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void setConfigLocation(String configLocation)
/* 110:    */   {
/* 111:209 */     if (StringUtils.hasText(configLocation)) {
/* 112:210 */       throw new UnsupportedOperationException(
/* 113:211 */         "GenericWebApplicationContext does not support configLocation. Do you still have an 'contextConfigLocations' init-param set?");
/* 114:    */     }
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setConfigLocations(String[] configLocations)
/* 118:    */   {
/* 119:217 */     if (!ObjectUtils.isEmpty(configLocations)) {
/* 120:218 */       throw new UnsupportedOperationException(
/* 121:219 */         "GenericWebApplicationContext does not support configLocations. Do you still have an 'contextConfigLocations' init-param set?");
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public String[] getConfigLocations()
/* 126:    */   {
/* 127:225 */     throw new UnsupportedOperationException();
/* 128:    */   }
/* 129:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.support.GenericWebApplicationContext
 * JD-Core Version:    0.7.0.1
 */