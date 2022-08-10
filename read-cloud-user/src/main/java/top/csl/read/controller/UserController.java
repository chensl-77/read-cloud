package top.csl.read.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.csl.read.common.result.Result;
import top.csl.read.param.UserParam;
import top.csl.read.service.UserService;

/**
 * @Author: csl
 * @DateTime: 2022/8/10 9:10
 **/
@Api(tags = "用户模块")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "userParam",value = "用户注册信息",required = true,dataType = "UserParam")
    @PostMapping("/register")
    public Result register(@RequestBody UserParam userParam) {
        return this.userService.register(userParam);
    }

}
