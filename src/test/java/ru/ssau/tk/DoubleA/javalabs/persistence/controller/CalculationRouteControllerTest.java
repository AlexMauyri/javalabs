package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
import ru.ssau.tk.DoubleA.javalabs.functions.*;
import ru.ssau.tk.DoubleA.javalabs.operations.MiddleSteppingDifferentialOperator;
import ru.ssau.tk.DoubleA.javalabs.operations.TabulatedIntegrationOperator;
import ru.ssau.tk.DoubleA.javalabs.persistence.dto.CalculationDataDTO;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;
import ru.ssau.tk.DoubleA.javalabs.persistence.service.CalculationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MathApplication.class)
@AutoConfigureMockMvc
public class CalculationRouteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CalculationService calculationService;

    private String token;
    private final String login = "{\"username\":\"Vovan\",\"password\":\"1984\"}";

    private ObjectMapper functionSetMapper;
    private List<Calculation> createdCalculationRoutes = new ArrayList<>();
    private final double[] xVal = new double[]{9, -111, 343245};
    private final double[] yVal = new double[]{-700, 4004, 34};
    private final List<List<MathFunction>> functionSets = Arrays.asList(
            Arrays.asList(
                    new SqrFunction(),
                    new IdentityFunction(),
                    new MiddleSteppingDifferentialOperator(1E-5)),
            Arrays.asList(
                    new ArrayTabulatedFunction(new SqrFunction(), 1, 10, 7),
                    new TabulatedIntegrationOperator(),
                    new IdentityFunction()),
            Arrays.asList(
                    new LinkedListTabulatedFunction(new SqrFunction(), -7, 3, 25),
                    new ArrayTabulatedFunction(new IdentityFunction(), 0, 255, 3),
                    new NRootCalculateFunction(4))
    );

    @BeforeEach
    public void setUp() throws Exception {
        functionSetMapper = new ObjectMapper();
        functionSetMapper.activateDefaultTyping(
                functionSetMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        MvcResult result = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(login)).andExpect(status().isOk()).andReturn();
        token = "Bearer " + result.getResponse().getContentAsString();

        for (int id = 0; id < 3; id++) {
            mvc.perform(post("/calculations_routes")
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("appliedValue", Double.toString(xVal[id]))
                            .param("resultValue", Double.toString(yVal[id]))
                            .content(functionSetMapper.writeValueAsString(functionSets.get(id))))
                    .andExpect(status().isCreated())
                    .andExpect(content().string("Calculation created successfully"));

            String calculationJson = mvc.perform(get("/calculations_routes/find")
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("appliedValue", Double.toString(xVal[id]))
                            .content(functionSetMapper.writeValueAsString(functionSets.get(id))))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            createdCalculationRoutes.add(new ObjectMapper().readValue(calculationJson, Calculation.class));
        }
    }

    @AfterEach
    public void tearDown() {
        for (Calculation createdCalculationRoute : createdCalculationRoutes) {
            calculationService.deleteById(createdCalculationRoute.getId());
        }
    }

    @Test
    void createAndFindCalculationTest() throws Exception {
        mvc.perform(post("/calculations_routes")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("appliedValue", Double.toString(1.23))
                        .param("resultValue", Double.toString(4.56))
                        .content(functionSetMapper.writeValueAsString(functionSets.getFirst())))
                .andExpect(status().isCreated())
                .andExpect(content().string("Calculation created successfully"));

        String calculationJson = mvc.perform(get("/calculations_routes/find")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("appliedValue", Double.toString(1.23))
                        .content(functionSetMapper.writeValueAsString(functionSets.getFirst())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdCalculationRoutes.add(new ObjectMapper().readValue(calculationJson, Calculation.class));
    }

    @Test
    void getCalculationByIdTest() throws Exception {
        for (int i = 0; i < 3 ; i++) {
            String calculationDataDTOJson = mvc.perform(get("/calculations_routes/" + createdCalculationRoutes.get(i).getId())
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            CalculationDataDTO calculationData = new ObjectMapper().readValue(calculationDataDTOJson, CalculationDataDTO.class);

            assert(calculationData.getAppliedValue() == createdCalculationRoutes.get(i).getAppliedX());
            assert(calculationData.getResultValue() == createdCalculationRoutes.get(i).getResultY());
        }
    }

    @Test
    void getAllCalculationsByFilterTest() throws Exception {
        String calculationsDataArrayJson = mvc.perform(get("/calculations_routes/filter")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CalculationDataDTO[] calculations = new ObjectMapper().readValue(calculationsDataArrayJson, CalculationDataDTO[].class);

        assert (calculations.length == 3);
    }
}
