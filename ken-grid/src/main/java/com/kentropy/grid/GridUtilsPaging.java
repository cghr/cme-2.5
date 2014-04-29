/*  1:   */ package com.kentropy.grid;
/*  2:   */ 
/*  3:   */ import com.dhtmlx.connector.GridConnector;
/*  4:   */ import com.dhtmlx.connector.ThreadSafeConnectorServlet;
/*  5:   */ import com.kentropy.util.SpringApplicationContext;
/*  6:   */ import java.sql.Connection;
/*  7:   */ import java.sql.SQLException;
/*  8:   */ import java.util.HashMap;
/*  9:   */ import java.util.Set;
/* 10:   */ import javax.servlet.http.HttpServletRequest;
/* 11:   */ import javax.servlet.http.HttpServletResponse;
/* 12:   */ import javax.servlet.http.HttpSession;
/* 13:   */ import javax.sql.DataSource;
/* 14:   */ import org.springframework.web.servlet.ModelAndView;
/* 15:   */ import org.springframework.web.servlet.mvc.Controller;
/* 16:   */ 
/* 17:   */ public class GridUtilsPaging
/* 18:   */   extends ThreadSafeConnectorServlet
/* 19:   */   implements Controller
/* 20:   */ {
/* 21:   */   private static final long serialVersionUID = 1L;
/* 22:22 */   StringBuffer fields = new StringBuffer();
/* 23:23 */   String sql = "";
/* 24:24 */   String id = "";
/* 25:25 */   int recordsPerPage = 50;
/* 26:26 */   DataSource dataSource = (DataSource)SpringApplicationContext.getBean("dataSource");
/* 27:27 */   Connection connection = null;
/* 28:   */   
/* 29:   */   protected void configure(HttpServletRequest request, HttpServletResponse response)
/* 30:   */   {
/* 31:   */     try
/* 32:   */     {
/* 33:33 */       this.connection = this.dataSource.getConnection();
/* 34:   */       
/* 35:35 */       HttpSession session = request.getSession(true);
/* 36:36 */       this.fields = ((StringBuffer)session.getAttribute("gridHeadings"));
/* 37:37 */       this.sql = ((String)session.getAttribute("sql"));
/* 38:38 */       this.id = ((String)session.getAttribute("gridId"));
/* 39:39 */       this.recordsPerPage = ((Integer)session.getAttribute("dynamicLoading")).intValue();
/* 40:   */       
/* 41:41 */       GridConnector gc = new GridConnector(this.connection);
/* 42:   */       
/* 43:43 */       gc.servlet(request, response);
/* 44:44 */       gc.dynamic_loading(this.recordsPerPage);
/* 45:46 */       if (session.getAttribute("hasSelectFilters") != null) {
/* 46:48 */         if (((Boolean)session.getAttribute("hasSelectFilters")).booleanValue())
/* 47:   */         {
/* 48:50 */           HashMap selectFilterOptions = (HashMap)session.getAttribute("selectFilterMap");
/* 49:51 */           Set keys = selectFilterOptions.keySet();
/* 50:53 */           for (Object key : keys) {
/* 51:55 */             gc.set_options((String)key, (HashMap)selectFilterOptions.get(key));
/* 52:   */           }
/* 53:   */         }
/* 54:   */       }
/* 55:62 */       gc.render_sql(this.sql, this.id, this.fields.toString());
/* 56:   */     }
/* 57:   */     catch (Exception e)
/* 58:   */     {
/* 59:66 */       e.printStackTrace();
/* 60:   */       try
/* 61:   */       {
/* 62:69 */         this.connection.close();
/* 63:   */       }
/* 64:   */       catch (SQLException e1)
/* 65:   */       {
/* 66:72 */         e1.printStackTrace();
/* 67:   */       }
/* 68:   */       try
/* 69:   */       {
/* 70:79 */         this.connection.close();
/* 71:   */       }
/* 72:   */       catch (SQLException e)
/* 73:   */       {
/* 74:82 */         e.printStackTrace();
/* 75:   */       }
/* 76:   */     }
/* 77:   */     finally
/* 78:   */     {
/* 79:   */       try
/* 80:   */       {
/* 81:79 */         this.connection.close();
/* 82:   */       }
/* 83:   */       catch (SQLException e)
/* 84:   */       {
/* 85:82 */         e.printStackTrace();
/* 86:   */       }
/* 87:   */     }
/* 88:   */   }
/* 89:   */   
/* 90:   */   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
/* 91:   */     throws Exception
/* 92:   */   {
/* 93:90 */     configure(request, response);
/* 94:91 */     return null;
/* 95:   */   }
/* 96:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-grid\ken-grid.jar
 * Qualified Name:     com.kentropy.grid.GridUtilsPaging
 * JD-Core Version:    0.7.0.1
 */