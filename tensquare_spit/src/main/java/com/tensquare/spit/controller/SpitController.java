package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 增加吐槽
     */
    @RequestMapping(method = RequestMethod.POST )
    public Result addspit(@RequestBody Spit spit){
        spitService.addspit(spit);
       return new Result(true, StatusCode.OK,"吐槽成功");
    }

    /**
     * Spit全部列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    /**
     * 根据ID查询吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(spitId));
    }

    /**
     * 修改吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@RequestBody Spit spit,@PathVariable String spitId){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 根据ID删除吐槽
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID查询吐槽数据（分页）
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page ,@PathVariable int size){
        Page<Spit> pageData = spitService.findByParentid(parentid,page,size);
        PageResult<Spit> pageResult = new PageResult<>(pageData.getTotalElements(),pageData.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String spitId){
        String userId = "110";
        //判断有没点过赞，记录在redis缓存中
        if (redisTemplate.opsForValue().get(userId+"_"+spitId) != null){
            return new Result(false,StatusCode.REPERROR,"你已经点过赞了");
        }
        spitService.updateThumbup(spitId);
        //设置已点赞标志
        redisTemplate.opsForValue().set(userId+"_"+spitId,"1");
        return new Result(true,StatusCode.OK,"点赞成功");
    }

    /**
     * 浏览加一
     */
    @RequestMapping(value = "/visit/{spitId}",method = RequestMethod.PUT)
    public Result updateVisits(@PathVariable String spitId){
        spitService.updateVisits(spitId);
        return new Result(true,StatusCode.OK,"浏览加一");
    }

    /**
     * 分享加一
     */
    @RequestMapping(value = "/share/{spitId}",method = RequestMethod.PUT)
    public Result updateShare(@PathVariable String spitId){
        spitService.updateShare(spitId);
        return new Result(true,StatusCode.OK,"分享成功");
    }
}
