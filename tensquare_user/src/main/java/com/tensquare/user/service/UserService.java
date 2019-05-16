package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.Admin;
import com.tensquare.user.pojo.User;
import entity.Result;
import entity.StatusCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * user的业务处理层
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 根据手机号和密码查询用户
     */
    public User findByMobileAndPassword(User inUser){
        User user = userDao.findByMobile(inUser.getMobile());
        //根据用户输入的mobile判断有没有这用户，有就再验证加密后密码
        if (user!=null && encoder.matches(inUser.getPassword(),user.getPassword())){
            return user;
        }else{
            return null;
        }
    }

    /**
     * 添加用户
     */
    public void addUser(User user){
        user.setId(idWorker.nextId()+"");

        //密码加密
        String encrypted_password = encoder.encode(user.getPassword());

        user.setPassword(encrypted_password);
        userDao.save(user);
    }

    /**
     * 用户全部列表
     */
    public List<User> findAllUser(){
        return userDao.findAll();
    }

    /**
     * 根据ID查询用户
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }

    /**
     * 修改用户
     */
    public void updateUser(User user,String id){
        user.setId(id);
        userDao.save(user);
    }

    /**
     * 根据ID删除用户(必须admin角色才有权限)
     */
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    /**
     * 构建查询条件
     *  @return Specification<Label>
     */
    private Specification<User> createSpecification(User user){
        return new Specification<User>() {
            /**
             *
             * @param root 根对象，也就是要把条件封装到哪个对象中，where 类名=label.getid
             * @param criteriaQuery 封装的都是查询关键字，比如groud by order by（基本用不着）
             * @param cb 用了封装条件对象的，如果返回null，表示不需要条件
             * @return Specification<Label>
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new 一个list集合，来存放所有的条件
                List<Predicate> predicateList = new ArrayList<>();
                if (user.getNickname()!=null && !"".equals(user.getNickname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class),
                            "%"+user.getNickname()+"%");//where labelname like "%小明%"(label.getLabelname()就是小明,as(String.class)指定类型)
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
     * 用户分页
     */
    public Page<User> searchUser(User user,int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return userDao.findAll(createSpecification(user),pageable);
    }

    /**
     * 发生手机验证码
     */
    public void sendSMS(String mobile) {
        //生成六位随机数
        String VCode = RandomStringUtils.randomNumeric(6);
        //放进缓存,设置过期时间
        redisTemplate.opsForValue().set("VCode"+mobile,VCode,5, TimeUnit.MINUTES);
        //将手机号码和验证码用户map保存
        Map<String ,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("VCode",VCode);
        //将map放进sms的消息队列中
        rabbitTemplate.convertAndSend("sms",map);
        //在控制台显示，为了开发方便
        System.out.println(mobile+"的手机验证码为："+VCode);

    }
}
