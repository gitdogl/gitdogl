package weaver.interfaces.ht.oa.dto;

/**
 * 人员信息
 * 
 * @author Administrator
 * 
 */
public class SubcompanyBill {
	// 表id
	private int id;
	// 公司名称
	private String subcompanyname;
	// 公司编号
	private String subcompanycode;
	// sap外键
	private String outkey;

	/**
	 * 返回 表id
	 * 
	 * @return 表id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 设置 表id
	 * 
	 * @param id
	 *            表id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 返回 公司名称
	 * 
	 * @return 公司名称
	 */
	public String getSubcompanyname() {
		return subcompanyname;
	}

	/**
	 * 设置 公司名称
	 * 
	 * @param subcompanyname
	 *            公司名称
	 */
	public void setSubcompanyname(String subcompanyname) {
		this.subcompanyname = subcompanyname;
	}

	/**
	 * 返回 公司编号
	 * 
	 * @return 公司编号
	 */
	public String getSubcompanycode() {
		return subcompanycode;
	}

	/**
	 * 设置 公司编号
	 * 
	 * @param subcompanycode
	 *            公司编号
	 */
	public void setSubcompanycode(String subcompanycode) {
		this.subcompanycode = subcompanycode;
	}

	/**
	 * 返回 sap外键
	 * 
	 * @return sap外键
	 */
	public String getOutkey() {
		return outkey;
	}

	/**
	 * 设置 sap外键
	 * 
	 * @param outkey
	 *            sap外键
	 */
	public void setOutkey(String outkey) {
		this.outkey = outkey;
	}

}
