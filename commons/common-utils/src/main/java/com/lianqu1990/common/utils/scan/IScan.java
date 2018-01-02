//mpsite_resource_jcr
package com.lianqu1990.common.utils.scan;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * ClassName : IScan 
 * Use : TODO. 
 * date : 2014-8-26 下午5:19:54 
 *
 * @author Han
 * @version 
 */
public interface IScan {
	public Set<Class<?>> scan(String pack);
	public Set<Class<?>> scan(String pack, Class<? extends Annotation> annotation);
	public Set<Class<?>> scan(String pack, Class<? extends Annotation> annotation, Class<?> iface);
}
