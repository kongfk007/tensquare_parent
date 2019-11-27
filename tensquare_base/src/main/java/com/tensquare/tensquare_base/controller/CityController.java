package com.tensquare.tensquare_base.controller;

import com.tensquare.tensquare_base.pojo.City;
import com.tensquare.tensquare_base.service.CityService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;
    /**
     * 新增城市
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addcity(@RequestBody City city){
        cityService.addcity(city);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 返回城市列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",cityService.findAll());
    }

    /**
     * 修改城市
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result updateCity(@RequestBody City city,@PathVariable String id){
        cityService.updateCity(city,id);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除城市
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteCity(@PathVariable String id){
        cityService.deleteCity(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据ID查询城市
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",cityService.findById(id));
    }

    /**
     * 条件查询
     * @return List<Label>
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result fingSearch(@RequestBody City city) {
        return new Result(true,StatusCode.OK,"查询成功",cityService.fingSearch(city));
    }


    /**
     * 分页条件查询
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody City city,@PathVariable int page,@PathVariable int size) {
        Page<City>  pageData = cityService.pageQuery(city,page,size);
        return new Result(true,StatusCode.OK,"查询成功",
                new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }
}
