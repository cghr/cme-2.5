/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Method;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.servlet.http.HttpServletRequest;
/*   6:    */ import javax.servlet.http.HttpServletResponse;
/*   7:    */ import org.apache.velocity.VelocityContext;
/*   8:    */ import org.apache.velocity.context.Context;
/*   9:    */ import org.apache.velocity.tools.view.ToolboxManager;
/*  10:    */ import org.apache.velocity.tools.view.context.ChainedContext;
/*  11:    */ import org.apache.velocity.tools.view.servlet.ServletToolboxManager;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ import org.springframework.util.ReflectionUtils;
/*  14:    */ 
/*  15:    */ public class VelocityToolboxView
/*  16:    */   extends VelocityView
/*  17:    */ {
/*  18:    */   private String toolboxConfigLocation;
/*  19:    */   
/*  20:    */   public void setToolboxConfigLocation(String toolboxConfigLocation)
/*  21:    */   {
/*  22: 82 */     this.toolboxConfigLocation = toolboxConfigLocation;
/*  23:    */   }
/*  24:    */   
/*  25:    */   protected String getToolboxConfigLocation()
/*  26:    */   {
/*  27: 89 */     return this.toolboxConfigLocation;
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*  31:    */     throws Exception
/*  32:    */   {
/*  33:104 */     ChainedContext velocityContext = new ChainedContext(
/*  34:105 */       new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());
/*  35:108 */     if (getToolboxConfigLocation() != null)
/*  36:    */     {
/*  37:109 */       ToolboxManager toolboxManager = ServletToolboxManager.getInstance(
/*  38:110 */         getServletContext(), getToolboxConfigLocation());
/*  39:111 */       Map toolboxContext = toolboxManager.getToolbox(velocityContext);
/*  40:112 */       velocityContext.setToolbox(toolboxContext);
/*  41:    */     }
/*  42:115 */     return velocityContext;
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected void initTool(Object tool, Context velocityContext)
/*  46:    */     throws Exception
/*  47:    */   {
/*  48:126 */     Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", new Class[] { Object.class });
/*  49:127 */     if (initMethod != null) {
/*  50:128 */       ReflectionUtils.invokeMethod(initMethod, tool, new Object[] { velocityContext });
/*  51:    */     }
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityToolboxView
 * JD-Core Version:    0.7.0.1
 */