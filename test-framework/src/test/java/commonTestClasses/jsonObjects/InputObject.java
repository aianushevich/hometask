package commonTestClasses.jsonObjects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InputObject {
    private String separator;
    private String input;

    public InputObject(String input) {
        this.input = input;
    }
}
