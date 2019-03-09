package com.cloud.protectedservice;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableDiscoveryClient
@SpringBootApplication
public class ProtectedServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProtectedServiceApplication.class, args);
	}
	
	@Bean
	public Docket api() throws IOException, XmlPullParserException {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = reader.read(new FileReader("pom.xml"));
		Collection<VendorExtension> vendorExtesions = new ArrayList<VendorExtension>();
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.cloud.protectedservice"))
				.paths(PathSelectors.any())
				.build().apiInfo(new ApiInfo("Account Service Api Documentation", "Documentation automatically generated", model.getParent().getVersion(), null, new Contact("Marius Iancu", "-", "marius.danut94@gmail.com"), null, null, vendorExtesions));
	}
}

