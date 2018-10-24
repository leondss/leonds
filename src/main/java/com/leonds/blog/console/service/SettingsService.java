package com.leonds.blog.console.service;


import com.leonds.blog.domain.entity.Settings;
import com.leonds.blog.domain.enums.SettingsName;
import com.leonds.core.QueryParams;
import com.leonds.core.orm.Page;

import java.util.List;

/**
 * @author Leon
 */
public interface SettingsService {
    Settings save(Settings model);

    String getValue(SettingsName settingsName);

    Settings getByName(String name);

    Settings getByIdIsNotAndName(String id, String name);

    Settings getByCode(String code);

    Settings getByIdIsNotAndCode(String id, String code);

    Settings getById(String id);

    void remove(List<String> ids);

    Page<Settings> getPage(QueryParams queryParams);

}
