/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import java.util.Vector;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import javax.servlet.ServletException;
/*  11:    */ import javax.servlet.http.HttpServlet;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import javax.servlet.http.HttpServletResponse;
/*  14:    */ import net.xoetrope.xui.data.XBaseModel;
/*  15:    */ import net.xoetrope.xui.data.XModel;
/*  16:    */ 
/*  17:    */ public class TransferServlet
/*  18:    */   extends HttpServlet
/*  19:    */ {
/*  20:    */   public static void main(String[] args)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 22 */     File f = new File("d:\\tmp\\test.zip");
/*  24: 23 */     f.createNewFile();
/*  25:    */   }
/*  26:    */   
/*  27:    */   protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
/*  28:    */     throws ServletException, IOException
/*  29:    */   {
/*  30: 29 */     super.doGet(arg0, arg1);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
/*  34:    */     throws ServletException, IOException
/*  35:    */   {
/*  36: 35 */     super.doPost(arg0, arg1);
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected void doPut(HttpServletRequest arg0, HttpServletResponse arg1)
/*  40:    */     throws ServletException, IOException
/*  41:    */   {
/*  42: 41 */     super.doPut(arg0, arg1);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  46:    */     throws ServletException, IOException
/*  47:    */   {
/*  48:    */     try
/*  49:    */     {
/*  50: 49 */       System.out.println("in" + request.getContentType() + " " + request.getMethod() + " " + request.getContentLength());
/*  51: 50 */       String path = getServletContext().getRealPath("/mbox");
/*  52: 51 */       if (request.getParameter("action") != null)
/*  53:    */       {
/*  54: 53 */         if (request.getParameter("action").equals("ping"))
/*  55:    */         {
/*  56: 55 */           response.getWriter().println("Transfer");
/*  57: 56 */           return;
/*  58:    */         }
/*  59: 58 */         if (request.getParameter("action").equals("upload"))
/*  60:    */         {
/*  61: 60 */           Server2 server = new Server2(request.getInputStream(), response.getOutputStream());
/*  62: 61 */           server.path = path;
/*  63: 62 */           server.run();
/*  64:    */         }
/*  65: 64 */         if (request.getParameter("action").equals("download"))
/*  66:    */         {
/*  67: 66 */           Server server = new Server(request.getInputStream(), response.getOutputStream());
/*  68: 67 */           server.path = path;
/*  69: 68 */           server.run();
/*  70:    */         }
/*  71: 70 */         if (request.getParameter("action").equals("syncrepo"))
/*  72:    */         {
/*  73: 72 */           RepServer server = new RepServer(request.getInputStream(), response.getOutputStream());
/*  74: 73 */           server.path = getServletContext().getRealPath("/");
/*  75: 74 */           server.run();
/*  76:    */         }
/*  77: 76 */         if (request.getParameter("action").equals("authenticate"))
/*  78:    */         {
/*  79: 78 */           String username = request.getParameter("username");
/*  80: 79 */           String password = request.getParameter("password");
/*  81: 80 */           if (TestXUIDB.getInstance().isPhysician(username, password))
/*  82:    */           {
/*  83: 81 */             XModel xm = new XBaseModel();
/*  84: 82 */             TestXUIDB.getInstance().getPhysicianDetails("username='" + username + "'", xm);
/*  85:    */             
/*  86: 84 */             response.setContentType("text/xml");
/*  87: 85 */             response.getWriter().println("<?xml version=\"1.0\" ?>\n<Datasets><data id=\"teamdata\"><data id=\"users\"><data id=\"" + 
/*  88: 86 */               username + "\" code=\"" + ((XModel)xm.get(0).get("id")).get() + "\" fullname=\"" + ((XModel)xm.get(0).get("name")).get() + "\" password=\"" + password + "\" role=\"physician\" gender=\"1\">" + 
/*  89: 87 */               "</data> " + 
/*  90: 88 */               "</data>" + 
/*  91: 89 */               "<data id=\"teams\">" + 
/*  92: 90 */               "<data id=\"" + ((XModel)xm.get(0).get("id")).get() + "\" name=\"team5\">" + 
/*  93: 91 */               "<data value=\"" + username + "\" id=\"" + ((XModel)xm.get(0).get("id")).get() + "\">" + 
/*  94: 92 */               "</data>" + 
/*  95: 93 */               "</data>" + 
/*  96: 94 */               "</data>" + 
/*  97: 95 */               "</data>" + 
/*  98: 96 */               "</Datasets>");
/*  99:    */           }
/* 100:    */           else
/* 101:    */           {
/* 102:100 */             response.sendError(403, "Not Authenticated");
/* 103:    */           }
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:    */     catch (Exception e)
/* 108:    */     {
/* 109:110 */       e.printStackTrace();
/* 110:    */     }
/* 111:    */   }
/* 112:    */   
/* 113:    */   public synchronized void createPhyLogs(String user, String id)
/* 114:    */   {
/* 115:116 */     Vector v = new Vector();
/* 116:117 */     v.add("username");
/* 117:    */     try
/* 118:    */     {
/* 119:119 */       String bookmark = TestXUIDB.getInstance().getLastChangeLog();
/* 120:120 */       TestXUIDB.getInstance().createChangeLog("physician", "where username='Prabhat Jha'", v);
/* 121:121 */       TestXUIDB.getInstance().sendServerLogs("admin", 
/* 122:122 */         id, 
/* 123:123 */         bookmark, "999999");
/* 124:    */     }
/* 125:    */     catch (Exception e)
/* 126:    */     {
/* 127:126 */       e.printStackTrace();
/* 128:    */     }
/* 129:    */   }
/* 130:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.TransferServlet
 * JD-Core Version:    0.7.0.1
 */