/*  1:   */ package org.springframework.web.filter;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.FilterChain;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ 
/*  9:   */ public class CharacterEncodingFilter
/* 10:   */   extends OncePerRequestFilter
/* 11:   */ {
/* 12:   */   private String encoding;
/* 13:47 */   private boolean forceEncoding = false;
/* 14:   */   
/* 15:   */   public void setEncoding(String encoding)
/* 16:   */   {
/* 17:58 */     this.encoding = encoding;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setForceEncoding(boolean forceEncoding)
/* 21:   */   {
/* 22:73 */     this.forceEncoding = forceEncoding;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/* 26:   */     throws ServletException, IOException
/* 27:   */   {
/* 28:82 */     if ((this.encoding != null) && ((this.forceEncoding) || (request.getCharacterEncoding() == null)))
/* 29:   */     {
/* 30:83 */       request.setCharacterEncoding(this.encoding);
/* 31:84 */       if (this.forceEncoding) {
/* 32:85 */         response.setCharacterEncoding(this.encoding);
/* 33:   */       }
/* 34:   */     }
/* 35:88 */     filterChain.doFilter(request, response);
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.filter.CharacterEncodingFilter
 * JD-Core Version:    0.7.0.1
 */