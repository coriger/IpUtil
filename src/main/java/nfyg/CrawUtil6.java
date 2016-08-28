package nfyg;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class CrawUtil6 {

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
                    result = HttpClientUtil.getJson("https://db-ip.com/"+line);
                    System.out.println(k++);
                    if(!StringUtils.isEmpty(result)){
                        Document document = Jsoup.parse(result);
                        String pro = document.select(".table-striped tr").get(6).select("td").get(0).text();
                        String city = document.select(".table-striped tr").get(7).select("td").get(0).text();
                        System.out.println(line+"|"+pro+"|"+city);
                        succssWriter.write(line+"|"+pro+"|"+city+"\n");
                        line = reader.readLine();
                        continue;
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
