package in.succinct.id.core.controller;

import com.venky.swf.controller.ModelController;
import com.venky.swf.controller.annotations.SingleRecordAction;
import com.venky.swf.db.Database;
import com.venky.swf.db.annotations.column.ui.mimes.MimeType;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.collab.db.model.participants.Application;
import com.venky.swf.views.BytesView;
import com.venky.swf.views.ForwardedView;
import com.venky.swf.views.View;
import in.succinct.id.core.db.model.onboarding.company.ClaimRequest;
import in.succinct.id.core.db.model.onboarding.company.Company;
import in.succinct.id.core.db.model.user.User;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CompaniesController extends ModelController<Company> {
    public CompaniesController(Path path) {
        super(path);
    }

    @SingleRecordAction(  icon = "fa-plug" , tooltip = "Claim ownership")
    public View claim(long id){
        Company company = Database.getTable(Company.class).get(id);
        ClaimRequest request = company.claim();
        return new ForwardedView(getPath(),"/claim_requests/verifyDomain/" + request.getId());
    }

    public View generate_beckn_json(long id){
        User user = getSessionUser();
        Company company = Database.getTable(Company.class).get(id);
        List<Application> applications = company.getApplications();
        //
        //company.getRawRecord().getAsProxy(Company.class);
        //Prepare beckn.json
        JSONObject content = new JSONObject();
        JSONObject beckn_json = new JSONObject();
        beckn_json.put("certificate","");
        beckn_json.put("content",content);
        content.put("subscriber",new JSONObject());
        return new BytesView(getPath(),beckn_json.toString().getBytes(StandardCharsets.UTF_8), MimeType.APPLICATION_JSON,"content-disposition", "attachment; filename=beckn.json" );
    }
}
