package com.example.serviceuser.controller.service;

import com.example.common.dto.output.UserOutputDTO;
import com.example.shiro.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/8 11:22
 * description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("service/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "findMapUserDtoByUserId",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Long, UserOutputDTO> findMapUserDtoByUserId(@RequestParam List<Long> userIdList) {
        return userService.findMapUserDtoByUserId(userIdList);
    }
}
