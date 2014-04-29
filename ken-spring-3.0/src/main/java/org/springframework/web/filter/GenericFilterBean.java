/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.util.Enumeration;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.Set;
/*   6:    */ import javax.servlet.Filter;
/*   7:    */ import javax.servlet.FilterConfig;
/*   8:    */ import javax.servlet.ServletContext;
/*   9:    */ import javax.servlet.ServletException;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.apache.commons.logging.LogFactory;
/*  12:    */ import org.springframework.beans.BeanWrapper;
/*  13:    */ import org.springframework.beans.BeansException;
/*  14:    */ import org.springframework.beans.MutablePropertyValues;
/*  15:    */ import org.springframework.beans.PropertyAccessorFactory;
/*  16:    */ import org.springframework.beans.PropertyValue;
/*  17:    */ import org.springframework.beans.PropertyValues;
/*  18:    */ import org.springframework.beans.factory.BeanNameAware;
/*  19:    */ import org.springframework.beans.factory.DisposableBean;
/*  20:    */ import org.springframework.beans.factory.InitializingBean;
/*  21:    */ import org.springframework.context.EnvironmentAware;
/*  22:    */ import org.springframework.core.env.Environment;
/*  23:    */ import org.springframework.core.io.Resource;
/*  24:    */ import org.springframework.core.io.ResourceEditor;
/*  25:    */ import org.springframework.core.io.ResourceLoader;
/*  26:    */ import org.springframework.util.Assert;
/*  27:    */ import org.springframework.util.StringUtils;
/*  28:    */ import org.springframework.web.context.ServletContextAware;
/*  29:    */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*  30:    */ import org.springframework.web.context.support.StandardServletEnvironment;
/*  31:    */ import org.springframework.web.util.NestedServletException;
/*  32:    */ 
/*  33:    */ public abstract class GenericFilterBean
/*  34:    */   implements Filter, BeanNameAware, EnvironmentAware, ServletContextAware, InitializingBean, DisposableBean
/*  35:    */ {
/*  36: 82 */   protected final Log logger = LogFactory.getLog(getClass());
/*  37: 88 */   private final Set<String> requiredProperties = new HashSet();
/*  38:    */   private FilterConfig filterConfig;
/*  39:    */   private String beanName;
/*  40: 94 */   private Environment environment = new StandardServletEnvironment();
/*  41:    */   private ServletContext servletContext;
/*  42:    */   
/*  43:    */   public final void setBeanName(String beanName)
/*  44:    */   {
/*  45:107 */     this.beanName = beanName;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setEnvironment(Environment environment)
/*  49:    */   {
/*  50:120 */     this.environment = environment;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public final void setServletContext(ServletContext servletContext)
/*  54:    */   {
/*  55:131 */     this.servletContext = servletContext;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void afterPropertiesSet()
/*  59:    */     throws ServletException
/*  60:    */   {
/*  61:143 */     initFilterBean();
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected final void addRequiredProperty(String property)
/*  65:    */   {
/*  66:157 */     this.requiredProperties.add(property);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public final void init(FilterConfig filterConfig)
/*  70:    */     throws ServletException
/*  71:    */   {
/*  72:170 */     Assert.notNull(filterConfig, "FilterConfig must not be null");
/*  73:171 */     if (this.logger.isDebugEnabled()) {
/*  74:172 */       this.logger.debug("Initializing filter '" + filterConfig.getFilterName() + "'");
/*  75:    */     }
/*  76:175 */     this.filterConfig = filterConfig;
/*  77:    */     try
/*  78:    */     {
/*  79:179 */       PropertyValues pvs = new FilterConfigPropertyValues(filterConfig, this.requiredProperties);
/*  80:180 */       BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/*  81:181 */       ResourceLoader resourceLoader = new ServletContextResourceLoader(filterConfig.getServletContext());
/*  82:182 */       bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, this.environment));
/*  83:183 */       initBeanWrapper(bw);
/*  84:184 */       bw.setPropertyValues(pvs, true);
/*  85:    */     }
/*  86:    */     catch (BeansException ex)
/*  87:    */     {
/*  88:187 */       String msg = "Failed to set bean properties on filter '" + 
/*  89:188 */         filterConfig.getFilterName() + "': " + ex.getMessage();
/*  90:189 */       this.logger.error(msg, ex);
/*  91:190 */       throw new NestedServletException(msg, ex);
/*  92:    */     }
/*  93:194 */     initFilterBean();
/*  94:196 */     if (this.logger.isDebugEnabled()) {
/*  95:197 */       this.logger.debug("Filter '" + filterConfig.getFilterName() + "' configured successfully");
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   protected void initBeanWrapper(BeanWrapper bw)
/* 100:    */     throws BeansException
/* 101:    */   {}
/* 102:    */   
/* 103:    */   public final FilterConfig getFilterConfig()
/* 104:    */   {
/* 105:222 */     return this.filterConfig;
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected final String getFilterName()
/* 109:    */   {
/* 110:237 */     return this.filterConfig != null ? this.filterConfig.getFilterName() : this.beanName;
/* 111:    */   }
/* 112:    */   
/* 113:    */   protected final ServletContext getServletContext()
/* 114:    */   {
/* 115:252 */     return this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext;
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected void initFilterBean()
/* 119:    */     throws ServletException
/* 120:    */   {}
/* 121:    */   
/* 122:    */   public void destroy() {}
/* 123:    */   
/* 124:    */   private static class FilterConfigPropertyValues
/* 125:    */     extends MutablePropertyValues
/* 126:    */   {
/* 127:    */     public FilterConfigPropertyValues(FilterConfig config, Set<String> requiredProperties)
/* 128:    */       throws ServletException
/* 129:    */     {
/* 130:297 */       Set<String> missingProps = (requiredProperties != null) && (!requiredProperties.isEmpty()) ? 
/* 131:298 */         new HashSet(requiredProperties) : null;
/* 132:    */       
/* 133:300 */       Enumeration<?> en = config.getInitParameterNames();
/* 134:301 */       while (en.hasMoreElements())
/* 135:    */       {
/* 136:302 */         String property = (String)en.nextElement();
/* 137:303 */         Object value = config.getInitParameter(property);
/* 138:304 */         addPropertyValue(new PropertyValue(property, value));
/* 139:305 */         if (missingProps != null) {
/* 140:306 */           missingProps.remove(property);
/* 141:    */         }
/* 142:    */       }
/* 143:311 */       if ((missingProps != null) && (missingProps.size() > 0)) {
/* 144:312 */         throw new ServletException(
/* 145:313 */           "Initialization from FilterConfig for filter '" + config.getFilterName() + 
/* 146:314 */           "' failed; the following required properties were missing: " + 
/* 147:315 */           StringUtils.collectionToDelimitedString(missingProps, ", "));
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.GenericFilterBean
 * JD-Core Version:    0.7.0.1
 */