# roadside-assistance

## About this project
This project manages Roadside Assistance service

### How to run the project
#### Clone the project from GitHub
git clone https://github.com/sanjib-bag/roadside-assistance.git

#### Go to the project directory
cd roadside-assistance/

#### Set JAVA_HOME
export JAVA_HOME=<Directory path to Java Home installation>
- Please note that this project requires Java 1.8 (it is tested using corretto-1.8.0_372 Java version)

#### Run maven test from project home directory (use mvnw or mvnw.cmd based on the machine OS)
./mvnw clean test

- Please note that due to time constraints not all classes are implemented and all test cases are not written for all classes.
- Mainly the RoadsideAssistanceService is implemented using RoadsideAssistanceServiceImpl class.
- The test cases for RoadsideAssistanceServiceImpl class can be found in RoadsideAssistanceServiceImplTest.
- The RoadsideAssistanceRepository implementation (i.e. RoadsideAssistanceRepositoryImpl) is written to support test cases execution only. This may not be the actual real-life implementations.
- The REST interface are not implemented as part of the RoadsideAssistanceController.
- The main focus of this assignment is to provide implementation of RoadsideAssistanceService and possible test cases.