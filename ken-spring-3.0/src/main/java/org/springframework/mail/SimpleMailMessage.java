/*   1:    */ package org.springframework.mail;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Date;
/*   6:    */ import org.springframework.util.Assert;
/*   7:    */ import org.springframework.util.ObjectUtils;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ public class SimpleMailMessage
/*  11:    */   implements MailMessage, Serializable
/*  12:    */ {
/*  13:    */   private String from;
/*  14:    */   private String replyTo;
/*  15:    */   private String[] to;
/*  16:    */   private String[] cc;
/*  17:    */   private String[] bcc;
/*  18:    */   private Date sentDate;
/*  19:    */   private String subject;
/*  20:    */   private String text;
/*  21:    */   
/*  22:    */   public SimpleMailMessage() {}
/*  23:    */   
/*  24:    */   public SimpleMailMessage(SimpleMailMessage original)
/*  25:    */   {
/*  26: 73 */     Assert.notNull(original, "The 'original' message argument cannot be null");
/*  27: 74 */     this.from = original.getFrom();
/*  28: 75 */     this.replyTo = original.getReplyTo();
/*  29: 76 */     if (original.getTo() != null) {
/*  30: 77 */       this.to = copy(original.getTo());
/*  31:    */     }
/*  32: 79 */     if (original.getCc() != null) {
/*  33: 80 */       this.cc = copy(original.getCc());
/*  34:    */     }
/*  35: 82 */     if (original.getBcc() != null) {
/*  36: 83 */       this.bcc = copy(original.getBcc());
/*  37:    */     }
/*  38: 85 */     this.sentDate = original.getSentDate();
/*  39: 86 */     this.subject = original.getSubject();
/*  40: 87 */     this.text = original.getText();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setFrom(String from)
/*  44:    */   {
/*  45: 92 */     this.from = from;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public String getFrom()
/*  49:    */   {
/*  50: 96 */     return this.from;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setReplyTo(String replyTo)
/*  54:    */   {
/*  55:100 */     this.replyTo = replyTo;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getReplyTo()
/*  59:    */   {
/*  60:104 */     return this.replyTo;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void setTo(String to)
/*  64:    */   {
/*  65:108 */     this.to = new String[] { to };
/*  66:    */   }
/*  67:    */   
/*  68:    */   public void setTo(String[] to)
/*  69:    */   {
/*  70:112 */     this.to = to;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public String[] getTo()
/*  74:    */   {
/*  75:116 */     return this.to;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void setCc(String cc)
/*  79:    */   {
/*  80:120 */     this.cc = new String[] { cc };
/*  81:    */   }
/*  82:    */   
/*  83:    */   public void setCc(String[] cc)
/*  84:    */   {
/*  85:124 */     this.cc = cc;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public String[] getCc()
/*  89:    */   {
/*  90:128 */     return this.cc;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void setBcc(String bcc)
/*  94:    */   {
/*  95:132 */     this.bcc = new String[] { bcc };
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setBcc(String[] bcc)
/*  99:    */   {
/* 100:136 */     this.bcc = bcc;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public String[] getBcc()
/* 104:    */   {
/* 105:140 */     return this.bcc;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void setSentDate(Date sentDate)
/* 109:    */   {
/* 110:144 */     this.sentDate = sentDate;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public Date getSentDate()
/* 114:    */   {
/* 115:148 */     return this.sentDate;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void setSubject(String subject)
/* 119:    */   {
/* 120:152 */     this.subject = subject;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public String getSubject()
/* 124:    */   {
/* 125:156 */     return this.subject;
/* 126:    */   }
/* 127:    */   
/* 128:    */   public void setText(String text)
/* 129:    */   {
/* 130:160 */     this.text = text;
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String getText()
/* 134:    */   {
/* 135:164 */     return this.text;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public void copyTo(MailMessage target)
/* 139:    */   {
/* 140:174 */     Assert.notNull(target, "The 'target' message argument cannot be null");
/* 141:175 */     if (getFrom() != null) {
/* 142:176 */       target.setFrom(getFrom());
/* 143:    */     }
/* 144:178 */     if (getReplyTo() != null) {
/* 145:179 */       target.setReplyTo(getReplyTo());
/* 146:    */     }
/* 147:181 */     if (getTo() != null) {
/* 148:182 */       target.setTo(getTo());
/* 149:    */     }
/* 150:184 */     if (getCc() != null) {
/* 151:185 */       target.setCc(getCc());
/* 152:    */     }
/* 153:187 */     if (getBcc() != null) {
/* 154:188 */       target.setBcc(getBcc());
/* 155:    */     }
/* 156:190 */     if (getSentDate() != null) {
/* 157:191 */       target.setSentDate(getSentDate());
/* 158:    */     }
/* 159:193 */     if (getSubject() != null) {
/* 160:194 */       target.setSubject(getSubject());
/* 161:    */     }
/* 162:196 */     if (getText() != null) {
/* 163:197 */       target.setText(getText());
/* 164:    */     }
/* 165:    */   }
/* 166:    */   
/* 167:    */   public String toString()
/* 168:    */   {
/* 169:204 */     StringBuilder sb = new StringBuilder("SimpleMailMessage: ");
/* 170:205 */     sb.append("from=").append(this.from).append("; ");
/* 171:206 */     sb.append("replyTo=").append(this.replyTo).append("; ");
/* 172:207 */     sb.append("to=").append(StringUtils.arrayToCommaDelimitedString(this.to)).append("; ");
/* 173:208 */     sb.append("cc=").append(StringUtils.arrayToCommaDelimitedString(this.cc)).append("; ");
/* 174:209 */     sb.append("bcc=").append(StringUtils.arrayToCommaDelimitedString(this.bcc)).append("; ");
/* 175:210 */     sb.append("sentDate=").append(this.sentDate).append("; ");
/* 176:211 */     sb.append("subject=").append(this.subject).append("; ");
/* 177:212 */     sb.append("text=").append(this.text);
/* 178:213 */     return sb.toString();
/* 179:    */   }
/* 180:    */   
/* 181:    */   public boolean equals(Object other)
/* 182:    */   {
/* 183:218 */     if (this == other) {
/* 184:219 */       return true;
/* 185:    */     }
/* 186:221 */     if (!(other instanceof SimpleMailMessage)) {
/* 187:222 */       return false;
/* 188:    */     }
/* 189:224 */     SimpleMailMessage otherMessage = (SimpleMailMessage)other;
/* 190:    */     
/* 191:    */ 
/* 192:    */ 
/* 193:    */ 
/* 194:    */ 
/* 195:    */ 
/* 196:    */ 
/* 197:232 */     return (ObjectUtils.nullSafeEquals(this.from, otherMessage.from)) && (ObjectUtils.nullSafeEquals(this.replyTo, otherMessage.replyTo)) && (Arrays.equals(this.to, otherMessage.to)) && (Arrays.equals(this.cc, otherMessage.cc)) && (Arrays.equals(this.bcc, otherMessage.bcc)) && (ObjectUtils.nullSafeEquals(this.sentDate, otherMessage.sentDate)) && (ObjectUtils.nullSafeEquals(this.subject, otherMessage.subject)) && (ObjectUtils.nullSafeEquals(this.text, otherMessage.text));
/* 198:    */   }
/* 199:    */   
/* 200:    */   public int hashCode()
/* 201:    */   {
/* 202:237 */     int hashCode = this.from == null ? 0 : this.from.hashCode();
/* 203:238 */     hashCode = 29 * hashCode + (this.replyTo == null ? 0 : this.replyTo.hashCode());
/* 204:239 */     for (int i = 0; (this.to != null) && (i < this.to.length); i++) {
/* 205:240 */       hashCode = 29 * hashCode + (this.to == null ? 0 : this.to[i].hashCode());
/* 206:    */     }
/* 207:242 */     for (int i = 0; (this.cc != null) && (i < this.cc.length); i++) {
/* 208:243 */       hashCode = 29 * hashCode + (this.cc == null ? 0 : this.cc[i].hashCode());
/* 209:    */     }
/* 210:245 */     for (int i = 0; (this.bcc != null) && (i < this.bcc.length); i++) {
/* 211:246 */       hashCode = 29 * hashCode + (this.bcc == null ? 0 : this.bcc[i].hashCode());
/* 212:    */     }
/* 213:248 */     hashCode = 29 * hashCode + (this.sentDate == null ? 0 : this.sentDate.hashCode());
/* 214:249 */     hashCode = 29 * hashCode + (this.subject == null ? 0 : this.subject.hashCode());
/* 215:250 */     hashCode = 29 * hashCode + (this.text == null ? 0 : this.text.hashCode());
/* 216:251 */     return hashCode;
/* 217:    */   }
/* 218:    */   
/* 219:    */   private static String[] copy(String[] state)
/* 220:    */   {
/* 221:256 */     String[] copy = new String[state.length];
/* 222:257 */     System.arraycopy(state, 0, copy, 0, state.length);
/* 223:258 */     return copy;
/* 224:    */   }
/* 225:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.SimpleMailMessage
 * JD-Core Version:    0.7.0.1
 */