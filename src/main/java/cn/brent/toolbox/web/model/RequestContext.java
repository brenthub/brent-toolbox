package cn.brent.toolbox.web.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import cn.brent.toolbox.util.BeanUtil;
import cn.brent.toolbox.web.util.RequestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class RequestContext {

	private final static ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();

	private final static String ENCODE = "UTF-8";

	private static String webroot = null;

	static {
		webroot = getWebrootPath();
	}

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Cookie> cookies;
	private long beginTime = 0L;

	/**
	 * 返回接收到该请求的时间
	 * 
	 * @return
	 */
	public long getBeginTime() {
		return this.beginTime;
	}

	private final static String getWebrootPath() {
		String root = RequestContext.class.getResource("/").getFile();
		try {
			if (root.endsWith(".svn/"))
				root = new File(root).getParentFile().getParentFile().getParentFile().getCanonicalPath();
			else
				root = new File(root).getParentFile().getParentFile().getCanonicalPath();
			root += File.separator;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return root;
	}

	/**
	 * 初始化请求上下文
	 * 
	 * @param ctx
	 * @param req
	 * @param res
	 */
	public static RequestContext begin( HttpServletRequest req, HttpServletResponse res) {
		RequestContext rc = new RequestContext();
		rc.request = req;
		rc.response = res;
		rc.response.setCharacterEncoding(ENCODE);
		rc.cookies = new HashMap<String, Cookie>();
		rc.beginTime = System.currentTimeMillis();
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				rc.cookies.put(ck.getName(), ck);
			}
		}
		contexts.set(rc);
		return rc;
	}

	/**
	 * 返回Web应用的路径
	 * 
	 * @return
	 */
	public static String root() {
		return webroot;
	}

	public static String getContextPath() {
		RequestContext ctx = RequestContext.get();
		return (ctx != null) ? ctx.contextPath() : "";
	}

	/**
	 * 获取当前请求的上下文
	 * 
	 * @return
	 */
	public static RequestContext get() {
		return contexts.get();
	}

	public static void end() {
		RequestContext rc=get();
		if(rc==null){
			return;
		}
		rc.request = null;
		rc.response = null;
		rc.cookies = null;
		contexts.remove();
	}

	public Locale locale() {
		return request.getLocale();
	}

	public long id() {
		return param("id", 0L);
	}

	public String ip() {
		String ip = RequestUtils.getRemoteAddr(request);
		if (ip == null)
			ip = "127.0.0.1";
		return ip;
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> params() {
		return request.getParameterNames();
	}

	public String param(String name, String... def_value) {
		String v = request.getParameter(name);
		return (v != null) ? v : ((def_value.length > 0) ? def_value[0] : null);
	}

	public long param(String name, long def_value) {
		return NumberUtils.toLong(param(name), def_value);
	}

	public int param(String name, int def_value) {
		return NumberUtils.toInt(param(name), def_value);
	}

	public byte param(String name, byte def_value) {
		return (byte) NumberUtils.toInt(param(name), def_value);
	}

	public double param(String name, double def_value) {
		try {
			return Double.parseDouble(param(name));
		} catch (Exception e) {
			return def_value;
		}

	}

	public String[] params(String name) {
		return request.getParameterValues(name);
	}

	public Object param(String name, Class<?> clz) {
		Object r = null;
		if (List.class.isAssignableFrom(clz)) {
			String[] values = request.getParameterValues(name);
			List<?> list = JSONArray.parseArray(JSON.toJSONString(values), clz.getComponentType());
			return list;
		} else {
			String v = request.getParameter(name);
			r = JSON.parseObject(JSON.toJSONString(v), clz);
			if (r == null) {
				r = 0;
			}
		}
		return r;
	}

	public String uri() {
		return request.getRequestURI();
	}

	public String contextPath() {
		return request.getContextPath();
	}

	public void redirect(String uri) throws IOException {
		response.sendRedirect(uri);
	}

	public void forward(String uri) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

	public void include(String uri) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.include(request, response);
	}

	public boolean isRobot() {
		return RequestUtils.isRobot(request);
	}

	/**
	 * 输出信息到浏览器
	 * 
	 * @param msg
	 * @throws IOException
	 */
	public void print(Object msg) throws IOException {
		if (!ENCODE.equalsIgnoreCase(response.getCharacterEncoding()))
			response.setCharacterEncoding(ENCODE);
		response.getWriter().print(msg);
	}

	public void error(int code, String... msg) throws IOException {
		if (msg.length > 0)
			response.sendError(code, msg[0]);
		else
			response.sendError(code);
	}

	public void forbidden() throws IOException {
		error(HttpServletResponse.SC_FORBIDDEN);
	}

	public void not_found() throws IOException {
		error(HttpServletResponse.SC_NOT_FOUND);
	}

	public HttpSession session() {
		return request.getSession(false);
	}

	public HttpSession session(boolean create) {
		return request.getSession(true);
	}

	public Object sessionAttr(String attr) {
		HttpSession ssn = session();
		return (ssn != null) ? ssn.getAttribute(attr) : null;
	}

	public HttpServletRequest request() {
		return request;
	}

	public HttpServletResponse response() {
		return response;
	}

	public Cookie cookie(String name) {
		return cookies.get(name);
	}

	public void cookie(String name, String value, int max_age, boolean all_sub_domain) {
		RequestUtils.setCookie(request, response, name, value, max_age, all_sub_domain);
	}

	public void deleteCookie(String name, boolean all_domain) {
		RequestUtils.deleteCookie(request, response, name, all_domain);
	}

	public String header(String name) {
		return request.getHeader(name);
	}

	public void header(String name, String value) {
		response.setHeader(name, value);
	}

	public void header(String name, int value) {
		response.setIntHeader(name, value);
	}

	public void header(String name, long value) {
		response.setDateHeader(name, value);
	}

	/**
	 * 设置public缓存，设置了此类型缓存要求此页面对任何人访问都是同样数据
	 * 
	 * @param minutes
	 *            分钟
	 * @return
	 */
	public void setPublicCache(int minutes) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			int seconds = minutes * 60;
			header("Cache-Control", "max-age=" + seconds);
			Calendar cal = Calendar.getInstance(request.getLocale());
			cal.add(Calendar.MINUTE, minutes);
			header("Expires", cal.getTimeInMillis());
		}
	}

	/**
	 * 设置私有缓存
	 * 
	 * @param minutes
	 * @return
	 */
	public void setPrivateCache(int minutes) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			header("Cache-Control", "private");
			Calendar cal = Calendar.getInstance(request.getLocale());
			cal.add(Calendar.MINUTE, minutes);
			header("Expires", cal.getTimeInMillis());
		}
	}

	/**
	 * 关闭缓存
	 */
	public void closeCache() {
		header("Pragma", "must-revalidate, no-cache, private");
		header("Cache-Control", "no-cache");
		header("Expires", "Sun, 1 Jan 2000 01:00:00 GMT");
	}

	/**
	 * 将HTTP请求参数映射到bean对象中
	 * 
	 * @param req
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T form(Class<T> beanClass) {
		Map<String, String[]> map = null;
		HashMap<String, Field> fields = BeanUtil.getClassFields(beanClass);
		try {
			T bean = beanClass.newInstance();
			map = request.getParameterMap();
			Map<String, Object> primitiveMap = new HashMap<String, Object>();

			for (Field f : fields.values()) {
				String key = f.getName();
				if (map.containsKey(key)) {
					primitiveMap.put(key, map.get(key));
				}
			}
			BeanUtils.populate(bean, primitiveMap);
			return bean;
		} catch (Throwable e) {
			try {
				Map<String, Object> primitiveMap = new HashMap<String, Object>();
				T bean = beanClass.newInstance();
				for (Field f : fields.values()) {
					String key = f.getName();
					// 属性为数组或者集合的属性单独处理
					if (map.containsKey(key)) {
						// 处理传递过来的List对象(传递的值为json 字符串)
						if (Collection.class.isAssignableFrom(f.getType())) {
							ParameterizedType pt = (ParameterizedType) f.getGenericType();
							Class type = (Class) pt.getActualTypeArguments()[0];
							String[] value_list = map.get(key);
							if (value_list != null && value_list.length > 0) {
								String value = value_list[0];
								if (StringUtils.isNotEmpty(value)) {
									List list = JSONArray.parseArray(value, type);
									primitiveMap.put(key, list);
								}
							}
						} else if (f.getType().isArray()) {
							Class type = (Class) f.getType().getComponentType();
							String[] value_list = map.get(key);
							if (value_list != null && value_list.length > 0) {
								String value = value_list[0];
								if (StringUtils.isNotEmpty(value)) {
									List list = JSONArray.parseArray(value, type);
									primitiveMap.put(key, list.toArray());
								}
							}

						}

					}
				}

				if (!primitiveMap.isEmpty()) {
					BeanUtils.populate(bean, primitiveMap);
				}
				return bean;
			} catch (Exception ee) {
				throw new RuntimeException(e.getMessage());
			}

		}
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 */
	public Object attribute(String key) {
		return request.getAttribute(key);
	}
}
