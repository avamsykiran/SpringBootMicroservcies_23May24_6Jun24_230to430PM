Objectives
---------------------------------------------------------------
    
    Spring Boot initialization, Dependency Injection
    REST Web Service architecture , HTTP Endpoint handling
    cross origin, Errors and Exception Handling,
    Building custom response using Response Entity
    Validating input request using javax.validation
    Logging, Lombak

    Eurekha Registry, 
    Zuul Server, Feign Client ,Ribbon

Dependency Injection

    interface EmployeeDAO {

    }

    @Repository
    class EmployeeDAOJdbcImpl implements EmployeeDAO {

    }

    @Repository
    class EmployeeDAOJpaImpl implements EmployeeDAO {

    }

    interface EmployeeService {

    }

    @Service
    class EmployeeServiceImpl implements EmployeeService{

        @Autowired
        @Qualifier("employeeDAOJpaImpl")
        private EmployeeDAO empDao;

    }

Container      to manage the life cycle and dependency injection of an object and such objects are called beans.

                BeanFactory
                ApplicationContext

Bean            is any object managed by the container.

Componenet      the class of which a bean is a type of is called a component.


Spring Boot

    1. RAD - due to auto-config
    2. Embeded Servers


    @SpringBootApplication  =   @Configuaration
                                @AutoConfiguration
                                @ComponentScan(basePackage)
                                @PropertySource

    SpringApplication.run(SpringBootDemoApplication.class, args);

        1. ApplicationContext is created
        2. All Components are scanned and registred.
        3. All Runners (if any) are executed
        4. Embeded Server (if any) is started.
        5. The application keeps on listening and responsding to the reqeusts as long as the server is running.
        6. The ApplicationContext is terminated after server shuts down
        7. Application terminates.

    Runners are class inhereted from CommandLineRunner interface.

Rest API Standards

    REpresentational State Transfer API

    Resource : Employees
    EndPoint: /emps
                                                                                        ClientSide          ServerSide
    CRUD        Paths       Http-Methods    ReqBody     RespBody    SuccessStatus       FailureStatus       FailureStatus
    ----------------------------------------------------------------------------------------------------------------------
    Create      /emps       POST            empObj      empObj      201 - CREATED       400 - BAD REQUEST   500 -I S E  
    Retrive     /emps       GET             NA          emps[]      200 - OK                                500 -I S E
                /emps/{id}  GET             NA          empObj      200 - OK            404 - NOT FOUND     500 -I S E
    Update      /emps/{id}  PUT             empObj      empObj      202 - ACCEPTED      400 - BAD REQUEST   500 -I S E    
    Delete      /emps/{id}  DELETE          NA          NA          204 - NO CONTENT    404 - NOT FOUND     500 -I S E

Spring Web MVC

    Single Front Controller Archetecture

    Repositories    <--entities--> Services <--models--> Controllers <----data--- FrontController   <----Reqeust---- Browser / WebClient
                                                            ↓                       ↑       |                                       ↑
                                                            ------ModelAndView-------       |                                       |
                                                                                            | model                                 |
                                                                                            |                                       |
                                                                                            ↓                                       | 
                                                                                            VIEW --------- RESP (html content)--->  |
Spring Web Rest API

  Single Front Controller Archetecture

    Repositories    <--entities--> Services <--models--> RestControllers <----data--- FrontController   <----Reqeust---- REST-Client
                                                            ↓                                                              ↑
                                                            ------(Model + HttpStatus) RESP ------------------------------->
                                                                                          

    @RestController =   @Controller + @ResponseBody

    ResponseEntity = ResponseBody + HttpStatus

    Each action of RestController is to return a ResponseEntity

    @RequestMapping(url)
        @GetMapping
        @PutMapping
        @DeleteMapping
        @PostMapping

    The Model Class is marked with @XmlRoot

    @Consumes
    @Produces

    @RestControllerAdvice
    @ExceptionHandler

