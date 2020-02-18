package streams.part1.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"ConstantConditions", "unused"})
class Exercise2 {

    @Test
    void calcAverageAgeOfEmployees() {
        List<Employee> employees = getEmployees();

        Double expected = employees.stream()
                .map(Employee::getPerson)
                .mapToInt(Person::getAge)
                .average()
                .orElse(-1);

        assertThat(expected, Matchers.closeTo(33.66, 0.1));
    }

    @Test
    void findPersonWithLongestFullName() {
        List<Employee> employees = getEmployees();

        Person expected = employees.stream()
                .map(Employee::getPerson)
                .max(Comparator.comparingInt(person -> person.getFullName().length()))
                .get();

        assertThat(expected, Matchers.is(employees.get(1).getPerson()));
    }

    @Test
    void findEmployeeWithMaximumDurationAtOnePosition() {
        List<Employee> employees = getEmployees();

        Employee expected = employees.stream()
                .max(Comparator.comparingInt(emp -> emp.getJobHistory()
                        .stream()
                        .mapToInt(JobHistoryEntry::getDuration)
                        .max()
                        .orElse(-1)))
                .get();

        assertThat(expected, Matchers.is(employees.get(4)));
    }

    /**
     * Вычислить общую сумму заработной платы для сотрудников.
     * Базовая ставка каждого сотрудника составляет 75_000.
     * Если на текущей позиции (последняя в списке) он работает больше трех лет - ставка увеличивается на 20%
     */
    @Test
    void calcTotalSalaryWithCoefficientWorkExperience() {
        List<Employee> employees = getEmployees();

        //my solution:
        Double expected = employees.stream()
                .map(Employee::getJobHistory)
                .flatMap(jobHistoryEntries -> jobHistoryEntries.stream().skip(jobHistoryEntries.size() - 1))
                .mapToDouble(JobHistoryEntry::getDuration)
                .reduce(0, (sum, duration) -> {
                    if (duration > 3) {
                        return sum + 75_000 * 1.2;
                    } else {
                        return sum + 75_000;
                    }
                });

        /*
        smarter solution:

        Double expected = employees.stream()
                .map(Employee::getJobHistory)
                .map(positions -> positions.get(positions.size() - 1))
                .map(JobHistoryEntry::getDuration)
                .map(d -> d > 3 ? 75000d * 1.2 : 75000)
                .mapToDouble(Double::new)
                .sum();
         */

        System.err.println(expected);

        double sum = 0;
        int salary = 75_000;
        double coeff = 0.2;

        for (Employee employee : employees) {
            List<JobHistoryEntry> jobHistory = employee.getJobHistory();
            if (jobHistory.get(jobHistory.size() - 1).getDuration() > 3) {
                sum += salary + salary * coeff;
            } else {
                sum += salary;
            }
        }

        System.out.println(sum);

        assertThat(expected, Matchers.closeTo(465000.0, 0.001));
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