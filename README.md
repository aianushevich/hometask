# QA-quiz #

## Overview ##

This is a test automation framework for testing Triangle Service.

## Technical content ##
- Java 8
- Rest Assured 4.1.0 (HTTP Requests)
- JUnit 4.12
- Gson 2.8.5 (Serialization/Deserialization)
- Lombok 1.18.12 (Getters)
- Gradle 6.5.1

## Mandatory Gradle dependencies ##

- testImplementation 'org.projectlombok:lombok:1.18.12'
- testAnnotationProcessor 'org.projectlombok:lombok:1.18.12'
- testImplementation 'io.rest-assured:rest-assured:4.1.0'
- testImplementation 'junit:junit:4.12'
- testImplementation 'com.google.code.gson:gson:2.8.5'

### Execution by JUnit ###

Run TrianglesTests class

### Execution from command line ###

Prerequisites:
- Installed Gradle (Follow https://gradle.org/install)

Command (Run from /hometask/test-framework directory):
- gradle clean test

Analyze results:
- File /hometask/test-framework/build/reports/tests/test/index.html

### Execution in Docker container ###

Prerequisites:
- Installed and started Docker (Follow https://www.docker.com/get-started)
- Installed Gradle (Follow https://gradle.org/install)
- Pulled Gradle image (docker pull gradle)

Command (Run from /hometask/test-framework directory):
- Unix: docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle gradle test
- Windows: docker run --rm -u gradle -v %cd%:/home/gradle/project -w /home/gradle/project gradle gradle test

Analyze results:
- File /hometask/test-framework/build/reports/tests/test/index.html

### About test cases ###
Classes:
- All test cases have been developed in test-framework/src/test/java/testSuits/TrianglesTests.java class
- All REST methods have been developed in test-framework/src/test/java/commonTestClasses/RestApi.java class
- All Serialization and Deserialization methods have been developed in test-framework/src/test/java/commonTestClasses/JsonConverter.java class
- All classes for each response body type have been developed in test-framework/src/test/java/commonTestClasses/jsonObjects directory

Test cases:
- Each test method has its own test data
- All test data is deleted after each test method's run (By @After class)
- There are separated REST methods for same endpoints: regular (with authorization for 200OK responses), Unauthorized (without authorization), Unprocessable (with 422 response), NotFound (with 404 response)
- All environmental variables are set as system properties in build.gradle file (HOST, TOKEN)
- Each test has its own javadoc transcript

### Defects ###
- All founded defects are created here: https://github.com/aianushevich/hometask/issues
- All tests failed because of bug are marked by @Ignore annotation and TODO comment.
