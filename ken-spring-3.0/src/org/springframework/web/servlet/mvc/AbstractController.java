/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import javax.servlet.http.HttpServletRequest;
/*   4:    */ import javax.servlet.http.HttpServletResponse;
/*   5:    */ import javax.servlet.http.HttpSession;
/*   6:    */ import org.springframework.web.servlet.ModelAndView;
/*   7:    */ import org.springframework.web.servlet.support.WebContentGenerator;
/*   8:    */ import org.springframework.web.util.WebUtils;
/*   9:    */ 
/*  10:    */ public abstract class AbstractController
/*  11:    */   extends WebContentGenerator
/*  12:    */   implements Controller
/*  13:    */ {
/*  14:102 */   private boolean synchronizeOnSession = false;
/*  15:    */   
/*  16:    */   public final void setSynchronizeOnSession(boolean synchronizeOnSession)
/*  17:    */   {
/*  18:125 */     this.synchronizeOnSession = synchronizeOnSession;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public final boolean isSynchronizeOnSession()
/*  22:    */   {
/*  23:132 */     return this.synchronizeOnSession;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29:140 */     checkAndPrepare(request, response, this instanceof LastModified);
/*  30:143 */     if (this.synchronizeOnSession)
/*  31:    */     {
/*  32:144 */       HttpSession session = request.getSession(false);
/*  33:145 */       if (session != null)
/*  34:    */       {
/*  35:146 */         Object mutex = WebUtils.getSessionMutex(session);
/*  36:147 */         synchronized (mutex)
/*  37:    */         {
/*  38:148 */           return handleRequestInternal(request, response);
/*  39:    */         }
/*  40:    */       }
/*  41:    */     }
/*  42:153 */     return handleRequestInternal(request, response);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected abstract ModelAndView handleRequestInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
/*  46:    */     throws Exception;
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.AbstractController
 * JD-Core Version:    0.7.0.1
 */