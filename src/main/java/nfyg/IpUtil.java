package nfyg;

import java.io.*;

/**
 * Created by coriger on 2016/8/27.
 */
public class IpUtil {

    public static void main(String[] args) throws Exception {
        int index = 0;
        int no = 1;
        int js = 0;
        File file = new File("F:\\WorkSpace\\IpUtil\\src\\main\\java\\nfyg\\ip.txt");
        String fileUrl = "F:\\WorkSpace\\IpUtil\\src\\main\\java\\nfyg\\ip_"+no+".txt";
        File file2 = new File(fileUrl);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2)));
        String line = null;
        line = reader.readLine();
        while(null != line){
            System.out.println("start ["+line+"] ...");
            String[] str = line.split("/");
            String startIp = str[0];
            String endIp = IPPoolUtil.getEndIP(str[0],Integer.parseInt(str[1])).getEndIP();
            // 遍历
            int beginInt = IPv4Util.ipToInt(startIp);
            int endInt = IPv4Util.ipToInt(endIp);
            while (beginInt <= endInt)
            {
//                System.out.println(IPv4Util.intToIp(beginInt));
                String ip = IPv4Util.intToIp(beginInt);
                if(ip.endsWith(".0")){
                    // 只存储前三位
                    writer.write(IPv4Util.intToIp(beginInt)+"\n");
                }
                beginInt += 1;
                js++;
                // 如果数目到达1000w 保存一次
                if(index++ >=30000000){
                    index = 0;
                    no++;
                    writer.flush();
                    writer.close();
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("F:\\WorkSpace\\IpUtil\\src\\main\\java\\nfyg\\ip_"+no+".txt"))));
                }
            }
            line = reader.readLine();
        }
        System.out.println("sum is ["+ js +"]");
        writer.close();
        reader.close();
    }

    private static int IpToInt(String ip) {
        String[] arr = ip.split("\\.");
        int p0 = Integer.parseInt(arr[0]);
        int p1 = Integer.parseInt(arr[1]);
        int p2 = Integer.parseInt(arr[2]);
        int p3 = Integer.parseInt(arr[3]);

        return (p0 << 32) | (p1 << 16) | (p2 << 8) | p3;
    }

    private static String IntToIp(int s)
    {
        int p0 = s >> 32 & 0xFF;
        int p1 = s >> 16 & 0xFF;
        int p2 = s >> 8 & 0xFF;
        int p3 = s & 0xFF;
        return p0+"."+p1+"."+p2+"."+p3;
    }


}
