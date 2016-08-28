package nfyg;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class CrawUtil5 {

    class TaoBaoResult{
        private int code;
        private ResultData data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public ResultData getData() {
            return data;
        }

        public void setData(ResultData data) {
            this.data = data;
        }
    }

    class ResultData{
        private String city;

        private String region;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
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
                    result = HttpClientUtil.getJson("http://ip.taobao.com/service/getIpInfo.php?ip="+line);
                    System.out.println(k++);
                    if(!StringUtils.isEmpty(result)){
                        TaoBaoResult taoResult = gson.fromJson(result, TaoBaoResult.class);
                        if(taoResult.getCode() == 0 && taoResult.getData() != null){
                            if(!StringUtils.isEmpty(taoResult.getData().getCity())){
                                System.out.println(line+"|"+taoResult.getData().getRegion()+"|"+taoResult.getData().getCity());
                                succssWriter.write(line+"|"+taoResult.getData().getRegion()+"|"+taoResult.getData().getCity()+"\n");
                                line = reader.readLine();
                                continue;
                            }
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
