/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.velocity.Template;
/*  10:    */ import org.apache.velocity.VelocityContext;
/*  11:    */ import org.apache.velocity.app.VelocityEngine;
/*  12:    */ import org.apache.velocity.context.Context;
/*  13:    */ import org.apache.velocity.exception.MethodInvocationException;
/*  14:    */ import org.apache.velocity.exception.ResourceNotFoundException;
/*  15:    */ import org.apache.velocity.tools.generic.DateTool;
/*  16:    */ import org.apache.velocity.tools.generic.NumberTool;
/*  17:    */ import org.springframework.beans.BeansException;
/*  18:    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*  19:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  20:    */ import org.springframework.context.ApplicationContextException;
/*  21:    */ import org.springframework.core.NestedIOException;
/*  22:    */ import org.springframework.web.servlet.support.RequestContextUtils;
/*  23:    */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*  24:    */ import org.springframework.web.util.NestedServletException;
/*  25:    */ 
/*  26:    */ public class VelocityView
/*  27:    */   extends AbstractTemplateView
/*  28:    */ {
/*  29:    */   private Map<String, Class> toolAttributes;
/*  30:    */   private String dateToolAttribute;
/*  31:    */   private String numberToolAttribute;
/*  32:    */   private String encoding;
/*  33: 96 */   private boolean cacheTemplate = false;
/*  34:    */   private VelocityEngine velocityEngine;
/*  35:    */   private Template template;
/*  36:    */   
/*  37:    */   public void setToolAttributes(Map<String, Class> toolAttributes)
/*  38:    */   {
/*  39:131 */     this.toolAttributes = toolAttributes;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setDateToolAttribute(String dateToolAttribute)
/*  43:    */   {
/*  44:145 */     this.dateToolAttribute = dateToolAttribute;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setNumberToolAttribute(String numberToolAttribute)
/*  48:    */   {
/*  49:159 */     this.numberToolAttribute = numberToolAttribute;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setEncoding(String encoding)
/*  53:    */   {
/*  54:169 */     this.encoding = encoding;
/*  55:    */   }
/*  56:    */   
/*  57:    */   protected String getEncoding()
/*  58:    */   {
/*  59:176 */     return this.encoding;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setCacheTemplate(boolean cacheTemplate)
/*  63:    */   {
/*  64:187 */     this.cacheTemplate = cacheTemplate;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected boolean isCacheTemplate()
/*  68:    */   {
/*  69:194 */     return this.cacheTemplate;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setVelocityEngine(VelocityEngine velocityEngine)
/*  73:    */   {
/*  74:204 */     this.velocityEngine = velocityEngine;
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected VelocityEngine getVelocityEngine()
/*  78:    */   {
/*  79:211 */     return this.velocityEngine;
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected void initApplicationContext()
/*  83:    */     throws BeansException
/*  84:    */   {
/*  85:221 */     super.initApplicationContext();
/*  86:223 */     if (getVelocityEngine() == null) {
/*  87:225 */       setVelocityEngine(autodetectVelocityEngine());
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected VelocityEngine autodetectVelocityEngine()
/*  92:    */     throws BeansException
/*  93:    */   {
/*  94:    */     try
/*  95:    */     {
/*  96:239 */       VelocityConfig velocityConfig = (VelocityConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(
/*  97:240 */         getApplicationContext(), VelocityConfig.class, true, false);
/*  98:241 */       return velocityConfig.getVelocityEngine();
/*  99:    */     }
/* 100:    */     catch (NoSuchBeanDefinitionException ex)
/* 101:    */     {
/* 102:244 */       throw new ApplicationContextException(
/* 103:245 */         "Must define a single VelocityConfig bean in this web application context (may be inherited): VelocityConfigurer is the usual implementation. This bean may be given any name.", 
/* 104:    */         
/* 105:247 */         ex);
/* 106:    */     }
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean checkResource(Locale locale)
/* 110:    */     throws Exception
/* 111:    */   {
/* 112:    */     try
/* 113:    */     {
/* 114:260 */       this.template = getTemplate(getUrl());
/* 115:261 */       return true;
/* 116:    */     }
/* 117:    */     catch (ResourceNotFoundException localResourceNotFoundException)
/* 118:    */     {
/* 119:264 */       if (this.logger.isDebugEnabled()) {
/* 120:265 */         this.logger.debug("No Velocity view found for URL: " + getUrl());
/* 121:    */       }
/* 122:267 */       return false;
/* 123:    */     }
/* 124:    */     catch (Exception ex)
/* 125:    */     {
/* 126:270 */       throw new NestedIOException(
/* 127:271 */         "Could not load Velocity template for URL [" + getUrl() + "]", ex);
/* 128:    */     }
/* 129:    */   }
/* 130:    */   
/* 131:    */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 132:    */     throws Exception
/* 133:    */   {
/* 134:285 */     exposeHelpers(model, request);
/* 135:    */     
/* 136:287 */     Context velocityContext = createVelocityContext(model, request, response);
/* 137:288 */     exposeHelpers(velocityContext, request, response);
/* 138:289 */     exposeToolAttributes(velocityContext, request);
/* 139:    */     
/* 140:291 */     doRender(velocityContext, response);
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request)
/* 144:    */     throws Exception
/* 145:    */   {}
/* 146:    */   
/* 147:    */   protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/* 148:    */     throws Exception
/* 149:    */   {
/* 150:330 */     return createVelocityContext(model);
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected Context createVelocityContext(Map<String, Object> model)
/* 154:    */     throws Exception
/* 155:    */   {
/* 156:345 */     return new VelocityContext(model);
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected void exposeHelpers(Context velocityContext, HttpServletRequest request, HttpServletResponse response)
/* 160:    */     throws Exception
/* 161:    */   {
/* 162:364 */     exposeHelpers(velocityContext, request);
/* 163:    */   }
/* 164:    */   
/* 165:    */   protected void exposeHelpers(Context velocityContext, HttpServletRequest request)
/* 166:    */     throws Exception
/* 167:    */   {}
/* 168:    */   
/* 169:    */   protected void exposeToolAttributes(Context velocityContext, HttpServletRequest request)
/* 170:    */     throws Exception
/* 171:    */   {
/* 172:394 */     if (this.toolAttributes != null) {
/* 173:395 */       for (Map.Entry<String, Class> entry : this.toolAttributes.entrySet())
/* 174:    */       {
/* 175:396 */         String attributeName = (String)entry.getKey();
/* 176:397 */         Class toolClass = (Class)entry.getValue();
/* 177:    */         try
/* 178:    */         {
/* 179:399 */           Object tool = toolClass.newInstance();
/* 180:400 */           initTool(tool, velocityContext);
/* 181:401 */           velocityContext.put(attributeName, tool);
/* 182:    */         }
/* 183:    */         catch (Exception ex)
/* 184:    */         {
/* 185:404 */           throw new NestedServletException("Could not instantiate Velocity tool '" + attributeName + "'", ex);
/* 186:    */         }
/* 187:    */       }
/* 188:    */     }
/* 189:410 */     if ((this.dateToolAttribute != null) || (this.numberToolAttribute != null))
/* 190:    */     {
/* 191:411 */       Locale locale = RequestContextUtils.getLocale(request);
/* 192:412 */       if (this.dateToolAttribute != null) {
/* 193:413 */         velocityContext.put(this.dateToolAttribute, new LocaleAwareDateTool(locale, null));
/* 194:    */       }
/* 195:415 */       if (this.numberToolAttribute != null) {
/* 196:416 */         velocityContext.put(this.numberToolAttribute, new LocaleAwareNumberTool(locale, null));
/* 197:    */       }
/* 198:    */     }
/* 199:    */   }
/* 200:    */   
/* 201:    */   protected void initTool(Object tool, Context velocityContext)
/* 202:    */     throws Exception
/* 203:    */   {}
/* 204:    */   
/* 205:    */   protected void doRender(Context context, HttpServletResponse response)
/* 206:    */     throws Exception
/* 207:    */   {
/* 208:459 */     if (this.logger.isDebugEnabled()) {
/* 209:460 */       this.logger.debug("Rendering Velocity template [" + getUrl() + "] in VelocityView '" + getBeanName() + "'");
/* 210:    */     }
/* 211:462 */     mergeTemplate(getTemplate(), context, response);
/* 212:    */   }
/* 213:    */   
/* 214:    */   protected Template getTemplate()
/* 215:    */     throws Exception
/* 216:    */   {
/* 217:480 */     if ((isCacheTemplate()) && (this.template != null)) {
/* 218:481 */       return this.template;
/* 219:    */     }
/* 220:484 */     return getTemplate(getUrl());
/* 221:    */   }
/* 222:    */   
/* 223:    */   protected Template getTemplate(String name)
/* 224:    */     throws Exception
/* 225:    */   {
/* 226:499 */     return getEncoding() != null ? 
/* 227:500 */       getVelocityEngine().getTemplate(name, getEncoding()) : 
/* 228:501 */       getVelocityEngine().getTemplate(name);
/* 229:    */   }
/* 230:    */   
/* 231:    */   protected void mergeTemplate(Template template, Context context, HttpServletResponse response)
/* 232:    */     throws Exception
/* 233:    */   {
/* 234:    */     try
/* 235:    */     {
/* 236:517 */       template.merge(context, response.getWriter());
/* 237:    */     }
/* 238:    */     catch (MethodInvocationException ex)
/* 239:    */     {
/* 240:520 */       Throwable cause = ex.getWrappedThrowable();
/* 241:521 */       throw new NestedServletException(
/* 242:522 */         "Method invocation failed during rendering of Velocity view with name '" + 
/* 243:523 */         getBeanName() + "': " + ex.getMessage() + "; reference [" + ex.getReferenceName() + 
/* 244:524 */         "], method '" + ex.getMethodName() + "'", 
/* 245:525 */         cause == null ? ex : cause);
/* 246:    */     }
/* 247:    */   }
/* 248:    */   
/* 249:    */   private static class LocaleAwareDateTool
/* 250:    */     extends DateTool
/* 251:    */   {
/* 252:    */     private final Locale locale;
/* 253:    */     
/* 254:    */     private LocaleAwareDateTool(Locale locale)
/* 255:    */     {
/* 256:540 */       this.locale = locale;
/* 257:    */     }
/* 258:    */     
/* 259:    */     public Locale getLocale()
/* 260:    */     {
/* 261:545 */       return this.locale;
/* 262:    */     }
/* 263:    */   }
/* 264:    */   
/* 265:    */   private static class LocaleAwareNumberTool
/* 266:    */     extends NumberTool
/* 267:    */   {
/* 268:    */     private final Locale locale;
/* 269:    */     
/* 270:    */     private LocaleAwareNumberTool(Locale locale)
/* 271:    */     {
/* 272:560 */       this.locale = locale;
/* 273:    */     }
/* 274:    */     
/* 275:    */     public Locale getLocale()
/* 276:    */     {
/* 277:565 */       return this.locale;
/* 278:    */     }
/* 279:    */   }
/* 280:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityView
 * JD-Core Version:    0.7.0.1
 */