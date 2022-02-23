package com.seetech.footmassage2.core.util;//package com.seetech.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author lwk
 * @Date 2021/4/26 10:39
 * @Version 1.0
 * @Description
 */
public class CalendeUtil {//todo 使用LocalDateTime & Duration实现
    //过去
    public static final boolean PAST_DATE = true;
    //未来
    public static final boolean FETURE_DATE = false;


    /**
     * 获取当前时间到过去或未来第 past 天的日期（精确到 时 分 秒）
     *
     * @param past          自定义 过去的第 past 天
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return LocalDateTime 类型日期
     */
    public static LocalDateTime getPastLocalDateTimeDate(int past, boolean paseAndfeture) {
        Calendar calendar = Calendar.getInstance();
        if (paseAndfeture) { //过去
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        } else { //未来
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        }
        //获取当前时间
        Date today = calendar.getTime();
        //将date类型的数据转换成精却到时分秒的String类型的日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(today);
        //将String类型的日期转换成 LocalDateTime 类型
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(result, df);
    }

    /**
     * 获取指定时间到过去第 past 天的日期
     *
     * @param past          自定义 过去的第 past 天
     * @param localDateTime 指定的时间
     * @return
     */
    public static LocalDateTime getPastLocalDateTimeDate(int past, LocalDateTime localDateTime) {
        //将指定的时间转换成 Date 类型的时间
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建日历类
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, past);
        String result = format.format(calendar.getTime());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(result, df);
    }


    /**
     * 获取当前时间到过去第 past 天的日期 （精确到 时 分 秒）
     *
     * @param past          自定义 过去的第 past 天
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return String 类型日期
     */
    public static String getPastStringDate(int past, boolean paseAndfeture) {
        Calendar calendar = Calendar.getInstance();
        if (paseAndfeture) { //过去
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        } else { //未来
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        }
        //获取当前时间
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(today);
    }

    /**
     * 获取当前时间到过去第 past 天的日期
     *
     * @param past          自定义 过去的第 past 天
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return Date 类型日期
     */
    public static Date getPastDate(int past, boolean paseAndfeture) {
        Calendar calendar = Calendar.getInstance();
        if (paseAndfeture) { //过去
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        } else { //未来
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        }
        return calendar.getTime();
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals     intervals天内
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return LocalDateTime类型   日期数组
     */
    public static List<LocalDateTime> getPastLocalDateTimeDateList(int intervals, boolean paseAndfeture) {
        List<LocalDateTime> pastDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            if (paseAndfeture) {//过去
                pastDaysList.add(getPastLocalDateTimeDate(i, PAST_DATE));
            } else {//未来
                pastDaysList.add(getPastLocalDateTimeDate(i, FETURE_DATE));
            }
        }
        return pastDaysList;
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals     intervals天内
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return String类型   日期数组
     */
    public static List<String> getPastStringDateList(int intervals, boolean paseAndfeture) {
        List<String> pastDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            if (paseAndfeture) {//过去
                pastDaysList.add(getPastStringDate(i, PAST_DATE));
            } else {//未来
                pastDaysList.add(getPastStringDate(i, FETURE_DATE));
            }
        }
        return pastDaysList;
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals     intervals天内
     * @param paseAndfeture 选择常量 是需要 “过去”(CalenderUtil.PAST_DATE) 还是 “未来”(CalenderUtil.FETURE_DATE)
     * @return Date类型   日期数组
     */
    public static List<Date> getPastDateList(int intervals, boolean paseAndfeture) {
        List<Date> pastDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            if (paseAndfeture) {//过去
                pastDaysList.add(getPastDate(i, PAST_DATE));
            } else {//未来
                pastDaysList.add(getPastDate(i, FETURE_DATE));
            }
        }
        return pastDaysList;
    }


    /**
     * 获取当前时间到第二天凌晨的秒数
     *
     * @return 返回值单位为[s:秒]
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }


//    public static void main(String[] args) throws IOException {
//
//        //下载文件
//        HttpGet httpGet = new HttpGet("https://openapi.tianquetech.com/capital/fileDownload/download?downUrl=https%3A%2F%2Fosscdn.suixingpay.com%2F0%2F2%2F32MDJhYjIyYWZ6N0NhbzJheDIwMjFfMDZfMDlfMDZfMjFfZjhlMjY1NTJiMGI1NGIwYzhlM2QwZjExZDk2ZTQ2Y2QyYUtpY21fYnVja2V0MmFiMQ.csv");
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response = client.execute(httpGet);
//        //随机生成一个文件名
//        String fileName = UUID.randomUUID().toString();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String newData = simpleDateFormat.format(new Date());
//
//
//        System.out.println(response.getEntity());
//        //取到文件流
//        InputStream is = response.getEntity().getContent();
//
//
//        String filepath = "E:\\IDEAL-project\\xr2\\machine-after-sale\\src\\main\\java\\com\\seetech\\machineaftersale\\util\\test\\" + newData + "-" + fileName + ".csv";
//
//        File file = new File(filepath);
//        file.getParentFile().mkdirs();
//        FileOutputStream fileout = new FileOutputStream(file);
//
//        byte[] buffer = new byte[2014];
//        int ch = 0;
//        while ((ch = is.read(buffer)) != -1) {
//            fileout.write(buffer, 0, ch);
//        }
//
//        System.err.println("成功！！");
//
//
//        //成功后删除文件
//        file.delete();
//
////        String filePath = "C:\\Users\\xr\\Downloads\\97039056_20210609_trade.csv";
////
////        try {
////            // 创建CSV读对象
////            CsvReader csvReader = new CsvReader(filePath);
////
////            // 读表头
////            csvReader.readHeaders();
////            while (csvReader.readRecord()){
////                // 读一整行
////                System.out.println(csvReader.getRawRecord());
////                // 读这行的某一列
////                System.err.println(csvReader.get("商户订单号"));
////            }
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }
}
