package com.example.serviceshortmessage.controller;

import com.example.serviceshortmessage.base.BaseController;
import com.example.serviceshortmessage.base.ResultMessage;
import com.example.serviceshortmessage.service.ShortMessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 14:48
 * description:
 */
@AllArgsConstructor
@RestController
public class ShortMessageController extends BaseController {

    private final ShortMessageService shortMessageService;

    @PostMapping(value = "sendShortMessageFromCode",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultMessage sendShortMessageFromCode(@RequestParam String phoneNumbers,
                                                  @RequestParam String code,
                                                  @RequestParam String codeType) {
        shortMessageService.sendShortMessageFromCode(phoneNumbers, code, codeType);
        return success();
    }
}
