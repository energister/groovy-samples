class VariableInjectingClass {
    String someVariable = 'some variable to be "injected" into closure'

    void methodWithCallback(Closure callback) {
        // More details at https://dzone.com/articles/groovy-closures-owner-delegate
        callback.setDelegate(this)
        callback.call()

        // Another example at https://github.com/jgritman/httpbuilder/wiki/POST-Examples#posting-with-the-request-method
    }
}

class ClosuresVariablesInjectionDemo {
    static void main(String[] args) {
        new VariableInjectingClass().methodWithCallback() {
            // Note that closer have access to someVariable
            // which hasn't been defined within this class or as a parameter
            println someVariable  // will print content of the VariableInjectingClass.someVariable
        }
    }
}

