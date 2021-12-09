package Extra;

/**
 * Object that represents a person, hold their name and username used to a login to rgistered account
 */
public class Names {
    /**
     * variable to store the persons name
     */
    private String name;

    /**
     * variable to store the persons account username
     */
    private String userName;

    /**
     * Constructors, take in their name as a parameter
     * @param name persons name
     */
    public Names(String name, String username) {
        this.name = name;
        this.userName = username;
    }

    /**
     * Returns the persons name stored in their respective object
     * @return persons name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the persons name to a specified value
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the persons account username
     * @return account username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the persons username to a new specified value
     * @param userName new username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
