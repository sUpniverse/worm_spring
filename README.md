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

       - ```java
         implements EnvironmentAware, InitializingBean
         ```

       - ##### 한 어플리케이션에 Environment는 하나만 존재하기 때문에 Environment 객체가 아까 설정한 Environment를 가르키며 그 값을 불러온다. 

         - `private Environment env`를 선언

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

        -  메소드를 만들어 주고 위와같이 configurer를 통해 반환되어 field에 저장된 값을 set을 통하여 하나하나 가져온다.


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

- 구현 순서 - 1 (XML 기반의 AOP 구현) 

  1. 의존 설정(`pom.xml`)

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

  4. bean의 정보가 있는 `CTX.xml` 파일에 Aspect설정 및 Namespaces의 AOP 체크 후 bean 설정

     - ```xml
       <bean id="logAop" class="com.javalec.ex.LogAop"></bean>
       	<aop:config>
       		<aop:aspect id="logger" ref="logAop">
       			<aop:pointcut expression="within(com.javalec.ex.*)" id="publicM"/>
       			<aop:around pointcut-ref="publicM" method="loggerAop" />
       		</aop:aspect>
       	</aop:config>
       ```

       - 설정을 통해 proxy가 대행으로 일을 하게 만든다. 각각의 aop 속성 설정에 따라

- Advice 종류 (aop 속성)

  - ```xml
    <aop:before> : 메소드 실행 전에 advice실행
    <aop:after-returning> : 메소드 실행 후 advice 실행
    <aop:after-thorwing> : 메소드 실행 중 exception 발생 시 advice 실행
    <aop:after> : 메소드 실행 중 exception이 발생하여도 advice 실행
    <aop:around> : 메소드 실행 전/후 및 exception 발생시 advice 실행
    ```

- 구현순서 -2 (@을 이용한 AOP구현)

  1. `pom.xml` 의존 설정 동일 

  2. 공통 기능의 Advice 클래스 만들기 (클래스 @Aspect선언)

     ```java
     @Aspect
     public class LogAop {
         @Pointcut("within(com.javalec.ex.*)")
             private void pointcutMethod() {		
             }		

         @Around("pointcutMethod()")
             public Object loggerAop(ProceedingJoinPoint joinpoint) throws Throwable {
             }

         @Before("within(com.javalec.ex.*)")
             public void beforAdvice() {		
             }
     }
     ```

     - xml에서 했던 aop:config를 Advice 클래스 내에 Pointcut과 Around, Before를 선언해준다 
     - Around는 핵심메소드 전과 후에 Before는 핵심메소드 전에 실행된다.

  3. `CTX.xml`에 `<aop:aspectj-autoproxy />` 를 추가해 주고 나머지는 다 같다. (단, aop:config는 하지 않는다)

- Aspectj Ponitcut 표현식

  - Pointcut 지정 시 Aspectj 문법을 사용한다.
    - `*` : 모든
    - `.` : 현재
    - `..` : 0개 이상
    - Execution
    - within
    - bean


#### 12. Controller

- 클라이언트의 요청을 처리할 메소드 (보통 대게 처음 Pjt생성시 HomeController로 되어있음)

- Controller의 내부 코드

  ```java
  @Controller
  public class MyController {
  	
  	@RequestMapping("/view")
  	public String view() {		
  		return "view";
  	}
  ```

  - `@Controller` 를 해줘야 Dispacher가 controller 인식한다.
  - `@RequestMapping`을 통해 요청경로(path)를 설정하고 `return` 을 통해 뷰페이지 이름을 반환

- 뷰페이지 이름 생성 방법

  - 뷰페이지 이름 = prefix + 요청처리 메소드 (return ) 반환 값 +  suffix

- 뷰에 데이터 전달 1

  ```java
  @RequestMapping("/content/contentView")
  	public String contentView(Model model) {
  		
  		model.addAttribute("id","abcde" );
  		
  		return "content/contentView";
  	}
  ```

  - Model을 파라미터로 넘겨 받은 후 `model.addAttribute`로  데이터 값을 보내준다.
  - 후 view.jsp에서 `${id}`로 값을 넘겨 받는다.

