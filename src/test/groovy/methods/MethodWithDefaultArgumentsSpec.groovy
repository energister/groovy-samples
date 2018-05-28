package methods

import spock.lang.Specification

/**
 * See http://docs.groovy-lang.org/latest/html/documentation/#_default_arguments
 */
class MethodWithDefaultArgumentsSpec extends Specification {

    String test(final int first = 10, final int second = 20) {
        "test($first, $second)"
    }

    def "May be invoked with no arguments (default values)"() {
        expect:
        test() == 'test(10, 20)'
    }

    def "May be invoked with positional parameters"() {
        expect:
        test(300, 400) == 'test(300, 400)'
    }

    def "May be invoked with second default value"() {
        expect:
        test(500) == 'test(500, 20)'
    }

    def "Unable to invoke with first default value (passing Map as an argument)"() {
        when:
        test(second: 500)

        then:
        MissingMethodException ex = thrown()
        ex.message.startsWith("No signature of method")
        ex.message.contains("is applicable for argument types: (java.util.LinkedHashMap)")
    }
}
