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
import com.getindata.ververica.client.model.VvpTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ListTablesResponse
 */

public class ListTablesResponse {
  @JsonProperty("tables")
  private List<VvpTable> tables = null;

  public ListTablesResponse tables(List<VvpTable> tables) {
    this.tables = tables;
    return this;
  }

  public ListTablesResponse addTablesItem(VvpTable tablesItem) {
    if (this.tables == null) {
      this.tables = new ArrayList<VvpTable>();
    }
    this.tables.add(tablesItem);
    return this;
  }

   /**
   * Get tables
   * @return tables
  **/
  @ApiModelProperty(value = "")
  public List<VvpTable> getTables() {
    return tables;
  }

  public void setTables(List<VvpTable> tables) {
    this.tables = tables;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListTablesResponse listTablesResponse = (ListTablesResponse) o;
    return Objects.equals(this.tables, listTablesResponse.tables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tables);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListTablesResponse {\n");
    
    sb.append("    tables: ").append(toIndentedString(tables)).append("\n");
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

