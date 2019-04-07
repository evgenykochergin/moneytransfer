# Money transfer task

Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

Explicit requirements:

1. Keep it simple and to the point (e.g. no need to implement any authentication, assume the APi is invoked by another internal system/service)
2. Use whatever frameworks/libraries you like (except Spring, sorry!) but don't forget about the requirement #1
3. The datastore should run in-memory for the sake of this test
4. The final result should be executable as a standalone program (should not require a pre-installed container/server)
5. Demonstrate with tests that the API works as expected

Implicit requirements:

1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

## Libraries used
- **Guice** - dependency injection, inversion of control (IoC) 
- **H2** - in-memory database
- **Jackson** - for JSON serialization and deserialization
- **HikariCP** - JDBC connection pool
- **Jetty** - embedded HTTP server
- **JUnit** - unit testing
- **Hamcrest** - flexible unit testing extension
- **REST-assured** - testing and validation of REST services
