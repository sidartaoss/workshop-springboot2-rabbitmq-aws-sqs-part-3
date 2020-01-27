# Spring Boot Microservice Messaging with RabbitMQ and AWS SQS - Part 3

## Reference Documentation

This project is a summary to the third and concluding part of Binit Datta's course on Udemy. It is based primarily on the references below:

* [Spring Boot 2 Microservice Messaging in RabbitMQ and AWS SQS](https://www.udemy.com/course/spring-boot-2-messaging-with-rabbitmq-activemq-and-aws-sqs/)


* [Regions and Availability Zones](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.RegionsAndAvailabilityZones.html)


* [What is Command Query Responsibility Segregation (CQRS)?](https://culttt.com/2015/01/14/command-query-responsibility-segregation-cqrs/)



### Features

These are the key features that we'll cover in this project:

        * Spring Boot 2.x

        * Producer/Consumer microservice applications

        * AWS EC2

        * AWS Relational Database Service (RDS)

        * AWS Simple Queue Service (SQS)


### Description

In this project, we use AWS SQS to send a message from a Producer to a Consumer microservice application. Before we actually start creating AWS Spring Boot programs, we will give a solid understanding about the basic concepts of Cloud and AWS; Regions and Availability Zones a little bit before we try to actually start programming for the Cloud.

Understanding the Cloud in simple terms is very critical, so we'll develop a shared understanding.


### IAAS Deployment Model

Our applications can see their own hardware. We can make our own applications see their own hardware, deploy them on bare metal machines. 

5, 10, 20 years back, we used to buy hardware and install in direct operating system on the hardware; and that's called bare metal. And on the operating system we used to deploy our application; that's called a bare metal setup.

But it is not natural for applications to have their own hardware. Our applications may not have their own resource providing hardware. So, sharing is the realistic approach.

Modern applications share the same hardware.

That is called virtualization. Sharing hardware is called virtualization. And this is the heart of Cloud based hosting. Once we understand what virtualization is, what it brings to the table, how it works, then that becomes the foundation of our understanding of the Cloud.

So, let's do a little bit of architectural diagrams.

![IAAS Deployment Model](images/Captura-de-tela-de-2020-01-26-10-57-21.png)

IAAS Deployment Model, by Binit Datta, [Udemy](https://www.udemy.com/course/spring-boot-2-messaging-with-rabbitmq-activemq-and-aws-sqs/).

At the bottom most layer, we have Infrastructure; e.g., Networking.

Host Operating System is just on top of it, it gets installed.

On top of the Host Operating System, there is a Hypervisor. And on the Hypervisor we can install multiple Guest OS's (Operating Systems).

Guest OS could be your MAC OS, we could install various forms of Linux, we can even install a Windows. On each one of the Guest OS, we can install our application. 

In this shared hardware, we may be running, for instance, three applications, each coupled with their operating system, which is a Guest OS. And that is the moment, like 2010/11, that it started in-house.

There is a company called VMware, very famous, that started the virtualization process. And this become readily very, very popular; people, companies started understanding how they can reuse their expensive massive hardware in this optimized way, installing multiple operating systems, running the databases, running the applications servers and the applications simultaneously on the same bare metal hardware.

That's the model.

And this model was taken up by large commercial Cloud Providers in a bigger way and that is the foundation of the Cloud.


### Docker Deployment Model


Following this, there is a slightly more modern deployment model.

![Docker Deployment Model](images/Captura-de-tela-de-2020-01-26-10-54-30.png)

Docker Deployment Model, by Binit Datta, [Udemy](https://www.udemy.com/course/spring-boot-2-messaging-with-rabbitmq-activemq-and-aws-sqs/).

So, this is called the concept of Containers, which is not new.

The advantage is that it is a leaner model; the same container is deployed to our Development environment, Production environment, Stage environment. Infrastructure is also shared. Then, on top of the Host Operating System, we have a Docker Engine, which is very, very lightweight.

And, as we could see, for instance, if we would look at the top boxes, what is the big elephant that is missing? It is the Guest OS. Then, normally, typically, the Guest OS comes into the range of 3 to 4 GB and if we can remove that, that is good for your application flexibility and lightweightness, as well as good for the environment, because it is not wasting disk space.

So, that is a very leaner model: Docker model, Container model. That is very closely associated with the Microservice model as well.


## AWS Structure


### Regions and Availability Zones


In this section, we will see a little bit under the hood, and we will understand what AWS structure is.

AWS, at least, has 18 to 20 Regions. Each Region is a physically geographical area (probably in a city). We have AWS Regions in West Virginia, in San Francisco area, in Japan (Tokio), in Mumbai, Singapore, etc.

![Regions and Availability Zones](images/regions-and-availability-zones.png)

Regions and Availability Zones, by unknown, [AWS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.RegionsAndAvailabilityZones.html).

What is the benefit of having AWS Regions?

Because if one particular geographical area is getting a disaster (earthquake, tsunami, wildfire or is becoming a war zone), than AWS will be able to provide survice from other geopraphical isolated and separate regions.

So, this is a conceptual design: the application designers/architects have to take advantage of this. It is quite possible that if we deploy our application only in a single AWS region, and that region goes out, AWS is not entirely responsible. So, there is joint responsibility: high availability or Cloud disaster recovery is a joint responsibility between AWS and the application architecture team.

So, inside of one single Region, there are multiple Availability Zones.

What are Availability Zones?

We can say that one Availability Zone will be several buildings connected through a land setup, like we can imagine a cluster of 10 buildings, each connected with physical wires, and that becomes an Availability Zone.

What is the advantage?

So, normally, AWS says, for normall availability, if we would deploy our database in multiple Availability Zones, AWS ensures that one Availability Zone, two Availability Zones or multiple Availability Zones will not go down normally at the same time; then we gain instant high availability.

In that case, even if deploy in a single Region, we get high availability just by using the multiple Availability Zones.

Region outage is really really rare, so, basically, for normal high availability, we are good if we consider just Availability Zones. But if we want to really have applications disaster recovery really ready, then we would need to consider multiple region deployment as well, which is more difficult than a single region.


#### AWS Advantages of Regions and Availability Zones



            On Premise Disaster Recovery

Is really expensive. In case of disaster, all the things, from buildings, hardware, software and people need to be replaced miles away from each other.



		AWS Regions

AWS Regions are insured against natural disasters and even war. So, we can migrate and bring up new capability online within minutes if we design properly.



		Low Cost

When we compare the cost of multi Region availability to the cost of the same on premise. The Cloud operates on the virtualization model and the hardware is shared among a lot of customers, so AWS can provide the cheap cost.




#### Formal Concepts


		What are Regions?

Geographically separate cluster of Availability Zones connected in a Metropolitan Area Network (MAN)

		


		What are Availability Zones?
		
Geographically separate by closely located set of buildings connected in a Local Area Network (LAN)


		

		One Region Many Availability Zones
		
One Region typically offers several Availability Zones. For small outages and normal DR (Disaster Recovery), we can expect that multi-AZ (Availability Zone) deployment will takes us far.




## CIDR



In this section, we'll start understanding AWS networking, but, before we do that, we need to have a very clear understanding about Classless Inter Domain Routing, which is called CIDR.

CIDR is the basis of all Cloud Networking, even on premise.



#### AWS CIDR

To understand Cloud based Networking, the understanding of IPv4 address scheme and CIDR is very critical.

What are IPv4 addresses?

Four decimal numbers separated by a dot.

Example: 192.168.0.1


	
#### Octet

Each number separated by a dot (such as 192 or 168 or 0 or 1) is called an Octet and is represented in binary.


To feel comfortable creating VPC and Subnets, having a clear understanding of CIDR is useful.

Example:

	Decimal			Binary

	192 			11000000

	168 			10101000

	0 			00000000

	1			00000001



#### IPv4 Limitation

Limited Number of IPv4 addresses

With millions of new devices joining the Internet, we would have ran out of physical IP addresses much before. 



#### We Do Not Have Unique Physical Addresses

In real life, it is possible for two cities to have the same house number. However, the house number coupled with city name, zip code, country, etc., makes the address unique. The idea is very very closely related to the Classless Inter Domain Routing idea: it's the same concept.


### How CIDR Works



#### 192.168.0.0/24


Specifying the /24 here means that we are denoting 3 out of 4 bytes as the network prefix and one byte as the subnet position.
		
If we draw a paralell, the moment we say: "Don't consider the house address, first consider the state, which is California". California has hundreds of locations. Then, let's come down one level, let's say the level is a city: San Francisco. Now, we have a smaller location inside the city: let's consider a zip code, because a city has multiple zip codes. And, inside a zip code, let's consider a street address. That's how post addresses are delivered.

When a network package comes, that's how routers behave. Routing the network package to the exact network subnet, it tries to determine where the network is, and that's where the concept of the network part of the address and the host part of the address comes. So, here in the /24, we are saying: in this: 192.168.0.0 address, there will be first 3 bytes that will be the network notifying part. And the last one will be the host notifying part.



#### 192.168.0.0/16

This is more normal, as we can get the last two positions for creating subnets. In this case, we are saying just the first two bytes will be the network notifying address. We'll see how it works.


##### Subnet Address

Subnet addresses under the 192.168.0.0/16 would be like 192.168.1.0, 192.168.2.0, and so on. 192 and 168 are common for both the addresses, and 1.0 and 2.0 are different; so the last two become the host addresses and the first two become the networking address: that's how routers behave.

Routers must be able to identify from our IPv4 address what network it is. And that's where we help by telling the Router: the first two, or the first three is the network address, and it becomes very easy for the router to route network packages.


#### 192.168.1.0/24

Specifying the /24 here means that we are denoting 3 out of 4 bytes as the network prefix, including the network and the subnet and one byte as the host position.


#### Actual Host Address

As an example, host addresses under the subnet 192.168.1.0/24 could be 192.168.1.10 (.10 here is the only host part, otherwise, we can see 192.168.12 is the full network part), 192.168.1.11, etc. So, 10 and 12 are the host part and then the other beginning 3 is the network part.


#### Other Subnet Host Addresses

Other subnets could have hosts such as 192.168.2.10, 192.168.2.12, etc. So, we have one network, one subnet, which starts with 3 bytes that are 192.168.1 and another subnet that are 192.168.2, and then there could be 192.168.3, and so on. So, each one of them, the subnets may have the last digit, like 0 to 255 address.

So, in this scheme, you can only use 256 hosts, which is really less.



## AWS RDS

In this section, we'll talk about AWS Relational Database service, which is in short called RDS.


		PAAS

AWS RDS is a Platform As A Service or PAAS from AWS for Relational Databases.
This means we are free from installation, licenses, patching, upgrades, etc.


		


		Database Products

AWS supports Aurora, PosgreSQL, MySQL, Oracle and SQL Server.





		Easy to Administer

AWS RDS has Admin Console Access, as well as can be managed through AWS CLI and Cloudformation.





		
		
		Highly Scalable

For many RDS Database types, AWS supports Read Replicas, that can help us scale our applications for higher traffic. What are Read Replicas? So, all read request will be served by a separate database instance, freeing the master for only catering to the writes.





        Available and Durable

AWS RDS supports Multi AZ (Availability Zone) deployments, and in the event of the master failure, the slave instance can quickly assume the role of the master and AWS will silently change the DNS behind the scenes.




		Fast

We have the choice of fast SSD Storage that are good for Online Transaction Processing (OLTP) applications as well as a general purpose one.





		Secure

AWS RDS runs within our own VPC subnets and are controlled by Network Access Control List (NACL) and Security Group rules.




		Inexpensive

AWS RDS may save us large sum of money we will pay otherwise if we choose to have the database installed on IAAS or on premise.

So, in the picture below, we can see what it means by a Multi AZ deployment. So, the master is in one Availability Zone. The slave is in a different Availability Zone and all these Availability Zones are in a single Region. So, the writes go to the master and the reads go to the slave, and AWS asynchronously can update the master and slave.


![AWS RDS Multi AZ Deployments](images/Captura-de-tela-de-2020-01-26-17-00-52.png)

AWS RDS Multi AZ Deployments can deliver high availability, by Binit Datta, [Udemy](https://www.udemy.com/course/spring-boot-2-messaging-with-rabbitmq-activemq-and-aws-sqs/).




		EASY

When we specify Multi AZ option during the database creation, AWS automatically creates a synchronous standby replica.





		Increased Latency

Due to the synchronous nature of maintaining slaves accross AZ's, applicationss may experience slight higher latencies (only the writes). For production, applications provisioned Input/Output Operations  Per Second (IOPS) and larger DB Instance sizes are recommended, so that network latency, the pipe we are experiencing is big enough to take our traffic. AWS also guarantees low latencies across AZs.






		Notification

AWS can send us notification via email or SMS when database failure is initiated. What is a database failure? When the master fails, the slave will become the master and AWS will update the DNS. If our application has established a connection to the master, through the same DNS, and the master when down, the slave becomes the master, that DNS got updated, but your application will have to reestablish the connection. So we should expect when there is a connection failure for the application to reestablish the connection.

In the picture below, we can see two Availability Zones, in one Availability Zone, there is a RDS Master instance and in the other Availability Zone, there is a RDS Standby Replica (slave) instance. As we can see, Synchronous Replication is happening from master to slave, and the slave is taking for all the reads.

![AWS RDS Read Replicas](images/Captura-de-tela-de-2020-01-26-16-52-45.png)

AWS RDS Read Replicas, by Binit Datta, [Udemy](https://www.udemy.com/course/spring-boot-2-messaging-with-rabbitmq-activemq-and-aws-sqs/).







	    More Reads than Writes

If our application has more database reads than writes, creating a Read Replica can help us scale well. The updates are asynchronous and won't impact latency. 






		Batch Reads

We can also make our business data warehouse/reporting application use the Read Replicas, leaving the primary database for OLTP customers.






		Multi Region

AWS can also support multi Region Read Replicas as well.



## AWS Messaging


In this section, we will talk about AWS Messaging.


		How We Use Messaging

We call friends and family and leave a message if they are not available to pick up our calls.





		Applications Use Messaging The Same Way For Responding Fast

Order Management Systems (OMS's), for example, do not wait to send an email to the customer in order to respond quickly, because it has to respond within milliseconds, and using an Email Service may take the latency to seconds, which is not desirable. So, it sends a message to the Email Service instead and the email comes a few seconds or minutes after.
		





        IAAS or PAAS

You can make your Messaging Platform, i.e., RabbitMQ or use AWS SQS.

When we consider Messaging, which is really critical for our applications, we have two choices, we can choose our own Infrastructure As A Service (IAAS), like creating a new EC2 instance VM and install RabbitMQ or any other Messaging software and we have our messaging that way or we can use AWS Platform As A Service (PAAS), which simply calls AWS Simple Queue Service (SQS).







### Why we need to use PAAS? Why PAAS is better than IAAS?



		Security

Our messages sent to SQS can be secured from unwanted audiences and even encrypted if they need to be.







		Durability

SQS messages are durable as they are stored on multiple servers. AWS supports at least once delivery for standard queues and exactly once delivery for FIFO Queues.




		Availability


AWS SQS, like all AWS services, use redundant infrastructure to support high availability.





	
		Scalability

AWS SQS supports high scalability at a very low cost compared to our home grown IAAS based scalability.







		Reliability

SQS locks our messages during processing. Multiple producers and multiple consumers can safely produce and consume messages without thinking about being consistent.








		Custom Use Cases

When message sizes are too big for SQS, we can use other services like S3, DynamoDB and store the pointers in SQS.

For example, if we were trying to send an image as a message, and the image is too big, we can store the image in a AWS S3, which is a Blob storage, and the S3 URL we can send that instead as a message to SQS.




## Create AWS Environment

Through the web wizard of the AWS EC2 Service, we should create a new EC2 Instance. Below we can see some of its configuration details.


![AWS EC2 Instance configuration details](images/Captura-de-tela-de-2020-01-26-17-29-32.png)


Now, we should ssh into the newly created VM and do the following:

                - Update the patches

                - Install JDK 8 

                - Install Gradle and Git



### Create AWS MySQL RDS

Through the web wizard of the AWS RDS Service, we should create a new MySQL RDS Database. Below we can see some of its configuration details.


![AWS MySQL RDS configuration details](images/Captura-de-tela-de-2020-01-26-17-46-50.png)


### AWS IAM


We should create a new user for generating AWS Credentials as well. We can do that through the web wizard of the AWS IAM Service. We can see some of its configuration details below.


![AWS MySQL RDS configuration details](images/Captura-de-tela-de-2020-01-26-22-08-17.png)


We'll then create the security credentials to provide to AWS when we send a message to the AWS SQS Queue in our Producer Application.


### Create Simple Queue Service Queue


Now, through the web wizard of the AWS SQS Service, we should create a new Queue named exactly as TODO_Queue. Below we can see some of its configuration details.


![AWS SQS configuration details](images/Captura-de-tela-de-2020-01-26-17-53-59.png)


### Verify DB Connection From Local Client

To verify the Database connection to AWS RDS MySQL Database from our local client, we can open a new Terminal window and enter the following command:


        mysql -h [AWS RDS endpoint] -u udemyawssqsdb -p
-


        sidarta@sidarta-Inspiron-7580:~$ mysql -h udemyawssqsdb.c5m7lgw4quio.us-east-1.rds.amazonaws.com -u udemyawssqsdb -p
        Enter password: 
        Welcome to the MySQL monitor.  Commands end with ; or \g.
        Your MySQL connection id is 2215
        Server version: 8.0.15 Source distribution

        Copyright (c) 2000, 2017, Oracle and/or its affiliates. All rights reserved.

        Oracle is a registered trademark of Oracle Corporation and/or its
        affiliates. Other names may be trademarks of their respective
        owners.

        Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

        mysql> use udemyawssqsdb;
        Database changed
        mysql> show tables;
        +--------------------------------+
        | Tables_in_udemyawssqsdb        |
        +--------------------------------+
        | rollingstone_aws_sqs_demo_todo |
        +--------------------------------+
        1 row in set (0.26 sec)



## AWS SQS Programming


### Creating the Spring Boot Producer Application


There are two Microservices for this application, working together. The first Microservice is called rollingstone-ecommerce-cqrs-todo-rest-api.

So, the Producer application is a REST API that will receive a message from the client and convert the message into a Java POJO and send a message to AWS SQS, and there will be another Consumer application that will receive that message from the AWS SQS and persist the message into RDS Dabatase.

The CQRS initials in the Producer application name stand for Command Query Responsibility Segregation.




#### Command Query Responsibility Segregation (CQRS)


Command Query Responsibility Segregation (CQRS) is an architectural pattern that separates reading and writing into two different models.

This means that every method should either be a Command that performs an action or a Query that returns data. A Command cannot return data and a Query cannot change the data.

In order to read data from the database we would need a separate read model. For example, this might be a dedicated Repository that is specifically tuned to return the correct data for an appropriate view.

By splitting the application into dedicated read and write models, you move the responsibility into dedicated objects. The write model does not need to be concerned with returning data and the read model can be specifically written to return the correct data to satisfy the application's requirements.



### Explaining and Understanding the Applications Flow

There is a lot of interfaces being used, so it would be helping all of us a lot if we can explain the applications flow before we actually start testing it.

So, as we said, there is a couple of Microservices, and before we go further, let's see some of the Java classes in these applications.

The first class we'll encounter in the Producer application is the TodoController. Let's focus on the TodoController class and not only the class, its constructor.

So, in the first Microservice, rollingstone-ecommerce-cqrs-todo-rest-api, TodoController is the Java class that represents the public facing API, i.e., the class that is receiving all requests from the Internet.

So, there are many methods in the class, but we'll focus on the createTodo() method, which is actually sending a message to SQS, a Todo message.

And the first thing to understand is the TodoController constructor, how the constructor works.

            private final GenericCommandBus genericCommandBus;
            
            private final TodoService todoService;
            
            private static Validator validator;

            public TodoController(GenericCommandBus genericCommandBus, TodoService todoService) {
                this.genericCommandBus = genericCommandBus;
                this.todoService = todoService;
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                validator = factory.getValidator();
            }


The constructor takes two parameters, an instance of the GenericCommandBus interface and an instance of the TodoService class.

When we specify an interface in a constructor/method, what we are basically telling Java is that we will send an instance of a class that implements the interface.

So, instead of GenericCommandBus, what will come to the TodoController class's constructor will be an instance of a class that implements the GenericCommandBus interface.

Looks like com.rollingstone.dispatcher.DispatcherCommandBus implements GenericCommandBus:


            @Component
            public class DispatchingCommandBus implements GenericCommandBus {

                private final GenericCommandDispatcher genericCommandDispatcher;
                
                public DispatchingCommandBus(GenericCommandDispatcher genericCommandDispatcher) {
                    this.genericCommandDispatcher = genericCommandDispatcher;
                }

                @Override
                public <T extends GenericCommand> Future<GenericCommandResult> send(T command) {
                    return this.genericCommandDispatcher.dispatch(command);
                }
                
            }


So, what we are saying is that we can send an instance of GenericCommandBus to this TodoController constructor. 

Therefore, at runtime, Spring will be able to locate this DispatchingCommandBus as it implements GenericCommandBus, and it is annotated with the @Component annotation. So, it will be easy to Spring to create an instance and fit the constructor easily.

The second paramenter, which is really easy, is the TodoService. It is annotated with the @Service annotation. So, Spring will be able to pick it up and send it here.

So, we have understood how we the constructor works. Let's move on to understand the actual method: createTodo().


    @PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public RSResponse<Todo> createTodo(@RequestBody Todo todo) {
		RSResponse<Todo> rsResponse = new RSResponse<>();
		
		LOG.info("Received request to create Todo");
		
		Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);
		
		String errorMessage = this.buildErrorMessage(constraintViolations);
		
		if (!errorMessage.isEmpty()) {
			LOG.error("Error When Creating Todo: " + errorMessage);
			rsResponse.setErrorMessage("Error When Creating Todo: " + errorMessage);
			return rsResponse;
		} else {
			TodoCommand todoCommand = new TodoCommand();
			todoCommand.setTodo(todo);
			todoCommand.setId(UUID.randomUUID());
			
			GenericCommandHeader header = new GenericCommandHeader(GenericCommandType.CREATE_TODO.toString(), SCHEMA_VERSION, new Timestamp(System.currentTimeMillis()));
			
			todoCommand.setHeader(header);
			
			this.genericCommandBus.send(todoCommand);
			
			this.eventPublisher.publishEvent(new TodoServiceEvent(this, todo, "TodoCreatedEvent"));
			
			rsResponse.setMessage("Todo sent to AWS for creation");
			rsResponse.setPayload(todo);
			
			return rsResponse;
			
		}
	}


Now, let's understand how the createTodo() method works.

It receives a HTTP Post request, which includes a HTTP RequestBody, and converts that JSON object to a Todo Java POJO.

The Todo Java POJO is annotated with Validation annotations. So, we are using Java validation.

So, we can easily use Java validator API to determine if the request object is valid, has all the mandatory in it or not; if the validation fails, we'll send a 200 response with the error message to our client. However, if the validation succeeds, we will create a Command message using a TodoCommand Java class.

So, what is a TodoCommand?

The TodoCommand is a message carrier and has an id like our FedEx USPS shipment has a unique tracking id.

The TodoCommand also has a Header, like our FedEx package, or USPS package, as an evelope, containing the To Address and From Address.

So, that is the role of the Header here. 

Finally, the TodoCommand has a payload like our FedEx package has the payload, the Todo object we received as request is the payload here.

So, when we open a physical FedEx package, we find our letter or actual object that was shipped to us inside. In this also when we open the TodoCommand message, we find three things:

    * an ID, which is the FedEx tracking id

    * a Header

    * a Payload


Once we create the TodoCommand and set an unique ID as well as the Payload, with the Todo object we received as the request payload we create the GenericCommandHeader.

So, this is like when we ship through FedEx they want us to write the destination address, our destination address is a AWS SQS Queue name, and that Queue name will be held indirectly in the GenericCommandHeader.

The GenericCommandHeader contains our destination address, i.e., AWS SQS Queue name, like our FedEx package has the destination's address.

The GenericCommandHeader also has the timestamp, like FedEx shipment has the timestamp to tell us when we shipped it.

Finally, the GenericCommandHeader has a schema version to help determine what version of the object we are sending.

After we create the GenericCommandHeader, we simply set the GenericCommandHeader to the TodoCommand.

Immediately after, we use the CommandBus to send the message which is the TodoCommand instance.

The CommandBus is an instance of DispatchingCommandBus.

So immediately after we have understood the TodoController.createTodo() method and how it works, we need to go to and take a look at the DispatchingCommandBus class.

Then, TodoController calls DispatchingCommandBus.

The DispatchingCommandBus uses SQSCommandDispatcher. 

The SQSCommandDispatcher uses the SQSCommandQueueNameResolver to determine the actual AWS SQS Queue name from the CommandHeader type. As we said, the CommandHeader has the destination address.

The SQSCommandDispatcher then uses the SQSQueueSender class to send the message to AWS SQS.

Finally, the SQSQueueSender uses Spring Framework's AWS Library's QueueMessagingTemplate class to finally send the message to AWS SQS Queue.


### Create Properties


These are the configuration properties in the Producer microservice application:


            queue.debug=false
            server.port=8082
            management.server.port=9082
            sqs.todo.queue=TODO_Queue
            spring.database.driverClassName=com.mysql.jdbc.Driver
            spring.datasource.url=jdbc:mysql://udemyawssqsdb.c5m7lgw4quio.us-east-1.rds.amazonaws.com:3306/udemyawssqsdb
            spring.datasource.username=udemyawssqsdb
            spring.datasource.password=awssqsdb
            spring.datasource.validationQuery=SELECT 1
            spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
            spring.jpa.hibernate.ddl-auto=update
            cloud.aws.region.static=us-east-1
            cloud.aws.stack.auto=false
            management.endpoints.web.exposure.exclude=*
            aws.access-key=AKIAR5ZWVXHP4S46OHML
            aws.secret-key=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



### Execute the Test


            * ssh into the EC2 instance (public IP: 3.92.24.99)


            * git clone Producer and Consumer microservice applications

                    * git clone https://github.com/sidartaoss/rollingstone-ecommerce-cqrs-todo-rest-api.git

                    * git clone https://github.com/sidartaoss/rollingstone-ecommerce-cqrs-todo-command-consumer.git


            * Start the Producer microservice application

                    * cd rollingstone-ecommerce-cqrs-todo-rest-api

                    * gradle clean build -x test

                    * java -jar build/libs/rollingstone-ecommerce-cqrs-todo-rest-api-1.0.jar


Let's come back to our REST client and prepare a request to the EC2 instance.


            * Open Postman

            * Enter the URL: http://3.92.24.99:8082/rsecommerce/cqrs/todo/api

            * Select POST as HTTP method

            * Select the Body Tab

            * Enter

                {
                    
                    "shortDescription": "First TODO for test",
                    "longDescription": "First TODO for test Long Description"
                    
                }

            
![Postman - Save new Todo (AWS EC2)](images/Captura-de-tela-de-2020-01-27-08-17-29.png)


            * Open AWS Console and go to the Simple Queue Service.

            * Select TODO_Queue Queue

            * In the Queue Actions button, select View/Delete Messages and then click the Start Polling for Messages button


![AWS Console (SQS) - Polling for messages](images/Captura-de-tela-de-2020-01-27-08-27-05.png)


            * In the Postman Rest client, click Send

            * Check if we are getting the Http 201 Created


![Postman - 201 Created](images/Captura-de-tela-de-2020-01-27-08-38-39.png)



            * In the AWS SQS Console, check if there are any messages


If there are any messages, it means that the AWS SQS Queue has received a message from the Producer microservice application.


![AWS SQS Console - Checking for new messages](images/Captura-de-tela-de-2020-01-27-08-40-32.png)


            * Check the Application Console

    2020-01-27 12:07:22.500  INFO 11814 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8082 (http) with context path ''
    2020-01-27 12:07:22.501  INFO 11814 --- [           main] stoneEcommerceCqrsTodoRestApiApplication : Started RollingstoneEcommerceCqrsTodoRestApiApplication in 10.294 seconds (JVM running for 10.83)
    2020-01-27 12:08:03.364  INFO 11814 --- [nio-8082-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
    2020-01-27 12:08:03.364  INFO 11814 --- [nio-8082-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
    2020-01-27 12:08:03.383  INFO 11814 --- [nio-8082-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 19 ms
    2020-01-27 12:08:03.446  INFO 11814 --- [nio-8082-exec-1] com.rollingstone.api.TodoController      : Received request to create Todo
    2020-01-27 12:08:03.458  INFO 11814 --- [nio-8082-exec-1] c.r.cloud.messaging.aws.SQSQueueSender   : The Queue name is: TODO_Queue
    2020-01-27 12:08:04.065  INFO 11814 --- [nio-8082-exec-1] c.r.listener.TodoEventListener           : Received Todo Event Type: TodoCreatedEvent 
    2020-01-27 12:08:04.066  INFO 11814 --- [nio-8082-exec-1] c.r.listener.TodoEventListener           : Received Todo from Todo Event: Todo [id=null, shortDescription=First TODO for test, longDescription=First TODO for test Long Description] 



As we can see in the logs above, a new message was sent by the Producer application to the TODO_Queue queue. Before starting the Consumer application, let's check the database table


    mysql> select * from rollingstone_aws_sqs_demo_todo;
    Empty set (0.18 sec)


As we can see, there is still no record created, because the message was not consumed and put into the table. So, we'll open another Terminal window and we'll ssh into the EC2 instance and start the Consumer application.


                    * cd rollingstone-ecommerce-cqrs-todo-command-consumer

                    * gradle clean build -x test

                    * java -jar build/libs/rollingstone-ecommerce-cqrs-todo-command-consumer-1.0.jar

As we can see in the logs below, the moment we run the Consumer application, it starts looking into the SQS Queue and it receives the message that was sent by the Producer application. SQS is sending the message to the Consumer application, and then it is able to process the message.

    2020-01-27 12:11:42.073  INFO 11949 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8084 (http) with context path ''
    2020-01-27 12:11:42.073  INFO 11949 --- [           main] mmerceCqrsTodoCommandConsumerApplication : Started RollingstoneEcommerceCqrsTodoCommandConsumerApplication in 9.934 seconds (JVM running for 10.543)
    2020-01-27 12:11:42.226  INFO 11949 --- [enerContainer-2] c.r.listener.AWSSQSQueueListener         : We received the TodoCommand Message from AWS SQS
    2020-01-27 12:11:42.227  INFO 11949 --- [enerContainer-2] c.r.listener.AWSSQSQueueListener         : The Todo We Received is: Todo [id=null, shortDescription=First TODO for test, longDescription=First TODO for test Long Description]
    2020-01-27 12:11:42.237  INFO 11949 --- [enerContainer-2] c.r.aop.CommandListenerAspect            : :::: AOP Method that would be called in saveTodo use case: execution(void com.rollingstone.service.TodoService.saveTodo(Todo))
    2020-01-27 12:11:42.246  INFO 11949 --- [enerContainer-2] com.rollingstone.service.TodoService     : Calling saveTodo(): Todo [id=null, shortDescription=First TODO for test, longDescription=First TODO for test Long Description]
    2020-01-27 12:11:42.336  INFO 11949 --- [enerContainer-2] c.r.listener.AWSSQSQueueListener         : Created a new Todo: Todo [id=13, shortDescription=First TODO for test, longDescription=First TODO for test Long Description]



Now, we'll go back to our local MySQL client and let's check if the record was inserted by the Consumer application.


    mysql> select * from rollingstone_aws_sqs_demo_todo;
    +----+--------------------------------------+---------------------+
    | id | long_description                     | short_description   |
    +----+--------------------------------------+---------------------+
    | 13 | First TODO for test Long Description | First TODO for test |
    +----+--------------------------------------+---------------------+
    1 row in set (0.18 sec)



Then, let's check all the Todo records that were created.


![Postman - Get All](images/Captura-de-tela-de-2020-01-27-09-19-33.png)


Let's check the newly created Todo record by ID.


![Postman - Get by ID](images/Captura-de-tela-de-2020-01-27-09-21-45.png)


And now let's delete the newly created Todo record.


![Postman - Deletion](images/Captura-de-tela-de-2020-01-27-09-25-13.png)


We can see that the operation was successful because we received a Http 204 No Content response. Let's check the database table as well.


    mysql> select * from rollingstone_aws_sqs_demo_todo;
    Empty set (0.18 sec)


And we can check in the Postman Rest client the deletion of the Todo record with a Get Request as well.

![Postman - Checking for deletion](images/Captura-de-tela-de-2020-01-27-09-28-59.png)



