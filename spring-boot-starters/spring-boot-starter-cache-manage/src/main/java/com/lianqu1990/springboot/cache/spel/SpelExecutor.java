package com.lianqu1990.springboot.cache.spel;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author hanchao
 * @date 2017/10/7 17:51
 */
public class SpelExecutor implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private SpelExpressionParser parser = new SpelExpressionParser();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 解析spel表达式
     * @param spel
     * @param args
     * @return
     */
    public Object execute(String spel,Map<String,Object> args){
        Expression expression = parser.parseExpression(spel);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        for (String variable : args.keySet()) {
            context.setVariable(variable,args.get(variable));
        }
        return expression.getValue(context);
    }

}
