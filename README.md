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
    
javax.validators

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
        