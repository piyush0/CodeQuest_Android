package com.codingblocks.attendancetracker.models;

import java.util.ArrayList;

/**
 * Created by piyush0 on 12/12/16.
 */

public class Student {

    String name;
    String batch;
    String image_url;


    public Student(String name, String batch, String image_url) {
        this.name = name;
        this.batch = batch;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public static ArrayList<Student> getDummyStudents(){

        ArrayList<Student> students = new ArrayList<>();

        students.add(new Student("Piyush","Crux","abcd"));
        students.add(new Student("Abcd","Pandora","abcd"));
        students.add(new Student("Efgh","Elixir","abcd"));
        students.add(new Student("Ijkl","Launchpad","abcd"));
        students.add(new Student("Mnop","Django","abcd"));


        return students;

    }
}
