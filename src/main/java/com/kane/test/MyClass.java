package com.kane.test;

import java.lang.reflect.Field;

public class MyClass {
    private int field1;
    private int field2;
    private int field3;
    // ... 这里省略了其他字段

    public MyClass(int field1, int field2, int field3 /* ... 这里省略了其他字段 */) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        // ... 设置其他字段的值
    }

    public void addToFields(MyClass other) throws IllegalAccessException {
        Class<?> clazz = MyClass.class;
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == int.class) {
                int value1 = field.getInt(this);
                int value2 = field.getInt(other);
                field.setInt(this, value1 + value2);
            }
        }
    }

    // ... 其他代码

    public static void main(String[] args) throws IllegalAccessException {
        MyClass obj1 = new MyClass(1, 2, 3 /* ... 这里省略了其他字段 */);
        MyClass obj2 = new MyClass(4, 5, 6 /* ... 这里省略了其他字段 */);

        obj1.addToFields(obj2);

        // 输出结果
        System.out.println("field1: " + obj1.getField1());
        System.out.println("field2: " + obj1.getField2());
        System.out.println("field3: " + obj1.getField3());
        // ... 输出其他字段的值
    }

    // ... Getter和Setter方法
    // 这里省略了Getter和Setter方法的实现，你需要根据字段的类型适当修改
    public int getField1() {
        return field1;
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }

    public int getField3() {
        return field3;
    }

    public void setField3(int field3) {
        this.field3 = field3;
    }

}