JPA - Annotations

        @Entity
        @Table

        @Inheretence(strategy = JOINED/SINGLE_TABLE/TABLE_PER_CLASS)

            Employee    eid,fullName,salary
                |
                --------------------------
                |                       |
                ContractEmployee        Manager
                    duration                allowence

            SINGLE_TABLE    allemps eid,fullName,sal,dur,allow

            JOINED          emps    eid,fullName,sal
                            cemps   eid,dur
                            mgrs    eid,allow

            TABLE_PER_CLASS emps1    eid,fullName,sal
                            cemps1   eid,fullName,sal,dur
                            mgrs1    eid,fullName,sal,allow

        @Embedable

        @Id
        @EmbededId
        @GeneratedValue
        @Column
        @Transiant
        @Enumeration
        @Embeded
        @OneToOne
        @OneToMany
        @ManyToOne
        @ManyToMany

Spring Data JPA

   this module provide auto-implemented repositories.

   CrudRepository
    |- JpaRepository
            save(entity)
            findAll()
            findById(id)
            deleteById(id)
            existsById(id)

    public interface EmployeeRepo extends JpaRepository<Employee,Long> {

        Optional<Employee> findByMailId(String mailId);
        boolean existsByMailId(String mailId);
        List<Employee> findAllByFirstName(String firstName);

        @Query("SELECT e FROM Employee e WHERE e.joinDate BETWEEN :start and :end")
        List<Employee> getAllInJoinDateRange(LocalDate start,LocalDate end);

        @Query("SELECT e.fullName,e.basicPay FROM Employee e")
        List<Object[]> getNamesAndBasicPays();
    }

Project Lombok

    @Getter
    @Setter
    @NoArgConstructor
    @Data           //generates all getters,setters and no-arg-constructor
    @Value
    @EqualsAndHashcode
    @AllArgConstructor
    @ToString
    @Slf4j
    @Log4j
    
javax.validators

    @Valid, BindingResult::hasErrors()

    @NotNull
    @NotBlank
    @Max
    @Min
    @Size
    @Present
    @Past
    @Future
    @Email
    @Pattern
    ...etc.,

@RestController
@RequestMapping("/emps")
public vlass EmployeeApi {

    @Autowired
    private EmployeeService empService;

    @PostMapping
    public ResponseEntity<Employee> add(@RequestBody @Valid Employee emp,BindingResult results) 
            throws InvalidEmployeeException{
        if(results.hasErrors()){
            throw new InvalidEmployeeException(results);
        }

        emp = empService.add(emp);
        return  new ResponseEntity<>(emp,HttpStatus.CREATED);
    }
}

