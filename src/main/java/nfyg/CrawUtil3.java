package nfyg;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class CrawUtil3 {

    class TaoBaoResult{
        private int errNum;
        private ResultData retData;

        public int getErrNum() {
            return errNum;
        }

        public void setErrNum(int errNum) {
            this.errNum = errNum;
        }

        public ResultData getRetData() {
            return retData;
        }

        public void setRetData(ResultData retData) {
            this.retData = retData;
        }
    }

    class ResultData{
        private String city;

        private String province;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }

    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        int k = 0;
        for(int i = 1;i>=1;i--){
            // 单个文件抓取  没有城市的 放在另一个文件中
            File file = new File("F:\\ip_"+i+".txt");
            File success = new File("F:\\"+file.getName()+"_success");
            File error = new File("F:\\"+file.getName()+"_error");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            BufferedWriter succssWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(success)));
            BufferedWriter errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(error)));
            String line = null;
            line = reader.readLine();
            while(null != line){
                // 调用新浪接口
                String result = null;
                try {
                    result = HttpClientUtil.getJson("http://whois.pconline.com.cn/?ip="+line);
                    System.out.println(k++);
                    if(!StringUtils.isEmpty(result)){
                        Document document = Jsoup.parse(result);
                        String resp = document.select("form p").get(1).text();
                        System.out.println(resp);
                        String[] respStr = resp.substring(3).split("\\s+");
                        String rcs = respStr[0];
                        if(rcs.contains("省")){
                            String[] sss = rcs.split("省");
                            System.out.println(line+"|"+sss[0]+"|"+sss[1]);
                            succssWriter.write(line+"|"+sss[0]+"|"+sss[1]+"\n");
                            line = reader.readLine();
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                errorWriter.write(line+"\n");
                line = reader.readLine();
            }
            System.out.println("read "+ file.getName() + " finish ...");
            succssWriter.flush();
            errorWriter.flush();
            succssWriter.close(); errorWriter.close(); reader.close();
        }
    }

}
