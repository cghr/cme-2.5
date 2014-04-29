/*  1:   */ package com.dhtmlx.connector;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.servlet.ServletException;
/*  5:   */ import javax.servlet.http.HttpServlet;
/*  6:   */ import javax.servlet.http.HttpServletRequest;
/*  7:   */ import javax.servlet.http.HttpServletResponse;
/*  8:   */ 
/*  9:   */ public abstract class ConnectorServlet
/* 10:   */   extends HttpServlet
/* 11:   */ {
/* 12:   */   private static final long serialVersionUID = -6509163446916007821L;
/* 13:   */   
/* 14:   */   public void doGet(HttpServletRequest req, HttpServletResponse res)
/* 15:   */     throws ServletException, IOException
/* 16:   */   {
/* 17:26 */     BaseConnector.global_http_request = req;
/* 18:27 */     BaseConnector.global_http_response = res;
/* 19:28 */     configure();
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void doPost(HttpServletRequest req, HttpServletResponse res)
/* 23:   */     throws ServletException, IOException
/* 24:   */   {
/* 25:35 */     doGet(req, res);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected abstract void configure();
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.ConnectorServlet
 * JD-Core Version:    0.7.0.1
 */