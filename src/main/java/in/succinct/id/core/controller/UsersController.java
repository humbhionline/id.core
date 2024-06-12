package in.succinct.id.core.controller;

import com.venky.swf.controller.annotations.SingleRecordAction;
import com.venky.swf.db.Database;
import com.venky.swf.db.model.Model;
import com.venky.swf.path.Path;
import com.venky.swf.routing.Config;
import com.venky.swf.routing.KeyCase;
import com.venky.swf.views.View;
import in.succinct.id.core.db.model.onboarding.company.Company;
import in.succinct.id.core.db.model.onboarding.company.CompanyNetworkDomain;
import in.succinct.id.core.db.model.onboarding.company.CompanyNetworkUsage;
import in.succinct.id.core.db.model.onboarding.company.NetworkDomain;
import in.succinct.id.core.db.model.onboarding.company.NetworkUsage;
import in.succinct.id.core.db.model.user.User;
import in.succinct.plugins.kyc.db.model.submissions.Document;
import in.succinct.plugins.kyc.db.model.submissions.KycGroup;
import in.succinct.plugins.kyc.db.model.submissions.SubmittedDocument;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UsersController extends com.venky.swf.plugins.collab.controller.UsersController {

    public UsersController(Path path) {
        super(path);
    }



    @SingleRecordAction(icon = "fas fa-address-card" , tooltip = "Verify Address")
    public View verifyAddress(long id){
        User user = Database.getTable(User.class).get(id);
        user.setTxnProperty("being.verified",true);
        user.setAddressVerified(true);
        user.save();
        return show(user);
    }


    @Override
    protected Map<Class<? extends Model>, List<String>> getIncludedModelFields() {
        Map<Class<? extends Model>,List<String>> map = super.getIncludedModelFields();
        if( getReturnIntegrationAdaptor() == null ){
            return map ;
        }


        addToIncludedModelFieldsMap(map, SubmittedDocument.class,Arrays.asList("ID","FILE","COMPANY_ID","USER_ID"));
        addToIncludedModelFieldsMap(map, Document.class,Collections.singletonList("ID"));
        addToIncludedModelFieldsMap(map, CompanyNetworkDomain.class,Arrays.asList("ID","COMPANY_ID"));
        addToIncludedModelFieldsMap(map, NetworkDomain.class,Collections.singletonList("ID"));
        addToIncludedModelFieldsMap(map, CompanyNetworkUsage.class,Arrays.asList("ID","COMPANY_ID"));
        addToIncludedModelFieldsMap(map, NetworkUsage.class,Collections.singletonList("ID"));
        addToIncludedModelFieldsMap(map, KycGroup.class,Collections.singletonList("ID"));
        return map;
    }

    @Override
    protected Map<Class<? extends Model>, List<Class<? extends Model>>> getConsideredChildModels() {
        Map<Class<? extends Model>, List<Class<? extends Model>>> m = super.getConsideredChildModels();

        m.get(User.class).add(SubmittedDocument.class);
        m.get(Company.class).add(SubmittedDocument.class);
        //m.get(Application.class).addAll(Arrays.asList(ApplicationPublicKey.class, WhiteListIp.class, EndPoint.class));
        //m.get(EndPoint.class).add(EventHandler.class);
        return m;
    }


}
