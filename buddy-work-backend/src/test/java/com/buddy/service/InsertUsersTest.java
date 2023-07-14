package com.buddy.service;

import com.buddy.model.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 导入用户测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InsertUsersTest {

    @Resource
    private UserService userService;

    private ExecutorService executorService = new
            ThreadPoolExecutor(40, 1000, 10000, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户
     */
    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        final int MAX_LENGTH = 6;
        List<User> userList = new ArrayList<>();
        StringBuilder idString;
        for (int i = 1; i <= INSERT_NUM; i++) {
            idString = new StringBuilder();
            User user = new User();
            for (int j = 0; j < MAX_LENGTH - Integer.toString(i).length(); j++) {
                idString.append("0");
            }
            idString.append(i);
            user.setUsername("user - " + idString);
            user.setUserAccount("user" + idString);
            user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
            user.setGender(0);
            user.setUserPassword("user" + idString);
            user.setPhone("18823" + idString);
            user.setEmail(idString + "@qq.com");
            user.setTags("[]");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode(idString.toString());
            userList.add(user);
        }
        // 20 秒 10 万条
        userService.saveBatch(userList, 10000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 生成编号
     */
    @Test
    public void testCode() {
        final int INSERT_NUM = 100000;
        final int MAX_LENGTH = 6;
        StringBuilder idString;
        for (int i = 1; i <= INSERT_NUM; i++) {
            idString = new StringBuilder();
            for (int j = 0; j < MAX_LENGTH - Integer.toString(i).length(); j++) {
                idString.append("0");
            }
            idString.append(i);
            System.out.println(idString.toString());
        }
    }

    public static void main(String[] args) {
        final int INSERT_NUM = 100000;
        final int MAX_LENGTH = 6;
        StringBuilder idString;
        for (int i = 1; i <= INSERT_NUM; i++) {
            idString = new StringBuilder();
            for (int j = 0; j < MAX_LENGTH - Integer.toString(i).length(); j++) {
                idString.append("0");
            }
            idString.append(i);
            System.out.println(idString.toString());
        }
    }


    /**
     * 并发批量插入用户
     */
    @Test
    public void doConcurrencyInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 分十组
        int batchSize = 5000;
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUsername("假鱼皮");
                user.setUserAccount("fakeyupi");
                user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setPhone("123");
                user.setEmail("123@qq.com");
                user.setTags("[]");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("11111111");
                userList.add(user);
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 20 秒 10 万条
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
