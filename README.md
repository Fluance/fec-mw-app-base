## Known issues
### Spring Boot 1.3.0.RELEASE not compatible with Spring Framework 4.2.2 
#### Issue
IllegalAccessError: tried to access method org.springframework.core.convert.support.DefaultConversionService.addCollectionConverters(Lorg/springframework/core/convert/converter/ConverterRegistry;)V from class org.springframework.boot.bind.RelaxedConversionService
#### Fix
Upgrade to Spring Framework 4.2.3