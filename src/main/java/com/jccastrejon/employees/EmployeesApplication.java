package com.jccastrejon.employees;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import com.google.common.base.Predicates;

import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class EmployeesApplication {

	@Autowired
    BuildProperties buildProperties;

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper returnValue;
		Provider<LocalDate> localDateProvider;
		Converter<String, LocalDate> stringDateConverter;

		returnValue = new ModelMapper();
		localDateProvider = new AbstractProvider<LocalDate>() {
			@Override
			public LocalDate get() {
				return LocalDate.now();
			}
		};
	
		stringDateConverter = new AbstractConverter<String, LocalDate>() {
			@Override
			protected LocalDate convert(String source) {
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate localDate = LocalDate.parse(source, format);
				return localDate;
			}
		};
	
	
		returnValue.createTypeMap(String.class, LocalDate.class);
		returnValue.addConverter(stringDateConverter);
		returnValue.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);

		return returnValue;
	}

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))              
          .paths(PathSelectors.any())                          
		  .build()
		  .apiInfo(apiInfo());                                           
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo(
		  "Employees API", 
		  buildProperties.getName(), 
		  buildProperties.getVersion(),
		  "",
		  new Contact("Juan Castrejón", "https//jccastrejon.com", "jccastrejon@gmail.com"), 
		  "", "", Collections.emptyList());
	}
}
