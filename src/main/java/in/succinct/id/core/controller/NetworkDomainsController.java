package in.succinct.id.core.controller;

import com.venky.swf.path.Path;
import com.venky.swf.plugins.collab.controller.OpenSearchController;
import in.succinct.id.core.db.model.onboarding.company.NetworkDomain;

public class NetworkDomainsController extends OpenSearchController<NetworkDomain> {
    public NetworkDomainsController(Path path) {
        super(path);
    }


}
