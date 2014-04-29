/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.Collection;
/*   6:    */ import java.util.List;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.w3c.dom.CharacterData;
/*   9:    */ import org.w3c.dom.Comment;
/*  10:    */ import org.w3c.dom.Element;
/*  11:    */ import org.w3c.dom.EntityReference;
/*  12:    */ import org.w3c.dom.Node;
/*  13:    */ import org.w3c.dom.NodeList;
/*  14:    */ import org.xml.sax.ContentHandler;
/*  15:    */ 
/*  16:    */ public abstract class DomUtils
/*  17:    */ {
/*  18:    */   public static List<Element> getChildElementsByTagName(Element ele, String[] childEleNames)
/*  19:    */   {
/*  20: 60 */     Assert.notNull(ele, "Element must not be null");
/*  21: 61 */     Assert.notNull(childEleNames, "Element names collection must not be null");
/*  22: 62 */     List<String> childEleNameList = Arrays.asList(childEleNames);
/*  23: 63 */     NodeList nl = ele.getChildNodes();
/*  24: 64 */     List<Element> childEles = new ArrayList();
/*  25: 65 */     for (int i = 0; i < nl.getLength(); i++)
/*  26:    */     {
/*  27: 66 */       Node node = nl.item(i);
/*  28: 67 */       if (((node instanceof Element)) && (nodeNameMatch(node, childEleNameList))) {
/*  29: 68 */         childEles.add((Element)node);
/*  30:    */       }
/*  31:    */     }
/*  32: 71 */     return childEles;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static List<Element> getChildElementsByTagName(Element ele, String childEleName)
/*  36:    */   {
/*  37: 86 */     return getChildElementsByTagName(ele, new String[] { childEleName });
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static Element getChildElementByTagName(Element ele, String childEleName)
/*  41:    */   {
/*  42: 97 */     Assert.notNull(ele, "Element must not be null");
/*  43: 98 */     Assert.notNull(childEleName, "Element name must not be null");
/*  44: 99 */     NodeList nl = ele.getChildNodes();
/*  45:100 */     for (int i = 0; i < nl.getLength(); i++)
/*  46:    */     {
/*  47:101 */       Node node = nl.item(i);
/*  48:102 */       if (((node instanceof Element)) && (nodeNameMatch(node, childEleName))) {
/*  49:103 */         return (Element)node;
/*  50:    */       }
/*  51:    */     }
/*  52:106 */     return null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static String getChildElementValueByTagName(Element ele, String childEleName)
/*  56:    */   {
/*  57:117 */     Element child = getChildElementByTagName(ele, childEleName);
/*  58:118 */     return child != null ? getTextValue(child) : null;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public static List<Element> getChildElements(Element ele)
/*  62:    */   {
/*  63:128 */     Assert.notNull(ele, "Element must not be null");
/*  64:129 */     NodeList nl = ele.getChildNodes();
/*  65:130 */     List<Element> childEles = new ArrayList();
/*  66:131 */     for (int i = 0; i < nl.getLength(); i++)
/*  67:    */     {
/*  68:132 */       Node node = nl.item(i);
/*  69:133 */       if ((node instanceof Element)) {
/*  70:134 */         childEles.add((Element)node);
/*  71:    */       }
/*  72:    */     }
/*  73:137 */     return childEles;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public static String getTextValue(Element valueEle)
/*  77:    */   {
/*  78:149 */     Assert.notNull(valueEle, "Element must not be null");
/*  79:150 */     StringBuilder sb = new StringBuilder();
/*  80:151 */     NodeList nl = valueEle.getChildNodes();
/*  81:152 */     for (int i = 0; i < nl.getLength(); i++)
/*  82:    */     {
/*  83:153 */       Node item = nl.item(i);
/*  84:154 */       if ((((item instanceof CharacterData)) && (!(item instanceof Comment))) || ((item instanceof EntityReference))) {
/*  85:155 */         sb.append(item.getNodeValue());
/*  86:    */       }
/*  87:    */     }
/*  88:158 */     return sb.toString();
/*  89:    */   }
/*  90:    */   
/*  91:    */   public static boolean nodeNameEquals(Node node, String desiredName)
/*  92:    */   {
/*  93:166 */     Assert.notNull(node, "Node must not be null");
/*  94:167 */     Assert.notNull(desiredName, "Desired name must not be null");
/*  95:168 */     return nodeNameMatch(node, desiredName);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public static ContentHandler createContentHandler(Node node)
/*  99:    */   {
/* 100:178 */     return new DomContentHandler(node);
/* 101:    */   }
/* 102:    */   
/* 103:    */   private static boolean nodeNameMatch(Node node, String desiredName)
/* 104:    */   {
/* 105:183 */     return (desiredName.equals(node.getNodeName())) || (desiredName.equals(node.getLocalName()));
/* 106:    */   }
/* 107:    */   
/* 108:    */   private static boolean nodeNameMatch(Node node, Collection desiredNames)
/* 109:    */   {
/* 110:188 */     return (desiredNames.contains(node.getNodeName())) || (desiredNames.contains(node.getLocalName()));
/* 111:    */   }
/* 112:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.DomUtils
 * JD-Core Version:    0.7.0.1
 */