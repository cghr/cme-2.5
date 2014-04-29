/*  1:   */ package com.kentropy.cme.decrypt;
/*  2:   */ 
/*  3:   */ import com.kentropy.crypt.DesEncryptor;
/*  4:   */ import java.io.File;
/*  5:   */ import java.io.FileInputStream;
/*  6:   */ import javax.servlet.ServletContext;
/*  7:   */ import javax.servlet.http.HttpServletRequest;
/*  8:   */ import javax.servlet.http.HttpServletResponse;
/*  9:   */ import javax.servlet.http.HttpSession;
/* 10:   */ import org.springframework.web.servlet.ModelAndView;
/* 11:   */ import org.springframework.web.servlet.mvc.Controller;
/* 12:   */ 
/* 13:   */ public class DecryptImage
/* 14:   */   implements Controller
/* 15:   */ {
/* 16:   */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 17:   */     throws Exception
/* 18:   */   {
/* 19:21 */     String image = request.getParameter("image");
/* 20:22 */     DesEncryptor crypt = new DesEncryptor();
/* 21:23 */     String path = request.getSession().getServletContext().getRealPath("/") + "data/split";
/* 22:24 */     FileInputStream fis = new FileInputStream(new File(path + "/" + image));
/* 23:25 */     response.setContentType("image/png");
/* 24:26 */     crypt.decrypt(fis, response.getOutputStream());
/* 25:27 */     fis.close();
/* 26:   */     
/* 27:   */ 
/* 28:   */ 
/* 29:31 */     return null;
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-decrypt\ken-cme-decrypt.jar
 * Qualified Name:     com.kentropy.cme.decrypt.DecryptImage
 * JD-Core Version:    0.7.0.1
 */