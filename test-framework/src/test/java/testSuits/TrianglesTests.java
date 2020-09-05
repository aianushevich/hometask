package testSuits;

import commonTestClasses.jsonObjects.ErrorObject;
import commonTestClasses.jsonObjects.TriangleObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static commonTestClasses.JsonConverter.inputObjectToJson;
import static commonTestClasses.RestApi.*;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

public class TrianglesTests {
    public static String authToken;
    public static String testUser;
    private final static String INITIAL_INPUT = "3;4;5";
    private final static String FAKE_TRIANGLE_ID = "15d15ba9-1745-4aa2-824b-6c6b89367f53";
    private final static String UNAUTHORIZED_ERROR = "Unauthorized";
    private final static String UNPROCESSABLE_ERROR = "Unprocessable Entity";
    private final static String UNPROCESSABLE_MESSAGE = "Limit exceeded";

    @BeforeClass
    public static void configureRestAssured() {
        authToken = System.getProperty("TOKEN");
        baseURI = System.getProperty("HOST");
        testUser = System.getProperty("TEST_USER");
    }

    /**
     * Test #1: testTriangleCreationWithIncorrectToken
     * 1. Try to create triangle with incorrect token (POST /triangle) and parse response into ErrorObject.
     * 2. Verify applicable error is received.
     * 3. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithIncorrectToken() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnauthorized(inputObjectToJson(INITIAL_INPUT));
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #2: testTriangleReceivingByIdWithIncorrectToken
     * 1. Create new triangle.
     * 2. Try to get created triangle by id with incorrect token (GET /triangle/<id>) and parse response into ErrorObject.
     * 3. Verify applicable error is received.
     */

    @Test
    public void testTriangleReceivingByIdWithIncorrectToken() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = getTriangleByIdUnauthorized(triangle.getId());
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
    }

    /**
     * Test #3: testTriangleDeletingByIdWithIncorrectToken
     * 1. Create new triangle.
     * 2. Try to delete triangle by id with incorrect token (DELETE /triangle/<id>) and parse response into ErrorObject.
     * 3. Verify applicable error is received.
     * 4. Verify triangle hasn't been deleted.
     */

    @Test
    public void testTriangleDeletingByIdWithIncorrectToken() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = deleteTriangleByIdUnauthorized(triangle.getId());
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
        getTriangleById(triangle.getId());
    }

    /**
     * Test #4: testGettingAllTrianglesWithIncorrectToken
     * 1. Try to get all triangles with incorrect token (GET /triangle/all) and parse response into ErrorObject.
     * 2. Verify applicable error is received.
     */

    @Test
    public void testGettingAllTrianglesWithIncorrectToken() {
        ErrorObject error = getAllTrianglesUnauthorized();
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
    }

    /**
     * Test #5: testTriangleAreaGettingByIdWithIncorrectToken
     * 1. Create new triangle.
     * 2. Try to get triangle's area by id with incorrect token (GET /triangle/<id>/area) and parse response into ErrorObject.
     * 3. Verify applicable error is received.
     */

    @Test
    public void testTriangleAreaGettingByIdWithIncorrectToken() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = getTriangleAreaUnauthorized(triangle.getId());
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
    }

    /**
     * Test #6: testTrianglePerimeterGettingByIdWithIncorrectToken
     * 1. Create new triangle.
     * 2. Try to get triangle's perimeter by id with incorrect token (GET /triangle/<id>/area) and parse response into ErrorObject.
     * 3. Verify applicable error is received.
     */

    @Test
    public void testTrianglePerimeterGettingByIdWithIncorrectToken() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = getTrianglePerimeterUnauthorized(triangle.getId());
        assertEquals(UNAUTHORIZED_ERROR, error.getError());
    }

    /**
     * Test #7: testTriangleCreationLimit
     * 1. Create 10 triangles (POST /triangle).
     * 2. Verify it is not possible to create more than 10 triangles.
     */

    @Test
    public void testTriangleCreationLimit() {
        while (getAllTriangles().length < 10)
            createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(INITIAL_INPUT));
        System.out.println(getAllTriangles().length);
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(UNPROCESSABLE_MESSAGE, error.getMessage());
    }

    /**
     * Test #: testTriangleCreationPositiveCase
     * 1. Create triangle (POST /triangle).
     * 2.
     */

    @Test
    public void testTriangleCreationPositiveCase() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        assertEquals(3.0, triangle.getFirstSide(), 0.0);
        assertEquals(4.0, triangle.getSecondSide(), 0.0);
        assertEquals(5.0, triangle.getThirdSide(), 0.0);
    }

    @After
    public void resetTestData() {
        TriangleObject[] list = getAllTriangles();
        for (TriangleObject triangle : list)
            deleteTriangleById(triangle.getId());
    }
}
