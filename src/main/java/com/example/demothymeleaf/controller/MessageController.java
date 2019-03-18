package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.model.Message;
import com.example.demothymeleaf.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MessageController {
    Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public ModelAndView list(){
        Iterable<Message> messages = this.messageRepository.findAll();

        logger.info("列表信息开始打印了：");
        for (Message message : messages) {
            logger.info("summary = " + message.getSummary() + " ; " + "id = " + message.getId() + " ; " + "message = " + message.getText() + " ; " + "createtime = " + message.getCreated());
        }
        return new ModelAndView("messages/list","messages",messages);
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Message message){
        logger.info(".............");
        logger.info("summary = " + message.getSummary() + ";" + "message = " + message.getText());
        return new ModelAndView("messages/view","message",message);
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute Message message){
        return "messages/form";
    }

    @PostMapping
    @ResponseBody
    public ModelAndView create(@Valid Message message, BindingResult result, RedirectAttributes redirect){
        if (result.hasErrors()){
            return new ModelAndView("messages/form","formErrors", result.getAllErrors());
        }
        message = this.messageRepository.save(message);
        redirect.addFlashAttribute("globalMessage","Successfully created a new message : " + message.getSummary());
        logger.info("新增的信息为：");
        logger.info("summary = " + message.getSummary() + " ; " + "id = " + message.getId() + " ; " + "message = " + message.getText() + " ; " + "createtime = " + message.getCreated());
        return new ModelAndView("redirect:/{message.id}", "message.id", message.getId());
    }

    @RequestMapping("foo")
    public String foo(){
        throw new RuntimeException("Exception exception in controller");
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id){
        Message deleteMessage = this.messageRepository.findMessage(id);
        this.messageRepository.deleteMessage(id);
        Iterable<Message> messages = this.messageRepository.findAll();
        logger.info("删除的信息为：" + "id = " + deleteMessage.getId() + ";" + "sumary = " + deleteMessage.getSummary() + ";" + "message = " + deleteMessage.getText() + ";" + "createtime = " + deleteMessage.getCreated());
        logger.info("删除后的信息列表为：");
        for (Message message : messages) {
            logger.info("summary = " + message.getSummary() + " ; " + "id = " + message.getId() + " ; " + "message = " + message.getText() + " ; " + "createtime = " + message.getCreated());
        }
        return new ModelAndView("messages/list","messages", messages);
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Message message){
        return new ModelAndView("messages/form","message",message);
    }
}
