package ru.ssau.tk.DoubleA.javalabs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/createPlot")
    public String createPlotPage() {
        return "createPlot";
    }

    @GetMapping("/doDifferential")
    public String doDifferentialPage() {
        return "doDifferential";
    }

    @GetMapping("/doIntegral")
    public String doIntegralPage() {
        return "doIntegral";
    }

    @GetMapping("/functionArithmetic")
    public String functionArithmeticPage() {
        return "functionArithmetic";
    }

    @GetMapping("/createNewFunction")
    public String createNewFunctionPage() {
        return "createNewFunction";
    }

    @GetMapping("popup/createTabulatedFunction")
    public String chooseMethodPage() {
        return "popup/createTabulatedFunction";
    }

    @GetMapping("popup/tableCreation")
    public String createFromTablePage() {
        return "popup/tableCreation";
    }

    @GetMapping("popup/functionCreation")
    public String createFromFunctionPage() {
        return "popup/functionCreation";
    }

    @GetMapping("popup/saveFile")
    public String saveFunctionPage() {
        return "popup/saveFile";
    }

    @GetMapping("popup/error")
    public String errorPage() {
        return "popup/error";
    }

    @GetMapping("popup/addNewPoint")
    public String addNewPointPage() {
        return "popup/addNewPoint";
    }
}
