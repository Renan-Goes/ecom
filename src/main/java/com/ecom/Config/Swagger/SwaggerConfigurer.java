package com.ecom.Config.Swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("E-commerce API")
        .description("API for generic e-commerce website")
        .version("1.0.0")
        .contact(new Contact("Renan Goes", "https://github.com/Renan-Goes/", "renanagmelo@gmail.com"))
        .build();
  }
  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.ant("/**"))
        .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(
            new SecurityReference("Token Access", authorizationScopes));
    }


  @Bean
  public Docket swagger() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any()).build().useDefaultResponseMessages(false).apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .securitySchemes(Arrays.asList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
        .securityContexts(Arrays.asList(securityContext()));
  }

}
