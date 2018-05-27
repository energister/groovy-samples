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

    String test(String required, TestMethodOptionalParameters optionalArguments) {
        "test('$required', ${optionalArguments.first}, ${optionalArguments.second})"
    }

    String test(String required, Map optionalArguments = [:]) {
        test(required, new TestMethodOptionalParameters(optionalArguments))
    }

    def "May be invoked with no arguments (default values)"() {
        expect:
        test('required') == "test('required', 10, 20)"
    }

    def "May not be invoked with positional parameters"() {
        when:
        test('required', first: 300, second: 400)

        then:
        MissingMethodException ex = thrown()

        // unexpected behaviour
        ex.message.startsWith("No signature of method: " +
                                      "methods.DedicatedClassForOptionalMethodParametersSpec.test() " +
                                      "is applicable for argument types: " +
                                      "(java.util.LinkedHashMap, java.lang.String) " +
                                      "values: [[first:300, second:400], required]")
        // Groovy changes order of the arguments in invocation!
    }

    def "May be invoked with map as an optional parameters"() {
        expect:
        test('required', [first: 300, second: 400]) == "test('required', 300, 400)"
    }

    def "May be invoked with second default value"() {
        expect:
        test('required', [first: 500]) == "test('required', 500, 20)"
    }

    def "May be invoked with first default value"() {
        expect:
        test('required', [second: 600]) == "test('required', 10, 600)"
    }

    def "May not be invoked with the value of invalid type"() {
        when:
        test('required', [first: "some string"])

        then:
        GroovyCastException ex = thrown()
        ex.message == "Cannot cast object 'some string' with class 'java.lang.String' to class 'int'"
    }

    def "May be invoked with non-existent arguments"() {
        when:
        test('required', [first: 500, nonExistingParameter: "value"])

        then:
        MissingPropertyException ex = thrown()
        ex.message.startsWith("No such property: nonExistingParameter")
    }
}
