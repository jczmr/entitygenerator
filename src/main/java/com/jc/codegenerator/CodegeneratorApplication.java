package com.jc.codegenerator;

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodegeneratorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CodegeneratorApplication.class, args);
		
		GenerateEntity generateEntity  = new GenerateEntity();
		String sourcePath = "src/main/java/"; // This path could vary depending on your project structure
		String classFileName = "MyNewClass";
		String dataFile = "src/main/resources/table_columns.txt"; // This path could vary depending on your project structure
		generateEntity.generateClassFile(sourcePath, classFileName, generateEntity.readFile(new File(dataFile)));
		
		/*
		 * Once execution is done.
		 * Please refresh package explorer in your IDE, to see generated file, 
		 * and made the corresponding imports for java types like Date, BigDecimal etc.
		 */ 
	}
}