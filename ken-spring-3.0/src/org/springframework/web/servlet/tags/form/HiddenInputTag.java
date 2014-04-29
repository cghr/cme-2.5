/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public class HiddenInputTag
/*  6:   */   extends AbstractHtmlElementTag
/*  7:   */ {
/*  8:   */   protected int writeTagContent(TagWriter tagWriter)
/*  9:   */     throws JspException
/* 10:   */   {
/* 11:44 */     tagWriter.startTag("input");
/* 12:45 */     writeDefaultAttributes(tagWriter);
/* 13:46 */     tagWriter.writeAttribute("type", "hidden");
/* 14:47 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 15:48 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, "hidden"));
/* 16:49 */     tagWriter.endTag();
/* 17:50 */     return 0;
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.HiddenInputTag
 * JD-Core Version:    0.7.0.1
 */