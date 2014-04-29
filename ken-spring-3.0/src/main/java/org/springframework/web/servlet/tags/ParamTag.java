/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ import javax.servlet.jsp.tagext.BodyContent;
/*  5:   */ import javax.servlet.jsp.tagext.BodyTagSupport;
/*  6:   */ 
/*  7:   */ public class ParamTag
/*  8:   */   extends BodyTagSupport
/*  9:   */ {
/* 10:   */   private String name;
/* 11:   */   private String value;
/* 12:   */   private Param param;
/* 13:   */   
/* 14:   */   public int doEndTag()
/* 15:   */     throws JspException
/* 16:   */   {
/* 17:45 */     this.param = new Param();
/* 18:46 */     this.param.setName(this.name);
/* 19:47 */     if (this.value != null) {
/* 20:48 */       this.param.setValue(this.value);
/* 21:50 */     } else if (getBodyContent() != null) {
/* 22:52 */       this.param.setValue(getBodyContent().getString().trim());
/* 23:   */     }
/* 24:56 */     ParamAware paramAwareTag = (ParamAware)findAncestorWithClass(this, 
/* 25:57 */       ParamAware.class);
/* 26:58 */     if (paramAwareTag == null) {
/* 27:59 */       throw new JspException(
/* 28:60 */         "The param tag must be a descendant of a tag that supports parameters");
/* 29:   */     }
/* 30:63 */     paramAwareTag.addParam(this.param);
/* 31:   */     
/* 32:65 */     return 6;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public void setName(String name)
/* 36:   */   {
/* 37:79 */     this.name = name;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void setValue(String value)
/* 41:   */   {
/* 42:91 */     this.value = value;
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.ParamTag
 * JD-Core Version:    0.7.0.1
 */