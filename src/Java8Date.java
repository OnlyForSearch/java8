import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 第5章 新的日期和时间API
 *
 * @author fengyu .
 * @Date 2018-01-01 15:08
 */
public class Java8Date {

    //5.1 时间线
    @Test
    public void testTimeline() throws InterruptedException {

        Instant start = Instant.now();

        TimeUnit.SECONDS.sleep(4);

        Instant end = Instant.now();
        Duration between = Duration.between(start, end);//计算连个时间点的距离
        System.out.println(between.toMillis());
        System.out.println(between.toHours());
        System.out.println(between.toMinutes());
        System.out.println(between.toDays());

        //时间计算
        //between.plus();
    }


    //    5.2 本地日期
    @Test
    public void testLocalDate() {
        LocalDate now = LocalDate.now();//今天的日期
        LocalDate alonezosBirthday = LocalDate.of(1903, 6, 14);
        alonezosBirthday = LocalDate.of(1903, Month.JUNE, 14);
        alonezosBirthday = LocalDate.of(2014, 1, 1).plusDays(255);//计算一年中的第256天

        alonezosBirthday.plus(Period.ofYears(1));//获取下一年的生日日期;
        //等价
        alonezosBirthday.plusYears(1);

        Period until = alonezosBirthday.until(now);
        //获取两个本地时间的距离
        long until1 = alonezosBirthday.until(now, ChronoUnit.DAYS);
        //获取两个本地时间的距离,按天算的

        LocalDate.of(2016, 1, 31).plusMonths(1);//加一个月 2016.02.29
        LocalDate.of(2016, 3, 31).minusMonths(1);//减一个月2016.02.29

        int value = LocalDate.of(1900, 1, 1).getDayOfWeek().getValue();//获取星期几
        System.out.println("获取星期几:" + value + ":" + DayOfWeek.of(value));


    }


    ///5.3 日期校正器
    @Test
    public void testDateCorrector() {

        LocalDate firstTusday = LocalDate.of(2017, 1, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));


    }
    //5.4 本地时间
    @Test
    public void testLocalTime() {
        LocalTime rightNow = LocalTime.now();

        LocalTime bedTime = LocalTime.of(22, 33);
        LocalTime localTime = bedTime.plusHours(8);//早上6点半醒来


    }


    //    5.5 带时区的时间
    @Test
    public void testTimeZoneWithTime() {

        ZonedDateTime of = ZonedDateTime.of(1969, 7, 16, 9, 32, 0, 0, ZoneId.of("America/New_York"));
        System.out.println(of);//1969-07-16T09:32-04:00[America/New_York]
        //用法和LocalDateTime一样

    }

    //5.6 格式化和解析
    //@Test
    public void testFormattingAndParsing() {

        //DateTimeFormatter.ISO_DATE_TIME.format(LocalDate.now());


        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        String format = formatter.format(LocalDate.now());
        System.out.println(format);


        //本地语言环境
        String format1 = formatter.withLocale(Locale.FRANCE).format(LocalDate.now());
        System.out.println(format1);


        //自定义日期格式
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm");
        System.out.println(formatter1.format(LocalDate.now()));



        //解析字符串日期
        LocalDate parse = LocalDate.parse("1903-06-14");
    
        ZonedDateTime parse1 = ZonedDateTime.parse("1969-07-16 03:32:00-0400", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxx"));
    
    }


}
