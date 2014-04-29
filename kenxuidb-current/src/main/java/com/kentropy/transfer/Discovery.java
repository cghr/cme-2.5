/*   1:    */ package com.kentropy.transfer;
/*   2:    */ 
/*   3:    */ import java.awt.Frame;
/*   4:    */ import java.awt.TextArea;
/*   5:    */ import java.io.FileInputStream;
/*   6:    */ import java.io.FileNotFoundException;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.net.Socket;
/*  10:    */ import java.net.UnknownHostException;
/*  11:    */ import java.util.Enumeration;
/*  12:    */ import java.util.Observable;
/*  13:    */ import java.util.Observer;
/*  14:    */ import java.util.Properties;
/*  15:    */ import java.util.Vector;
/*  16:    */ 
/*  17:    */ public class Discovery
/*  18:    */   implements Runnable
/*  19:    */ {
/*  20: 20 */   public static Vector network = new Vector();
/*  21: 21 */   public static Vector alive = new Vector();
/*  22: 23 */   public static data1 test = null;
/*  23: 25 */   public static TextArea lbl = null;
/*  24:    */   
/*  25:    */   static
/*  26:    */   {
/*  27: 29 */     Properties p = new Properties();
/*  28:    */     try
/*  29:    */     {
/*  30: 31 */       p.load(new FileInputStream("resources/network.properties"));
/*  31: 32 */       Enumeration keys = p.keys();
/*  32: 33 */       String tt = null;
/*  33: 35 */       while (keys.hasMoreElements())
/*  34:    */       {
/*  35: 36 */         String key = keys.nextElement().toString();
/*  36: 37 */         network.add(key + "," + p.getProperty(key));
/*  37:    */       }
/*  38:    */     }
/*  39:    */     catch (FileNotFoundException e)
/*  40:    */     {
/*  41: 42 */       e.printStackTrace();
/*  42:    */     }
/*  43:    */     catch (IOException e)
/*  44:    */     {
/*  45: 45 */       e.printStackTrace();
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean findPeer(String host, int port)
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 53 */       Socket sock = new Socket(host, port);
/*  54: 54 */       sock.close();
/*  55:    */       
/*  56: 56 */       return true;
/*  57:    */     }
/*  58:    */     catch (UnknownHostException e)
/*  59:    */     {
/*  60: 60 */       e.printStackTrace();
/*  61: 61 */       return false;
/*  62:    */     }
/*  63:    */     catch (IOException e)
/*  64:    */     {
/*  65: 64 */       e.printStackTrace();
/*  66:    */     }
/*  67: 65 */     return false;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void checkNetwork()
/*  71:    */   {
/*  72: 70 */     alive = new Vector();
/*  73: 71 */     for (int i = 0; i < network.size(); i++)
/*  74:    */     {
/*  75: 73 */       String val = network.get(i).toString();
/*  76: 74 */       String[] params = val.split(",");
/*  77: 75 */       if (findPeer(params[0], Integer.parseInt(params[1])))
/*  78:    */       {
/*  79: 77 */         alive.add(network.get(i).toString());
/*  80: 78 */         test.setMsg(network.get(i).toString() + " alive");
/*  81:    */         
/*  82: 80 */         System.out.println(network.get(i).toString() + " alive");
/*  83:    */       }
/*  84:    */       else
/*  85:    */       {
/*  86: 83 */         test.setMsg(network.get(i).toString() + " not alive");
/*  87:    */       }
/*  88:    */     }
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void run()
/*  92:    */   {
/*  93:    */     for (;;)
/*  94:    */     {
/*  95: 92 */       checkNetwork();
/*  96:    */       try
/*  97:    */       {
/*  98: 94 */         Thread.currentThread();Thread.yield();
/*  99: 95 */         Thread.currentThread();Thread.sleep(5000L);
/* 100:    */       }
/* 101:    */       catch (InterruptedException e)
/* 102:    */       {
/* 103: 98 */         e.printStackTrace();
/* 104:    */       }
/* 105:    */     }
/* 106:    */   }
/* 107:    */   
/* 108:    */   public Discovery(Observer o)
/* 109:    */   {
/* 110:105 */     test = new data1();
/* 111:106 */     test.addObserver(o);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public static void main(String[] args)
/* 115:    */   {
/* 116:110 */     Frame frm = new Frame("tet");
/* 117:111 */     frm.setBounds(10, 10, 800, 600);
/* 118:112 */     lbl = new TextArea();
/* 119:113 */     lbl.setBounds(10, 10, 400, 100);
/* 120:114 */     lbl.setText("TEST");
/* 121:115 */     frm.add(lbl);
/* 122:116 */     frm.show();
/* 123:    */   }
/* 124:    */   
/* 125:    */   public static class data1
/* 126:    */     extends Observable
/* 127:    */   {
/* 128:121 */     String msg = null;
/* 129:    */     
/* 130:    */     public void setMsg(String msg)
/* 131:    */     {
/* 132:124 */       System.out.println("Notify called" + countObservers());this.msg = msg;
/* 133:    */       
/* 134:126 */       setChanged();
/* 135:    */       
/* 136:128 */       notifyObservers(msg);
/* 137:    */     }
/* 138:    */   }
/* 139:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.transfer.Discovery
 * JD-Core Version:    0.7.0.1
 */