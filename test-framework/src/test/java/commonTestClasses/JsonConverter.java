package commonTestClasses;

import com.google.gson.Gson;
import commonTestClasses.jsonObjects.ErrorObject;
import commonTestClasses.jsonObjects.InputObject;
import commonTestClasses.jsonObjects.ResultObject;
import commonTestClasses.jsonObjects.TriangleObject;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonConverter {
    private final static String FILE_BASE_PATH = "src/test/resources/";

    public static String fileToString(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(FILE_BASE_PATH + filePath)));
        } catch (IOException e) {
            return null;
        }
    }

    public static TriangleObject[] jsonToTrianglesList(Response response) {
        return new Gson().fromJson(response.asString(), TriangleObject[].class);
    }

    public static TriangleObject jsonToTriangleObject(Response response) {
        return new Gson().fromJson(response.asString(), TriangleObject.class);
    }

    public static ResultObject jsonToResultObject(Response response) {
        return new Gson().fromJson(response.asString(), ResultObject.class);
    }

    public static ErrorObject jsonToErrorObject(Response response) {
        return new Gson().fromJson(response.asString(), ErrorObject.class);
    }

    public static String inputObjectToJson(String input) {
        return new Gson().toJson(new InputObject(input));
    }

    public static String inputObjectToJson(String separator, String input) {
        return new Gson().toJson(new InputObject(separator, input));
    }
}
