package streams.part2.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import lambda.data.PersonFirstPositionPair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SuppressWarnings({"ConstantConditions", "unused"})
class Exercise1 {

    @Test
    void calcTotalYearsSpentInEpam() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Long hours = employees.stream()
                .map(Employee::getJobHistory)
                .flatMap(Collection::stream)
                .filter(job -> "EPAM".equals(job.getEmployer()))
                .mapToLong(JobHistoryEntry::getDuration)
                .sum();

        assertThat(hours, is(19L));
    }

    @Test
    void findPersonsWithQaExperience() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Set<Person> workedAsQa = employees.stream()
                .filter(emp -> emp.getJobHistory()
                        .stream()
                        .map(JobHistoryEntry::getPosition)
                        .anyMatch("QA"::equals))
                .map(Employee::getPerson)
                .collect(toSet());


        assertThat(workedAsQa, containsInAnyOrder(
                employees.get(2).getPerson(),
                employees.get(4).getPerson(),
                employees.get(5).getPerson()
        ));
    }

    @Test
    void composeFullNamesOfEmployeesUsingLineSeparatorAsDelimiter() {
        List<Employee> employees = getEmployees();
        System.out.println("".length());

        // TODO реализация
        String result = employees.stream()
                .map(Employee::getPerson)
                .map(Person::getFullName)
                .collect(joining(System.lineSeparator()));

        assertThat(result, is(
                "Иван Мельников\n"
                        + "Александр Дементьев\n"
                        + "Дмитрий Осинов\n"
                        + "Анна Светличная\n"
                        + "Игорь Толмачёв\n"
                        + "Иван Александров"));
    }

    @Test
    @SuppressWarnings("Duplicates")
    void groupPersonsByFirstPositionUsingToMap() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        //my solution is not very optimal
        Map<String, Set<Person>> result = null; //employees.stream()
//                .map(employee -> new PersonFirstPositionPair(employee.getPerson(), employee.getJobHistory().get(0).getPosition()))
//                .collect(toMap(PersonFirstPositionPair::getFirstPosition,
//                        Exercise1::convertToSet,
//                        Exercise1::addToSet));

        //better solution:
        result = employees.stream()
                .collect(toMap(employee -> employee.getJobHistory().get(0).getPosition(),
                        employee -> new HashSet<>(Collections.singletonList(employee.getPerson())),
                        (left, right) -> {
                            left.addAll(right);
                            return left;
                        }));


        assertThat(result, hasEntry(is("dev"), contains(employees.get(0).getPerson())));
        assertThat(result, hasEntry(is("QA"), containsInAnyOrder(employees.get(2).getPerson(), employees.get(5).getPerson())));
        assertThat(result, hasEntry(is("tester"), containsInAnyOrder(employees.get(1).getPerson(), employees.get(3).getPerson(), employees.get(4).getPerson())));
    }

    private static Set<Person> convertToSet(PersonFirstPositionPair pair) {
        Set<Person> set = new HashSet<>();
        set.add(pair.getPerson());
        return set;
    }

    private static Set<Person> addToSet(Set<Person> left, Set<Person> right) {
        left.addAll(right);
        return left;
    }

    @Test
    @SuppressWarnings("Duplicates")
    void groupPersonsByFirstPositionUsingGroupingByCollector() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Map<String, Set<Person>> result = employees.stream()
                .collect(groupingBy(emp -> emp.getJobHistory().get(0).getPosition(),
                        mapping(Employee::getPerson, toSet())));

        assertThat(result, hasEntry(is("dev"), contains(employees.get(0).getPerson())));
        assertThat(result, hasEntry(is("QA"), containsInAnyOrder(employees.get(2).getPerson(), employees.get(5).getPerson())));
        assertThat(result, hasEntry(is("tester"), containsInAnyOrder(employees.get(1).getPerson(), employees.get(3).getPerson(), employees.get(4).getPerson())));
    }

    private static List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("Иван", "Мельников", 30),
                        Arrays.asList(
                                new JobHistoryEntry(2, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Александр", "Дементьев", 28),
                        Arrays.asList(
                                new JobHistoryEntry(1, "tester", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Дмитрий", "Осинов", 40),
                        Arrays.asList(
                                new JobHistoryEntry(3, "QA", "yandex"),
                                new JobHistoryEntry(1, "QA", "mail.ru"),
                                new JobHistoryEntry(1, "dev", "mail.ru")
                        )),
                new Employee(
                        new Person("Анна", "Светличная", 21),
                        Collections.singletonList(
                                new JobHistoryEntry(1, "tester", "T-Systems")
                        )),
                new Employee(
                        new Person("Игорь", "Толмачёв", 50),
                        Arrays.asList(
                                new JobHistoryEntry(5, "tester", "EPAM"),
                                new JobHistoryEntry(6, "QA", "EPAM")
                        )),
                new Employee(
                        new Person("Иван", "Александров", 33),
                        Arrays.asList(
                                new JobHistoryEntry(2, "QA", "T-Systems"),
                                new JobHistoryEntry(3, "QA", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM")
                        ))
        );
    }
}
