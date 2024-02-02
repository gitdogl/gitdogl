package weaver.interfaces.zkd.dn2023.oa.entity;



public class OAHumanMesEntity {
    /**
     * 名字
     */
    private String name;

    /**
     * 部门id
     */
    private String departmentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "HumanMesEntity{" +
                "name='" + name + '\'' +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}
