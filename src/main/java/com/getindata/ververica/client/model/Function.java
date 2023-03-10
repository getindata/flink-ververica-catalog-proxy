/*
 * Ververica Platform API
 * The Ververica Platform APIs, excluding Application Manager.
 *
 * OpenAPI spec version: 2.7.0
 * Contact: platform@ververica.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.getindata.ververica.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Function
 */

public class Function {
  @JsonProperty("className")
  private String className = null;

  @JsonProperty("description")
  private String description = null;

  /**
   * Gets or Sets functionLanguage
   */
  public enum FunctionLanguageEnum {
    FUNCTION_LANGUAGE_INVALID("FUNCTION_LANGUAGE_INVALID"),
    
    FUNCTION_LANGUAGE_JAVA("FUNCTION_LANGUAGE_JAVA"),
    
    FUNCTION_LANGUAGE_SCALA("FUNCTION_LANGUAGE_SCALA"),
    
    FUNCTION_LANGUAGE_PYTHON("FUNCTION_LANGUAGE_PYTHON"),
    
    UNRECOGNIZED("UNRECOGNIZED");

    private String value;

    FunctionLanguageEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FunctionLanguageEnum fromValue(String value) {
      for (FunctionLanguageEnum b : FunctionLanguageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("functionLanguage")
  private FunctionLanguageEnum functionLanguage = null;

  /**
   * Gets or Sets functionType
   */
  public enum FunctionTypeEnum {
    FUNCTION_TYPE_INVALID("FUNCTION_TYPE_INVALID"),
    
    FUNCTION_TYPE_FLINK("FUNCTION_TYPE_FLINK"),
    
    FUNCTION_TYPE_UNKNOWN_EXTERNAL("FUNCTION_TYPE_UNKNOWN_EXTERNAL"),
    
    UNRECOGNIZED("UNRECOGNIZED");

    private String value;

    FunctionTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static FunctionTypeEnum fromValue(String value) {
      for (FunctionTypeEnum b : FunctionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("functionType")
  private FunctionTypeEnum functionType = null;

  @JsonProperty("name")
  private String name = null;

  public Function className(String className) {
    this.className = className;
    return this;
  }

   /**
   * Get className
   * @return className
  **/
  @ApiModelProperty(value = "")
  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Function description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Function functionLanguage(FunctionLanguageEnum functionLanguage) {
    this.functionLanguage = functionLanguage;
    return this;
  }

   /**
   * Get functionLanguage
   * @return functionLanguage
  **/
  @ApiModelProperty(value = "")
  public FunctionLanguageEnum getFunctionLanguage() {
    return functionLanguage;
  }

  public void setFunctionLanguage(FunctionLanguageEnum functionLanguage) {
    this.functionLanguage = functionLanguage;
  }

  public Function functionType(FunctionTypeEnum functionType) {
    this.functionType = functionType;
    return this;
  }

   /**
   * Get functionType
   * @return functionType
  **/
  @ApiModelProperty(value = "")
  public FunctionTypeEnum getFunctionType() {
    return functionType;
  }

  public void setFunctionType(FunctionTypeEnum functionType) {
    this.functionType = functionType;
  }

  public Function name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function function = (Function) o;
    return Objects.equals(this.className, function.className) &&
        Objects.equals(this.description, function.description) &&
        Objects.equals(this.functionLanguage, function.functionLanguage) &&
        Objects.equals(this.functionType, function.functionType) &&
        Objects.equals(this.name, function.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className, description, functionLanguage, functionType, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Function {\n");
    
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    functionLanguage: ").append(toIndentedString(functionLanguage)).append("\n");
    sb.append("    functionType: ").append(toIndentedString(functionType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

