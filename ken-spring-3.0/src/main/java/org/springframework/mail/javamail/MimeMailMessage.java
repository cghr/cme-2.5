/*   1:    */ package org.springframework.mail.javamail;
/*   2:    */ 
/*   3:    */ import java.util.Date;
/*   4:    */ import javax.mail.MessagingException;
/*   5:    */ import javax.mail.internet.MimeMessage;
/*   6:    */ import org.springframework.mail.MailMessage;
/*   7:    */ import org.springframework.mail.MailParseException;
/*   8:    */ 
/*   9:    */ public class MimeMailMessage
/*  10:    */   implements MailMessage
/*  11:    */ {
/*  12:    */   private final MimeMessageHelper helper;
/*  13:    */   
/*  14:    */   public MimeMailMessage(MimeMessageHelper mimeMessageHelper)
/*  15:    */   {
/*  16: 50 */     this.helper = mimeMessageHelper;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public MimeMailMessage(MimeMessage mimeMessage)
/*  20:    */   {
/*  21: 58 */     this.helper = new MimeMessageHelper(mimeMessage);
/*  22:    */   }
/*  23:    */   
/*  24:    */   public final MimeMessageHelper getMimeMessageHelper()
/*  25:    */   {
/*  26: 65 */     return this.helper;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public final MimeMessage getMimeMessage()
/*  30:    */   {
/*  31: 72 */     return this.helper.getMimeMessage();
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setFrom(String from)
/*  35:    */     throws MailParseException
/*  36:    */   {
/*  37:    */     try
/*  38:    */     {
/*  39: 78 */       this.helper.setFrom(from);
/*  40:    */     }
/*  41:    */     catch (MessagingException ex)
/*  42:    */     {
/*  43: 81 */       throw new MailParseException(ex);
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void setReplyTo(String replyTo)
/*  48:    */     throws MailParseException
/*  49:    */   {
/*  50:    */     try
/*  51:    */     {
/*  52: 87 */       this.helper.setReplyTo(replyTo);
/*  53:    */     }
/*  54:    */     catch (MessagingException ex)
/*  55:    */     {
/*  56: 90 */       throw new MailParseException(ex);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setTo(String to)
/*  61:    */     throws MailParseException
/*  62:    */   {
/*  63:    */     try
/*  64:    */     {
/*  65: 96 */       this.helper.setTo(to);
/*  66:    */     }
/*  67:    */     catch (MessagingException ex)
/*  68:    */     {
/*  69: 99 */       throw new MailParseException(ex);
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void setTo(String[] to)
/*  74:    */     throws MailParseException
/*  75:    */   {
/*  76:    */     try
/*  77:    */     {
/*  78:105 */       this.helper.setTo(to);
/*  79:    */     }
/*  80:    */     catch (MessagingException ex)
/*  81:    */     {
/*  82:108 */       throw new MailParseException(ex);
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setCc(String cc)
/*  87:    */     throws MailParseException
/*  88:    */   {
/*  89:    */     try
/*  90:    */     {
/*  91:114 */       this.helper.setCc(cc);
/*  92:    */     }
/*  93:    */     catch (MessagingException ex)
/*  94:    */     {
/*  95:117 */       throw new MailParseException(ex);
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setCc(String[] cc)
/* 100:    */     throws MailParseException
/* 101:    */   {
/* 102:    */     try
/* 103:    */     {
/* 104:123 */       this.helper.setCc(cc);
/* 105:    */     }
/* 106:    */     catch (MessagingException ex)
/* 107:    */     {
/* 108:126 */       throw new MailParseException(ex);
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void setBcc(String bcc)
/* 113:    */     throws MailParseException
/* 114:    */   {
/* 115:    */     try
/* 116:    */     {
/* 117:132 */       this.helper.setBcc(bcc);
/* 118:    */     }
/* 119:    */     catch (MessagingException ex)
/* 120:    */     {
/* 121:135 */       throw new MailParseException(ex);
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public void setBcc(String[] bcc)
/* 126:    */     throws MailParseException
/* 127:    */   {
/* 128:    */     try
/* 129:    */     {
/* 130:141 */       this.helper.setBcc(bcc);
/* 131:    */     }
/* 132:    */     catch (MessagingException ex)
/* 133:    */     {
/* 134:144 */       throw new MailParseException(ex);
/* 135:    */     }
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void setSentDate(Date sentDate)
/* 139:    */     throws MailParseException
/* 140:    */   {
/* 141:    */     try
/* 142:    */     {
/* 143:150 */       this.helper.setSentDate(sentDate);
/* 144:    */     }
/* 145:    */     catch (MessagingException ex)
/* 146:    */     {
/* 147:153 */       throw new MailParseException(ex);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   public void setSubject(String subject)
/* 152:    */     throws MailParseException
/* 153:    */   {
/* 154:    */     try
/* 155:    */     {
/* 156:159 */       this.helper.setSubject(subject);
/* 157:    */     }
/* 158:    */     catch (MessagingException ex)
/* 159:    */     {
/* 160:162 */       throw new MailParseException(ex);
/* 161:    */     }
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setText(String text)
/* 165:    */     throws MailParseException
/* 166:    */   {
/* 167:    */     try
/* 168:    */     {
/* 169:168 */       this.helper.setText(text);
/* 170:    */     }
/* 171:    */     catch (MessagingException ex)
/* 172:    */     {
/* 173:171 */       throw new MailParseException(ex);
/* 174:    */     }
/* 175:    */   }
/* 176:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.MimeMailMessage
 * JD-Core Version:    0.7.0.1
 */