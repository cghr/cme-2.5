/*   1:    */ package org.springframework.web.multipart.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import javax.servlet.FilterChain;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServletRequest;
/*   7:    */ import javax.servlet.http.HttpServletResponse;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.springframework.web.context.WebApplicationContext;
/*  10:    */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*  11:    */ import org.springframework.web.filter.OncePerRequestFilter;
/*  12:    */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*  13:    */ import org.springframework.web.multipart.MultipartResolver;
/*  14:    */ 
/*  15:    */ public class MultipartFilter
/*  16:    */   extends OncePerRequestFilter
/*  17:    */ {
/*  18:    */   public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "filterMultipartResolver";
/*  19: 68 */   private final MultipartResolver defaultMultipartResolver = new StandardServletMultipartResolver();
/*  20: 70 */   private String multipartResolverBeanName = "filterMultipartResolver";
/*  21:    */   
/*  22:    */   public void setMultipartResolverBeanName(String multipartResolverBeanName)
/*  23:    */   {
/*  24: 78 */     this.multipartResolverBeanName = multipartResolverBeanName;
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected String getMultipartResolverBeanName()
/*  28:    */   {
/*  29: 86 */     return this.multipartResolverBeanName;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*  33:    */     throws ServletException, IOException
/*  34:    */   {
/*  35:102 */     MultipartResolver multipartResolver = lookupMultipartResolver(request);
/*  36:    */     
/*  37:104 */     HttpServletRequest processedRequest = request;
/*  38:105 */     if (multipartResolver.isMultipart(processedRequest))
/*  39:    */     {
/*  40:106 */       if (this.logger.isDebugEnabled()) {
/*  41:107 */         this.logger.debug("Resolving multipart request [" + processedRequest.getRequestURI() + 
/*  42:108 */           "] with MultipartFilter");
/*  43:    */       }
/*  44:110 */       processedRequest = multipartResolver.resolveMultipart(processedRequest);
/*  45:    */     }
/*  46:113 */     else if (this.logger.isDebugEnabled())
/*  47:    */     {
/*  48:114 */       this.logger.debug("Request [" + processedRequest.getRequestURI() + "] is not a multipart request");
/*  49:    */     }
/*  50:    */     try
/*  51:    */     {
/*  52:119 */       filterChain.doFilter(processedRequest, response);
/*  53:    */     }
/*  54:    */     finally
/*  55:    */     {
/*  56:122 */       if ((processedRequest instanceof MultipartHttpServletRequest)) {
/*  57:123 */         multipartResolver.cleanupMultipart((MultipartHttpServletRequest)processedRequest);
/*  58:    */       }
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected MultipartResolver lookupMultipartResolver(HttpServletRequest request)
/*  63:    */   {
/*  64:137 */     return lookupMultipartResolver();
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected MultipartResolver lookupMultipartResolver()
/*  68:    */   {
/*  69:149 */     WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/*  70:150 */     String beanName = getMultipartResolverBeanName();
/*  71:151 */     if ((wac != null) && (wac.containsBean(beanName)))
/*  72:    */     {
/*  73:152 */       if (this.logger.isDebugEnabled()) {
/*  74:153 */         this.logger.debug("Using MultipartResolver '" + beanName + "' for MultipartFilter");
/*  75:    */       }
/*  76:155 */       return (MultipartResolver)wac.getBean(beanName, MultipartResolver.class);
/*  77:    */     }
/*  78:158 */     return this.defaultMultipartResolver;
/*  79:    */   }
/*  80:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.MultipartFilter
 * JD-Core Version:    0.7.0.1
 */