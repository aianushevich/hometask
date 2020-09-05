package commonTestClasses;

import commonTestClasses.jsonObjects.ErrorObject;
import commonTestClasses.jsonObjects.ResultObject;
import commonTestClasses.jsonObjects.TriangleObject;
import io.restassured.response.Response;

import java.util.logging.Logger;

import static commonTestClasses.JsonConverter.*;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static testSuits.TrianglesTests.*;

public class RestApi {
    private final static String CREATE_PATH = "/triangle";
    private final static String GET_BY_ID_PATH = "/triangle/";
    private final static String GET_ALL_PATH = "/triangle/all";
    private final static String DELETE_BY_ID_PATH = "/triangle/";
    private final static String GET_PERIMETER_PATH = "/triangle/%s/perimeter";
    private final static String GET_AREA_PATH = "/triangle/%s/area";
    private final static String WRONG_TOKEN = "wrong_token_for_test";
    private final static int STATUS_OK = 200;
    private final static int STATUS_UNPROCESSABLE = 422;
    private final static int STATUS_UNAUTHORIZED = 401;
    private final static int STATUS_NOT_FOUND = 404;

    private static Logger logger = Logger.getLogger(Logger.class.getName());

    public static TriangleObject getTriangleById(String id) {
        Response response = given()
                .header("X-User", authToken)
                .get(GET_BY_ID_PATH + id);
        assertEquals(STATUS_OK, response.getStatusCode());
        logger.info("Triangle has been successfully received by GET method");
        return jsonToTriangleObject(response);
    }

    public static ErrorObject getTriangleByIdUnauthorized(String id) {
        Response response = given()
                .header("X-User", WRONG_TOKEN)
                .get(GET_BY_ID_PATH + id);
        assertEquals(STATUS_UNAUTHORIZED, response.getStatusCode());
        logger.info("Triangle hasn't been received. Check your token");
        return jsonToErrorObject(response);
    }

    public static TriangleObject createTriangle(String body) {
        Response response = given().body(body)
                .header("X-User", authToken)
                .contentType("application/json;charset=utf-8")
                .post(CREATE_PATH);
        assertEquals(STATUS_OK, response.statusCode());
        logger.info("Triangle has been successfully created by POST method");
        return jsonToTriangleObject(response);
    }

    public static ErrorObject createTriangleUnauthorized(String body) {
        Response response = given().body(body)
                .header("X-User", WRONG_TOKEN)
                .contentType("application/json;charset=utf-8")
                .post(CREATE_PATH);
        assertEquals(STATUS_UNAUTHORIZED, response.statusCode());
        logger.info("Triangle hasn't been created. Check your token");
        return jsonToErrorObject(response);
    }

    public static ErrorObject createTriangleUnprocessable(String body) {
        Response response = given().body(body)
                .header("X-User", authToken)
                .contentType("application/json;charset=utf-8")
                .post(CREATE_PATH);
        assertEquals(STATUS_UNPROCESSABLE, response.statusCode());
        logger.info("Triangle hasn't been created. Check your request");
        return jsonToErrorObject(response);
    }

    public static TriangleObject[] getAllTriangles() {
        Response response = given()
                .header("X-User", authToken)
                .get(GET_ALL_PATH);
        assertEquals(STATUS_OK, response.getStatusCode());
        logger.info("All triangles have been successfully received by GET method");
        return jsonToTrianglesList(response);
    }

    public static ErrorObject getAllTrianglesUnauthorized() {
        Response response = given()
                .header("X-User", WRONG_TOKEN)
                .get(GET_ALL_PATH);
        assertEquals(STATUS_UNAUTHORIZED, response.getStatusCode());
        logger.info("All triangles haven't been received by GET method. Check your token");
        return jsonToErrorObject(response);
    }

    public static ResultObject getTriangleArea(String id) {
        Response response = given()
                .header("X-User", authToken)
                .get(String.format(GET_AREA_PATH, id));
        assertEquals(STATUS_OK, response.getStatusCode());
        logger.info("Triangle's area has been successfully received by GET method");
        return jsonToResultObject(response);
    }

    public static ErrorObject getTriangleAreaUnauthorized(String id) {
        Response response = given()
                .header("X-User", WRONG_TOKEN)
                .get(String.format(GET_AREA_PATH, id));
        assertEquals(STATUS_UNAUTHORIZED, response.getStatusCode());
        logger.info("Triangle's area hasn't been received by GET method. Check your token");
        return jsonToErrorObject(response);
    }

    public static ResultObject getTrianglePerimeter(String id) {
        Response response = given()
                .header("X-User", authToken)
                .get(String.format(GET_PERIMETER_PATH, id));
        assertEquals(STATUS_OK, response.getStatusCode());
        logger.info("Triangle's perimeter has been successfully received by GET method");
        return jsonToResultObject(response);
    }

    public static ErrorObject getTrianglePerimeterUnauthorized(String id) {
        Response response = given()
                .header("X-User", WRONG_TOKEN)
                .get(String.format(GET_PERIMETER_PATH, id));
        assertEquals(STATUS_UNAUTHORIZED, response.getStatusCode());
        logger.info("Triangle's perimeter hasn't been received by GET method. Check your token");
        return jsonToErrorObject(response);
    }

    public static void deleteTriangleById(String id) {
        Response response = given()
                .header("X-User", authToken)
                .delete(DELETE_BY_ID_PATH + id);
        assertEquals(STATUS_OK, response.getStatusCode());
        logger.info("Triangle has been successfully deleted");
    }

    public static ErrorObject deleteTriangleByIdUnauthorized(String id) {
        Response response = given()
                .header("X-User", WRONG_TOKEN)
                .delete(DELETE_BY_ID_PATH + id);
        assertEquals(STATUS_UNAUTHORIZED, response.getStatusCode());
        logger.info("Triangle hasn't been deleted. Check your token");
        return jsonToErrorObject(response);
    }
}