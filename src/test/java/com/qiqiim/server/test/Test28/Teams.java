package com.qiqiim.server.test.Test28;

import java.util.*;
import java.util.List;

public class Teams {
    private List<Student> studentList;
    public Teams(List<Student> students) {
        this.studentList = students;
    }

    public void addStudent(List<Student> student){
        studentList.addAll(student);
    }

    void deleteStudent(Student student){
        // to avoid the index out of bounds exception,use iterator
        Iterator<Student> iterator = studentList.iterator();
        while (iterator.hasNext()){
            if (iterator.next().getID() == student.getID())
                iterator.remove();
        }
    }

    void deleteList(List<Student> list){
        Iterator<Student> iterator = studentList.iterator();
        while (iterator.hasNext()){
            for (Student student : list) {
                if (iterator.next().isSame(student))
                    iterator.remove();
            }
        }
    }

    void print(){
        // sort by last name and first name first
        studentList.sort(new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                String first = student.getLastName() + student.getFirstName();
                String second = t1.getLastName() + t1.getFirstName();
                return first.compareTo(second);
            }
        });
        StringBuilder builder = new StringBuilder("team : ");
        for (int i = 0; i < studentList.size(); i++) {
            builder.append(studentList.get(i).toString());
            // only the last member shouldn't add ','
            if (i != studentList.size()-1)
                builder.append(",");
        }
        System.out.println(builder.toString());
    }

    public static void main(String[] args){
        List<Student> list = new ArrayList<>();
        list.add(new Student(1,"Walker","Paul","speed"));
        Teams speed = new Teams(list);
        List<Student> newList = new ArrayList<>();
        newList.add(new Student(2,"Diesel","Vin","speed"));
        newList.add(new Student(0,"Brewster","Jordana","speed"));
        speed.addStudent(newList);
        speed.print();
    }
}
