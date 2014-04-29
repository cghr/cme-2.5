/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServlet;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ 
/*  9:   */ public abstract class ThreadSafeConnectorServlet
/* 10:   */   extends HttpServlet
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = 8532251727722723922L;
/* 13:   */   
/* 14:   */   public void doGet(HttpServletRequest req, HttpServletResponse res)
/* 15:   */     throws ServletException, IOException
/* 16:   */   {
/* 17:18 */     configure(req, res);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void doPost(HttpServletRequest req, HttpServletResponse res)
/* 21:   */     throws ServletException, IOException
/* 22:   */   {
/* 23:25 */     doGet(req, res);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected abstract void configure(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ThreadSafeConnectorServlet
 * JD-Core Version:    0.7.0.1
 */