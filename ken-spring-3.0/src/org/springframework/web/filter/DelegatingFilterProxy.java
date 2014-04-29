/*   1:    */ package org.springframework.web.filter;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.Filter;
/*   5:    */ import javax.servlet.FilterChain;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.ServletRequest;
/*   8:    */ import javax.servlet.ServletResponse;
/*   9:    */ import org.springframework.context.ConfigurableApplicationContext;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.web.context.WebApplicationContext;
/*  12:    */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*  13:    */ 
/*  14:    */ public class DelegatingFilterProxy
/*  15:    */   extends GenericFilterBean
/*  16:    */ {
/*  17:    */   private String contextAttribute;
/*  18:    */   private WebApplicationContext webApplicationContext;
/*  19:    */   private String targetBeanName;
/*  20: 89 */   private boolean targetFilterLifecycle = false;
/*  21:    */   private Filter delegate;
/*  22: 93 */   private final Object delegateMonitor = new Object();
/*  23:    */   
/*  24:    */   public DelegatingFilterProxy() {}
/*  25:    */   
/*  26:    */   public DelegatingFilterProxy(Filter delegate)
/*  27:    */   {
/*  28:118 */     Assert.notNull(delegate, "delegate Filter object must not be null");
/*  29:119 */     this.delegate = delegate;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public DelegatingFilterProxy(String targetBeanName)
/*  33:    */   {
/*  34:136 */     this(targetBeanName, null);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public DelegatingFilterProxy(String targetBeanName, WebApplicationContext wac)
/*  38:    */   {
/*  39:160 */     Assert.hasText(targetBeanName, "target Filter bean name must not be null or empty");
/*  40:161 */     setTargetBeanName(targetBeanName);
/*  41:162 */     this.webApplicationContext = wac;
/*  42:163 */     if (wac != null) {
/*  43:164 */       setEnvironment(wac.getEnvironment());
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setContextAttribute(String contextAttribute)
/*  48:    */   {
/*  49:173 */     this.contextAttribute = contextAttribute;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getContextAttribute()
/*  53:    */   {
/*  54:181 */     return this.contextAttribute;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setTargetBeanName(String targetBeanName)
/*  58:    */   {
/*  59:191 */     this.targetBeanName = targetBeanName;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected String getTargetBeanName()
/*  63:    */   {
/*  64:198 */     return this.targetBeanName;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setTargetFilterLifecycle(boolean targetFilterLifecycle)
/*  68:    */   {
/*  69:210 */     this.targetFilterLifecycle = targetFilterLifecycle;
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected boolean isTargetFilterLifecycle()
/*  73:    */   {
/*  74:218 */     return this.targetFilterLifecycle;
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void initFilterBean()
/*  78:    */     throws ServletException
/*  79:    */   {
/*  80:224 */     synchronized (this.delegateMonitor)
/*  81:    */     {
/*  82:225 */       if (this.delegate == null)
/*  83:    */       {
/*  84:227 */         if (this.targetBeanName == null) {
/*  85:228 */           this.targetBeanName = getFilterName();
/*  86:    */         }
/*  87:234 */         WebApplicationContext wac = findWebApplicationContext();
/*  88:235 */         if (wac != null) {
/*  89:236 */           this.delegate = initDelegate(wac);
/*  90:    */         }
/*  91:    */       }
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
/*  96:    */     throws ServletException, IOException
/*  97:    */   {
/*  98:246 */     Filter delegateToUse = null;
/*  99:247 */     synchronized (this.delegateMonitor)
/* 100:    */     {
/* 101:248 */       if (this.delegate == null)
/* 102:    */       {
/* 103:249 */         WebApplicationContext wac = findWebApplicationContext();
/* 104:250 */         if (wac == null) {
/* 105:251 */           throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
/* 106:    */         }
/* 107:253 */         this.delegate = initDelegate(wac);
/* 108:    */       }
/* 109:255 */       delegateToUse = this.delegate;
/* 110:    */     }
/* 111:259 */     invokeDelegate(delegateToUse, request, response, filterChain);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public void destroy()
/* 115:    */   {
/* 116:264 */     Filter delegateToUse = null;
/* 117:265 */     synchronized (this.delegateMonitor)
/* 118:    */     {
/* 119:266 */       delegateToUse = this.delegate;
/* 120:    */     }
/* 121:268 */     if (delegateToUse != null) {
/* 122:269 */       destroyDelegate(delegateToUse);
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected WebApplicationContext findWebApplicationContext()
/* 127:    */   {
/* 128:292 */     if (this.webApplicationContext != null)
/* 129:    */     {
/* 130:294 */       if (((this.webApplicationContext instanceof ConfigurableApplicationContext)) && 
/* 131:295 */         (!((ConfigurableApplicationContext)this.webApplicationContext).isActive())) {
/* 132:297 */         ((ConfigurableApplicationContext)this.webApplicationContext).refresh();
/* 133:    */       }
/* 134:300 */       return this.webApplicationContext;
/* 135:    */     }
/* 136:302 */     String attrName = getContextAttribute();
/* 137:303 */     if (attrName != null) {
/* 138:304 */       return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/* 139:    */     }
/* 140:307 */     return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/* 141:    */   }
/* 142:    */   
/* 143:    */   protected Filter initDelegate(WebApplicationContext wac)
/* 144:    */     throws ServletException
/* 145:    */   {
/* 146:326 */     Filter delegate = (Filter)wac.getBean(getTargetBeanName(), Filter.class);
/* 147:327 */     if (isTargetFilterLifecycle()) {
/* 148:328 */       delegate.init(getFilterConfig());
/* 149:    */     }
/* 150:330 */     return delegate;
/* 151:    */   }
/* 152:    */   
/* 153:    */   protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
/* 154:    */     throws ServletException, IOException
/* 155:    */   {
/* 156:346 */     delegate.doFilter(request, response, filterChain);
/* 157:    */   }
/* 158:    */   
/* 159:    */   protected void destroyDelegate(Filter delegate)
/* 160:    */   {
/* 161:357 */     if (isTargetFilterLifecycle()) {
/* 162:358 */       delegate.destroy();
/* 163:    */     }
/* 164:    */   }
/* 165:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.DelegatingFilterProxy
 * JD-Core Version:    0.7.0.1
 */