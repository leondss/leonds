package com.leonds.repository;

import com.leonds.LeondsApplicationTests;
import com.leonds.console.repository.SysUserRepository;
import com.leonds.domain.entity.SysUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Leon
 */
public class SysUserRepositoryTest extends LeondsApplicationTests {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Test
    public void testAdd() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("liuqq");
        sysUser.setPassword("hahha");
        sysUserRepository.save(sysUser);
    }

}