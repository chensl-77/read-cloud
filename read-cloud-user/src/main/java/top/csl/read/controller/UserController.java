package top.csl.read.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import top.csl.read.client.BookService;
import top.csl.read.common.result.Result;
import top.csl.read.param.UserParam;
import top.csl.read.service.UserService;
import top.csl.read.vo.AuthVO;

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
    @ApiImplicitParam(name = "userParam", value = "用户注册信息", required = true, dataType = "UserParam")
    @PostMapping("/register")
    public Result register(@RequestBody UserParam userParam) {
        return this.userService.register(userParam);
    }


    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 200, message = "", response = AuthVO.class)})
    @PostMapping("/login")
    public Result<AuthVO> login(@RequestBody UserParam userParam) {
        String loginName = userParam.getLoginName();
        String password = userParam.getUserPwd();
        return userService.login(loginName, password);
    }

    @ApiOperation(value = "用户签到", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Result.class)})
    @PostMapping("/sign")
    public Result sign(@RequestHeader("userId") Integer userId) {
        return this.userService.sign(userId);
    }

    @ApiOperation(value = "统计当月连续签到天数", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Integer.class)})
    @PostMapping("/signdays")
    public Result signdays(@RequestHeader("userId") Integer userId) {
        return this.userService.signdays(userId);
    }

    @Autowired
    @Lazy
    private BookService bookService;

    @GetMapping("get")
    public Result get() {
        return bookService.getBookById("60034881");
    }
}