- 뷰에 데이터 전달 2

  ```java
  @RequestMapping("/modelAndView/modelView")
  	public ModelAndView modelAndView() {
  		
  		ModelAndView mv = new ModelAndView();
  		mv.addObject("id", "abcded");
  		mv.setViewName("/modelAndView/modelView");		
  		
  		return mv;
  	}
  ```

  - `ModelAndView` 객체를 이용하여 Object 설정 및 View 설정까지 동시에 하고 mv를 return 한다.

- Class 에 `@RequestMapping`

  ```java
  @Controller
  @RequestMapping("/board")
  public class SampleRequestMapping {
  	
  	@RequestMapping("/view")
  	public String view() {
  				
  		return "/board/view"; 
  	}		
  }
  ```

  - controller의 /board + /view = /board/view 로 mapping 된다.
  - 원하는 목적의 controller 제작 후 mapping 하면 될듯? 게시판, 회원 등등?

#### 13.  데이터전송  

- `HttpServletRequest`을 이용한 데이터 전송

  - Model의 경우엔 데이터를 보낼떄, HttpServletRequest는 데이터를 받을때 사용.

    ```java
    @RequestMapping("/member/memberView")
    	public String viewMember(HttpServletRequest request, Model model) {
    		
    		String id = request.getParameter("id");
    		String pw = request.getParameter("pw");
    		
    		model.addAttribute("id",id);
    		model.addAttribute("pw", pw);
            
    		return "/member/memberView";		
    	}
    ```

- `@RequestParam`을 이용한 데이터 전송

  - url의 값이 없다면 무조건 400에러가 발생 

    ```java
    @RequestMapping("/member/confirm")
    	public String memberConfirm(@RequestParam("id") String id,@RequestParam("pw") String pw, Model model) {
    		
    		model.addAttribute("identify",id);
    		model.addAttribute("password", pw);
    		
    		return "/member/confirm";
    	}
    ```

- 데이터(커맨드) 객체

  - 파라미터의 값이 많아 코드의 양이 많기에 DTO를 이용한다.

    ```java
    @RequestMapping("/join/form")
    	public String  memberjoin(Member member) {		
    		
    		return "/join/form";
    	}
    ```

- `@Pathvariable` 

  - 경로에 변수를 넣어 파라미터로 사용

    ```java
    @RequestMapping("/member/{memberId}")
    	public String getMember(@PathVariable String memberId, Model model ) {
    		
    		model.addAttribute("memberId",memberId);		
    		return "/member/memberOK";
    	}
    ```

    - `member/memberOk.jsp`에  데이터가 나타난다 하지만 url은 `member/10` 이렇게 되어있다.

#### 14. @RequestMapping 파라미터

- `@RequestMapping` 에서 요청을 받을 때 Get과 Post방식을 구분 할 수 있다.

  ```java
  	@RequestMapping(value = "/", method = RequestMethod.GET or .Post)
  	혹은 @RequestMapping("/") 로도 가능
  	public String home(Locale locale, Model model) {
  		
  		model.addAttribute("serverTime", formattedDate );
  		
  		return "home";
  	}
  ```

- `@ModelAttribute` 를 이용해 객체의 이름을 변경할 수 있다.

  ```java
  @RequestMapping("/studentView")
  	public String studentView(@ModelAttribute("studentinfo") StudentInformation studentinformation) {
  				
  		return "studentView";
  	}
  ```

  - `.jsp` 에서 `${studentinfo}`로 받을 수 있어서 간편히 길이를 줄일 수 있다.

- 리다이렉트 `redirect`

- ```java
  return redirect:원하는경로(full name도 가능)
  ```


#### 15. Validation (검증)

- Server에서 데이터 값을 검증한다.

- Validator를 이용한 검증

  - Controller의 Validator 객체를 이용하여 검증을 한다.

    ```java
    @Controller
    public class StudentController {
    	
    	@RequestMapping("/student/create")
    	public String studentCreate(@ModelAttribute("student") Student student, BindingResult result) {
    		
            String page = "createDonePage";
    		
    		StudentValidator validator = new StudentValidator();
    		validator.validate(student, result);
    		if(result.hasErrors()) {
    			page = "createPage";
    		}		
    		return page;
    	}
    }
    ```

    - `BindingResult` : 검증의 결과를 담아주는 객체

  - Validator를 implement 받은 클래스를 만들어 validation 목록을 정의한다.

    ```java
    public class StudentValidator implements Validator {

    	@Override
    	public boolean supports(Class<?> arg0) {
    		return Student.class.isAssignableFrom(arg0);
    	}

    	@Override
    	public void validate(Object obj, Errors errors) {
    		System.out.println("validate()");
    		Student student  = (Student)obj;
    		
    		String studentName = student.getName();
    		if(studentName == null || studentName.trim().isEmpty()) {
    			System.out.println("studentName is null or empty");
    			errors.rejectValue("name", "trouble");
    		}
    	}
    }
    ```

    - 2개의 메소드를 받으며,  `supports`에선 검증할 객체의 타입정보를 명시, `validate`에선 검증할 것 정의
    - obj를 통해 무엇이 들어올지 모르니 최상단 객체인 Object 객체로 받는다. 
    - `Errors`객체는 발생한 에러가 어떠한 것인지 명시하기위해 이용

