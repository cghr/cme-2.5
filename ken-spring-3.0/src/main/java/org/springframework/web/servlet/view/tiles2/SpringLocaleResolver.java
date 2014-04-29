/*  1:   */ package org.springframework.web.servlet.view.tiles2;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import javax.servlet.http.HttpServletRequest;
/*  5:   */ import javax.servlet.jsp.PageContext;
/*  6:   */ import org.apache.tiles.context.TilesRequestContext;
/*  7:   */ import org.apache.tiles.jsp.context.JspTilesRequestContext;
/*  8:   */ import org.apache.tiles.locale.impl.DefaultLocaleResolver;
/*  9:   */ import org.apache.tiles.servlet.context.ServletTilesRequestContext;
/* 10:   */ import org.springframework.web.servlet.support.RequestContextUtils;
/* 11:   */ 
/* 12:   */ public class SpringLocaleResolver
/* 13:   */   extends DefaultLocaleResolver
/* 14:   */ {
/* 15:   */   public Locale resolveLocale(TilesRequestContext context)
/* 16:   */   {
/* 17:47 */     if ((context instanceof JspTilesRequestContext))
/* 18:   */     {
/* 19:48 */       PageContext pc = ((JspTilesRequestContext)context).getPageContext();
/* 20:49 */       return RequestContextUtils.getLocale((HttpServletRequest)pc.getRequest());
/* 21:   */     }
/* 22:51 */     if ((context instanceof ServletTilesRequestContext))
/* 23:   */     {
/* 24:52 */       HttpServletRequest request = ((ServletTilesRequestContext)context).getRequest();
/* 25:53 */       if (request != null) {
/* 26:54 */         return RequestContextUtils.getLocale(request);
/* 27:   */       }
/* 28:   */     }
/* 29:57 */     return super.resolveLocale(context);
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.tiles2.SpringLocaleResolver
 * JD-Core Version:    0.7.0.1
 */