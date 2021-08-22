# Addressbook Information

## Please Thoughprocess.md for information on how I went about the code.

### Technical Requrements
* Operating system : Any linux machine which can build and run a shell script
	* Gradle , will be installed on use
	* Docker 19
	* open jdk 11
	
*  Software used
	* Open Jdk 11
	* Docker 19
	* SpringBoot 2.5
	* Junit 5
	* Hibernate with JPA
	* Lombok
	* Jacoco
	* PMD (quality)
	* CheckStyles - googleCheckstyle
	* Docker-compose for microservice deployment
	* Swagger Docs for ease of use. (Open-api 3 specs) The new Spring swagger specs has some bugs :P
	* H2 Db for Testing
	* Postgres for Production
	* Liquibase for DB versioning
	* Jasypt for encryption db password.
	* Gradle for build
	* Spring boot actuator for health check , used inside docker compose
	
	
	
### Assumptions and Excuses
* Added less logging using logback and lombok
* Lack of time , hence could not do multi user support. Currently its a global addressbook.
	* Can make multi user with little bit more time. Need to add a User Table and associate with addressbook table
* Provided API which will be consumed by frontend.
	* Not Secure with apikey
	* Would have used Spring cloud gateway to do the same. (but lacked time)
* Time Spent : 4 days 3 hours per day.
* Unit Tests in DB and Core module.
* Integration test in Rest Module (includes starting server) Little less coverage due to time constraints.



### How to Run
* Checkout the Repository
* Go to current working directory 'address-book-app'
* Run the below commands
```
./build_run.sh.sh
```

* The above will download gradle, build the project and publish Jacoco reports
* It will build docker images for the projects
* it will start the docker compose which uses postgres and springboot

### Access the application API
* Go to http://localhost:8080/address-book.html and play with the API. (Added addressbook with ID 1) You can click the button just to add contacts. duplicate contacts can be added.


