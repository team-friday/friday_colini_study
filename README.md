### requirement

@ back-end
* java 8
* spring-boot 2.1.3
* spring-boot-data:redis-reactive, lettuce
* JPA

@ front-end
* front-end client : sock.js
* browser : chrome 64, IE 10


### protocol
* http
* websocket


### library

```groovy
compile('org.springframework.boot:spring-boot-starter-web')
compile('org.springframework.boot:spring-boot-starter-webflux')
compile('org.springframework.boot:spring-boot-starter-websocket')
compile('org.springframework.boot:spring-boot-starter-data-redis-reactive')
compile('io.reactivex.rxjava2:rxjava:2.2.2')
compile('biz.paluch.redis:lettuce:4.4.4.Final')
```

### test library
```groovy
testCompile('org.springframework.boot:spring-boot-starter-test')
testCompile('org.asynchttpclient:async-http-client:2.8.1')
testCompile('org.mockito:mockito-core:2.11.0')
```



### 요구사항 

1. 스터디 채팅 룸 생성, 관리 
- chat room 관련 CRUD 대한 응답 요청은 http rest api 사용
- 사용자 개인이 채팅방 생성 기준
- back-end 구조를 따른다.
- 메시지 내용은 api로 제공하지 않는다.


2. 비동기 채팅 서버 구현
- webflux(based-netty-container), rxjava, reactive-redis, lettuce 
- websocket을 통한 메시지 데이터 처리 사항에 대한 모든 것은 비동기로 처리.


3. 기타
- redis는 event-loop 기반 모델의 싱글 쓰레드, redis pub/sub 활용
- web mvc, JPA (jpa 혹은 jdbc 자체가 blocking API 이므로, 스프링은 web mvc를 사용할 것을 권고)
( https://redis.io/topics/latency#single-threaded-nature-of-redis )



### DB 모델링  

![Alt text](./study-core/src/main/resources/model.png)

- study-channel (channelId, channelName, userName, createdAt)
- study-channel-member (id, userName, authority, registerAt, modifiedAt, channelId)



### message data format

- 메시징 처리 
- text frame -> 서버에서 json 변경
- redis pub / sub 을 통해 topic - users 매칭


key : v1/chat/user/[topic]
values :

ex)
```json
{
   "createdAt" : "20190502101125",
   "expired" : "20191102101125",
   "message" : "test1",
   "id" : "choi",
   "session" : "uuid-32",
   "room" : "chat/choi/java"  
}
```


