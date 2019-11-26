package ru.mai.webclassifier.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mai.webclassifier.database.models.TextClass;
import ru.mai.webclassifier.models.*;
import ru.mai.webclassifier.services.ClassificationService;

@Controller
public class TextClassifyController {
    private ClassificationService classificationService;
    
    public TextClassifyController() throws Exception {
        classificationService = new ClassificationService();
    }
    
    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public String classificationForm(Model model) {
        model.addAttribute("classifyForm", new ClassifyForm());
        return "classifyform";
    }
    
    @RequestMapping(value = "/classify", method = RequestMethod.POST)
    public String classificationResult(@ModelAttribute ClassifyForm classificatorForm, Model model) throws Exception {
        TextClass textClass = classificationService.predict(classificatorForm.getText());
        model.addAttribute("textClass", "\"" + TextClass.toRus(textClass) + "\"");
        return "classifyresult";
    }
}
