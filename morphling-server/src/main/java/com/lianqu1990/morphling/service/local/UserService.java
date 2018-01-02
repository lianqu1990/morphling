package com.lianqu1990.morphling.service.local;

import com.google.common.collect.Sets;
import com.lianqu1990.common.bean.page.Pager;
import com.lianqu1990.common.consts.DataState;
import com.lianqu1990.morphling.common.bean.UserFilter;
import com.lianqu1990.morphling.dao.jpa.api.RoleDao;
import com.lianqu1990.morphling.dao.jpa.api.UserDao;
import com.lianqu1990.morphling.dao.jpa.api.UserRoleDao;
import com.lianqu1990.morphling.dao.jpa.entity.Role;
import com.lianqu1990.morphling.dao.jpa.entity.User;
import com.lianqu1990.morphling.dao.jpa.entity.UserRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/11/8 10:22
 */
@Service
public class UserService extends BaseService<User,Integer> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Override
    public JpaRepository<User, Integer> getDefaultDao() {
        return userDao;
    }

    public void loginSuccess(int id,String ip){
        User user = userDao.getOne(id);
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(ip);
        userDao.save(user);
    }

    public void setRole(List<Integer> roleIds,int userId){
        Set<Integer> oldRoles = roleDao.findByUserId(userId).stream().map(Role::getId).collect(Collectors.toSet());
        Set<Integer> newRoles = Sets.newHashSet(roleIds);
        Set<Integer> toDelete = Sets.difference(oldRoles,newRoles);
        Set<Integer> toAdd = Sets.difference(newRoles,oldRoles);
        for (Integer roleId : toAdd) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(new Date());
            userRole.setState(DataState.EFFECTIVE);
            userRoleDao.save(userRole);
        }
        if(CollectionUtils.isNotEmpty(toDelete)){
            userRoleDao.deleteLogic(userId, toDelete);
        }
    }


    @Override
    public void delete(Integer id) {
        userDao.deleteLogic(id);
    }

    public Pager<User> findByFilter(UserFilter filter, Pager pager){
        Page<User> page = userDao.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(StringUtils.isNotBlank(filter.getUsername())){
                    predicate = cb.and(predicate,cb.equal(root.get("username"),filter.getUsername()));
                }
                if(StringUtils.isNotBlank(filter.getName())){
                    predicate = cb.and(predicate,cb.equal(root.get("name"),filter.getName()));
                }
                return query.where(predicate).getRestriction();
            }
        },new PageRequest(pager.getCurrentPage()-1, pager.getOnePageSize(),new Sort(Sort.Direction.DESC,"createTime")));
        pager.setTotalResults(page.getTotalElements());
        pager.setData(page.getContent());
        return pager;
    }
}
