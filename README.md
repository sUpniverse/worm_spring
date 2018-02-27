## Spring 꿈틀거리기



#### 7-1. 생명주기

- 생성 -> 설정 -> 사용 -> 종료
  - 생성
    - new를 이용하여 객체 생성
  - 설정
    - `객체.load("classpath:.xml")` 를 이용하여 설정
  - 사용
    - `객체 이름 = 생성된 객체.getBean("",객채.class)` 를 이용하여 사용을 정의
  -  종료
    - `생성된 객체.close()`

#### 7-2. 빈 생명주기

- `implements InitializingBean, DisposableBean`를 통해 생명주기 설정
  - `public void afterPropertiesSet()`  -> 객체.refresh() 과정에서 호출되는 부분
  - `public void destroy() -> 객체.close()`의 과정에서 호출, `객체.destroy()`가능
- 메소드를 만든후 @PostConstruct, @PreDestroy 이용하여 설정
  - 임의의 init, destroy 메소를 만든후 @PostConstruct, @PreDestroy를 이용하여 선언

#### 7-3. 빈 범위 (scope)

- 컨테이너가 생성되고, 빈이 생성될 때, 생성된 빈은 scope를 가짐 (영향을 끼치는 범위)
  - <bean> 선언시 scope를 통해 설정가능
    - ```xml
      <bean id="", class="", scope="">
      ```

    - default일 경우엔 (scope를 지정하지 않을 경우는 singleton이 default)


#### 8-1. Environment 객체를 이용한 외부파일 가져온 후 설정

- Environment가 각 Property를 가지고 있고 접근한다.

- Context --> ctx.getEnvironmnet() --> Environment --> env.getProperty() --> PropertySource

- Project의 성격에 맞게 사용을 해도 안해도 된다.

- Step

  1. ```java
     ConfigurableApplicationContext ctx = new GenericXmlApplicationContext();
     ```

     - `GenericXmlApplicationConetext()`의 최상위 객체임..

  2. ```Java
     ConfigurableEnvironment env = ctx.getEnvironmnet();
     ```

     - ctx에 있는 env를 가져온다.

  3. ```java
     MutablePropertySource propertySource = env.getPropertySource();
     ```

     - 가져온 Environment안에 있는 PropertySource를 가져온다.
     - set을 하지 않았기에 빈 객체이다.

  4. ```Java
     propertySource.addLast(new ResoucePropertySouce("classpath:admin.pro"))
     ```

     - resouces에 porperties를 설정해준 후  가져왔던 propertysource에 addLast라는 메소드를 이용해서 등록해준다. (List나 queue처럼 add하는 개념)

  5. ```java
     GenericXmlApplicatonContext gCtx = (GenericXmlApplicatonContext)ctx;
     gCtx.load("applicationCTX.xml");
     gCtx.refresh();
     ```

     - bean을 생성한 CTX.xml파일을 가져온다. 그래서 Context를 얻고 load를 한다.
     - refresh()를 통하여 bean객체를 생성한다.

  6. ```java
     AdminConnection adminConnection = gCtx.getbean("adminConnection,AdminConnection.class");
     ```

     - 가져온 bean에 설정되어진 adminConnection객체를 가져온다.

     - AdminConnection.java 파일을 통해 설정해 준다.

       - EnvironmentAware, InitializingBean을 implements해준다.

       - ##### 한 어플리케이션에 Environment는 하나만 존재하기 때문에 Environment 객체가 아까 설정한 Environment를 가르키며 그 값을 불러온다. 

         - ##### `private Environment env`를 선언

       - `setEnvironment()`를 통해 Environment를 설정해준다.

         - 메소드 안에 setter를 이용해 만들어진 메소드 `setenv(env)`를 한다.

       - AfterPropertiesSet() 메소드가 자동으로 Override되며 메소드에 해당 하는 Adminid 와 Adminpassword를 설정 코드를 삽입해준다.

         - 똑같이 setter를 이용해 해당 field의 선언된 값을 `env.getProperty("")`를 이용하여 가져온후 set해준다.

           `setAdminId(env.getProperty("admin.id")) `

       - 호출 순서 setEnvironment() -> afterPropertieSet ()

####8-2. Xml or Java파일을 이용하여 외부파일 이용 설정 (Environment 사용X) 

- XML을 이용한 설정

  - applicationCTX.xml파일에 adminConnection bean을 설정해주는 것은 동일

  - 다만 bean안에 property를 직접 선언하여 가져온다.

    1. ```xml
       <context:property-placeholder location="classpath:~~">
       ```

       - 사용하여 아까와 마찬가지로 만들어논 properties에 접근


       - 다만 xml 설정이 필요하므로 아래 Namespaces 탭을 이용하여 context에 체크를 하여 설정을 불러온다.

    2. ```xml
       <property name=" ">
       	<value> ${admin.id} </value>
       </property>
       ```

  - 다른 adminConnection.java 파일은 Environment를 사용하지 않으므로 InitializingBean, DisposableBean만 implements하여 set과 destroy만 해주면 된다.

  - ctx.close() 


