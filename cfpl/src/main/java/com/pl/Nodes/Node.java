package com.pl.Nodes;

public abstract class Node {
   public Node next;
   public Node() {}

   public void setNext(Node next) {
      this.next = next;
   }

   public Node getNext() {
      return next;
   }
}
