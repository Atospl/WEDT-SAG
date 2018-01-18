package enums;

public enum Site {

    BBC("BBC"),
    CNN("CNN"),
    WASHINGTONTIMES("WASHINGTONTIMES"),
    NYTIMES("NYTIMES");

    public String name;

    Site(String name) {
        this.name = name;
    }

}
