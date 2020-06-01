package com.qiqiim.server.test.Test28;

public class Student implements Comparable<Student>{
    private int ID;
    private String lastName;
    private String firstName;
    private String teamName;

    public Student(int ID, String lastName, String firstName, String teamName) {
        this.ID = ID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.teamName = teamName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID=" + ID +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    public boolean isSame(Student other){
        return getID() == other.getID();
    }

    @Override
    public int compareTo(Student o) {
        String me = lastName + firstName;
        String she = o.lastName + o.firstName;
        return me.compareTo(she);
    }
}
