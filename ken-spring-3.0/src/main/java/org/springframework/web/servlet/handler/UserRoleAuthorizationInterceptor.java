/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServletRequest;
/*  6:   */ import javax.servlet.http.HttpServletResponse;
/*  7:   */ 
/*  8:   */ public class UserRoleAuthorizationInterceptor
/*  9:   */   extends HandlerInterceptorAdapter
/* 10:   */ {
/* 11:   */   private String[] authorizedRoles;
/* 12:   */   
/* 13:   */   public final void setAuthorizedRoles(String[] authorizedRoles)
/* 14:   */   {
/* 15:42 */     this.authorizedRoles = authorizedRoles;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 19:   */     throws ServletException, IOException
/* 20:   */   {
/* 21:50 */     if (this.authorizedRoles != null) {
/* 22:51 */       for (String role : this.authorizedRoles) {
/* 23:52 */         if (request.isUserInRole(role)) {
/* 24:53 */           return true;
/* 25:   */         }
/* 26:   */       }
/* 27:   */     }
/* 28:57 */     handleNotAuthorized(request, response, handler);
/* 29:58 */     return false;
/* 30:   */   }
/* 31:   */   
/* 32:   */   protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler)
/* 33:   */     throws ServletException, IOException
/* 34:   */   {
/* 35:75 */     response.sendError(403);
/* 36:   */   }
/* 37:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor
 * JD-Core Version:    0.7.0.1
 */