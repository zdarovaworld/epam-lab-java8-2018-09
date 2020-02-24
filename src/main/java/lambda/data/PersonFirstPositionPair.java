package lambda.data;

public class PersonFirstPositionPair {
    private Person person;
    private String firstPosition;

    public PersonFirstPositionPair(Person person, String firstPosition) {
        this.person = person;
        this.firstPosition = firstPosition;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getFirstPosition() {
        return firstPosition;
    }

    public void setFirstPosition(String firstPosition) {
        this.firstPosition = firstPosition;
    }
}
