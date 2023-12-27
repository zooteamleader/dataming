package data.mining.entity;


import lombok.Data;

@Data
public class Score {
    private int son;
    private int mom;

    public Score(){}

    public Score(int s, int m){
        son = s;
        mom = m;
    }

    /**
     * 分数相加并调用simplify方法进行约分
     * @param s1
     * @param s2
     * @return
     */
    public static Score getAdd(Score s1, Score s2){
        if (s1.getMom() == 0 || s2.getMom() == 0) return s1.getMom() == 0 ? s2 : s1;
        int commonMom = s1.getMom() * s2.getMom();
        int commonSon = s1.getSon() * s2.getMom() + s2.getSon() * s1.getMom();
        Score addResult = simplify(commonSon, commonMom);
        return addResult;
    }

    /**
     * 分数相乘并调用simplify方法进行约分
     * @param s1
     * @param s2
     * @return
     */
    public static Score getMultiply(Score s1, Score s2){
        int tempMom = s1.getMom() * s2.getMom();
        int tempSon = s1.getSon() * s2.getSon();
        Score simplifyResult = simplify(tempSon, tempMom);
        return simplifyResult;
    }

    /**
     * 对分子分母进行约分
     * @param s
     * @param m
     * @return
     */
    private static Score simplify(int s, int m){
        int common = getCommon(s, m);
        s = s / common;
        m = m / common;
        Score result = new Score(s, m);
        return result;
    }

    /**
     * 找分子分母的最大公约数
     * @param s
     * @param m
     * @return
     */
    private static int getCommon(int s, int m){
        for (int i = s; i >= 1; i--) {
            if (m%i==0 && s%i==0){
                return i;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return son+ "/" + mom ;
    }
}


