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
import com.getindata.ververica.client.model.Database;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ListDatabasesResponse
 */

public class ListDatabasesResponse {
  @JsonProperty("databases")
  private List<Database> databases = null;

  public ListDatabasesResponse databases(List<Database> databases) {
    this.databases = databases;
    return this;
  }

  public ListDatabasesResponse addDatabasesItem(Database databasesItem) {
    if (this.databases == null) {
      this.databases = new ArrayList<Database>();
    }
    this.databases.add(databasesItem);
    return this;
  }

   /**
   * Get databases
   * @return databases
  **/
  @ApiModelProperty(value = "")
  public List<Database> getDatabases() {
    return databases;
  }

  public void setDatabases(List<Database> databases) {
    this.databases = databases;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListDatabasesResponse listDatabasesResponse = (ListDatabasesResponse) o;
    return Objects.equals(this.databases, listDatabasesResponse.databases);
  }

  @Override
  public int hashCode() {
    return Objects.hash(databases);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListDatabasesResponse {\n");
    
    sb.append("    databases: ").append(toIndentedString(databases)).append("\n");
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

