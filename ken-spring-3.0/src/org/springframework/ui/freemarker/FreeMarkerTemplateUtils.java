/*  1:   */ package org.springframework.ui.freemarker;
/*  2:   */ 
/*  3:   */ import freemarker.template.Template;
/*  4:   */ import freemarker.template.TemplateException;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.io.StringWriter;
/*  7:   */ 
/*  8:   */ public abstract class FreeMarkerTemplateUtils
/*  9:   */ {
/* 10:   */   public static String processTemplateIntoString(Template template, Object model)
/* 11:   */     throws IOException, TemplateException
/* 12:   */   {
/* 13:48 */     StringWriter result = new StringWriter();
/* 14:49 */     template.process(model, result);
/* 15:50 */     return result.toString();
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.freemarker.FreeMarkerTemplateUtils
 * JD-Core Version:    0.7.0.1
 */