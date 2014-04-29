/*   1:    */ package org.springframework.remoting.httpinvoker;
/*   2:    */ 
/*   3:    */ import com.sun.net.httpserver.Headers;
/*   4:    */ import com.sun.net.httpserver.HttpExchange;
/*   5:    */ import com.sun.net.httpserver.HttpHandler;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.InputStream;
/*   8:    */ import java.io.ObjectInputStream;
/*   9:    */ import java.io.ObjectOutputStream;
/*  10:    */ import java.io.OutputStream;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
/*  13:    */ import org.springframework.remoting.support.RemoteInvocation;
/*  14:    */ import org.springframework.remoting.support.RemoteInvocationResult;
/*  15:    */ 
/*  16:    */ public class SimpleHttpInvokerServiceExporter
/*  17:    */   extends RemoteInvocationSerializingExporter
/*  18:    */   implements HttpHandler
/*  19:    */ {
/*  20:    */   public void handle(HttpExchange exchange)
/*  21:    */     throws IOException
/*  22:    */   {
/*  23:    */     try
/*  24:    */     {
/*  25: 66 */       RemoteInvocation invocation = readRemoteInvocation(exchange);
/*  26: 67 */       RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
/*  27: 68 */       writeRemoteInvocationResult(exchange, result);
/*  28: 69 */       exchange.close();
/*  29:    */     }
/*  30:    */     catch (ClassNotFoundException ex)
/*  31:    */     {
/*  32: 72 */       exchange.sendResponseHeaders(500, -1L);
/*  33: 73 */       this.logger.error("Class not found during deserialization", ex);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange)
/*  38:    */     throws IOException, ClassNotFoundException
/*  39:    */   {
/*  40: 91 */     return readRemoteInvocation(exchange, exchange.getRequestBody());
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange, InputStream is)
/*  44:    */     throws IOException, ClassNotFoundException
/*  45:    */   {
/*  46:110 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(exchange, is));
/*  47:111 */     return doReadRemoteInvocation(ois);
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected InputStream decorateInputStream(HttpExchange exchange, InputStream is)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53:125 */     return is;
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result)
/*  57:    */     throws IOException
/*  58:    */   {
/*  59:137 */     exchange.getResponseHeaders().set("Content-Type", getContentType());
/*  60:138 */     exchange.sendResponseHeaders(200, 0L);
/*  61:139 */     writeRemoteInvocationResult(exchange, result, exchange.getResponseBody());
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result, OutputStream os)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67:159 */     ObjectOutputStream oos = createObjectOutputStream(decorateOutputStream(exchange, os));
/*  68:160 */     doWriteRemoteInvocationResult(result, oos);
/*  69:161 */     oos.flush();
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected OutputStream decorateOutputStream(HttpExchange exchange, OutputStream os)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75:175 */     return os;
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.httpinvoker.SimpleHttpInvokerServiceExporter
 * JD-Core Version:    0.7.0.1
 */