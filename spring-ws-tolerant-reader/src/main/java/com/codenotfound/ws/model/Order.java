package com.codenotfound.ws.model;

public class Order {

  private String orderId;

  public Order(String orderId) {
    this.orderId = orderId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  @Override
  public String toString() {
    return "Order[orderId=" + orderId + "]";
  }
}
