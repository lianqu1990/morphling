package com.lianqu1990.common.utils.code;

import com.lianqu1990.common.utils.date.DateFormatUtil;
import com.lianqu1990.common.utils.date.TimeMark;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 缺陷是，订单量没那么大，导致机器码|序列 后，一般都是4096
 * 通过将毫秒引入序列后修正
 * 后来加了format以后性能受损，比idcenter慢10倍，每秒可以生成50w，idcenter将近500w，不过这也是idcenter极限
 * 够用，暂不优化
 * @author hanchao
 * @date 2017/4/20 19:01
 */
@Slf4j
public class OrderNoCenter {

    public static final Logger logger = LoggerFactory.getLogger(OrderNoCenter.class);
    private static final String WORKERID_PATH = "/etc/workerId";

    private OrderNoCenter() {
    }

    private static class OrderNoCenterHolder{
        private static OrderNoCenter instance = new OrderNoCenter();
    }

    public static OrderNoCenter getInstance() {
        return OrderNoCenterHolder.instance;
    }

    /**
     * 节点 ID 默认取1
     */
    private long workerId = 1;
    /**
     * 序列id 默认取1
     */
    private long sequence = 1;

    /**
     * 机器标识位数
     */
    private final long workerIdBits = 10L;
    /**
     * 机器ID最大值
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits); //结果就是2的workerBits次方-1,能表示的最大数.全部1亦或10位0，就是0开头最后10位1
    /**
     * 毫秒内自增位
     */
    private final long sequenceBits = 12L;
    /**
     * 机器ID偏左移12位
     */
    private final long workerIdShift = sequenceBits;
    /**
     * 数据中心ID左移17位
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 时间毫秒左移22位
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits;

    private long lastTimestamp = -1L;

    public void initParam() {
        // 从默认位置读取workerId,最大1024
        try {
            File conf = new File(WORKERID_PATH);
            if(conf.exists()){
                String str = FileUtils.readFileToString(conf);
                workerId = Integer.parseInt(str);
            }else{
                logger.warn(" worker id not found,will use default value...");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        logger.info(" worker id is {}",workerId);
        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("workerId is illegal: "
                    + workerId);
        }
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public String create() {
        return nextNo();
    }

    /**
     * 获取id 线程安全
     *
     * @return
     */
    private synchronized String nextNo() {
        long timestamp = timeGen();
        // 时间错误
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.");
        }
        // 当前毫秒内，则+1
        if (lastTimestamp == timestamp) {
            // 当前毫秒内计数满了，则等待下一秒
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID,最大十位数

        long id = ((timestamp % 1000) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        String timestampStr = DateFormatUtil.NUMBER_FORMAT.format(timestamp);
        return timestampStr+String.format("%010d",id);
    }

    /**
     * 等待下一个毫秒的到来
     *
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 最大十位，最小7位,补0format
     */
    /*public void test(){
        String t = String.valueOf((1L << 22) | (1 << 12) | 0);
        String t1 = String.valueOf((999L << 22) | (1023 << 12) | 0);
        System.out.println(DateFormatUtils.NUMBER_FORMAT.format(System.currentTimeMillis())+"-"+t);
        System.out.println(DateFormatUtils.NUMBER_FORMAT.format(System.currentTimeMillis())+"-"+t1);
        long l1 = (1L << 22) | (1 << 12) | 0;
        long l2 = (999L << 22) | (1023 << 12) | 0;
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(String.format("%010d",l1));
        System.out.println(String.format("%010d",l2));
    }*/

    public static void main(String[] args){
        //性能测试
        TimeMark mark = TimeMark.newInstance();
        for (int i = 0; i < 1000000; i++) {
            OrderNoCenter.getInstance().create();
        }
        mark.simplePrint();

        mark.mark();

        for (int i = 0; i < 1000000; i++) {
            IdCenter.getInstance().getId();
        }
        mark.simplePrint();
    }
}
