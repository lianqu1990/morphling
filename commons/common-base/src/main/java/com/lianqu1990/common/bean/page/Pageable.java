package com.lianqu1990.common.bean.page;

import java.io.Serializable;
import java.util.List;

/**
 * @author hanchao
 * @date 2017/9/6 13:54
 */
public interface Pageable<T> extends Serializable {

    long getCursor();

    int getTotal();

    List<T> getResutls();

}
