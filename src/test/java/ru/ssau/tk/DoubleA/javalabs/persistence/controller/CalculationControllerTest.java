package ru.ssau.tk.DoubleA.javalabs.persistence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = MathApplication.class)
@AutoConfigureMockMvc
public class CalculationControllerTest {

    @Autowired
    private MockMvc mvc;

    private List<Calculation> createdCalculations = new ArrayList<>();
    private double[] xVal = new double[]{47, -62, 91, -99, -23};
    private double[] yVal = new double[]{-30, -4, -73, 42, 3};
    private long[] hash = new long[]{1, 2, 3, 3, 3};
    private ObjectMapper mapper;

    @BeforeEach
    @WithMockUser(username = "Vovan", password = "1984")
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        for (int id = 0; id <= 4; id++) {
            Calculation calculation = new Calculation();
            calculation.setAppliedX(xVal[id]);
            calculation.setResultY(yVal[id]);
            calculation.setHash(hash[id]);

            mvc.perform(post("/calculations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(calculation)));

            createdCalculations.add(calculation);
        }
    }

    @AfterEach
    @WithMockUser(username = "Vovan", password = "1984")
    public void tearDown() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mvc.perform(get("/calculations")).andReturn();
        Calculation[] calculations = mapper.readValue(result.getResponse().getContentAsString(), Calculation[].class);
        for (Calculation calculation : calculations) {
            mvc.perform(delete("/calculations/{id}", calculation.getId()));
        }
    }

    @Test
    @WithMockUser(username = "Vovan", password = "1984")
    void getTest() throws Exception {
        MvcResult result = mvc.perform(get("/calculations")).andReturn();


        Calculation[] calculations = mapper.readValue(result.getResponse().getContentAsString(), Calculation[].class);

        for (int i = 0; i < calculations.length; i++) {
            Assertions.assertEquals(calculations[i].getAppliedX(), xVal[i]);
            Assertions.assertEquals(calculations[i].getResultY(), yVal[i]);
            Assertions.assertEquals(calculations[i].getHash(), hash[i]);
        }

        for (int i = 0; i < calculations.length; i++) {
            int id = calculations[i].getId();
            result = mvc.perform(get("/calculations/{id}", id)).andReturn();
            Calculation calculation = mapper.readValue(result.getResponse().getContentAsString(), Calculation.class);
            Assertions.assertEquals(calculation.getAppliedX(), xVal[i]);
            Assertions.assertEquals(calculation.getResultY(), yVal[i]);
            Assertions.assertEquals(calculation.getHash(), hash[i]);
        }

        mvc.perform(get("/calculations/{id}", -1));
        mvc.perform(get("/calculations/hash/{hash}", -101));


        result = mvc.perform(get("/calculations/filter?appliedValue=-10&operationX=lessThen&sortingY=ASCENDING"))
                .andReturn();

        calculations = mapper.readValue(result.getResponse().getContentAsString(), Calculation[].class);

        Assertions.assertEquals(calculations[0].getAppliedX(), -62);
        Assertions.assertEquals(calculations[1].getAppliedX(), -23);
        Assertions.assertEquals(calculations[2].getAppliedX(), -99);
    }

    @Test
    @WithMockUser(username = "Vovan", password = "1984")
    void createTest() throws Exception {
        MvcResult result = mvc.perform(post("/calculations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"appliedX\":123,\"resultY\":213,\"hash\":1984}"))
                .andExpect(status().isCreated()).andReturn();

        Calculation calculation = mapper.readValue(result.getResponse().getContentAsString(), Calculation.class);

        result = mvc.perform(get("/calculations/{id}", calculation.getId())).andReturn();

        Calculation calculation2 = mapper.readValue(result.getResponse().getContentAsString(), Calculation.class);

        Assertions.assertEquals(calculation2.getAppliedX(), calculation.getAppliedX());
        Assertions.assertEquals(calculation2.getResultY(), calculation.getResultY());
        Assertions.assertEquals(calculation2.getHash(), calculation.getHash());
    }

    @Test
    @WithMockUser(username = "Vovan", password = "1984")
    void updateTest() throws Exception {
        mvc.perform(post("/calculations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"appliedX\":\"123\",\"resultY\":\"213\",\"hash\":\"1984\"}"));

        MvcResult result = mvc.perform(get("/calculations/hash/1984")).andReturn();
        Calculation[] calculations = mapper.readValue(result.getResponse().getContentAsString(), Calculation[].class);
        result = mvc.perform(put("/calculations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"id\":%d,\"appliedX\":321,\"resultY\":999,\"hash\":2005}", calculations[0].getId())))
                .andReturn();

        Calculation calculation = mapper.readValue(result.getResponse().getContentAsString(), Calculation.class);

        result = mvc.perform(get("/calculations/" + calculation.getId())).andReturn();

        Calculation calculation2 = mapper.readValue(result.getResponse().getContentAsString(), Calculation.class);

        Assertions.assertEquals(calculation2.getAppliedX(), calculation.getAppliedX());
        Assertions.assertEquals(calculation2.getResultY(), calculation.getResultY());
        Assertions.assertEquals(calculation2.getHash(), calculation.getHash());


        mvc.perform(put("/calculations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":-1,\"appliedX\":321,\"resultY\":999,\"hash\":2005}")).andReturn();

        mvc.perform(delete("/calculations/-1")).andReturn();
    }
}
