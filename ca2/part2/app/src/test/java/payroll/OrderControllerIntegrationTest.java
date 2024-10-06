package payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderModelAssembler assembler;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
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
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
