/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.ObjectInputStream;
/*   6:    */ import java.io.ObjectOutputStream;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import javax.servlet.ServletException;
/*   9:    */ import javax.servlet.http.HttpServletRequest;
/*  10:    */ import javax.servlet.http.HttpServletResponse;
/*  11:    */ import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
/*  12:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  13:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  14:    */ import org.springframework.web.HttpRequestHandler;
/*  15:    */ import org.springframework.web.util.NestedServletException;
/*  16:    */ 
/*  17:    */ public class HttpInvokerServiceExporter
/*  18:    */   extends RemoteInvocationSerializingExporter
/*  19:    */   implements HttpRequestHandler
/*  20:    */ {
/*  21:    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response)
/*  22:    */     throws ServletException, IOException
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 72 */       RemoteInvocation invocation = readRemoteInvocation(request);
/*  27: 73 */       RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
/*  28: 74 */       writeRemoteInvocationResult(request, response, result);
/*  29:    */     }
/*  30:    */     catch (ClassNotFoundException ex)
/*  31:    */     {
/*  32: 77 */       throw new NestedServletException("Class not found during deserialization", ex);
/*  33:    */     }
/*  34:    */   }
/*  35:    */   
/*  36:    */   protected RemoteInvocation readRemoteInvocation(HttpServletRequest request)
/*  37:    */     throws IOException, ClassNotFoundException
/*  38:    */   {
/*  39: 95 */     return readRemoteInvocation(request, request.getInputStream());
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected RemoteInvocation readRemoteInvocation(HttpServletRequest request, InputStream is)
/*  43:    */     throws IOException, ClassNotFoundException
/*  44:    */   {
/*  45:114 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(request, is));
/*  46:    */     try
/*  47:    */     {
/*  48:116 */       return doReadRemoteInvocation(ois);
/*  49:    */     }
/*  50:    */     finally
/*  51:    */     {
/*  52:119 */       ois.close();
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected InputStream decorateInputStream(HttpServletRequest request, InputStream is)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:134 */     return is;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected void writeRemoteInvocationResult(HttpServletRequest request, HttpServletResponse response, RemoteInvocationResult result)
/*  63:    */     throws IOException
/*  64:    */   {
/*  65:148 */     response.setContentType(getContentType());
/*  66:149 */     writeRemoteInvocationResult(request, response, result, response.getOutputStream());
/*  67:    */   }
/*  68:    */   
/*  69:    */   protected void writeRemoteInvocationResult(HttpServletRequest request, HttpServletResponse response, RemoteInvocationResult result, OutputStream os)
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:171 */     ObjectOutputStream oos = createObjectOutputStream(decorateOutputStream(request, response, os));
/*  73:    */     try
/*  74:    */     {
/*  75:173 */       doWriteRemoteInvocationResult(result, oos);
/*  76:    */     }
/*  77:    */     finally
/*  78:    */     {
/*  79:176 */       oos.close();
/*  80:    */     }
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected OutputStream decorateOutputStream(HttpServletRequest request, HttpServletResponse response, OutputStream os)
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:194 */     return os;
/*  87:    */   }
/*  88:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter
 * JD-Core Version:    0.7.0.1
 */