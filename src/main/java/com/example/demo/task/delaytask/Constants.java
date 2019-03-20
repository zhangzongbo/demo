package com.example.demo.task.delaytask;

/**
 * @author zhangzongbo
 * @date 19-3-18 下午3:58
 */
public final class Constants {

    public static final String DELAY_TASK_QUEUE = "parrot:service:report:delayTaskQueue";

    private static final String QUEUE_PREFIX = "concurrent_redis_delay_queue_";

    private static final String EXECUTION_KEYSET_PREFIX = "exe_key_set_";

    private static final String RANDOM_EXECUTION_PREFIX = "random_exe_";

    private static final String RECOVER_MSG_LOCK_PREFIX = "recover_msg_lock";

}
