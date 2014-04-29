/*   1:    */ package com.kentropy.security;
/*   2:    */ 
/*   3:    */ import com.kentropy.db.TestXUIDB;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Properties;
/*   9:    */ import java.util.Set;
/*  10:    */ import javax.security.auth.Subject;
/*  11:    */ import javax.security.auth.callback.Callback;
/*  12:    */ import javax.security.auth.callback.CallbackHandler;
/*  13:    */ import javax.security.auth.callback.NameCallback;
/*  14:    */ import javax.security.auth.callback.PasswordCallback;
/*  15:    */ import javax.security.auth.callback.UnsupportedCallbackException;
/*  16:    */ import javax.security.auth.login.FailedLoginException;
/*  17:    */ import javax.security.auth.login.LoginException;
/*  18:    */ import javax.security.auth.spi.LoginModule;
/*  19:    */ 
/*  20:    */ public class Login1
/*  21:    */   implements LoginModule
/*  22:    */ {
/*  23: 28 */   Subject subject = null;
/*  24: 29 */   Map<String, ?> sharedState = null;
/*  25: 30 */   CallbackHandler callbackHandler = null;
/*  26: 32 */   String username = null;
/*  27: 33 */   String password = null;
/*  28: 34 */   String session = null;
/*  29:    */   String name;
/*  30:    */   String teamId;
/*  31: 37 */   KenPrincipal p = null;
/*  32:    */   public String url;
/*  33: 39 */   public boolean isMd5Enabled = true;
/*  34:    */   private String id;
/*  35:    */   
/*  36:    */   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
/*  37:    */   {
/*  38: 44 */     this.subject = subject;
/*  39: 45 */     this.sharedState = sharedState;
/*  40: 46 */     this.callbackHandler = callbackHandler;
/*  41:    */     
/*  42: 48 */     Properties props = (Properties)TestXUIDB.getInstance().getBean("client-authentication");
/*  43:    */     
/*  44: 50 */     System.out.println("Props:" + props);
/*  45:    */     
/*  46: 52 */     this.url = props.getProperty("URL");
/*  47:    */     
/*  48: 54 */     String md5Enabled = props.getProperty("md5");
/*  49: 55 */     if (md5Enabled.equals("enable")) {
/*  50: 56 */       this.isMd5Enabled = true;
/*  51:    */     } else {
/*  52: 58 */       this.isMd5Enabled = false;
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public boolean authenticate()
/*  57:    */     throws Exception
/*  58:    */   {
/*  59: 71 */     throw new Error("Unresolved compilation problem: \n\tType mismatch: cannot convert from element type Object to String\n");
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean login()
/*  63:    */     throws LoginException
/*  64:    */   {
/*  65:136 */     System.out.println(" login1 ");
/*  66:137 */     if ((this.subject.getPrincipals().isEmpty()) || (!this.subject.getPrincipals().contains(new KenPrincipal())))
/*  67:    */     {
/*  68:139 */       this.p = ((KenPrincipal)TestXUIDB.getInstance().getBean("ken-principal"));
/*  69:    */     }
/*  70:    */     else
/*  71:    */     {
/*  72:143 */       this.p = ((KenPrincipal)this.subject.getPrincipals().iterator().next());
/*  73:144 */       this.username = this.p.getUser();
/*  74:145 */       this.password = this.p.getPassword();
/*  75:146 */       this.session = this.p.getSessionID();
/*  76:    */     }
/*  77:149 */     if (this.username == null)
/*  78:    */     {
/*  79:151 */       Callback[] callbacks = new Callback[2];
/*  80:152 */       callbacks[0] = new NameCallback("Username");
/*  81:153 */       callbacks[1] = new PasswordCallback("Password", false);
/*  82:    */       try
/*  83:    */       {
/*  84:156 */         this.callbackHandler.handle(callbacks);
/*  85:157 */         this.username = ((NameCallback)callbacks[0]).getName();
/*  86:158 */         char[] passwordCharArray = ((PasswordCallback)callbacks[1]).getPassword();
/*  87:159 */         this.password = new String(passwordCharArray);
/*  88:    */       }
/*  89:    */       catch (IOException e)
/*  90:    */       {
/*  91:163 */         e.printStackTrace();
/*  92:    */       }
/*  93:    */       catch (UnsupportedCallbackException e)
/*  94:    */       {
/*  95:166 */         e.printStackTrace();
/*  96:    */       }
/*  97:    */     }
/*  98:    */     try
/*  99:    */     {
/* 100:172 */       if (authenticate())
/* 101:    */       {
/* 102:174 */         System.out.println(" Authenticated");
/* 103:175 */         return true;
/* 104:    */       }
/* 105:178 */       this.session = null;
/* 106:179 */       this.p.setUser(null);
/* 107:180 */       this.p.setPassword(null);
/* 108:    */       
/* 109:182 */       throw new FailedLoginException("online failed");
/* 110:    */     }
/* 111:    */     catch (Exception e)
/* 112:    */     {
/* 113:186 */       e.printStackTrace();
/* 114:    */     }
/* 115:187 */     return false;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public boolean commit()
/* 119:    */     throws LoginException
/* 120:    */   {
/* 121:193 */     if (this.session != null)
/* 122:    */     {
/* 123:195 */       this.p.setSessionID(this.session);
/* 124:196 */       System.out.println(" commit1 " + this.session + " " + this.teamId);
/* 125:197 */       this.p.setUser(this.username);
/* 126:198 */       this.p.setPassword(this.password);
/* 127:199 */       this.p.setTeamId(this.teamId);
/* 128:200 */       this.p.setName(this.name);
/* 129:201 */       this.p.setId(this.id);
/* 130:202 */       this.subject.getPrincipals().remove(this.p);
/* 131:203 */       this.subject.getPrincipals().add(this.p);
/* 132:    */       
/* 133:205 */       this.username = null;
/* 134:206 */       this.password = null;
/* 135:207 */       this.session = null;
/* 136:208 */       this.name = null;
/* 137:209 */       this.teamId = null;
/* 138:210 */       this.p = null;
/* 139:211 */       this.id = null;
/* 140:212 */       return true;
/* 141:    */     }
/* 142:215 */     this.username = null;
/* 143:216 */     this.password = null;
/* 144:217 */     this.session = null;
/* 145:218 */     this.name = null;
/* 146:219 */     this.teamId = null;
/* 147:220 */     this.p = null;
/* 148:221 */     this.id = null;
/* 149:    */     
/* 150:223 */     return false;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public boolean abort()
/* 154:    */     throws LoginException
/* 155:    */   {
/* 156:229 */     System.out.println(" abort1 ");
/* 157:230 */     return true;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public boolean logout()
/* 161:    */     throws LoginException
/* 162:    */   {
/* 163:235 */     return false;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static void main(String[] args) {}
/* 167:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.security.Login1
 * JD-Core Version:    0.7.0.1
 */