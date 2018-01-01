import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

public class Java8Stream {

    public static final String P_L = "\\s+";
    static String contents = "contents Ps = new String(Files. readAllBytes( Paths. get( \"Java8Stream.java\")), StandardCharsets.UTF_8)";
    Stream<String> words = Stream.of(contents.split(P_L));//数组方式
    @BeforeClass
    public static void setup() throws IOException {
        //   contents = new String(Files.readAllBytes(Paths.get("Java8Stream.java")), StandardCharsets.UTF_8);
    }

    @Test //2.1 从迭代器到Stream操作
    public void testIterator() throws IOException {

        List<String> list = Arrays.asList(contents.split(P_L));


        int count = 0;
        for (String s : list) {
            if (s.length() > 12)
                count++;
        }
        System.out.println("===迭代方式===" + count);


        long streamCount = list.stream().filter(w -> w.length() > 12).count();
        System.out.println("===Stream===" + streamCount);

        long streamParallelCount = list.parallelStream().filter(w -> w.length() > 12).count();
        System.out.println("===parallelStream===" + streamParallelCount);//并行执行过滤和统计


    }


    @Test//2.2 创建Stream
    public void testCreateStream() {

        Stream<String> words = Stream.of(contents.split(P_L));//数组方式
        Stream<String> words1 = Stream.of("123", "456", "789");//可变参数方式
        Stream<String> words2 = Stream.of();//数组方式

        //创建不含任何元素的Stream
        Stream<String> empty = Stream.empty();//泛型推导


        //创建一个含常量值的Stream
        Stream<String> echos = Stream.generate(() -> "Echo");
        Stream<String> echosInner = Stream.generate(new Supplier<String>() {
            @Override
            public String get() {
                return "Echo";
            }
        });

        //创建一个随机数字的Stream
        Stream<Double> random = Stream.generate(Math::random);
        Stream<Double> randomInner = Stream.generate(new Supplier<Double>() {
            @Override
            public Double get() {
                return Math.random();
            }
        });


        //创建一个 0 1 2 3 ... 的无限序列
        Stream<BigInteger> bigIntegerStream = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
        Stream<BigInteger> bigIntegerInner = Stream.iterate(BigInteger.ZERO, new UnaryOperator<BigInteger>() {
            @Override
            public BigInteger apply(BigInteger bigInteger) {
                return bigInteger.add(BigInteger.ONE);
            }

        });


        Stream<String> stringStream = Pattern.compile(P_L).splitAsStream(contents);


    }


    @Test//2.3 filter、map和flatMap方法
    public void testFilterAndMapAndFlatMap() {

        //   Stream<String> stringStream = Pattern.compile(P_L).splitAsStream(contents);
        List<String> list = Arrays.asList(contents.split(P_L));
        Stream<String> stream = list.stream();

        Stream<String> stringStream = stream.map(String::toUpperCase);
        System.out.println(Arrays.asList(stringStream.toString()));

        Stream<String> stream1 = list.stream();
        Stream<Character> stringStream1 = stream1.map(x -> x.charAt(0));
        System.out.println(Arrays.asList(stringStream1.toArray()));


        Stream<String> stream2 = list.stream();
        Stream<Stream<Character>> streamCharacter = stream2.map(w -> characterStream(w));
        System.out.println(Arrays.asList(streamCharacter.toArray()));

        Stream<String> stream3 = list.stream();
        Stream<Character> streamCharacter3 = stream3.flatMap(w -> characterStream(w));//产生扁平化的流
        System.out.println(Arrays.asList(streamCharacter3.toArray()));

    }

