package api.exercise;

import lambda.data.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@SuppressWarnings({"ConstantConditions", "unused", "MismatchedQueryAndUpdateOfCollection"})
class Exercise1 {

    enum Status {
        UNKNOWN,
        PENDING,
        COMMENTED,
        ACCEPTED,
        DECLINED
    }

    @Test
    void acceptGreaterThan21OthersDecline() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);
        candidates.put(helen, Status.PENDING);

        // TODO implementation
        // my solution
        candidates.forEach((person, status) ->
                candidates.computeIfPresent(person, (key, value) -> key.getAge() > 21 ? Status.ACCEPTED : Status.DECLINED));

        // better solution:
        // candidates.replaceAll(((person, status) -> person.getAge() > 21 ? Status.ACCEPTED : Status.DECLINED));

        assertThat(candidates, Matchers.hasEntry(ivan, Status.ACCEPTED));
        assertThat(candidates, Matchers.hasEntry(helen, Status.ACCEPTED));
        assertThat(candidates, Matchers.hasEntry(alex, Status.DECLINED));
    }

    @Test
    void acceptGreaterThan21OthersRemove() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);
        candidates.put(helen, Status.PENDING);

        candidates.put(new Person("a", "a", 19), Status.PENDING);
        candidates.put(new Person("b", "c", 16), Status.PENDING);
        candidates.put(new Person("b", "c", 5), Status.PENDING);

        // TODO implementation

        candidates.keySet().removeIf(person -> person.getAge() < 21);
        candidates.replaceAll(((person, status) -> Status.ACCEPTED));

        assertThat(candidates, Matchers.hasEntry(ivan, Status.ACCEPTED));
        assertThat(candidates, Matchers.hasEntry(helen, Status.ACCEPTED));
        assertThat(candidates, not(hasKey(alex)));
    }

    @Test
    void getStatus() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Map<Person, Status> candidates = new HashMap<>();
        candidates.put(alex, Status.PENDING);
        candidates.put(ivan, Status.PENDING);

        // TODO implementation

        Status alexStatus = candidates.getOrDefault(alex, Status.UNKNOWN);
        Status ivanStatus = candidates.getOrDefault(ivan, Status.UNKNOWN);
        Status helenStatus = candidates.getOrDefault(helen, Status.UNKNOWN);

        assertThat(alexStatus, is(Status.PENDING));
        assertThat(ivanStatus, is(Status.PENDING));
        assertThat(helenStatus, is(Status.UNKNOWN));
    }

    @Test
    void putToNewValuesIfNotExists() {
        Person alex = new Person("Алексей", "Мельников", 20);
        Person ivan = new Person("Иван", "Стрельцов", 24);
        Person helen = new Person("Елена", "Рощина", 22);
        Person dmitry = new Person("Дмитрий", "Егоров", 30);
        Map<Person, Status> oldValues = new HashMap<>();
        oldValues.put(alex, Status.PENDING);
        oldValues.put(dmitry, Status.DECLINED);
        oldValues.put(ivan, Status.ACCEPTED);

        Map<Person, Status> newValues = new HashMap<>();
        newValues.put(alex, Status.DECLINED);
        newValues.put(helen, Status.PENDING);

        // TODO implementation

        oldValues.forEach((newValues::putIfAbsent));

        assertThat(newValues, hasEntry(alex, Status.DECLINED));
        assertThat(newValues, hasEntry(ivan, Status.ACCEPTED));
        assertThat(newValues, hasEntry(helen, Status.PENDING));
    }
}
