package com.qiqiim.server.test.Test28;

public class New28 {
    private static List<Integer> result = new List<>();
    public static void main(String[] args){
        List<Integer> a = new List<>();
        List<Integer> b = new List<>(-1,new List<>(4,new List<>(5,new List<>())));
        result = unique(a,b);
        result.printList();
    }

    private static List<Integer> unique(List<Integer> a, List<Integer> b) {
        if (!a.isEmpty() || !b.isEmpty()){
            // if a is empty,just need to append b to the end of result
            if (a.isEmpty()){
                return result.append(result,b);
            }
            // if b is empty,just need to append a to the end of result
            if (b.isEmpty()){
                return result.append(result,a);
            }
            // if a.head is smaller than b.head,we add a.head to the end of result,than recursively add the result of unique(a.tail,b) to the end of result
            if (a.getHead() < b.getHead()){
                return result.append(result.addToEnd(result,a.getHead()),unique(a.getTail(),b));
            }
            // same as above,just recursively calculate the result of unique(a,b.tail)
            if (a.getHead() > b.getHead()){
                return result.append(result.addToEnd(result,b.getHead()),unique(a, b.getTail()));
            }
            // if a.head is equal to b.head,we should add nothing
            if (a.getHead().intValue() == b.getHead().intValue()){
                return result.append(result,unique(a.getTail(),b.getTail()));
            }
        }
        return result;
    }
}
