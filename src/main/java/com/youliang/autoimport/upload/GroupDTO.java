package com.youliang.autoimport.upload;

import com.bbd.finance.atom.admin.domain.dto.RoleDTO;
import java.util.List;

public class GroupDTO extends  GroupInfoDTO{
    /** 下级组织 */
    private List<com.bbd.finance.atom.admin.domain.dto.GroupDTO> children;

    /** 角色 */
    private List<RoleDTO>  roles;

    public List<com.bbd.finance.atom.admin.domain.dto.GroupDTO> getChildren() {
        return children;
    }

    public void setChildren(List<com.bbd.finance.atom.admin.domain.dto.GroupDTO> children) {
        this.children = children;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
