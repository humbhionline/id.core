package in.succinct.id.core.extensions;

import com.venky.swf.plugins.collab.extensions.participation.CompanySpecificParticipantExtension;
import in.succinct.id.core.db.model.onboarding.company.CompanyNetworkDomain;

public class CompanyNetworkDomainParticipantExtension extends CompanySpecificParticipantExtension<CompanyNetworkDomain> {
    static {
        registerExtension(new CompanyNetworkDomainParticipantExtension());
    }
}
