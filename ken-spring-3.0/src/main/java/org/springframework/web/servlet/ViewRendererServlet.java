/*   1:    */ package org.springframework.web.servlet;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.ServletException;
/*   6:    */ import javax.servlet.http.HttpServlet;
/*   7:    */ import javax.servlet.http.HttpServletRequest;
/*   8:    */ import javax.servlet.http.HttpServletResponse;
/*   9:    */ import org.springframework.web.util.NestedServletException;
/*  10:    */ 
/*  11:    */ public class ViewRendererServlet
/*  12:    */   extends HttpServlet
/*  13:    */ {
/*  14: 53 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
/*  15: 56 */   public static final String VIEW_ATTRIBUTE = ViewRendererServlet.class.getName() + ".VIEW";
/*  16: 59 */   public static final String MODEL_ATTRIBUTE = ViewRendererServlet.class.getName() + ".MODEL";
/*  17:    */   
/*  18:    */   protected final void doGet(HttpServletRequest request, HttpServletResponse response)
/*  19:    */     throws ServletException, IOException
/*  20:    */   {
/*  21: 66 */     processRequest(request, response);
/*  22:    */   }
/*  23:    */   
/*  24:    */   protected final void doPost(HttpServletRequest request, HttpServletResponse response)
/*  25:    */     throws ServletException, IOException
/*  26:    */   {
/*  27: 73 */     processRequest(request, response);
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
/*  31:    */     throws ServletException, IOException
/*  32:    */   {
/*  33:    */     try
/*  34:    */     {
/*  35: 86 */       renderView(request, response);
/*  36:    */     }
/*  37:    */     catch (ServletException ex)
/*  38:    */     {
/*  39: 89 */       throw ex;
/*  40:    */     }
/*  41:    */     catch (IOException ex)
/*  42:    */     {
/*  43: 92 */       throw ex;
/*  44:    */     }
/*  45:    */     catch (Exception ex)
/*  46:    */     {
/*  47: 95 */       throw new NestedServletException("View rendering failed", ex);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected void renderView(HttpServletRequest request, HttpServletResponse response)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54:109 */     View view = (View)request.getAttribute(VIEW_ATTRIBUTE);
/*  55:110 */     if (view == null) {
/*  56:111 */       throw new ServletException("Could not complete render request: View is null");
/*  57:    */     }
/*  58:113 */     Map<String, Object> model = (Map)request.getAttribute(MODEL_ATTRIBUTE);
/*  59:114 */     view.render(model, request, response);
/*  60:    */   }
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.ViewRendererServlet
 * JD-Core Version:    0.7.0.1
 */