package com.jc.codegenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class GenerateEntity {
	
	public GenerateEntity() {
	}
	
	public void generateClassFile(String source_path, String className, List<String> lines) {

		String pathname = String.format("%s%s/", source_path, GenerateEntity.class.getPackageName());		
		StringBuilder parametrosConstructor = new StringBuilder();
		StringBuilder parametrosConstructorBody = new StringBuilder();
		StringBuilder gettersAndSetters = new StringBuilder();
		File file = new File(String.format("%s%s.java", pathname.replace('.', '/'), className));
		StringBuilder classDefinition = new StringBuilder(
				String.format("package %s;\r\n\r\npublic class %s {\r\n\r\n", GenerateEntity.class.getPackageName(), className));

		for (String string : lines) {
			String fila = string.replaceAll("\s{1,}", "\t");

			String[] arrayFila = fila.split("\t");
			if (arrayFila.length > 0) {

				String[] arrayColumna = arrayFila[0].split("_");
				String s0 = "";
				String s1 = "";
				StringBuilder s1Temporal = new StringBuilder();

				for (String string2 : arrayColumna) {
					s0 = string2.toLowerCase();
					s1Temporal.append(s0.substring(0, 1).toUpperCase() + s0.substring(1));
				}

				s1 = s1Temporal.substring(0, 1).toLowerCase() + s1Temporal.substring(1);
				String tipoDato = arrayFila[1];
				String atributo = String.format("\t%s %s;\r\n", getJavaDataTypeFromOracle11GDataType(tipoDato), s1);
				// Get parameters for constructor
				parametrosConstructor.append(String.format("%s %s, ",
						getJavaDataTypeFromOracle11GDataType(tipoDato).replace("private ", ""), s1));
				// Get parameters for constructor body
				parametrosConstructorBody.append(String.format("\t\tthis.%s = %s;\r\n", s1, s1));
				classDefinition.append(atributo);
				// getters and Setters
				gettersAndSetters.append(String.format("\r\n\tpublic void set%s%s(%s %s) {\r\n",
						s1.substring(0, 1).toUpperCase(), s1.substring(1),
						getJavaDataTypeFromOracle11GDataType(tipoDato).replace("private ", ""), s1));
				gettersAndSetters.append(String.format("\t\tthis.%s = %s;\r\n\t}\r\n", s1, s1));

				gettersAndSetters.append(String.format("\r\n\tpublic %s get%s%s() {\r\n",
						getJavaDataTypeFromOracle11GDataType(tipoDato).replace("private ", ""),
						s1.substring(0, 1).toUpperCase(), s1.substring(1)));
				gettersAndSetters.append(String.format("\t\treturn %s;\r\n\t}\r\n", s1));
			}
		}

		classDefinition.append(generateConstructorWithAndWithOutParameters(className, lines,
				parametrosConstructor.toString(), parametrosConstructorBody.toString()));
		classDefinition.append(gettersAndSetters);
		classDefinition.append("}");
		
		writeToFile(file, classDefinition.toString());
	}

	private String generateConstructorWithAndWithOutParameters(String className, List<String> lines,
			String parametrosConstructor, String parametrosConstructorBody) {

		StringBuilder constructors = new StringBuilder();
		constructors.append(String.format("\r\n\tpublic %s() {\r\n\t}\r\n", className));
		constructors.append(String.format("\r\n\tpublic %s(%s) {\r\n%s\t}\r\n", className,
				parametrosConstructor.substring(0, (parametrosConstructor.length() - 2)), parametrosConstructorBody));
		return constructors.toString();
	}

	private static String getJavaDataTypeFromOracle11GDataType(String oracleDataType) {
		
		String PRIVATE_STRING = "private String";
		String PRIVATE_INT = "private int";
		String PRIVATE_DOUBLE = "private double";
		String PRIVATE_BIGDECIMAL = "private BigDecimal";
		String PRIVATE_TIMESTAMP = "private java.sql.Timestamp";
		String PRIVATE_CLOB = "private java.sql.Clob";
		String PRIVATE_BLOB = "private java.sql.Blob";
		
		switch (oracleDataType) {
		case "VARCHAR":
			return PRIVATE_STRING;
		case "VARCHAR2":
			return PRIVATE_STRING;
		case "NVARCHAR2":
			return PRIVATE_STRING;
		case "CHAR":
			return PRIVATE_STRING;
		case "INTEGER":
			return PRIVATE_INT;
		case "INT":
			return PRIVATE_INT;
		case "SMALLINT":
			return PRIVATE_INT;
		case "REAL":
			return "float";
		case "DOUBLE PRECISION":
			return PRIVATE_DOUBLE;
		case "FLOAT":
			return PRIVATE_DOUBLE;
		case "DEC":
			return PRIVATE_BIGDECIMAL;
		case "DECIMAL":
			return PRIVATE_BIGDECIMAL;
		case "NUMBER":
			return PRIVATE_BIGDECIMAL;
		case "NUMERIC":
			return PRIVATE_BIGDECIMAL;
		case "DATE":
			return PRIVATE_TIMESTAMP;
		case "TIMESTAMP":
			return PRIVATE_TIMESTAMP;
		case "CLOB":
			return PRIVATE_CLOB;
		case "BLOB":
			return PRIVATE_BLOB;
		default:
			return "private unknown_data_type";
		}
	}

	public List<String> readFile(File file) {
		List<String> lines = new ArrayList<>();
		try {
			lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public void writeToFile(File file, String data) {

		try {
			FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}