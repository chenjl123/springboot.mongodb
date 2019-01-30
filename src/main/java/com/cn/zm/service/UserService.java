package com.cn.zm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cn.zm.bean.Msg;
import com.cn.zm.bean.User;

import net.sf.json.JSONObject;

public interface UserService {
	void add(User user);
	
	List<User> findAll();

    User getUser(String id);

    void update(User user);

    void insertAll(List<User> users);

    void remove(String id);

    List<User> findByPage(User user, Pageable pageable);
    
    List<User> findListAge(int gl, int lt);
    
    void addMsg(Msg msg);
    
    List<JSONObject> groupList();
}
