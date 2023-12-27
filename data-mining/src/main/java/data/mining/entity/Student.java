
package data.mining.entity;

import lombok.Data;

@Data
public class Student {
    private String name;
    private int height;
    private String rank;

    public Student(){}

    public Student(String n, int h){
        name = n;
        height = h;
    }

    @Override
    public String toString() {
        return
                "<" + name +
                        ", " + height +
                        ", " + rank + ">";
    }

    public Student(String n, int h, String r){
        name = n;
        height = h;
        rank = r;
    }
}

