package com.kentvu.toolbox

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import org.opentest4j.TestAbortedException

class StepDefinitions {
    @Given("I have {int} cukes in my belly")
    fun I_have_cukes_in_my_belly(cukes: Int) {
        val belly = Belly()
        belly.eat(cukes)
    }

    @When("I wait {int} hour")
    fun i_wait_hour(int1: Int?) {
        // Write code here that turns the phrase above into concrete actions
        throw TestAbortedException("TODO")
    }
}