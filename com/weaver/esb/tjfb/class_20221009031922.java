package com.weaver.esb.tjfb;

import weaver.conn.RecordSet;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description todo
 * @Author wudi
 * @Date 2022/10/9 15:25
 **/
public class class_20221009031922 {
    /**
     * @param: param(Map collections)
     * 参数名称不能包含特殊字符+,.[]!"#$%&'()*:;<=>?@\^`{}|~/ 中文字符、标点 U+007F U+0000到U+001F
     */

    // 后面使用枚举类
    private static final String benbu = "1328961595511754754";
    private static final String gongsi = "1328698494992338946";
    private static final String bankuai = "1510067188933423105";
    private static final String bumen = "1328698495545987074";
    private static final String xiangmu = "1510067318877155329";
    private static final String bankuai_gongsi = "1427870989383254018";


    public Map execute(Map<String, Object> params) {
        /**
         *
         遍历所有记录
         判断当前orgCategory == 部门/项目orgCategory，是的话继续
         根据parentOrgId向上找，直到orgCategory == 本部/公司/板块，此时这个组织就是部门所属分部
         那么把orgId赋值给subCompanyId字段
         */

        // key为orgId，value为DeptDto
        HashMap<String, DeptDto> deptDtoMap = new HashMap<>();

        RecordSet rs = new RecordSet();
        String sql = "SELECT orgId,orgCategory,parentOrgId FROM zz";
        rs.execute(sql);

        while (rs.next()) {
            String orgId = rs.getString(1);
            String orgCategory = rs.getString(2);
            String parentOrgId = rs.getString(3);

            DeptDto dept = new DeptDto(orgId, orgCategory, parentOrgId);
            deptDtoMap.put(orgId, dept);
        }

        processAllDept(deptDtoMap);

        HashMap<String, Object> map = new HashMap<>();
        map.put("code",1);
        map.put("info",deptDtoMap);
        return map;
    }

    public void processAllDept(HashMap<String, DeptDto> deptDtoMap) {
        for (Map.Entry<String, DeptDto> entry : deptDtoMap.entrySet()) {
            DeptDto deptDto = entry.getValue();

            String orgCategory = deptDto.getOrgCategory();

            // 判断当前orgCategory == 部门/项目orgCategory，是的话继续
            if (bumen.equals(orgCategory) || xiangmu.equals(orgCategory)) {
                String subCompany = getSubCompany(deptDto, deptDtoMap);
                if (subCompany == null || subCompany.equals("")) {
                    continue;
                }

                // 找到后把orgId赋值给subCompanyId字段
                setSubCompany(subCompany, deptDto.getOrgId());
            }
        }
    }

    // 获取所属分部id
    public String getSubCompany(DeptDto dept, HashMap<String, DeptDto> deptDtoMap) {
        String parentOrgId = dept.getParentOrgId();
        DeptDto parentDeptDto = deptDtoMap.get(parentOrgId);

        // 只要上级不是分部，那么继续往上找
        while (parentDeptDto != null && !orgCategoryIsSubCompany(parentDeptDto.getOrgCategory())) {
            parentOrgId = parentDeptDto.getParentOrgId();
            parentDeptDto = deptDtoMap.get(parentOrgId);
        }

        return parentDeptDto == null ? null : parentDeptDto.getOrgId();
    }

    // 判断节点的orgCategory是否是分部orgCategory
    public Boolean orgCategoryIsSubCompany(String orgCategory) {
        return benbu.equals(orgCategory) || gongsi.equals(orgCategory) || bankuai.equals(orgCategory)
                || bankuai_gongsi.equals(orgCategory);
    }

    // 给某个部门赋值所属分部
    public void setSubCompany(String subCompany, String orgId) {
        String sql = "UPDATE zz SET subCompanyId = '" + subCompany + "' WHERE orgId = '" + orgId + "' ";
        RecordSet rs = new RecordSet();
        rs.execute(sql);
    }
}

/**
 * @Description DeptDto封装类
 * @Author wudi
 * @Date 2022/10/9 15:55
 **/
class DeptDto {
    public DeptDto() {
    }

    public DeptDto(String orgId, String orgCategory, String parentOrgId) {
        this.orgId = orgId;
        this.orgCategory = orgCategory;
        this.parentOrgId = parentOrgId;
    }

    public String orgId;
    public String orgCategory;
    public String parentOrgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCategory() {
        return orgCategory;
    }

    public void setOrgCategory(String orgCategory) {
        this.orgCategory = orgCategory;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }
}
