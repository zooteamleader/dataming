package data.mining.main;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.paukov.combinatorics3.Generator;
import org.paukov.combinatorics3.IGenerator;

import java.util.*;

public class Apriori {
    //定义单行数据
    public static String[] item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12,item13,item14;
    //定义数据集
    public static List<String[]> dataList = new ArrayList<>();
    public static double minSupport = 0.5;//定义最小支持度
    public static double minSupportCount = 0.0;//定义最小支持数
    public static double minConfidence = 0.5;//定义最小置信度
    //存放所有频繁项目集
    public static Map<Integer, Multimap<Integer, String[]>> allItemSets = new HashMap<>();

    public static void main(String[] args) {
        // 初始化数据集
        initDataList();
        String[] allElement = getAllElement(dataList);
        //获取候选集L1
        List<String[]> candidateL1 = getCombination(allElement, 1);
        Multimap<Integer, String[]> itemSetsL1 = getItemSets(candidateL1);
        allItemSets.put(1, itemSetsL1);

        printResult(itemSetsL1, 1);

        List<List<String[]>> stack = new ArrayList<>();
        stack.add(candidateL1);
        for (int k = 2; true; k++) {
            List<String[]> candidateLast = stack.get(0);
            String[] allElementLast = getAllElement(candidateLast);
            List<String[]> candidateNow = getCombination(allElementLast, k);
            Multimap<Integer, String[]> itemSetsLk = getItemSets(candidateNow);
            if (itemSetsLk.isEmpty()) break;
            allItemSets.put(k, itemSetsLk);
            printResult(itemSetsLk, k);
            stack.remove(0);
            stack.add(candidateNow);
        }
        correlation();
    }
    /**
     * 是否满足置信度
     */
    public static Integer getCountByConfidence(String[] subSetStr){
        int count = 0; //存放子集在数据集中出现的次数
        for (String[] dataItem : dataList) {
            //比如在数据集第一项{A,B,C,D,E,F,G}中是否出现过A,B.C
            int index = -1;
            for (int i = 0; i < subSetStr.length; i++) {
                //比如A出现过，则index>0，若A没出现过，则index=-1
                index = Arrays.binarySearch(dataItem, subSetStr[i]);
                if (index < 0) break;
            }
            if (index >= 0){ //出现过ABC
                count++;
            }
        }
        return count;
    }
    public static boolean isConfidence(String[] subSetStr1, String[] value){
        double confidence = getCountByConfidence(value) * 1.0 / getCountByConfidence(subSetStr1);
        if (confidence >= minConfidence) return true;
        return false;
    }


    /**
     * 获取含有所有元素的数组
     * @param itemSet
     * @return
     */
    public static String[] getAllElement(List<String[]> itemSet){
        List<String> allElementList = new ArrayList<>(); //存放项目集中所有有的元素
        for (int i = 0; i < itemSet.size(); i++) {
                        String[] item = itemSet.get(i);
            if (i == 0){ //将第一项的所有元素加入到allElementList中
                for (int j = 0; j < item.length; j++) {
                    allElementList.add(item[j]);
                }
            }else {
                for (int z = 0; z < item.length; z++) {
                    //判断allElementList中是否已经存在item[z]
                    int isExist = Arrays.binarySearch(allElementList.toArray(new String[allElementList.size()]), item[z]);
                    if (isExist < 0){ //不存在item[z]
                        allElementList.add(item[z]); //添加item[z]到allElementList中
                    }
                }
            }
        }
        return allElementList.toArray(new String[allElementList.size()]);
    }

