package com.kane.practice.newfeature.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaMapReturnNullTest {

    public static void main(String[] args) {
        List<Bean> beanList = new ArrayList<>();

        beanList.add(new Bean(40, 50, 60));
        beanList.add(new Bean(70, 80, 90));
        beanList.add(new Bean(10, 20, 30));
        beanList.add(null);

        beanList.stream().map(bean -> {
            if(bean == null){
                return null;
            }
            if (bean.getField1() == 40) {
                return null;
            }
            MapedBean mapedBean = new MapedBean();
            mapedBean.setSum(bean.getField1() + bean.getField2() + bean.getField3());

            return mapedBean;
        }).filter(bean -> bean != null).collect(Collectors.toList()).forEach(System.out::println);


    }

    static class MapedBean {
        private int sum;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        @Override
        public String toString() {
            return "MapedBean{" +
                    "sum=" + sum +
                    '}';
        }
    }
}
