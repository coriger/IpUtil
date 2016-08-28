package nfyg;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class CrawUtil {

    class SinaResult{
        private String country;
        private String province;
        private String city;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
//线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(15);
//线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(1000);
        poolTaskExecutor.initialize();

        int k = 0;
        for(int i = 1;i>=1;i--){
            poolTaskExecutor.submit(new Ip(i));
        }
    }

    static class Ip implements Runnable{

        private int i;

        public Ip(int i) {
            this.i = i;
        }

        public void run(){
            // 单个文件抓取  没有城市的 放在另一个文件中
            try {
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
                        result = HttpClientUtil.getJson("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="+line);
                        if(!StringUtils.isEmpty(result)){
                            SinaResult sinaResult = gson.fromJson(result,SinaResult.class);
                            if(!StringUtils.isEmpty(sinaResult.getCity()) && !StringUtils.isEmpty(sinaResult.getProvince())){
                                System.out.println(line+"|"+sinaResult.getProvince()+"|"+sinaResult.getCity());
                                succssWriter.write(line+"|"+sinaResult.getProvince()+"|"+sinaResult.getCity()+"\n");
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
                succssWriter.close();
                errorWriter.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
