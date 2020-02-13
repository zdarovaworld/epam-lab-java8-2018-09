package lambda.part3.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"unused", "ConstantConditions"})
class Exercise4 {

    private static class LazyCollectionHelper<T, R> {

        private final List<T> source;
        private final Function<T, List<R>> mapper;

        private LazyCollectionHelper(List<T> source, Function<T, List<R>> mapping) {
            this.source = source;
            this.mapper = mapping;
        }

        public static <T> LazyCollectionHelper<T, T> from(List<T> list) {
            return new LazyCollectionHelper<>(list, Collections::singletonList);
        }

        public <U> LazyCollectionHelper<T, U> flatMap(Function<R, List<U>> flatMapping) {
            return new LazyCollectionHelper<>(source, this.mapper.andThen(list -> listTransform(list, flatMapping)));
        }

        public <U> LazyCollectionHelper<T, U> flatMapWithCleverListTransform(Function<R, List<U>> flatMapping) {
            return new LazyCollectionHelper<>(source, cleverListTransform(flatMapping).compose(mapper));
        }

        public <U> LazyCollectionHelper<T, U> map(Function<R, U> mapping) {
            return new LazyCollectionHelper<>(source, mapper.andThen(list -> listTransform(list, x -> Collections.singletonList(mapping.apply(x)))));
        }

        public <U> LazyCollectionHelper<T, U> mapWithCleverListTransform(Function<R, U> mapping) {
            return new LazyCollectionHelper<>(source, cleverListTransform(mapping.andThen(Collections::singletonList)).compose(mapper));
        }

        public List<R> force() {
            List<R> result = new ArrayList<>();
            source.forEach(element -> result.addAll(mapper.apply(element)));
            return result;
        }

        private <U, N> List<N> listTransform(List<U> list, Function<U, List<N>> function) {
            List<N> result = new ArrayList<>();
            list.forEach(element -> result.addAll(function.apply(element)));
            return result;
        }

        private <U, V> Function<List<U>, List<V>> cleverListTransform(Function<U, List<V>> function) {
            return list -> {
                List<V> result = new ArrayList<>();
                list.forEach(function.andThen(result::addAll)::apply);
                return result;
            };
        }
    }

    @Test
    void mapEmployeesToCodesOfLetterTheirPositionsUsingLazyFlatMapHelper() {
        List<Employee> employees = getEmployees();

        List<Integer> codes = LazyCollectionHelper.from(employees)
                .flatMapWithCleverListTransform(Employee::getJobHistory)
                .mapWithCleverListTransform(JobHistoryEntry::getPosition)
                .flatMapWithCleverListTransform(Exercise4::calcCodes)
                .mapWithCleverListTransform(x -> x)
                .force();
        // TODO              LazyCollectionHelper.from(employees)
        // TODO                                  .flatMap(Employee -> JobHistoryEntry)
        // TODO                                  .map(JobHistoryEntry -> String(position))
        // TODO                                  .flatMap(String -> Character(letter))
        // TODO                                  .map(Character -> Integer(code letter)
        // TODO                                  .force();
        assertThat(codes, Matchers.contains(calcCodes("dev", "dev", "tester", "dev", "dev", "QA", "QA", "dev", "tester", "tester", "QA", "QA", "QA", "dev").toArray()));
    }

    private static List<Integer> calcCodes(String...strings) {
        List<Integer> codes = new ArrayList<>();
        for (String string : strings) {
            for (char letter : string.toCharArray()) {
                codes.add((int) letter);
            }
        }
        return codes;
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
