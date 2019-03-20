package com.example.demo.task.delaytask;

import lombok.Data;

/**
 * Created by houan on 18/4/25.
 */

@Data
public class DelayMessage {

    /**
     * 正执行消息的token
     */
    private String tmpKey;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 延迟时间 纳秒
     */
    private long delay;

    /**
     * 到期时间 纳秒
     */
    private long expire;

    /**
     * 创建时间 纳秒
     */
    private long registerTime;

    public DelayMessage(long delay, String tmpKey, String message){
        this.tmpKey = tmpKey;
        this.message = message;

        this.delay = delay;
        this.registerTime = System.nanoTime();
        this.expire = this.delay + this.registerTime;
    }

    @Override
    public String toString() {
        return "DelayMessage{" +
                "tmpKey='" + tmpKey + '\'' +
                ", message='" + message + '\'' +
                ", delay=" + delay +
                ", expire=" + expire +
                ", registerTime=" + registerTime +
                '}';
    }
}
