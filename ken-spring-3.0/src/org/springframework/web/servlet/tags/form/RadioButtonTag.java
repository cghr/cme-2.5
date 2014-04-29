/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public class RadioButtonTag
/*  6:   */   extends AbstractSingleCheckedElementTag
/*  7:   */ {
/*  8:   */   protected void writeTagDetails(TagWriter tagWriter)
/*  9:   */     throws JspException
/* 10:   */   {
/* 11:39 */     tagWriter.writeAttribute("type", getInputType());
/* 12:40 */     Object resolvedValue = evaluate("value", getValue());
/* 13:41 */     renderFromValue(resolvedValue, tagWriter);
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected String getInputType()
/* 17:   */   {
/* 18:46 */     return "radio";
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.RadioButtonTag
 * JD-Core Version:    0.7.0.1
 */