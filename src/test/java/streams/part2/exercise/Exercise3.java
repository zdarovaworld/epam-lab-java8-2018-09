package streams.part2.exercise;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"unused", "ConstantConditions"})
class Exercise3 {

    @Test
    void createLimitedStringWithOddNumbersSeparatedBySpaces() {
        int countNumbers = 10;

        String result = Stream.iterate(1, value -> value + 2)
                .limit(countNumbers)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        assertThat(result, is("1 3 5 7 9 11 13 15 17 19"));
    }

    @Test
    void extractEvenNumberedCharactersToNewString() {
        String source = "abcdefghijklm";

        String result = IntStream.range(0, source.length())
                .filter(i -> i % 2 == 0)
                .mapToObj(source::charAt)
                .map(Objects::toString)
                .collect(Collectors.joining());

        assertThat(result, is("acegikm"));
    }
}
