package com.colini.study.core.test;


import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ref {@link = https://jojoldu.tistory.com/123}
 *
 * study-core 프로젝트에는 JpaTest만 수행한다.
 * 해당 프로젝트는 Spring Context를 불러오는 프로젝트가 없기때문에 임시 포인트를 지정하기 위해, 이클래스를 생성함
 */
@SpringBootApplication
public class CoreApplicationTests {
    public void contextLoads() {}
}
