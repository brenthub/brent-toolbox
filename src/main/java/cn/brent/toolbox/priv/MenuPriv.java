package cn.brent.toolbox.priv;

import java.util.HashMap;
import java.util.Map;

public abstract class MenuPriv {

	private Map<String, String> privs = new HashMap<String, String>();

	private String menuId;
	private String memo;

	protected void addItem(String priv, String name) {
		privs.put(priv, name);
	}

	protected MenuPriv(String menuId, String memo) {
		this.menuId = menuId;
		this.memo = memo;
	}

	public String getMenuId() {
		return menuId;
	}

	public Map<String, String> getPrivs() {
		return privs;
	}

	public String getMemo() {
		return memo;
	}

}