- Java파일을 이용한 설정

  - ```java
    AnnotationConfigApplication ctx = new AnnotationConfigApplication(ApplicationConfig.class)
    ```

  - ApplicationConfig.java파일을 생성

    1. ```java
       @Configuration
       Public class ApplicationConfig {}
       ```

       - class까지도 @를 써서 선언해준다.

    2. ```java
        @Value("${admin.id}") 
        private String adminid
        ```

        - 선언을 통해 각 값을 가져옴 xml의 `<property> <value>`설정과 같음


    3. ```java
       @Bean 
       public static PropertySourcePlaceholderConfigurer Properties() {
       PropertySourcePlaceholderConfiguerer configurer = new PropertySourcePlaceholderConfiguerer();
       	Resource[] locations = new Resource[1];
       	location[0] = new ClasspathResouce("admin.properties");
       	configurer.setLocation(locations);
       	return configurer;
       }
       ```

       - 조금 어렵지만, 코드를 해석해보면 전혀 어렵지 않음 각 property값을 location배열에 저장한 다음 configurer에 location을 넣어주고 반환하여 값을 가져오게 함
       - 잘보면 location변수로 선언한건 `configurer.setLocation`메소드 때문이다.

    4. ```java
       @Bean
       Public AdminConnection adminconfig() {
       	AdminConnection ad = new AdminConnection()
       	ad.setAdminId(adminId);
       }
       ```

       - 메소드를 만들어 주고 위와같이 configurer를 통해 반환되어 field에 저장된 값을 

         set을 통하여 하나하나 가져온다.

#### 8-3. 여러개의 빈을 생성하여 상황에 따라 가져오는 방법

- 입력되는 값에 따라 bean을 선택하여 가져 온다.

- xml

  - `ctx.getEnvironment().setActiveProfiles("")`를 통해 선택하여 값을 가져온다.
  - `ctx.load(".xml",".xml") `여러개의 xml파일을 load하도록 한다.
  - `ctx.refresh()`는 필수
  - 해당 xml의 `<schemaLocation profile="~~">`을 설정해야 한다.

- java

  - xml과 Main은 거의 동일하며 `ctx.getEnvironment`까지는 동일

  - load가 아닌 `ctx.register(~.class,~.class)`를 통하여 load한다.

  - `ctx.refresh()`는 여기도 필수

  - 그다음 각 해당 .java파일에 설정을 해주어야 함

    - ```java
      @Configuration
      @Profile("class명")
      ```

      - 위와 같이 설정을 해줌 xml은 location을 java는 profile 어노테이션을 바꾸어줌

    - ```java
      @Bean
      public DTO 메소드명 () { 
       DTO 가져오고 set해주면 된다.
      }
      ```

-  개발환경에 따른 설정파일을 설정해 주어서 각 개발단계에서 OS의 시스템 값을 읽은 후 OS에 따른 설정 값을 가져와서 구동하면 편리하게 사용할 수 있다.


#### 9. AOP (Aspect Oriented Programming)

- 공통기능에 상속을 활용하는 방법에는 한계가 있어서 AOP가 등장하게 됨

- 용어

  - Aspect : 공통 기능
  - Advice : Aspect의 기능 자체
  - Jointpoint : Advice를 적용해야 되는 부분 
  - Ponitcut : JointPoint의 부분으로 실제로 Advice가 적용된 부분
  - Weaving : Advice를 핵심 기능에 적용하는 행위

- 구현 방법 : proxy를 이용한다.

  - Clinet ——> Proxy ——> Target
  - 공통기능을 핵심기능에 바로 적용하는것이 아니라 Proxy에게 요청을 하여 Proxy가 공통기능을 수행하고 핵심기능을 수행한다. 만약 공-핵-공이라고 하면 proxy가 공-핵-공 순으로 실행

- 구현 순서 (XML 기반의 AOP 구현)

  1. 의존 설정(pom.xml)

     - ```xml
       <dependency>
       	<groupId>org.aspectj</groupId>
       	<artifactId>aspectjweaver</artifactId>
       	<version>1.7.4</version>		
       </dependency>
       ```

  2. 공통 기능의 클래스 제작 Advice 역할 클래스

     - ```java
       public Object loggerAop(ProceedingJoinPoint joinpoint) throws Throwable {
       	try {
       		Object obj = joinpoint.proceed();
       	}
       }
       ```

       - `jointpoint.proceed()` 를 통해 advice

  3. XML설정 파일에 Aspect 설정

  4. bean의 정보가 있는 `CTX.xml` 파일에도 Namespaces의 AOP 체크 후 bean 설정

     - ```xml
       <bean id="" class=""></bean>
       	<aop:config>
       		<aop:aspect id="" ref="">
       			<aop:pointcut expression="" id=""/>
       			<aop:around pointcut-ref="" method="" />
       		</aop:aspect>
       	</aop:config>
       ```

       - 설정을 통해 proxy가 대행으로 일을 하게 만든다. 각각의 aop설정에 따라

- Advice 종류

  - ```xml
    <aop:before> : 메소드 실행 전에 advice실행
    <aop:after-returning> : 메소드 실행 후 advice 실행
    <aop:after-thorwing> : 메소드 실행 중 exception 발생 시 advice 실행
    <aop:after> : 메소드 실행 중 exception이 발생하여도 advice 실행
    <aop:around> : 메소드 실행 전/후 및 exception 발생시 advice 실행
    ```