package in.succinct.id.core.db.model.user;

import com.venky.swf.db.table.ModelImpl;
import in.succinct.plugins.kyc.db.model.DocumentedModelProxy;
import in.succinct.plugins.kyc.db.model.submissions.SubmittedDocument;

import java.util.List;

public class UserImpl extends ModelImpl<User> {
    public UserImpl(User user){
        super(user);
        documentedModelProxy = new DocumentedModelProxy<>(user);
    }
    DocumentedModelProxy<User> documentedModelProxy ;

    public List<SubmittedDocument> getSubmittedDocuments(){
        return documentedModelProxy.getSubmittedDocuments();
    }

}
