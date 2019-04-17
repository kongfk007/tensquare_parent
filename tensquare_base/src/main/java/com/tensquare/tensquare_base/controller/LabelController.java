package com.tensquare.tensquare_base.controller;

import com.tensquare.tensquare_base.pojo.Label;
import com.tensquare.tensquare_base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制层
 */
@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelService;

    /**
     * 查询全部标签
     * @return List<Label>
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.fingAll());
    }

    /**
     * 根据ID查询标签
     * @return Label
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findAllById(id));
    }

    /**
     * 增加标签
     * @return void
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addLabel(@RequestBody Label label){
        labelService.addLabel(label);
        return new Result(true,StatusCode.OK,"增加标签成功");
    }


    /**
     * 修改标签
     * @return void
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result updateLabel(@RequestBody Label label,@PathVariable String id){
        label.setId(id);
        labelService.updateLabel(label);
        return new Result(true,StatusCode.OK,"修改标签成功");
    }

    /**
     * 删除标签
     * @return void
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result deleteLabel(@PathVariable String id){
        labelService.deleteLabelById(id);
        return new Result(true,StatusCode.OK,"删除标签成功");
    }

    /**
     * 条件查询
     * @return void
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result fingSearch(@RequestBody Label label){
        List<Label> labels = labelService.fingSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",labels);
    }

    /**
     * 分页条件查询
     * @return void
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label>  pageData = labelService.pageQuery(label,page,size);
        System.out.println(pageData);
        return new Result(true,StatusCode.OK,"查询成功",
                new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }

}
