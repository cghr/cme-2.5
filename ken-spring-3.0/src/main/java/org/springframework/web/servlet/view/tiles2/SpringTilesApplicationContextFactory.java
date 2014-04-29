/*   1:    */ package org.springframework.web.servlet.view.tiles2;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.net.URL;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.LinkedHashMap;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.servlet.ServletContext;
/*  12:    */ import org.apache.tiles.Initializable;
/*  13:    */ import org.apache.tiles.TilesApplicationContext;
/*  14:    */ import org.apache.tiles.context.AbstractTilesApplicationContextFactory;
/*  15:    */ import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
/*  16:    */ import org.springframework.core.io.Resource;
/*  17:    */ import org.springframework.core.io.support.ResourcePatternResolver;
/*  18:    */ import org.springframework.web.context.support.ServletContextResourcePatternResolver;
/*  19:    */ 
/*  20:    */ public class SpringTilesApplicationContextFactory
/*  21:    */   extends AbstractTilesApplicationContextFactory
/*  22:    */   implements Initializable
/*  23:    */ {
/*  24:    */   private Map<String, String> params;
/*  25:    */   
/*  26:    */   public void init(Map<String, String> params)
/*  27:    */   {
/*  28: 51 */     this.params = params;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public TilesApplicationContext createApplicationContext(Object context)
/*  32:    */   {
/*  33: 55 */     return new SpringWildcardServletTilesApplicationContext((ServletContext)context, this.params);
/*  34:    */   }
/*  35:    */   
/*  36:    */   private static class SpringWildcardServletTilesApplicationContext
/*  37:    */     extends ServletTilesApplicationContext
/*  38:    */   {
/*  39:    */     private final Map<String, String> mergedInitParams;
/*  40:    */     private final ResourcePatternResolver resolver;
/*  41:    */     
/*  42:    */     public SpringWildcardServletTilesApplicationContext(ServletContext servletContext, Map<String, String> params)
/*  43:    */     {
/*  44: 70 */       super();
/*  45: 71 */       this.mergedInitParams = new LinkedHashMap();
/*  46: 72 */       Enumeration initParamNames = servletContext.getInitParameterNames();
/*  47: 73 */       while (initParamNames.hasMoreElements())
/*  48:    */       {
/*  49: 74 */         String initParamName = (String)initParamNames.nextElement();
/*  50: 75 */         this.mergedInitParams.put(initParamName, servletContext.getInitParameter(initParamName));
/*  51:    */       }
/*  52: 77 */       if (params != null) {
/*  53: 78 */         this.mergedInitParams.putAll(params);
/*  54:    */       }
/*  55: 80 */       this.resolver = new ServletContextResourcePatternResolver(servletContext);
/*  56:    */     }
/*  57:    */     
/*  58:    */     public Map<String, String> getInitParams()
/*  59:    */     {
/*  60: 85 */       return this.mergedInitParams;
/*  61:    */     }
/*  62:    */     
/*  63:    */     public URL getResource(String path)
/*  64:    */       throws IOException
/*  65:    */     {
/*  66: 90 */       URL retValue = null;
/*  67: 91 */       Set<URL> urlSet = getResources(path);
/*  68: 92 */       if ((urlSet != null) && (!urlSet.isEmpty())) {
/*  69: 93 */         retValue = (URL)urlSet.iterator().next();
/*  70:    */       }
/*  71: 95 */       return retValue;
/*  72:    */     }
/*  73:    */     
/*  74:    */     public Set<URL> getResources(String path)
/*  75:    */       throws IOException
/*  76:    */     {
/*  77:100 */       Set<URL> urlSet = null;
/*  78:101 */       Resource[] resources = this.resolver.getResources(path);
/*  79:102 */       if ((resources != null) && (resources.length > 0))
/*  80:    */       {
/*  81:103 */         urlSet = new HashSet();
/*  82:104 */         for (Resource resource : resources) {
/*  83:105 */           urlSet.add(resource.getURL());
/*  84:    */         }
/*  85:    */       }
/*  86:108 */       return urlSet;
/*  87:    */     }
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.SpringTilesApplicationContextFactory
 * JD-Core Version:    0.7.0.1
 */