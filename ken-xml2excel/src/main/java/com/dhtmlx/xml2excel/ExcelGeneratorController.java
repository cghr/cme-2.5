/*  1:   */ package com.dhtmlx.xml2excel;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URLDecoder;
/*  5:   */ import javax.servlet.http.HttpServlet;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ import org.springframework.web.servlet.ModelAndView;
/*  9:   */ import org.springframework.web.servlet.mvc.Controller;
/* 10:   */ 
/* 11:   */ public class ExcelGeneratorController
/* 12:   */   extends HttpServlet
/* 13:   */   implements Controller
/* 14:   */ {
/* 15:   */   public void doPost(HttpServletRequest req, HttpServletResponse resp)
/* 16:   */     throws IOException
/* 17:   */   {
/* 18:15 */     String xml = req.getParameter("grid_xml");
/* 19:16 */     xml = URLDecoder.decode(xml, "UTF-8");
/* 20:17 */     new ExcelWriter().generate(xml, resp);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp)
/* 24:   */     throws Exception
/* 25:   */   {
/* 26:24 */     doPost(req, resp);
/* 27:   */     
/* 28:26 */     return null;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-xml2excel\ken-xml2excel.jar
 * Qualified Name:     com.dhtmlx.xml2excel.ExcelGeneratorController
 * JD-Core Version:    0.7.0.1
 */