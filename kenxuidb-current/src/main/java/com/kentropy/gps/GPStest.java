/*  1:   */ package com.kentropy.gps;
/*  2:   */ 
/*  3:   */ import java.io.DataInputStream;
/*  4:   */ import java.io.DataOutputStream;
/*  5:   */ import java.io.PrintStream;
/*  6:   */ import java.net.Socket;
/*  7:   */ import org.apache.log4j.BasicConfigurator;
/*  8:   */ 
/*  9:   */ public class GPStest
/* 10:   */ {
/* 11:   */   public static String[] getGPS(String host)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:14 */     CustomSerialClient csc = new CustomSerialClient();
/* 15:15 */     csc.read();
/* 16:16 */     String[] ret = new String[2];
/* 17:17 */     if (csc.currentFix == 1) {
/* 18:18 */       throw new Exception("No fix");
/* 19:   */     }
/* 20:19 */     ret[0] = csc.lat;
/* 21:20 */     ret[1] = csc.longi;
/* 22:21 */     return ret;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static String[] getGPS1(String host)
/* 26:   */     throws Exception
/* 27:   */   {
/* 28:25 */     String cmd = "P";
/* 29:26 */     Socket client = new Socket(host, 2947);
/* 30:27 */     String[] coords1 = (String[])null;
/* 31:   */     
/* 32:29 */     DataInputStream din = new DataInputStream(client.getInputStream());
/* 33:30 */     DataOutputStream dout = new DataOutputStream(client.getOutputStream());
/* 34:31 */     dout.writeBytes("P");
/* 35:32 */     din.readLine();
/* 36:33 */     dout.writeBytes(cmd);
/* 37:   */     
/* 38:35 */     Thread.currentThread();Thread.sleep(500L);
/* 39:36 */     String line = din.readLine();
/* 40:37 */     System.out.println(line);
/* 41:38 */     dout.writeBytes(cmd);
/* 42:39 */     line = din.readLine();
/* 43:40 */     System.out.println(line);
/* 44:41 */     String coords = "";
/* 45:42 */     if (line.indexOf(cmd + "=") != -1)
/* 46:   */     {
/* 47:44 */       int pos = line.indexOf(cmd + "=");
/* 48:45 */       coords = line.substring(pos + (cmd + "=").length(), line.length());
/* 49:46 */       System.out.println(coords);
/* 50:   */     }
/* 51:48 */     client.close();
/* 52:49 */     if (coords.equals("?")) {
/* 53:50 */       throw new Exception("No fix");
/* 54:   */     }
/* 55:52 */     coords1 = coords.split(" ");
/* 56:53 */     return coords1;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public static void main(String[] args)
/* 60:   */     throws Exception
/* 61:   */   {
/* 62:   */     
/* 63:59 */     for (int i = 0; i < 1; i++) {
/* 64:   */       try
/* 65:   */       {
/* 66:61 */         String[] tt = getGPS("192.168.1.107");
/* 67:62 */         System.out.println("lat=" + tt[0] + "\tlon=" + tt[1]);
/* 68:   */       }
/* 69:   */       catch (Exception e)
/* 70:   */       {
/* 71:66 */         e.printStackTrace();
/* 72:   */       }
/* 73:   */     }
/* 74:   */   }
/* 75:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.gps.GPStest
 * JD-Core Version:    0.7.0.1
 */