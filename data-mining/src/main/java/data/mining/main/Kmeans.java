package data.mining.main;

import data.mining.entity.Cluster;
import data.mining.entity.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Kmeans {
    //定义初始数据集
    public static List<Point> dataList = new ArrayList<>();
    //定义簇的数目
    public static Integer k = 3;

    public static void main(String[] args) {
        //初始化数据集和初始簇
        initDataList();
        List<Cluster> clusterList = getInitCluster();
        while(true){
            for (int j = 0; j < k; j++) {
                clusterList.get(j).getSameList().clear();
            }
            for (Point point : dataList) {
                int index = getBelongCluster(point, clusterList); //获取point属于的那个簇在clusterList中的下标
                clusterList.get(index).getSameList().add(point); //把point加入到clusterList的对应簇中;
            }
            if (!calculateClusterCore(clusterList)) break;
        }
        int k=0;
        for (Cluster cluster : clusterList) {
            //System.out.println(cluster);
            k++;
            System.out.print("簇"+k+"：");
            System.out.println(cluster);
        }
    }

    /**
     * 获取含有k个不重复随机数的数组
     * @return
     */
    public static int[] getRandomArray(){
        Random random = new Random();
        int[] randomArray = new int[k];
        for (int i = 0; i < k; i++) {
            int randomItem = random.nextInt(12);
            //为保证randomArray中存放的随机数不重复
            while (Arrays.binarySearch(randomArray, randomItem) >= 0) randomItem = random.nextInt(12);
            randomArray[i] = randomItem;
        }
        return randomArray;
    }

    /**
     * 获取任意k个对象作为初始簇中心，将含有k个簇的集合返回
     * @return
     */
    public static List<Cluster> getInitCluster(){
        List<Cluster> clusterList = new ArrayList<>();
        int[] randomArray = getRandomArray();
        //任意选取k个对象作为初始簇中心，数据集中k个对象的下标存放在randomArray中
        for (int i = 0; i < randomArray.length; i++) {
            Point point = dataList.get(randomArray[i]);
            Cluster cluster = new Cluster(point);
            clusterList.add(cluster);
        }
//        for (int i = 1; i < 4; i++) {
//            Point point = dataList.get(i);
//           Cluster cluster = new Cluster(point);
//            clusterList.add(cluster);
//        }
        return clusterList;
    }

    /**
     * 计算出新的簇中心并返回簇的点集合是否有变化
     * @param clusterList
     * @return
     */
    public static boolean calculateClusterCore(List<Cluster> clusterList){
        boolean flag = false;//没有变化就终止
        //遍历簇集合中的每一项，更新其簇中心
        for (Cluster cluster : clusterList) {
            List<Point> sameList = cluster.getSameList();
                        double sumX = 0; //存放簇中点集合所有的X坐标之和
            double sumY = 0; //存放簇中点集合所有的Y坐标之和
            for (Point point : sameList) {
                sumX += point.getX();
                sumY += point.getY();
            }
            //更新簇的中心
            Point clusterCore = new Point(sumX * 1.0 / sameList.size(), sumY * 1.0 / sameList.size());
            if (!Point.getIsSame(clusterCore, cluster.getCorePoint())) flag = true;
            cluster.setCorePoint(clusterCore);
        }
        return flag;
    }

    /**
     * 获取某个点属于哪个簇的下标
     * @param point
     * @return
     */
    public static int getBelongCluster(Point point, List<Cluster> clusterList){
        double closestDistance = 0.0; //存放point距离簇中心最近的距离
        int resultClusterIndex = 0; //存放point属于的那个簇的下标
        int index = 0;
        //遍历簇集合，计算point到簇中心的距离，找出point属于的簇
        for (Cluster cluster : clusterList) {
            double distance = Point.calculateDistance(point, cluster.getCorePoint());
            if (index == 0) closestDistance = distance;
            if (distance < closestDistance){
                closestDistance = distance;
                resultClusterIndex = index;
            }
            index++;
        }
        return resultClusterIndex;
    }


    /**
     * 初始化数据集
     */
    public static void initDataList(){
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(2, 4);
        Point p4 = new Point(4, 3);
        Point p5 = new Point(5, 8);
        Point p6 = new Point(6, 7);
        Point p7 = new Point(6, 9);
        Point p8 = new Point(7, 9);
        Point p9 = new Point(9, 5);
        Point p10 = new Point(1, 12);
        Point p11 = new Point(3, 12);
        Point p12 = new Point(5, 12);
        Point p13 = new Point(3, 3);

        dataList.add(p1);
        dataList.add(p2);
        dataList.add(p3);
        dataList.add(p4);
        dataList.add(p5);
        dataList.add(p6);
        dataList.add(p7);
        dataList.add(p8);
        dataList.add(p9);
        dataList.add(p10);
        dataList.add(p11);
        dataList.add(p12);
        dataList.add(p13);
    }
}


