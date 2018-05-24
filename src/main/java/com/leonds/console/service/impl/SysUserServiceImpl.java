package com.leonds.console.service.impl;

import com.leonds.console.repository.SysUserRepository;
import com.leonds.console.service.SysRoleService;
import com.leonds.console.service.SysUserService;
import com.leonds.core.CheckUtils;
import com.leonds.core.MessageUtils;
import com.leonds.domain.entity.SysUser;
import com.leonds.domain.enums.SysUserStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leon
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public SysUser save(SysUser sysUser, List<String> roleIds) {
        CheckUtils.checkObject(sysUser);
        SysUser oldUser;
        if (StringUtils.isBlank(sysUser.getId())) {
            oldUser = sysUserRepository.findByUsername(sysUser.getUsername());
        } else {
            oldUser = sysUserRepository.findByIdIsNotAndUsername(sysUser.getId(), sysUser.getUsername());
        }
        CheckUtils.checkState(oldUser == null, MessageUtils.get("sysUser.username.exists"));

        SysUser newUser = new SysUser();
        if (StringUtils.isBlank(sysUser.getId())) {
            BeanUtils.copyProperties(sysUser, newUser);
            newUser.setStatus(SysUserStatus.ON.name());
        } else {
            SysUser user = getById(sysUser.getId());
            BeanUtils.copyProperties(user, newUser);

            /*if (StringUtils.isNotBlank(sysUser.getUsername())) {
                newUser.setUsername(sysUser.getUsername());
            }
            if (StringUtils.isNotBlank(sysUser.getPassword())) {
                newUser.setPassword(sysUser.getPassword());
            }*/
        }
        SysUser result = sysUserRepository.save(newUser);
        if (roleIds != null) {
            sysRoleService.saveUserRole(result.getId(), roleIds);
        }
        return result;
    }

    @Override
    public SysUser getById(String id) {
        return sysUserRepository.findById(id).orElse(null);
    }

    @Override
    public Page<SysUser> getPage(PageRequest pageRequest, String text, String status) {
        //规格定义
        Specification<SysUser> specification = (Specification<SysUser>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(text)) {
                Predicate likeNickName = cb.like(root.get("nickName").as(String.class), "%" + text + "%");
                predicates.add(likeNickName);
            }
            if (StringUtils.isNotBlank(status)) {
                Predicate predicate = cb.equal(root.get("status").as(String.class), status);
                predicates.add(predicate);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        pageRequest.getSort().and(Sort.by(Sort.Order.desc("creationTime")));
        return sysUserRepository.findAll(specification, pageRequest);
    }

    @Override
    public void remove(List<String> ids) {
        ids.forEach(id -> {
            sysUserRepository.deleteById(id);
            sysRoleService.removeUserRole(id);
        });
    }

    @Override
    public void enable(List<String> ids) {
        updateStatus(ids, SysUserStatus.OFF, SysUserStatus.ON);
    }

    private void updateStatus(List<String> ids, SysUserStatus old, SysUserStatus current) {
        ids.forEach(id -> {
            SysUser sysUser = getById(id);
            if (sysUser != null && sysUser.getStatus().equals(old.name())) {
                sysUser.setStatus(current.name());
                sysUserRepository.save(sysUser);
            }
        });
    }

    @Override
    public void disable(List<String> ids) {
        updateStatus(ids, SysUserStatus.ON, SysUserStatus.OFF);
    }

    @Override
    public void resetPassword(SysUser sysUser) {
        CheckUtils.checkNotBlank(sysUser.getPassword(), MessageUtils.get("sysUser.password.notBlank"));

        SysUser newUser = getById(sysUser.getId());
        CheckUtils.checkNotNull(newUser, MessageUtils.get("sysUser.not.exists"));

        newUser.setPassword(sysUser.getPassword());
        sysUserRepository.save(newUser);
    }
}
