package com.tensquare.user.service;

import com.tensquare.user.dao.AdminDao;
import com.tensquare.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

@Service
@Transactional
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 新增管理员密码加密
     */
    public void add(Admin admin){
        admin.setId(idWorker.nextId()+"");
        //加密后密码
        String encrypted_password = encoder.encode(admin.getPassword());
        admin.setPassword(encrypted_password);
        adminDao.save(admin);
    }

    /**
     * 根据登陆名和密码查询
     */
    public Admin findByLoginnameAndPassword(Admin inAdmin) {
        Admin admin = adminDao.findByLoginname(inAdmin.getLoginname());
        //判断用户有没有，然后再验证加密后密码
        if (admin!=null && encoder.matches(inAdmin.getPassword(),admin.getPassword()) ){
            return admin;
        }else {
            return null;
        }
    }

    /**
     * 根据ID删除
     */
    public void deleteById(String id){
        adminDao.deleteById(id);
    }
}
