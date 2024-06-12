package in.succinct.id.core.db.model.docs;

import com.venky.swf.db.annotations.column.pm.PARTICIPANT;
import com.venky.swf.plugins.collab.db.model.participants.admin.Company;
import com.venky.swf.plugins.collab.db.model.user.User;

public interface SubmittedDocument extends in.succinct.plugins.kyc.db.model.submissions.SubmittedDocument{
    @PARTICIPANT
    public Long getDocumentedModelId();

    @PARTICIPANT("COMPANY")
    public Long getCompanyId();
    public void setCompanyId(Long id);
    public Company getCompany();

    @PARTICIPANT("USER")
    public Long getUserId();
    public void setUserId(Long id);
    public User getUser();
}
