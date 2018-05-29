package methods

import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.Specification

/**
 * Same as {@link MethodWithNamedArgumentsSpec}
 * but doesn't allow to add non-existing arguments to the method
 */
class DedicatedClassForOptionalMethodParametersSpec extends Specification {

    class TestMethodOptionalParameters {
        int first = 10
        int second = 20
    }

    // for some reason Groovy requires a map
    // which comprise named arguments (http://docs.groovy-lang.org/latest/html/documentation/#_named_arguments)
    // to be defined as a first parameter
    // regardless of where such arguments are positioned in the method invocation
    String test(Map optionalArguments = [:], String required) {
        doTest(required, new TestMethodOptionalParameters(optionalArguments))
    }

    /**
     * Hide internal implementation (with the TestMethodOptionalParameters)
     * by marking method as private.
     */
    private String doTest(String required, TestMethodOptionalParameters optionalArguments) {
        "test('$required', ${optionalArguments.first}, ${optionalArguments.second})"
    }

    def "May be invoked with no arguments (default values)"() {
        expect:
        test('required') == "test('required', 10, 20)"
    }

    def "May be invoked with all optional parameters"() {
        expect:
        test('required', first: 300, second: 400) == "test('required', 300, 400)"
    }

    def "May be invoked with default second value"() {
        expect:
        test('required', first: 500) == "test('required', 500, 20)"
    }

    def "May be invoked with default first value"() {
        expect:
        test('required', second: 600) == "test('required', 10, 600)"
    }

    def "May not be invoked with the value of invalid type"() {
        when:
        test('required', first: "some string")

        then:
        GroovyCastException ex = thrown()
        ex.message == "Cannot cast object 'some string' with class 'java.lang.String' to class 'int'"
    }

    def "May not be invoked with non-existent parameters"() {
        when:
        test('required', first: 500, nonExistingParameter: "value")

        then:
        MissingPropertyException ex = thrown()
        ex.message.startsWith("No such property: nonExistingParameter")
    }
}
