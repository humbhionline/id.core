package in.succinct.id.core.extensions;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.extensions.ModelOperationExtension;
import in.succinct.id.core.db.model.onboarding.company.Company;

import java.util.UUID;

public class CompanyExtension extends ModelOperationExtension<Company> {
    static {
        registerExtension(new CompanyExtension());
    }

    @Override
    protected void beforeValidate(Company company) {
        if (ObjectUtil.isVoid(company.getSubscriberId())){
            company.setSubscriberId(UUID.randomUUID().toString());
        }
    }


}
