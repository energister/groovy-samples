package methods

import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.Specification

/**
 * Same as {@link MethodWithNamedArgumentsSpec}
 * but doesn't allow to add non-existing arguments to the method
 */
class DedicatedClassForMethodParametersSpec extends Specification {

    class TestMethodParameters {
        int first = 10
        int second = 20
    }

    String test(TestMethodParameters arguments) {
        "test(${arguments.first}, ${arguments.second})"
    }

    String test(Map arguments = [:]) {
        test(new TestMethodParameters(arguments))
    }

    def "May be invoked with no arguments (default values)"() {
        expect:
        test() == 'test(10, 20)'
    }

    def "May be invoked with positional parameters"() {
        expect:
        test(first: 300, second: 400) == 'test(300, 400)'
    }

    def "May be invoked with second default value"() {
        expect:
        test(first: 500) == 'test(500, 20)'
    }

    def "May be invoked with first default value"() {
        expect:
        test(second: 600) == 'test(10, 600)'
    }

    def "May not be invoked with the value of invalid type"() {
        when:
        test(first: "some string")

        then:
        GroovyCastException ex = thrown()
        ex.message == "Cannot cast object 'some string' with class 'java.lang.String' to class 'int'"
    }

    def "May be invoked with non-existent arguments"() {
        when:
        test(first: 500, nonExistingParameter: "value")

        then:
        MissingPropertyException ex = thrown()
        ex.message.startsWith("No such property: nonExistingParameter")
    }
}
