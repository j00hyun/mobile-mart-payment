package com.automart.util.annotation;

public enum GqlType {
  QUERY("Query"), MUTATION("Mutation");

  private String value;

  GqlType(String value) {
      this.value = value;
  }
  public String getValue() {
      return this.value;
  }

}