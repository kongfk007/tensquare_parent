package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.pojo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    /**
     * 添加好友，入门级业务
     * @param userid
     * @param friendid
     * @return 0已经是好友，1添加好友成功
     */
    public int addFriend(String userid, String friendid) {
        //判断用户是否已经添加过了这个为好友
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if (friend!=null){
            return 0;
        }
        //添加好友，让好友表中userid到friendid方向的type为0
        Friend newFriend = new Friend(userid,friendid,"0");
        friendDao.save(newFriend);
        //判断friendid到userid是否有数据，有就把双方状态改为1
        if (friendDao.findByUseridAndFriendid(friendid, userid)!=null){
            //把双方都改为1
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;
    }
}