    public static Stream<Character> characterStream(String s) {
        List<Character> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.add(c);
        }
        return result.stream();


    }


    //2.4提取子流和组合流
    @Test
    public void testExtractSubStreamAndCombinationStream() {
        //limit(n); 产生指定n长度的新流,原始流长度小于n,返回原始
        Stream<Double> limit = Stream.generate(Math::random).limit(10);
        System.out.println(Arrays.toString(limit.toArray()));

        //skip(n),丢掉前面指定的n个元素
        Stream<String> split = Stream.of(contents.split(P_L)).skip(2);
        System.out.println(Arrays.toString(split.toArray()));

        //concat 合并两个流
        Stream<Character> helloWorld = Stream.concat(characterStream("Hello"), characterStream(" World"));
        printStream(helloWorld);


        //peek 产生与原始流具有相同的元素的流,  但是每次获取元素时候,都会调用一个函数,方便调试
        Object[] objects = Stream.iterate(1.0, p -> p * .2).peek(x -> System.out.println("Fetch..." + x)).limit(20).toArray();
        System.out.println(Arrays.toString(objects));


    }
    private <T> void printStream(Stream<T> helloWorld) {System.out.println(Arrays.toString(helloWorld.toArray()));}

    //2.5 有状态的转换
    @Test
    public void testStateTransitions() {

        //distinct 产生具有相同顺序的,不重复的新流
        Stream<String> uniqueWords = Stream.of("123", "123", "123", "456").distinct();
        printStream(uniqueWords);//[123, 456]

        //产生排序的流
        Stream<String> sorted = words.sorted(Comparator.comparing(String::length).reversed());
        printStream(sorted);


    }

    //2.6 简单的聚合方法:将流聚合为一个值,以便在程序中使用,聚合方法都是终止操作,流终止操作后就不能在应用其他操作
    @Test
    public void simpleAggregationMethod() {
        //count():返回流中总数
        //min():返回流中最小值
        //max():返回流中最大值
        Optional<String> largest = words.max(String::compareToIgnoreCase);
        //Optional 表示可能会分装返回值,也可能没有返回(流为空的时候)
        if (largest.isPresent()) {
            System.out.println("largest:"+ largest.get());
        }



        //返回非空集合的第一个值,通常和filter一起使用
        Optional<String> first = Stream.of("123 456 Q1 793 Q2 q3".split("\\s")).filter(x->x.startsWith("Q")).findFirst();
        if (first.isPresent()){
        System.out.println(first.get());
        }


        //返回非空集合的第一个值,通常和filter一起使用
        Optional<String> second = getSplitStream().parallel().filter(x -> x.startsWith("Q")).findAny();
        if (first.isPresent()) {
            System.out.println(second.toString());
        }


        //判断是否匹配
        // parallel()并行提高速度
        //anyMatch 任意一个
        boolean q = getSplitStream().parallel().anyMatch(x -> x.startsWith("Q"));
        assertTrue(q);
        //全部匹配
         q = getSplitStream().parallel().allMatch(x -> x.startsWith("Q"));
        assertFalse(q);
        //都没有
        q = getSplitStream().parallel().noneMatch(x -> x.startsWith("Q"));
        assertFalse(q);
    
    
    }
    private Stream<String> getSplitStream() {return Stream.of("123 456 Q1 793 Q2 q3 Qr".split("\\s"));}


    //2.7 Optional类型:: 使用Optional值
    @Test
    public void testOptionalType_UseOptionalValues() {

        //Optional<T> 对象或者是一个T类型对象的分装,或者不表示任何对象,不会返回null
        Optional<String> optionalValue = words.max(String::compareToIgnoreCase);
        if (optionalValue.isPresent()) {//isPresent,判断对象是否有值
            System.out.println("largest:" + optionalValue.get().toUpperCase());//get()返回该对象,否则抛出一个NoSuchElementException
        }


        //ifPresent存在可选值的时候传递给函数
        List<String> result = new ArrayList<>();
        optionalValue.ifPresent(e-> result.add(e));
        Assert.assertEquals(1,result.size());
        optionalValue.ifPresent(result::add);
        Assert.assertEquals(2,result.size());
        Assert.assertEquals(2,result.size());

        List<String> result2 = new ArrayList<>();
        Optional<Boolean> aBoolean = optionalValue.map(result2::add);// 产生3个值,true,false,null
        Assert.assertEquals(1, result2.size());



        //orElse没有可选值的时候,使用一个默认值
        Stream<String> distinct = Stream.of("1 13 14 25 13  22".split("4\\dds")).distinct();
        Optional<String> optionalString = distinct.findAny();
        //orElse没有可选值的时候,使用一个默认值
        String s = optionalString.orElse("");
     //   assertEquals("1 13 14 25 13  22",s);
        //orElseGet user.dir 通过计算设计默认值
        String userDir = optionalString.orElseGet(()->System.getProperty("user.dir"));
        //assertEquals("1 13 14 25 13  22", userDir);

        //orElseTrhrow  没有值的时候抛出异常
        String orElseThrow = optionalString.orElseThrow(NoSuchElementException::new);
       // assertEquals("1 13 14 25 13  22", orElseThrow);



    }
    //2.7 Optional类型   2.7.2 创建可选值   //2.7 Optional类型:: 使用Optional值
    @Test
    public void testOptionalType_CreateAnOptionalValue() {
        Optional<String> optionalValue = words.max(String::compareToIgnoreCase);
        Optional<Integer> integer = optionalValue.ofNullable(2);
        final Optional<Double> inverse = inverse(3.);
    
    }

    public static  Optional<Double> inverse(Double x) {

        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    //2.7.3 使用flatMap来组合可选值函数
    @Test
    public void testOptionalType_flatMap() {

        Optional<Double> result = inverse(Math.random()).flatMap(Java8Stream::squareRoot);


    }

    public static Optional<Double> squareRoot(Double x) {

        return x == 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }


    @Test//2.8 聚合操作
    public void testAggregationOperations
   () {

        Random random = new Random(47);
        //reduce
        Stream<Integer> values = getIntegerStream(random);
        //reduce将所有元素组合成一个值
        Optional<Integer> sum = values.reduce((x, y) -> x + y);
      assertEquals(17,sum.get().intValue());


        Optional<Integer> sum2 = getIntegerStream(random).reduce(Integer::sum);
        assertEquals(18, sum2.get().intValue());

        //第二种形式设置一个元素作为起点,当流为空时候返回标识值
        Integer sum3 = getIntegerStream(random).reduce(0, (x, y) -> x + y);
        assertEquals(18, sum3.intValue());

        //第三种形式,提供2个函数
        int result = words.reduce(0, (total, world) -> total + world.length(), (total1, total2) -> total1 + total2);
    
    
    }
    private Stream<Integer> getIntegerStream(Random random) {return Stream.generate(() -> random.nextInt(10)).limit(4);}

    //2.9 收集结果::流处理完之后查看结果
    @Test
    public void testCollectTheResults() {

        //1传递一个正确类型的数组构造器
        String[] strings = words.toArray(String[]::new);


        //2放置进一个HashSet,线程不安全的,需要使用collect()

        //1 一个创建目标类型的实例的方法
        //2一个添加方法
        //3 将两个对象整合到一起的方法
        HashSet<Object> collect = getSplitStream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        System.out.println(collect);
        
        
        
        
        //使用快捷方法
        List<String> list = getSplitStream().collect(toList());
        Set<String> collect1 = getSplitStream().collect(toSet());
        TreeSet<String> collect2 = getSplitStream().collect(toCollection(TreeSet::new));

        //处理结果
        String collect3 = getSplitStream().collect(joining());
        String collect4 = getSplitStream().collect(joining(";"));


        //包含字符串以外的对象,需要首先转为字符串
        String collect5 = getSplitStream().map(Objects::toString).collect(joining("-"));


        //将流聚合为一个总和,平均值,最大值或者最小值
        IntSummaryStatistics summary = getSplitStream().collect(summarizingInt(String::length));

        double average = summary.getAverage();
        double count = summary.getCount();
        double max = summary.getMax();
        double min = summary.getMin();

    }



    //2.10 将结果收集到Map中
    @Test
    public void testCollectTheResultsInTheMap() {

        //Stream<List<People>> people = Stream.of(Arrays.asList(new Per(), new People())).limit(5);
        List<People> peoples = Arrays.asList(new People(), new People());
        Stream<People> person = peoples.stream();

        //1.生成键和值
        Map<Long, String> collect = person.collect(toMap(People::getId, People::getName));
        Stream<People> person2 = peoples.stream();

        //2.将实际元素作为值
        Map<Long, People> collect1 = person2.collect(toMap(People::getId, Function.identity()));


        //3.1多个元素具有相同的键,会抛出异常重复键,则提供第三个函数,决定键值的值,
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> collect2 = locales.collect(toMap(l -> l.getDisplayLanguage(), l -> l.getDisplayLanguage(l), (existingValue, newValue) -> existingValue));//保留旧值


        //3.1.2多个元素具有相同的键,会抛出异常重复键,则提供第三个函数,决定键值的值
        Stream<Locale> locales2 = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> stringSetMap = locales2.collect(toMap(l -> l.getDisplayCountry(), l -> Collections.singleton(l.getDisplayLanguage()), (a, b) -> {
            Set<String> strings = new HashSet<String>(a);
            strings.addAll(b);
            return strings;
        }));//值是一个set



        //得到一个TreeMap
        Stream<People> stream = peoples.stream();
        TreeMap<Long, People> collect3 = stream.collect(toMap(People::getId, Function.identity(), (existingValue, newValue) -> {
            throw new IllegalStateException();
        }, TreeMap::new));


    }

    //2.11 分组和分片
    @Test
    public void testGroupingAndsharding() {

        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());

       //对共同特性的值进行分组groupingBy
        Map<String, List<Locale>> countryToLocales = locales.collect(groupingBy(Locale::getCountry));
        //保留旧值
        List<Locale> ch = countryToLocales.get("CH");
        System.out.println(ch);



//使用predicate函数,流被分成组,一组返回true,另一组返回false,这种情况下partitioningBy比groupingBy更有效率
        Stream<Locale> stream = Stream.of(Locale.getAvailableLocales());
        Map<Boolean, List<Locale>> en = stream.collect(partitioningBy(l -> l.getLanguage().equals("en")));
        System.out.println(en.get(true));

//使用predicate函数,获得一个并发的map,当用于并行流时候可以并发地插入值,等同 toConcurrentMap
        Stream<Locale> stream2 = Stream.of(Locale.getAvailableLocales());
        Map<Boolean, List<Locale>> en2 = stream2.collect(groupingByConcurrent(l -> l.getLanguage().equals("en")));
        System.out.println(en2);


        //Collectors.toSet()希望map的值是set而不是List
        Stream<Locale> stream3 = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<Locale>> collect = stream3.collect(groupingBy(Locale::getCountry, toSet()));
        System.out.println(collect);


        //分组后的元素总个数
        Stream<Locale> stream4 = Stream.of(Locale.getAvailableLocales());
        Map<String, Long> collect4 = stream4.collect(groupingBy(Locale::getCountry, counting()));
        System.out.println(collect4);


        //分组后的元素求和
        Stream<Locale> stream5 = Stream.of(Locale.getAvailableLocales());
        //Map<String, Integer> collect5 = stream5.collect(Collectors.groupingBy(City::getDisplayCountry, Collectors.summarizingInt(Locale::getPopulation)));
        //System.out.println(collect5);

  //分组后的元素求最大值Collectors.maxBy(Locale::getCountry)
  //      Stream<Locale> stream6 = Stream.of(Locale.getAvailableLocales());
        //Map<String, Integer> collect6 = stream6.collect(groupingBy(Locale::getLanguage, maxBy(Locale::getDisplayLanguage)));
        //System.out.println(collect6);



  //分组后的元素求最小值 Collectors.mminBy(Locale::getCountry)
  //      Stream<Locale> stream6 = Stream.of(Locale.getAvailableLocales());
        //Map<String, Integer> collect6 = stream6.collect(Collectors.groupingBy(Locale::getLanguage, Collectors.mminBy(Locale::getCountry)));
        //System.out.println(collect6);



        //mapping
        Stream<Locale> locales1 = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> collect2 = locales1.collect(groupingBy(l -> l.getDisplayCountry(), mapping(l -> l.getDisplayLanguage(), toSet())));
        System.out.println(collect2);

        Stream<Locale> locales2 = Stream.of(Locale.getAvailableLocales());
        Map<String, Optional<String>> collect1 = locales2.collect(groupingBy(Locale::getDisplayCountry, mapping(Locale::getDisplayLanguage, maxBy(Comparator.comparing
                (String::length)))));
        System.out.println(collect1);



        //reducing()
        Stream<Locale> localesReducing = Stream.of(Locale.getAvailableLocales());
        Map<String, String> collect3 = localesReducing.collect(groupingBy(Locale::getDisplayCountry, reducing("", Locale::getDisplayLanguage, (s, t) -> s.length() == 0 ? t : s +
                ", " + t)));
        System.out.println(collect3);




    
    }

    //2.12原始类型流
    @Test
    public void testPrimitiveTypeStream() {
      //基本类型 short,char,byte,和boolean使用IntStream
        IntStream intStream = IntStream.of(0, 1, 2, 3, 4, 5);
        //产生0到99,不包括上线100
        IntStream range = IntStream.range(0, 100);
  //产生0到99,包括上线100
        IntStream rangeClosed = IntStream.rangeClosed(0, 100);

        //将对象流转换为原始流
        IntStream intStream1 = words.mapToInt(String::length);
        LongStream longStream1 = getSplitStream().mapToLong(String::length);
        DoubleStream doubleStream1 = getSplitStream().mapToDouble(String::length);
    
        //将原始类型流转换为对象流,使用boxed()
        Stream<Integer> boxed = IntStream.range(0, 100).boxed();


        //float,double
        DoubleStream doubleStream = DoubleStream.of(0, 1, 2, 3, 4, 5);
    
    
        //long
        LongStream longStream = LongStream.of(0, 1, 2, 3, 4, 5);
    
    
    }

 //2.13 并行流
 @Test
    public void testParallelStream() {
     //默认是一个串行流

     //parallel()产生一个并行流,产生的结果应该和串行流一样的结果
     Stream<String> stream = Stream.of("1", "33", "11", "53", "15", "13", "15", "13").parallel();



     //unordered()放弃有序来加快limit方法的速度
     Stream<String> limit = stream.parallel().unordered().limit(5);


 }


 @Test
    public void test() {

    }


}


class People {

    long getId(){
        Random random = new Random();
        return random.nextInt(1000);
    }

     String getName() {
        return "默认方法";
    }

}
