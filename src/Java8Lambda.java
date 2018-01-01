import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.sort;
import static junit.framework.Assert.assertEquals;

public class Java8Lambda {

    static String[] string = new String[]{"1", "dd", "11", "aa", "ad"};
    private String userName;

    @Test
    public  void testLambda() {
        sort(string, new LengthComparetor());
        sort(string, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });//匿名内部类
        sort(string, (String first, String second) -> Integer.compare(first.length(), second.length()));
        //参数,->箭头,表达式,多个表达式使用{}


        sort(string, (String first, String second) -> {//多个表达式用{}
            if (first.length() > second.length())
                return 1;
            if (first.length() < second.length())
                return 1;
            return 0; //必须所有分支都返回
            // return;//必须所有分支都返回
        });

        Comparator<String> lengthComparator = (first, second) -> Integer.compare(first.length(), second.length());//表达式类型推倒
        sort(string, lengthComparator);


        EventHandler<ActionEvent> listener = event -> System.out.println("Thanks");
        //等价==>
        EventHandler<ActionEvent> listener2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Thanks");

            }
        };


        Comparator<String> lengthComparator2 = (final String first, @Deprecated final String second) -> Integer.compare(first.length(), second.length());//表达式类型推倒,可以加参数类型和注解,
        // 无需使用return
        sort(string, lengthComparator2);

        BiFunction<String, String, Integer> bi = (s1, s2) -> 1;
        assertEquals(1, bi.apply("1", "3").intValue());

    }

    @Test//测试函数式接口函数接口是只有一个抽象方法的接口，用作Lambda 表达式的类型。@FunctionalInterface
    public void testFunctionInterface() {

    }


    @Test//方法引用
    public void testMethodReference() {

        Buttons buttons = new Buttons();
        System.out.println("=====对象::实例方法=====");
        System.out.println("=====lambda方式=====");
        //void listener (String name);
        // listener.listener(" listener.listener::");
        buttons.setOnAction(name -> System.out.println(name));
        buttons.setOnAction((name) -> System.out.println(name));

        System.out.println("=====方法引用::=====");
        buttons.setOnAction(System.out::println);

        System.out.println("=====匿名类的实现方式=====");
        buttons.setOnAction(new Buttonlistener() {
            @Override
            public void listener(String name) {
                System.out.println(name);
            }
        });

        buttons.setOnAction(this::equals);
        buttons.setOnAction(x -> this.equals(x));


        System.out.println("=====1 类::实例方法=====");
        //方法引用
        // 1 类::实例方法,第一个参数会成为执行方法的对象
        Arrays.sort(string, String::compareToIgnoreCase);// 等价

        Arrays.sort(string, (x, y) -> x.compareToIgnoreCase(y));//等价

        Arrays.sort(string, new Comparator<String>() {
            @Override
            public int compare(String x, String y) {
                return x.compareToIgnoreCase(y);//第一个参数会成为执行方法的对象
            }
        });//匿名内部类

        System.out.println("=====1 类::静态方法 =====");
        //2类::静态方法 ,方法引用等同与提供方法参数的lambda表达式
        ClassAndStaticMethods c = new ClassAndStaticMethods();
        c.method(Math::pow);
        c.method((i, j) -> Math.pow(i, j));     // 等价
        c.method(new ClassAndStaticMethod() {
            @Override
            public double staticMetod(double i, double j) {
                return Math.pow(i, j);
            }
        });     // 等价


    }
    private String formatUserName(String name) {
        return name;
    }

    @Test
    public void testMethodReference2() {

        class Greeter {

            public void greet() {
                System.out.println("11");
            }
        }
        class ConcurrentGreeter extends Greeter {

            public void greet() {
                Thread thread = new Thread(super::greet);//调用父类中greet
                thread.start();
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ConcurrentGreeter.super.greet();
                    }
                });//等价
                thread2.start();

            }
        }

        Greeter greeter = new ConcurrentGreeter();
        greeter.greet();

        class ConcurrentGreeter2 extends Greeter {

            public void greet() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                thread.start();
                super.greet();

            }
        }


    }

    @Test //构造器引用
    public void testConstructorReference() {
        List<String> lables = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));


        Stream<Button> stream = lables.stream().map(Button::new);////构造器引用



  /*      List<Button> collect = stream.collect(Collectors.toList());
        System.out.println(collect);*/
    }

    private void constructorReference(int [] i) {

    }


    @Test //变量作用域
    public void testParameterScope() throws InterruptedException {
        repeatMessage("变量作用域", 1000);
        TimeUnit.SECONDS.sleep(4);

        List<String> strings = new ArrayList<>();
        for (String p : strings) {
            new Thread(() -> {
                strings.add(p);
            });
        }

        String s1;
        //Comparator<String>comparator=(s1,second)-> s1.compareToIgnoreCase(s1);

    }
    public static void repeatMessage(String text, int count) {
        Runnable r = () -> {
            //  count--;发生错误,自由变量指的是那些不是参数并且不再代码中定义的,自由变量不能被修改,不能更改已经捕获的变量的值在lambda中,因为lambda表达式中的变量不是线程安全的
            for (int i = 0; i < count; i++) {
                System.out.println(text + "->" + i);
                Thread.yield();

            }

        };
        new Thread(r).start();

    }

    @Test//默认方法
    public void testDefualtMethod() {

        List<String> list = new ArrayList<>(Arrays.asList("1", "dd", "11", "aa", "ad"));
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("=============");
        list.forEach(System.out::println);


    }

    @Test//静态方法
    public void testStaticMethod() {

        Comparator.comparing(Person::getName);


    }

    public String getUserName() {
        return userName;
    }
}
