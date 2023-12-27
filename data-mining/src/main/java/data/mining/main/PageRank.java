package data.mining.main;

import data.mining.entity.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageRank {
    //定义转移矩阵
    public static List<Score[]> dataList = new ArrayList<>();
    //定义转移矩阵中的每一项
    public static Score s00,s01,s02,s03,s10,s11,s12,s13,s20,s21,s22,s23,s30,s31,s32,s33;

    public static void main(String[] args) {
        initData();
        Score roScore = new Score(1, 1);

        Score[] R0 = {roScore, roScore, roScore, roScore};
        Score[] pageRank = R0;
        int r=0;
        while (true){
            r++;
            Score[] tmpVk = getPageRank(pageRank);
            if (isRank(pageRank, tmpVk)) break; //新得到的PageRank的迭代终止条件是否满足，则继续迭代，小于则不再迭代
            pageRank = tmpVk;
            System.out.println("R"+r+"="+Arrays.toString(pageRank));
        }


    }
    /**
     * 获取PageRank矩阵
     * @param Vk
     * @return
     */
    public static Score[] getPageRank(Score[] Vk){
        List<Score> pageRankList = new ArrayList<>();
        for (Score[] dataItem : dataList) {
            Score itemSum = new Score(0,0); //itemSum中存放PageRank矩阵的每一项
            //通过遍历数据集的每一行和Vk的每一列实现矩阵乘法
            for (int i = 0; i < dataItem.length; i++) {
                Score multiply = Score.getMultiply(dataItem[i], Vk[i]);//矩阵乘法*r[i]
                itemSum = Score.getAdd(multiply, itemSum); //将对应项相乘的结果累加到itemSum中
            }
            pageRankList.add(itemSum);
        }
        return pageRankList.toArray(new Score[pageRankList.size()]);
    }
    /**
     * 判断V1和V2的PageRank矩阵内的值是否相等
     * @param V1
     * @param V2
     * @return
     */
    public static boolean isRank(Score[] V1, Score[] V2){
        double s=0;
        for (int i = 0; i < V1.length; i++) {
            s=s+Math.abs(V2[i].getSon()*1.0/V2[i].getMom()-V1[i].getSon()*1.0/V1[i].getMom());
//            double s=0;
//            double p1=Math.abs(V2[0].getSon()*1.0/V2[0].getMom()-V1[0].getSon()*1.0/V1[0].getMom());
//            double p2=Math.abs(V2[1].getSon()*1.0/V2[1].getMom()-V1[1].getSon()*1.0/V1[1].getMom());
//            double p3=Math.abs(V2[2].getSon()*1.0/V2[2].getMom()-V1[2].getSon()*1.0/V1[2].getMom());
//            double p4=Math.abs(V2[3].getSon()*1.0/V2[3].getMom()-V1[3].getSon()*1.0/V1[3].getMom());
//            s=p1+p2+p3+p4;
            double n=(double)Math.round(s*1000)/1000;
            int subSon = V1[i].getSon() - V2[i].getSon();
            int subMom = V1[i].getMom() - V2[i].getMom();

            if (n>=0.001) return false;//设置迭代终止条件

        }
        double A=V1[0].getSon()*1.0/V1[0].getMom();
        double B=V1[1].getSon()*1.0/V1[1].getMom();
        double C=V1[2].getSon()*1.0/V1[2].getMom();
        double D=V1[3].getSon()*1.0/V1[3].getMom();
        System.out.println("最终A、B、C、D的等级值为"+V1[0].getSon()+"/"+V1[0].getMom()+"、"+V1[1].getSon()+"/"+V1[1].getMom()+"、"+V1[2].getSon()+"/"+V1[2].getMom()+"和"+V1[3].getSon()+"/"+V1[3].getMom());
        System.out.println("最终A、B、C、D的等级值为"+A+"、"+B+"、"+C+"和"+D);
        return true;
    }



    /**
     * 初始化数据集
     */
    public static void initData(){
        s00 = new Score(0, 0);
        s01 = new Score(1, 2);
        s02 = new Score(1, 1);
        s03 = new Score(0, 0);
        s10 = new Score(1, 3);
        s11 = new Score(0, 0);
        s12 = new Score(0, 0);
        s13 = new Score(1, 2);
        s20 = new Score(1, 3);
        s21 = new Score(0, 0);
        s22 = new Score(0, 0);
        s23 = new Score(1, 2);
        s30 = new Score(1, 3);
        s31 = new Score(1, 2);
        s32 = new Score(0, 0);
        s33 = new Score(0, 0);

        Score[] s0 = {s00, s01, s02, s03};
        Score[] s1 = {s10, s11, s12, s13};
        Score[] s2 = {s20, s21, s22, s23};
        Score[] s3 = {s30, s31, s32, s33};

        dataList.add(s0);
        dataList.add(s1);
        dataList.add(s2);
        dataList.add(s3);
    }
}

