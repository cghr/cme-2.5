/*  1:   */ package com.kentropy;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import com.kentropy.security.client.UserAuthentication;
/*  5:   */ import de.schlund.pfixxml.util.MD5Utils;
/*  6:   */ import java.io.PrintStream;
/*  7:   */ import java.util.Properties;
/*  8:   */ 
/*  9:   */ public class test
/* 10:   */   implements Runnable
/* 11:   */ {
/* 12:12 */   public int opt = 0;
/* 13:13 */   static String[] username = { "amoliprashant@yahoo.com", "nimitt.gupta@kentropy.com", "sagar.patke@kentropy.com" };
/* 14:14 */   public static Runnable[] test = new Thread[3];
/* 15:16 */   int id = 0;
/* 16:   */   
/* 17:   */   public static void main(String[] args)
/* 18:   */   {
/* 19:20 */     for (int i = 0; i < test.length; i++)
/* 20:   */     {
/* 21:22 */       test tt = new test();
/* 22:23 */       tt.opt = (i % 3);
/* 23:24 */       tt.id = i;
/* 24:25 */       Thread t = new Thread(tt);
/* 25:26 */       test[i] = t;
/* 26:   */       
/* 27:28 */       t.start();
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void run()
/* 32:   */   {
/* 33:34 */     String username1 = username[this.opt];
/* 34:35 */     String password = MD5Utils.hex_md5("census");
/* 35:36 */     UserAuthentication ua = new UserAuthentication();
/* 36:37 */     Properties p = new Properties();
/* 37:   */     
/* 38:39 */     System.out.println("Started " + this.id);
/* 39:40 */     for (int i = 0; i < 10; i++) {
/* 40:   */       try
/* 41:   */       {
/* 42:   */         try
/* 43:   */         {
/* 44:46 */           ua.authenticate1(username1, password);
/* 45:   */         }
/* 46:   */         catch (Exception localException1) {}catch (NoSuchMethodError localNoSuchMethodError) {}
/* 47:55 */         String retU = TestXUIDB.getInstance().getProperty("username");
/* 48:56 */         String retP = TestXUIDB.getInstance().getProperty("password");
/* 49:   */         
/* 50:58 */         System.out.println("Thread id " + this.id + " Original " + username1 + " " + password + " Returned " + retU + " " + retP);
/* 51:59 */         Thread.currentThread();Thread.sleep(100L);
/* 52:   */       }
/* 53:   */       catch (Exception e)
/* 54:   */       {
/* 55:62 */         e.printStackTrace();
/* 56:   */       }
/* 57:   */     }
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.test
 * JD-Core Version:    0.7.0.1
 */