@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<String> handleInvalidEmployeeException(InvalidEmployeeException exp){
        BindingResult results = exp.getResults();
        //conver the resutls into errMessage
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exp){
        //log the exception
        return new ResponseEntity<>("Inconvinience Regretteed!",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
        

Micro Services
------------------------------------------------------------------------------

    Case Study BudgetTracking APP
        1. We need to have different consumer or account holders to register
        2. Each accountHolder mst be able to record his spending or earning transactions.
        3. Generate a statement periodically displaying the total spending , the total earning and the balance.


    Monolythical App 

        One build per application that contains all the modules of the application.
    
        1. Scalability
        2. Avialability
        3. Interoperability

            BudgetTracking Application
                1. Profiles Module
                2. Transactions Module
                3. Statement Module

    Microservices

        A microservice is an isolated independently deployable module of a large
        application eco system.

            we will have a spearate deployment for SalesModule
            we will have a spearate deployment for HRModule
            we will have a spearate deployment for DeliveriesModules ...etc.,

        Because each module is a spepart isolated deployment, we can scale them independnetly.
        Each modules can be shut down, maintained and redeployed without stopping other services.
        Each module (microservice) can be developed using any technology we want.

        Chanllenges in Developing and Maintaining Microservices

            - Decomposiiton
            - Inter-Service Communication
            - Single Point Of Contact
            - Monitoring and Maintaining

        Microservice Design Patterns

            Sub-Domain Pattern guides through bounded-context.

                We will decompose the budgetTrackinApp into 3 microservices
                    (a) Profiles-Service
                            AccountHolder
                                accountHolderId
                                fullName
                                mobileNumber
                                mailId
                                userId
                                password

                    (b) Transactions-Service
                            AccountHolder
                                accoountHolderId
                                txns: Set<Txn>

                            Txn
                                dateOfTxn
                                txnId
                                txnAmount
                                txnType
                                owner : AccountHolder

                    (c) Statement-Service                
                            AccountHolder
                                accountHolderId
                                fullName
                                mobileNumber
                                mailId

                            Txn
                                dateOfTxn
                                txnId
                                txnAmount
                                txnType

                            Statement
                                owner : AccountHolder
                                txns: Set<txns>
                                startDate: Date
                                endDate: Date
                                totalSpending
                                totalEarning
                                balance

            Shared Database Pattern

                Having a single DB for all microservices
                in brown field apps

            Database Per Service Pattern

                Each microservice has its own database
                in all green field apps

            Discovery Service Pattern

                discovery-service
                    |
                    |- all microservices will register their address with discovery-service
                    |- the address are retrived from here by the needy microservices

            Data Aggregation Pattern

                Aggregation is about desiging a microservice that can collect info
                from other microservices analyze and aggreagate the data and pass the 
                aggregated data to the client, saving the client from making multiple requests
                for different parts of the data.

                the 'statement-microservice' is an example for this pattern.

            Client Side Component Pattern

                Each component of the UI/UX application can place
                their individual reqeusts to different microservices parellelly
                and should be receiving the resposnes as well parllelly.     

Decomposition by sub-domain

    budgettracking 
        profiles service
            AccountHolder Entity
                Long ahId
                String fullName
                String mobile
                String mailId

        txns service
            AccountHolder Entity
                Long ahId
                Double currentBalance
                Set<Txn> txns
            Txn           Entity
                Long txnId
                String header
                Double amount
                TxnType type
                LocalDate txnDate
                AccountHolder holder

        statement service
            AccountHolder Model
                Long ahId
                String fullName
                String mobile
                String mailId
                Double currentBalance

            Txn           Model
                Long txnId
                String header
                Double amount
                TxnType type
                LocalDate txnDate

            Statement     Model
                LocalDate start
                LocalDate end
                AccountHolder profile
                Set<Txn> txns
                totalCredit
                totalDebit
                statementBalance

Aggregator Pattern

    req for statement ------------> statement-service ---------------> profile service
                                                    <---account holder data---
                                                    --------------------> txns service
                                                    <----list of txns-------
                                     does the composition and computation
            <---statement obj-------  into statement obj

Discovery Service Design Pattern

                discovery-service
                (spring cloud netflix eureka discovery service)
                        ↑|
                    registration of urls 
                    and retrival of urls
                        |↓
            -------------------------------------
            |               |                   |
    profile-service     txns-service     statement-service

Api Gateway Pattern Design Pattern

    Andriod App/Angular App/ReactJS App
        ↑↓
     api-gateway
     (spring cloud api gateway)
        |
        |
        | <---->   discovery-service
            ↑    ( netflix eureka discovery service)
            |            ↑|
            |        registration of urls 
            |       and retrival of urls
            ↓            |↓
            -------------------------------------
            |               |                   |                
    profile-service     txns-service     statement-service
       


Implementing Budget-tracker
                                        
    Step#1  implementing decomposed services and do inter-service communication and aggregator
        in.bta:bta-profiles
            dependencies
                org.springframework.boot:spring-boot-starter-web
                org.springframework.boot:spring-boot-devtools
                org.springframework.cloud:spring-cloud-openfeign
                mysq1:mysql-connector-java
                org.springframework.boot:spring-boot-starter-data-jpa
            configuaration
                spring.application.name=profiles
                server.port=9100

                spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
                spring.datasource.username=root
                spring.datasource.password=root
                spring.datasource.url=jdbc:mysql://localhost:3306/bapsDB?createDatabaseIfNotExist=true
                spring.jpa.hibernate.ddl-auto=update

        in.bta:bta-txns
            dependencies
                org.springframework.boot:spring-boot-starter-web
                org.springframework.boot:spring-boot-devtools
                org.springframework.cloud:spring-cloud-openfeign
                mysq1:mysql-connector-java
                org.springframework.boot:spring-boot-starter-data-jpa
            configuaration
                spring.application.name=txns
                server.port=9200

                spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
                spring.datasource.username=root
                spring.datasource.password=root
                spring.datasource.url=jdbc:mysql://localhost:3306/batxnsDB?createDatabaseIfNotExist=true
                spring.jpa.hibernate.ddl-auto=update

        in.bta:bta-statement
            dependencies
                org.springframework.boot:spring-boot-starter-web
                org.springframework.boot:spring-boot-devtools
                org.springframework.cloud:spring-cloud-openfeign
            configuaration
                spring.application.name=statement
                server.port=9300

    Step#2  implementing discovery service and client side load balancing
        in.bta:bta-discovery
            dependencies
                org.springframework.boot:spring-boot-devtools
                org.springframework.cloud:spring-cloud-starter-netflix-eureka-server
            configuaration
                @EnableEurekaServer    on Application class

                spring.application.name=discovery
                server.port=9000

                eureka.instance.hostname=localhost
                eureka.client.registerWithEureka=false
                eureka.client.fetchRegistry=false
                eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
                eureka.server.waitTimeInMsWhenSyncEmpty=0

        in.bta:bta-profiles
            dependencies
                ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
            configuaration
                ++@EnableDiscoveryClient  on Application class

                eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                eureka.client.registryFetchIntervalSeconds=5
                eureka.instance.leaseRenewalIntervalInSeconds=5
                eureka.instance.leaseExpirationDurationInSeconds=5

                spring.cloud.loadbalancer.ribbon.enabled=false

        in.bta:bta-txns
            dependencies
                ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
            configuaration
                ++@EnableDiscoveryClient  on Application class

                eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                eureka.client.registryFetchIntervalSeconds=5
                eureka.instance.leaseRenewalIntervalInSeconds=5
                eureka.instance.leaseExpirationDurationInSeconds=5

                spring.cloud.loadbalancer.ribbon.enabled=false

        in.bta:bta-statement
            dependencies
                ++ org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                ++ org.springframework.cloud:spring-cloud-starter-loadbalancer
            configuaration
                ++@EnableDiscoveryClient  on Application class

                eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                eureka.client.registryFetchIntervalSeconds=5
                eureka.instance.leaseRenewalIntervalInSeconds=5
                eureka.instance.leaseExpirationDurationInSeconds=5

                spring.cloud.loadbalancer.ribbon.enabled=false    

    Step 3: Implement API Gateway Design Pattern
        in.bta:bta-gateway
            dependencies
                org.springframework.boot:spring-boot-devtools
                org.springframework.cloud:spring-cloud-starter-gateway
                org.springframework.cloud:spring-cloud-starter-netflix-eureka-client
                org.springframework.cloud:spring-cloud-starter-loadbalancer
            configuaration
                @EnableDiscoveryClient          on Application class

                spring.application.name=gateway
                server.port=9999

                eureka.client.serviceUrl.defaultZone=http://localhost:9000/eureka/
                eureka.client.initialInstanceInfoReplicationIntervalSeconds=5
                eureka.client.registryFetchIntervalSeconds=5
                eureka.instance.leaseRenewalIntervalInSeconds=5
                eureka.instance.leaseExpirationDurationInSeconds=5

                spring.cloud.gateway.discovery.locator.enabled=true
                spring.cloud.gateway.discovery.locator.lower-case-service-id=true
                
        in.bta:bta-discovery
        in.bta:bta-profiles
        in.bta:bta-txns
        in.bta:bta-statement
              