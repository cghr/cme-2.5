/*   1:    */ package com.kentropy.wfforms;
/*   2:    */ 
/*   3:    */ import com.kentropy.graphs.Graph;
/*   4:    */ import com.kentropy.graphs.Graph.GraphNode;
/*   5:    */ import com.kentropy.util.SpringUtils;
/*   6:    */ import com.kentropy.util.web.FormSubmit;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.text.SimpleDateFormat;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.Hashtable;
/*  12:    */ import javax.servlet.ServletException;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ import javax.servlet.http.HttpSession;
/*  16:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  17:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  18:    */ import org.springframework.web.servlet.ModelAndView;
/*  19:    */ 
/*  20:    */ public class WfFormSubmit
/*  21:    */   extends FormSubmit
/*  22:    */ {
/*  23:    */   public static void main(String[] args) {}
/*  24:    */   
/*  25:    */   protected void service(HttpServletRequest request, HttpServletResponse response)
/*  26:    */     throws ServletException, IOException
/*  27:    */   {
/*  28: 34 */     super.service(request, response);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  32:    */     throws Exception
/*  33:    */   {
/*  34: 42 */     String user = request.getSession().getAttribute("username").toString();
/*  35: 43 */     ModelAndView mv = super.handleRequest(request, response);
/*  36: 44 */     JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  37: 45 */     SqlRowSet rs = jt.queryForRowSet("select LAST_INSERT_ID() last_id");
/*  38: 46 */     String id = request.getParameter("id");
/*  39: 47 */     String form = request.getParameter("jsonId");
/*  40: 48 */     String qry = "";
/*  41: 49 */     String time1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
/*  42: 50 */     if (id == null)
/*  43:    */     {
/*  44: 51 */       if (rs.next()) {
/*  45: 52 */         id = rs.getString("last_id");
/*  46:    */       }
/*  47: 55 */       qry = "insert into workflow_tasks (pid,assigned_to,assigned_time,due_time,start_time,end_time,status,action_taken) values()";
/*  48: 56 */       System.out.println("1:" + qry);
/*  49: 57 */       Graph g = Graph.getGraph("Form", "1");
/*  50: 58 */       Graph.GraphNode gn = g.getNode(form);
/*  51: 59 */       String wf = (String)gn.getRelationships().get("WF");
/*  52: 60 */       Graph g1 = Graph.getGraph(wf, "1");
/*  53:    */       
/*  54: 62 */       Graph.GraphNode gn1 = g1.getNode("WF.Start");
/*  55: 63 */       String wfNode = (String)gn1.getRelationships().get("WF.event.onLaunch");
/*  56: 64 */       Graph.GraphNode wfNode1 = g1.getNode(wfNode);
/*  57: 65 */       String assignNode = (String)wfNode1.getRelationships().get("assign");
/*  58: 66 */       System.out.println(" assign-->" + assignNode);
/*  59: 67 */       String[] arr = assignNode.split("\\.");
/*  60: 68 */       String creator = user;
/*  61: 69 */       if (arr[0].equals("Form")) {
/*  62: 71 */         if (arr[1].equals("Creator")) {
/*  63: 73 */           if (arr.length > 2)
/*  64:    */           {
/*  65: 75 */             Graph empGraph = Graph.getGraph(arr[2], "1");
/*  66: 76 */             Graph.GraphNode gn2 = empGraph.getNode("Emp." + user);
/*  67:    */             
/*  68: 78 */             String empNode = (String)gn2.getRelationships().get(arr[3]);
/*  69: 79 */             System.out.println(" Employee " + empNode);
/*  70: 80 */             System.out.println("2:insert into timesheet (form,form_id,assignedto,task,plan_startdate,plan_enddate,wf,wfnode ) VALUES('" + form + "','" + id + "','" + empNode + "','Process',NOW(),NOW(),'" + wf + "','" + wfNode + "'");
/*  71: 81 */             jt.execute("insert into timesheet (form,form_id,assignedto,task,plan_startdate,plan_enddate,wf,wfnode ) VALUES('" + form + "','" + id + "','" + empNode + "','Process',NOW(),NOW(),'" + wf + "','" + wfNode + "')");
/*  72:    */           }
/*  73:    */         }
/*  74:    */       }
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78: 96 */       System.out.println("3:" + qry);
/*  79:    */     }
/*  80: 98 */     jt.update(qry);
/*  81:    */     
/*  82:    */ 
/*  83:    */ 
/*  84:102 */     return mv;
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-utilities6\ken-utilities6.jar
 * Qualified Name:     com.kentropy.wfforms.WfFormSubmit
 * JD-Core Version:    0.7.0.1
 */