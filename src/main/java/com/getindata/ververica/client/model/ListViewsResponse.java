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
import com.getindata.ververica.client.model.VvpView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * ListViewsResponse
 */

public class ListViewsResponse {
  @JsonProperty("views")
  private List<VvpView> views = null;

  public ListViewsResponse views(List<VvpView> views) {
    this.views = views;
    return this;
  }

  public ListViewsResponse addViewsItem(VvpView viewsItem) {
    if (this.views == null) {
      this.views = new ArrayList<VvpView>();
    }
    this.views.add(viewsItem);
    return this;
  }

   /**
   * Get views
   * @return views
  **/
  @ApiModelProperty(value = "")
  public List<VvpView> getViews() {
    return views;
  }

  public void setViews(List<VvpView> views) {
    this.views = views;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListViewsResponse listViewsResponse = (ListViewsResponse) o;
    return Objects.equals(this.views, listViewsResponse.views);
  }

  @Override
  public int hashCode() {
    return Objects.hash(views);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListViewsResponse {\n");
    
    sb.append("    views: ").append(toIndentedString(views)).append("\n");
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

