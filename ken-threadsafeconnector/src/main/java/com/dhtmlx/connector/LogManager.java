/*   1:    */ package com.dhtmlx.connector;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Writer;
/*   5:    */ import java.util.Date;
/*   6:    */ 
/*   7:    */ public class LogManager
/*   8:    */ {
/*   9:    */   private static LogManager instance;
/*  10:    */   
/*  11:    */   public static LogManager getInstance()
/*  12:    */   {
/*  13: 23 */     if (instance == null) {
/*  14: 24 */       instance = new LogManager();
/*  15:    */     }
/*  16: 25 */     return instance;
/*  17:    */   }
/*  18:    */   
/*  19: 29 */   private boolean enabled = false;
/*  20: 32 */   public boolean client_log = false;
/*  21:    */   private Writer log_writer;
/*  22: 38 */   private StringBuffer session = new StringBuffer();
/*  23:    */   
/*  24:    */   public void enable_log(Writer writer, boolean client)
/*  25:    */   {
/*  26: 47 */     if (this.log_writer != null) {
/*  27:    */       try
/*  28:    */       {
/*  29: 49 */         this.log_writer.close();
/*  30:    */       }
/*  31:    */       catch (IOException localIOException) {}
/*  32:    */     }
/*  33: 54 */     this.log_writer = writer;
/*  34: 55 */     this.client_log = client;
/*  35: 56 */     this.enabled = (writer != null);
/*  36:    */     
/*  37: 58 */     log("\n\n====================================\nLog started, " + new Date().toString() + "\n====================================");
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void enable_log(Writer writer)
/*  41:    */   {
/*  42: 68 */     enable_log(writer, false);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void log(String data)
/*  46:    */   {
/*  47: 77 */     if (this.enabled) {
/*  48:    */       try
/*  49:    */       {
/*  50: 79 */         this.session.append("\n" + data);
/*  51: 80 */         this.log_writer.write("\n" + data);
/*  52:    */       }
/*  53:    */       catch (IOException localIOException) {}
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void log(String name, String value)
/*  58:    */   {
/*  59: 94 */     if (this.enabled) {
/*  60: 95 */       log(name + " : " + value);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String get_session_log()
/*  65:    */   {
/*  66:104 */     return this.session.toString();
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void close()
/*  70:    */   {
/*  71:111 */     if (this.log_writer == null) {
/*  72:111 */       return;
/*  73:    */     }
/*  74:    */     try
/*  75:    */     {
/*  76:114 */       this.log_writer.flush();
/*  77:115 */       this.log_writer.close();
/*  78:    */     }
/*  79:    */     catch (IOException e)
/*  80:    */     {
/*  81:118 */       e.printStackTrace();
/*  82:    */     }
/*  83:    */   }
/*  84:    */   
/*  85:    */   protected void finalize()
/*  86:    */   {
/*  87:126 */     close();
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-threadsafeconnector\ken-threadsafeconnector.jar
 * Qualified Name:     com.dhtmlx.connector.LogManager
 * JD-Core Version:    0.7.0.1
 */