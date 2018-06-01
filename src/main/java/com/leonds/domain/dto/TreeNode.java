package com.leonds.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leon
 */
@Data
public class TreeNode {
    private String id;
    private Boolean isLeaf;
    private List<TreeNode> children = new ArrayList<>();
    private Boolean disabled;
    private String name;
    private String link;
    private String permission;
    private Integer position;
    private String type;
    private String pid;
}
