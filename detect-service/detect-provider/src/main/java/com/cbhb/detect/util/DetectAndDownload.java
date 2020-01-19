package com.cbhb.detect.util;

import com.cbhb.detect.dto.Detect90Request;
import com.cbhb.detect.dto.DetectCardnumDateRequest;
import com.cbhb.detect.dto.DetectResultDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DetectAndDownload {

    public static String FIRST_PAGE = "http://www.shenzhentong.com/service/invoice_101007009.html";

    public static String YZM_PAGE = "http://www.shenzhentong.com/ajax/WaterMark.ashx";

    public static String LOGIN_PAGE = "http://www.shenzhentong.com/Ajax/ElectronicInvoiceAjax.aspx";

    public static String DETECT_PAGE = "http://www.shenzhentong.com/service/fplist_101007009_";

    public static String DETAIL_PAGE = "http://www.shenzhentong.com/Ajax/ElectronicInvoiceAjax.aspx";

    public static String DOWNLOAD_PAGE = "http://www.shenzhentong.com/service/fpdetail.aspx?nodecode=101007009&pid=";

    public static String FPPATH= System.getProperty("os.name").toLowerCase().contains("windows")?"d:/fp/":"/tmp/fp/";

    public static String nowMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

    Log log = LogFactory.getLog(DetectAndDownload.class);

    static {
        File file = new File(FPPATH+nowMonth);
        if (!file.exists()) {
            file.mkdir();
        }
    }


    CloseableHttpClient httpClient = null;

    CookieStore cookieStore = null;

//    private String imgPath;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

//    public static void main(String[] args) throws URISyntaxException {
//        String cardnum = "689549173";
//        String detectDate = "20191017";
//        DetectAndDetail dad = new DetectAndDetail("e:/");
//        dad.getCookie();
//
//        dad.getYZM();
//        for (int i = 0; i < 500; i++) {
//            if (dad.buildSession("" + i, cardnum)) {
//                System.out.println("Check Ok !!!");
//                break;
//            }
//            if (i >= 300) throw new RuntimeException("验证码未通过......");
//        }
//
//        DetectResultDto dto = dad.find(cardnum, detectDate);
//
//        System.out.println("DTO:\n" + dto);
//        String pdfPath = "d:/" + cardnum + "-" + detectDate + "-开" + dto.getAmt() + "元.pdf";
//        dad.download(dad.getDetail(), pdfPath);
//
//        //开过的含有:发票信息
//        //不能开的含有:display:none
//
//    }



    private void getCookie() {

        try {
            URIBuilder uriBuilder = new URIBuilder(FIRST_PAGE);
            cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            CloseableHttpResponse response = httpClient.execute(httpGet);
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void getYZM() {
        try(FileOutputStream fos = new FileOutputStream(new File(FPPATH + "/javaimg.gif"))) {
            URIBuilder uriBuilder = new URIBuilder(YZM_PAGE);
            HttpPost postYZM = new HttpPost(uriBuilder.build());
            CloseableHttpResponse yzmResponse = httpClient.execute(postYZM);
            HttpEntity entity = yzmResponse.getEntity();
            entity.writeTo(fos);
            yzmResponse.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean buildSession(String yzm, String cardnum) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(LOGIN_PAGE);
            HttpPost postLogin = new HttpPost(uriBuilder.build());
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair tpNP = new BasicNameValuePair("tp", "1");
            BasicNameValuePair yzmNP = new BasicNameValuePair("yzm", yzm);
            BasicNameValuePair cardnumNP = new BasicNameValuePair("cardnum", cardnum);

            list.add(tpNP);
            list.add(yzmNP);
            list.add(cardnumNP);
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            postLogin.setEntity(entityParam);
            CloseableHttpResponse loginResponse = httpClient.execute(postLogin);
            HttpEntity entity = loginResponse.getEntity();
            String response_content = IOUtils.toString(entity.getContent());
            if (response_content != null && response_content.contains("100")) {
                return true;
            }
            loginResponse.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private DetectResultDto find(String cardnum, String detectDate) {
        DetectResultDto detectResultDto = new DetectResultDto();
        String message = null;
        LocalDate localDate = LocalDate.parse(detectDate, formatter);
        try {
            URIBuilder uriBuilder = new URIBuilder(DETECT_PAGE + cardnum + "_" + detectDate + ".html");
            HttpGet detectGet = new HttpGet(uriBuilder.build());
            CloseableHttpResponse detectResponse = httpClient.execute(detectGet);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            detectResponse.getEntity().writeTo(baos);
            String content = baos.toString("UTF-8");
//            global_content = content;
            detectResultDto.setCardnum(cardnum);
            detectResultDto.setDetectContent(content);
            detectResultDto.setDate(localDate.format(formatter));
            detectResultDto.setDetectTime(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            if (content == null) {
                detectResultDto.setDetectType(DetectResultDto.DetectType.ERROR);
                detectResultDto.setMessage("异常");
                detectResultDto.setAmt(0);
                detectResponse.close();
                return detectResultDto;
            }
            if (content.contains("display:none")) {
                detectResultDto.setDetectType(DetectResultDto.DetectType.NORECORD);
                detectResultDto.setMessage("----无充值记录");
                detectResultDto.setAmt(0);
                detectResponse.close();
                return detectResultDto;
            } else if (content.contains("发票信息")) {

                detectResultDto.setDetectType(DetectResultDto.DetectType.ALREADY);
                detectResultDto.setMessage("----已经开过了------------");
                detectResultDto.setAmt(0);
                detectResponse.close();
                return detectResultDto;
            } else {
                StringBuilder sb = new StringBuilder(cardnum + ":" + detectDate + "  ******可以开******");
                Pattern pattern = Pattern.compile("((\\d)*)元");
                int amt = 0;
                for (String s : content.split("\n")) {
                    if (s != null && s.contains("odd_body") && s.contains("ipcbcxjg")) {
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            sb.append(matcher.group()).append("###");
                            amt += Integer.parseInt(matcher.group(1));
                        }
                    }
                }
                detectResultDto.setDetectType(DetectResultDto.DetectType.CAN);
                detectResultDto.setMessage(sb.toString());
                detectResultDto.setAmt(amt);

                detectResponse.close();
                return detectResultDto;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detectResultDto;
    }


    //获取下载特征码
    private DetectResultDto getDetail(DetectResultDto detectResultDto,String companyName,String companyID,String phone) {
        String content = detectResultDto.getDetectContent();
        URIBuilder uriBuilder = null;

        Pattern lshPattern = Pattern.compile("lsh=\"((\\d)*)");
        Pattern zdhPattern = Pattern.compile("zdh=\"((\\d)*)");
        Pattern khPattern = Pattern.compile("kh=\"((\\d)*)");
        Pattern rqPattern = Pattern.compile("rq=\"((\\d)*)");
        Pattern sjPattern = Pattern.compile("sj=\"((\\d)*)");

        String lsh = "";
        String zdh = "";
        String kh = "";
        String rq = "";
        String sj = "";


        StringBuilder sb = new StringBuilder();

        for (String s : content.split("\n")) {
            if (s != null && s.contains("odd_body") && s.contains("ipcbcxjg")) {
                Matcher matcher = lshPattern.matcher(s);
                if (matcher.find()) {
                    lsh = matcher.group(1);
                }
                matcher = zdhPattern.matcher(s);
                if (matcher.find()) {
                    zdh = matcher.group(1);
                }
                matcher = khPattern.matcher(s);
                if (matcher.find()) {
                    kh = matcher.group(1);
                }
                matcher = rqPattern.matcher(s);
                if (matcher.find()) {
                    rq = matcher.group(1);
                }
                matcher = sjPattern.matcher(s);
                if (matcher.find()) {
                    sj = matcher.group(1);
                }
            }
        }
        String dataStr = "";
        try {
            uriBuilder = new URIBuilder(DETAIL_PAGE);
            HttpPost detectPost = new HttpPost(uriBuilder.build());

            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair tpNP = new BasicNameValuePair("tp", "3");
            BasicNameValuePair jlshNP = new BasicNameValuePair("jlsh", lsh);
            BasicNameValuePair jzdhNP = new BasicNameValuePair("jzdh", zdh);
            BasicNameValuePair jkhNP = new BasicNameValuePair("jkh", kh);
            BasicNameValuePair jrqNP = new BasicNameValuePair("jrq", rq);
            BasicNameValuePair jsjNP = new BasicNameValuePair("jsj", sj);
            //发票抬头名称
            BasicNameValuePair jfirmfpmcNP = new BasicNameValuePair("jfirmfpmc", companyName==null?"渤海银行股份有限公司":companyName);
            //纳税人识别号
            BasicNameValuePair jfirmsbhNP = new BasicNameValuePair("jfirmsbh", companyID==null?"911200007109339563":companyID);
            //地址
            BasicNameValuePair jfirmaddreNP = new BasicNameValuePair("jfirmaddre", "");
            //电话号码：
            BasicNameValuePair jfirmtelNP = new BasicNameValuePair("jfirmtel", "");
            //开户银行：
            BasicNameValuePair jfirmyhNP = new BasicNameValuePair("jfirmyh", "");
            //银行账号：
            BasicNameValuePair jfirmyhzhNP = new BasicNameValuePair("jfirmyhzh", "");
            //手机号码：
            BasicNameValuePair jfirmphoneNP = new BasicNameValuePair("jfirmphone", phone==null?"13526627725":phone);

            list.add(tpNP);
            list.add(jlshNP);
            list.add(jzdhNP);
            list.add(jkhNP);
            list.add(jrqNP);
            list.add(jsjNP);
            list.add(jfirmfpmcNP);
            list.add(jfirmsbhNP);
            list.add(jfirmaddreNP);
            list.add(jfirmtelNP);
            list.add(jfirmyhNP);
            list.add(jfirmyhzhNP);
            list.add(jfirmphoneNP);


            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");

            detectPost.setEntity(entityParam);

            CloseableHttpResponse detectResponse = httpClient.execute(detectPost);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            detectResponse.getEntity().writeTo(baos);
            String res_content = baos.toString("UTF-8");

            System.out.println("开发票后的报文: \n" + res_content);

            Pattern strsPattern = Pattern.compile("strs\":\"((\\d)*)");

            Matcher matcher = strsPattern.matcher(res_content);

            if (matcher.find()) {
                dataStr = matcher.group(1);
                detectResultDto.setPid(dataStr);
            } else {
                //未匹配成功

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return detectResultDto;
    }

    private DetectResultDto download(DetectResultDto detectResultDto, String downpath) {


        String dataStr = detectResultDto.getPid();

        try {
            Thread.sleep(Long.parseLong("3000"));
            if (dataStr == null || "".equals(dataStr)) {
                throw new RuntimeException("特征码不能为空");
            }
            CookieStore downCookie = new BasicCookieStore();
            CloseableHttpClient downClient = HttpClients.custom().setDefaultCookieStore(downCookie).build();
//        URIBuilder uriBuilder = new URIBuilder("http://dzfp.szhtxx.cn:10000/downInvoice/download/914403007703110594/2020/01/14/TG401656028850163712.pdf");
            URIBuilder uriBuilder = new URIBuilder("http://www.shenzhentong.com//service/fpdetail.aspx?nodecode=101007009&pid=" + dataStr);
            HttpGet downloadGet = new HttpGet(uriBuilder.build());
            CloseableHttpResponse downloadResponse = downClient.execute(downloadGet);

            URIBuilder downURI = new URIBuilder("http://www.shenzhentong.com/ajax/electronicinvoiceajax.aspx");
            HttpPost postInvocation = new HttpPost(downURI.build());
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair tpNP = new BasicNameValuePair("tp", "4");
            BasicNameValuePair pidNP = new BasicNameValuePair("pid", dataStr);
            list.add(tpNP);
            list.add(pidNP);
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");

            postInvocation.setEntity(entityParam);
            CloseableHttpResponse downloadExec = downClient.execute(postInvocation);
            ByteArrayOutputStream downBaos = new ByteArrayOutputStream();
            downloadExec.getEntity().writeTo(downBaos);
            String downloadResponse1 = downBaos.toString();
            System.out.println("downloadResponse1:" + downloadResponse1);
            if(downloadResponse1.split("strs\":\"").length<2){
                return detectResultDto;
            }
            String urlArr = downloadResponse1.split("strs\":\"")[1];
            String realURL = "";
            if (urlArr != null && urlArr.length() > 1) {
                System.out.println(urlArr.substring(0, urlArr.length() - 2));
                realURL = urlArr.substring(0, urlArr.length() - 2);
                detectResultDto.setUrl(realURL);
            }


            URIBuilder pdfURI = new URIBuilder(realURL);
            HttpGet pdfGet = new HttpGet(pdfURI.build());
            CloseableHttpResponse pdfResponse = downClient.execute(pdfGet);
            FileOutputStream pdfFOS = new FileOutputStream(downpath);
            pdfResponse.getEntity().writeTo(pdfFOS);

            pdfFOS.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return detectResultDto;
    }

    //根据卡号,日期进行探测
    public DetectResultDto detectByCardnumDate(String cardnum,String date){
        getCookie();
        getYZM();
        for (int i = 0; i < 301; i++) {
            if(buildSession(""+i,cardnum)){
                log.info("============Check OK  ============");
                break;
            }
            if(i>=300) throw new RuntimeException("验证码未通过......");
        }

        return  find(cardnum, date);
    }


    public List<DetectResultDto> detectCardnum90(String cardnum){
        List<DetectResultDto> list = new ArrayList<>();
        getCookie();
        getYZM();
        for (int i  = 0; i < 500; i++) {
            if(buildSession(""+i,cardnum)){
                break;
            }
            if(i>=300) throw new RuntimeException("验证码未通过......");
        }

        LocalDate localDate = LocalDate.now();

        for (int i = 0; i <=90; i++) {
            localDate = localDate.minusDays(1);
            String detectDate = localDate.format(formatter);
            DetectResultDto detectResultDto = find(cardnum, detectDate);
            if(detectResultDto.getDetectType()== DetectResultDto.DetectType.CAN|| detectResultDto.getDetectType()== DetectResultDto.DetectType.ALREADY){
                list.add(detectResultDto);
            }

        }

//        list.stream().filter(dr->dr.getDetectType()== DetectResultDto.DetectType.CAN || dr.getDetectType()== DetectResultDto.DetectType.ALREADY).collect(Collectors.toList());
                //.forEach(System.out::println);

        try (FileOutputStream fos = new FileOutputStream(FPPATH+"/detect-"+Thread.currentThread().getName()+".log",true);){

            fos.write(("\n======"+cardnum+"========START"+LocalDate.now().format(formatter)+"=========================\n").getBytes());
            list.stream().filter(dr->dr.getDetectType()== DetectResultDto.DetectType.CAN || dr.getDetectType()== DetectResultDto.DetectType.ALREADY).forEach(d -> {
                try {
                    fos.write((d.toString()+"\n").getBytes());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            fos.write(("\n======"+cardnum+"========END"+LocalDate.now().format(formatter)+"=========================\n").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    //根据卡号,日期,下载
    public DetectResultDto downloadCardnumDate(String cardnum,String date){
        DetectResultDto detectResultDto = detectByCardnumDate(cardnum, date);
        DetectResultDto downloadDto = null;
        if(detectResultDto!=null||detectResultDto.getDetectType()== DetectResultDto.DetectType.CAN){
            String pid = getDetail(detectResultDto, "深圳市深圳通有限公司", "914403007703110594", "13211436580").getPid();
            if (pid != null && !StringUtils.isBlank(pid)) {
                downloadDto = download(detectResultDto,FPPATH+"/"+nowMonth+"/"+cardnum+"-"+date+"-"+detectResultDto.getAmt()+".pdf");
            }
        }
        return downloadDto;
    }

    //根据卡号自动安全下载
    public DetectResultDto downloadAuto(String cardnum){
        List<DetectResultDto> detectResultDtos = detectCardnum90(cardnum);

        String safeDate = LocalDate.now().minusDays(89).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        DetectResultDto detectResultDto = detectResultDtos.stream().filter(dto -> safeDate.equals(dto.getDate())).findFirst().orElse(null);
        DetectResultDto downloadDto = null;


        int canCount  = detectResultDtos.stream().filter(drd->drd.getDetectType()== DetectResultDto.DetectType.CAN).collect(Collectors.toList()).size();
        int alreadyCount = detectResultDtos.stream().filter(drd->drd.getDetectType()== DetectResultDto.DetectType.ALREADY).collect(Collectors.toList()).size();
        log.info(String.format("\ncan:%d \n already: %d \n",canCount,alreadyCount));
        if(alreadyCount>0 || canCount <5){
            return null;
        }else{
            if(detectResultDto!=null||detectResultDto.getDetectType()== DetectResultDto.DetectType.CAN){
                String pid = getDetail(detectResultDto, "渤海银行股份有限公司", "911200007109339563", "13722657816").getPid();
                if (pid != null && !StringUtils.isBlank(pid)) {
                    downloadDto = download(detectResultDto,FPPATH+"/"+nowMonth+"/"+cardnum+"-"+safeDate+"-"+detectResultDto.getAmt()+".pdf");
                    log.info("===================downloadDto=====start==========================");
                    log.info(downloadDto);
                    log.info("===================downloadDto=====end==========================");
                }
            }
        }


        return null;
    }

    public static void main(String[] args) {
        DetectAndDownload dad = new DetectAndDownload();
        dad.downloadAuto("362494787");
//        dad.detectCardnum90("362491833");

//        DetectResultDto d1 = dad.downloadCardnumDate("362494170","20191027");
//        DetectResultDto d2 = dad.downloadCardnumDate("689549144","20191105");
//        System.out.println("d1:"+d1);
//        System.out.println("d2:"+d2);

//        DetectCardnumDateRequest request = new DetectCardnumDateRequest();
//        request.setCardnum("362491833");
//        request.setDate("20191225");
//        System.out.println(dad.detectByCardnumDate(request));
    }
}
