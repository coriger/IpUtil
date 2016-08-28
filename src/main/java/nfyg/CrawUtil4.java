package nfyg;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class CrawUtil4 {

    class TaoBaoResult{
        private int showapi_res_code;
        private ResultData showapi_res_body;

        public int getShowapi_res_code() {
            return showapi_res_code;
        }

        public void setShowapi_res_code(int showapi_res_code) {
            this.showapi_res_code = showapi_res_code;
        }

        public ResultData getShowapi_res_body() {
            return showapi_res_body;
        }

        public void setShowapi_res_body(ResultData showapi_res_body) {
            this.showapi_res_body = showapi_res_body;
        }
    }

    class ResultData{
        private String city;

        private String region;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        int k = 0;
        for(int i = 1;i>=1;i--){
            // 单个文件抓取  没有城市的 放在另一个文件中
            File file = new File("/data/tool/ip_"+i+".txt");
            File success = new File("/data/tool/"+file.getName()+"_success");
            File error = new File("/data/tool/"+file.getName()+"_error");
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
                        }else if(rcs.contains("北京")){
                            System.out.println(line+"|北京|北京");
                            succssWriter.write(line+"|北京|北京"+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("西藏") || rcs.contains("新疆") || rcs.contains("宁夏") || rcs.contains("广西")){
                            System.out.println(line+"|"+rcs.substring(0,2)+"|"+rcs.substring(2));
                            succssWriter.write(line+"|"+rcs.substring(0,2)+"|"+rcs.substring(2)+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("香港")){
                            System.out.println(line+"|香港|香港");
                            succssWriter.write(line+"|香港|香港"+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("内蒙古")){
                            System.out.println(line+"|"+rcs.substring(0,3)+"|"+rcs.substring(3));
                            succssWriter.write(line+"|"+rcs.substring(0,3)+"|"+rcs.substring(3)+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("上海")){
                            System.out.println(line+"|上海|上海");
                            succssWriter.write(line+"|上海|上海"+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("天津")){
                            System.out.println(line+"|天津|天津");
                            succssWriter.write(line+"|天津|天津"+"\n");
                            line = reader.readLine();
                            continue;
                        }else if(rcs.contains("重庆")){
                            System.out.println(line+"|重庆|重庆");
                            succssWriter.write(line+"|重庆|重庆"+"\n");
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
