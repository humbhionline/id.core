package in.succinct.id.core.extensions;

import in.succinct.id.core.db.model.onboarding.company.Company;
import in.succinct.id.core.db.model.user.User;
import in.succinct.plugins.kyc.util.DocumentedModelRegistry;

public class DocumentedModelRegistrar {
    static {
        DocumentedModelRegistry.getInstance().register(Company.class);
        DocumentedModelRegistry.getInstance().register(User.class);
    }
}
