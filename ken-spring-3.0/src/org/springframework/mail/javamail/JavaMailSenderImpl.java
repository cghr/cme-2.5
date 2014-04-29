/*   1:    */ package org.springframework.mail.javamail;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Date;
/*   7:    */ import java.util.LinkedHashMap;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Map;
/*  10:    */ import java.util.Properties;
/*  11:    */ import javax.activation.FileTypeMap;
/*  12:    */ import javax.mail.AuthenticationFailedException;
/*  13:    */ import javax.mail.MessagingException;
/*  14:    */ import javax.mail.NoSuchProviderException;
/*  15:    */ import javax.mail.Session;
/*  16:    */ import javax.mail.Transport;
/*  17:    */ import javax.mail.internet.MimeMessage;
/*  18:    */ import org.springframework.mail.MailAuthenticationException;
/*  19:    */ import org.springframework.mail.MailException;
/*  20:    */ import org.springframework.mail.MailParseException;
/*  21:    */ import org.springframework.mail.MailPreparationException;
/*  22:    */ import org.springframework.mail.MailSendException;
/*  23:    */ import org.springframework.mail.SimpleMailMessage;
/*  24:    */ import org.springframework.util.Assert;
/*  25:    */ 
/*  26:    */ public class JavaMailSenderImpl
/*  27:    */   implements JavaMailSender
/*  28:    */ {
/*  29:    */   public static final String DEFAULT_PROTOCOL = "smtp";
/*  30:    */   public static final int DEFAULT_PORT = -1;
/*  31:    */   private static final String HEADER_MESSAGE_ID = "Message-ID";
/*  32: 80 */   private Properties javaMailProperties = new Properties();
/*  33:    */   private Session session;
/*  34:    */   private String protocol;
/*  35:    */   private String host;
/*  36: 88 */   private int port = -1;
/*  37:    */   private String username;
/*  38:    */   private String password;
/*  39:    */   private String defaultEncoding;
/*  40:    */   private FileTypeMap defaultFileTypeMap;
/*  41:    */   
/*  42:    */   public JavaMailSenderImpl()
/*  43:    */   {
/*  44:105 */     ConfigurableMimeFileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
/*  45:106 */     fileTypeMap.afterPropertiesSet();
/*  46:107 */     this.defaultFileTypeMap = fileTypeMap;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setJavaMailProperties(Properties javaMailProperties)
/*  50:    */   {
/*  51:119 */     this.javaMailProperties = javaMailProperties;
/*  52:120 */     synchronized (this)
/*  53:    */     {
/*  54:121 */       this.session = null;
/*  55:    */     }
/*  56:    */   }
/*  57:    */   
/*  58:    */   public Properties getJavaMailProperties()
/*  59:    */   {
/*  60:132 */     return this.javaMailProperties;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public synchronized void setSession(Session session)
/*  64:    */   {
/*  65:144 */     Assert.notNull(session, "Session must not be null");
/*  66:145 */     this.session = session;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public synchronized Session getSession()
/*  70:    */   {
/*  71:153 */     if (this.session == null) {
/*  72:154 */       this.session = Session.getInstance(this.javaMailProperties);
/*  73:    */     }
/*  74:156 */     return this.session;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void setProtocol(String protocol)
/*  78:    */   {
/*  79:163 */     this.protocol = protocol;
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getProtocol()
/*  83:    */   {
/*  84:170 */     return this.protocol;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setHost(String host)
/*  88:    */   {
/*  89:178 */     this.host = host;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String getHost()
/*  93:    */   {
/*  94:185 */     return this.host;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void setPort(int port)
/*  98:    */   {
/*  99:194 */     this.port = port;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public int getPort()
/* 103:    */   {
/* 104:201 */     return this.port;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public void setUsername(String username)
/* 108:    */   {
/* 109:216 */     this.username = username;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public String getUsername()
/* 113:    */   {
/* 114:223 */     return this.username;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void setPassword(String password)
/* 118:    */   {
/* 119:238 */     this.password = password;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public String getPassword()
/* 123:    */   {
/* 124:245 */     return this.password;
/* 125:    */   }
/* 126:    */   
/* 127:    */   public void setDefaultEncoding(String defaultEncoding)
/* 128:    */   {
/* 129:254 */     this.defaultEncoding = defaultEncoding;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public String getDefaultEncoding()
/* 133:    */   {
/* 134:262 */     return this.defaultEncoding;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public void setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap)
/* 138:    */   {
/* 139:279 */     this.defaultFileTypeMap = defaultFileTypeMap;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public FileTypeMap getDefaultFileTypeMap()
/* 143:    */   {
/* 144:287 */     return this.defaultFileTypeMap;
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void send(SimpleMailMessage simpleMessage)
/* 148:    */     throws MailException
/* 149:    */   {
/* 150:296 */     send(new SimpleMailMessage[] { simpleMessage });
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void send(SimpleMailMessage[] simpleMessages)
/* 154:    */     throws MailException
/* 155:    */   {
/* 156:300 */     List<MimeMessage> mimeMessages = new ArrayList(simpleMessages.length);
/* 157:301 */     for (SimpleMailMessage simpleMessage : simpleMessages)
/* 158:    */     {
/* 159:302 */       MimeMailMessage message = new MimeMailMessage(createMimeMessage());
/* 160:303 */       simpleMessage.copyTo(message);
/* 161:304 */       mimeMessages.add(message.getMimeMessage());
/* 162:    */     }
/* 163:306 */     doSend((MimeMessage[])mimeMessages.toArray(new MimeMessage[mimeMessages.size()]), simpleMessages);
/* 164:    */   }
/* 165:    */   
/* 166:    */   public MimeMessage createMimeMessage()
/* 167:    */   {
/* 168:323 */     return new SmartMimeMessage(getSession(), getDefaultEncoding(), getDefaultFileTypeMap());
/* 169:    */   }
/* 170:    */   
/* 171:    */   public MimeMessage createMimeMessage(InputStream contentStream)
/* 172:    */     throws MailException
/* 173:    */   {
/* 174:    */     try
/* 175:    */     {
/* 176:328 */       return new MimeMessage(getSession(), contentStream);
/* 177:    */     }
/* 178:    */     catch (MessagingException ex)
/* 179:    */     {
/* 180:331 */       throw new MailParseException("Could not parse raw MIME content", ex);
/* 181:    */     }
/* 182:    */   }
/* 183:    */   
/* 184:    */   public void send(MimeMessage mimeMessage)
/* 185:    */     throws MailException
/* 186:    */   {
/* 187:336 */     send(new MimeMessage[] { mimeMessage });
/* 188:    */   }
/* 189:    */   
/* 190:    */   public void send(MimeMessage[] mimeMessages)
/* 191:    */     throws MailException
/* 192:    */   {
/* 193:340 */     doSend(mimeMessages, null);
/* 194:    */   }
/* 195:    */   
/* 196:    */   public void send(MimeMessagePreparator mimeMessagePreparator)
/* 197:    */     throws MailException
/* 198:    */   {
/* 199:344 */     send(new MimeMessagePreparator[] { mimeMessagePreparator });
/* 200:    */   }
/* 201:    */   
/* 202:    */   public void send(MimeMessagePreparator[] mimeMessagePreparators)
/* 203:    */     throws MailException
/* 204:    */   {
/* 205:    */     try
/* 206:    */     {
/* 207:349 */       List<MimeMessage> mimeMessages = new ArrayList(mimeMessagePreparators.length);
/* 208:350 */       for (MimeMessagePreparator preparator : mimeMessagePreparators)
/* 209:    */       {
/* 210:351 */         MimeMessage mimeMessage = createMimeMessage();
/* 211:352 */         preparator.prepare(mimeMessage);
/* 212:353 */         mimeMessages.add(mimeMessage);
/* 213:    */       }
/* 214:355 */       send((MimeMessage[])mimeMessages.toArray(new MimeMessage[mimeMessages.size()]));
/* 215:    */     }
/* 216:    */     catch (MailException ex)
/* 217:    */     {
/* 218:358 */       throw ex;
/* 219:    */     }
/* 220:    */     catch (MessagingException ex)
/* 221:    */     {
/* 222:361 */       throw new MailParseException(ex);
/* 223:    */     }
/* 224:    */     catch (IOException ex)
/* 225:    */     {
/* 226:364 */       throw new MailPreparationException(ex);
/* 227:    */     }
/* 228:    */     catch (Exception ex)
/* 229:    */     {
/* 230:367 */       throw new MailPreparationException(ex);
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages)
/* 235:    */     throws MailException
/* 236:    */   {
/* 237:384 */     Map<Object, Exception> failedMessages = new LinkedHashMap();
/* 238:    */     try
/* 239:    */     {
/* 240:388 */       Transport transport = getTransport(getSession());
/* 241:389 */       transport.connect(getHost(), getPort(), getUsername(), getPassword());
/* 242:    */     }
/* 243:    */     catch (AuthenticationFailedException ex)
/* 244:    */     {
/* 245:392 */       throw new MailAuthenticationException(ex);
/* 246:    */     }
/* 247:    */     catch (MessagingException ex)
/* 248:    */     {
/* 249:396 */       for (int i = 0; i < mimeMessages.length; i++)
/* 250:    */       {
/* 251:397 */         Object original = originalMessages != null ? originalMessages[i] : mimeMessages[i];
/* 252:398 */         failedMessages.put(original, ex);
/* 253:    */       }
/* 254:400 */       throw new MailSendException("Mail server connection failed", ex, failedMessages);
/* 255:    */     }
/* 256:    */     Transport transport;
/* 257:    */     try
/* 258:    */     {
/* 259:404 */       for (int i = 0; i < mimeMessages.length; i++)
/* 260:    */       {
/* 261:405 */         MimeMessage mimeMessage = mimeMessages[i];
/* 262:    */         try
/* 263:    */         {
/* 264:407 */           if (mimeMessage.getSentDate() == null) {
/* 265:408 */             mimeMessage.setSentDate(new Date());
/* 266:    */           }
/* 267:410 */           String messageId = mimeMessage.getMessageID();
/* 268:411 */           mimeMessage.saveChanges();
/* 269:412 */           if (messageId != null) {
/* 270:414 */             mimeMessage.setHeader("Message-ID", messageId);
/* 271:    */           }
/* 272:416 */           transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
/* 273:    */         }
/* 274:    */         catch (MessagingException ex)
/* 275:    */         {
/* 276:419 */           Object original = originalMessages != null ? originalMessages[i] : mimeMessage;
/* 277:420 */           failedMessages.put(original, ex);
/* 278:    */         }
/* 279:    */       }
/* 280:    */     }
/* 281:    */     finally
/* 282:    */     {
/* 283:    */       try
/* 284:    */       {
/* 285:426 */         transport.close();
/* 286:    */       }
/* 287:    */       catch (MessagingException ex)
/* 288:    */       {
/* 289:429 */         if (!failedMessages.isEmpty()) {
/* 290:430 */           throw new MailSendException("Failed to close server connection after message failures", ex, 
/* 291:431 */             failedMessages);
/* 292:    */         }
/* 293:434 */         throw new MailSendException("Failed to close server connection after message sending", ex);
/* 294:    */       }
/* 295:    */     }
/* 296:439 */     if (!failedMessages.isEmpty()) {
/* 297:440 */       throw new MailSendException(failedMessages);
/* 298:    */     }
/* 299:    */   }
/* 300:    */   
/* 301:    */   protected Transport getTransport(Session session)
/* 302:    */     throws NoSuchProviderException
/* 303:    */   {
/* 304:452 */     String protocol = getProtocol();
/* 305:453 */     if (protocol == null)
/* 306:    */     {
/* 307:454 */       protocol = session.getProperty("mail.transport.protocol");
/* 308:455 */       if (protocol == null) {
/* 309:456 */         protocol = "smtp";
/* 310:    */       }
/* 311:    */     }
/* 312:459 */     return session.getTransport(protocol);
/* 313:    */   }
/* 314:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.JavaMailSenderImpl
 * JD-Core Version:    0.7.0.1
 */