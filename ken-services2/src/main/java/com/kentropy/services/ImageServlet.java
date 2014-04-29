/*  1:   */ package com.kentropy.services;
/*  2:   */ 
/*  3:   */ import com.kentropy.db.TestXUIDB;
/*  4:   */ import java.io.File;
/*  5:   */ import java.io.FileInputStream;
/*  6:   */ import java.io.IOException;
/*  7:   */ import java.io.OutputStream;
/*  8:   */ import java.io.PrintStream;
/*  9:   */ import javax.servlet.ServletContext;
/* 10:   */ import javax.servlet.ServletException;
/* 11:   */ import javax.servlet.http.HttpServlet;
/* 12:   */ import javax.servlet.http.HttpServletRequest;
/* 13:   */ import javax.servlet.http.HttpServletResponse;
/* 14:   */ 
/* 15:   */ public class ImageServlet
/* 16:   */   extends HttpServlet
/* 17:   */ {
/* 18:   */   protected void service(HttpServletRequest request, HttpServletResponse response)
/* 19:   */     throws ServletException, IOException
/* 20:   */   {
/* 21:24 */     String user = TestXUIDB.getInstance().getProperty("image_user");
/* 22:25 */     String password = TestXUIDB.getInstance().getProperty("image_password");
/* 23:26 */     String[] tt = request.getRequestURI().split("/");
/* 24:27 */     String file = tt[(tt.length - 1)];
/* 25:28 */     String path = getServletContext().getRealPath("/") + "data/split/" + file;
/* 26:29 */     File f = new File(path);
/* 27:30 */     boolean downloaded = false;
/* 28:31 */     if (!f.exists())
/* 29:   */     {
/* 30:34 */       String cmeURL = TestXUIDB.getInstance().getProperty("CME_URL");
/* 31:35 */       String[] tt1 = cmeURL.split(",");
/* 32:36 */       for (int i = 0; i < tt1.length; i++) {
/* 33:   */         try
/* 34:   */         {
/* 35:39 */           new IntegrationService().downloadFile(tt1[i] + "Decrypt.do?image=" + file, user, password, tt1[i] + "Authenticate.do", path);
/* 36:40 */           downloaded = true;
/* 37:   */         }
/* 38:   */         catch (Exception e)
/* 39:   */         {
/* 40:45 */           e.printStackTrace();
/* 41:46 */           System.out.println("Problem getting  " + file + " from " + tt1[i]);
/* 42:   */         }
/* 43:   */       }
/* 44:50 */       if (!downloaded)
/* 45:   */       {
/* 46:52 */         System.out.println("Could not download the file from any of the sources");
/* 47:53 */         return;
/* 48:   */       }
/* 49:   */     }
/* 50:59 */     FileInputStream in = new FileInputStream(path);
/* 51:60 */     byte[] b = new byte[2048];
/* 52:61 */     response.setContentLength((int)new File(path).length());
/* 53:62 */     OutputStream fout = response.getOutputStream();
/* 54:63 */     int count = in.read(b);
/* 55:64 */     while (count > 0)
/* 56:   */     {
/* 57:66 */       fout.write(b, 0, count);
/* 58:67 */       count = in.read(b);
/* 59:68 */       System.out.println(count);
/* 60:   */     }
/* 61:71 */     fout.close();
/* 62:72 */     in.close();
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.ImageServlet
 * JD-Core Version:    0.7.0.1
 */