package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友数据访问层
 */
public interface FriendDao extends JpaRepository<Friend,String>, JpaSpecificationExecutor<Friend> {

    Friend findByUseridAndFriendid(String userid,String friend);

    @Modifying
    @Query(value ="UPDATE tb_friend SET islike=? WHERE userid=? AND friendid=?" ,nativeQuery = true)
    void updateIslike(String islike,String userid,String friend);
}
