/*   1:    */ package org.springframework.web.servlet.view.xslt;
/*   2:    */ 
/*   3:    */ import java.util.Properties;
/*   4:    */ import javax.xml.transform.ErrorListener;
/*   5:    */ import javax.xml.transform.URIResolver;
/*   6:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*   7:    */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*   8:    */ 
/*   9:    */ public class XsltViewResolver
/*  10:    */   extends UrlBasedViewResolver
/*  11:    */ {
/*  12:    */   private String sourceKey;
/*  13:    */   private URIResolver uriResolver;
/*  14:    */   private ErrorListener errorListener;
/*  15: 44 */   private boolean indent = true;
/*  16:    */   private Properties outputProperties;
/*  17: 48 */   private boolean cacheTemplates = true;
/*  18:    */   
/*  19:    */   public XsltViewResolver()
/*  20:    */   {
/*  21: 52 */     setViewClass(XsltView.class);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setSourceKey(String sourceKey)
/*  25:    */   {
/*  26: 65 */     this.sourceKey = sourceKey;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setUriResolver(URIResolver uriResolver)
/*  30:    */   {
/*  31: 73 */     this.uriResolver = uriResolver;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setErrorListener(ErrorListener errorListener)
/*  35:    */   {
/*  36: 86 */     this.errorListener = errorListener;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void setIndent(boolean indent)
/*  40:    */   {
/*  41: 97 */     this.indent = indent;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setOutputProperties(Properties outputProperties)
/*  45:    */   {
/*  46:107 */     this.outputProperties = outputProperties;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setCacheTemplates(boolean cacheTemplates)
/*  50:    */   {
/*  51:116 */     this.cacheTemplates = cacheTemplates;
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected Class requiredViewClass()
/*  55:    */   {
/*  56:122 */     return XsltView.class;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  60:    */     throws Exception
/*  61:    */   {
/*  62:127 */     XsltView view = (XsltView)super.buildView(viewName);
/*  63:128 */     view.setSourceKey(this.sourceKey);
/*  64:129 */     if (this.uriResolver != null) {
/*  65:130 */       view.setUriResolver(this.uriResolver);
/*  66:    */     }
/*  67:132 */     if (this.errorListener != null) {
/*  68:133 */       view.setErrorListener(this.errorListener);
/*  69:    */     }
/*  70:135 */     view.setIndent(this.indent);
/*  71:136 */     view.setOutputProperties(this.outputProperties);
/*  72:137 */     view.setCacheTemplates(this.cacheTemplates);
/*  73:138 */     return view;
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.xslt.XsltViewResolver
 * JD-Core Version:    0.7.0.1
 */