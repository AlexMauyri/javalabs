package ru.ssau.tk.DoubleA.javalabs.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.tk.DoubleA.javalabs.functions.TabulatedFunction;
import ru.ssau.tk.DoubleA.javalabs.io.FunctionsIO;

import java.io.*;

@RestController
public class TabulatedFunctionConverterController {
    @PostMapping("/convertFromBLOB")
    @ResponseBody
    public String convertFromBLOB(@RequestBody byte[] bytes) throws JsonProcessingException {
        TabulatedFunction function = null;
        try(BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes))) {
            function = FunctionsIO.deserialize(inputStream);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(function);
    }

    @PostMapping("/convertFromXML")
    @ResponseBody
    public String convertFromXML(@RequestBody String xmlFunction) throws JsonProcessingException {
        TabulatedFunction function = null;
        try(BufferedReader reader = new BufferedReader(new StringReader(xmlFunction))) {
            function = FunctionsIO.deserializeXml(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ObjectMapper().writeValueAsString(function);
    }
}
