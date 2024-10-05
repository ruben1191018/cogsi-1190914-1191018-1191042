package payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderModelAssembler assembler;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        // Initialize sample Order objects
        order1 = new Order();
        order1.setId(1L);
        order1.setStatus(Status.IN_PROGRESS);
        order1.setDescription("Order 1");

        order2 = new Order();
        order2.setId(2L);
        order2.setStatus(Status.COMPLETED);
        order2.setDescription("Order 2");
    }

    @Test
    void testGetAllOrders() throws Exception {
        // Mock the order repository to return a list of orders
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Mock the assembler behavior to convert each order into an EntityModel
        when(assembler.toModel(order1)).thenReturn(EntityModel.of(order1));
        when(assembler.toModel(order2)).thenReturn(EntityModel.of(order2));

        // Perform GET request and validate the response
        mockMvc.perform(get("/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
