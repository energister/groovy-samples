import spock.lang.Specification

class MapConstructorsSpec extends Specification {

    class Person {
        String firstName
        String lastName = "defaultLastName"

        @Override
        String toString() {
            "$firstName $lastName"
        }
    }

    def "Can instantiate with the named arguments"() {
        expect:
        new Person(firstName:'Ada', lastName:'Lovelace').toString() == "Ada Lovelace"
    }

    def "Can skip some arguments (use default one)"() {
        expect:
        new Person(firstName:'Ada').toString() == "Ada defaultLastName"
    }

    def "Can't instantiate with the positional arguments"() {
        when:
        new Person('Ada', 'Lovelace')

        then:
        GroovyRuntimeException ex = thrown()
        ex.message.contains("Could not find matching constructor")
    }


    def "Can't instantiate with additional non-existent properties"() {
        when:
        new Person(firstName:'Ada', lastName:'Lovelace', age: 18)

        then:
        MissingPropertyException ex = thrown()
        ex.message == "No such property: age for class: MapConstructorsSpec"
    }
}
