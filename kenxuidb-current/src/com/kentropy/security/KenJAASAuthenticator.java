/*  1:   */ package com.kentropy.security;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import java.io.PrintStream;
/*  5:   */ import java.security.AccessController;
/*  6:   */ import java.util.Set;
/*  7:   */ import javax.security.auth.Subject;
/*  8:   */ import javax.security.auth.callback.CallbackHandler;
/*  9:   */ import javax.security.auth.login.LoginContext;
/* 10:   */ import javax.security.auth.login.LoginException;
/* 11:   */ 
/* 12:   */ public class KenJAASAuthenticator
/* 13:   */ {
/* 14:14 */   public static Subject subject = new Subject();
/* 15:15 */   public static KenPrincipal p = null;
/* 16:   */   
/* 17:   */   public static void main(String[] args)
/* 18:   */     throws LoginException
/* 19:   */   {
/* 20:18 */     run("TestLogin", 3);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public static void run(String conf, int tries)
/* 24:   */     throws LoginException
/* 25:   */   {
/* 26:24 */     subject = (Subject)TestXUIDB.getInstance().getBean("ken-subject");
/* 27:25 */     p = (KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal");
/* 28:26 */     for (int i = 0; i < tries; i++) {
/* 29:   */       try
/* 30:   */       {
/* 31:29 */         if (!subject.getPrincipals().contains(p)) {
/* 32:31 */           subject.getPrincipals().add(p);
/* 33:   */         }
/* 34:34 */         System.out.println("uSER " + ((KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal")).getUser() + ">>>");
/* 35:   */         
/* 36:36 */         LoginContext lc = new LoginContext(conf, subject, (CallbackHandler)TestXUIDB.getInstance().getBean("callback"));
/* 37:37 */         lc.login();
/* 38:38 */         System.out.println(Subject.getSubject(AccessController.getContext()));
/* 39:   */         
/* 40:40 */         Subject tt1 = (Subject)TestXUIDB.getInstance().getBean("ken-subject");
/* 41:41 */         System.out.println(((KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal")).getSessionID() + ">>>");
/* 42:   */         
/* 43:43 */         System.out.println(" success" + p.getSessionID() + " " + p.getName() + " " + p.getTeamId());
/* 44:44 */         Utils.savePrincipal(p);
/* 45:   */       }
/* 46:   */       catch (LoginException e)
/* 47:   */       {
/* 48:48 */         e.printStackTrace();
/* 49:49 */         p.setUser(null);
/* 50:50 */         p.setPassword(null);
/* 51:51 */         p.setSessionID(null);
/* 52:52 */         throw e;
/* 53:   */       }
/* 54:   */       catch (Exception e)
/* 55:   */       {
/* 56:55 */         e.printStackTrace();
/* 57:   */       }
/* 58:   */     }
/* 59:   */   }
/* 60:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.KenJAASAuthenticator
 * JD-Core Version:    0.7.0.1
 */