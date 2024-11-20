//package ru.ssau.tk.DoubleA.javalabs.persistence.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.ssau.tk.DoubleA.javalabs.bootloader.MathApplication;
//import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;
//import ru.ssau.tk.DoubleA.javalabs.persistence.service.AppliedFunctionService;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = MathApplication.class)
//@AutoConfigureMockMvc
//public class AppliedFunctionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AppliedFunctionService appliedFunctionService;
//
//    @InjectMocks
//    private AppliedFunctionController appliedFunctionController;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(appliedFunctionController).build();
//    }
//
//    @Test
//    public void testGetFunction() throws Exception {
//        AppliedFunction function = new AppliedFunction();
//
//        when(appliedFunctionService.getById(1)).thenReturn(function);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/functions/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.someProperty").value("test"));
//    }
//
//    @Test
//    public void testGetAllFunctions() throws Exception {
//        AppliedFunction function1 = new AppliedFunction();
//
//        AppliedFunction function2 = new AppliedFunction();
//
//        List<AppliedFunction> functions = Arrays.asList(function1, function2);
//
//        when(appliedFunctionService.getAll()).thenReturn(functions);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/functions"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
//    }
//
//    @Test
//    public void testCreateFunction() throws Exception {
//        AppliedFunction function = new AppliedFunction();
//
//        appliedFunctionService.create(any(AppliedFunction.class));
//        //when(appliedFunctionService.create(any(AppliedFunction.class))).thenReturn(function);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/functions")
//                        .contentType("application/json")
//                        .content("{\"id\": 1, \"someProperty\": \"test\"}"))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.someProperty").value("test"));
//    }
//
//    @Test
//    public void testDeleteFunction() throws Exception {
//        AppliedFunction function = new AppliedFunction();
//
//        when(appliedFunctionService.getById(1)).thenReturn(function);
//        doNothing().when(appliedFunctionService).deleteById(1);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/functions/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Deleted element with id 1"));
//
//        verify(appliedFunctionService, times(1)).deleteById(1);
//    }
//}
