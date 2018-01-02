package com.lianqu1990.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hanchao
 * @date 2017/9/6 18:24
 */
@Slf4j
public class PooledKryoFactory extends KryoFactory {

    private final Queue<Kryo> pool = new ConcurrentLinkedQueue();
    private int maxTotal = 5000;
    private int maxIdle = 1000;
    private static AtomicBoolean clear = new AtomicBoolean(false);

    @Override
    public void returnKryo(Kryo kryo) {
        if(kryo == null){
            return;
        }
        pool.offer(kryo);
        //简单的移除操作，待完善
        if (pool.size() >= maxTotal && !clear.get()) {
            log.info(">>> kryo pool is larger than maxTotal...");
            if(clear.compareAndSet(false,true)){
                log.info(">>> kryo pool is larger than maxTotal,start clean...");
                try {
                    Thread thread = new Thread(() -> {
                        try {
                            while (pool.size() > maxIdle) {
                                pool.poll();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            clear.set(false);
                        }
                    });
                    thread.setDaemon(true);
                    thread.start();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void close() {
        pool.clear();
    }

    public Kryo getKryo() {
        Kryo kryo = pool.poll();
        if (kryo == null) {
            kryo = createKryo();
        }
        return kryo;
    }


    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

}

