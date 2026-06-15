package seeya.insight.app.util;

import com.jcraft.jsch.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author : herotice
 * @Date : 2017. 9. 28.
 * @Version :
 * @Description : 공통적으로 쓰이는 기능들 모음
 */
public class CommonUtil {
    public final static int SI_UNIT = 1000;
    public final static int BI_UNIT = 1024;
    public final static int BASE_UNIT = SI_UNIT;

    // 특수문자 존재여부 체크
    public static boolean specialStrExistCheck(String str) {
        String specialText = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*";

        if (!str.matches(specialText)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentMonday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return formatter.format(cal.getTime());
    }

    // 현재 날짜 일요일
    public static String getCurrentSunday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.DATE, 7);
        return formatter.format(cal.getTime());
    }

    // 현재 날짜 주차
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        String week = String.valueOf(cal.get(Calendar.WEEK_OF_MONTH));
        return week;
    }

    // 특정 년,월,주 차에 월요일 구하기
    public static String getMonday(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return formatter.format(cal.getTime());
    }

    // 특정 년,월,주 차에 일요일 구하기
    public static String getSunday(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.DATE, 7);

        return formatter.format(cal.getTime());
    }

    // 특정 일자 기준 월요일 부터 일요일까지 날짜 배열 구하기
    public static String[] getWeekDayArry(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String[] dateArray = new String[7];

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                cal.add(Calendar.DATE, 0);
            } else {
                cal.add(Calendar.DATE, 1);
            }
            dateArray[i] = formatter.format(cal.getTime());
        }

        return dateArray;
    }

    // 특정 일자 기준 월요일 부터 일요일까지 차트에 쓰일 날짜(요일포함) 배열 구하기
    public static String[] getWeekDayChartArry(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM월dd일", Locale.KOREA);
        String[] weekOfDay = new String[] { "월", "화", "수", "목", "금", "토", "일" };
        String[] dateArray = null;
        List<String> dateList = new ArrayList<String>();

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                cal.add(Calendar.DATE, 0);
            } else {
                cal.add(Calendar.DATE, 1);
            }
            dateList.add(formatter.format(cal.getTime()) + "(" + weekOfDay[i] + ")");
        }

        dateArray = dateList.toArray(new String[dateList.size()]);
        return dateArray;
    }

    // 시작일 부터 종료일까지 날짜 배열 구하기
    public static String[] getWeekDayRangeArry(String sDate, String eDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String startDay = sDate.replaceAll("-", "");
        String endDay = eDate.replaceAll("-", "");
        List<String> dateList = new ArrayList<String>();
        String[] strDateArray = null;

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDay.substring(0, 4)), Integer.parseInt(startDay.substring(4, 6)) - 1,
                Integer.parseInt(startDay.substring(6, 8)));

        while (true) {
            dateList.add(formatter.format(cal.getTime()));

            if (endDay.equals(formatter.format(cal.getTime()))) {
                break;
            }
            cal.add(Calendar.DATE, 1);
        }

        strDateArray = dateList.toArray(new String[dateList.size()]);

        return strDateArray;
    }

    // 시작일 부터 종료일까지 날짜 배열 구하기
    public static String[] getWeekDayChartRangeArry(String sDate, String eDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM월dd일", Locale.KOREA);
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String[] weekOfDay = new String[] { "일", "월", "화", "수", "목", "금", "토" };
        String startDay = sDate.replaceAll("-", "");
        String endDay = eDate.replaceAll("-", "");
        String strDateArray[] = null;
        List<String> strDateList = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDay.substring(0, 4)), Integer.parseInt(startDay.substring(4, 6)) - 1,
                Integer.parseInt(startDay.substring(6, 8)));

        while (true) {
            strDateList.add(formatter.format(cal.getTime()) + "(" + weekOfDay[cal.get(Calendar.DAY_OF_WEEK) - 1] + ")");

            if (endDay.equals(formatter2.format(cal.getTime()))) {
                break;
            }
            cal.add(Calendar.DATE, 1);
        }

        strDateArray = strDateList.toArray(new String[strDateList.size()]);

        return strDateArray;
    }

    // 시작일 부터 종료일까지 주의 몇번째 날에 해당하는지 배열 구하기
    public static int[] getDayOfWeekNumArry(String sDate, String eDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String startDay = sDate.replaceAll("-", "");
        String endDay = eDate.replaceAll("-", "");
        List<Integer> dayOfWeekList = new ArrayList<Integer>();
        int[] dayOfWeekArray = null;

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(startDay.substring(0, 4)), Integer.parseInt(startDay.substring(4, 6)) - 1,
                Integer.parseInt(startDay.substring(6, 8)));

        while (true) {
            // 1~7, 일~토
            dayOfWeekList.add(cal.get(Calendar.DAY_OF_WEEK));

            if (endDay.equals(formatter.format(cal.getTime()))) {
                break;
            }
            cal.add(Calendar.DATE, 1);
        }

        dayOfWeekArray = new int[dayOfWeekList.size()];

        for (int i = 0; i < dayOfWeekList.size(); i++) {
            dayOfWeekArray[i] = dayOfWeekList.get(i).intValue();
        }

        return dayOfWeekArray;
    }

    // 전주 월요일, 일요일 일자 구하기
    public static List<String> getBeforeWeekCalculator(Calendar cal) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(cal.getTime());
        List<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        // 오늘이 일요일이면 6일 전으로 설정하고
        // 일요일이 아니라면 일주일전 월요일로 설정한다.
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            // 일요일이면 6일 전으로 설정
            currentCal.add(Calendar.DATE, -6);
        } else {
            // 일주일전 월요일로 설정
            currentCal.add(Calendar.DATE, -7);
            int diff = 2 - cal.get(Calendar.DAY_OF_WEEK);
            //System.out.println(diff + "일");
            currentCal.add(Calendar.DATE, diff);
        }

        result.add(sdf.format(currentCal.getTime()));
        // 일요일을 구하기 위해 6일을 더한다.
        currentCal.add(Calendar.DATE, 6);
        result.add(sdf.format(currentCal.getTime()));

        return result;
    }

    // JSONObject 를 Map 으로 변환한다.
    public static Map<String, Object> convertJsontoMap(JSONObject jsonObj, Map<String, Object> result) {
        try {
            Iterator<String> iter = jsonObj.keys();
            String key = "";

            while (iter.hasNext()) {
                key = String.valueOf(iter.next());

                if (jsonObj.get(key) instanceof JSONObject) {
                    // 해당 데이터가 JSONObject 형태의 데이터 이면. 제귀함수 시전.
                    Map<String, Object> map = new HashMap<String, Object>();
                    result.put(String.valueOf(key), convertJsontoMap(jsonObj.getJSONObject(key), map));
                } else if (jsonObj.get(key) instanceof JSONArray) {
                    // 해당 데이터가 JSONObject 형태의 데이터 이면 이녀석도 jsonObject 단위로 뽑아내서 제귀함수 시전.
                    List<Object> list = new ArrayList<Object>();
                    JSONArray jsonArray = jsonObj.getJSONArray(key);

                    if (jsonArray.length() > 0 && jsonArray.get(0) instanceof JSONObject) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            list.add(convertJsontoMap(jsonArray.getJSONObject(i), map));
                        }
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            list.add(jsonArray.get(i));
                        }
                    }

                    result.put(String.valueOf(key), list);
                } else {
                    // 실제 사용할 데이터 이면.
                    result.put(String.valueOf(key), jsonObj.getString(key));
                }
            }
        } catch (org.json.JSONException jsone) {
            jsone.printStackTrace();
        }
        return result;
    }

    public static String convertByteUnit(String num) {
        double n = 0;
        String[] units = { "", "kb", "Mb", "Gb", "Tb" };
        int unitCnt = 0;

        n = Double.parseDouble(num);

        while (n > BASE_UNIT) {
            unitCnt++;
            n = (n / (double)BASE_UNIT);
        }

        return String.valueOf(((n == 0) ? 0 : (Math.round(n * 100d) / 100d))) + units[unitCnt];
    }

    public static String convertPacketUnit(String num) {
        double n = 0;
        String[] units = { "", "k", "M", "G", "T" };
        int unitCnt = 0;

        n = Double.parseDouble(num);

        while (n > 1000) {
            unitCnt++;
            n = n / 1000d;
        }

        return String.valueOf(((n == 0) ? 0 : (Math.round(n * 100d) / 100d))) + units[unitCnt];
    }

    // SFTP 서버 연결
    public static ChannelSftp connect(String host, int port, String username, String password){
        try {
            //System.out.println("connecting..." + host);
            // 1. JSch 객체를 생성한다.
            JSch jsch = new JSch();
            // 2. 세션 객체를 생성한다(사용자 이름, 접속할 호스트, 포트를 인자로 전달한다.)
            Session session = jsch.getSession(username, host, port);
            // 4. 세션과 관련된 정보를 설정한다.(호스트 정보를 검사하지 않는다.
            session.setConfig("StrictHostKeyChecking", "no");
            // 4. 패스워드를 설정한다.
            session.setPassword(password);
            // 5. 접속한다.
            session.connect();
    
            // 6. sftp 채널을 연다.
            Channel channel = session.openChannel("sftp");
            // 7. 채널에 연결한다.
            channel.connect();
            // 8. 채널을 FTP용 채널 객체로 캐스팅한다.
            //System.out.println("connect success..." + host);
            return (ChannelSftp) channel;
        }catch(Exception e) {
            return null;
        }
    }

    // 파일서버와 SFTP 세션 종료
    public static void disconnect(ChannelSftp channelSftp, Session session) {
        channelSftp.quit();
        session.disconnect();
    }

    // 단일 파일 다운로드
    public static InputStream download(ChannelSftp channelSftp, String dir, String fileNm) {
        InputStream in = null;

        try { // 경로탐색후 inputStream에 데이터를 넣음
            channelSftp.cd(dir);
            in = channelSftp.get(fileNm);

        } catch (SftpException se) {
            se.printStackTrace();
        }

        return in;
    }

    // 다중 파일 다운로드
    @SuppressWarnings("unchecked")
    public static Map<String, InputStream> downloadArray(ChannelSftp channelSftp, String dir, String startFileNm, String endFileNm){
        Vector<ChannelSftp.LsEntry> list        = null;
        Map<String, InputStream> result         = new HashMap<String, InputStream>(); 
        int procCnt                             = 0;
        	
        try{ //경로탐색후 inputStream에 데이터를 넣음
            channelSftp.cd(dir);
            list = channelSftp.ls(dir);
            //System.out.println("### aaa : ls proc...");
            for(ChannelSftp.LsEntry entry : list) {
                String fileName = entry.getFilename();
                if(!".".equals(fileName) && !"..".equals(fileName)) {
                    double start    = Double.parseDouble(startFileNm);
                    double end      = Double.parseDouble(endFileNm);
                    double current  = Double.parseDouble(fileName.split("_")[2]);
                    if(current >= start && current <= end) {
                        result.put(fileName, channelSftp.get(fileName));
                        //System.out.println("### fileName : " + fileName);
                        procCnt++;
                    }
                }
            }
            //System.out.println("### procCnt : " + procCnt);
            if(procCnt == 0) {
                result = null;
            }
        }catch(SftpException se){
            se.printStackTrace();
            result = null;
        }
        //System.out.println("### result : " + result);
        return result;
    }

    // 단일 파일 업로드
    public static void upload(ChannelSftp channelSftp, String dir, File file) {
        FileInputStream in = null;

        try { // 파일을 가져와서 inputStream에 넣고 저장경로를 찾아 put
            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());
        } catch (SftpException se) {
            se.printStackTrace();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public static String convertDateTime(String date, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd, yyyy HH:mm:ss", Locale.KOREAN);
        String[] strMonth = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        String convertDate = "";

        Calendar setDate = Calendar.getInstance();
        if ("S".equals(type.toUpperCase())) {
            setDate.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
                    Integer.parseInt(date.substring(6, 8)), Integer.parseInt(date.substring(8, 10)),
                    Integer.parseInt(date.substring(10, 12)), Integer.parseInt(date.substring(12, 14)));
        } else {
            setDate.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
                    Integer.parseInt(date.substring(6, 8)), Integer.parseInt(date.substring(8, 10)),
                    Integer.parseInt(date.substring(10, 12)), Integer.parseInt(date.substring(12, 14)));
        }

        convertDate = strMonth[setDate.get(Calendar.MONTH)] + " " + sdf.format(setDate.getTime());

        return convertDate;
    }

    /**
     * 널이면 true 리턴
     * @param s
     * @return
     */
    public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>)s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>)s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[])s).length == 0);
        }
        return false;
    }
}
