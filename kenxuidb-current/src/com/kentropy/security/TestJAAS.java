/*  1:   */ package com.kentropy.security;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.security.Principal;
/*  5:   */ import java.util.Iterator;
/*  6:   */ import java.util.Set;
/*  7:   */ import javax.security.auth.Subject;
/*  8:   */ import javax.security.auth.callback.CallbackHandler;
/*  9:   */ import javax.security.auth.login.LoginContext;
/* 10:   */ import javax.security.auth.login.LoginException;
/* 11:   */ import org.apache.log4j.Logger;
/* 12:   */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/* 13:   */ 
/* 14:   */ public class TestJAAS
/* 15:   */ {
/* 16:16 */   Logger logger = Logger.getLogger(getClass().getName());
/* 17:   */   Principal p;
/* 18:   */   LoginContext lc;
/* 19:   */   
/* 20:   */   public String getSession()
/* 21:   */   {
/* 22:22 */     if (this.p != null) {
/* 23:23 */       return this.p.getName();
/* 24:   */     }
/* 25:25 */     return null;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void init()
/* 29:   */     throws LoginException
/* 30:   */   {
/* 31:29 */     CallbackHandler handler = (CallbackHandler)new ClassPathXmlApplicationContext("appContext.xml").getBean("callback");
/* 32:30 */     System.out.println(" After Callback bean");
/* 33:31 */     System.out.println(" Trying to login");
/* 34:32 */     this.lc = new LoginContext("CMELogin", handler);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void run()
/* 38:   */     throws Exception
/* 39:   */   {
/* 40:36 */     boolean loginStatus = true;
/* 41:38 */     for (int i = 0; i < 3; i++) {
/* 42:   */       try
/* 43:   */       {
/* 44:41 */         this.logger.info(" Before login");
/* 45:42 */         this.lc.login();
/* 46:43 */         this.logger.info(" After login");
/* 47:44 */         Set p1 = this.lc.getSubject().getPrincipals();
/* 48:45 */         Iterator pItr = p1.iterator();
/* 49:46 */         this.p = ((Principal)pItr.next());
/* 50:47 */         this.logger.info("Principal " + this.p.getName());
/* 51:   */       }
/* 52:   */       catch (LoginException e)
/* 53:   */       {
/* 54:51 */         e.printStackTrace();
/* 55:52 */         this.logger.error(e);
/* 56:53 */         loginStatus = false;
/* 57:   */       }
/* 58:   */     }
/* 59:57 */     if (loginStatus)
/* 60:   */     {
/* 61:58 */       System.out.println("Login Successful.");
/* 62:   */     }
/* 63:   */     else
/* 64:   */     {
/* 65:61 */       System.out.println("Login Failed.");
/* 66:62 */       this.logger.error("Login Failed.");
/* 67:63 */       throw new Exception("Login falied");
/* 68:   */     }
/* 69:   */   }
/* 70:   */   
/* 71:   */   public static void main(String[] args)
/* 72:   */     throws Exception
/* 73:   */   {
/* 74:69 */     String userName = "test";
/* 75:70 */     String password = "test";
/* 76:   */     
/* 77:72 */     TestJAAS tt = new TestJAAS();
/* 78:73 */     tt.init();
/* 79:74 */     tt.run();
/* 80:   */   }
/* 81:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.TestJAAS
 * JD-Core Version:    0.7.0.1
 */