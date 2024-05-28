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