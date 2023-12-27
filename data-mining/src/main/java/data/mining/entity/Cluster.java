package data.mining.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cluster {
    private Point corePoint;
    private List<Point> sameList = new ArrayList<>();

    public Cluster(){}

    @Override
    public String toString() {
        return
                "平均值点=" + corePoint +
                        ", 簇=" + sameList ;
    }

    public Cluster(Point cp){
        corePoint = cp;
    }
}

