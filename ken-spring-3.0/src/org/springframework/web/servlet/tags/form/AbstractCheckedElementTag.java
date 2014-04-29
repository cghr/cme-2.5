/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public abstract class AbstractCheckedElementTag
/*  6:   */   extends AbstractHtmlInputElementTag
/*  7:   */ {
/*  8:   */   protected void renderFromValue(Object value, TagWriter tagWriter)
/*  9:   */     throws JspException
/* 10:   */   {
/* 11:38 */     renderFromValue(value, value, tagWriter);
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected void renderFromValue(Object item, Object value, TagWriter tagWriter)
/* 15:   */     throws JspException
/* 16:   */   {
/* 17:47 */     String displayValue = convertToDisplayString(value);
/* 18:48 */     tagWriter.writeAttribute("value", processFieldValue(getName(), displayValue, getInputType()));
/* 19:49 */     if ((isOptionSelected(value)) || ((value != item) && (isOptionSelected(item)))) {
/* 20:50 */       tagWriter.writeAttribute("checked", "checked");
/* 21:   */     }
/* 22:   */   }
/* 23:   */   
/* 24:   */   private boolean isOptionSelected(Object value)
/* 25:   */     throws JspException
/* 26:   */   {
/* 27:59 */     return SelectedValueComparator.isSelected(getBindStatus(), value);
/* 28:   */   }
/* 29:   */   
/* 30:   */   protected void renderFromBoolean(Boolean boundValue, TagWriter tagWriter)
/* 31:   */     throws JspException
/* 32:   */   {
/* 33:68 */     tagWriter.writeAttribute("value", processFieldValue(getName(), "true", getInputType()));
/* 34:69 */     if (boundValue.booleanValue()) {
/* 35:70 */       tagWriter.writeAttribute("checked", "checked");
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   protected String autogenerateId()
/* 40:   */     throws JspException
/* 41:   */   {
/* 42:79 */     return TagIdGenerator.nextId(super.autogenerateId(), this.pageContext);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected abstract int writeTagContent(TagWriter paramTagWriter)
/* 46:   */     throws JspException;
/* 47:   */   
/* 48:   */   protected abstract String getInputType();
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractCheckedElementTag
 * JD-Core Version:    0.7.0.1
 */