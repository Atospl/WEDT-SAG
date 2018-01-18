package enums;

public enum Site {

    BBC("BBC"),
    CNN("CNN"),
    WASHINGTONTIMES("WASHINGTONTIMES"),
    REUTERS("REUTERS"),
    GUARDIAN("GUARDIAN"),
    NYTIMES("NYTIMES");

    public String name;

    Site(String name) {
        this.name = name;
    }

}
