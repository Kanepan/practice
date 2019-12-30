/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.bugs;
import com.kane.practice.utils.ProtostuffUtils;
import com.kane.practice.utils.http.SupplyResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 ** 新增字段放在 中间或者属性的最上面
 *  1. 字段类型如果是String,则反序列化没问题
 *  2. 如果是 List 或者,不定长的.则反序列化有问题
 * * 如果放在字段的最下面,则不影响反序列化.
 *
 * 结论,最好对dto新增字段要放在最下面.
 */

public class ProtostuffTest {

//    private static RuntimeSchema<SupplyResult> schema = RuntimeSchema.createFrom(SupplyResult.class);

    public static void main(String[] args) {


        encode();

        decode();


    }



    private static void encode(){
        //encode
        SupplyResult<MyBean2> source = new SupplyResult();
        MyBean2 bean2 = new MyBean2();
        bean2.setFlag(10);
        bean2.setName("kane");

        SubBean subBean = new SubBean();
        subBean.setTest("im sub");
        List<SubBean> list = new ArrayList();
        list.add(subBean);
//        bean2.setList(list);
        bean2.setNewF("hahah");

        source.setModule(bean2);
        source.setSuccess();

        //        byte[] bytes = ProtostuffIOUtil.toByteArray(source, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

        byte[] bytes = ProtostuffUtils.serialize(source);

        File file  = new File("d:\\protoTest");
        try {
            FileUtils.writeByteArrayToFile(file,bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decode(){
        File file  = new File("d:\\protoTest");
        try {
            byte[] bytes =  FileUtils.readFileToByteArray(file);

//            SupplyResult result = schema.newMessage();
            SupplyResult result = null;
            try {
//                ProtostuffIOUtil.mergeFrom(bytes, result, schema);

                result =  ProtostuffUtils.deserialize(bytes,SupplyResult.class);
            } catch (Exception e) {
                return;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    static class SubBean {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        @Override
        public String toString() {
            return "SubBean{" +
                    "test='" + test + '\'' +
                    '}';
        }
    }


    static class MyBean2  implements Serializable {
        private static final long serialVersionUID = 2L;
       // private List<SubBean> list;
        private String newF ;
        private String name;
        private Integer flag;

        public String getNewF() {
            return newF;
        }

        public void setNewF(String newF) {
            this.newF = newF;
        }

        @Override
        public String toString() {
            return "MyBean2{" +
                    "newF='" + newF + '\'' +
                    ", name='" + name + '\'' +
                    ", flag=" + flag +
                    '}';
        }

//                @Override
//        public String toString() {
//            return "MyBean2{" +
//                    "name='" + name + '\'' +
//                    ", flag=" + flag +
//                    '}';
//        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }
    }
}