    /**
     * 获取候选集
     * @param allItemStr 含频繁项目集所有元素的数组
     * @param k 要生成k候选集
     * @return
     */
    public static List<String[]> getCombination(String[] allItemStr, int k){
        //定义候选集
        List<String[]> candidateSets = new ArrayList<>();
        //对allItemStr进行k组合
        IGenerator<List<String>> candidateList = Generator.combination(allItemStr).simple(k);
        for (List<String> candidate : candidateList) {
            String[] candidateStr = candidate.toArray(new String[candidate.size()]);
            candidateSets.add(candidateStr);//将每一项组合放入候选集中
        }
        return candidateSets;
    }
    /**
     * 获取排列组合结果（候选集）每一项在数据集中出现的次数
     * @param itemList
     * @return
     */
    public static Multimap<Integer, String[]> getItemCount(List<String[]> itemList){
        Multimap<Integer, String[]> itemCount = ArrayListMultimap.create();
        //遍历项目集
        for (String[] item : itemList) {
            int count = 0; //存放数据集中出现的次数
            for (String[] dataItem : dataList) {
                //比如在数据集第一项{A,B,C,D,E,F,G}中是否出现过A,B.C
                int index = -1;
                for (int i = 0; i < item.length; i++) {
                    //比如A出现过，则index>0，若A没出现过，则index=-1
                    index = Arrays.binarySearch(dataItem, item[i]);
                    if (index < 0) break;
                }
                if (index >= 0){ //出现过ABC
                    count++;
                }
            }
            itemCount.put(count, item); //存放(ABC出现的次数，{A,B,C})
        }
        return itemCount;
    }
    /**
     * 处理候选集，获取频繁项目集
     * @param itemList 候选集
     * @return 频繁项目集
     */
    public static Multimap<Integer, String[]> getItemSets(List<String[]> itemList){
        Multimap<Integer, String[]> itemSets = ArrayListMultimap.create(); //项目集
        //得到排列组合结果（候选集）每一项在数据集中出现的次数
        Multimap<Integer, String[]> itemCount = getItemCount(itemList);
        //使用迭代器遍历multimap
                Iterator<Map.Entry<Integer, String[]>> iterator = itemCount.entries().iterator();
        //遍历排列组合结果的每一项，将出现次数不小于minSupportCount的项加入到itemSets
        while (iterator.hasNext()){
            Map.Entry<Integer, String[]> entry = iterator.next();
            if (entry.getKey() >= minSupportCount){
                itemSets.put(entry.getKey(), entry.getValue());
            }
        }
        return itemSets;
    }
    /**
     * 打印出所有频繁项目集
     * @param itemSet
     * @param k
     */
    public static void printResult(Multimap<Integer, String[]> itemSet, int k){
        System.out.print("L" + k + "={");
        //使用迭代器遍历multimap
        Iterator<Map.Entry<Integer, String[]>> iterator = itemSet.entries().iterator();
        //遍历排列组合结果的每一项，将出现次数大于minSupportCount的项加入到itemSets
        while (iterator.hasNext()){
            Map.Entry<Integer, String[]> entry = iterator.next();
            String[] value = entry.getValue();
            for (int i = 0; i < value.length; i++) {
                System.out.print(value[i]);
            }
            System.out.print(",");
        }
        System.out.println("}");
    }

    /**
     * 找强关联规则
     */
    public static void correlation(){
        //遍历所有频繁项目集
        for (int k = 1; k <= allItemSets.size(); k++) {
            //获取k-频繁项目集
            Multimap<Integer, String[]> keyItemSet = allItemSets.get(k);
            Iterator<Map.Entry<Integer, String[]>> iterator = keyItemSet.entries().iterator();
            //遍历k频繁项目集
            while (iterator.hasNext()){
                Map.Entry<Integer, String[]> entry = iterator.next();
                String[] value = entry.getValue();

                for (int i = 1; i < value.length; i++) {
                    List<String[]> subSet = getCombination(value, i); //非空子集的集合
                    for (String[] subSetItem : subSet) { //subSetItm是频繁项目集每一个非空子集
                        List<String> valueList = new ArrayList<>();
                        Collections.addAll(valueList, value);
                        List<String> subSetItemList = Arrays.asList(subSetItem);
                        //去除已经求得子集后的valueList
                        valueList.removeAll(subSetItemList); //此时valueList中存放非空子集的补集
                        if (isConfidence(subSetItem, value)){
                            System.out.println(Arrays.toString(subSetItem) + "==>" + Arrays.toString(valueList.toArray(new String[valueList.size()])));
                        }
                    }
                }
            }
        }
    }


    /**
     * 初始化数据集
     */
    public static void initDataList(){
        //初始化单行数据
        item1 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        item2 = new String[]{"A", "B", "C", "D", "E", "H"};
        item3 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        item4 = new String[]{"A", "B", "C", "G", "H"};
        item5 = new String[]{"A", "B", "C", "D", "G", "H"};
        item6 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        item7 = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        item8 = new String[]{"A", "B", "C", "E", "G", "H"};
        item9 = new String[]{"A", "B", "C", "D", "E", "F", "H"};
        item10 = new String[]{"C", "D", "E", "F", "G", "H"};
        item11 = new String[]{"A", "B", "C", "D", "G", "H"};
        item12 = new String[]{"A", "C", "D", "E", "F", "G", "H"};
        item13 = new String[]{"A", "B", "C", "E", "F", "G", "H"};
        item14 = new String[]{"B", "C", "E", "F", "G", "H"};

        //初始化数据集
        dataList.add(item1);
        dataList.add(item2);
        dataList.add(item3);
        dataList.add(item4);
        dataList.add(item5);
        dataList.add(item6);
        dataList.add(item7);
        dataList.add(item8);
        dataList.add(item9);
        dataList.add(item10);
        dataList.add(item11);
        dataList.add(item12);
        dataList.add(item13);
        dataList.add(item14);

        //赋值给最小支持的数
        minSupportCount = dataList.size() * minSupport;
    }
}
