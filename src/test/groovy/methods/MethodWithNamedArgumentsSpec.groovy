package methods

import org.codehaus.groovy.runtime.typehandling.GroovyCastException
import spock.lang.Specification

/**
 * See http://docs.groovy-lang.org/latest/html/documentation/#_named_arguments
 */
class MethodWithNamedArgumentsSpec extends Specification {

    String test(Map arguments = [:]) {
        int first = arguments.get('first', 10)
        int second = arguments.get('second', 20)

        "test($first, $second)"
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
        noExceptionThrown()  // actually this is undesired behaviour of the named arguments
    }
}
