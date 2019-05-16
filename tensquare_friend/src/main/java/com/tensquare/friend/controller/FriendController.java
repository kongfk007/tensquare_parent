package com.tensquare.friend.controller;

import com.netflix.discovery.converters.Auto;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友
     * @param friendid 想添加好友的id
     * @param type 1喜欢，0不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT )
    public Result addFriend(@PathVariable String friendid,@PathVariable String type){
        //验证是否登录
        Claims claims=(Claims)request.getAttribute("claims_user");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        //获取当前用户id
        String userid = claims.getId();
        //判断添加好友
        System.out.println(type);
        if (type!=null){
            if (type.equals("1")){
                //添加好友，0已经是好友，1添加好友成功
                int flag = friendService.addFriend(userid,friendid);
                System.out.println(flag);
                if (flag==0){
                    return new Result(false, StatusCode.ERROR,"已经是你好友了");
                }
            }
        }else {
            return new Result(false, StatusCode.ERROR,"type参数异常");
        }
        return new Result(true, StatusCode.OK,"添加好友成功");
    }
}
