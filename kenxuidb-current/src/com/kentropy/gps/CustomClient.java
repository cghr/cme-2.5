/*  1:   */ package com.kentropy.gps;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import ocss.nmea.api.NMEAClient;
/*  5:   */ import ocss.nmea.api.NMEAEvent;
/*  6:   */ 
/*  7:   */ public class CustomClient
/*  8:   */   extends NMEAClient
/*  9:   */ {
/* 10:   */   public CustomClient(String s, String[] sa)
/* 11:   */   {
/* 12:11 */     super(s, sa);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void dataDetectedEvent(NMEAEvent e)
/* 16:   */   {
/* 17:16 */     System.out.println("Received:" + e.getContent());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static void main(String[] args)
/* 21:   */   {
/* 22:21 */     String prefix = "II";
/* 23:22 */     String[] array = { "HDM", "GLL", "XTE", "MWV", "VHW" };
/* 24:23 */     CustomClient customClient = new CustomClient(prefix, array);
/* 25:24 */     customClient.initClient();
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.gps.CustomClient
 * JD-Core Version:    0.7.0.1
 */