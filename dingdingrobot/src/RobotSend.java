import com.taobao.api.ApiException;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class RobotSend {
        public static void main(String[] args) throws ApiException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, FileNotFoundException {
            //获取getUrl方法得到的url
            String url = getUrl();
            DingTalkClient client = new DefaultDingTalkClient(url);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            //获取当前系统时间并格式化
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(new Date());
            text.setContent(getMessage()+"告警时间:"+date);
            request.setText(text);
            OapiRobotSendResponse response = client.execute(request);

        }
        public static String getUrl() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
            Long timestamp = System.currentTimeMillis();
            String secret = "SECc427dfc049238dd6324a3b728a3ee58fdd08459561b164b29345a10e8f82992c";
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
            //定义token
            String access_token = "ee468a53ace3e14cd102d636da45281896ed234096b525acf604cd779c6f65b1";
            //拼接字符串
            String url = "https://oapi.dingtalk.com/robot/send"+
                    "?access_token=%s"+
                    "&timestamp=%s"+
                    "&sign=%s";
            String serverUrl= String.format(
                    url,
                    access_token,
                    timestamp,
                    sign
            );
            return serverUrl;
        }
        public static String getMessage() throws FileNotFoundException {

       FileReader fileReader = null;
       fileReader = new FileReader("/root/notepad/dingding_robot/log.txt");
       Scanner sc = new Scanner(fileReader);
       String line = null;
       while((sc.hasNextLine()) && (line = sc.nextLine()) != null){
           if(!sc.hasNextLine()){
             return line;
           }

       }

       return line;
   }








    }


