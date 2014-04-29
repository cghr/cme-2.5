/*   1:    */ package com.kentropy.services;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.net.DatagramPacket;
/*   6:    */ import java.net.DatagramSocket;
/*   7:    */ import java.net.InetAddress;
/*   8:    */ import java.text.DecimalFormat;
/*   9:    */ import java.util.Date;
/*  10:    */ 
/*  11:    */ public class SntpClient
/*  12:    */ {
/*  13:    */   public double getTimeStamp(long tm)
/*  14:    */   {
/*  15: 44 */     double timestamp = 
/*  16: 45 */       tm / 1000.0D + 2208988800.0D;
/*  17: 46 */     return timestamp;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public double getClockOffset(NtpMessage msg, long tm)
/*  21:    */   {
/*  22: 51 */     return (msg.receiveTimestamp - msg.originateTimestamp + (
/*  23: 52 */       msg.transmitTimestamp - getTimeStamp(tm))) / 2.0D;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public NtpMessage getMessage(String serverName)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 58 */     DatagramSocket socket = new DatagramSocket();
/*  30: 59 */     InetAddress address = InetAddress.getByName(serverName);
/*  31: 60 */     byte[] buf = new NtpMessage().toByteArray();
/*  32: 61 */     DatagramPacket packet = 
/*  33: 62 */       new DatagramPacket(buf, buf.length, address, 123);
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37: 66 */     NtpMessage.encodeTimestamp(packet.getData(), 40, 
/*  38: 67 */       System.currentTimeMillis() / 1000.0D + 2208988800.0D);
/*  39:    */     
/*  40: 69 */     socket.send(packet);
/*  41:    */     
/*  42:    */ 
/*  43:    */ 
/*  44: 73 */     System.out.println("NTP request sent, waiting for response...\n");
/*  45: 74 */     packet = new DatagramPacket(buf, buf.length);
/*  46: 75 */     socket.receive(packet);
/*  47: 76 */     socket.close();
/*  48: 77 */     NtpMessage msg = new NtpMessage(packet.getData());
/*  49: 78 */     return msg;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static void main(String[] args)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:    */     String serverName;
/*  56: 88 */     if (args.length == 1)
/*  57:    */     {
/*  58: 90 */       serverName = args[0];
/*  59:    */     }
/*  60:    */     else
/*  61:    */     {
/*  62: 94 */       printUsage(); return;
/*  63:    */     }
/*  64:    */     String serverName;
/*  65:102 */     NtpMessage msg = new SntpClient().getMessage(serverName);
/*  66:103 */     double destinationTimestamp = new SntpClient().getTimeStamp(System.currentTimeMillis());
/*  67:    */     
/*  68:    */ 
/*  69:    */ 
/*  70:107 */     double roundTripDelay = destinationTimestamp - msg.originateTimestamp - (
/*  71:108 */       msg.transmitTimestamp - msg.receiveTimestamp);
/*  72:    */     
/*  73:110 */     double localClockOffset = 
/*  74:111 */       (msg.receiveTimestamp - msg.originateTimestamp + (
/*  75:112 */       msg.transmitTimestamp - destinationTimestamp)) / 2.0D;
/*  76:    */     
/*  77:114 */     double localClockOffset1 = new SntpClient().getClockOffset(msg, System.currentTimeMillis());
/*  78:    */     
/*  79:116 */     double localClockOffset2 = new SntpClient().getClockOffset(msg, new Date().getTime());
/*  80:    */     
/*  81:118 */     System.out.println("NTP server: " + serverName);
/*  82:119 */     System.out.println(msg.toString());
/*  83:    */     
/*  84:    */ 
/*  85:122 */     System.out.println("Dest. timestamp:     " + 
/*  86:123 */       NtpMessage.timestampToString(destinationTimestamp));
/*  87:    */     
/*  88:125 */     System.out.println("Round-trip delay: " + 
/*  89:126 */       new DecimalFormat("0.00").format(roundTripDelay * 1000.0D) + " ms");
/*  90:    */     
/*  91:128 */     System.out.println("Local clock offset: " + 
/*  92:129 */       new DecimalFormat("0.00").format(localClockOffset * 1000.0D) + " ms");
/*  93:    */     
/*  94:131 */     System.out.println("Local clock offset1: " + 
/*  95:132 */       new DecimalFormat("0.00").format(localClockOffset1 * 1000.0D) + " ms");
/*  96:    */     
/*  97:134 */     System.out.println("Local clock offset2: " + 
/*  98:135 */       new DecimalFormat("0.00").format(localClockOffset2 * 1000.0D) + " ms");
/*  99:    */   }
/* 100:    */   
/* 101:    */   static void printUsage()
/* 102:    */   {
/* 103:147 */     System.out.println(
/* 104:148 */       "NtpClient - an NTP client for Java.\n\nThis program connects to an NTP server and prints the response to the console.\n\n\nUsage: java NtpClient server\n\n\nThis program is copyright (c) Adam Buckley 2004 and distributed under the terms\nof the GNU General Public License.  This program is distributed in the hope\nthat it will be useful, but WITHOUT ANY WARRANTY; without even the implied\nwarranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\nGeneral Public License available at http://www.gnu.org/licenses/gpl.html for\nmore details.");
/* 105:    */   }
/* 106:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-services2\ken-services2.jar
 * Qualified Name:     com.kentropy.services.SntpClient
 * JD-Core Version:    0.7.0.1
 */