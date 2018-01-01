import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 第3章 使用lambda编程
 *
 * @author fengyu .
 * @Date 2018-01-01 14:01
 */
public class Java8LambdaProgramming {


    public static void info(Logger logger, Supplier<String> message) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(message.get());

        }

    }
    //3.1 延迟执行
    @Test
    public void testDelayedImplementation() {

        Logger logger = Logger.getLogger("Java8LambdaProgramming");

        logger.info(() -> "x:1" + Math.random() + ",y:1" + Math.random());
        info(logger, () -> "x:2" + Math.random() + ",y:2" + Math.random());

    }
    //3.2 lambda表达式的参数
    @Test
    public void testLambdaExpressionParameters() {

        String[] names = {"123", "fsd2", "123a", "s1d2", "1233", "sd23"};
        Arrays.sort(names, (s, t) -> Integer.compare(s.length(), t.length()));


        //需要使用参数的时候
        repeat(10,x->System.out.println(x));

        ////不需要使用参数的时候
      repeat(10,()->System.out.println("int n, Runnable action"));
        ;

    }
    public static void repeat(int n, IntConsumer action) {

        for (int i = 0; i < n; i++) {
            action.accept(i);
        }

    }
 public static void repeat(int n, Runnable action){

     for (int i = 0; i < n; i++) {
         action.run();
     }
 }

    //3.3 选择一个函数式接口
    /**


     Supplier
     Consumer
     BiConsumer
     Function
     BiFunction
     UnaryOperator
     BinaryOperator
     Predicate
     BiPredicate




     为原始类型提供的函数式接口
     p  q 为int,long,doublel类型
     P,Q为Int,Long,Double类型

     BooleanSupplier

     DoubleBinaryOperator
     DoubleConsumer
     DoubleFunction
     DoublePredicate
     DoubleSupplier
     DoubleToIntFunction
     DoubleToLongFunction
     DoubleUnaryOperator

     IntBinaryOperator
     IntConsumer
     IntFunction
     IntPredicate
     IntSupplier
     IntToDoubleFunction
     IntToLongFunction
     IntUnaryOperator
     LongBinaryOperator
     LongConsumer
     LongFunction
     LongPredicate
     LongSupplier
     LongToDoubleFunction
     LongToIntFunction
     LongUnaryOperator
     ObjDoubleConsumer
     ObjIntConsumer
     ObjLongConsumer

     ToDoubleBiFunction
     ToDoubleFunction
     ToIntBiFunction
     ToIntFunction
     ToLongBiFunction
     ToLongFunction

     *
     * */
    @Test
    public void testSelectAFunctionalInterface() {

        Image image = new Image("20171128201958.png");
        transform(image, Color::brighter);
    }

    public static Image transform(Image in, UnaryOperator<Color> f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width,height);

        for (int x=0;x<width;x++) {
            for (int y=0;y<height;y++) {
                out.getPixelWriter().setColor(x,y,f.apply(in.getPixelReader().getColor(x,y)));

            }
        }
        return out;
    }


    ///3.4返回函数
    @Test
    public void testReturnFunction() {
        Image image = new Image("E:\\java8\\20171128201958.png");
        //transform(image,(c,factor)->c.deriveColor(0,1,factor,1),1.2);
        Image transform = transform(image, brighter(1.2));
    }

    public static UnaryOperator<Color> brighter(double facter) {
        // ///3.4返回函数
        return c -> c.deriveColor(0, 1, facter, 1);
    }


    //3.5 组合
    @Test
    public void testCompose() {
//效率不高,浪费空间爱你
        Image image = new Image("20171128201958.png");
        Image image2 = transform(image, Color::brighter);
        Image finalImage = transform(image2, Color::grayscale);

        //这样就不会产生中间图片
        Image transform = transform(image, compose(Color::brighter, Color::grayscale));

    }

/**/
    public static<T> UnaryOperator<T> compose(UnaryOperator<T> op1, UnaryOperator<T>op2) {
        // ///
        return t -> op2.apply(op1.apply(t));
    }

 @Test
    //3.6延迟
 public void testDelay() {
     Image image = new Image("20171128201958.png");
     Image finalImage = LatentImage.from(image).transform(Color::brighter).transform(Color::grayscale).toImage();
 }


//3.7 并行操作
 public void testParallelOperation() {

     //当你拥有一个函数式接口的对象,并且需要多次调用它时候,先问问自己是否能够利用并发来进行处理
}

//3.8 处理异常

    @Test
    public void handleException() {
        //lambda表达式抛出异常时候,异常会传递给调用者

    }
//    //同步执行
    public static void doInOrder(Runnable first, Runnable second) {
        first.run();//如果  first.run();抛出异常然后doInOrder方法被终止,   second.run();就永远不会运行
        second.run();

    }
    //异步执行
    public static void doInOrderAsync(Runnable first, Runnable second) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                first.run();//如果  first.run();抛出异常然后,second.run();就永远不会运行,doInOrderAsy()会立即返回并进行另一个线程的工作因此无法让方法重新抛出异常,
                second.run();
            }
        };
        thread.start();


    }

    //处理异常
    public static void doInOrderAsy(Runnable first, Runnable second, Consumer<Throwable>handler) {

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    first.run();
                    second.run();
                } catch (Throwable e) {
                    handler.accept(e);
                }
            }
        };
        thread.start();


    }

    //处理异常
    public static <T> void doInOrderAsy(Supplier<T> first, Consumer<T> second, Consumer<Throwable>handler) {

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    T result = first.get();
                    second.accept(result);
                } catch (Throwable e) {
                    handler.accept(e);
                }
            }
        };
        thread.start();


    }




    //3.9 lambda表达式和泛型

    @Test
    public void testLambdaExpressionsAndGenerics() {

    }
}



class  LatentImage{

    private  Image in;
    private List<UnaryOperator<Color>>pendingOperations;
    public LatentImage(Image in) {
        this.in = in;
    }
    public static LatentImage from(Image in) {

        return new LatentImage(in);
    }

    LatentImage transform(UnaryOperator<Color> f) {
        pendingOperations.add(f);
        return this;
    }

    public Image toImage() {

        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = out.getPixelReader().getColor(x, y);

                for (UnaryOperator<Color> f : pendingOperations) {
                    out.getPixelWriter().setColor(x,y, color);
                }

                //.getCo(x, y, f.apply(in.getPixelReader().getColor(x, y)));

            }
        }
        return out;

    }


}