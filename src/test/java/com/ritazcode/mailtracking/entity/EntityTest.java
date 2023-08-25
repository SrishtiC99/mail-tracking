package com.ritazcode.mailtracking.entity;

import com.ritazcode.mailtracking.service.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class EntityTest extends BaseTest {
    @Test
    @DisplayName("should test all entities getters and setters")
    void testAllEntities(){
        BeanTester bean = new BeanTester();
        bean.testBean(HistoryItem.class);
        bean.testBean(PostalItem.class);
        bean.testBean(PostOffice.class);
    }
}
