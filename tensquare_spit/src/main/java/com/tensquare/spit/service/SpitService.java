package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {
    @Autowired
    SpitDao spitDao;
    @Autowired
    IdWorker idWorker;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 增加吐槽
     */
    public void addspit(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布时间
        spit.setVisits(0);//浏览量
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//评论数
        spit.setShare(0);//分享数
        spit.setState("1");//审核状态
        spitDao.save(spit);
        if (spit.getParentid()!=null && !"".equals(spit.getParentid())){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
    }

    /**
     * Spit全部列表
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 根据ID查询吐槽
     */
    public Spit findById(String _id){
        return spitDao.findById(_id).get();
    }

    /**
     * 修改吐槽
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     * 根据ID删除吐槽
     */
    public void deleteById(String _id){
        spitDao.deleteById(_id);
    }

    /**
     * 根据上级ID查询吐槽数据（分页）
     */
    public Page<Spit> findByParentid(String parentid, int page ,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    /**
     * 吐槽点赞
     */
    public void updateThumbup(String spitId) {
        //第一种效率不高
//        Spit spit = spitDao.findById(spitId).get();
//        if (spit.getThumbup()!=null) {
//            spit.setThumbup(spit.getThumbup() + 1);
//        }else {
//            spit.setThumbup(1);
//        }
//        spitDao.save(spit);
        //第二种
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }

    /**
     * 浏览加一
     */
    public void updateVisits(String spitId) {
        //第二种
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("visits",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }

    /**
     * 分享加一
     */
    public void updateShare(String spitId) {
        //第二种
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("share",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }

}
