package com.cn.zm.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cn.zm.bean.Msg;
import com.cn.zm.bean.User;
import com.cn.zm.service.UserService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@PostMapping("/add")
	public String add(@RequestBody User user){
		user.setCreate_time(new Date());
		service.add(user);
		return "添加用户成功";
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@PostMapping("/update")
	public String update(@RequestBody User user){
		service.update(user);
		return "修改用户成功";
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@PostMapping("/delete")
	public String delete(@RequestParam(value="id") String id){
		service.remove(id);
		return "删除成功";
	}
	
	/**
	 * 查询所有用户
	 * @return
	 */
	@PostMapping("/userList")
	public List<User> userList(){
		return service.findAll();
	}
	
	/**
	 * 分页查询用户
	 * @param user
	 * @param page
	 * @param pageCount
	 * @return
	 */
	@PostMapping("/userPage")
	public List<User> userPage(@RequestBody User user, @RequestParam int page, @RequestParam int pageCount){
		Pageable pageable = new PageRequest(page, pageCount);
		return service.findByPage(user, pageable);
	}
	
	/**
	 * 通过id获取用户
	 * @param id
	 * @return
	 */
	@PostMapping("/userGetById")
	public User userGetById(@RequestParam String id){
		return service.getUser(id);
	}
	
	/**
	 * 批量添加用户
	 * @param users
	 * @return
	 */
	@PostMapping("/addBatch")
	public String addBatch(@RequestBody List<User> users){
		users.forEach(user -> {
			user.setCreate_time(new Date());
		});
		service.insertAll(users);
		return "添加成功";
	}
	
	/**
	 * 查询年龄段用户
	 * @param gt
	 * @param lt
	 * @return
	 */
	@PostMapping("/findListAge")
	public List<User> findListAge(@RequestParam int gt, @RequestParam int lt){
		return service.findListAge(gt, lt);
	}
	
	/**
	 * 添加用户消息
	 * @param msg
	 * @return
	 */
	@PostMapping("/addMsg")
	public String addMsg(@RequestBody Msg msg){
		msg.setCreate_time(new Date());
		service.addMsg(msg);
		return "添加成功";
	}
	
	/**
	 * 分组统计查询
	 * @return
	 */
	@PostMapping("/groupList")
	public List<JSONObject> groupList(){
		return service.groupList();
	}
}
