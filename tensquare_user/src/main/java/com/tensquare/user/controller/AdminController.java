package com.tensquare.user.controller;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import com.tensquare.user.service.AdminService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;

    /**
     * 根据ID删除
     */
    @RequestMapping(value = "/{adminId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String adminId){
        adminService.deleteById(adminId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 新增管理员(密码加密)
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result(true, StatusCode.OK,"增加管理员成功");
    }

    /**
     *管理员登录
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Admin inAdmin){
        Admin admin = adminService.findByLoginnameAndPassword(inAdmin);
        if (admin==null){
            return new Result(false, StatusCode.LOGINERROR,"账号或密码错误");
        }
        //使前后端可通话的操作，采用jwt实现
        //生成token（令牌）
        String token = jwtUtil.createJWT(admin.getId(),admin.getLoginname(),"admin");
        Map<String ,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("loginname",admin.getLoginname());
        map.put("roles","admin");
        return new Result(true, StatusCode.OK,"登录成功",map);
    }
}
