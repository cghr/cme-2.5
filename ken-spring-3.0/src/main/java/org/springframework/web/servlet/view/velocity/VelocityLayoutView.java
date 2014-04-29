/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import java.io.StringWriter;
/*   4:    */ import java.util.Locale;
/*   5:    */ import javax.servlet.http.HttpServletResponse;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.velocity.Template;
/*   8:    */ import org.apache.velocity.context.Context;
/*   9:    */ import org.apache.velocity.exception.ResourceNotFoundException;
/*  10:    */ import org.springframework.core.NestedIOException;
/*  11:    */ 
/*  12:    */ public class VelocityLayoutView
/*  13:    */   extends VelocityToolboxView
/*  14:    */ {
/*  15:    */   public static final String DEFAULT_LAYOUT_URL = "layout.vm";
/*  16:    */   public static final String DEFAULT_LAYOUT_KEY = "layout";
/*  17:    */   public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";
/*  18: 72 */   private String layoutUrl = "layout.vm";
/*  19: 74 */   private String layoutKey = "layout";
/*  20: 76 */   private String screenContentKey = "screen_content";
/*  21:    */   
/*  22:    */   public void setLayoutUrl(String layoutUrl)
/*  23:    */   {
/*  24: 85 */     this.layoutUrl = layoutUrl;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setLayoutKey(String layoutKey)
/*  28:    */   {
/*  29: 99 */     this.layoutKey = layoutKey;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setScreenContentKey(String screenContentKey)
/*  33:    */   {
/*  34:111 */     this.screenContentKey = screenContentKey;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public boolean checkResource(Locale locale)
/*  38:    */     throws Exception
/*  39:    */   {
/*  40:123 */     if (!super.checkResource(locale)) {
/*  41:124 */       return false;
/*  42:    */     }
/*  43:    */     try
/*  44:    */     {
/*  45:129 */       getTemplate(this.layoutUrl);
/*  46:130 */       return true;
/*  47:    */     }
/*  48:    */     catch (ResourceNotFoundException ex)
/*  49:    */     {
/*  50:133 */       throw new NestedIOException("Cannot find Velocity template for URL [" + this.layoutUrl + 
/*  51:134 */         "]: Did you specify the correct resource loader path?", ex);
/*  52:    */     }
/*  53:    */     catch (Exception ex)
/*  54:    */     {
/*  55:137 */       throw new NestedIOException(
/*  56:138 */         "Could not load Velocity template for URL [" + this.layoutUrl + "]", ex);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void doRender(Context context, HttpServletResponse response)
/*  61:    */     throws Exception
/*  62:    */   {
/*  63:150 */     renderScreenContent(context);
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68:    */ 
/*  69:156 */     String layoutUrlToUse = (String)context.get(this.layoutKey);
/*  70:157 */     if (layoutUrlToUse != null)
/*  71:    */     {
/*  72:158 */       if (this.logger.isDebugEnabled()) {
/*  73:159 */         this.logger.debug("Screen content template has requested layout [" + layoutUrlToUse + "]");
/*  74:    */       }
/*  75:    */     }
/*  76:    */     else {
/*  77:164 */       layoutUrlToUse = this.layoutUrl;
/*  78:    */     }
/*  79:167 */     mergeTemplate(getTemplate(layoutUrlToUse), context, response);
/*  80:    */   }
/*  81:    */   
/*  82:    */   private void renderScreenContent(Context velocityContext)
/*  83:    */     throws Exception
/*  84:    */   {
/*  85:174 */     if (this.logger.isDebugEnabled()) {
/*  86:175 */       this.logger.debug("Rendering screen content template [" + getUrl() + "]");
/*  87:    */     }
/*  88:178 */     StringWriter sw = new StringWriter();
/*  89:179 */     Template screenContentTemplate = getTemplate(getUrl());
/*  90:180 */     screenContentTemplate.merge(velocityContext, sw);
/*  91:    */     
/*  92:    */ 
/*  93:183 */     velocityContext.put(this.screenContentKey, sw.toString());
/*  94:    */   }
/*  95:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityLayoutView
 * JD-Core Version:    0.7.0.1
 */