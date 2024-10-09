import org.junit.Test;
import static org.junit.Assert.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GWTTest {
    @Test
    public void testExample() {
        // Given
        int input = 5;
        int expected = 10;

        // When
        int result = someFunction(input);

        // Then
        assertEquals(expected, result);
    }

    // 테스트할 메소드
    private int someFunction(int input) {
        return input * 2; // 예시 함수
    }
}

public class StockOrderSteps {

    @Given("I have a valid account")
    public void i_have_a_valid_account() {
        // 계좌 유효성 검사 로직
    }

    @When("I place an order for {int} shares of stock at {double}")
    public void i_place_an_order_for_shares_of_stock(int quantity, double price) {
        // 주식 주문 로직
    }

    @Then("the order should be confirmed")
    public void the_order_should_be_confirmed() {
        // 주문 확인 로직
    }
}
