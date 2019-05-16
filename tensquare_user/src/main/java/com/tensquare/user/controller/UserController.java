package com.tensquare.user.controller;

import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;


    /**
     * user登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody User inUser){
        User user = userService.findByMobileAndPassword(inUser);
        if(user == null){
            return new Result(false, StatusCode.LOGINERROR,"账号或密码错误");
        }
        String token = jwtUtil.createJWT(user.getId(), user.getMobile(), "user");
        Map<String , Object> map = new HashMap<>();
        map.put("token",token);
        map.put("nickname",user.getNickname());
        map.put("roles","user");
        return new Result(true, StatusCode.OK,"用户登录成功",map);
    }

    /**
     * 发生手机验证码
     */
    @RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
    public Result sendSMS(@PathVariable String mobile){
        userService.sendSMS(mobile);
        return new Result(true, StatusCode.OK,"发送成功");
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
    public Result registerUser(@RequestBody User user,@PathVariable String code){
        String VCode = (String) redisTemplate.opsForValue().get("VCode"+user.getMobile());
        if (VCode==null||VCode.isEmpty()){
            return new Result(false, StatusCode.ERROR,"请先获取验证码！");
        }else if (!VCode.equals(code)){
            return new Result(false, StatusCode.ERROR,"验证码错误");
        }
        userService.addUser(user);
        //注册成功后删除缓存
        redisTemplate.delete("VCode"+user.getMobile());
        return new Result(true, StatusCode.OK,"注册成功");
    }

    /**
     * 添加用户
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addUser(@RequestBody User user){
        userService.addUser(user);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 用户全部列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAllUser(){
        List<User> users = userService.findAllUser();
        return new Result(true, StatusCode.OK,"查询成功",users);
    }

    /**
     * 根据ID查询用户
     */
    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String userId){
        User user = userService.findById(userId);
        return new Result(true, StatusCode.OK,"查询成功",user);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/{userId}",method = RequestMethod.PUT)
    public Result updateUser(@RequestBody User user,@PathVariable String userId){
        userService.updateUser(user,userId);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 根据ID删除用户(必须admin角色才有权限)
     */
    @RequestMapping(value = "/{userId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String userId){
//        //获取请求头
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader==null || !authHeader.startsWith("Bearer ")){
//            return new Result(false, StatusCode.ACCESSERROR,"没有权限操作");
//        }
//
//        //截去头部，提取token
//        String token = authHeader.substring(7);
//        try{
//            Claims claims = jwtUtil.parseJWT(token);
//            //不是admin角色也没有权限删除用户操作
//            if (!claims.get("roles").equals("admin")){
//                return new Result(false, StatusCode.ACCESSERROR,"令牌或角色不正确，没有权限操作");
//            }
//        }catch (Exception e){
//            return new Result(false, StatusCode.ERROR,"令牌有误:"+e);
//        }
        String token = (String) request.getAttribute("claims_admin");
        if (token == null || "".equals(token)){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        userService.deleteById(userId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 用户分页
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.GET)
    public Result searchUser(@RequestBody User user,@PathVariable int page,@PathVariable int size){
        Page<User> pageData= userService.searchUser(user,page,size);
        return new Result(true, StatusCode.OK,"查询分页成功",
                new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }


}
