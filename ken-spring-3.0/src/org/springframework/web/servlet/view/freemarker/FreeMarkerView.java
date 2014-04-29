/*   1:    */ package org.springframework.web.servlet.view.freemarker;
/*   2:    */ 
/*   3:    */ import freemarker.core.ParseException;
/*   4:    */ import freemarker.ext.jsp.TaglibFactory;
/*   5:    */ import freemarker.ext.servlet.AllHttpScopesHashModel;
/*   6:    */ import freemarker.ext.servlet.HttpRequestHashModel;
/*   7:    */ import freemarker.ext.servlet.HttpRequestParametersHashModel;
/*   8:    */ import freemarker.ext.servlet.HttpSessionHashModel;
/*   9:    */ import freemarker.ext.servlet.ServletContextHashModel;
/*  10:    */ import freemarker.template.Configuration;
/*  11:    */ import freemarker.template.ObjectWrapper;
/*  12:    */ import freemarker.template.SimpleHash;
/*  13:    */ import freemarker.template.Template;
/*  14:    */ import freemarker.template.TemplateException;
/*  15:    */ import java.io.FileNotFoundException;
/*  16:    */ import java.io.IOException;
/*  17:    */ import java.util.Collections;
/*  18:    */ import java.util.Enumeration;
/*  19:    */ import java.util.HashSet;
/*  20:    */ import java.util.Locale;
/*  21:    */ import java.util.Map;
/*  22:    */ import javax.servlet.GenericServlet;
/*  23:    */ import javax.servlet.ServletConfig;
/*  24:    */ import javax.servlet.ServletContext;
/*  25:    */ import javax.servlet.ServletException;
/*  26:    */ import javax.servlet.ServletRequest;
/*  27:    */ import javax.servlet.ServletResponse;
/*  28:    */ import javax.servlet.http.HttpServletRequest;
/*  29:    */ import javax.servlet.http.HttpServletResponse;
/*  30:    */ import javax.servlet.http.HttpSession;
/*  31:    */ import org.apache.commons.logging.Log;
/*  32:    */ import org.springframework.beans.BeansException;
/*  33:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  34:    */ import org.springframework.beans.factory.BeanInitializationException;
/*  35:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  36:    */ import org.springframework.context.ApplicationContextException;
/*  37:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  38:    */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*  39:    */ 
/*  40:    */ public class FreeMarkerView
/*  41:    */   extends AbstractTemplateView
/*  42:    */ {
/*  43:    */   private String encoding;
/*  44:    */   private Configuration configuration;
/*  45:    */   private TaglibFactory taglibFactory;
/*  46:    */   private ServletContextHashModel servletContextHashModel;
/*  47:    */   
/*  48:    */   public void setEncoding(String encoding)
/*  49:    */   {
/*  50:104 */     this.encoding = encoding;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected String getEncoding()
/*  54:    */   {
/*  55:111 */     return this.encoding;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setConfiguration(Configuration configuration)
/*  59:    */   {
/*  60:124 */     this.configuration = configuration;
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected Configuration getConfiguration()
/*  64:    */   {
/*  65:131 */     return this.configuration;
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected void initServletContext(ServletContext servletContext)
/*  69:    */     throws BeansException
/*  70:    */   {
/*  71:145 */     if (getConfiguration() != null)
/*  72:    */     {
/*  73:146 */       this.taglibFactory = new TaglibFactory(servletContext);
/*  74:    */     }
/*  75:    */     else
/*  76:    */     {
/*  77:149 */       FreeMarkerConfig config = autodetectConfiguration();
/*  78:150 */       setConfiguration(config.getConfiguration());
/*  79:151 */       this.taglibFactory = config.getTaglibFactory();
/*  80:    */     }
/*  81:154 */     GenericServlet servlet = new GenericServletAdapter(null);
/*  82:    */     try
/*  83:    */     {
/*  84:156 */       servlet.init(new DelegatingServletConfig(null));
/*  85:    */     }
/*  86:    */     catch (ServletException ex)
/*  87:    */     {
/*  88:159 */       throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
/*  89:    */     }
/*  90:161 */     this.servletContextHashModel = new ServletContextHashModel(servlet, getObjectWrapper());
/*  91:    */   }
/*  92:    */   
/*  93:    */   protected FreeMarkerConfig autodetectConfiguration()
/*  94:    */     throws BeansException
/*  95:    */   {
/*  96:    */     try
/*  97:    */     {
/*  98:173 */       return (FreeMarkerConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(
/*  99:174 */         getApplicationContext(), FreeMarkerConfig.class, true, false);
/* 100:    */     }
/* 101:    */     catch (NoSuchBeanDefinitionException ex)
/* 102:    */     {
/* 103:177 */       throw new ApplicationContextException(
/* 104:178 */         "Must define a single FreeMarkerConfig bean in this web application context (may be inherited): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.", 
/* 105:    */         
/* 106:180 */         ex);
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected ObjectWrapper getObjectWrapper()
/* 111:    */   {
/* 112:190 */     ObjectWrapper ow = getConfiguration().getObjectWrapper();
/* 113:191 */     return ow != null ? ow : ObjectWrapper.DEFAULT_WRAPPER;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public boolean checkResource(Locale locale)
/* 117:    */     throws Exception
/* 118:    */   {
/* 119:    */     try
/* 120:    */     {
/* 121:203 */       getTemplate(getUrl(), locale);
/* 122:204 */       return true;
/* 123:    */     }
/* 124:    */     catch (FileNotFoundException localFileNotFoundException)
/* 125:    */     {
/* 126:207 */       if (this.logger.isDebugEnabled()) {
/* 127:208 */         this.logger.debug("No FreeMarker view found for URL: " + getUrl());
/* 128:    */       }
/* 129:210 */       return false;
/* 130:    */     }
/* 131:    */     catch (ParseException ex)
/* 132:    */     {
/* 133:213 */       throw new ApplicationContextException(
/* 134:214 */         "Failed to parse FreeMarker template for URL [" + getUrl() + "]", ex);
/* 135:    */     }
/* 136:    */     catch (IOException ex)
/* 137:    */     {
/* 138:217 */       throw new ApplicationContextException(
/* 139:218 */         "Could not load FreeMarker template for URL [" + getUrl() + "]", ex);
/* 140:    */     }
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 144:    */     throws Exception
/* 145:    */   {
/* 146:232 */     exposeHelpers(model, request);
/* 147:233 */     doRender(model, request, response);
/* 148:    */   }
/* 149:    */   
/* 150:    */   protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request)
/* 151:    */     throws Exception
/* 152:    */   {}
/* 153:    */   
/* 154:    */   protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 155:    */     throws Exception
/* 156:    */   {
/* 157:274 */     exposeModelAsRequestAttributes(model, request);
/* 158:    */     
/* 159:276 */     SimpleHash fmModel = buildTemplateModel(model, request, response);
/* 160:278 */     if (this.logger.isDebugEnabled()) {
/* 161:279 */       this.logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
/* 162:    */     }
/* 163:282 */     Locale locale = RequestContextUtils.getLocale(request);
/* 164:283 */     processTemplate(getTemplate(locale), fmModel, response);
/* 165:    */   }
/* 166:    */   
/* 167:    */   protected SimpleHash buildTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 168:    */   {
/* 169:295 */     AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
/* 170:296 */     fmModel.put("JspTaglibs", this.taglibFactory);
/* 171:297 */     fmModel.put("Application", this.servletContextHashModel);
/* 172:298 */     fmModel.put("Session", buildSessionModel(request, response));
/* 173:299 */     fmModel.put("Request", new HttpRequestHashModel(request, response, getObjectWrapper()));
/* 174:300 */     fmModel.put("RequestParameters", new HttpRequestParametersHashModel(request));
/* 175:301 */     fmModel.putAll(model);
/* 176:302 */     return fmModel;
/* 177:    */   }
/* 178:    */   
/* 179:    */   private HttpSessionHashModel buildSessionModel(HttpServletRequest request, HttpServletResponse response)
/* 180:    */   {
/* 181:313 */     HttpSession session = request.getSession(false);
/* 182:314 */     if (session != null) {
/* 183:315 */       return new HttpSessionHashModel(session, getObjectWrapper());
/* 184:    */     }
/* 185:318 */     return new HttpSessionHashModel(null, request, response, getObjectWrapper());
/* 186:    */   }
/* 187:    */   
/* 188:    */   protected Template getTemplate(Locale locale)
/* 189:    */     throws IOException
/* 190:    */   {
/* 191:334 */     return getTemplate(getUrl(), locale);
/* 192:    */   }
/* 193:    */   
/* 194:    */   protected Template getTemplate(String name, Locale locale)
/* 195:    */     throws IOException
/* 196:    */   {
/* 197:348 */     return getEncoding() != null ? 
/* 198:349 */       getConfiguration().getTemplate(name, locale, getEncoding()) : 
/* 199:350 */       getConfiguration().getTemplate(name, locale);
/* 200:    */   }
/* 201:    */   
/* 202:    */   protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
/* 203:    */     throws IOException, TemplateException
/* 204:    */   {
/* 205:366 */     template.process(model, response.getWriter());
/* 206:    */   }
/* 207:    */   
/* 208:    */   private class DelegatingServletConfig
/* 209:    */     implements ServletConfig
/* 210:    */   {
/* 211:    */     private DelegatingServletConfig() {}
/* 212:    */     
/* 213:    */     public String getServletName()
/* 214:    */     {
/* 215:390 */       return FreeMarkerView.this.getBeanName();
/* 216:    */     }
/* 217:    */     
/* 218:    */     public ServletContext getServletContext()
/* 219:    */     {
/* 220:394 */       return FreeMarkerView.this.getServletContext();
/* 221:    */     }
/* 222:    */     
/* 223:    */     public String getInitParameter(String paramName)
/* 224:    */     {
/* 225:398 */       return null;
/* 226:    */     }
/* 227:    */     
/* 228:    */     public Enumeration<String> getInitParameterNames()
/* 229:    */     {
/* 230:402 */       return Collections.enumeration(new HashSet());
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   private static class GenericServletAdapter
/* 235:    */     extends GenericServlet
/* 236:    */   {
/* 237:    */     public void service(ServletRequest servletRequest, ServletResponse servletResponse) {}
/* 238:    */   }
/* 239:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.freemarker.FreeMarkerView
 * JD-Core Version:    0.7.0.1
 */