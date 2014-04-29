/*  1:   */ package com.kentropy.grid;
/*  2:   */ 
/*  3:   */ import com.dhtmlx.xml2excel.ExcelWriter;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.io.PrintWriter;
/*  7:   */ import java.net.URLDecoder;
/*  8:   */ import javax.servlet.ServletException;
/*  9:   */ import javax.servlet.http.HttpServlet;
/* 10:   */ import javax.servlet.http.HttpServletRequest;
/* 11:   */ import javax.servlet.http.HttpServletResponse;
/* 12:   */ import org.springframework.web.servlet.ModelAndView;
/* 13:   */ import org.springframework.web.servlet.mvc.Controller;
/* 14:   */ 
/* 15:   */ public class KenGenerateExcel
/* 16:   */   extends HttpServlet
/* 17:   */   implements Controller
/* 18:   */ {
/* 19:   */   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
/* 20:   */     throws ServletException, IOException
/* 21:   */   {
/* 22:21 */     int report = Integer.parseInt(req.getParameter("report"));
/* 23:   */     
/* 24:23 */     String sql = "";
/* 25:25 */     switch (report)
/* 26:   */     {
/* 27:   */     case 1: 
/* 28:28 */       sql = "SELECT b.username,b.name,a.*,IF(c.cancl IS NULL,0,c.cancl) cancelled,IF(d.recon IS NULL,0,d.recon) reconciliation,IF(e.icd_illdefined IS NULL,0,icd_illdefined) icd_illdefined,f.total_tasks_completed,g.total_tasks_completed_last_month  FROM (SELECT assignedto id,SUM(IF(task LIKE '%task0/task0',1,0)) cod_assg,SUM(IF(task LIKE '%task0/task0' AND STATUS=1,1,0)) cod_comp,SUM(IF(task LIKE '%task0/task0' AND STATUS IS NULL,1,0)) cod_pending,SUM(IF(task LIKE '%task0/task1' ,1,0)) recon_assg,SUM(IF(task LIKE '%task0/task1' AND STATUS=1,1,0)) recon_comp,SUM(IF(task LIKE '%task0/task1' AND STATUS IS NULL,1,0)) recon_pending,SUM(IF(task LIKE '%task0/task2',1,0)) adj_assg,SUM(IF(task LIKE '%task0/task2' AND STATUS=1,1,0)) adj_comp,SUM(IF(task LIKE '%task0/task2' AND STATUS IS NULL,1,0)) adj_pending  FROM tasks GROUP BY id) a LEFT JOIN physician b ON a.id=b.id  LEFT JOIN (SELECT COUNT(*) cancl,physician1 FROM cancellation GROUP BY physician1) c ON c.physician1=a.id LEFT JOIN (SELECT COUNT(*) recon,assignedto FROM tasks WHERE task LIKE '%task0/task1'  GROUP BY assignedto ) d ON d.assignedto=a.id LEFT JOIN (SELECT COUNT(*) icd_illdefined,physician FROM cme_report p  JOIN icd_illdefined q ON p.icd=q.icd GROUP BY p.physician) e ON e.physician=a.id LEFT JOIN (SELECT SUM(IF(task LIKE '%task0/task%' AND STATUS=1,1,0)) total_tasks_completed,assignedto FROM tasks GROUP BY assignedto) f ON a.id=f.assignedto LEFT JOIN (SELECT SUM(IF(task LIKE '%task0/task%' AND STATUS=1 AND CONCAT(YEAR(endtime),'-',MONTH(endtime))=CONCAT(YEAR(NOW()),'-',MONTH(NOW())-1),1,0)) total_tasks_completed_last_month,assignedto FROM tasks GROUP BY assignedto) g ON a.id=g.assignedto";
/* 29:   */     }
/* 30:32 */     GridUtils gu = new GridUtils();
/* 31:33 */     gu.getGridModel(sql, 150);
/* 32:34 */     String xml = gu.getExcel().toString();
/* 33:   */     try
/* 34:   */     {
/* 35:36 */       new ExcelWriter().generate(xml, resp);
/* 36:   */     }
/* 37:   */     catch (Exception e)
/* 38:   */     {
/* 39:39 */       System.out.println("ERROR WHILE PARSING XML");
/* 40:40 */       resp.getWriter().println("<span style='color:red'>ERROR PARSING XML !<span>");
/* 41:41 */       e.printStackTrace();
/* 42:   */     }
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected void doPost(HttpServletRequest req, HttpServletResponse resp)
/* 46:   */     throws ServletException, IOException
/* 47:   */   {
/* 48:48 */     String sql = req.getParameter("sql");
/* 49:49 */     if ((sql == null) || (sql.equals(""))) {
/* 50:50 */       return;
/* 51:   */     }
/* 52:51 */     sql = URLDecoder.decode(sql, "UTF-8");
/* 53:52 */     GridUtils gu = new GridUtils();
/* 54:53 */     gu.getGridModel(sql, 150);
/* 55:54 */     String xml = gu.getExcel().toString();
/* 56:   */     try
/* 57:   */     {
/* 58:56 */       new ExcelWriter().generate(xml, resp);
/* 59:   */     }
/* 60:   */     catch (Exception e)
/* 61:   */     {
/* 62:59 */       System.out.println("ERROR WHILE PARSING XML");
/* 63:60 */       resp.getWriter().println("<span style='color:red'>ERROR PARSING XML !<span>");
/* 64:61 */       e.printStackTrace();
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp)
/* 69:   */     throws Exception
/* 70:   */   {
/* 71:68 */     if (req.getParameter("action").equals("doGet")) {
/* 72:69 */       doGet(req, resp);
/* 73:   */     } else {
/* 74:71 */       doPost(req, resp);
/* 75:   */     }
/* 76:73 */     return null;
/* 77:   */   }
/* 78:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.KenGenerateExcel
 * JD-Core Version:    0.7.0.1
 */