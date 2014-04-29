/*    1:     */ package jxl.write.biff;
/*    2:     */ 
/*    3:     */ import java.io.IOException;
/*    4:     */ import java.io.OutputStream;
/*    5:     */ import java.util.ArrayList;
/*    6:     */ import java.util.HashMap;
/*    7:     */ import java.util.Iterator;
/*    8:     */ import jxl.biff.BaseCompoundFile;
/*    9:     */ import jxl.biff.BaseCompoundFile.PropertyStorage;
/*   10:     */ import jxl.biff.IntegerHelper;
/*   11:     */ import jxl.common.Assert;
/*   12:     */ import jxl.common.Logger;
/*   13:     */ import jxl.read.biff.BiffException;
/*   14:     */ 
/*   15:     */ final class CompoundFile
/*   16:     */   extends BaseCompoundFile
/*   17:     */ {
/*   18:  52 */   private static Logger logger = Logger.getLogger(CompoundFile.class);
/*   19:     */   private OutputStream out;
/*   20:     */   private ExcelDataOutput excelData;
/*   21:     */   private int size;
/*   22:     */   private int requiredSize;
/*   23:     */   private int numBigBlockDepotBlocks;
/*   24:     */   private int numSmallBlockDepotChainBlocks;
/*   25:     */   private int numSmallBlockDepotBlocks;
/*   26:     */   private int numExtensionBlocks;
/*   27:     */   private int extensionBlock;
/*   28:     */   private int excelDataBlocks;
/*   29:     */   private int rootStartBlock;
/*   30:     */   private int excelDataStartBlock;
/*   31:     */   private int bbdStartBlock;
/*   32:     */   private int sbdStartBlockChain;
/*   33:     */   private int sbdStartBlock;
/*   34:     */   private int additionalPropertyBlocks;
/*   35:     */   private int numSmallBlocks;
/*   36:     */   private int numPropertySets;
/*   37:     */   private int numRootEntryBlocks;
/*   38:     */   private ArrayList additionalPropertySets;
/*   39:     */   private HashMap standardPropertySets;
/*   40:     */   private int bbdPos;
/*   41:     */   private byte[] bigBlockDepot;
/*   42:     */   
/*   43:     */   private static final class ReadPropertyStorage
/*   44:     */   {
/*   45:     */     BaseCompoundFile.PropertyStorage propertyStorage;
/*   46:     */     byte[] data;
/*   47:     */     int number;
/*   48:     */     
/*   49:     */     ReadPropertyStorage(BaseCompoundFile.PropertyStorage ps, byte[] d, int n)
/*   50:     */     {
/*   51: 172 */       this.propertyStorage = ps;
/*   52: 173 */       this.data = d;
/*   53: 174 */       this.number = n;
/*   54:     */     }
/*   55:     */   }
/*   56:     */   
/*   57:     */   public CompoundFile(ExcelDataOutput data, int l, OutputStream os, jxl.read.biff.CompoundFile rcf)
/*   58:     */     throws CopyAdditionalPropertySetsException, IOException
/*   59:     */   {
/*   60: 206 */     this.size = l;
/*   61: 207 */     this.excelData = data;
/*   62:     */     
/*   63: 209 */     readAdditionalPropertySets(rcf);
/*   64:     */     
/*   65: 211 */     this.numRootEntryBlocks = 1;
/*   66: 212 */     this.numPropertySets = 
/*   67: 213 */       (4 + (this.additionalPropertySets != null ? this.additionalPropertySets.size() : 0));
/*   68: 216 */     if (this.additionalPropertySets != null)
/*   69:     */     {
/*   70: 218 */       this.numSmallBlockDepotChainBlocks = getBigBlocksRequired(this.numSmallBlocks * 4);
/*   71: 219 */       this.numSmallBlockDepotBlocks = getBigBlocksRequired(
/*   72: 220 */         this.numSmallBlocks * 64);
/*   73:     */       
/*   74:     */ 
/*   75: 223 */       this.numRootEntryBlocks = (this.numRootEntryBlocks + getBigBlocksRequired(this.additionalPropertySets.size() * 128));
/*   76:     */     }
/*   77: 227 */     int blocks = getBigBlocksRequired(l);
/*   78: 231 */     if (l < 4096) {
/*   79: 233 */       this.requiredSize = 4096;
/*   80:     */     } else {
/*   81: 237 */       this.requiredSize = (blocks * 512);
/*   82:     */     }
/*   83: 240 */     this.out = os;
/*   84:     */     
/*   85:     */ 
/*   86:     */ 
/*   87: 244 */     this.excelDataBlocks = (this.requiredSize / 512);
/*   88: 245 */     this.numBigBlockDepotBlocks = 1;
/*   89:     */     
/*   90: 247 */     int blockChainLength = 109;
/*   91:     */     
/*   92: 249 */     int startTotalBlocks = this.excelDataBlocks + 
/*   93: 250 */       8 + 
/*   94: 251 */       8 + 
/*   95: 252 */       this.additionalPropertyBlocks + 
/*   96: 253 */       this.numSmallBlockDepotBlocks + 
/*   97: 254 */       this.numSmallBlockDepotChainBlocks + 
/*   98: 255 */       this.numRootEntryBlocks;
/*   99:     */     
/*  100: 257 */     int totalBlocks = startTotalBlocks + this.numBigBlockDepotBlocks;
/*  101:     */     
/*  102:     */ 
/*  103: 260 */     this.numBigBlockDepotBlocks = ((int)Math.ceil(totalBlocks / 
/*  104: 261 */       128.0D));
/*  105:     */     
/*  106:     */ 
/*  107: 264 */     totalBlocks = startTotalBlocks + this.numBigBlockDepotBlocks;
/*  108:     */     
/*  109:     */ 
/*  110: 267 */     this.numBigBlockDepotBlocks = ((int)Math.ceil(totalBlocks / 
/*  111: 268 */       128.0D));
/*  112:     */     
/*  113:     */ 
/*  114: 271 */     totalBlocks = startTotalBlocks + this.numBigBlockDepotBlocks;
/*  115: 275 */     if (this.numBigBlockDepotBlocks > blockChainLength - 1)
/*  116:     */     {
/*  117: 279 */       this.extensionBlock = 0;
/*  118:     */       
/*  119:     */ 
/*  120: 282 */       int bbdBlocksLeft = this.numBigBlockDepotBlocks - blockChainLength + 1;
/*  121:     */       
/*  122: 284 */       this.numExtensionBlocks = ((int)Math.ceil(bbdBlocksLeft / 
/*  123: 285 */         127.0D));
/*  124:     */       
/*  125:     */ 
/*  126:     */ 
/*  127: 289 */       totalBlocks = startTotalBlocks + 
/*  128: 290 */         this.numExtensionBlocks + 
/*  129: 291 */         this.numBigBlockDepotBlocks;
/*  130: 292 */       this.numBigBlockDepotBlocks = ((int)Math.ceil(totalBlocks / 
/*  131: 293 */         128.0D));
/*  132:     */       
/*  133:     */ 
/*  134: 296 */       totalBlocks = startTotalBlocks + 
/*  135: 297 */         this.numExtensionBlocks + 
/*  136: 298 */         this.numBigBlockDepotBlocks;
/*  137:     */     }
/*  138:     */     else
/*  139:     */     {
/*  140: 302 */       this.extensionBlock = -2;
/*  141: 303 */       this.numExtensionBlocks = 0;
/*  142:     */     }
/*  143: 308 */     this.excelDataStartBlock = this.numExtensionBlocks;
/*  144:     */     
/*  145:     */ 
/*  146: 311 */     this.sbdStartBlock = -2;
/*  147: 312 */     if ((this.additionalPropertySets != null) && (this.numSmallBlockDepotBlocks != 0)) {
/*  148: 314 */       this.sbdStartBlock = 
/*  149:     */       
/*  150:     */ 
/*  151: 317 */         (this.excelDataStartBlock + this.excelDataBlocks + this.additionalPropertyBlocks + 16);
/*  152:     */     }
/*  153: 322 */     this.sbdStartBlockChain = -2;
/*  154: 324 */     if (this.sbdStartBlock != -2) {
/*  155: 326 */       this.sbdStartBlockChain = (this.sbdStartBlock + this.numSmallBlockDepotBlocks);
/*  156:     */     }
/*  157: 330 */     if (this.sbdStartBlockChain != -2) {
/*  158: 332 */       this.bbdStartBlock = 
/*  159: 333 */         (this.sbdStartBlockChain + this.numSmallBlockDepotChainBlocks);
/*  160:     */     } else {
/*  161: 337 */       this.bbdStartBlock = 
/*  162:     */       
/*  163:     */ 
/*  164: 340 */         (this.excelDataStartBlock + this.excelDataBlocks + this.additionalPropertyBlocks + 16);
/*  165:     */     }
/*  166: 344 */     this.rootStartBlock = 
/*  167: 345 */       (this.bbdStartBlock + this.numBigBlockDepotBlocks);
/*  168: 348 */     if (totalBlocks != this.rootStartBlock + this.numRootEntryBlocks)
/*  169:     */     {
/*  170: 350 */       logger.warn("Root start block and total blocks are inconsistent  generated file may be corrupt");
/*  171:     */       
/*  172: 352 */       logger.warn("RootStartBlock " + this.rootStartBlock + " totalBlocks " + totalBlocks);
/*  173:     */     }
/*  174:     */   }
/*  175:     */   
/*  176:     */   private void readAdditionalPropertySets(jxl.read.biff.CompoundFile readCompoundFile)
/*  177:     */     throws CopyAdditionalPropertySetsException, IOException
/*  178:     */   {
/*  179: 367 */     if (readCompoundFile == null) {
/*  180: 369 */       return;
/*  181:     */     }
/*  182: 372 */     this.additionalPropertySets = new ArrayList();
/*  183: 373 */     this.standardPropertySets = new HashMap();
/*  184: 374 */     int blocksRequired = 0;
/*  185:     */     
/*  186: 376 */     int numPropertySets = readCompoundFile.getNumberOfPropertySets();
/*  187: 378 */     for (int i = 0; i < numPropertySets; i++)
/*  188:     */     {
/*  189: 380 */       BaseCompoundFile.PropertyStorage ps = readCompoundFile.getPropertySet(i);
/*  190:     */       
/*  191: 382 */       boolean standard = false;
/*  192: 384 */       if (ps.name.equalsIgnoreCase("Root Entry"))
/*  193:     */       {
/*  194: 386 */         standard = true;
/*  195: 387 */         ReadPropertyStorage rps = new ReadPropertyStorage(ps, null, i);
/*  196: 388 */         this.standardPropertySets.put("Root Entry", rps);
/*  197:     */       }
/*  198: 392 */       for (int j = 0; (j < STANDARD_PROPERTY_SETS.length) && (!standard); j++) {
/*  199: 394 */         if (ps.name.equalsIgnoreCase(STANDARD_PROPERTY_SETS[j]))
/*  200:     */         {
/*  201: 397 */           BaseCompoundFile.PropertyStorage ps2 = readCompoundFile.findPropertyStorage(ps.name);
/*  202: 398 */           Assert.verify(ps2 != null);
/*  203: 400 */           if (ps2 == ps)
/*  204:     */           {
/*  205: 402 */             standard = true;
/*  206: 403 */             ReadPropertyStorage rps = new ReadPropertyStorage(ps, null, i);
/*  207: 404 */             this.standardPropertySets.put(STANDARD_PROPERTY_SETS[j], rps);
/*  208:     */           }
/*  209:     */         }
/*  210:     */       }
/*  211: 409 */       if (!standard) {
/*  212:     */         try
/*  213:     */         {
/*  214: 413 */           byte[] data = (byte[])null;
/*  215: 414 */           if (ps.size > 0) {
/*  216: 416 */             data = readCompoundFile.getStream(i);
/*  217:     */           } else {
/*  218: 420 */             data = new byte[0];
/*  219:     */           }
/*  220: 422 */           ReadPropertyStorage rps = new ReadPropertyStorage(ps, data, i);
/*  221: 423 */           this.additionalPropertySets.add(rps);
/*  222: 425 */           if (data.length > 4096)
/*  223:     */           {
/*  224: 427 */             int blocks = getBigBlocksRequired(data.length);
/*  225: 428 */             blocksRequired += blocks;
/*  226:     */           }
/*  227:     */           else
/*  228:     */           {
/*  229: 432 */             int blocks = getSmallBlocksRequired(data.length);
/*  230: 433 */             this.numSmallBlocks += blocks;
/*  231:     */           }
/*  232:     */         }
/*  233:     */         catch (BiffException e)
/*  234:     */         {
/*  235: 438 */           logger.error(e);
/*  236: 439 */           throw new CopyAdditionalPropertySetsException();
/*  237:     */         }
/*  238:     */       }
/*  239:     */     }
/*  240: 444 */     this.additionalPropertyBlocks = blocksRequired;
/*  241:     */   }
/*  242:     */   
/*  243:     */   public void write()
/*  244:     */     throws IOException
/*  245:     */   {
/*  246: 454 */     writeHeader();
/*  247: 455 */     writeExcelData();
/*  248: 456 */     writeDocumentSummaryData();
/*  249: 457 */     writeSummaryData();
/*  250: 458 */     writeAdditionalPropertySets();
/*  251: 459 */     writeSmallBlockDepot();
/*  252: 460 */     writeSmallBlockDepotChain();
/*  253: 461 */     writeBigBlockDepot();
/*  254: 462 */     writePropertySets();
/*  255:     */   }
/*  256:     */   
/*  257:     */   private void writeAdditionalPropertySets()
/*  258:     */     throws IOException
/*  259:     */   {
/*  260: 473 */     if (this.additionalPropertySets == null) {
/*  261: 475 */       return;
/*  262:     */     }
/*  263: 478 */     for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  264:     */     {
/*  265: 480 */       ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  266: 481 */       byte[] data = rps.data;
/*  267: 483 */       if (data.length > 4096)
/*  268:     */       {
/*  269: 485 */         int numBlocks = getBigBlocksRequired(data.length);
/*  270: 486 */         int requiredSize = numBlocks * 512;
/*  271:     */         
/*  272: 488 */         this.out.write(data, 0, data.length);
/*  273:     */         
/*  274: 490 */         byte[] padding = new byte[requiredSize - data.length];
/*  275: 491 */         this.out.write(padding, 0, padding.length);
/*  276:     */       }
/*  277:     */     }
/*  278:     */   }
/*  279:     */   
/*  280:     */   private void writeExcelData()
/*  281:     */     throws IOException
/*  282:     */   {
/*  283: 505 */     this.excelData.writeData(this.out);
/*  284:     */     
/*  285: 507 */     byte[] padding = new byte[this.requiredSize - this.size];
/*  286: 508 */     this.out.write(padding);
/*  287:     */   }
/*  288:     */   
/*  289:     */   private void writeDocumentSummaryData()
/*  290:     */     throws IOException
/*  291:     */   {
/*  292: 518 */     byte[] padding = new byte[4096];
/*  293:     */     
/*  294:     */ 
/*  295: 521 */     this.out.write(padding);
/*  296:     */   }
/*  297:     */   
/*  298:     */   private void writeSummaryData()
/*  299:     */     throws IOException
/*  300:     */   {
/*  301: 531 */     byte[] padding = new byte[4096];
/*  302:     */     
/*  303:     */ 
/*  304: 534 */     this.out.write(padding);
/*  305:     */   }
/*  306:     */   
/*  307:     */   private void writeHeader()
/*  308:     */     throws IOException
/*  309:     */   {
/*  310: 545 */     byte[] headerBlock = new byte[512];
/*  311: 546 */     byte[] extensionBlockData = new byte[512 * this.numExtensionBlocks];
/*  312:     */     
/*  313:     */ 
/*  314: 549 */     System.arraycopy(IDENTIFIER, 0, headerBlock, 0, IDENTIFIER.length);
/*  315:     */     
/*  316:     */ 
/*  317: 552 */     headerBlock[24] = 62;
/*  318: 553 */     headerBlock[26] = 3;
/*  319: 554 */     headerBlock[28] = -2;
/*  320: 555 */     headerBlock[29] = -1;
/*  321: 556 */     headerBlock[30] = 9;
/*  322: 557 */     headerBlock[32] = 6;
/*  323: 558 */     headerBlock[57] = 16;
/*  324:     */     
/*  325:     */ 
/*  326: 561 */     IntegerHelper.getFourBytes(this.numBigBlockDepotBlocks, 
/*  327: 562 */       headerBlock, 
/*  328: 563 */       44);
/*  329:     */     
/*  330:     */ 
/*  331: 566 */     IntegerHelper.getFourBytes(this.sbdStartBlockChain, 
/*  332: 567 */       headerBlock, 
/*  333: 568 */       60);
/*  334:     */     
/*  335:     */ 
/*  336: 571 */     IntegerHelper.getFourBytes(this.numSmallBlockDepotChainBlocks, 
/*  337: 572 */       headerBlock, 
/*  338: 573 */       64);
/*  339:     */     
/*  340:     */ 
/*  341: 576 */     IntegerHelper.getFourBytes(this.extensionBlock, 
/*  342: 577 */       headerBlock, 
/*  343: 578 */       68);
/*  344:     */     
/*  345:     */ 
/*  346: 581 */     IntegerHelper.getFourBytes(this.numExtensionBlocks, 
/*  347: 582 */       headerBlock, 
/*  348: 583 */       72);
/*  349:     */     
/*  350:     */ 
/*  351: 586 */     IntegerHelper.getFourBytes(this.rootStartBlock, 
/*  352: 587 */       headerBlock, 
/*  353: 588 */       48);
/*  354:     */     
/*  355:     */ 
/*  356:     */ 
/*  357: 592 */     int pos = 76;
/*  358:     */     
/*  359:     */ 
/*  360: 595 */     int blocksToWrite = Math.min(this.numBigBlockDepotBlocks, 
/*  361: 596 */       109);
/*  362:     */     
/*  363: 598 */     int blocksWritten = 0;
/*  364: 600 */     for (int i = 0; i < blocksToWrite; i++)
/*  365:     */     {
/*  366: 602 */       IntegerHelper.getFourBytes(this.bbdStartBlock + i, 
/*  367: 603 */         headerBlock, 
/*  368: 604 */         pos);
/*  369: 605 */       pos += 4;
/*  370: 606 */       blocksWritten++;
/*  371:     */     }
/*  372: 610 */     for (int i = pos; i < 512; i++) {
/*  373: 612 */       headerBlock[i] = -1;
/*  374:     */     }
/*  375: 615 */     this.out.write(headerBlock);
/*  376:     */     
/*  377:     */ 
/*  378: 618 */     pos = 0;
/*  379: 620 */     for (int extBlock = 0; extBlock < this.numExtensionBlocks; extBlock++)
/*  380:     */     {
/*  381: 622 */       blocksToWrite = Math.min(this.numBigBlockDepotBlocks - blocksWritten, 
/*  382: 623 */         127);
/*  383: 625 */       for (int j = 0; j < blocksToWrite; j++)
/*  384:     */       {
/*  385: 627 */         IntegerHelper.getFourBytes(this.bbdStartBlock + blocksWritten + j, 
/*  386: 628 */           extensionBlockData, 
/*  387: 629 */           pos);
/*  388: 630 */         pos += 4;
/*  389:     */       }
/*  390: 633 */       blocksWritten += blocksToWrite;
/*  391:     */       
/*  392:     */ 
/*  393: 636 */       int nextBlock = blocksWritten == this.numBigBlockDepotBlocks ? 
/*  394: 637 */         -2 : extBlock + 1;
/*  395: 638 */       IntegerHelper.getFourBytes(nextBlock, extensionBlockData, pos);
/*  396: 639 */       pos += 4;
/*  397:     */     }
/*  398: 642 */     if (this.numExtensionBlocks > 0)
/*  399:     */     {
/*  400: 645 */       for (int i = pos; i < extensionBlockData.length; i++) {
/*  401: 647 */         extensionBlockData[i] = -1;
/*  402:     */       }
/*  403: 650 */       this.out.write(extensionBlockData);
/*  404:     */     }
/*  405:     */   }
/*  406:     */   
/*  407:     */   private void checkBbdPos()
/*  408:     */     throws IOException
/*  409:     */   {
/*  410: 662 */     if (this.bbdPos >= 512)
/*  411:     */     {
/*  412: 665 */       this.out.write(this.bigBlockDepot);
/*  413:     */       
/*  414:     */ 
/*  415: 668 */       this.bigBlockDepot = new byte[512];
/*  416: 669 */       this.bbdPos = 0;
/*  417:     */     }
/*  418:     */   }
/*  419:     */   
/*  420:     */   private void writeBlockChain(int startBlock, int numBlocks)
/*  421:     */     throws IOException
/*  422:     */   {
/*  423: 683 */     int blocksToWrite = numBlocks - 1;
/*  424: 684 */     int blockNumber = startBlock + 1;
/*  425: 686 */     while (blocksToWrite > 0)
/*  426:     */     {
/*  427: 688 */       int bbdBlocks = Math.min(blocksToWrite, (512 - this.bbdPos) / 4);
/*  428: 690 */       for (int i = 0; i < bbdBlocks; i++)
/*  429:     */       {
/*  430: 692 */         IntegerHelper.getFourBytes(blockNumber, this.bigBlockDepot, this.bbdPos);
/*  431: 693 */         this.bbdPos += 4;
/*  432: 694 */         blockNumber++;
/*  433:     */       }
/*  434: 697 */       blocksToWrite -= bbdBlocks;
/*  435: 698 */       checkBbdPos();
/*  436:     */     }
/*  437: 702 */     IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
/*  438: 703 */     this.bbdPos += 4;
/*  439: 704 */     checkBbdPos();
/*  440:     */   }
/*  441:     */   
/*  442:     */   private void writeAdditionalPropertySetBlockChains()
/*  443:     */     throws IOException
/*  444:     */   {
/*  445: 714 */     if (this.additionalPropertySets == null) {
/*  446: 716 */       return;
/*  447:     */     }
/*  448: 719 */     int blockNumber = this.excelDataStartBlock + this.excelDataBlocks + 16;
/*  449: 720 */     for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  450:     */     {
/*  451: 722 */       ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  452: 723 */       if (rps.data.length > 4096)
/*  453:     */       {
/*  454: 725 */         int numBlocks = getBigBlocksRequired(rps.data.length);
/*  455:     */         
/*  456: 727 */         writeBlockChain(blockNumber, numBlocks);
/*  457: 728 */         blockNumber += numBlocks;
/*  458:     */       }
/*  459:     */     }
/*  460:     */   }
/*  461:     */   
/*  462:     */   private void writeSmallBlockDepotChain()
/*  463:     */     throws IOException
/*  464:     */   {
/*  465: 738 */     if (this.sbdStartBlockChain == -2) {
/*  466: 740 */       return;
/*  467:     */     }
/*  468: 743 */     byte[] smallBlockDepotChain = 
/*  469: 744 */       new byte[this.numSmallBlockDepotChainBlocks * 512];
/*  470:     */     
/*  471: 746 */     int pos = 0;
/*  472: 747 */     int sbdBlockNumber = 1;
/*  473: 749 */     for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  474:     */     {
/*  475: 751 */       ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  476: 753 */       if ((rps.data.length <= 4096) && 
/*  477: 754 */         (rps.data.length != 0))
/*  478:     */       {
/*  479: 756 */         int numSmallBlocks = getSmallBlocksRequired(rps.data.length);
/*  480: 757 */         for (int j = 0; j < numSmallBlocks - 1; j++)
/*  481:     */         {
/*  482: 759 */           IntegerHelper.getFourBytes(sbdBlockNumber, 
/*  483: 760 */             smallBlockDepotChain, 
/*  484: 761 */             pos);
/*  485: 762 */           pos += 4;
/*  486: 763 */           sbdBlockNumber++;
/*  487:     */         }
/*  488: 767 */         IntegerHelper.getFourBytes(-2, smallBlockDepotChain, pos);
/*  489: 768 */         pos += 4;
/*  490: 769 */         sbdBlockNumber++;
/*  491:     */       }
/*  492:     */     }
/*  493: 773 */     this.out.write(smallBlockDepotChain);
/*  494:     */   }
/*  495:     */   
/*  496:     */   private void writeSmallBlockDepot()
/*  497:     */     throws IOException
/*  498:     */   {
/*  499: 783 */     if (this.additionalPropertySets == null) {
/*  500: 785 */       return;
/*  501:     */     }
/*  502: 788 */     byte[] smallBlockDepot = 
/*  503: 789 */       new byte[this.numSmallBlockDepotBlocks * 512];
/*  504:     */     
/*  505: 791 */     int pos = 0;
/*  506: 793 */     for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  507:     */     {
/*  508: 795 */       ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  509: 797 */       if (rps.data.length <= 4096)
/*  510:     */       {
/*  511: 799 */         int smallBlocks = getSmallBlocksRequired(rps.data.length);
/*  512: 800 */         int length = smallBlocks * 64;
/*  513: 801 */         System.arraycopy(rps.data, 0, smallBlockDepot, pos, rps.data.length);
/*  514: 802 */         pos += length;
/*  515:     */       }
/*  516:     */     }
/*  517: 806 */     this.out.write(smallBlockDepot);
/*  518:     */   }
/*  519:     */   
/*  520:     */   private void writeBigBlockDepot()
/*  521:     */     throws IOException
/*  522:     */   {
/*  523: 818 */     this.bigBlockDepot = new byte[512];
/*  524: 819 */     this.bbdPos = 0;
/*  525: 822 */     for (int i = 0; i < this.numExtensionBlocks; i++)
/*  526:     */     {
/*  527: 824 */       IntegerHelper.getFourBytes(-3, this.bigBlockDepot, this.bbdPos);
/*  528: 825 */       this.bbdPos += 4;
/*  529: 826 */       checkBbdPos();
/*  530:     */     }
/*  531: 829 */     writeBlockChain(this.excelDataStartBlock, this.excelDataBlocks);
/*  532:     */     
/*  533:     */ 
/*  534:     */ 
/*  535:     */ 
/*  536: 834 */     int summaryInfoBlock = this.excelDataStartBlock + 
/*  537: 835 */       this.excelDataBlocks + 
/*  538: 836 */       this.additionalPropertyBlocks;
/*  539: 838 */     for (int i = summaryInfoBlock; i < summaryInfoBlock + 7; i++)
/*  540:     */     {
/*  541: 840 */       IntegerHelper.getFourBytes(i + 1, this.bigBlockDepot, this.bbdPos);
/*  542: 841 */       this.bbdPos += 4;
/*  543: 842 */       checkBbdPos();
/*  544:     */     }
/*  545: 846 */     IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
/*  546: 847 */     this.bbdPos += 4;
/*  547: 848 */     checkBbdPos();
/*  548: 851 */     for (int i = summaryInfoBlock + 8; i < summaryInfoBlock + 15; i++)
/*  549:     */     {
/*  550: 853 */       IntegerHelper.getFourBytes(i + 1, this.bigBlockDepot, this.bbdPos);
/*  551: 854 */       this.bbdPos += 4;
/*  552: 855 */       checkBbdPos();
/*  553:     */     }
/*  554: 859 */     IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
/*  555: 860 */     this.bbdPos += 4;
/*  556: 861 */     checkBbdPos();
/*  557:     */     
/*  558:     */ 
/*  559: 864 */     writeAdditionalPropertySetBlockChains();
/*  560: 866 */     if (this.sbdStartBlock != -2)
/*  561:     */     {
/*  562: 869 */       writeBlockChain(this.sbdStartBlock, this.numSmallBlockDepotBlocks);
/*  563:     */       
/*  564:     */ 
/*  565: 872 */       writeBlockChain(this.sbdStartBlockChain, this.numSmallBlockDepotChainBlocks);
/*  566:     */     }
/*  567: 877 */     for (int i = 0; i < this.numBigBlockDepotBlocks; i++)
/*  568:     */     {
/*  569: 879 */       IntegerHelper.getFourBytes(-3, this.bigBlockDepot, this.bbdPos);
/*  570: 880 */       this.bbdPos += 4;
/*  571: 881 */       checkBbdPos();
/*  572:     */     }
/*  573: 885 */     writeBlockChain(this.rootStartBlock, this.numRootEntryBlocks);
/*  574: 888 */     if (this.bbdPos != 0)
/*  575:     */     {
/*  576: 890 */       for (int i = this.bbdPos; i < 512; i++) {
/*  577: 892 */         this.bigBlockDepot[i] = -1;
/*  578:     */       }
/*  579: 894 */       this.out.write(this.bigBlockDepot);
/*  580:     */     }
/*  581:     */   }
/*  582:     */   
/*  583:     */   private int getBigBlocksRequired(int length)
/*  584:     */   {
/*  585: 907 */     int blocks = length / 512;
/*  586:     */     
/*  587: 909 */     return length % 512 > 0 ? blocks + 1 : blocks;
/*  588:     */   }
/*  589:     */   
/*  590:     */   private int getSmallBlocksRequired(int length)
/*  591:     */   {
/*  592: 921 */     int blocks = length / 64;
/*  593:     */     
/*  594: 923 */     return length % 64 > 0 ? blocks + 1 : blocks;
/*  595:     */   }
/*  596:     */   
/*  597:     */   private void writePropertySets()
/*  598:     */     throws IOException
/*  599:     */   {
/*  600: 933 */     byte[] propertySetStorage = new byte[512 * this.numRootEntryBlocks];
/*  601:     */     
/*  602: 935 */     int pos = 0;
/*  603: 936 */     int[] mappings = (int[])null;
/*  604: 939 */     if (this.additionalPropertySets != null)
/*  605:     */     {
/*  606: 941 */       mappings = new int[this.numPropertySets];
/*  607: 944 */       for (int i = 0; i < STANDARD_PROPERTY_SETS.length; i++)
/*  608:     */       {
/*  609: 946 */         ReadPropertyStorage rps = 
/*  610: 947 */           (ReadPropertyStorage)this.standardPropertySets.get(STANDARD_PROPERTY_SETS[i]);
/*  611: 949 */         if (rps != null) {
/*  612: 951 */           mappings[rps.number] = i;
/*  613:     */         } else {
/*  614: 955 */           logger.warn("Standard property set " + STANDARD_PROPERTY_SETS[i] + 
/*  615: 956 */             " not present in source file");
/*  616:     */         }
/*  617:     */       }
/*  618: 961 */       int newMapping = STANDARD_PROPERTY_SETS.length;
/*  619: 962 */       for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  620:     */       {
/*  621: 964 */         ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  622: 965 */         mappings[rps.number] = newMapping;
/*  623: 966 */         newMapping++;
/*  624:     */       }
/*  625:     */     }
/*  626: 970 */     int child = 0;
/*  627: 971 */     int previous = 0;
/*  628: 972 */     int next = 0;
/*  629:     */     
/*  630:     */ 
/*  631: 975 */     int size = 0;
/*  632: 977 */     if (this.additionalPropertySets != null)
/*  633:     */     {
/*  634: 980 */       size += getBigBlocksRequired(this.requiredSize) * 512;
/*  635:     */       
/*  636:     */ 
/*  637: 983 */       size += getBigBlocksRequired(4096) * 512;
/*  638: 984 */       size += getBigBlocksRequired(4096) * 512;
/*  639: 987 */       for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  640:     */       {
/*  641: 989 */         ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  642: 990 */         if (rps.propertyStorage.type != 1) {
/*  643: 992 */           if (rps.propertyStorage.size >= 4096) {
/*  644: 995 */             size = size + getBigBlocksRequired(rps.propertyStorage.size) * 512;
/*  645:     */           } else {
/*  646:1000 */             size = size + getSmallBlocksRequired(rps.propertyStorage.size) * 64;
/*  647:     */           }
/*  648:     */         }
/*  649:     */       }
/*  650:     */     }
/*  651:1007 */     BaseCompoundFile.PropertyStorage ps = new BaseCompoundFile.PropertyStorage(this, "Root Entry");
/*  652:1008 */     ps.setType(5);
/*  653:1009 */     ps.setStartBlock(this.sbdStartBlock);
/*  654:1010 */     ps.setSize(size);
/*  655:1011 */     ps.setPrevious(-1);
/*  656:1012 */     ps.setNext(-1);
/*  657:1013 */     ps.setColour(0);
/*  658:     */     
/*  659:1015 */     child = 1;
/*  660:1016 */     if (this.additionalPropertySets != null)
/*  661:     */     {
/*  662:1018 */       ReadPropertyStorage rps = 
/*  663:1019 */         (ReadPropertyStorage)this.standardPropertySets.get("Root Entry");
/*  664:1020 */       child = mappings[rps.propertyStorage.child];
/*  665:     */     }
/*  666:1022 */     ps.setChild(child);
/*  667:     */     
/*  668:1024 */     System.arraycopy(ps.data, 0, 
/*  669:1025 */       propertySetStorage, pos, 
/*  670:1026 */       128);
/*  671:1027 */     pos += 128;
/*  672:     */     
/*  673:     */ 
/*  674:     */ 
/*  675:1031 */     ps = new BaseCompoundFile.PropertyStorage(this, "Workbook");
/*  676:1032 */     ps.setType(2);
/*  677:1033 */     ps.setStartBlock(this.excelDataStartBlock);
/*  678:     */     
/*  679:1035 */     ps.setSize(this.requiredSize);
/*  680:     */     
/*  681:     */ 
/*  682:     */ 
/*  683:1039 */     previous = 3;
/*  684:1040 */     next = -1;
/*  685:1042 */     if (this.additionalPropertySets != null)
/*  686:     */     {
/*  687:1044 */       ReadPropertyStorage rps = 
/*  688:1045 */         (ReadPropertyStorage)this.standardPropertySets.get("Workbook");
/*  689:1046 */       previous = rps.propertyStorage.previous != -1 ? 
/*  690:1047 */         mappings[rps.propertyStorage.previous] : -1;
/*  691:1048 */       next = rps.propertyStorage.next != -1 ? 
/*  692:1049 */         mappings[rps.propertyStorage.next] : -1;
/*  693:     */     }
/*  694:1052 */     ps.setPrevious(previous);
/*  695:1053 */     ps.setNext(next);
/*  696:1054 */     ps.setChild(-1);
/*  697:     */     
/*  698:1056 */     System.arraycopy(ps.data, 0, 
/*  699:1057 */       propertySetStorage, pos, 
/*  700:1058 */       128);
/*  701:1059 */     pos += 128;
/*  702:     */     
/*  703:     */ 
/*  704:1062 */     ps = new BaseCompoundFile.PropertyStorage(this, "\005SummaryInformation");
/*  705:1063 */     ps.setType(2);
/*  706:1064 */     ps.setStartBlock(this.excelDataStartBlock + this.excelDataBlocks);
/*  707:1065 */     ps.setSize(4096);
/*  708:     */     
/*  709:1067 */     previous = 1;
/*  710:1068 */     next = 3;
/*  711:1070 */     if (this.additionalPropertySets != null)
/*  712:     */     {
/*  713:1072 */       ReadPropertyStorage rps = 
/*  714:1073 */         (ReadPropertyStorage)this.standardPropertySets.get("\005SummaryInformation");
/*  715:1075 */       if (rps != null)
/*  716:     */       {
/*  717:1077 */         previous = rps.propertyStorage.previous != -1 ? 
/*  718:1078 */           mappings[rps.propertyStorage.previous] : -1;
/*  719:1079 */         next = rps.propertyStorage.next != -1 ? 
/*  720:1080 */           mappings[rps.propertyStorage.next] : -1;
/*  721:     */       }
/*  722:     */     }
/*  723:1084 */     ps.setPrevious(previous);
/*  724:1085 */     ps.setNext(next);
/*  725:1086 */     ps.setChild(-1);
/*  726:     */     
/*  727:1088 */     System.arraycopy(ps.data, 0, 
/*  728:1089 */       propertySetStorage, pos, 
/*  729:1090 */       128);
/*  730:1091 */     pos += 128;
/*  731:     */     
/*  732:     */ 
/*  733:1094 */     ps = new BaseCompoundFile.PropertyStorage(this, "\005DocumentSummaryInformation");
/*  734:1095 */     ps.setType(2);
/*  735:1096 */     ps.setStartBlock(this.excelDataStartBlock + this.excelDataBlocks + 8);
/*  736:1097 */     ps.setSize(4096);
/*  737:1098 */     ps.setPrevious(-1);
/*  738:1099 */     ps.setNext(-1);
/*  739:1100 */     ps.setChild(-1);
/*  740:     */     
/*  741:1102 */     System.arraycopy(ps.data, 0, 
/*  742:1103 */       propertySetStorage, pos, 
/*  743:1104 */       128);
/*  744:1105 */     pos += 128;
/*  745:1110 */     if (this.additionalPropertySets == null)
/*  746:     */     {
/*  747:1112 */       this.out.write(propertySetStorage);
/*  748:1113 */       return;
/*  749:     */     }
/*  750:1116 */     int bigBlock = this.excelDataStartBlock + this.excelDataBlocks + 16;
/*  751:1117 */     int smallBlock = 0;
/*  752:1119 */     for (Iterator i = this.additionalPropertySets.iterator(); i.hasNext();)
/*  753:     */     {
/*  754:1121 */       ReadPropertyStorage rps = (ReadPropertyStorage)i.next();
/*  755:     */       
/*  756:1123 */       int block = rps.data.length > 4096 ? 
/*  757:1124 */         bigBlock : smallBlock;
/*  758:     */       
/*  759:1126 */       ps = new BaseCompoundFile.PropertyStorage(this, rps.propertyStorage.name);
/*  760:1127 */       ps.setType(rps.propertyStorage.type);
/*  761:1128 */       ps.setStartBlock(block);
/*  762:1129 */       ps.setSize(rps.propertyStorage.size);
/*  763:     */       
/*  764:     */ 
/*  765:1132 */       previous = rps.propertyStorage.previous != -1 ? 
/*  766:1133 */         mappings[rps.propertyStorage.previous] : -1;
/*  767:1134 */       next = rps.propertyStorage.next != -1 ? 
/*  768:1135 */         mappings[rps.propertyStorage.next] : -1;
/*  769:1136 */       child = rps.propertyStorage.child != -1 ? 
/*  770:1137 */         mappings[rps.propertyStorage.child] : -1;
/*  771:     */       
/*  772:1139 */       ps.setPrevious(previous);
/*  773:1140 */       ps.setNext(next);
/*  774:1141 */       ps.setChild(child);
/*  775:     */       
/*  776:1143 */       System.arraycopy(ps.data, 0, 
/*  777:1144 */         propertySetStorage, pos, 
/*  778:1145 */         128);
/*  779:1146 */       pos += 128;
/*  780:1148 */       if (rps.data.length > 4096) {
/*  781:1150 */         bigBlock += getBigBlocksRequired(rps.data.length);
/*  782:     */       } else {
/*  783:1154 */         smallBlock += getSmallBlocksRequired(rps.data.length);
/*  784:     */       }
/*  785:     */     }
/*  786:1158 */     this.out.write(propertySetStorage);
/*  787:     */   }
/*  788:     */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-jxl\ken-jxl.jar
 * Qualified Name:     jxl.write.biff.CompoundFile
 * JD-Core Version:    0.7.0.1
 */