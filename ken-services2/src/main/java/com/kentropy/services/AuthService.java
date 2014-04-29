/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.SpringApplicationContext;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.io.PrintWriter;
/*   7:    */ import java.util.Properties;
/*   8:    */ import javax.servlet.ServletException;
/*   9:    */ import javax.servlet.http.HttpServlet;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import javax.servlet.http.HttpSession;
/*  13:    */ import org.springframework.context.ApplicationContext;
/*  14:    */ 
/*  15:    */ public class AuthService
/*  16:    */   extends HttpServlet
/*  17:    */ {
/*  18:    */   private String md5;
/*  19:    */   
/*  20:    */   public static void main(String[] args) {}
/*  21:    */   
/*  22:    */   public void init()
/*  23:    */     throws ServletException
/*  24:    */   {
/*  25: 54 */     ApplicationContext context = SpringApplicationContext.getApplicationContext();
/*  26: 55 */     Properties props = (Properties)context.getBean("authentication");
/*  27:    */     
/*  28: 57 */     this.md5 = props.getProperty("md5");
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  32:    */     throws ServletException, IOException
/*  33:    */   {
/*  34: 66 */     String action = request.getParameter("action");
/*  35: 67 */     if (action != null) {
/*  36: 69 */       if (action.equals("getSession"))
/*  37:    */       {
/*  38: 72 */         System.out.println("SessionId=" + request.getSession().getId());
/*  39: 73 */         response.getWriter().print("SessionId=" + request.getSession().getId());
/*  40: 74 */         return;
/*  41:    */       }
/*  42:    */     }
/*  43: 77 */     boolean md5_flag = false;
/*  44: 78 */     String sessionId = request.getSession().getId();
/*  45: 79 */     System.out.println("SessionId again =" + request.getSession().getId() + "---");
/*  46: 80 */     if (this.md5.equals("enable")) {
/*  47: 81 */       md5_flag = true;
/*  48:    */     }
/*  49: 82 */     String[] creds = new String[4];
/*  50: 83 */     boolean auth_flag = new TransactionClient().authenticate(request.getParameter("username"), request.getParameter("password"), sessionId.trim(), md5_flag, creds);
/*  51: 85 */     if (auth_flag)
/*  52:    */     {
/*  53: 88 */       HttpSession session = request.getSession(true);
/*  54: 89 */       session.setAttribute("username", creds[0]);
/*  55: 90 */       session.setAttribute("loginStatus", "true");
/*  56: 91 */       session.setAttribute("roles", creds[1]);
/*  57: 92 */       session.setAttribute("browsedPages", "/login");
/*  58:    */       
/*  59:    */ 
/*  60:    */ 
/*  61: 96 */       session.setAttribute("oneTimeLogin", Boolean.valueOf(false));
/*  62:    */       
/*  63: 98 */       String teamId = (String)session.getAttribute("teamId");
/*  64:    */       
/*  65:100 */       response.getWriter().println("username=" + creds[0] + "&fullname=" + creds[3] + "&teamId=" + teamId + "&id=" + creds[2]);
/*  66:101 */       response.setStatus(200);
/*  67:    */     }
/*  68:    */     else
/*  69:    */     {
/*  70:105 */       response.setStatus(403);
/*  71:    */     }
/*  72:    */   }
/*  73:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.AuthService
 * JD-Core Version:    0.7.0.1
 */