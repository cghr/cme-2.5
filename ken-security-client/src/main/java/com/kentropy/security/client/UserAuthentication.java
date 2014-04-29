/*   1:    */ package com.kentropy.security.client;
/*   2:    */ 
/*   3:    */ import com.kentropy.crypt.DesEncryptor;
/*   4:    */ import de.schlund.pfixxml.util.MD5Utils;
/*   5:    */ import java.io.File;
/*   6:    */ import java.io.FileOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.InputStream;
/*   9:    */ import java.io.OutputStream;
/*  10:    */ import java.io.PrintStream;
/*  11:    */ import java.net.HttpURLConnection;
/*  12:    */ import java.net.URL;
/*  13:    */ import java.net.URLConnection;
/*  14:    */ import java.util.Map;
/*  15:    */ import java.util.Properties;
/*  16:    */ import java.util.Set;
/*  17:    */ import javax.swing.JOptionPane;
/*  18:    */ 
/*  19:    */ public class UserAuthentication
/*  20:    */ {
/*  21:    */   public String url;
/*  22: 29 */   private String dest = "resources/teamdata.xml";
/*  23:    */   public String[] login;
/*  24: 31 */   public boolean isMd5Enabled = true;
/*  25: 33 */   private File file = new File(this.dest);
/*  26: 35 */   Properties props = null;
/*  27:    */   
/*  28:    */   public String[] getLogin()
/*  29:    */   {
/*  30: 72 */     return this.login;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getURL()
/*  34:    */   {
/*  35: 76 */     return this.url;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public static void main(String[] args)
/*  39:    */     throws Exception
/*  40:    */   {
/*  41: 80 */     UserAuthentication userAuthentication = new UserAuthentication();
/*  42: 81 */     userAuthentication.authenticate1("cmeuser1", "census");
/*  43:    */   }
/*  44:    */   
/*  45:    */   public boolean isFirstLogin()
/*  46:    */   {
/*  47: 86 */     System.out.println("Verifying First Login");
/*  48: 87 */     System.out.println(this.file.getAbsolutePath());
/*  49:    */     
/*  50: 89 */     return !this.file.exists();
/*  51:    */   }
/*  52:    */   
/*  53:130 */   public String sessionID = null;
/*  54:131 */   public String username = null;
/*  55:132 */   private String md5Password = null;
/*  56:133 */   private String password = null;
/*  57:    */   
/*  58:    */   public boolean authenticate1(String username, String password)
/*  59:    */     throws IOException
/*  60:    */   {
/*  61:136 */     this.username = username;
/*  62:137 */     this.password = password;
/*  63:138 */     this.md5Password = MD5Utils.hex_md5(password);
/*  64:139 */     return authenticate();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean authenticate()
/*  68:    */     throws IOException
/*  69:    */   {
/*  70:145 */     String appURL = this.url + "/Authenticate.do";
/*  71:    */     
/*  72:147 */     URL url1 = new URL(appURL + "?action=getSession");
/*  73:148 */     System.out.println("appURL" + url1);
/*  74:149 */     HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/*  75:150 */     Map map = urlConnection.getHeaderFields();
/*  76:151 */     Set<String> set = map.keySet();
/*  77:152 */     for (String key : set) {
/*  78:153 */       System.out.println("Key:" + key + " Value" + urlConnection.getHeaderField(key));
/*  79:    */     }
/*  80:155 */     this.sessionID = urlConnection.getHeaderField("Set-Cookie");
/*  81:156 */     this.username = this.username;
/*  82:    */     
/*  83:158 */     System.out.println("SessionID:" + this.sessionID);
/*  84:    */     
/*  85:160 */     String session = this.sessionID.split(";")[0].split("=")[1].trim();
/*  86:161 */     System.out.println("SessionID" + session);
/*  87:162 */     String secret = null;
/*  88:163 */     if (this.isMd5Enabled)
/*  89:    */     {
/*  90:164 */       String secretPass = this.md5Password;
/*  91:165 */       System.out.println("SecretPass" + secretPass);
/*  92:166 */       String md5Secret = MD5Utils.hex_md5(secretPass + session);
/*  93:167 */       secret = md5Secret;
/*  94:    */     }
/*  95:    */     else
/*  96:    */     {
/*  97:170 */       secret = this.password;
/*  98:    */     }
/*  99:172 */     System.out.println("secret " + secret);
/* 100:    */     
/* 101:174 */     url1 = new URL(appURL);
/* 102:175 */     System.out.println("appURL" + url1);
/* 103:176 */     urlConnection = (HttpURLConnection)url1.openConnection();
/* 104:177 */     urlConnection.setRequestProperty("Cookie", this.sessionID + ";");
/* 105:    */     
/* 106:179 */     urlConnection.setRequestMethod("POST");
/* 107:180 */     urlConnection.setDoOutput(true);
/* 108:181 */     urlConnection.getOutputStream().write(("username=" + this.username + "&password=" + secret).getBytes());
/* 109:182 */     urlConnection.getOutputStream().close();
/* 110:183 */     urlConnection.getHeaderFields();
/* 111:    */     
/* 112:185 */     System.out.println("Response:" + urlConnection.getResponseCode());
/* 113:    */     
/* 114:187 */     return urlConnection.getResponseCode() == 200;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public boolean authenticate(String username, String password)
/* 118:    */     throws IOException
/* 119:    */   {
/* 120:191 */     String appURL = this.url + "/Authenticate.do";
/* 121:192 */     this.username = username;
/* 122:193 */     this.password = password;
/* 123:194 */     this.md5Password = MD5Utils.hex_md5(password);
/* 124:195 */     URL url1 = new URL(appURL + "?action=getSession");
/* 125:196 */     System.out.println("appURL" + url1);
/* 126:197 */     HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/* 127:198 */     Map map = urlConnection.getHeaderFields();
/* 128:199 */     Set<String> set = map.keySet();
/* 129:200 */     for (String key : set) {
/* 130:201 */       System.out.println("Key:" + key + " Value" + urlConnection.getHeaderField(key));
/* 131:    */     }
/* 132:203 */     this.sessionID = urlConnection.getHeaderField("Set-Cookie");
/* 133:204 */     this.username = username;
/* 134:    */     
/* 135:206 */     System.out.println("SessionID:" + this.sessionID);
/* 136:    */     
/* 137:208 */     String session = this.sessionID.split(";")[0].split("=")[1].trim();
/* 138:209 */     System.out.println("SessionID" + session);
/* 139:210 */     String secret = null;
/* 140:211 */     if (this.isMd5Enabled)
/* 141:    */     {
/* 142:212 */       String secretPass = this.md5Password;
/* 143:213 */       System.out.println("SecretPass" + secretPass);
/* 144:214 */       String md5Secret = MD5Utils.hex_md5(secretPass + session);
/* 145:215 */       secret = md5Secret;
/* 146:    */     }
/* 147:    */     else
/* 148:    */     {
/* 149:218 */       secret = password;
/* 150:    */     }
/* 151:220 */     System.out.println("secret " + secret);
/* 152:    */     
/* 153:222 */     url1 = new URL(appURL);
/* 154:223 */     System.out.println("appURL" + url1);
/* 155:224 */     urlConnection = (HttpURLConnection)url1.openConnection();
/* 156:225 */     urlConnection.setRequestProperty("Cookie", this.sessionID + ";");
/* 157:    */     
/* 158:227 */     urlConnection.setRequestMethod("POST");
/* 159:228 */     urlConnection.setDoOutput(true);
/* 160:229 */     urlConnection.getOutputStream().write(("username=" + this.username + "&password=" + secret).getBytes());
/* 161:230 */     urlConnection.getOutputStream().close();
/* 162:231 */     urlConnection.getHeaderFields();
/* 163:    */     
/* 164:233 */     System.out.println("Response:" + urlConnection.getResponseCode());
/* 165:    */     
/* 166:235 */     return urlConnection.getResponseCode() == 200;
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setCredentials1(String username, String password)
/* 170:    */   {
/* 171:241 */     setCredentials(username, MD5Utils.hex_md5(password));
/* 172:242 */     System.out.println("Set username:" + username + " password:" + this.md5Password);
/* 173:    */   }
/* 174:    */   
/* 175:    */   public void setCredentials(String username, String md5Password)
/* 176:    */   {
/* 177:246 */     this.username = username;
/* 178:247 */     this.md5Password = md5Password;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public boolean authenticate1()
/* 182:    */     throws IOException
/* 183:    */   {
/* 184:251 */     if ((this.username == null) || (this.password == null))
/* 185:    */     {
/* 186:252 */       System.out.println("ERROR: Username and password are not present. Use authenticate1(username,password) for the first time.");
/* 187:253 */       return false;
/* 188:    */     }
/* 189:255 */     System.out.println("ReAuthenticating");
/* 190:256 */     return authenticate();
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void getTeamData1()
/* 194:    */     throws IOException
/* 195:    */   {
/* 196:261 */     URL url1 = new URL(this.url + "/Transfer.do?action=getTeamData");
/* 197:262 */     URLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/* 198:263 */     urlConnection.setRequestProperty("Cookie", this.sessionID);
/* 199:264 */     InputStream is = urlConnection.getInputStream();
/* 200:    */     
/* 201:266 */     System.out.println("file " + this.file.getAbsolutePath());
/* 202:267 */     FileOutputStream fos = new FileOutputStream(this.file);
/* 203:268 */     byte[] b = new byte[1024];
/* 204:269 */     int n = is.read(b);
/* 205:270 */     while (n > 0)
/* 206:    */     {
/* 207:271 */       fos.write(b, 0, n);
/* 208:272 */       n = is.read(b);
/* 209:    */     }
/* 210:274 */     is.close();
/* 211:275 */     fos.close();
/* 212:276 */     System.out.println("Completed downloading");
/* 213:    */   }
/* 214:    */   
/* 215:    */   public void getTeamData()
/* 216:    */     throws IOException
/* 217:    */   {
/* 218:280 */     URL url1 = new URL(this.url + "/Transfer.do?action=getTeamData");
/* 219:281 */     URLConnection urlConnection = (HttpURLConnection)url1.openConnection();
/* 220:282 */     urlConnection.setRequestProperty("Cookie", this.sessionID);
/* 221:283 */     InputStream is = urlConnection.getInputStream();
/* 222:284 */     FileOutputStream fos = new FileOutputStream(this.file);
/* 223:    */     try
/* 224:    */     {
/* 225:286 */       DesEncryptor encryptor = new DesEncryptor();
/* 226:287 */       encryptor.encrypt(is, fos);
/* 227:    */     }
/* 228:    */     catch (ClassNotFoundException e)
/* 229:    */     {
/* 230:289 */       e.printStackTrace();
/* 231:    */     }
/* 232:291 */     System.out.println("Completed downloading");
/* 233:    */   }
/* 234:    */   
/* 235:    */   static void userDialog(String message)
/* 236:    */   {
/* 237:300 */     JOptionPane.showMessageDialog(null, message, "alert", 
/* 238:301 */       2);
/* 239:    */   }
/* 240:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-security-client\ken-security-client.jar
 * Qualified Name:     com.kentropy.security.client.UserAuthentication
 * JD-Core Version:    0.7.0.1
 */