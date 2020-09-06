package testSuits;

import commonTestClasses.jsonObjects.ErrorObject;
import commonTestClasses.jsonObjects.ResultObject;
import commonTestClasses.jsonObjects.TriangleObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static commonTestClasses.JsonConverter.inputObjectToJson;
import static commonTestClasses.RestApi.*;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

public class TrianglesTests {
    public static String authToken;
    private final static String INITIAL_INPUT = "3;4;5";
    private final static String ZERO_INPUT = "3;0;5";
    private final static String NEGATIVE_INPUT = "-3;-4;-5";
    private final static String TWO_SIDES_INPUT = "3;4;";
    private final static String FOUR_SIDES_INPUT = "3;4;5;6";
    private final static String TOO_BIG_SIDE_INPUT = "3;4;10";
    private final static String FLOAT_INPUT = "3.7;4.3;5.2";
    private final static String SIDE_EQUALS_OTHERS_SUM_INPUT = "3;6;3";
    private final static String EMPTY_SEPARATOR_INPUT = "345.1";
    private final static String DOT_SEPARATOR_INPUT = "3.4.5";
    private final static String SLASH_SEPARATOR_INPUT = "8/10/12";
    private final static String INCORRECT_INPUT = "3;$;5";
    private final static String SLASH_SEPARATOR = "/";
    private final static String SEMICOLON_SEPARATOR = ";";
    private final static String EMPTY_SEPARATOR = "";
    private final static String DOT_SEPARATOR = ".";
    private final static String UNAUTHORIZED_ERROR = "Unauthorized";
    private final static String UNPROCESSABLE_ERROR = "Unprocessable Entity";
    private final static String NOT_FOUND_ERROR = "Not Found";
    private final static String UNPROCESSABLE_MESSAGE = "Limit exceeded";

    @BeforeClass
    public static void configureRestAssured() {
        authToken = System.getProperty("TOKEN");
        baseURI = System.getProperty("HOST");
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

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/1
    @Test
    @Ignore
    public void testTriangleCreationLimit() {
        while (getAllTriangles().length < 10)
            createTriangle(inputObjectToJson(INITIAL_INPUT));
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(INITIAL_INPUT));
        System.out.println(getAllTriangles().length);
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(UNPROCESSABLE_MESSAGE, error.getMessage());
    }

    /**
     * Test #8: testTriangleCreationWithoutSeparatorPositiveCase
     * 1. Create triangle (POST /triangle) using request without separator attribute.
     * 2. Verify triangle has been successfully created.
     */

