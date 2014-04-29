/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public abstract class AbstractSingleCheckedElementTag
/*  6:   */   extends AbstractCheckedElementTag
/*  7:   */ {
/*  8:   */   private Object value;
/*  9:   */   private Object label;
/* 10:   */   
/* 11:   */   public void setValue(Object value)
/* 12:   */   {
/* 13:48 */     this.value = value;
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected Object getValue()
/* 17:   */   {
/* 18:55 */     return this.value;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void setLabel(Object label)
/* 22:   */   {
/* 23:63 */     this.label = label;
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected Object getLabel()
/* 27:   */   {
/* 28:70 */     return this.label;
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected int writeTagContent(TagWriter tagWriter)
/* 32:   */     throws JspException
/* 33:   */   {
/* 34:81 */     tagWriter.startTag("input");
/* 35:82 */     String id = resolveId();
/* 36:83 */     writeOptionalAttribute(tagWriter, "id", id);
/* 37:84 */     writeOptionalAttribute(tagWriter, "name", getName());
/* 38:85 */     writeOptionalAttributes(tagWriter);
/* 39:86 */     writeTagDetails(tagWriter);
/* 40:87 */     tagWriter.endTag();
/* 41:   */     
/* 42:89 */     Object resolvedLabel = evaluate("label", getLabel());
/* 43:90 */     if (resolvedLabel != null)
/* 44:   */     {
/* 45:91 */       tagWriter.startTag("label");
/* 46:92 */       tagWriter.writeAttribute("for", id);
/* 47:93 */       tagWriter.appendValue(convertToDisplayString(resolvedLabel));
/* 48:94 */       tagWriter.endTag();
/* 49:   */     }
/* 50:97 */     return 0;
/* 51:   */   }
/* 52:   */   
/* 53:   */   protected abstract void writeTagDetails(TagWriter paramTagWriter)
/* 54:   */     throws JspException;
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag
 * JD-Core Version:    0.7.0.1
 */