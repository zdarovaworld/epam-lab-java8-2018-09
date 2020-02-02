package lambda.part1.exercise;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import lambda.data.Person;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class Exercise1 {

    @Test
    void sortPersonsByAgeUsingArraysSortLocalComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort

        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                return Integer.compare(person.getAge(), t1.getAge());
            }
        };

        Arrays.sort(persons, comparator);

        assertThat(persons, is(arrayContaining(
                new Person("Иван", "Мельников", 20),
                new Person("Николай", "Зимов", 30),
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45)
        )));
    }

    @Test
    void sortPersonsByAgeUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort


        Arrays.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                return Integer.compare(person.getAge(), t1.getAge());

            }
        });

        assertThat(persons, is(arrayContaining(
                new Person("Иван", "Мельников", 20),
                new Person("Николай", "Зимов", 30),
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45)
        )));
    }

    @Test
    void sortPersonsByLastNameThenFirstNameUsingArraysSortAnonymousComparator() {
        Person[] persons = getPersons();

        // TODO use Arrays.sort

        Arrays.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person t1) {
                int sortByLastName = person.getLastName().compareTo(t1.getLastName());
                return sortByLastName == 0 ? person.getFirstName().compareTo(t1.getFirstName()) : sortByLastName;
            }
        });

        assertThat(persons, is(arrayContaining(
                new Person("Алексей", "Доренко", 40),
                new Person("Артем", "Зимов", 45),
                new Person("Николай", "Зимов", 30),
                new Person("Иван", "Мельников", 20)
        )));
    }

    @Test
    void findFirstWithAge30UsingGuavaPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        // TODO use FluentIterable
        Person person = null;

        Predicate<Person> isFirstWithAge30 = new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                return 30 == input.getAge();
            }
        };
        Optional<Person> personOptional = FluentIterable.from(persons).firstMatch(isFirstWithAge30);
        person = personOptional.get();

        assertThat(person, is(new Person("Николай", "Зимов", 30)));
    }

    @Test
    void findFirstWithAge30UsingGuavaAnonymousPredicate() {
        List<Person> persons = Arrays.asList(getPersons());

        // TODO use FluentIterable
        Person person = null;
        Optional<Person> personOptional = FluentIterable.from(persons).firstMatch(new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                return 30 == input.getAge();
            }
        });
        person = personOptional.get();


        assertThat(person, is(new Person("Николай", "Зимов", 30)));
    }

    private Person[] getPersons() {
        return new Person[]{
                new Person("Иван", "Мельников", 20),
                new Person("Алексей", "Доренко", 40),
                new Person("Николай", "Зимов", 30),
                new Person("Артем", "Зимов", 45)
        };
    }
}
