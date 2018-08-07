package com.leonds.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeNode {
    private String id;
    private String name;
    private String url;
    private String path;
    private String component;
    private String permission;
    private int position;
    private String type;
    private String pid;
    private int childNum;
    private String icon;
    private List<TreeNode> children = new ArrayList<>();
}
