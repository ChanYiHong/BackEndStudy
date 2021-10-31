package hcy.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void field(){
        log.info("main start");
        Runnable userA = () -> {
            service.logic("userA");
        };
        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제 발생X
        sleep(100); // 동시성 문제 발생. 첫번 째 안끝났는데 두번쨰가 들어감.
        threadB.start();
        
        sleep(3000); // 메인 쓰레드 종료 대기. 이걸 안하면 쓰레드B는 도는데 메인 쓰레드가 종료되어버림..
        log.info("main exit");


        // 쓰레드 각각 전용 저장소를 가짐.
        // 저장 name=userA -> nameStore=null
        // 저장 name=userB -> nameStore=null
        // 조회 nameStore=userA
        // 조회 nameStore=userB
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
