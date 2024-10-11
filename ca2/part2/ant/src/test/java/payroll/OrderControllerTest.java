package payroll;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) // Use SpringRunner for JUnit 4
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private OrderController mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderModelAssembler assembler;

    private Order order1;
    private Order order2;

    @Before
    public void setUp() {
        orderRepository.deleteAll();

        order1 = new Order();
        order1.setId(1L);
        order1.setStatus(Status.IN_PROGRESS);
        order1.setDescription("Order 1");

        order2 = new Order();
        order2.setId(2L);
        order2.setStatus(Status.COMPLETED);
        order2.setDescription("Order 2");

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        /**
        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
         */
    }
}
