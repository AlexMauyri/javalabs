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
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = MathApplication.class)
@AutoConfigureMockMvc
public class AppliedFunctionControllerTest {

    @Autowired
    private MockMvc mvc;
    private String token;
    private final String login = "{\"username\":\"Vovan\",\"password\":\"1984\"}";
    private Calculation calculation;
    private List<AppliedFunction> functions = new ArrayList<>();
    private byte[] testSerializedFunction = new byte[]{0, 1, 2, 3, 4};
    private ObjectMapper mapper;

    @BeforeEach
    public void setUp() throws Exception {
        MvcResult result = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(login)).andExpect(status().isOk()).andReturn();
        token = "Bearer " + result.getResponse().getContentAsString();
        mapper = new ObjectMapper();
        calculation = new Calculation();
        calculation.setAppliedX(5.25);
        calculation.setResultY(12.07);
        calculation.setHash(123123);

        mvc.perform(post("/calculations").header(HttpHeaders.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(calculation))).andExpect(status().isCreated());

        result = mvc.perform(get("/calculations/hash/{hash}", 123123).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        Calculation[] calculations = mapper.readValue(result.getResponse().getContentAsString(), Calculation[].class);
        calculation = calculations[0];
        for (int id = 1; id <= 5; id++) {
            AppliedFunction appliedFunction = new AppliedFunction();
            appliedFunction.setCalculationId(calculation);
            appliedFunction.setFunctionOrder(id);
            appliedFunction.setFunctionSerialized(testSerializedFunction);
            if (id == 4) {
                appliedFunction.setModStrict(false);
                appliedFunction.setModUnmodifiable(true);
            }
            if (id == 5) {
                appliedFunction.setModStrict(true);
                appliedFunction.setModUnmodifiable(false);
            }

            mvc.perform(post("/functions").header(HttpHeaders.AUTHORIZATION, token).contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(appliedFunction))).andExpect(status().isCreated());
            functions.add(appliedFunction);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/functions").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        AppliedFunction[] functions1 = mapper.readValue(mvcResult.getResponse().getContentAsString(), AppliedFunction[].class);
        for (AppliedFunction appliedFunction : functions1) {
            mvc.perform(delete("/functions/{id}", appliedFunction.getId()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());
        }
        mvcResult = mvc.perform(get("/calculations").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        Calculation[] calculations = mapper.readValue(mvcResult.getResponse().getContentAsString(), Calculation[].class);
        for (Calculation calculation : calculations) {
            mvc.perform(delete("/calculations/{id}", calculation.getId()).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk());
        }
    }

    @Test
    void getTest() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/functions/calculation/{calculationId}", calculation.getId())
                .header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        AppliedFunction[] functions1 = mapper.readValue(mvcResult.getResponse().getContentAsString(), AppliedFunction[].class);


        mvcResult = mvc.perform(get("/functions").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        functions1 = mapper.readValue(mvcResult.getResponse().getContentAsString(), AppliedFunction[].class);
        mvcResult = mvc.perform(get("/functions/{id}", functions1[0].getId()).header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk()).andReturn();

        mvc.perform(get("/functions/calculation/{calculationId}", -1)
                .header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isNotFound());
        mvc.perform(get("/functions/{id}", -1)
                .header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isNotFound());
    }

    @Test
    void deleteAndUpdateTest() throws Exception {
        mvc.perform(delete("/functions/{id}", -1).header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isNotFound());
        MvcResult mvcResult = mvc.perform(get("/functions").header(HttpHeaders.AUTHORIZATION, token)).andExpect(status().isOk()).andReturn();
        AppliedFunction[] functions1 = mapper.readValue(mvcResult.getResponse().getContentAsString(), AppliedFunction[].class);
        functions1[0].setModUnmodifiable(true);
        String str = mapper.writeValueAsString(functions1[0]);
        mvc.perform(put("/functions")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)).andExpect(status().isOk());
        functions1[0].setId(1);
        str = mapper.writeValueAsString(functions1[0]);
        mvc.perform(put("/functions")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(str)).andExpect(status().isNotFound());
    }


}
