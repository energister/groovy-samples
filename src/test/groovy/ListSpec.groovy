import groovy.transform.TypeChecked
import spock.lang.Specification

class ListSpec extends Specification {

    def 'plus method creates a new list'() {
        List<Number> initialList = [3, 1, 4]

        when:
        List newList = initialList + [3, 5]

        then:
        newList.size() == initialList.size() + 2

        // initialList isn't affected
        initialList.size() == old(initialList.size())
    }

    def 'left-shift does modify the original list'() {
        List<Number> list = [3, 1, 4]

        when:
        list << 3

        then:
        list.size() == old(list.size()) + 1
        list == [3, 1, 4, 3]
    }

    // @TypeChecked
    def "Groovy doesn't perform type check on adding items to list"() {
        given:
        List<Integer> numbers = [3, 1, 4]

        when:
        numbers << 'abc'

        then:
        noExceptionThrown()
        numbers[-1] == 'abc'
    }

    //    @TypeChecked
    def "add items to list"() {
        List<Integer> initialList = [3, 1, 4]

        when:
        def newList = initialList << [7, 6]  // ! Don't do this !

        then:
        // You'll get 4 instead of 5
        newList.size() != 3 + 2
        // because you add a list to the initialList, not the two individual values
        newList.size() == 3 + 1
        newList == [3, 1, 4, [7, 6]]
        // This is because Groovy doesn't perform type check while adding values to a list
        // so it is possible to add a List<> to the List<Integer>
    }
}
