package spock

import spock.lang.Specification

class ExceptionVerificationSpec extends Specification {

    def "no exceptions"() {
        when:
        int a = 5

        then:
        noExceptionThrown()
    }
}
