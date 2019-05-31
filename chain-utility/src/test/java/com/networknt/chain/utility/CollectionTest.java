package com.networknt.chain.utility;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.networknt.chain.utility.Collection.EMPTY_STRING_ARRAY;
import static com.networknt.chain.utility.Collection.tail;
import static com.networknt.chain.utility.Collection.create;
import static com.networknt.chain.utility.Collection.join;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CollectionTest {

    @Test
    public void testTail() {
        assertThat(tail(EMPTY_STRING_ARRAY), is(EMPTY_STRING_ARRAY));
        assertThat(tail(create("a", "b", "c")), is(create("b", "c")));
        assertThat(tail(create("a")), is(EMPTY_STRING_ARRAY));
    }

    @Test
    public void testCreate() {
        assertThat(create("a"), is(new String[] { "a" }));
        assertThat(create(""), is(new String[] { "" }));
        assertThat(create("a", "b"), is(new String[] { "a", "b" }));
    }

    @Test
    public void testJoin() {
        assertThat(join(Arrays.asList("a  ", "b ", " c "), ","), is("a,b,c"));
        assertThat(join(Arrays.asList("a", "b", "c", "d"), ","), is("a,b,c,d"));
        assertThat(join(Arrays.asList("a  ", "b ", " c "), ", "), is("a, b, c"));
        assertThat(join(Arrays.asList("a", "b", "c", "d"), ", "), is("a, b, c, d"));
    }

    @Test
    public void testJoinWithFunction() {
        final List<FakeSpec> specs1 = Arrays.asList(
                new FakeSpec("a"),
                new FakeSpec("b"),
                new FakeSpec("c"));
        assertThat(join(specs1, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs2 = Arrays.asList(
                new FakeSpec("a"),
                new FakeSpec("b"),
                new FakeSpec("c"));
        assertThat(join(specs2, ", ", FakeSpec::getName), is("a, b, c"));

        final List<FakeSpec> specs3 = Arrays.asList(
                new FakeSpec(" a"),
                new FakeSpec("b  "),
                new FakeSpec(" c "));
        assertThat(join(specs3, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs4 = Arrays.asList(
                new FakeSpec(" a"),
                new FakeSpec("b  "),
                new FakeSpec(" c "));
        assertThat(join(specs4, ", ", FakeSpec::getName), is("a, b, c"));
    }

    /**
     * Fake object to test
     */
    private final class FakeSpec {
        private final String name;

        private FakeSpec(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
