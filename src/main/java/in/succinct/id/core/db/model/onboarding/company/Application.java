package in.succinct.id.core.db.model.onboarding.company;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.plugins.collab.db.model.participants.ApplicationContext;

import java.util.List;

public interface Application extends com.venky.swf.plugins.collab.db.model.participants.Application {

    @Override
    @IS_NULLABLE(false)
    Long getCompanyId();




}
