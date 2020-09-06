package commonTestClasses;

import com.google.gson.Gson;
import commonTestClasses.jsonObjects.ErrorObject;
import commonTestClasses.jsonObjects.InputObject;
import commonTestClasses.jsonObjects.ResultObject;
import commonTestClasses.jsonObjects.TriangleObject;
import io.restassured.response.Response;

public class JsonConverter {
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
