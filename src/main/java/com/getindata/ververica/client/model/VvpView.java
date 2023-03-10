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
import com.getindata.ververica.client.model.VvpSchema;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VvpView
 */

public class VvpView {
  @JsonProperty("comment")
  private String comment = null;

  @JsonProperty("expandedQuery")
  private String expandedQuery = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("properties")
  private Map<String, String> properties = null;

  @JsonProperty("query")
  private String query = null;

  @JsonProperty("schema")
  private VvpSchema schema = null;

  public VvpView comment(String comment) {
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @ApiModelProperty(value = "")
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public VvpView expandedQuery(String expandedQuery) {
    this.expandedQuery = expandedQuery;
    return this;
  }

   /**
   * Get expandedQuery
   * @return expandedQuery
  **/
  @ApiModelProperty(value = "")
  public String getExpandedQuery() {
    return expandedQuery;
  }

  public void setExpandedQuery(String expandedQuery) {
    this.expandedQuery = expandedQuery;
  }

  public VvpView name(String name) {
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

  public VvpView properties(Map<String, String> properties) {
    this.properties = properties;
    return this;
  }

  public VvpView putPropertiesItem(String key, String propertiesItem) {
    if (this.properties == null) {
      this.properties = new HashMap<String, String>();
    }
    this.properties.put(key, propertiesItem);
    return this;
  }

   /**
   * Get properties
   * @return properties
  **/
  @ApiModelProperty(value = "")
  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public VvpView query(String query) {
    this.query = query;
    return this;
  }

   /**
   * Get query
   * @return query
  **/
  @ApiModelProperty(value = "")
  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public VvpView schema(VvpSchema schema) {
    this.schema = schema;
    return this;
  }

   /**
   * Get schema
   * @return schema
  **/
  @ApiModelProperty(value = "")
  public VvpSchema getSchema() {
    return schema;
  }

  public void setSchema(VvpSchema schema) {
    this.schema = schema;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VvpView vvpView = (VvpView) o;
    return Objects.equals(this.comment, vvpView.comment) &&
        Objects.equals(this.expandedQuery, vvpView.expandedQuery) &&
        Objects.equals(this.name, vvpView.name) &&
        Objects.equals(this.properties, vvpView.properties) &&
        Objects.equals(this.query, vvpView.query) &&
        Objects.equals(this.schema, vvpView.schema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment, expandedQuery, name, properties, query, schema);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VvpView {\n");
    
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    expandedQuery: ").append(toIndentedString(expandedQuery)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
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

