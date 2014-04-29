/*   1:    */ package org.springframework.web.servlet.view.tiles2;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletContext;
/*   6:    */ import javax.servlet.ServletException;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import javax.servlet.http.HttpServletResponse;
/*   9:    */ import org.apache.tiles.TilesApplicationContext;
/*  10:    */ import org.apache.tiles.TilesContainer;
/*  11:    */ import org.apache.tiles.context.TilesRequestContext;
/*  12:    */ import org.apache.tiles.definition.DefinitionsFactory;
/*  13:    */ import org.apache.tiles.impl.BasicTilesContainer;
/*  14:    */ import org.apache.tiles.servlet.context.ServletTilesApplicationContext;
/*  15:    */ import org.apache.tiles.servlet.context.ServletTilesRequestContext;
/*  16:    */ import org.apache.tiles.servlet.context.ServletUtil;
/*  17:    */ import org.springframework.web.servlet.support.JstlUtils;
/*  18:    */ import org.springframework.web.servlet.support.RequestContext;
/*  19:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*  20:    */ import org.springframework.web.util.WebUtils;
/*  21:    */ 
/*  22:    */ public class TilesView
/*  23:    */   extends AbstractUrlBasedView
/*  24:    */ {
/*  25: 58 */   private volatile boolean exposeForwardAttributes = false;
/*  26:    */   
/*  27:    */   protected void initServletContext(ServletContext sc)
/*  28:    */   {
/*  29: 71 */     if ((sc.getMajorVersion() == 2) && (sc.getMinorVersion() < 5)) {
/*  30: 72 */       this.exposeForwardAttributes = true;
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean checkResource(final Locale locale)
/*  35:    */     throws Exception
/*  36:    */   {
/*  37: 79 */     TilesContainer container = ServletUtil.getContainer(getServletContext());
/*  38: 80 */     if (!(container instanceof BasicTilesContainer)) {
/*  39: 82 */       return true;
/*  40:    */     }
/*  41: 84 */     BasicTilesContainer basicContainer = (BasicTilesContainer)container;
/*  42: 85 */     TilesApplicationContext appContext = new ServletTilesApplicationContext(getServletContext());
/*  43: 86 */     TilesRequestContext requestContext = new ServletTilesRequestContext(appContext, null, null)
/*  44:    */     {
/*  45:    */       public Locale getRequestLocale()
/*  46:    */       {
/*  47: 89 */         return locale;
/*  48:    */       }
/*  49: 91 */     };
/*  50: 92 */     return basicContainer.getDefinitionsFactory().getDefinition(getUrl(), requestContext) != null;
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  54:    */     throws Exception
/*  55:    */   {
/*  56: 99 */     ServletContext servletContext = getServletContext();
/*  57:100 */     TilesContainer container = ServletUtil.getContainer(servletContext);
/*  58:101 */     if (container == null) {
/*  59:102 */       throw new ServletException("Tiles container is not initialized. Have you added a TilesConfigurer to your web application context?");
/*  60:    */     }
/*  61:106 */     exposeModelAsRequestAttributes(model, request);
/*  62:107 */     JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));
/*  63:109 */     if (!response.isCommitted()) {
/*  64:113 */       if (this.exposeForwardAttributes) {
/*  65:    */         try
/*  66:    */         {
/*  67:115 */           WebUtils.exposeForwardRequestAttributes(request);
/*  68:    */         }
/*  69:    */         catch (Exception localException)
/*  70:    */         {
/*  71:119 */           this.exposeForwardAttributes = false;
/*  72:    */         }
/*  73:    */       }
/*  74:    */     }
/*  75:124 */     container.render(getUrl(), new Object[] { request, response });
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.TilesView
 * JD-Core Version:    0.7.0.1
 */