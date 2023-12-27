package data.mining.main;

import data.mining.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class KNN {
    //定义初始数据集
    public static List<Student> dataList = new ArrayList<>();

    public static void main(String[] args) {
        initData();
        Student stuV0 = new Student("易昌", 174);
        Student student = Knn(stuV0);
        System.out.print("knn认为"+student.getName()+"为"+student.getRank());
        System.out.println(student.toString());
    }


    /**
     * 计算出stuV0距离categoryList集合中最远的学生对象
     * @param student1
     * @param categoryList
     * @return
     */
    public static Student getCalculate(Student student1, List<Student> categoryList) {
        int maxHeight = 0; //存放stuV0与类别集合categoryList的最远距离
        Student resultStu = new Student(); //存放要返回的学生，即stuV0与类别集合categoryList距离最远的学生
        for (Student stuU : categoryList) {
            int v0ToU = Math.abs(student1.getHeight() - stuU.getHeight()); //stuV0与stuU的距离
            if (v0ToU > maxHeight){ //stuV0与stuU的距离大于maxHeight，则对maxHeight和resultStu进行更新
                maxHeight = v0ToU;
                resultStu = stuU;
            }
        }
        return resultStu;
    }
    /**
     * 找出同等级占比最多的学生等级
     * @param categoryList
     * @return
     */
    public static String getCategoryStudent(List<Student> categoryList){
        int tallCount = 0;
        int midCount = 0;
        int smallCount = 0;
        for (Student stuU : categoryList) {
            if (stuU.getRank().equals("高")) tallCount++;
            else if (stuU.getRank().equals("中等")) midCount++;
            else smallCount++;
        }
        if(tallCount>=midCount&&tallCount>=smallCount){
            return "高";
        } else if (midCount>=tallCount&&midCount>=smallCount) {
            return "中等";
        }else return "矮";
    }
    /**
     * 对输入学生类进行Knn算法实例化该学生的等级后，将该学生返回
     * @param stuV0
     * @return
     */
    public static Student Knn(Student stuV0){
        List<Student> categoryList = new ArrayList<>(); //存放距离stuV0最近的k个学生，最初存放数据集的前5项
        for (int i = 0; i < dataList.size(); i++) {
            if (i < 5) categoryList.add(dataList.get(i));
            else {
                //stuV0距离剩下数据集中某项的距离
                int v0Tod = Math.abs(stuV0.getHeight() - dataList.get(i).getHeight());//新来的
                Student stuU =  getCalculate(stuV0, categoryList); //存放stuV0距离类别集合中最远的学生
                int uToV0 = Math.abs(stuU.getHeight() - stuV0.getHeight());//最远k个的距离
                System.out.println(categoryList.toString());
                if (uToV0 > v0Tod){
                    categoryList.remove(stuU); //在集合列表中去除stuU
                    categoryList.add(dataList.get(i));
                }
            }
        }
        System.out.println("最后的输出N="+categoryList.toString());
        String rank = getCategoryStudent(categoryList);
        stuV0.setRank(rank);

        return stuV0;
    }

    /**
     * 初始化数据
     */
    public static void initData(){
        Student s1 = new Student("李丽", 150, "矮");
        Student s2 = new Student("吉米", 192, "高");
        Student s3 = new Student("马大华", 170, "中等");
        Student s4 = new Student("王晓华", 173, "中等");
        Student s5 = new Student("刘敏", 160, "矮");
        Student s6 = new Student("张强", 175, "中等");
        Student s7 = new Student("李秦", 160, "矮");
        Student s8 = new Student("王壮", 190, "高");
        Student s9 = new Student("刘冰", 168, "中等");
        Student s10 = new Student("张喆", 178, "中等");
        Student s11 = new Student("杨毅", 170, "中等");
        Student s12 = new Student("徐田", 168, "中等");
        Student s13 = new Student("高杰", 165, "矮");
        Student s14 = new Student("张晓", 178, "中等");

        dataList.add(s1);
        dataList.add(s2);
        dataList.add(s3);
        dataList.add(s4);
        dataList.add(s5);
        dataList.add(s6);
        dataList.add(s7);
        dataList.add(s8);
        dataList.add(s9);
        dataList.add(s10);
        dataList.add(s11);
        dataList.add(s12);
        dataList.add(s13);
        dataList.add(s14);
    }
}
