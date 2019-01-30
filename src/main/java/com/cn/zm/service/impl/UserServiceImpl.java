package com.cn.zm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cn.zm.bean.Msg;
import com.cn.zm.bean.User;
import com.cn.zm.service.UserService;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private MongoTemplate mongo;
	
	@Override
	public void add(User user) {
		// TODO Auto-generated method stub
		mongo.insert(user);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		//按创建时间降序
		Sort sort = new Sort(Sort.Direction.DESC, "create_time");
		return mongo.find(new Query().with(sort), User.class);
	}

	@Override
	public User getUser(String id) {
		// TODO Auto-generated method stub
		return mongo.findOne(new Query(Criteria.where("id").is(id)), User.class);
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		Criteria criteria = Criteria.where("id").is(user.getId());
		Query query = new Query(criteria);
		Update update = Update.update("user_name", user.getUser_name()).set("user_code", user.getUser_code());
		mongo.updateFirst(query, update, User.class);
	}

	@Override
	public void insertAll(List<User> users) {
		// TODO Auto-generated method stub
		mongo.insertAll(users);
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		Criteria criteria = Criteria.where("id").is(id);
		Query query = new Query(criteria);
		mongo.remove(query, User.class);
	}

	@Override
	public List<User> findByPage(User user, Pageable pageable) {
		// TODO Auto-generated method stub
		Criteria criteria = Criteria.where("user_name").regex("^" + user.getUser_name());
		Query query = new Query(criteria);
		return mongo.find(query.with(pageable), User.class);
	}

	@Override
	public List<User> findListAge(int gt, int lt){
		//where age >= gt and age <= lt
		Query query = new Query();
		Criteria gtcri = Criteria.where("age").gte(gt);
		Criteria ltcri = Criteria.where("age").lte(lt);
		//query.addCriteria(new Criteria().andOperator(gtcri, ltcri));
		
		//where age >= gt and age <= lt and user_code = cjll
		Criteria ocri = Criteria.where("age").gte(gt)
				.lte(lt)
				.and("user_code").is("cj11");
		//query.addCriteria(ocri);
		
		//where user_name like "陈吉1%" or user_code = 'lf'
		Criteria cname = Criteria.where("user_name").regex("^陈吉1");
		Criteria ccode = Criteria.where("user_code").is("lf");
		Criteria orcri = new Criteria();
		orcri.orOperator(cname, ccode);
		query.addCriteria(orcri);
		
		//排序  order by create_time desc, age asc
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.DESC, "create_time"));
		orders.add(new Order(Direction.ASC, "age"));
		Sort sort = new Sort(orders);
		query.with(sort);
		
		//分页+排序查询
		Pageable pable = new PageRequest(1, 2, sort);
		query.with(pable);
		return mongo.find(query, User.class);
	}

	
	
	@Override
	public void addMsg(Msg msg) {
		// TODO Auto-generated method stub
		mongo.insert(msg);
	}

	@Override
	public List<JSONObject> groupList() {
		//分组统计查询
		//select uid,count(1) num from msg where 1=1 group by uid limt 1
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.project("uid", "title"), 
			Aggregation.group("uid").count().as("num"),
			Aggregation.project("uid", "num")
			//Aggregation.limit(1)
			//Aggregation.sort(Sort.Direction.DESC, "num")
			//Aggregation.match(Criteria.where("aa").is(""))
		);
		AggregationResults<JSONObject> result =  mongo.aggregate(agg, "msg", JSONObject.class);
		return result.getMappedResults();
	}
	
}
