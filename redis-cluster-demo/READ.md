# Getting Started

### Redis Cluster Example With Redisson and Jedis
For further reference, please consider the following sections:

* Önce redis cluster'ı başlatırılır. Docker Compose ile redis cluster'ı oluşturmak için repository'deki ilgili örnekler incelenebilir
* Proje build edildikten sonra root dizinde 
```bash
docker build --no-cache -t myredisapp .
```  
komutu ile springboot projesinin docker imajı oluşturulur.
* Docker compose'un cluster için oluşturduğu network sorgulanır.
```bash
* docker network ls
```

Örneğin **redis-docker-compose_app_subnet** olsun
```bash
docker run -it --rm --net redis-docker-compose_app_subnet -p 8080:8080 myredisapp
``` 
komutu ile yeni bir container oluşturulup log'lardan cluster'a bağlanıp bağlanmadığı ve diğer komutların sağlıklı bir şekilde çalışıp çalışmadığı izlenebilir.



* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#data.nosql.redis)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)

