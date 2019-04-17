package com.tensquare.tensquare_base.service;

import com.tensquare.tensquare_base.dao.LabelDao;
import com.tensquare.tensquare_base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabelService {
    @Autowired
    LabelDao labelDao;
    @Autowired
    IdWorker idWorker;

    /**
     * 查询全部标签
     * @return List<Label>
     */
    public List<Label> fingAll(){
        return labelDao.findAll();
    }

    /**
     * 根据ID查询标签
     * @return Label
     */
    public Label findAllById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 增加标签
     */
    public void addLabel(Label label){
        labelDao.save(label);
    }

    /**
     * 修改标签
     */
    public void updateLabel(Label label){
        labelDao.save(label);
    }

    /**
     * 删除标签
     */
    public void deleteLabelById(String id){
        labelDao.deleteById(id);
    }

    /**
     * 构建查询条件
     *  @return Specification<Label>
     */
    private Specification<Label> createSpecification(Label label){
        return new Specification<Label>() {
            /**
             *
             * @param root 根对象，也就是要把条件封装到哪个对象中，where 类名=label.getid
             * @param criteriaQuery 封装的都是查询关键字，比如groud by order by（基本用不着）
             * @param cb 用了封装条件对象的，如果返回null，表示不需要条件
             * @return Specification<Label>
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new 一个list集合，来存放所有的条件
                List<Predicate> predicateList = new ArrayList<>();
                if (label.getLabelname()!=null && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class),
                            "%"+label.getLabelname()+"%");//where labelname like "%小明%"(label.getLabelname()就是小明,as(String.class)指定类型)
                    predicateList.add(predicate);
                }
                if (label.getState()!=null && !"".equals(label.getState())){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());//state = "1"(label.getState()就是"1 ")
                    predicateList.add(predicate);
                }
                //new一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[predicateList.size()];
                //把list转为数组
                parr = predicateList.toArray(parr);
                //cb.or等于or；；；；cb.and等于and
                return cb.and(parr);//where labelname like "%小明%" and state = "1"
            }
        };
    }

    /**
     * 条件查询
     * @return List<Label>
     */
    public List<Label> fingSearch(Label label) {
        return labelDao.findAll(createSpecification(label));
    }

    /**
     * 分页条件查询
     * @return List<Label>
     */
    public Page<Label> pageQuery(Label label, int page, int size) {
        //封装分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(createSpecification(label),pageable);
    }
}
