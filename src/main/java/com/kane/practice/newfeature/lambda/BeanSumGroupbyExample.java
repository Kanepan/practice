package com.kane.practice.newfeature.lambda;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanSumGroupbyExample {
    public static void main(String[] args) {
        List<Bean3> beanList = new ArrayList<>();
        beanList.add(new Bean3(1, "A", 10, 20, 30, 40, 50, 60, 70, 80));
        beanList.add(new Bean3(2, "B", 10, 20, 30, 40, 50, 60, 70, 80));
        beanList.add(new Bean3(1, "C", 10, 20, 30, 40, 50, 60, 70, 80));
        beanList.add(new Bean3(1, "C", 10, 20, 30, 40, 50, 60, 70, 80));
        beanList.add(new Bean3(1, "C", 10, 20, 30, 40, 50, 60, 70, 80));

        beanList.add(new Bean3(2, "A", 10, 20, 30, 40, 50, 60, 70, 80));
        beanList.add(new Bean3(2, "A", 10, 20, 30, 40, 50, 60, 70, 80));

        // 根据ID字段分组，并对其他字段求和创建新对象
        Map<String, SumBean> sumByGroup = beanList.stream()
//                .collect(Collectors.groupingBy(Bean3::getId, Collectors.reducing(new SumBean(), Bean3::toSumBean, SumBean::combine)));

                .collect(Collectors.toMap(Bean3::groupby, Bean3::toSumBean, SumBean::combine2));

//        List<SumBean> mergedList = new ArrayList<>(sumByGroup.values());


        // 打印结果
        for (Map.Entry<String, SumBean> entry : sumByGroup.entrySet()) {
            System.out.println("Sum of fields for ID " + entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Bean类
class Bean3 {
    private int id;
    private String group;
    private int field1;
    private int field2;
    private int field3;
    private int field4;
    private int field5;
    private int field6;
    private int field7;
    private int field8;

    public Bean3(int id, String group, int field1, int field2, int field3, int field4, int field5, int field6, int field7, int field8) {
        this.id = id;
        this.group = group;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.field5 = field5;
        this.field6 = field6;
        this.field7 = field7;
        this.field8 = field8;
    }

    public String groupby() {
        return this.id + this.group;
    }

    public int getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public int getField1() {
        return field1;
    }

    public int getField2() {
        return field2;
    }

    public int getField3() {
        return field3;
    }

    public int getField4() {
        return field4;
    }

    public int getField5() {
        return field5;
    }

    public int getField6() {
        return field6;
    }

    public int getField7() {
        return field7;
    }

    public int getField8() {
        return field8;
    }

    public SumBean toSumBean() {
        System.out.println("我被转化成 sumbean ID" + this.id);
        return new SumBean(id, field1, field2, field3, field4, field5, field6, field7, field8);
    }
}

// 求和结果类
class SumBean {
    private int id;

    private int field1Sum;
    private int field2Sum;
    private int field3Sum;
    private int field4Sum;
    private int field5Sum;
    private int field6Sum;
    private int field7Sum;
    private int field8Sum;

    public SumBean() {
        System.out.println("我被创建了");
        this.field1Sum = 0;
        this.field2Sum = 0;
        this.field3Sum = 0;
        this.field4Sum = 0;
        this.field5Sum = 0;
        this.field6Sum = 0;
        this.field7Sum = 0;
        this.field8Sum = 0;
    }

    public SumBean(int id, int field1Sum, int field2Sum, int field3Sum, int field4Sum, int field5Sum, int field6Sum, int field7Sum, int field8Sum) {
        this.id = id;
        this.field1Sum = field1Sum;
        this.field2Sum = field2Sum;
        this.field3Sum = field3Sum;
        this.field4Sum = field4Sum;
        this.field5Sum = field5Sum;
        this.field6Sum = field6Sum;
        this.field7Sum = field7Sum;
        this.field8Sum = field8Sum;
    }

    public SumBean combine(SumBean other) {
        System.out.println("this id " + id + " other id " + other.id);


//        this.id = other.id;
        SumBean combinedBean = new SumBean();
        combinedBean.id = this.id; //
        combinedBean.field1Sum = this.field1Sum + other.field1Sum;
        combinedBean.field2Sum = this.field2Sum + other.field2Sum;
        combinedBean.field3Sum = this.field3Sum + other.field3Sum;
        combinedBean.field4Sum = this.field4Sum + other.field4Sum;
        combinedBean.field5Sum = this.field5Sum + other.field5Sum;
        combinedBean.field6Sum = this.field6Sum + other.field6Sum;
        combinedBean.field7Sum = this.field7Sum + other.field7Sum;
        combinedBean.field8Sum = this.field8Sum + other.field8Sum;
        return combinedBean;

    }


    public SumBean combine2(SumBean other) {
        System.out.println("聚合：this id: " + this.id + " other id " + other.id);
//        this.id = other.id;
        this.field1Sum += other.field1Sum;
        this.field2Sum += other.field2Sum;
        this.field3Sum += other.field3Sum;
        this.field4Sum += other.field4Sum;
        this.field5Sum += other.field5Sum;
        this.field6Sum += other.field6Sum;
        this.field7Sum += other.field7Sum;
        this.field8Sum += other.field8Sum;
        return this;
    }

    @Override
    public String toString() {
        return "SumBean{" +
                "id=" + id +
                "field1Sum=" + field1Sum +
                ", field2Sum=" + field2Sum +
                ", field3Sum=" + field3Sum +
                ", field4Sum=" + field4Sum +
                ", field5Sum=" + field5Sum +
                ", field6Sum=" + field6Sum +
                ", field7Sum=" + field7Sum +
                ", field8Sum=" + field8Sum +
                '}';
    }
}