- `ValidationUtils`를 이용하여 `if()`의 값 검증 대신  사용할 수있다.

  ```java
  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "trouble");
  ```

- `@InitBinder`,`@Valid` 를 이용하여 stundentValidator 객체를 만들지 않고 사용하기

  ```java
  @RequestMapping("/student/create")
  	public String studentCreate(@ModelAttribute("student") @Valid Student student, BindingResult result) {
  		String page = "createDonePage";        
  		if(result.hasErrors()) {
  			page = "createPage";
  		}		
  		return page;
  	}
  	
  	@InitBinder
  	protected void initBinder(WebDataBinder binder) {
  		binder.setValidator(new StudentValidator());
  	}
  ```

  - `pom.xml` 에 의존성 표시가 필요

    ```xml
    <dependency>
    			<groupId>org.hibernate</groupId>
    			<artifactId>hibernate-validator</artifactId>
    			<version>4.2.0.Final</version>
    </dependency>
    ```


#### 20. JDBC를 이용해서 코드 간소화

- JdbcTemplate 빈을 만들어서 간편하게 사용한다.

  - 방법 

    - `pom.xml` dependency를 추가해준다.

      ```xml
        <dependency>
         <groupId>org.springframework</groupId>
         <artifactId>spring-jdbc</artifactId>
         <version>${org.springframework-version}</version>
        </dependency>
      ```

    - controller에 Jdbc template를 추가 

      ```java
      public JdbcTemplate template;				//전역변수
      	
      	public void setTemplate(JdbcTemplate template) {
      		this.template = template;
      	}
      ```

    - `appServlet/servlet-context.xml` 에 bean을 추가해준다

      ```xml
      <beans:bean name="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
      		<beans:property name="driverClassName" value="com.mysql.jdbc.Driver"></beans:property>
      		<beans:property name="url" value="jdbc:mysql://localhost:3306/springdb"></beans:property>
      		<beans:property name="username" value="root"></beans:property>
      		<beans:property name="password" value="22345335"></beans:property>
          </beans:bean>

      <!-- dataSource의 정보를 입력해 놓고 template 빈을 만들어 ref로 가져온다. -->

          <beans:bean name="template" class="org.springframework.jdbc.core.JdbcTemplate">
          		<beans:property name="dataSource" ref="dataSource"></beans:property>    
          </beans:bean>	
      ```

    - 해당 bean을 찾을 수 있도록 @Autowired를 추가하고, util class를 통해 저장해 둔다.

      ```Java
      /* Dao class 내부  */
      public JdbcTemplate template;

      @Autowired  //위와 비교하여 새로 추가된 부분 template를 자동으로 set하기 위함이다.
      public void setTemplate(JdbcTemplate template) {
          this.template = template;
          Constant.template = this.template;  // 새로 추가된 부분
      }
      ```
      ```java
      /* Constant 클래스를 만들러 선언 */
      public class Constant {
      	public static JdbcTemplate template;
      }
      ```

    -  mvc 게시판을 이용해서 만든 list, write, modify, delete 를 쉽게 바꾼다. (template를 이용한다.)

       ```java
       /* list */
       template.query(query,new BeanPropertyRowMapper<BDto>(BDto.class));

       /* view */
       template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));

       /* write */
       template.update(new PreparedStatementCreator());

       /* modify, delete */
       template.update(query,new PreparedStatementSetter());
       ```

       - Write, modify, delete는 Anonymous class를 만들어 class 내부를 완성한다.
       - 여기서의 query는 사용자가 직접 String query를 이용해 만든다.

