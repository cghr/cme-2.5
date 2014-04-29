/*  1:   */ package org.springframework.web.bind;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletRequest;
/*  4:   */ import org.springframework.beans.MutablePropertyValues;
/*  5:   */ import org.springframework.web.util.WebUtils;
/*  6:   */ 
/*  7:   */ public class ServletRequestParameterPropertyValues
/*  8:   */   extends MutablePropertyValues
/*  9:   */ {
/* 10:   */   public static final String DEFAULT_PREFIX_SEPARATOR = "_";
/* 11:   */   
/* 12:   */   public ServletRequestParameterPropertyValues(ServletRequest request)
/* 13:   */   {
/* 14:51 */     this(request, null, null);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public ServletRequestParameterPropertyValues(ServletRequest request, String prefix)
/* 18:   */   {
/* 19:63 */     this(request, prefix, "_");
/* 20:   */   }
/* 21:   */   
/* 22:   */   public ServletRequestParameterPropertyValues(ServletRequest request, String prefix, String prefixSeparator)
/* 23:   */   {
/* 24:77 */     super(
/* 25:78 */       WebUtils.getParametersStartingWith(request, prefix != null ? prefix + prefixSeparator : null));
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.ServletRequestParameterPropertyValues
 * JD-Core Version:    0.7.0.1
 */