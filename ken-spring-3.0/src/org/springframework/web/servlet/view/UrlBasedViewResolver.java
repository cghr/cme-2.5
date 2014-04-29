/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Properties;
/*   7:    */ import org.springframework.beans.BeanUtils;
/*   8:    */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ import org.springframework.core.Ordered;
/*  11:    */ import org.springframework.util.CollectionUtils;
/*  12:    */ import org.springframework.util.PatternMatchUtils;
/*  13:    */ import org.springframework.web.servlet.View;
/*  14:    */ 
/*  15:    */ public class UrlBasedViewResolver
/*  16:    */   extends AbstractCachingViewResolver
/*  17:    */   implements Ordered
/*  18:    */ {
/*  19:    */   public static final String REDIRECT_URL_PREFIX = "redirect:";
/*  20:    */   public static final String FORWARD_URL_PREFIX = "forward:";
/*  21:    */   private Class viewClass;
/*  22:105 */   private String prefix = "";
/*  23:107 */   private String suffix = "";
/*  24:109 */   private String[] viewNames = null;
/*  25:    */   private String contentType;
/*  26:113 */   private boolean redirectContextRelative = true;
/*  27:115 */   private boolean redirectHttp10Compatible = true;
/*  28:    */   private String requestContextAttribute;
/*  29:119 */   private int order = 2147483647;
/*  30:122 */   private final Map<String, Object> staticAttributes = new HashMap();
/*  31:    */   private Boolean exposePathVariables;
/*  32:    */   
/*  33:    */   public void setViewClass(Class viewClass)
/*  34:    */   {
/*  35:133 */     if ((viewClass == null) || (!requiredViewClass().isAssignableFrom(viewClass))) {
/*  36:134 */       throw new IllegalArgumentException(
/*  37:135 */         "Given view class [" + (viewClass != null ? viewClass.getName() : null) + 
/*  38:136 */         "] is not of type [" + requiredViewClass().getName() + "]");
/*  39:    */     }
/*  40:138 */     this.viewClass = viewClass;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected Class getViewClass()
/*  44:    */   {
/*  45:145 */     return this.viewClass;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Class requiredViewClass()
/*  49:    */   {
/*  50:154 */     return AbstractUrlBasedView.class;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setPrefix(String prefix)
/*  54:    */   {
/*  55:161 */     this.prefix = (prefix != null ? prefix : "");
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected String getPrefix()
/*  59:    */   {
/*  60:168 */     return this.prefix;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setSuffix(String suffix)
/*  64:    */   {
/*  65:175 */     this.suffix = (suffix != null ? suffix : "");
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected String getSuffix()
/*  69:    */   {
/*  70:182 */     return this.suffix;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setContentType(String contentType)
/*  74:    */   {
/*  75:191 */     this.contentType = contentType;
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected String getContentType()
/*  79:    */   {
/*  80:198 */     return this.contentType;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setRedirectContextRelative(boolean redirectContextRelative)
/*  84:    */   {
/*  85:214 */     this.redirectContextRelative = redirectContextRelative;
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected boolean isRedirectContextRelative()
/*  89:    */   {
/*  90:223 */     return this.redirectContextRelative;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setRedirectHttp10Compatible(boolean redirectHttp10Compatible)
/*  94:    */   {
/*  95:241 */     this.redirectHttp10Compatible = redirectHttp10Compatible;
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected boolean isRedirectHttp10Compatible()
/*  99:    */   {
/* 100:248 */     return this.redirectHttp10Compatible;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void setRequestContextAttribute(String requestContextAttribute)
/* 104:    */   {
/* 105:257 */     this.requestContextAttribute = requestContextAttribute;
/* 106:    */   }
/* 107:    */   
/* 108:    */   protected String getRequestContextAttribute()
/* 109:    */   {
/* 110:264 */     return this.requestContextAttribute;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void setAttributes(Properties props)
/* 114:    */   {
/* 115:279 */     CollectionUtils.mergePropertiesIntoMap(props, this.staticAttributes);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setAttributesMap(Map<String, ?> attributes)
/* 119:    */   {
/* 120:290 */     if (attributes != null) {
/* 121:291 */       this.staticAttributes.putAll(attributes);
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public Map<String, Object> getAttributesMap()
/* 126:    */   {
/* 127:303 */     return this.staticAttributes;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void setViewNames(String[] viewNames)
/* 131:    */   {
/* 132:314 */     this.viewNames = viewNames;
/* 133:    */   }
/* 134:    */   
/* 135:    */   protected String[] getViewNames()
/* 136:    */   {
/* 137:322 */     return this.viewNames;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void setOrder(int order)
/* 141:    */   {
/* 142:330 */     this.order = order;
/* 143:    */   }
/* 144:    */   
/* 145:    */   public int getOrder()
/* 146:    */   {
/* 147:338 */     return this.order;
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void setExposePathVariables(Boolean exposePathVariables)
/* 151:    */   {
/* 152:354 */     this.exposePathVariables = exposePathVariables;
/* 153:    */   }
/* 154:    */   
/* 155:    */   protected void initApplicationContext()
/* 156:    */   {
/* 157:359 */     super.initApplicationContext();
/* 158:360 */     if (getViewClass() == null) {
/* 159:361 */       throw new IllegalArgumentException("Property 'viewClass' is required");
/* 160:    */     }
/* 161:    */   }
/* 162:    */   
/* 163:    */   protected Object getCacheKey(String viewName, Locale locale)
/* 164:    */   {
/* 165:371 */     return viewName;
/* 166:    */   }
/* 167:    */   
/* 168:    */   protected View createView(String viewName, Locale locale)
/* 169:    */     throws Exception
/* 170:    */   {
/* 171:386 */     if (!canHandle(viewName, locale)) {
/* 172:387 */       return null;
/* 173:    */     }
/* 174:390 */     if (viewName.startsWith("redirect:"))
/* 175:    */     {
/* 176:391 */       String redirectUrl = viewName.substring("redirect:".length());
/* 177:392 */       return new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
/* 178:    */     }
/* 179:395 */     if (viewName.startsWith("forward:"))
/* 180:    */     {
/* 181:396 */       String forwardUrl = viewName.substring("forward:".length());
/* 182:397 */       return new InternalResourceView(forwardUrl);
/* 183:    */     }
/* 184:400 */     return super.createView(viewName, locale);
/* 185:    */   }
/* 186:    */   
/* 187:    */   protected boolean canHandle(String viewName, Locale locale)
/* 188:    */   {
/* 189:414 */     String[] viewNames = getViewNames();
/* 190:415 */     return (viewNames == null) || (PatternMatchUtils.simpleMatch(viewNames, viewName));
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected View loadView(String viewName, Locale locale)
/* 194:    */     throws Exception
/* 195:    */   {
/* 196:435 */     AbstractUrlBasedView view = buildView(viewName);
/* 197:436 */     View result = (View)getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
/* 198:437 */     return view.checkResource(locale) ? result : null;
/* 199:    */   }
/* 200:    */   
/* 201:    */   protected AbstractUrlBasedView buildView(String viewName)
/* 202:    */     throws Exception
/* 203:    */   {
/* 204:455 */     AbstractUrlBasedView view = (AbstractUrlBasedView)BeanUtils.instantiateClass(getViewClass());
/* 205:456 */     view.setUrl(getPrefix() + viewName + getSuffix());
/* 206:457 */     String contentType = getContentType();
/* 207:458 */     if (contentType != null) {
/* 208:459 */       view.setContentType(contentType);
/* 209:    */     }
/* 210:461 */     view.setRequestContextAttribute(getRequestContextAttribute());
/* 211:462 */     view.setAttributesMap(getAttributesMap());
/* 212:463 */     if (this.exposePathVariables != null) {
/* 213:464 */       view.setExposePathVariables(this.exposePathVariables.booleanValue());
/* 214:    */     }
/* 215:466 */     return view;
/* 216:    */   }
/* 217:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.UrlBasedViewResolver
 * JD-Core Version:    0.7.0.1
 */