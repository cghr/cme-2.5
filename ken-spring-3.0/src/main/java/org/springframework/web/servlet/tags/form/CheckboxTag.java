/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ import org.springframework.web.servlet.support.BindStatus;
/*  5:   */ 
/*  6:   */ public class CheckboxTag
/*  7:   */   extends AbstractSingleCheckedElementTag
/*  8:   */ {
/*  9:   */   protected int writeTagContent(TagWriter tagWriter)
/* 10:   */     throws JspException
/* 11:   */   {
/* 12:52 */     super.writeTagContent(tagWriter);
/* 13:54 */     if (!isDisabled())
/* 14:   */     {
/* 15:56 */       tagWriter.startTag("input");
/* 16:57 */       tagWriter.writeAttribute("type", "hidden");
/* 17:58 */       String name = "_" + getName();
/* 18:59 */       tagWriter.writeAttribute("name", name);
/* 19:60 */       tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
/* 20:61 */       tagWriter.endTag();
/* 21:   */     }
/* 22:64 */     return 0;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void writeTagDetails(TagWriter tagWriter)
/* 26:   */     throws JspException
/* 27:   */   {
/* 28:69 */     tagWriter.writeAttribute("type", getInputType());
/* 29:   */     
/* 30:71 */     Object boundValue = getBoundValue();
/* 31:72 */     Class valueType = getBindStatus().getValueType();
/* 32:74 */     if ((Boolean.class.equals(valueType)) || (Boolean.TYPE.equals(valueType)))
/* 33:   */     {
/* 34:76 */       if ((boundValue instanceof String)) {
/* 35:77 */         boundValue = Boolean.valueOf((String)boundValue);
/* 36:   */       }
/* 37:79 */       Boolean booleanValue = boundValue != null ? (Boolean)boundValue : Boolean.FALSE;
/* 38:80 */       renderFromBoolean(booleanValue, tagWriter);
/* 39:   */     }
/* 40:   */     else
/* 41:   */     {
/* 42:84 */       Object value = getValue();
/* 43:85 */       if (value == null) {
/* 44:86 */         throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
/* 45:   */       }
/* 46:88 */       Object resolvedValue = (value instanceof String) ? evaluate("value", (String)value) : value;
/* 47:89 */       renderFromValue(resolvedValue, tagWriter);
/* 48:   */     }
/* 49:   */   }
/* 50:   */   
/* 51:   */   protected String getInputType()
/* 52:   */   {
/* 53:95 */     return "checkbox";
/* 54:   */   }
/* 55:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.CheckboxTag
 * JD-Core Version:    0.7.0.1
 */