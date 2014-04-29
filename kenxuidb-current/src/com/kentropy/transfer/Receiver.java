/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import java.io.DataInputStream;
/*   4:    */ import java.io.DataOutputStream;
/*   5:    */ import java.io.FileWriter;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.net.Socket;
/*   8:    */ import java.util.zip.ZipInputStream;
/*   9:    */ 
/*  10:    */ public class Receiver
/*  11:    */ {
/*  12: 12 */   public Socket sock = null;
/*  13: 13 */   public DataOutputStream dout = null;
/*  14: 14 */   int count = 0;
/*  15: 15 */   public String state = "headers";
/*  16: 16 */   public String currentLogId = "";
/*  17: 17 */   public StringBuffer log = new StringBuffer();
/*  18: 18 */   public String headers = "";
/*  19:    */   
/*  20:    */   public void startLog()
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 22 */     System.out.println("Start ");
/*  24: 23 */     if (this.state.equals("Started"))
/*  25:    */     {
/*  26: 25 */       this.dout.writeBytes("Error: Two starts in one message\n");
/*  27: 26 */       return;
/*  28:    */     }
/*  29: 29 */     this.state = "Started";
/*  30: 30 */     this.count += 1;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void endLog()
/*  34:    */     throws Exception
/*  35:    */   {
/*  36: 35 */     if (!this.state.equals("Started"))
/*  37:    */     {
/*  38: 37 */       this.dout.writeBytes("Error: End without start\n");
/*  39: 38 */       return;
/*  40:    */     }
/*  41: 41 */     this.state = "ended";
/*  42: 43 */     if (!saveLog()) {
/*  43: 45 */       this.dout.writeBytes("Error: while saving " + this.currentLogId + "\n");
/*  44:    */     }
/*  45: 46 */     this.currentLogId = "";
/*  46:    */   }
/*  47:    */   
/*  48:    */   public boolean saveLog()
/*  49:    */   {
/*  50: 51 */     return true;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void handleHeader(String header)
/*  54:    */   {
/*  55: 55 */     System.out.println("Header " + header);
/*  56: 56 */     if (!header.trim().equals("")) {
/*  57: 57 */       this.headers += header;
/*  58:    */     } else {
/*  59: 59 */       this.state = "";
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void handleLine(String line)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 63 */     if (line.startsWith("<l "))
/*  67:    */     {
/*  68: 65 */       startLog();
/*  69: 66 */       this.log.append(line);
/*  70:    */     }
/*  71: 68 */     else if (line.startsWith("</l"))
/*  72:    */     {
/*  73: 70 */       this.log.append(line);
/*  74: 71 */       endLog();
/*  75:    */     }
/*  76:    */     else
/*  77:    */     {
/*  78: 74 */       this.log.append(line);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void run()
/*  83:    */   {
/*  84:    */     try
/*  85:    */     {
/*  86: 81 */       ZipInputStream zin = new ZipInputStream(this.sock.getInputStream());
/*  87: 82 */       zin.getNextEntry();
/*  88: 83 */       DataInputStream in = new DataInputStream(zin);
/*  89: 84 */       this.dout = new DataOutputStream(this.sock.getOutputStream());
/*  90: 85 */       String line = in.readLine();int count = 0;
/*  91: 86 */       this.log.append("<logs>");
/*  92: 87 */       while (line != null)
/*  93:    */       {
/*  94: 89 */         if (this.state.equals("headers")) {
/*  95: 91 */           handleHeader(line);
/*  96:    */         } else {
/*  97: 95 */           handleLine(line);
/*  98:    */         }
/*  99: 97 */         line = in.readLine();
/* 100: 98 */         count++;
/* 101:    */       }
/* 102:100 */       this.log.append("</logs>");
/* 103:101 */       FileWriter fw = new FileWriter("c:\\received.xml");
/* 104:102 */       System.out.println("Line count " + count);
/* 105:    */       
/* 106:104 */       fw.write(this.log.toString());
/* 107:105 */       fw.close();
/* 108:106 */       FileWriter fw1 = new FileWriter("c:\\received1.xml");
/* 109:107 */       System.out.println("Line count " + count);
/* 110:108 */       fw1.write(this.log.toString());
/* 111:109 */       fw1.close();
/* 112:    */     }
/* 113:    */     catch (Exception e)
/* 114:    */     {
/* 115:113 */       e.printStackTrace();
/* 116:    */     }
/* 117:    */   }
/* 118:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Receiver
 * JD-Core Version:    0.7.0.1
 */