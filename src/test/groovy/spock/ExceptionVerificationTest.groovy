package spock

import spock.lang.Specification

class ExceptionVerificationTest extends Specification {

    def "no exceptions"() {
        when:
        int a = 5

        then:
        noExceptionThrown()
    }
}
