package data.mining.main;

import com.alibaba.excel.EasyExcel;
import data.mining.entity.Cluster;
import data.mining.entity.ExcelData;
import data.mining.entity.Point;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DBSCAN {
    // 定义初始数据集
    public static List<Point> dataList = new ArrayList<>();
    // 定义半径e
    public static double e = 2.0;
    // 定义核心对象领域内对象的最少数目
    public static int MinPts = 3;

    public static void main(String[] args) {
       getFileData();
       //initDataList();
        List<Cluster> clusterList = new ArrayList<>();
        for (Point point : dataList) {
            if (isExistCluster(point, clusterList)) continue; //已经在簇中的点不再考虑
            List<Point> ePointList = getEPointList(point);
            if (ePointList.size() >= MinPts){ //说明点point是核心对象
                Cluster cluster = new Cluster(point);
                cluster.setSameList(ePointList);
                Cluster newCluster = canReachPoint(cluster);
                clusterList.add(newCluster);
            }
        }
        System.out.println("聚出的类为");
        int pointSum = 0;
        int i=0;
        for (Cluster cluster : clusterList) {
            i++;
            System.out.println("簇C"+i+":"+cluster.getSameList());
            pointSum += cluster.getSameList().size();
        }
      //  System.out.println(pointSum);
    }

    /**
     * 获取一个点的e领域内所有的点集合
     * @param point
     * @return
     */
    public static List<Point> getEPointList(Point point){
        List<Point> pointList = new ArrayList<>(); //存放point的e领域内所有的点
        for (Point p : dataList) {
            double ptoPoint = Point.calculateMHDDistance(point, p);
            if (ptoPoint <= e) pointList.add(p); //说明点p在point的e领域内
        }
        return pointList;
    }

    /**
     * 判断point是否已经在已存在的簇中
     * @param point
     * @param clusterList
     * @return
     */
     public static boolean isExistCluster(Point point, List<Cluster> clusterList){
        for (Cluster cluster : clusterList) {
            List<Point> pointList = cluster.getSameList();
            if (pointList.contains(point)) return true;
        }
        return false;
    }

    /**
     * 遍历核心对象直接密度可达的点，合并其所有密度可达的点
     * @param cluster
     * @return
     */
     public static Cluster canReachPoint(Cluster cluster){
        List<Point> pointList = cluster.getSameList();
        List<Point> reachPointList = new ArrayList<>(); //存放核心点所有密度可达的点（暂存要新加入进来的点）
        for (Point point : pointList) {
            Point corePoint = cluster.getCorePoint();
            if (Point.getIsSame(corePoint, point)) continue; //这里不再遍历核心对象点
            List<Point> reachList = getEPointList(point); //核心对象直接密度可达的点其e领域内所有的点的集合
            if (reachList.size() >= MinPts){ //说明point也是核心对象，其领域内的所有点也可以合并到cluster中
                for (Point reachPoint : reachList) {
                    if (pointList.contains(reachPoint)) continue; //对于pointList中已经有的点不再重复添加
                    reachPointList.add(reachPoint); //将密度可达的点添加到密度可达的点集合中
                }
            }
        }
        pointList.addAll(reachPointList); //将密度可达的点全加入到簇中
        cluster.setSameList(pointList);
        return cluster;
    }



    public static void getFileData(){
        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\10706\\Desktop\\DBSCAN.xlsx");

            List<ExcelData> fileData = EasyExcel.read(inputStream).head(ExcelData.class).sheet()
                    .headRowNumber(1).doReadSync();
            for (ExcelData excelData : fileData) {
                Point point = new Point(excelData.getX(), excelData.getY());
                dataList.add(point);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


