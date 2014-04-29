/*    1:     */ package org.springframework.mail.javamail;
/*    2:     */ 
/*    3:     */ import java.io.File;
/*    4:     */ import java.io.IOException;
/*    5:     */ import java.io.InputStream;
/*    6:     */ import java.io.OutputStream;
/*    7:     */ import java.io.UnsupportedEncodingException;
/*    8:     */ import java.util.Date;
/*    9:     */ import javax.activation.DataHandler;
/*   10:     */ import javax.activation.DataSource;
/*   11:     */ import javax.activation.FileDataSource;
/*   12:     */ import javax.activation.FileTypeMap;
/*   13:     */ import javax.mail.BodyPart;
/*   14:     */ import javax.mail.Message.RecipientType;
/*   15:     */ import javax.mail.MessagingException;
/*   16:     */ import javax.mail.internet.AddressException;
/*   17:     */ import javax.mail.internet.InternetAddress;
/*   18:     */ import javax.mail.internet.MimeBodyPart;
/*   19:     */ import javax.mail.internet.MimeMessage;
/*   20:     */ import javax.mail.internet.MimeMultipart;
/*   21:     */ import javax.mail.internet.MimePart;
/*   22:     */ import org.springframework.core.io.InputStreamSource;
/*   23:     */ import org.springframework.core.io.Resource;
/*   24:     */ import org.springframework.util.Assert;
/*   25:     */ 
/*   26:     */ public class MimeMessageHelper
/*   27:     */ {
/*   28:     */   public static final int MULTIPART_MODE_NO = 0;
/*   29:     */   public static final int MULTIPART_MODE_MIXED = 1;
/*   30:     */   public static final int MULTIPART_MODE_RELATED = 2;
/*   31:     */   public static final int MULTIPART_MODE_MIXED_RELATED = 3;
/*   32:     */   private static final String MULTIPART_SUBTYPE_MIXED = "mixed";
/*   33:     */   private static final String MULTIPART_SUBTYPE_RELATED = "related";
/*   34:     */   private static final String MULTIPART_SUBTYPE_ALTERNATIVE = "alternative";
/*   35:     */   private static final String CONTENT_TYPE_ALTERNATIVE = "text/alternative";
/*   36:     */   private static final String CONTENT_TYPE_HTML = "text/html";
/*   37:     */   private static final String CONTENT_TYPE_CHARSET_SUFFIX = ";charset=";
/*   38:     */   private static final String HEADER_PRIORITY = "X-Priority";
/*   39:     */   private static final String HEADER_CONTENT_ID = "Content-ID";
/*   40:     */   private final MimeMessage mimeMessage;
/*   41:     */   private MimeMultipart rootMimeMultipart;
/*   42:     */   private MimeMultipart mimeMultipart;
/*   43:     */   private final String encoding;
/*   44:     */   private FileTypeMap fileTypeMap;
/*   45: 172 */   private boolean validateAddresses = false;
/*   46:     */   
/*   47:     */   public MimeMessageHelper(MimeMessage mimeMessage)
/*   48:     */   {
/*   49: 188 */     this(mimeMessage, null);
/*   50:     */   }
/*   51:     */   
/*   52:     */   public MimeMessageHelper(MimeMessage mimeMessage, String encoding)
/*   53:     */   {
/*   54: 200 */     this.mimeMessage = mimeMessage;
/*   55: 201 */     this.encoding = (encoding != null ? encoding : getDefaultEncoding(mimeMessage));
/*   56: 202 */     this.fileTypeMap = getDefaultFileTypeMap(mimeMessage);
/*   57:     */   }
/*   58:     */   
/*   59:     */   public MimeMessageHelper(MimeMessage mimeMessage, boolean multipart)
/*   60:     */     throws MessagingException
/*   61:     */   {
/*   62: 225 */     this(mimeMessage, multipart, null);
/*   63:     */   }
/*   64:     */   
/*   65:     */   public MimeMessageHelper(MimeMessage mimeMessage, boolean multipart, String encoding)
/*   66:     */     throws MessagingException
/*   67:     */   {
/*   68: 246 */     this(mimeMessage, multipart ? 3 : 0, encoding);
/*   69:     */   }
/*   70:     */   
/*   71:     */   public MimeMessageHelper(MimeMessage mimeMessage, int multipartMode)
/*   72:     */     throws MessagingException
/*   73:     */   {
/*   74: 268 */     this(mimeMessage, multipartMode, null);
/*   75:     */   }
/*   76:     */   
/*   77:     */   public MimeMessageHelper(MimeMessage mimeMessage, int multipartMode, String encoding)
/*   78:     */     throws MessagingException
/*   79:     */   {
/*   80: 288 */     this.mimeMessage = mimeMessage;
/*   81: 289 */     createMimeMultiparts(mimeMessage, multipartMode);
/*   82: 290 */     this.encoding = (encoding != null ? encoding : getDefaultEncoding(mimeMessage));
/*   83: 291 */     this.fileTypeMap = getDefaultFileTypeMap(mimeMessage);
/*   84:     */   }
/*   85:     */   
/*   86:     */   public final MimeMessage getMimeMessage()
/*   87:     */   {
/*   88: 299 */     return this.mimeMessage;
/*   89:     */   }
/*   90:     */   
/*   91:     */   protected void createMimeMultiparts(MimeMessage mimeMessage, int multipartMode)
/*   92:     */     throws MessagingException
/*   93:     */   {
/*   94: 327 */     switch (multipartMode)
/*   95:     */     {
/*   96:     */     case 0: 
/*   97: 329 */       setMimeMultiparts(null, null);
/*   98: 330 */       break;
/*   99:     */     case 1: 
/*  100: 332 */       MimeMultipart mixedMultipart = new MimeMultipart("mixed");
/*  101: 333 */       mimeMessage.setContent(mixedMultipart);
/*  102: 334 */       setMimeMultiparts(mixedMultipart, mixedMultipart);
/*  103: 335 */       break;
/*  104:     */     case 2: 
/*  105: 337 */       MimeMultipart relatedMultipart = new MimeMultipart("related");
/*  106: 338 */       mimeMessage.setContent(relatedMultipart);
/*  107: 339 */       setMimeMultiparts(relatedMultipart, relatedMultipart);
/*  108: 340 */       break;
/*  109:     */     case 3: 
/*  110: 342 */       MimeMultipart rootMixedMultipart = new MimeMultipart("mixed");
/*  111: 343 */       mimeMessage.setContent(rootMixedMultipart);
/*  112: 344 */       MimeMultipart nestedRelatedMultipart = new MimeMultipart("related");
/*  113: 345 */       MimeBodyPart relatedBodyPart = new MimeBodyPart();
/*  114: 346 */       relatedBodyPart.setContent(nestedRelatedMultipart);
/*  115: 347 */       rootMixedMultipart.addBodyPart(relatedBodyPart);
/*  116: 348 */       setMimeMultiparts(rootMixedMultipart, nestedRelatedMultipart);
/*  117: 349 */       break;
/*  118:     */     default: 
/*  119: 351 */       throw new IllegalArgumentException("Only multipart modes MIXED_RELATED, RELATED and NO supported");
/*  120:     */     }
/*  121:     */   }
/*  122:     */   
/*  123:     */   protected final void setMimeMultiparts(MimeMultipart root, MimeMultipart main)
/*  124:     */   {
/*  125: 364 */     this.rootMimeMultipart = root;
/*  126: 365 */     this.mimeMultipart = main;
/*  127:     */   }
/*  128:     */   
/*  129:     */   public final boolean isMultipart()
/*  130:     */   {
/*  131: 374 */     return this.rootMimeMultipart != null;
/*  132:     */   }
/*  133:     */   
/*  134:     */   private void checkMultipart()
/*  135:     */     throws IllegalStateException
/*  136:     */   {
/*  137: 381 */     if (!isMultipart()) {
/*  138: 382 */       throw new IllegalStateException("Not in multipart mode - create an appropriate MimeMessageHelper via a constructor that takes a 'multipart' flag if you need to set alternative texts or add inline elements or attachments.");
/*  139:     */     }
/*  140:     */   }
/*  141:     */   
/*  142:     */   public final MimeMultipart getRootMimeMultipart()
/*  143:     */     throws IllegalStateException
/*  144:     */   {
/*  145: 399 */     checkMultipart();
/*  146: 400 */     return this.rootMimeMultipart;
/*  147:     */   }
/*  148:     */   
/*  149:     */   public final MimeMultipart getMimeMultipart()
/*  150:     */     throws IllegalStateException
/*  151:     */   {
/*  152: 414 */     checkMultipart();
/*  153: 415 */     return this.mimeMultipart;
/*  154:     */   }
/*  155:     */   
/*  156:     */   protected String getDefaultEncoding(MimeMessage mimeMessage)
/*  157:     */   {
/*  158: 426 */     if ((mimeMessage instanceof SmartMimeMessage)) {
/*  159: 427 */       return ((SmartMimeMessage)mimeMessage).getDefaultEncoding();
/*  160:     */     }
/*  161: 429 */     return null;
/*  162:     */   }
/*  163:     */   
/*  164:     */   public String getEncoding()
/*  165:     */   {
/*  166: 436 */     return this.encoding;
/*  167:     */   }
/*  168:     */   
/*  169:     */   protected FileTypeMap getDefaultFileTypeMap(MimeMessage mimeMessage)
/*  170:     */   {
/*  171: 447 */     if ((mimeMessage instanceof SmartMimeMessage))
/*  172:     */     {
/*  173: 448 */       FileTypeMap fileTypeMap = ((SmartMimeMessage)mimeMessage).getDefaultFileTypeMap();
/*  174: 449 */       if (fileTypeMap != null) {
/*  175: 450 */         return fileTypeMap;
/*  176:     */       }
/*  177:     */     }
/*  178: 453 */     ConfigurableMimeFileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
/*  179: 454 */     fileTypeMap.afterPropertiesSet();
/*  180: 455 */     return fileTypeMap;
/*  181:     */   }
/*  182:     */   
/*  183:     */   public void setFileTypeMap(FileTypeMap fileTypeMap)
/*  184:     */   {
/*  185: 473 */     this.fileTypeMap = (fileTypeMap != null ? fileTypeMap : getDefaultFileTypeMap(getMimeMessage()));
/*  186:     */   }
/*  187:     */   
/*  188:     */   public FileTypeMap getFileTypeMap()
/*  189:     */   {
/*  190: 480 */     return this.fileTypeMap;
/*  191:     */   }
/*  192:     */   
/*  193:     */   public void setValidateAddresses(boolean validateAddresses)
/*  194:     */   {
/*  195: 493 */     this.validateAddresses = validateAddresses;
/*  196:     */   }
/*  197:     */   
/*  198:     */   public boolean isValidateAddresses()
/*  199:     */   {
/*  200: 500 */     return this.validateAddresses;
/*  201:     */   }
/*  202:     */   
/*  203:     */   protected void validateAddress(InternetAddress address)
/*  204:     */     throws AddressException
/*  205:     */   {
/*  206: 516 */     if (isValidateAddresses()) {
/*  207: 517 */       address.validate();
/*  208:     */     }
/*  209:     */   }
/*  210:     */   
/*  211:     */   protected void validateAddresses(InternetAddress[] addresses)
/*  212:     */     throws AddressException
/*  213:     */   {
/*  214: 529 */     for (InternetAddress address : addresses) {
/*  215: 530 */       validateAddress(address);
/*  216:     */     }
/*  217:     */   }
/*  218:     */   
/*  219:     */   public void setFrom(InternetAddress from)
/*  220:     */     throws MessagingException
/*  221:     */   {
/*  222: 536 */     Assert.notNull(from, "From address must not be null");
/*  223: 537 */     validateAddress(from);
/*  224: 538 */     this.mimeMessage.setFrom(from);
/*  225:     */   }
/*  226:     */   
/*  227:     */   public void setFrom(String from)
/*  228:     */     throws MessagingException
/*  229:     */   {
/*  230: 542 */     Assert.notNull(from, "From address must not be null");
/*  231: 543 */     setFrom(parseAddress(from));
/*  232:     */   }
/*  233:     */   
/*  234:     */   public void setFrom(String from, String personal)
/*  235:     */     throws MessagingException, UnsupportedEncodingException
/*  236:     */   {
/*  237: 547 */     Assert.notNull(from, "From address must not be null");
/*  238: 548 */     setFrom(getEncoding() != null ? 
/*  239: 549 */       new InternetAddress(from, personal, getEncoding()) : new InternetAddress(from, personal));
/*  240:     */   }
/*  241:     */   
/*  242:     */   public void setReplyTo(InternetAddress replyTo)
/*  243:     */     throws MessagingException
/*  244:     */   {
/*  245: 553 */     Assert.notNull(replyTo, "Reply-to address must not be null");
/*  246: 554 */     validateAddress(replyTo);
/*  247: 555 */     this.mimeMessage.setReplyTo(new InternetAddress[] { replyTo });
/*  248:     */   }
/*  249:     */   
/*  250:     */   public void setReplyTo(String replyTo)
/*  251:     */     throws MessagingException
/*  252:     */   {
/*  253: 559 */     Assert.notNull(replyTo, "Reply-to address must not be null");
/*  254: 560 */     setReplyTo(parseAddress(replyTo));
/*  255:     */   }
/*  256:     */   
/*  257:     */   public void setReplyTo(String replyTo, String personal)
/*  258:     */     throws MessagingException, UnsupportedEncodingException
/*  259:     */   {
/*  260: 564 */     Assert.notNull(replyTo, "Reply-to address must not be null");
/*  261: 565 */     InternetAddress replyToAddress = getEncoding() != null ? 
/*  262: 566 */       new InternetAddress(replyTo, personal, getEncoding()) : new InternetAddress(replyTo, personal);
/*  263: 567 */     setReplyTo(replyToAddress);
/*  264:     */   }
/*  265:     */   
/*  266:     */   public void setTo(InternetAddress to)
/*  267:     */     throws MessagingException
/*  268:     */   {
/*  269: 572 */     Assert.notNull(to, "To address must not be null");
/*  270: 573 */     validateAddress(to);
/*  271: 574 */     this.mimeMessage.setRecipient(Message.RecipientType.TO, to);
/*  272:     */   }
/*  273:     */   
/*  274:     */   public void setTo(InternetAddress[] to)
/*  275:     */     throws MessagingException
/*  276:     */   {
/*  277: 578 */     Assert.notNull(to, "To address array must not be null");
/*  278: 579 */     validateAddresses(to);
/*  279: 580 */     this.mimeMessage.setRecipients(Message.RecipientType.TO, to);
/*  280:     */   }
/*  281:     */   
/*  282:     */   public void setTo(String to)
/*  283:     */     throws MessagingException
/*  284:     */   {
/*  285: 584 */     Assert.notNull(to, "To address must not be null");
/*  286: 585 */     setTo(parseAddress(to));
/*  287:     */   }
/*  288:     */   
/*  289:     */   public void setTo(String[] to)
/*  290:     */     throws MessagingException
/*  291:     */   {
/*  292: 589 */     Assert.notNull(to, "To address array must not be null");
/*  293: 590 */     InternetAddress[] addresses = new InternetAddress[to.length];
/*  294: 591 */     for (int i = 0; i < to.length; i++) {
/*  295: 592 */       addresses[i] = parseAddress(to[i]);
/*  296:     */     }
/*  297: 594 */     setTo(addresses);
/*  298:     */   }
/*  299:     */   
/*  300:     */   public void addTo(InternetAddress to)
/*  301:     */     throws MessagingException
/*  302:     */   {
/*  303: 598 */     Assert.notNull(to, "To address must not be null");
/*  304: 599 */     validateAddress(to);
/*  305: 600 */     this.mimeMessage.addRecipient(Message.RecipientType.TO, to);
/*  306:     */   }
/*  307:     */   
/*  308:     */   public void addTo(String to)
/*  309:     */     throws MessagingException
/*  310:     */   {
/*  311: 604 */     Assert.notNull(to, "To address must not be null");
/*  312: 605 */     addTo(parseAddress(to));
/*  313:     */   }
/*  314:     */   
/*  315:     */   public void addTo(String to, String personal)
/*  316:     */     throws MessagingException, UnsupportedEncodingException
/*  317:     */   {
/*  318: 609 */     Assert.notNull(to, "To address must not be null");
/*  319: 610 */     addTo(getEncoding() != null ? 
/*  320: 611 */       new InternetAddress(to, personal, getEncoding()) : 
/*  321: 612 */       new InternetAddress(to, personal));
/*  322:     */   }
/*  323:     */   
/*  324:     */   public void setCc(InternetAddress cc)
/*  325:     */     throws MessagingException
/*  326:     */   {
/*  327: 617 */     Assert.notNull(cc, "Cc address must not be null");
/*  328: 618 */     validateAddress(cc);
/*  329: 619 */     this.mimeMessage.setRecipient(Message.RecipientType.CC, cc);
/*  330:     */   }
/*  331:     */   
/*  332:     */   public void setCc(InternetAddress[] cc)
/*  333:     */     throws MessagingException
/*  334:     */   {
/*  335: 623 */     Assert.notNull(cc, "Cc address array must not be null");
/*  336: 624 */     validateAddresses(cc);
/*  337: 625 */     this.mimeMessage.setRecipients(Message.RecipientType.CC, cc);
/*  338:     */   }
/*  339:     */   
/*  340:     */   public void setCc(String cc)
/*  341:     */     throws MessagingException
/*  342:     */   {
/*  343: 629 */     Assert.notNull(cc, "Cc address must not be null");
/*  344: 630 */     setCc(parseAddress(cc));
/*  345:     */   }
/*  346:     */   
/*  347:     */   public void setCc(String[] cc)
/*  348:     */     throws MessagingException
/*  349:     */   {
/*  350: 634 */     Assert.notNull(cc, "Cc address array must not be null");
/*  351: 635 */     InternetAddress[] addresses = new InternetAddress[cc.length];
/*  352: 636 */     for (int i = 0; i < cc.length; i++) {
/*  353: 637 */       addresses[i] = parseAddress(cc[i]);
/*  354:     */     }
/*  355: 639 */     setCc(addresses);
/*  356:     */   }
/*  357:     */   
/*  358:     */   public void addCc(InternetAddress cc)
/*  359:     */     throws MessagingException
/*  360:     */   {
/*  361: 643 */     Assert.notNull(cc, "Cc address must not be null");
/*  362: 644 */     validateAddress(cc);
/*  363: 645 */     this.mimeMessage.addRecipient(Message.RecipientType.CC, cc);
/*  364:     */   }
/*  365:     */   
/*  366:     */   public void addCc(String cc)
/*  367:     */     throws MessagingException
/*  368:     */   {
/*  369: 649 */     Assert.notNull(cc, "Cc address must not be null");
/*  370: 650 */     addCc(parseAddress(cc));
/*  371:     */   }
/*  372:     */   
/*  373:     */   public void addCc(String cc, String personal)
/*  374:     */     throws MessagingException, UnsupportedEncodingException
/*  375:     */   {
/*  376: 654 */     Assert.notNull(cc, "Cc address must not be null");
/*  377: 655 */     addCc(getEncoding() != null ? 
/*  378: 656 */       new InternetAddress(cc, personal, getEncoding()) : 
/*  379: 657 */       new InternetAddress(cc, personal));
/*  380:     */   }
/*  381:     */   
/*  382:     */   public void setBcc(InternetAddress bcc)
/*  383:     */     throws MessagingException
/*  384:     */   {
/*  385: 662 */     Assert.notNull(bcc, "Bcc address must not be null");
/*  386: 663 */     validateAddress(bcc);
/*  387: 664 */     this.mimeMessage.setRecipient(Message.RecipientType.BCC, bcc);
/*  388:     */   }
/*  389:     */   
/*  390:     */   public void setBcc(InternetAddress[] bcc)
/*  391:     */     throws MessagingException
/*  392:     */   {
/*  393: 668 */     Assert.notNull(bcc, "Bcc address array must not be null");
/*  394: 669 */     validateAddresses(bcc);
/*  395: 670 */     this.mimeMessage.setRecipients(Message.RecipientType.BCC, bcc);
/*  396:     */   }
/*  397:     */   
/*  398:     */   public void setBcc(String bcc)
/*  399:     */     throws MessagingException
/*  400:     */   {
/*  401: 674 */     Assert.notNull(bcc, "Bcc address must not be null");
/*  402: 675 */     setBcc(parseAddress(bcc));
/*  403:     */   }
/*  404:     */   
/*  405:     */   public void setBcc(String[] bcc)
/*  406:     */     throws MessagingException
/*  407:     */   {
/*  408: 679 */     Assert.notNull(bcc, "Bcc address array must not be null");
/*  409: 680 */     InternetAddress[] addresses = new InternetAddress[bcc.length];
/*  410: 681 */     for (int i = 0; i < bcc.length; i++) {
/*  411: 682 */       addresses[i] = parseAddress(bcc[i]);
/*  412:     */     }
/*  413: 684 */     setBcc(addresses);
/*  414:     */   }
/*  415:     */   
/*  416:     */   public void addBcc(InternetAddress bcc)
/*  417:     */     throws MessagingException
/*  418:     */   {
/*  419: 688 */     Assert.notNull(bcc, "Bcc address must not be null");
/*  420: 689 */     validateAddress(bcc);
/*  421: 690 */     this.mimeMessage.addRecipient(Message.RecipientType.BCC, bcc);
/*  422:     */   }
/*  423:     */   
/*  424:     */   public void addBcc(String bcc)
/*  425:     */     throws MessagingException
/*  426:     */   {
/*  427: 694 */     Assert.notNull(bcc, "Bcc address must not be null");
/*  428: 695 */     addBcc(parseAddress(bcc));
/*  429:     */   }
/*  430:     */   
/*  431:     */   public void addBcc(String bcc, String personal)
/*  432:     */     throws MessagingException, UnsupportedEncodingException
/*  433:     */   {
/*  434: 699 */     Assert.notNull(bcc, "Bcc address must not be null");
/*  435: 700 */     addBcc(getEncoding() != null ? 
/*  436: 701 */       new InternetAddress(bcc, personal, getEncoding()) : 
/*  437: 702 */       new InternetAddress(bcc, personal));
/*  438:     */   }
/*  439:     */   
/*  440:     */   private InternetAddress parseAddress(String address)
/*  441:     */     throws MessagingException
/*  442:     */   {
/*  443: 706 */     InternetAddress[] parsed = InternetAddress.parse(address);
/*  444: 707 */     if (parsed.length != 1) {
/*  445: 708 */       throw new AddressException("Illegal address", address);
/*  446:     */     }
/*  447: 710 */     InternetAddress raw = parsed[0];
/*  448:     */     try
/*  449:     */     {
/*  450: 712 */       return getEncoding() != null ? 
/*  451: 713 */         new InternetAddress(raw.getAddress(), raw.getPersonal(), getEncoding()) : raw;
/*  452:     */     }
/*  453:     */     catch (UnsupportedEncodingException ex)
/*  454:     */     {
/*  455: 716 */       throw new MessagingException("Failed to parse embedded personal name to correct encoding", ex);
/*  456:     */     }
/*  457:     */   }
/*  458:     */   
/*  459:     */   public void setPriority(int priority)
/*  460:     */     throws MessagingException
/*  461:     */   {
/*  462: 728 */     this.mimeMessage.setHeader("X-Priority", Integer.toString(priority));
/*  463:     */   }
/*  464:     */   
/*  465:     */   public void setSentDate(Date sentDate)
/*  466:     */     throws MessagingException
/*  467:     */   {
/*  468: 737 */     Assert.notNull(sentDate, "Sent date must not be null");
/*  469: 738 */     this.mimeMessage.setSentDate(sentDate);
/*  470:     */   }
/*  471:     */   
/*  472:     */   public void setSubject(String subject)
/*  473:     */     throws MessagingException
/*  474:     */   {
/*  475: 747 */     Assert.notNull(subject, "Subject must not be null");
/*  476: 748 */     if (getEncoding() != null) {
/*  477: 749 */       this.mimeMessage.setSubject(subject, getEncoding());
/*  478:     */     } else {
/*  479: 752 */       this.mimeMessage.setSubject(subject);
/*  480:     */     }
/*  481:     */   }
/*  482:     */   
/*  483:     */   public void setText(String text)
/*  484:     */     throws MessagingException
/*  485:     */   {
/*  486: 767 */     setText(text, false);
/*  487:     */   }
/*  488:     */   
/*  489:     */   public void setText(String text, boolean html)
/*  490:     */     throws MessagingException
/*  491:     */   {
/*  492: 782 */     Assert.notNull(text, "Text must not be null");
/*  493:     */     MimePart partToUse;
/*  494:     */     MimePart partToUse;
/*  495: 784 */     if (isMultipart()) {
/*  496: 785 */       partToUse = getMainPart();
/*  497:     */     } else {
/*  498: 788 */       partToUse = this.mimeMessage;
/*  499:     */     }
/*  500: 790 */     if (html) {
/*  501: 791 */       setHtmlTextToMimePart(partToUse, text);
/*  502:     */     } else {
/*  503: 794 */       setPlainTextToMimePart(partToUse, text);
/*  504:     */     }
/*  505:     */   }
/*  506:     */   
/*  507:     */   public void setText(String plainText, String htmlText)
/*  508:     */     throws MessagingException
/*  509:     */   {
/*  510: 808 */     Assert.notNull(plainText, "Plain text must not be null");
/*  511: 809 */     Assert.notNull(htmlText, "HTML text must not be null");
/*  512:     */     
/*  513: 811 */     MimeMultipart messageBody = new MimeMultipart("alternative");
/*  514: 812 */     getMainPart().setContent(messageBody, "text/alternative");
/*  515:     */     
/*  516:     */ 
/*  517: 815 */     MimeBodyPart plainTextPart = new MimeBodyPart();
/*  518: 816 */     setPlainTextToMimePart(plainTextPart, plainText);
/*  519: 817 */     messageBody.addBodyPart(plainTextPart);
/*  520:     */     
/*  521:     */ 
/*  522: 820 */     MimeBodyPart htmlTextPart = new MimeBodyPart();
/*  523: 821 */     setHtmlTextToMimePart(htmlTextPart, htmlText);
/*  524: 822 */     messageBody.addBodyPart(htmlTextPart);
/*  525:     */   }
/*  526:     */   
/*  527:     */   private MimeBodyPart getMainPart()
/*  528:     */     throws MessagingException
/*  529:     */   {
/*  530: 826 */     MimeMultipart mimeMultipart = getMimeMultipart();
/*  531: 827 */     MimeBodyPart bodyPart = null;
/*  532: 828 */     for (int i = 0; i < mimeMultipart.getCount(); i++)
/*  533:     */     {
/*  534: 829 */       BodyPart bp = mimeMultipart.getBodyPart(i);
/*  535: 830 */       if (bp.getFileName() == null) {
/*  536: 831 */         bodyPart = (MimeBodyPart)bp;
/*  537:     */       }
/*  538:     */     }
/*  539: 834 */     if (bodyPart == null)
/*  540:     */     {
/*  541: 835 */       MimeBodyPart mimeBodyPart = new MimeBodyPart();
/*  542: 836 */       mimeMultipart.addBodyPart(mimeBodyPart);
/*  543: 837 */       bodyPart = mimeBodyPart;
/*  544:     */     }
/*  545: 839 */     return bodyPart;
/*  546:     */   }
/*  547:     */   
/*  548:     */   private void setPlainTextToMimePart(MimePart mimePart, String text)
/*  549:     */     throws MessagingException
/*  550:     */   {
/*  551: 843 */     if (getEncoding() != null) {
/*  552: 844 */       mimePart.setText(text, getEncoding());
/*  553:     */     } else {
/*  554: 847 */       mimePart.setText(text);
/*  555:     */     }
/*  556:     */   }
/*  557:     */   
/*  558:     */   private void setHtmlTextToMimePart(MimePart mimePart, String text)
/*  559:     */     throws MessagingException
/*  560:     */   {
/*  561: 852 */     if (getEncoding() != null) {
/*  562: 853 */       mimePart.setContent(text, "text/html;charset=" + getEncoding());
/*  563:     */     } else {
/*  564: 856 */       mimePart.setContent(text, "text/html");
/*  565:     */     }
/*  566:     */   }
/*  567:     */   
/*  568:     */   public void addInline(String contentId, DataSource dataSource)
/*  569:     */     throws MessagingException
/*  570:     */   {
/*  571: 879 */     Assert.notNull(contentId, "Content ID must not be null");
/*  572: 880 */     Assert.notNull(dataSource, "DataSource must not be null");
/*  573: 881 */     MimeBodyPart mimeBodyPart = new MimeBodyPart();
/*  574: 882 */     mimeBodyPart.setDisposition("inline");
/*  575:     */     
/*  576:     */ 
/*  577: 885 */     mimeBodyPart.setHeader("Content-ID", "<" + contentId + ">");
/*  578: 886 */     mimeBodyPart.setDataHandler(new DataHandler(dataSource));
/*  579: 887 */     getMimeMultipart().addBodyPart(mimeBodyPart);
/*  580:     */   }
/*  581:     */   
/*  582:     */   public void addInline(String contentId, File file)
/*  583:     */     throws MessagingException
/*  584:     */   {
/*  585: 908 */     Assert.notNull(file, "File must not be null");
/*  586: 909 */     FileDataSource dataSource = new FileDataSource(file);
/*  587: 910 */     dataSource.setFileTypeMap(getFileTypeMap());
/*  588: 911 */     addInline(contentId, dataSource);
/*  589:     */   }
/*  590:     */   
/*  591:     */   public void addInline(String contentId, Resource resource)
/*  592:     */     throws MessagingException
/*  593:     */   {
/*  594: 935 */     Assert.notNull(resource, "Resource must not be null");
/*  595: 936 */     String contentType = getFileTypeMap().getContentType(resource.getFilename());
/*  596: 937 */     addInline(contentId, resource, contentType);
/*  597:     */   }
/*  598:     */   
/*  599:     */   public void addInline(String contentId, InputStreamSource inputStreamSource, String contentType)
/*  600:     */     throws MessagingException
/*  601:     */   {
/*  602: 965 */     Assert.notNull(inputStreamSource, "InputStreamSource must not be null");
/*  603: 966 */     if (((inputStreamSource instanceof Resource)) && (((Resource)inputStreamSource).isOpen())) {
/*  604: 967 */       throw new IllegalArgumentException(
/*  605: 968 */         "Passed-in Resource contains an open stream: invalid argument. JavaMail requires an InputStreamSource that creates a fresh stream for every call.");
/*  606:     */     }
/*  607: 971 */     DataSource dataSource = createDataSource(inputStreamSource, contentType, "inline");
/*  608: 972 */     addInline(contentId, dataSource);
/*  609:     */   }
/*  610:     */   
/*  611:     */   public void addAttachment(String attachmentFilename, DataSource dataSource)
/*  612:     */     throws MessagingException
/*  613:     */   {
/*  614: 990 */     Assert.notNull(attachmentFilename, "Attachment filename must not be null");
/*  615: 991 */     Assert.notNull(dataSource, "DataSource must not be null");
/*  616: 992 */     MimeBodyPart mimeBodyPart = new MimeBodyPart();
/*  617: 993 */     mimeBodyPart.setDisposition("attachment");
/*  618: 994 */     mimeBodyPart.setFileName(attachmentFilename);
/*  619: 995 */     mimeBodyPart.setDataHandler(new DataHandler(dataSource));
/*  620: 996 */     getRootMimeMultipart().addBodyPart(mimeBodyPart);
/*  621:     */   }
/*  622:     */   
/*  623:     */   public void addAttachment(String attachmentFilename, File file)
/*  624:     */     throws MessagingException
/*  625:     */   {
/*  626:1013 */     Assert.notNull(file, "File must not be null");
/*  627:1014 */     FileDataSource dataSource = new FileDataSource(file);
/*  628:1015 */     dataSource.setFileTypeMap(getFileTypeMap());
/*  629:1016 */     addAttachment(attachmentFilename, dataSource);
/*  630:     */   }
/*  631:     */   
/*  632:     */   public void addAttachment(String attachmentFilename, InputStreamSource inputStreamSource)
/*  633:     */     throws MessagingException
/*  634:     */   {
/*  635:1040 */     String contentType = getFileTypeMap().getContentType(attachmentFilename);
/*  636:1041 */     addAttachment(attachmentFilename, inputStreamSource, contentType);
/*  637:     */   }
/*  638:     */   
/*  639:     */   public void addAttachment(String attachmentFilename, InputStreamSource inputStreamSource, String contentType)
/*  640:     */     throws MessagingException
/*  641:     */   {
/*  642:1064 */     Assert.notNull(inputStreamSource, "InputStreamSource must not be null");
/*  643:1065 */     if (((inputStreamSource instanceof Resource)) && (((Resource)inputStreamSource).isOpen())) {
/*  644:1066 */       throw new IllegalArgumentException(
/*  645:1067 */         "Passed-in Resource contains an open stream: invalid argument. JavaMail requires an InputStreamSource that creates a fresh stream for every call.");
/*  646:     */     }
/*  647:1070 */     DataSource dataSource = createDataSource(inputStreamSource, contentType, attachmentFilename);
/*  648:1071 */     addAttachment(attachmentFilename, dataSource);
/*  649:     */   }
/*  650:     */   
/*  651:     */   protected DataSource createDataSource(final InputStreamSource inputStreamSource, final String contentType, final String name)
/*  652:     */   {
/*  653:1084 */     new DataSource()
/*  654:     */     {
/*  655:     */       public InputStream getInputStream()
/*  656:     */         throws IOException
/*  657:     */       {
/*  658:1086 */         return inputStreamSource.getInputStream();
/*  659:     */       }
/*  660:     */       
/*  661:     */       public OutputStream getOutputStream()
/*  662:     */       {
/*  663:1089 */         throw new UnsupportedOperationException("Read-only javax.activation.DataSource");
/*  664:     */       }
/*  665:     */       
/*  666:     */       public String getContentType()
/*  667:     */       {
/*  668:1092 */         return contentType;
/*  669:     */       }
/*  670:     */       
/*  671:     */       public String getName()
/*  672:     */       {
/*  673:1095 */         return name;
/*  674:     */       }
/*  675:     */     };
/*  676:     */   }
/*  677:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.mail.javamail.MimeMessageHelper
 * JD-Core Version:    0.7.0.1
 */