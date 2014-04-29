/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ import com.kentropy.transfer.TransferController;
/*  4:   */ import java.io.IOException;
/*  5:   */ import javax.servlet.ServletException;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ import javax.servlet.http.HttpSession;
/*  9:   */ import org.springframework.web.servlet.ModelAndView;
/* 10:   */ 
/* 11:   */ public class TransferController1
/* 12:   */   extends TransferController
/* 13:   */ {
/* 14:   */   protected void service(HttpServletRequest request, HttpServletResponse response)
/* 15:   */     throws ServletException, IOException, SecurityException
/* 16:   */   {
/* 17:38 */     new IntegrationAuth().authenticate(request);
/* 18:39 */     request.getSession().setAttribute("username", "system");
/* 19:   */     
/* 20:   */ 
/* 21:42 */     super.service(request, response);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 25:   */     throws Exception
/* 26:   */   {
/* 27:50 */     service(request, response);
/* 28:51 */     return null;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static void main(String[] args) {}
/* 32:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.TransferController1
 * JD-Core Version:    0.7.0.1
 */