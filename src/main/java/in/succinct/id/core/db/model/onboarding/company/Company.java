package in.succinct.id.core.db.model.onboarding.company;

import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.ui.HIDDEN;
import com.venky.swf.routing.Config;
import in.succinct.plugins.kyc.db.model.DocumentedModel;

import java.util.List;

public interface Company extends com.venky.swf.plugins.collab.db.model.participants.admin.Company , DocumentedModel {

    public static final String[] DEFAULT_DOCUMENTS = Config.instance().
            getProperty("id.company.kyc.documents", "Company Registration,Company Tax Identifier").split(",");

    @UNIQUE_KEY("SUBSCRIBER")
    public String getSubscriberId();
    public void setSubscriberId(String subscriberId);


    @HIDDEN
    public List<ClaimRequest> getClaimRequests();

    public List<CompanyNetworkDomain> getCompanyNetworkDomains(); //Domains/
    public List<CompanyNetworkUsage> getCompanyNetworkUsages(); // Roles


    public ClaimRequest claim();
}
