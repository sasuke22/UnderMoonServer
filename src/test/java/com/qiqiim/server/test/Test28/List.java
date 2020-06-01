package com.qiqiim.server.test.Test28;

public class List<E> {
    private E head;
    private List<E> tail;
    private boolean isEmpty = false;
    public List(E a,List<E> L) {
        head = a;
        tail = L;
    }

    public List() {
        isEmpty = true;
    }

    public E getHead(){
        return head;
    }

    public List<E> getTail(){
        return tail;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public void setEmpty(){
        isEmpty = false;
    }

    List<E> addToEnd(List<E> L, E i){
        return append(L,new List(i,new List<>()));
    }

    List<E> append(List<E> L, List<E> M){
        if (L.isEmpty())
            return M;
        else
            return new List<>(L.getHead(),append(L.getTail(),M));
    }

    void printList() {
        if (!this.isEmpty()) {
            System.out.println(getHead());
            getTail().printList();
        }
    }

    public String toString() {
        return "[" + toStringAux() + "]";
    }

    public String toStringAux() {
        if (isEmpty()) {
            return "";
        } else if (getTail().isEmpty()) {
            return getHead() + "";
        } else {
            return getHead() + ", " + getTail().toStringAux();
        }
    }

    @Override
    public boolean equals(Object o) {
        List<E> list = (List<E>) o;
        if (this.isEmpty && list.isEmpty())
            return true;
        else if (this.isEmpty() || list.isEmpty()) {
            return false;
        } else if (this.getHead().equals(list.getHead())) {
            return this.getTail().equals(list.getTail());
        } else {
            return false;
        }
    }
}
