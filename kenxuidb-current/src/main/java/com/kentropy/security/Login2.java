/*   1:    */ package com.kentropy.security;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import de.schlund.pfixxml.util.MD5Utils;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.Map;
/*   9:    */ import java.util.Properties;
/*  10:    */ import java.util.Set;
/*  11:    */ import javax.security.auth.Subject;
/*  12:    */ import javax.security.auth.callback.Callback;
/*  13:    */ import javax.security.auth.callback.CallbackHandler;
/*  14:    */ import javax.security.auth.callback.NameCallback;
/*  15:    */ import javax.security.auth.callback.PasswordCallback;
/*  16:    */ import javax.security.auth.callback.UnsupportedCallbackException;
/*  17:    */ import javax.security.auth.login.FailedLoginException;
/*  18:    */ import javax.security.auth.login.LoginException;
/*  19:    */ import javax.security.auth.spi.LoginModule;
/*  20:    */ 
/*  21:    */ public class Login2
/*  22:    */   implements LoginModule
/*  23:    */ {
/*  24:    */   private String username;
/*  25:    */   private String password;
/*  26:    */   private Subject subject;
/*  27:    */   private KenPrincipal p;
/*  28:    */   private String session;
/*  29:    */   private CallbackHandler callbackHandler;
/*  30:    */   private boolean success;
/*  31:    */   private String teamId;
/*  32:    */   private String name;
/*  33:    */   private boolean isMd5Enabled;
/*  34:    */   private Map<String, ?> sharedState;
/*  35:    */   private String id;
/*  36:    */   
/*  37:    */   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
/*  38:    */   {
/*  39: 39 */     this.subject = subject;
/*  40: 40 */     this.sharedState = sharedState;
/*  41: 41 */     this.callbackHandler = callbackHandler;
/*  42: 42 */     Properties props = (Properties)TestXUIDB.getInstance().getBean("client-authentication");
/*  43:    */     
/*  44: 44 */     System.out.println("Props:" + props);
/*  45:    */     
/*  46: 46 */     String md5Enabled = props.getProperty("md5");
/*  47: 47 */     if (md5Enabled.equals("enable")) {
/*  48: 48 */       this.isMd5Enabled = true;
/*  49:    */     } else {
/*  50: 50 */       this.isMd5Enabled = false;
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean login()
/*  55:    */     throws LoginException
/*  56:    */   {
/*  57: 56 */     System.out.println(" login2 ");
/*  58: 57 */     if ((this.subject.getPrincipals().isEmpty()) || (!this.subject.getPrincipals().contains(new KenPrincipal())))
/*  59:    */     {
/*  60: 59 */       this.p = ((KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal"));
/*  61:    */     }
/*  62:    */     else
/*  63:    */     {
/*  64: 63 */       this.p = ((KenPrincipal)this.subject.getPrincipals().iterator().next());
/*  65: 64 */       this.username = this.p.getUser();
/*  66: 65 */       this.password = this.p.getPassword();
/*  67: 66 */       if (this.username == null) {
/*  68: 67 */         return false;
/*  69:    */       }
/*  70:    */     }
/*  71: 72 */     if (this.username == null)
/*  72:    */     {
/*  73: 74 */       Callback[] callbacks = new Callback[2];
/*  74: 75 */       callbacks[0] = new NameCallback("Username");
/*  75: 76 */       callbacks[1] = new PasswordCallback("Password", false);
/*  76:    */       try
/*  77:    */       {
/*  78: 79 */         this.callbackHandler.handle(callbacks);
/*  79: 80 */         this.username = ((NameCallback)callbacks[0]).getName();
/*  80: 81 */         char[] passwordCharArray = ((PasswordCallback)callbacks[1]).getPassword();
/*  81: 82 */         this.password = new String(passwordCharArray);
/*  82:    */       }
/*  83:    */       catch (IOException e)
/*  84:    */       {
/*  85: 86 */         e.printStackTrace();
/*  86:    */       }
/*  87:    */       catch (UnsupportedCallbackException e)
/*  88:    */       {
/*  89: 89 */         e.printStackTrace();
/*  90:    */       }
/*  91:    */     }
/*  92:    */     try
/*  93:    */     {
/*  94: 95 */       if (authenticate())
/*  95:    */       {
/*  96: 97 */         System.out.println(" Authenticated");
/*  97: 98 */         return true;
/*  98:    */       }
/*  99:101 */       throw new FailedLoginException("online failed");
/* 100:    */     }
/* 101:    */     catch (Exception e)
/* 102:    */     {
/* 103:105 */       e.printStackTrace();
/* 104:    */     }
/* 105:106 */     return false;
/* 106:    */   }
/* 107:    */   
/* 108:    */   private boolean authenticate()
/* 109:    */   {
/* 110:111 */     String u1 = TestXUIDB.getInstance().getProperty("username");
/* 111:112 */     String p1 = TestXUIDB.getInstance().getProperty("password");
/* 112:113 */     if (u1 == null) {
/* 113:114 */       return false;
/* 114:    */     }
/* 115:117 */     String pass = this.password;
/* 116:118 */     if ((pass != null) && (this.isMd5Enabled)) {
/* 117:119 */       pass = MD5Utils.hex_md5(this.password);
/* 118:    */     }
/* 119:122 */     if ((u1.equals(this.username)) && ((p1 == pass) || (p1.equals(pass))))
/* 120:    */     {
/* 121:124 */       this.teamId = TestXUIDB.getInstance().getProperty("teamId");
/* 122:125 */       this.name = TestXUIDB.getInstance().getProperty("fullname");
/* 123:126 */       this.id = TestXUIDB.getInstance().getProperty("id");
/* 124:127 */       this.success = true;
/* 125:128 */       return true;
/* 126:    */     }
/* 127:131 */     return false;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public boolean commit()
/* 131:    */     throws LoginException
/* 132:    */   {
/* 133:137 */     System.out.println(" commit2 ");
/* 134:138 */     if (this.success)
/* 135:    */     {
/* 136:140 */       this.p.setName(this.name);
/* 137:141 */       this.p.setUser(this.username);
/* 138:142 */       this.p.setPassword(this.password);
/* 139:143 */       this.p.setTeamId(this.teamId);
/* 140:144 */       this.p.setId(this.id);
/* 141:145 */       this.username = null;
/* 142:146 */       this.password = null;
/* 143:147 */       this.name = null;
/* 144:148 */       this.teamId = null;
/* 145:149 */       return true;
/* 146:    */     }
/* 147:152 */     this.username = null;
/* 148:153 */     this.password = null;
/* 149:154 */     this.name = null;
/* 150:155 */     this.teamId = null;
/* 151:156 */     this.id = null;
/* 152:    */     
/* 153:158 */     return false;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public boolean abort()
/* 157:    */     throws LoginException
/* 158:    */   {
/* 159:163 */     System.out.println(" abort2 ");
/* 160:164 */     return false;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public boolean logout()
/* 164:    */     throws LoginException
/* 165:    */   {
/* 166:169 */     System.out.println(" logout2 ");
/* 167:170 */     return false;
/* 168:    */   }
/* 169:    */   
/* 170:    */   public static void main(String[] args) {}
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.Login2
 * JD-Core Version:    0.7.0.1
 */