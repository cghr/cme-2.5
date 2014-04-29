/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public class CheckboxesTag
/*  6:   */   extends AbstractMultiCheckedElementTag
/*  7:   */ {
/*  8:   */   protected int writeTagContent(TagWriter tagWriter)
/*  9:   */     throws JspException
/* 10:   */   {
/* 11:38 */     super.writeTagContent(tagWriter);
/* 12:40 */     if (!isDisabled())
/* 13:   */     {
/* 14:42 */       tagWriter.startTag("input");
/* 15:43 */       tagWriter.writeAttribute("type", "hidden");
/* 16:44 */       String name = "_" + getName();
/* 17:45 */       tagWriter.writeAttribute("name", name);
/* 18:46 */       tagWriter.writeAttribute("value", processFieldValue(name, "on", getInputType()));
/* 19:47 */       tagWriter.endTag();
/* 20:   */     }
/* 21:50 */     return 0;
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected String getInputType()
/* 25:   */   {
/* 26:55 */     return "checkbox";
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.CheckboxesTag
 * JD-Core Version:    0.7.0.1
 */