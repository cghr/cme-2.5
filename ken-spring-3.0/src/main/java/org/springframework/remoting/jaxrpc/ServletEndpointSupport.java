/*   1:    */ package org.springframework.remoting.jaxrpc;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import javax.servlet.ServletContext;
/*   5:    */ import javax.xml.rpc.ServiceException;
/*   6:    */ import javax.xml.rpc.server.ServiceLifecycle;
/*   7:    */ import javax.xml.rpc.server.ServletEndpointContext;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.context.ApplicationContext;
/*  11:    */ import org.springframework.context.support.MessageSourceAccessor;
/*  12:    */ import org.springframework.web.context.WebApplicationContext;
/*  13:    */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*  14:    */ import org.springframework.web.util.WebUtils;
/*  15:    */ 
/*  16:    */ @Deprecated
/*  17:    */ public abstract class ServletEndpointSupport
/*  18:    */   implements ServiceLifecycle
/*  19:    */ {
/*  20: 64 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private ServletEndpointContext servletEndpointContext;
/*  22:    */   private WebApplicationContext webApplicationContext;
/*  23:    */   private MessageSourceAccessor messageSourceAccessor;
/*  24:    */   
/*  25:    */   public final void init(Object context)
/*  26:    */     throws ServiceException
/*  27:    */   {
/*  28: 81 */     if (!(context instanceof ServletEndpointContext)) {
/*  29: 82 */       throw new ServiceException("ServletEndpointSupport needs ServletEndpointContext, not [" + context + "]");
/*  30:    */     }
/*  31: 84 */     this.servletEndpointContext = ((ServletEndpointContext)context);
/*  32: 85 */     ServletContext servletContext = this.servletEndpointContext.getServletContext();
/*  33: 86 */     this.webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
/*  34: 87 */     this.messageSourceAccessor = new MessageSourceAccessor(this.webApplicationContext);
/*  35: 88 */     onInit();
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected final ServletEndpointContext getServletEndpointContext()
/*  39:    */   {
/*  40: 95 */     return this.servletEndpointContext;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected final ApplicationContext getApplicationContext()
/*  44:    */   {
/*  45:102 */     return this.webApplicationContext;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected final WebApplicationContext getWebApplicationContext()
/*  49:    */   {
/*  50:109 */     return this.webApplicationContext;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected final MessageSourceAccessor getMessageSourceAccessor()
/*  54:    */   {
/*  55:117 */     return this.messageSourceAccessor;
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected final ServletContext getServletContext()
/*  59:    */   {
/*  60:124 */     return this.webApplicationContext.getServletContext();
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected final File getTempDir()
/*  64:    */   {
/*  65:133 */     return WebUtils.getTempDir(getServletContext());
/*  66:    */   }
/*  67:    */   
/*  68:    */   protected void onInit()
/*  69:    */     throws ServiceException
/*  70:    */   {}
/*  71:    */   
/*  72:    */   public void destroy() {}
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.jaxrpc.ServletEndpointSupport
 * JD-Core Version:    0.7.0.1
 */