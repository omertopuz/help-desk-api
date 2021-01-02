package org.helpdesk.category.config;

import org.helpdesk.category.model.document.Category;
import org.helpdesk.category.model.document.HierarchicalCategory;
import org.helpdesk.category.util.HierarchicalCategorySearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

@Configuration
public class HierarchicalCategoryEventListener extends AbstractMongoEventListener<HierarchicalCategory> {

    @Autowired
    private AuditorAware<String> auditorAware;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<HierarchicalCategory> event) {
        HierarchicalCategorySearchUtil.setCommonProperties(event.getSource(),auditorAware.getCurrentAuditor());
    }

}
