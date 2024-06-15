package in.succinct.id.core.db.model.user;

import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.COLUMN_NAME;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.relationship.CONNECTED_VIA;
import com.venky.swf.db.annotations.column.ui.HIDDEN;
import com.venky.swf.routing.Config;
import in.succinct.plugins.kyc.db.model.DocumentedModel;
import in.succinct.plugins.kyc.db.model.submissions.SubmittedDocument;

import java.sql.Date;
import java.util.List;

public interface User extends com.venky.swf.plugins.collab.db.model.user.User , DocumentedModel {
    public static final String[] DEFAULT_DOCUMENTS = Config.instance().
            getProperty("id.user.kyc.documents", "PAN,GST,AADHAR").split(",");

    @IS_NULLABLE
    public Date getDateOfBirth();
    public void setDateOfBirth(Date dateOfBirth);

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isDateOfBirthVerified();
    public void setDateOfBirthVerified(boolean dateOfBirthVerified);

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isAddressVerified();
    public void setAddressVerified(boolean addressKycVerified);

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isPhoneNumberVerified();
    public void setPhoneNumberVerified(boolean phoneNumberKycVerified);

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isEmailVerified();
    public void setEmailVerified(boolean emailKycVerified);


    @IS_VIRTUAL(false)
    public String getFirstName();
    public void setFirstName(String name);

    @IS_VIRTUAL(false)
    public String getLastName();
    public void setLastName(String lastName);


    @CONNECTED_VIA("USER_ID")
    public List<SubmittedDocument> getSubmittedDocuments();
}
