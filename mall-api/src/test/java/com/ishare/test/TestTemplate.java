package com.ishare.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by YinLin on 2015/8/25.
 * Description :
 * Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mvc.xml","classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional
public class TestTemplate {

    public static final Logger log = LoggerFactory.getLogger(TestTemplate.class);
    @Autowired
    protected ApplicationContext context;

    public final int PAGE_SIZE = 5;

    public PageRequest pageRequest = new PageRequest(0, PAGE_SIZE, Sort.Direction.ASC, "id");
    @Test
    public void test() {
        Assert.assertTrue(false);
    }
}