    @Test
    public void testTriangleCreationWithoutSeparatorPositiveCase() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.0f, triangle.getThirdSide(), 0.0);
        triangle = getTriangleById(triangle.getId());
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.0f, triangle.getThirdSide(), 0.0);
    }

    /**
     * Test #9: testTriangleCreationWithoutSeparatorNegativeCase
     * 1. Create triangle (POST /triangle) with not semicolon separator using request without separator attribute.
     * 2. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithoutSeparatorNegativeCase() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(SLASH_SEPARATOR_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #10: testTriangleCreationWithSeparatorPositiveCase
     * 1. Create triangle (POST /triangle) using request with separator attribute.
     * 2. Verify triangle has been successfully created.
     */

    @Test
    public void testTriangleCreationWithSeparatorPositiveCase() {
        TriangleObject triangle = createTriangle(inputObjectToJson(SLASH_SEPARATOR, SLASH_SEPARATOR_INPUT));
        assertEquals(8.0f, triangle.getFirstSide(), 0.0);
        assertEquals(10.0f, triangle.getSecondSide(), 0.0);
        assertEquals(12.0f, triangle.getThirdSide(), 0.0);
        triangle = getTriangleById(triangle.getId());
        assertEquals(8.0f, triangle.getFirstSide(), 0.0);
        assertEquals(10.0f, triangle.getSecondSide(), 0.0);
        assertEquals(12.0f, triangle.getThirdSide(), 0.0);
    }

    /**
     * Test #11: testTriangleCreationWithSeparatorNegativeCase
     * 1. Create triangle (POST /triangle) with semicolon separator using request with slash separator attribute.
     * 2. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithSeparatorNegativeCase() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(SLASH_SEPARATOR, INITIAL_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #12: testTriangleCreationWithTwoSides
     * 1. Create triangle (POST /triangle) with only two sides.
     * 2. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithTwoSides() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(SEMICOLON_SEPARATOR, TWO_SIDES_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #13: testTriangleCreationWithFourSides
     * 1. Create triangle (POST /triangle) with four sides.
     * 2. Verify triangle hasn't been created.
     */

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/3
    @Ignore
    @Test
    public void testTriangleCreationWithFourSides() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(SEMICOLON_SEPARATOR, FOUR_SIDES_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #14: testTriangleCreationWithEmptySeparator
     * 1. Create triangle (POST /triangle) using request with empty separator.
     * 2. Verify triangle has been successfully created.
     */

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/2
    @Ignore
    @Test
    public void testTriangleCreationWithEmptySeparator() {
        TriangleObject triangle = createTriangle(inputObjectToJson(EMPTY_SEPARATOR, EMPTY_SEPARATOR_INPUT));
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.1f, triangle.getThirdSide(), 0.0);
        triangle = getTriangleById(triangle.getId());
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.1f, triangle.getThirdSide(), 0.0);
    }

    /**
     * Test #15: testTriangleCreationWithDotSeparator
     * 1. Create triangle (POST /triangle) using request with dot separator.
     * 2. Verify triangle has been successfully created.
     */

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/4
    @Ignore
    @Test
    public void testTriangleCreationWithDotSeparator() {
        TriangleObject triangle = createTriangle(inputObjectToJson(DOT_SEPARATOR, DOT_SEPARATOR_INPUT));
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.0f, triangle.getThirdSide(), 0.0);
        triangle = getTriangleById(triangle.getId());
        assertEquals(3.0f, triangle.getFirstSide(), 0.0);
        assertEquals(4.0f, triangle.getSecondSide(), 0.0);
        assertEquals(5.0f, triangle.getThirdSide(), 0.0);
    }

    /**
     * Test #16: testTriangleDeletion
     * 1. Create triangle (POST /triangle).
     * 2. Delete created triangle (DELETE /triangle/<id>).
     * 2. Verify triangle has been successfully deleted.
     */

    @Test
    public void testTriangleDeletion() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        triangle = getTriangleById(triangle.getId());
        deleteTriangleById(triangle.getId());
        ErrorObject error = getTriangleByIdNotFound(triangle.getId());
        assertEquals(NOT_FOUND_ERROR, error.getError());
    }

    /**
     * Test #17: testTriangleCreationWithNegativeParameters
     * 1. Create triangle (POST /triangle) with negative parameters.
     * 2. Verify triangle hasn't been created.
     */

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/5
    @Ignore
    @Test
    public void testTriangleCreationWithNegativeParameters() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(NEGATIVE_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #18: testTriangleCreationWithZeroParameter
     * 1. Create triangle (POST /triangle) with zero parameter.
     * 2. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithZeroParameter() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(ZERO_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #19: testTriangleCreationWithOneSideBiggerThanBothOther
     * 1. Create triangle (POST /triangle) with incorrect values (one side bigger than both other).
     * 2. Verify triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithOneSideBiggerThanBothOther() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(TOO_BIG_SIDE_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #20: testTriangleCreationWithOneSideEqualsBothOther
     * 1. Create triangle (POST /triangle) with incorrect values (one side equals both other sum).
     * 2. Verify triangle hasn't been created.
     */

    // TODO: Delete @Ignore annotation after this bug will be fixed: https://github.com/aianushevich/hometask/issues/6
    @Ignore
    @Test
    public void testTriangleCreationWithOneSideEqualsBothOther() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(SIDE_EQUALS_OTHERS_SUM_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    /**
     * Test #21: testTriangleCreationWithOneSideEqualsBothOther
     * 1. Create triangle (POST /triangle).
     * 2. Calculate perimeter and verify the result is correct.
     */

    @Test
    public void testTrianglePerimeterCalculation() {
        TriangleObject triangle = createTriangle(inputObjectToJson(FLOAT_INPUT));
        ResultObject perimeter = getTrianglePerimeter(triangle.getId());
        assertEquals(13.2f, perimeter.getResult(), 0.0);
    }

    /**
     * Test #22: testTriangleCreationWithOneSideEqualsBothOther
     * 1. Create triangle (POST /triangle).
     * 2. Calculate area and verify the result is correct.
     */

    @Test
    public void testTriangleAreaCalculation() {
        TriangleObject triangle = createTriangle(inputObjectToJson(INITIAL_INPUT));
        ResultObject perimeter = getTriangleArea(triangle.getId());
        assertEquals(6.0f, perimeter.getResult(), 0.0);
    }

    /**
     * Test #23: testTriangleCreationWithIncorrectInput
     * 1. Create triangle (POST /triangle) with incorrect symbols in input.
     * 2. Verify the triangle hasn't been created.
     */

    @Test
    public void testTriangleCreationWithIncorrectInput() {
        int trianglesNumber = getAllTriangles().length;
        ErrorObject error = createTriangleUnprocessable(inputObjectToJson(INCORRECT_INPUT));
        assertEquals(UNPROCESSABLE_ERROR, error.getError());
        assertEquals(trianglesNumber, getAllTriangles().length);
    }

    @After
    public void resetTestData() {
        TriangleObject[] list = getAllTriangles();
        for (TriangleObject triangle : list)
            deleteTriangleById(triangle.getId());
    }
}
