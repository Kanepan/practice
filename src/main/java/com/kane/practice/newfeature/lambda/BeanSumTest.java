package com.kane.practice.newfeature.lambda;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BeanSumTest {
    public static void main(String[] args) {
        //定义实体类
        class Cat{
            private String name;
            private int num;
            private int price;

            public Cat(String name, int num, int price) {
                this.name = name;
                this.num = num;
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }
        }


        //定义组合类
        class IntSummaryStatisticsList {
            private List<IntSummaryStatistics> list=new ArrayList<>();
            private List<ToIntFunction> columns=new ArrayList<>();

            public IntSummaryStatisticsList() {
                ToIntFunction<Cat> num =Cat::getNum;
                ToIntFunction<Cat> price =Cat::getPrice;
                columns.add(num);
                columns.add(price);
                list.add(new IntSummaryStatistics());
                list.add(new IntSummaryStatistics());
            }

            public void accept(Cat cat) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).accept(columns.get(i).applyAsInt(cat));
                }
            }

            public void combine(IntSummaryStatisticsList other) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).combine(other.list.get(i));
                }
            }

            @Override
            public String toString() {
                String str="[";
                for (int i = 0; i < list.size(); i++) {
                    str=str+list.get(i).toString()+",";
                }
                str=str.substring(0,str.length()-1);
                str+="]";
                return str;
            }
        }

        //重写收集类
        class CollectorImpl<T, A, R> implements Collector<T, A, R> {
            private final Supplier<A> supplier;
            private final BiConsumer<A, T> accumulator;
            private final BinaryOperator<A> combiner;
            private final Function<A, R> finisher;
            private final Set<Characteristics> characteristics;

            CollectorImpl(Supplier<A> supplier,
                          BiConsumer<A, T> accumulator,
                          BinaryOperator<A> combiner,
                          Function<A,R> finisher,
                          Set<Characteristics> characteristics) {
                this.supplier = supplier;
                this.accumulator = accumulator;
                this.combiner = combiner;
                this.finisher = finisher;
                this.characteristics = characteristics;
            }

            CollectorImpl(Supplier<A> supplier,
                          BiConsumer<A, T> accumulator,
                          BinaryOperator<A> combiner,
                          Set<Characteristics> characteristics) {
                this(supplier, accumulator, combiner, castingIdentity(), characteristics);
            }

            @Override
            public BiConsumer<A, T> accumulator() {
                return accumulator;
            }

            @Override
            public Supplier<A> supplier() {
                return supplier;
            }

            @Override
            public BinaryOperator<A> combiner() {
                return combiner;
            }

            @Override
            public Function<A, R> finisher() {
                return finisher;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return characteristics;
            }
        }

        //原始数据
        List<Cat> cats = new ArrayList<>();
        cats.add(new Cat("aaa", 1, 2));
        cats.add(new Cat("aaa", 3, 4));
        cats.add(new Cat("bbb", 5, 6));
        cats.add(new Cat("bbb", 7, 8));

        Map<String, IntSummaryStatisticsList> collect = cats.stream()
                .collect(Collectors.groupingBy(Cat::getName,new CollectorImpl<>(
                        IntSummaryStatisticsList::new,
                        (r,t)-> r.accept(t),
                        (l, r) -> { l.combine(r); return l; }, CH_ID)));
        System.out.println(collect);

    }

    static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    @SuppressWarnings("unchecked")
    static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }
}
