package com.kane.practice.concurrent.threadlocal;


import net.dongliu.requests.Session;

public class ThreadLocalScene {

    // 1. ThreadLocal的使用场景 session
    private static final ThreadLocal threadSession = new ThreadLocal();

    public static Session getSession() {
        Session s = (Session) threadSession.get();

        if (s == null) {
            //伪代码
            // s = new Session();
            // threadSession.set(s);
        }
        return s;
    }


    //2 . ThreadLocal的使用场景 事务
    //3. ThreadLocal的使用场景 数据跨层传递（controller,service, dao）  例如：租户信息，版本信息

    public static void main(String[] args) {
        User user = new User("jack");
        new Service1().service1(user);
    }

    static class Service1 {
        public void service1(User user) {
            //给ThreadLocal赋值，后续的服务直接通过ThreadLocal获取就行了。
            UserContextHolder.holder.set(user);
            new Service2().service2();
        }
    }

    static class Service2 {
        public void service2() {
            User user = UserContextHolder.holder.get();
            System.out.println("service2拿到的用户:" + user.name);
            new Service3().service3();
        }
    }

    static class Service3 {
        public void service3() {
            User user = UserContextHolder.holder.get();
            System.out.println("service3拿到的用户:" + user.name);
            //在整个流程执行完毕后，一定要执行remove
            UserContextHolder.holder.remove();
        }
    }

    static class UserContextHolder {
        //创建ThreadLocal保存User对象
        public static ThreadLocal<User> holder = new ThreadLocal<>();
    }

    static class User {
        String name;

        public User(String name) {
            this.name = name;
        }
    }


}
