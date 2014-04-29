/*  1:   */ package org.springframework.web.servlet.mvc.method.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import javax.servlet.ServletRequest;
/*  5:   */ import org.springframework.beans.MutablePropertyValues;
/*  6:   */ import org.springframework.web.bind.ServletRequestDataBinder;
/*  7:   */ import org.springframework.web.servlet.HandlerMapping;
/*  8:   */ 
/*  9:   */ public class ExtendedServletRequestDataBinder
/* 10:   */   extends ServletRequestDataBinder
/* 11:   */ {
/* 12:   */   public ExtendedServletRequestDataBinder(Object target)
/* 13:   */   {
/* 14:43 */     super(target);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ExtendedServletRequestDataBinder(Object target, String objectName)
/* 18:   */   {
/* 19:54 */     super(target, objectName);
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request)
/* 23:   */   {
/* 24:63 */     String attr = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
/* 25:64 */     mpvs.addPropertyValues((Map)request.getAttribute(attr));
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder
 * JD-Core Version:    0.7.0.1
 */