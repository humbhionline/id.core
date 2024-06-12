package in.succinct.id.core.extensions;

import com.venky.swf.plugins.collab.extensions.participation.CompanySpecificParticipantExtension;
import in.succinct.id.core.db.model.onboarding.company.CompanyNetworkUsage;

public class CompanyNetworkUsageParticipantExtension extends CompanySpecificParticipantExtension<CompanyNetworkUsage> {
    static {
        registerExtension(new CompanyNetworkUsageParticipantExtension());
    }
}
