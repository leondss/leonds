package com.leonds.blog.console.service.impl;

import com.leonds.blog.console.service.SettingsService;
import com.leonds.blog.domain.entity.Settings;
import com.leonds.blog.domain.enums.SettingsName;
import com.leonds.core.QueryParams;
import com.leonds.core.orm.Condition;
import com.leonds.core.orm.Filters;
import com.leonds.core.orm.Page;
import com.leonds.core.orm.PersistenceManager;
import com.leonds.core.utils.CheckUtils;
import com.leonds.core.utils.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Leon
 */
@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    private PersistenceManager pm;

    @Override
    @CacheEvict(value = "local", allEntries = true, beforeInvocation = true)
    public Settings save(Settings model) {
        CheckUtils.checkObject(model);
        Settings settingsOfName;
        if (StringUtils.isNotBlank(model.getId())) {
            settingsOfName = getByIdIsNotAndName(model.getId(), model.getName());
        } else {
            settingsOfName = getByName(model.getName());
        }
        CheckUtils.checkState(settingsOfName == null, MessageUtils.get("settings.name.exists"));

        Settings settingsOfCode;
        if (StringUtils.isNotBlank(model.getId())) {
            settingsOfCode = getByIdIsNotAndName(model.getId(), model.getCode());
        } else {
            settingsOfCode = getByName(model.getCode());
        }
        CheckUtils.checkState(settingsOfCode == null, MessageUtils.get("settings.name.exists"));
        return pm.save(model);
    }

    @Override
    @Cacheable(value = "local", key = "#settingsName.name()")
    public String getValue(SettingsName settingsName) {
        Settings settings = pm.findOne(Settings.class, Filters.and(Filters.eq("code", settingsName.name())));
        if (settings != null) {
            return settings.getValue();
        }
        return null;
    }

    @Override
    public Settings getByName(String name) {
        return pm.findOne(Settings.class, Filters.and(Filters.eq("name", name)));
    }

    @Override
    public Settings getByIdIsNotAndName(String id, String name) {
        return pm.findOne(Settings.class, Filters.and(Filters.eq("name", name), Filters.neq("id", id)));
    }

    @Override
    public Settings getByCode(String code) {
        return pm.findOne(Settings.class, Filters.and(Filters.eq("code", code)));
    }

    @Override
    public Settings getByIdIsNotAndCode(String id, String code) {
        return pm.findOne(Settings.class, Filters.and(Filters.eq("name", code), Filters.neq("id", id)));
    }

    @Override
    public Settings getById(String id) {
        return pm.get(Settings.class, id);
    }

    @Override
    @CacheEvict(value = "local", allEntries = true, beforeInvocation = true)
    public void remove(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            pm.remove(Settings.class, ids);
        }
    }

    @Override
    public Page<Settings> getPage(QueryParams queryParams) {
        Condition conditions = null;
        if (StringUtils.isNotBlank(queryParams.getQ())) {
            conditions = Filters.and(Filters.or(
                    Filters.like("name", queryParams.getQ()),
                    Filters.like("code", queryParams.getQ()),
                    Filters.like("value", queryParams.getQ())
            ));
        }
        return pm.findPage(Settings.class, queryParams.pageRequest(), conditions);
    }
}
