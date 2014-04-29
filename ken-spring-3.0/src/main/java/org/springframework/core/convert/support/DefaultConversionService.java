/*   1:    */ package org.springframework.core.convert.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.core.convert.ConversionService;
/*   5:    */ import org.springframework.core.convert.converter.ConverterRegistry;
/*   6:    */ 
/*   7:    */ public class DefaultConversionService
/*   8:    */   extends GenericConversionService
/*   9:    */ {
/*  10:    */   public DefaultConversionService()
/*  11:    */   {
/*  12: 42 */     addDefaultConverters(this);
/*  13:    */   }
/*  14:    */   
/*  15:    */   public static void addDefaultConverters(ConverterRegistry converterRegistry)
/*  16:    */   {
/*  17: 53 */     addScalarConverters(converterRegistry);
/*  18: 54 */     addCollectionConverters(converterRegistry);
/*  19: 55 */     addFallbackConverters(converterRegistry);
/*  20:    */   }
/*  21:    */   
/*  22:    */   private static void addScalarConverters(ConverterRegistry converterRegistry)
/*  23:    */   {
/*  24: 61 */     converterRegistry.addConverter(new StringToBooleanConverter());
/*  25: 62 */     converterRegistry.addConverter(Boolean.class, String.class, new ObjectToStringConverter());
/*  26:    */     
/*  27: 64 */     converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
/*  28: 65 */     converterRegistry.addConverter(Number.class, String.class, new ObjectToStringConverter());
/*  29:    */     
/*  30: 67 */     converterRegistry.addConverterFactory(new NumberToNumberConverterFactory());
/*  31:    */     
/*  32: 69 */     converterRegistry.addConverter(new StringToCharacterConverter());
/*  33: 70 */     converterRegistry.addConverter(Character.class, String.class, new ObjectToStringConverter());
/*  34:    */     
/*  35: 72 */     converterRegistry.addConverter(new NumberToCharacterConverter());
/*  36: 73 */     converterRegistry.addConverterFactory(new CharacterToNumberFactory());
/*  37:    */     
/*  38: 75 */     converterRegistry.addConverterFactory(new StringToEnumConverterFactory());
/*  39: 76 */     converterRegistry.addConverter(Enum.class, String.class, new EnumToStringConverter());
/*  40:    */     
/*  41: 78 */     converterRegistry.addConverter(new StringToLocaleConverter());
/*  42: 79 */     converterRegistry.addConverter(Locale.class, String.class, new ObjectToStringConverter());
/*  43:    */     
/*  44: 81 */     converterRegistry.addConverter(new PropertiesToStringConverter());
/*  45: 82 */     converterRegistry.addConverter(new StringToPropertiesConverter());
/*  46:    */   }
/*  47:    */   
/*  48:    */   private static void addCollectionConverters(ConverterRegistry converterRegistry)
/*  49:    */   {
/*  50: 86 */     ConversionService conversionService = (ConversionService)converterRegistry;
/*  51: 87 */     converterRegistry.addConverter(new ArrayToCollectionConverter(conversionService));
/*  52: 88 */     converterRegistry.addConverter(new CollectionToArrayConverter(conversionService));
/*  53:    */     
/*  54: 90 */     converterRegistry.addConverter(new ArrayToArrayConverter(conversionService));
/*  55: 91 */     converterRegistry.addConverter(new CollectionToCollectionConverter(conversionService));
/*  56: 92 */     converterRegistry.addConverter(new MapToMapConverter(conversionService));
/*  57:    */     
/*  58: 94 */     converterRegistry.addConverter(new ArrayToStringConverter(conversionService));
/*  59: 95 */     converterRegistry.addConverter(new StringToArrayConverter(conversionService));
/*  60:    */     
/*  61: 97 */     converterRegistry.addConverter(new ArrayToObjectConverter(conversionService));
/*  62: 98 */     converterRegistry.addConverter(new ObjectToArrayConverter(conversionService));
/*  63:    */     
/*  64:100 */     converterRegistry.addConverter(new CollectionToStringConverter(conversionService));
/*  65:101 */     converterRegistry.addConverter(new StringToCollectionConverter(conversionService));
/*  66:    */     
/*  67:103 */     converterRegistry.addConverter(new CollectionToObjectConverter(conversionService));
/*  68:104 */     converterRegistry.addConverter(new ObjectToCollectionConverter(conversionService));
/*  69:    */   }
/*  70:    */   
/*  71:    */   private static void addFallbackConverters(ConverterRegistry converterRegistry)
/*  72:    */   {
/*  73:108 */     ConversionService conversionService = (ConversionService)converterRegistry;
/*  74:109 */     converterRegistry.addConverter(new ObjectToObjectConverter());
/*  75:110 */     converterRegistry.addConverter(new IdToEntityConverter(conversionService));
/*  76:111 */     converterRegistry.addConverter(new FallbackObjectToStringConverter());
/*  77:    */   }
/*  78:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.support.DefaultConversionService
 * JD-Core Version:    0.7.0.1
 */