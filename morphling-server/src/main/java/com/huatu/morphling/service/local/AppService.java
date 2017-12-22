package com.huatu.morphling.service.local;

import com.huatu.common.bean.page.Pager;
import com.huatu.morphling.common.bean.AppFilter;
import com.huatu.morphling.common.bean.AppInstanceVO;
import com.huatu.morphling.common.bean.UserAppVO;
import com.huatu.morphling.dao.jpa.api.AppDao;
import com.huatu.morphling.dao.jpa.api.ClientDao;
import com.huatu.morphling.dao.jpa.entity.App;
import com.huatu.morphling.dao.jpa.entity.Client;
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
import java.util.List;

/**
 * @author hanchao
 * @date 2017/12/4 17:27
 */
@Service
public class AppService extends BaseService<App,Integer> {
    @Autowired
    private AppDao appDao;
    @Autowired
    private ClientDao clientDao;
    @Override
    public JpaRepository<App, Integer> getDefaultDao() {
        return appDao;
    }

    public List<App> findByEnv(String env){
        return appDao.findByEnv(env);
    }

    public List<UserAppVO> findByUser(int userId){
        return appDao.findUserApps(userId);
    }

    public List<App> findUserApps(int userId,String env){
        return appDao.findUserApps(userId,env);
    }

    public Pager<App> findExcludeByFilter(int userId, AppFilter filter, Pager pager){
        return appDao.findExcludeByFilter(userId,filter,pager);
    }

    public AppInstanceVO findInstanceByInstanceId(int id){
        return appDao.findByInstanceId(id);
    }

    public List<AppInstanceVO> findInstances(int appId){
        return appDao.findInstances(appId);
    }

    public List<AppInstanceVO> findInstances(List<Integer> instanceIds){
        return appDao.findInstances(instanceIds);
    }

    public List<Client> findClients(int appId,String env){
        return clientDao.findExcludeByApp(appId,env);
    }

    public Pager<App> findByFilter(AppFilter filter, Pager pager){
        Page<App> page = appDao.findAll(new Specification<App>() {
            @Override
            public Predicate toPredicate(Root<App> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(StringUtils.isNotBlank(filter.getName())){
                    predicate = cb.and(predicate,cb.equal(root.get("name"),filter.getName()));
                }
                if(StringUtils.isNotBlank(filter.getEnv())){
                    predicate = cb.and(predicate,cb.equal(root.get("env"),filter.getEnv()));
                }
                return query.where(predicate).getRestriction();
            }
        },new PageRequest(pager.getCurrentPage()-1, pager.getOnePageSize(),new Sort(Sort.Direction.DESC,"createTime")));
        pager.setTotalResults(page.getTotalElements());
        pager.setData(page.getContent());
        return pager;
    }
}
