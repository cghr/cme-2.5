/*   1:    */ package org.springframework.web.servlet.tags.form;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import javax.servlet.jsp.JspException;
/*   5:    */ import org.springframework.web.servlet.support.RequestContext;
/*   6:    */ import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
/*   7:    */ import org.springframework.web.util.ExpressionEvaluationUtils;
/*   8:    */ 
/*   9:    */ public abstract class AbstractFormTag
/*  10:    */   extends HtmlEscapingAwareTag
/*  11:    */ {
/*  12:    */   protected Object evaluate(String attributeName, Object value)
/*  13:    */     throws JspException
/*  14:    */   {
/*  15: 49 */     if ((value instanceof String)) {
/*  16: 50 */       return ExpressionEvaluationUtils.evaluate(attributeName, (String)value, this.pageContext);
/*  17:    */     }
/*  18: 53 */     return value;
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected boolean evaluateBoolean(String attributeName, String value)
/*  22:    */     throws JspException
/*  23:    */   {
/*  24: 64 */     Object evaluated = ExpressionEvaluationUtils.evaluate(attributeName, value, this.pageContext);
/*  25:    */     
/*  26: 66 */     return (Boolean.TRUE.equals(evaluated)) || (((evaluated instanceof String)) && (Boolean.valueOf((String)evaluated).booleanValue()));
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected final void writeOptionalAttribute(TagWriter tagWriter, String attributeName, String value)
/*  30:    */     throws JspException
/*  31:    */   {
/*  32: 80 */     if (value != null) {
/*  33: 81 */       tagWriter.writeOptionalAttributeValue(attributeName, getDisplayString(evaluate(attributeName, value)));
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected TagWriter createTagWriter()
/*  38:    */   {
/*  39: 92 */     return new TagWriter(this.pageContext);
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected final int doStartTagInternal()
/*  43:    */     throws Exception
/*  44:    */   {
/*  45:102 */     return writeTagContent(createTagWriter());
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected String getDisplayString(Object value)
/*  49:    */   {
/*  50:110 */     return ValueFormatter.getDisplayString(value, isHtmlEscape());
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected String getDisplayString(Object value, PropertyEditor propertyEditor)
/*  54:    */   {
/*  55:120 */     return ValueFormatter.getDisplayString(value, propertyEditor, isHtmlEscape());
/*  56:    */   }
/*  57:    */   
/*  58:    */   protected boolean isDefaultHtmlEscape()
/*  59:    */   {
/*  60:128 */     Boolean defaultHtmlEscape = getRequestContext().getDefaultHtmlEscape();
/*  61:129 */     return (defaultHtmlEscape == null) || (defaultHtmlEscape.booleanValue());
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected abstract int writeTagContent(TagWriter paramTagWriter)
/*  65:    */     throws JspException;
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractFormTag
 * JD-Core Version:    0.7.0.1
 */