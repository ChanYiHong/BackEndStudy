package hcy.typeconverter;

import hcy.typeconverter.converter.IntegerToStringConverter;
import hcy.typeconverter.converter.IpPortToStringConverter;
import hcy.typeconverter.converter.StringToIntegerConverter;
import hcy.typeconverter.converter.StringToIpPortConverter;
import hcy.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 스프링 내부에서 사용하는 ConversionService에 컨버터를 추가해 준다.
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
        
        registry.addFormatter(new MyNumberFormatter());
    }
}
