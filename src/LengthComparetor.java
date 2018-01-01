import java.util.Comparator;

/**
 * @author fengyu .
 * @Date 2017-12-31 18:44
 */

@FunctionalInterface
        //函数式接口标记注解
interface FuncInterface extends Comparator<String> {}

class LengthComparetor implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(o1.length(), o2.length());
    }
}

interface Person {

    long getId();

    default String getName() {
        return "默认方法";
    }

}

@FunctionalInterface
interface Buttonlistener {

    void listener(String name);


}

class Buttons {

    String setOnAction(Buttonlistener listener) {
        System.out.println("Buttons.setOnAction");
    listener.listener(" listener.listener::");
        return "......方法引用.";
    }
}


@FunctionalInterface
interface ClassAndStaticMethod {

    double staticMetod(double i, double j);


}


class ClassAndStaticMethods {

    public void method(ClassAndStaticMethod method) {
        double i = method.staticMetod(4.0, 5.0);
        System.out.println("ClassAndStaticMethods.method===" + i);
    }

}